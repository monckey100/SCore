package com.ssomar.score.features.custom.hiders;

import com.ssomar.score.SCore;
import com.ssomar.score.config.GeneralConfig;
import com.ssomar.score.features.*;
import com.ssomar.score.features.editor.GenericFeatureParentEditor;
import com.ssomar.score.features.editor.GenericFeatureParentEditorManager;
import com.ssomar.score.features.types.BooleanFeature;
import com.ssomar.score.menu.GUI;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.emums.ResetSetting;
import com.ssomar.score.utils.strings.StringConverter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Hiders extends FeatureWithHisOwnEditor<Hiders, Hiders, GenericFeatureParentEditor, GenericFeatureParentEditorManager> implements FeatureForItem {

    private BooleanFeature hideEnchantments;
    private BooleanFeature hideUnbreakable;
    private BooleanFeature hideAttributes;
    private BooleanFeature hidePotionEffects;
    private BooleanFeature hideUsage;
    private BooleanFeature hideDye;
    private BooleanFeature hideArmorTrim;
    private BooleanFeature hideDestroys;
    private BooleanFeature hidePlacedOn;
    private BooleanFeature hideAdditionalTooltip;
    private BooleanFeature hideTooltip;

    public Hiders(FeatureParentInterface parent) {
        super(parent, FeatureSettingsSCore.hiders);
        reset();
    }

    @Override
    public void reset() {
        this.hideEnchantments = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideEnchantments);
        this.hideUnbreakable = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideUnbreakable);
        this.hideAttributes = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideAttributes);
        this.hidePotionEffects = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hidePotionEffects);
        this.hideUsage = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideUsage);
        this.hideDye = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideDye);
        this.hideArmorTrim = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideArmorTrim);
        this.hideDestroys = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideDestroys);
        this.hidePlacedOn = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hidePlacedOn);
        this.hideAdditionalTooltip = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideAdditionalTooltip);
        this.hideTooltip = new BooleanFeature(getParent(),  false, FeatureSettingsSCore.hideTooltip);
    }

    @Override
    public List<String> load(SPlugin plugin, ConfigurationSection config, boolean isPremiumLoading) {
        List<String> error = new ArrayList<>();
        if (config.isConfigurationSection(this.getName())) {
            ConfigurationSection section = config.getConfigurationSection(this.getName());
            hideEnchantments.load(plugin, section, isPremiumLoading);
            hideUnbreakable.load(plugin, section, isPremiumLoading);
            hideAttributes.load(plugin, section, isPremiumLoading);
            hideUsage.load(plugin, section, isPremiumLoading);
            if(!SCore.is1v11Less()){
                hideDestroys.load(plugin, section, isPremiumLoading);
                hidePlacedOn.load(plugin, section, isPremiumLoading);
            }
            if(SCore.is1v17Plus()) hideDye.load(plugin, section, isPremiumLoading);
            if(SCore.is1v20Plus()) hideArmorTrim.load(plugin, section, isPremiumLoading);
            /* In 1.20.5+ New tag HIDE_ADDITIONAL_TOOLTIP  Setting to show/hide potion effects, book and firework information, map tooltips, patterns of banners, and enchantments of enchanted books.
            * It replaces the old hidePotionEffects */
            if(SCore.is1v20v5Plus()) {
                hideAdditionalTooltip.load(plugin, section, isPremiumLoading);
                hideTooltip.load(plugin, section, isPremiumLoading);
            }
            else hidePotionEffects.load(plugin, section, isPremiumLoading);
        }

        return error;
    }

    @Override
    public void save(ConfigurationSection config) {
        config.set(getName(), null);
        ConfigurationSection section = config.createSection(getName());
        hideEnchantments.save(section);
        hideUnbreakable.save(section);
        hideAttributes.save(section);
        hideUsage.save(section);
        if(!SCore.is1v11Less()){
            hideDestroys.save(section);
            hidePlacedOn.save(section);
        }
        if(SCore.is1v17Plus()) hideDye.save(section);
        if(SCore.is1v20Plus()) hideArmorTrim.save(section);
        if(SCore.is1v20v5Plus()){
            hideAdditionalTooltip.save(section);
            hideTooltip.save(section);}
        else hidePotionEffects.save(section);
        if(isSavingOnlyIfDiffDefault() && section.getKeys(false).isEmpty()){
            config.set(getName(), null);
            return;
        }

        if (GeneralConfig.getInstance().isEnableCommentsInConfig())
            config.setComments(this.getName(), StringConverter.decoloredString(Arrays.asList(getFeatureSettings().getEditorDescriptionBrut())));

    }

    @Override
    public Hiders getValue() {
        return this;
    }

    @Override
    public Hiders initItemParentEditor(GUI gui, int slot) {
        String[] finalDescription = new String[getEditorDescription().length + 11];
        System.arraycopy(getEditorDescription(), 0, finalDescription, 0, getEditorDescription().length);
        finalDescription[finalDescription.length - 11] = GUI.CLICK_HERE_TO_CHANGE;

        if(!SCore.is1v20v5Plus()) {
            finalDescription[finalDescription.length - 10] = "&7Hide tooltip: &6&lONLY 1.20.5 & +";
        }
        else if (hideTooltip.getValue())
            finalDescription[finalDescription.length - 10] = "&7Hide tooltip: &a&l✔";
        else
            finalDescription[finalDescription.length - 10] = "&7Hide tooltip: &c&l✘";

        if (hideDestroys.getValue())
            finalDescription[finalDescription.length - 9] = "&7Hide destroys: &a&l✔";
        else
            finalDescription[finalDescription.length - 9] = "&7Hide destroys: &c&l✘";

        if (hidePlacedOn.getValue())
            finalDescription[finalDescription.length - 8] = "&7Hide placedOn: &a&l✔";
        else
            finalDescription[finalDescription.length - 8] = "&7Hide placedOn: &c&l✘";

        if (hideEnchantments.getValue())
            finalDescription[finalDescription.length - 7] = "&7Hide enchantments: &a&l✔";
        else
            finalDescription[finalDescription.length - 7] = "&7Hide enchantments: &c&l✘";

        if (hideUnbreakable.getValue())
            finalDescription[finalDescription.length - 6] = "&7Hide unbreakable: &a&l✔";
        else
            finalDescription[finalDescription.length - 6] = "&7Hide unbreakable: &c&l✘";

        if (hideAttributes.getValue())
            finalDescription[finalDescription.length - 5] = "&7Hide attributes: &a&l✔";
        else
            finalDescription[finalDescription.length - 5] = "&7Hide attributes: &c&l✘";

        if (hideUsage.getValue())
            finalDescription[finalDescription.length - 4] = "&7Hide usage: &a&l✔";
        else
            finalDescription[finalDescription.length - 4] = "&7Hide usage: &c&l✘";

        if(!SCore.is1v17Plus()) {
            finalDescription[finalDescription.length - 3] = "&7Hide dye: &6&lONLY 1.17 & +";
        }
        else if (hideDye.getValue())
            finalDescription[finalDescription.length - 3] = "&7Hide dye: &a&l✔";
        else
            finalDescription[finalDescription.length - 3] = "&7Hide dye: &c&l✘";

        if(!SCore.is1v20Plus()) {
            finalDescription[finalDescription.length - 2] = "&7Hide armor trim: &6&lONLY 1.20 & +";
        }
        else if (hideArmorTrim.getValue())
            finalDescription[finalDescription.length - 2] = "&7Hide armor trim: &a&l✔";
        else
            finalDescription[finalDescription.length - 2] = "&7Hide armor trim: &c&l✘";

        if (SCore.is1v20v5Plus()) {
            if (hideAdditionalTooltip.getValue())
                finalDescription[finalDescription.length - 1] = "&7Hide additional tooltip: &a&l✔";
            else
                finalDescription[finalDescription.length - 1] = "&7Hide additional tooltip: &c&l✘";
        }
        else {
            if (hidePotionEffects.getValue())
                finalDescription[finalDescription.length - 1] = "&7Hide effects / banner tags: &a&l✔";
            else
                finalDescription[finalDescription.length - 1] = "&7Hide effects / banner tags: &c&l✘";
        }

        gui.createItem(getEditorMaterial(), 1, slot, GUI.TITLE_COLOR + getEditorName(), false, false, finalDescription);
        return this;
    }

    @Override
    public void updateItemParentEditor(GUI gui) {

    }

    @Override
    public Hiders clone(FeatureParentInterface newParent) {
        Hiders dropFeatures = new Hiders(newParent);
        dropFeatures.hideEnchantments = hideEnchantments.clone(dropFeatures);
        dropFeatures.hideUnbreakable = hideUnbreakable.clone(dropFeatures);
        dropFeatures.hideAttributes = hideAttributes.clone(dropFeatures);
        dropFeatures.hidePotionEffects = hidePotionEffects.clone(dropFeatures);
        dropFeatures.hideUsage = hideUsage.clone(dropFeatures);
        dropFeatures.hideDye = hideDye.clone(dropFeatures);
        dropFeatures.hideArmorTrim = hideArmorTrim.clone(dropFeatures);
        dropFeatures.hideDestroys = hideDestroys.clone(dropFeatures);
        dropFeatures.hidePlacedOn = hidePlacedOn.clone(dropFeatures);
        dropFeatures.hideAdditionalTooltip = hideAdditionalTooltip.clone(dropFeatures);
        dropFeatures.hideTooltip = hideTooltip.clone(dropFeatures);
        return dropFeatures;
    }

    @Override
    public List<FeatureInterface> getFeatures() {
        List<FeatureInterface> features = new ArrayList<>();
        features.add(hideEnchantments);
        features.add(hideUnbreakable);
        features.add(hideAttributes);
        features.add(hideUsage);
        if(!SCore.is1v11Less()){
            features.add(hideDestroys);
            features.add(hidePlacedOn);
        }
        if(SCore.is1v17Plus()) features.add(hideDye);
        if(SCore.is1v20Plus()) features.add(hideArmorTrim);
        if(SCore.is1v20v5Plus()){
            features.add(hideTooltip);
            features.add(hideAdditionalTooltip);
        }
        else features.add(hidePotionEffects);
        return features;
    }

    @Override
    public String getParentInfo() {
        return getParent().getParentInfo();
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        return getParent().getConfigurationSection();
    }

    @Override
    public File getFile() {
        return getParent().getFile();
    }

    @Override
    public void reload() {
        for (FeatureInterface feature : (List<FeatureInterface>) getParent().getFeatures()) {
            if (feature instanceof Hiders) {
                Hiders hiders = (Hiders) feature;
                hiders.setHideEnchantments(hideEnchantments);
                hiders.setHideUnbreakable(hideUnbreakable);
                hiders.setHideAttributes(hideAttributes);
                hiders.setHideUsage(hideUsage);
                if(!SCore.is1v11Less()){
                    hiders.setHideDestroys(hideDestroys);
                    hiders.setHidePlacedOn(hidePlacedOn);
                }
                if(SCore.is1v17Plus()) hiders.setHideDye(hideDye);
                if(SCore.is1v20Plus()) hiders.setHideArmorTrim(hideArmorTrim);
                if(SCore.is1v20v5Plus()) {
                    hiders.setHideAdditionalTooltip(hideAdditionalTooltip);
                    hiders.setHideTooltip(hideTooltip);
                }
                else hiders.setHidePotionEffects(hidePotionEffects);
                break;
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

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean isApplicable(@NotNull FeatureForItemArgs args) {
        return true;
    }

    @Override
    public void applyOnItemMeta(@NotNull FeatureForItemArgs args) {
        ItemMeta meta = args.getMeta();
        ItemFlag additionalFlag = SCore.is1v20v5Plus() ? ItemFlag.HIDE_ADDITIONAL_TOOLTIP : ItemFlag.valueOf("HIDE_POTION_EFFECTS");
        meta.removeItemFlags(additionalFlag, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);

        if (SCore.is1v20v5Plus()) {
            if (getHideAdditionalTooltip().getValue())
                meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ADDITIONAL_TOOLTIP});
            if (getHideTooltip().getValue())
                meta.setHideTooltip(true);
        } else {
            if (getHidePotionEffects().getValue())
                meta.addItemFlags(new ItemFlag[]{ItemFlag.valueOf("HIDE_POTION_EFFECTS")});
        }

        if (getHideAttributes().getValue())
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES});
        if (getHideEnchantments().getValue()) {
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
        }
        if (getHideUnbreakable().getValue())
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
        if (getHideDye().getValue() && SCore.is1v17Plus())
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_DYE});
        if (SCore.is1v20Plus() && getHideArmorTrim().getValue())
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ARMOR_TRIM});

        /* if ((SCore.is1v16() && !SCore.is1v16v1()) || SCore.is1v17() || SCore.is1v18())
            meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_DYE}); */
    }

    @Override
    public void loadFromItemMeta(@NotNull FeatureForItemArgs args) {

        ItemMeta meta = args.getMeta();
        ItemFlag additionalFlag = SCore.is1v20v5Plus() ? ItemFlag.HIDE_ADDITIONAL_TOOLTIP : ItemFlag.valueOf("HIDE_POTION_EFFECTS");
        if(meta.hasItemFlag(additionalFlag)) {
            if (SCore.is1v20v5Plus()) getHideAdditionalTooltip().setValue(true);
            else getHidePotionEffects().setValue(true);
        }
        if (meta.hasItemFlag(ItemFlag.HIDE_ATTRIBUTES))
            getHideAttributes().setValue(true);
        if (meta.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
            getHideEnchantments().setValue(true);
        if (meta.hasItemFlag(ItemFlag.HIDE_UNBREAKABLE))
            getHideUnbreakable().setValue(true);
        if (SCore.is1v17Plus() && meta.hasItemFlag(ItemFlag.HIDE_DYE))
            getHideDye().setValue(true);
        if (SCore.is1v20Plus() && meta.hasItemFlag(ItemFlag.HIDE_ARMOR_TRIM))
            getHideArmorTrim().setValue(true);
        if(SCore.is1v20v5Plus() && meta.isHideTooltip())
            getHideTooltip().setValue(true);

    }

    @Override
    public ResetSetting getResetSetting() {
        return ResetSetting.HIDERS;
    }
}
