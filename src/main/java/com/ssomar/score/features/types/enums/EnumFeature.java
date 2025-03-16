package com.ssomar.score.features.types.enums;

import com.ssomar.score.editor.NewGUIManager;
import com.ssomar.score.features.*;
import com.ssomar.score.languages.messages.TM;
import com.ssomar.score.languages.messages.Text;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.strings.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

@Getter
@Setter
public class EnumFeature<T extends Enum> extends FeatureAbstract<Optional<T>, EnumFeature> implements FeatureRequireOnlyClicksInEditor {

    private Class<T> enumClass;
    private T fallBackValue;
    private String enumName;
    private List<T> enumValues;
    private Optional<T> value;
    private Optional<T> defaultValue;

    public EnumFeature(FeatureParentInterface parent, Optional<T> defaultValue, FeatureSettingsInterface settings, Class<T> enumClass, T fallBackValue, String enumName, List<T> enumValues) {
        super(parent, settings);
        this.defaultValue = defaultValue;
        this.value = Optional.empty();
        this.enumClass = enumClass;
        this.fallBackValue = fallBackValue;
        this.enumName = enumName;
        this.enumValues = enumValues;
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        String colorStr = config.getString(this.getName(), "NULL").toUpperCase();
        if (colorStr.equals("NULL")) {
            if (defaultValue.isPresent()) {
                value = defaultValue;
            } else {
                errors.add("&cERROR, Couldn't load the "+enumName+" value of " + this.getName() + " from config, value: " + colorStr + " &7&o" + getParent().getParentInfo() + " &6>> "+enumValues);
                value = Optional.empty();
            }
            return errors;
        }
        try {
            T attributeSlot = (T) T.valueOf(enumClass, colorStr);
            value = Optional.ofNullable(attributeSlot);
            FeatureReturnCheckPremium<T> checkPremium = checkPremium(enumName, attributeSlot, defaultValue, isPremiumLoading);
            if (checkPremium.isHasError()) value = Optional.of(checkPremium.getNewValue());
        } catch (Exception e) {
            errors.add("&cERROR, Couldn't load the "+enumName+" value of " + this.getName() + " from config, value: " + colorStr + " &7&o" + getParent().getParentInfo() + " &6>> "+enumValues);
            value = Optional.empty();
        }
        return errors;
    }

    @Override
    public void save(ConfigurationSection config) {
        Optional<T> value = getValue();
        value.ifPresent(type -> config.set(this.getName(), type.name()));
    }

    @Override
    public Optional<T> getValue() {
        if (value.isPresent()) return value;
        else return defaultValue;
    }

    @Override
    public EnumFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = gui.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = TM.g(Text.EDITOR_CURRENTLY_NAME);

        gui.createItem(getEditorMaterial(), 1, slot, gui.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {
        Optional<T> value = getValue();
        T finalValue = value.orElse(fallBackValue);
        updateEnum(finalValue, gui);
    }

    @Override
    public EnumFeature clone(FeatureParentInterface newParent) {
        EnumFeature<T> clone = new EnumFeature<T>(newParent, getDefaultValue(), getFeatureSettings(), enumClass, fallBackValue, enumName, enumValues);
        clone.value = value;
        return clone;
    }

    @Override
    public void reset() {
        this.value = defaultValue;
    }

    @Override
    public void clickParentEditor(Player editor, NewGUIManager manager) {
        return;
    }

    @Override
    public boolean noShiftclicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean noShiftLeftclicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean noShiftRightclicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean shiftClicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean shiftLeftClicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean shiftRightClicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean leftClicked(Player editor, NewGUIManager manager) {
        updateEnum(nextEnum(getEnum((GUI) manager.getCache().get(editor))), (GUI) manager.getCache().get(editor));
        return true;
    }

    @Override
    public boolean rightClicked(Player editor, NewGUIManager manager) {
        updateEnum(prevEnum(getEnum((GUI) manager.getCache().get(editor))), (GUI) manager.getCache().get(editor));
        return true;
    }

    @Override
    public boolean doubleClicked(Player editor, NewGUIManager manager) {
        return false;
    }

    public T nextEnum(T slot) {
        boolean next = false;
        for (T check : getSortEnum()) {
            if (check.equals(slot)) {
                next = true;
                continue;
            }
            if (next) return check;
        }
        return getSortEnum().get(0);
    }

    public T prevEnum(T slot) {
        int i = -1;
        int cpt = 0;
        for (T check : getSortEnum()) {
            if (check.equals(slot)) {
                i = cpt;
                break;
            }
            cpt++;
        }
        if (i == 0) return getSortEnum().get(getSortEnum().size() - 1);
        else return getSortEnum().get(cpt - 1);
    }

    public void updateEnum(T slot, GUI gui) {

        this.value = Optional.of(slot);
        ItemStack item = gui.getByName(getEditorName());
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore().subList(0, getEditorDescription().length + 2);
        int maxSize = lore.size();
        maxSize += getSortEnum().size();
        if (maxSize > 17) maxSize = 17;
        boolean find = false;
        for (T check : getSortEnum()) {
            if (slot.equals(check)) {
                lore.add(StringConverter.coloredString("&2➤ &a" + slot.name()));
                find = true;
            } else if (find) {
                if (lore.size() == maxSize) break;
                lore.add(StringConverter.coloredString("&6✦ &e" + check.name()));
            }
        }
        for (T check : getSortEnum()) {
            if (lore.size() == maxSize) break;
            else {
                lore.add(StringConverter.coloredString("&6✦ &e" + check.name()));
            }
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        /* Update the gui only for the right click , for the left it updated automaticaly idk why */
        for (HumanEntity e : gui.getInv().getViewers()) {
            if (e instanceof Player) {
                Player p = (Player) e;
                p.updateInventory();
            }
        }
    }

    public T getEnum(GUI gui) {
        ItemStack item = gui.getByName(getEditorName());
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        for (String str : lore) {
            if (str.contains("➤ ")) {
                str = StringConverter.decoloredString(str).replaceAll(" Premium", "");
                return (T) T.valueOf(enumClass, str.split("➤ ")[1]);
            }
        }
        return null;
    }

    public List<T> getSortEnum() {
        SortedMap<String, T> map = new TreeMap<String, T>();
        for (T l : enumValues) {
            map.put(l.name(), l);
        }
        return new ArrayList<>(map.values());
    }

}
