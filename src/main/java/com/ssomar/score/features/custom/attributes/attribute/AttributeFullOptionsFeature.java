package com.ssomar.score.features.custom.attributes.attribute;

import com.ssomar.score.features.FeatureInterface;
import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.FeatureSettingsSCore;
import com.ssomar.score.features.FeatureWithHisOwnEditor;
import com.ssomar.score.features.editor.GenericFeatureParentEditor;
import com.ssomar.score.features.editor.GenericFeatureParentEditorManager;
import com.ssomar.score.features.types.*;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.backward_compatibility.AttributeUtils;
import com.ssomar.score.utils.emums.AttributeSlot;
import com.ssomar.score.utils.placeholders.StringPlaceholder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

@Getter
@Setter
public class AttributeFullOptionsFeature extends FeatureWithHisOwnEditor<AttributeFullOptionsFeature, AttributeFullOptionsFeature, GenericFeatureParentEditor, GenericFeatureParentEditorManager> {

    private AttributeFeature attribute;
    private OperationFeature operation;
    private DoubleFeature amount;
    private SlotFeature slot;
    private ColoredStringFeature attributeName;
    private UUIDFeature uuid;
    private String id;

    public AttributeFullOptionsFeature(FeatureParentInterface parent, String id) {
        super(parent, FeatureSettingsSCore.attribute);
        this.id = id;
        reset();
    }

    @Override
    public void reset() {
        this.attribute = new AttributeFeature(this, Optional.of(AttributeUtils.getAttribute("GENERIC_ARMOR")), FeatureSettingsSCore.attribute);
        this.operation = new OperationFeature(this, Optional.of(AttributeModifier.Operation.ADD_NUMBER), FeatureSettingsSCore.operation);
        this.amount = new DoubleFeature(this, Optional.of(1.0), FeatureSettingsSCore.amount);
        this.slot = new SlotFeature(this, Optional.of(AttributeSlot.HAND), FeatureSettingsSCore.slot);
        this.attributeName = new ColoredStringFeature(this, Optional.of("&eDefault name"), FeatureSettingsSCore.name);
        this.uuid = new UUIDFeature(this, FeatureSettingsSCore.uuid);
    }

    public AttributeModifier getAttributeModifier() {
        String attributeName = this.attributeName.getValue().orElse("Default name");
        double amount = this.amount.getValue(null, new StringPlaceholder()).orElse(1.0);
        AttributeModifier.Operation operation = this.operation.getValue().orElse(AttributeModifier.Operation.ADD_NUMBER);
        EquipmentSlot equipmentSlot = null;
        if (!slot.getValue().orElse(AttributeSlot.HAND).equals(AttributeSlot.ALL_SLOTS)) {
            equipmentSlot = this.slot.getEquipmentSlotValue().orElse(null);
        }
        return new AttributeModifier(uuid.getValue(), attributeName, amount, operation, equipmentSlot);
    }

    public AttributeModifier getAttributeModifier(UUID playerUUID, @Nullable StringPlaceholder sp) {
       // SsomarDev.testMsg("playerUUID: " + playerUUID, true);
        String attributeName = this.attributeName.getValue().orElse("Default name");
        if(sp == null) sp = new StringPlaceholder();
        double amount = this.amount.getValue(playerUUID, sp).orElse(1.0);
        AttributeModifier.Operation operation = this.operation.getValue().orElse(AttributeModifier.Operation.ADD_NUMBER);
        EquipmentSlot equipmentSlot = null;
        if (!slot.getValue().orElse(AttributeSlot.HAND).equals(AttributeSlot.ALL_SLOTS)) {
            equipmentSlot = this.slot.getEquipmentSlotValue().orElse(null);
        }
        return new AttributeModifier(uuid.getValue(), attributeName, amount, operation, equipmentSlot);
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> errors = new ArrayList<>();
        if (config.isConfigurationSection(id)) {
            ConfigurationSection enchantmentConfig = config.getConfigurationSection(id);
            errors.addAll(this.attribute.load(plugin, enchantmentConfig, isPremiumLoading));
            errors.addAll(this.operation.load(plugin, enchantmentConfig, isPremiumLoading));
            errors.addAll(this.amount.load(plugin, enchantmentConfig, isPremiumLoading));
            errors.addAll(this.slot.load(plugin, enchantmentConfig, isPremiumLoading));
            errors.addAll(this.attributeName.load(plugin, enchantmentConfig, isPremiumLoading));
            errors.addAll(this.uuid.load(plugin, enchantmentConfig, isPremiumLoading));
        } else {
            errors.add("&cERROR, Couldn't load the Attribute with its options because there is not section with the good ID: " + id + " &7&o" + getParent().getParentInfo());
        }
        return errors;
    }

