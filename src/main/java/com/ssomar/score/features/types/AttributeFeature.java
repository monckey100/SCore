package com.ssomar.score.features.types;

import com.ssomar.score.SCore;
import com.ssomar.score.SsomarDev;
import com.ssomar.score.editor.NewGUIManager;
import com.ssomar.score.features.*;
import com.ssomar.score.languages.messages.TM;
import com.ssomar.score.languages.messages.Text;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.backward_compatibility.AttributeUtils;
import com.ssomar.score.utils.item.UpdateItemInGUI;
import com.ssomar.score.utils.strings.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public class AttributeFeature extends FeatureAbstract<Optional<Attribute>, AttributeFeature> implements FeatureRequireOnlyClicksInEditor {

    private Optional<Attribute> value;
    private Optional<Attribute> defaultValue;

    public AttributeFeature(FeatureParentInterface parent, Optional<Attribute> defaultValue, FeatureSettingsInterface featureSettings) {
        super(parent, featureSettings);
        this.defaultValue = defaultValue;
        reset();
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        String attributeStr = config.getString(this.getName());
        Attribute attribute = null;
        if (SCore.is1v12Less()) {
            errors.add("&cERROR, Couldn't load the Attribute value of " + this.getName() + " from config, value: " + attributeStr + " &7&o" + getParent().getParentInfo() + " &6>> Attributes are only available for 1.13+");
            value = Optional.empty();
        } else {
            try {
                attribute = AttributeUtils.getAttribute(attributeStr);
                value = Optional.of(attribute);
                FeatureReturnCheckPremium<Attribute> checkPremium = checkPremium("Attribute", attribute, defaultValue, isPremiumLoading);
                if (checkPremium.isHasError()) value = Optional.of(checkPremium.getNewValue());
            } catch (Exception | Error e) {
                errors.add("&cERROR, Couldn't load the Attribute value of " + this.getName() + " from config, value: " + attributeStr + " &7&o" + getParent().getParentInfo() + " &6>> Attributes available: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/attribute/Attribute.html");
                value = Optional.empty();
            }
        }
        return errors;
    }

    @Override
    public void save(ConfigurationSection config) {
        Optional<Attribute> optional = getValue();
        if (optional.isPresent()) {
            config.set(this.getName(), AttributeUtils.getAttributes().get(optional.get()));
        } else config.set(this.getName(), null);
    }

    @Override
    public Optional<Attribute> getValue() {
        if (value.isPresent()) return value;
        else return defaultValue;
    }

    @Override
    public AttributeFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 2];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 2] = GUI.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = TM.g(Text.EDITOR_CURRENTLY_NAME);

        gui.createItem(getEditorMaterial(), 1, slot, GUI.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {
        Attribute att = null;
        if (SCore.is1v21v2Plus()) att = Attribute.ARMOR;
        else att = AttributeUtils.getAttribute("GENERIC_ARMOR");
        Optional<Attribute> optional = getValue();
        if (optional.isPresent()) updateAttribute(optional.get(), gui);
        else updateAttribute(att, gui);
    }

    @Override
    public AttributeFeature clone(FeatureParentInterface newParent) {
        AttributeFeature clone = new AttributeFeature(newParent, defaultValue, getFeatureSettings());
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
        Attribute attribute = getAttribute((GUI) manager.getCache().get(editor));
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        attribute = nextAttribute(attribute);
        updateAttribute(attribute, (GUI) manager.getCache().get(editor));
        return true;
    }

    @Override
    public boolean shiftRightClicked(Player editor, NewGUIManager manager) {
        Attribute attribute = getAttribute((GUI) manager.getCache().get(editor));
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        attribute = prevAttribute(attribute);
        updateAttribute(attribute, (GUI) manager.getCache().get(editor));
        return true;
    }

    @Override
    public boolean leftClicked(Player editor, NewGUIManager manager) {
        updateAttribute(nextAttribute(getAttribute((GUI) manager.getCache().get(editor))), (GUI) manager.getCache().get(editor));
        return true;
    }

    @Override
    public boolean rightClicked(Player editor, NewGUIManager manager) {
        updateAttribute(prevAttribute(getAttribute((GUI) manager.getCache().get(editor))), (GUI) manager.getCache().get(editor));
        return true;
    }

    @Override
    public boolean doubleClicked(Player editor, NewGUIManager manager) {
        return false;
    }

    @Override
    public boolean middleClicked(Player editor, NewGUIManager manager) {
        return false;
    }

    public Attribute nextAttribute(Attribute attribute) {
        boolean next = false;
        Map<Object, String> map = AttributeUtils.getAttributes();
        for (Object check : map.keySet()) {
            if (check.equals(attribute)) {
                next = true;
                continue;
            }
            if (next) return (Attribute) check;
        }
        return (Attribute) map.keySet().iterator().next();
    }

    public Attribute prevAttribute(Attribute attribute) {
        int i = -1;
        int cpt = 0;
        Map<Object, String> map = AttributeUtils.getAttributes();
        for (Object check : map.keySet()) {
            if (check.equals(attribute)) {
                i = cpt;
                break;
            }
            cpt++;
        }
        if (i == 0) return (Attribute) map.keySet().toArray()[map.size() - 1];
        else return (Attribute) map.keySet().toArray()[cpt - 1];
    }

    public void updateAttribute(Attribute attribute, GUI gui) {
        this.value = Optional.of(attribute);
        ItemStack item = gui.getByIdentifier(getEditorName());
        /*
         * Design set Type ENCHANTED_BOOK AND ADD THE GOOD ENCHANTMENT
         *
         * */
        ItemMeta meta = item.getItemMeta();
        Map<Object, String> map = AttributeUtils.getAttributes();
        List<String> lore = meta.getLore().subList(0, getEditorDescription().length + 2);
        int maxSize = lore.size();
        maxSize += map.size();
        if (maxSize > 17) maxSize = 17;
        boolean find = false;
        for (Object check : map.keySet()) {
            if (attribute == check) {
                lore.add(StringConverter.coloredString("&2➤ &a" + map.get(attribute)));
                find = true;
            } else if (find) {
                if (lore.size() == maxSize) break;
                lore.add(StringConverter.coloredString("&6✦ &e" + map.get(check)));
            }
        }
        for (Object check : map.keySet()) {
            if (lore.size() == maxSize) break;
            else {
                lore.add(StringConverter.coloredString("&6✦ &e" + map.get(check)));
            }
        }
        for (String str : lore) {
            SsomarDev.testMsg(str, true);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);

        /* Bug item no update idk why */
        UpdateItemInGUI.updateItemInGUI(gui, getEditorName(), meta.getDisplayName(), lore, item.getType());
    }

    public Attribute getAttribute(GUI gui) {
        ItemStack item = gui.getByIdentifier(getEditorName());
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        for (String str : lore) {
            if (str.contains("➤ ")) {
                str = StringConverter.decoloredString(str).replaceAll(" Premium", "");
                return AttributeUtils.getAttribute(str.split("➤ ")[1]);
            }
        }
        return null;
    }

}