    @Override
    public boolean isTheFeatureClickedParentEditor(String featureClicked) {
        return featureClicked.contains(getEditorName()) && featureClicked.contains("(" + id + ")");
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(id, null);
        ConfigurationSection attributeConfig = config.createSection(id);
        this.attribute.save(attributeConfig);
        this.operation.save(attributeConfig);
        this.amount.save(attributeConfig);
        this.slot.save(attributeConfig);
        this.attributeName.save(attributeConfig);
        this.uuid.save(attributeConfig);
    }

    @Override
    public AttributeFullOptionsFeature getValue() {
        return this;
    }

    @Override
    public AttributeFullOptionsFeature initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 6];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 6] = "&7Attribute: &e" + AttributeUtils.getAttributes().get(attribute.getValue().get());
        finalDescription[finalDescription.length - 5] = "&7Operation: &e" + operation.getValue().get();
        finalDescription[finalDescription.length - 4] = "&7Amount: &e" + amount.getValue().get();
        finalDescription[finalDescription.length - 3] = "&7Slot: &e" + this.slot.getValue().get().name();
        finalDescription[finalDescription.length - 2] = GUI.CLICK_HERE_TO_CHANGE;
        finalDescription[finalDescription.length - 1] = GUI.SHIFT_CLICK_TO_REMOVE;

        gui.createItem(getEditorMaterial(), 1, slot, GUI.TITLE_COLOR + getEditorName() + " - " + "(" + id + ")", false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public AttributeFullOptionsFeature clone(FeatureParentInterface newParent) {
        AttributeFullOptionsFeature eF = new AttributeFullOptionsFeature(newParent, id);
        eF.setAttribute(attribute.clone(eF));
        eF.setOperation(operation.clone(eF));
        eF.setAmount(amount.clone(eF));
        eF.setSlot(slot.clone(eF));
        eF.setAttributeName(attributeName.clone(eF));
        eF.setUuid(uuid.clone(eF));
        return eF;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        return new ArrayList<>(Arrays.asList(attribute, operation, amount, slot, attributeName, uuid));
    }

    @Override
    public String getParentInfo() {
        return getParent().getParentInfo();
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        ConfigurationSection parentSection = getParent().getConfigurationSection();
        if (parentSection.isConfigurationSection(getId())) {
            return parentSection.getConfigurationSection(getId());
        } else return parentSection.createSection(getId());
    }

    @Override
    public File getFile() {
        return getParent().getFile();
    }

    @Override
    public void reload() {
        for (FeatureInterface feature : (List<FeatureInterface>) getParent().getFeatures()) {
            if (feature instanceof AttributeFullOptionsFeature) {
                AttributeFullOptionsFeature aFOF = (AttributeFullOptionsFeature) feature;
                if (aFOF.getId().equals(id)) {
                    aFOF.setAttribute(attribute);
                    aFOF.setOperation(operation);
                    aFOF.setAmount(amount);
                    aFOF.setSlot(slot);
                    aFOF.setAttributeName(attributeName);
                    aFOF.setUuid(uuid);
                    break;
                }
            }
        }
    }

    @Override
    public void openBackEditor(@NotNull Player player) {
        getParent().openEditor(player);
    }

    @Override
    public void openEditor(@NotNull Player player) {
        GenericFeatureParentEditorManager.getInstance().startEditing(player, this);
    }

}
