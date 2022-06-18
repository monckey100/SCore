package com.ssomar.scoretestrecode.features.custom.potionsettings;

import com.ssomar.score.menu.GUI;
import com.ssomar.scoretestrecode.features.editor.FeatureEditorInterface;

public class PotionSettingsFeatureEditor extends FeatureEditorInterface<PotionSettingsFeature> {

    public PotionSettingsFeature dropFeatures;

    public PotionSettingsFeatureEditor(PotionSettingsFeature dropFeatures) {
        super("&lHiders Editor", 3*9);
        this.dropFeatures = dropFeatures.clone();
        load();
    }

    @Override
    public void load() {
        dropFeatures.getHideEnchantments().initAndUpdateItemParentEditor(this, 0);
        dropFeatures.getHideUnbreakable().initAndUpdateItemParentEditor(this, 1);
        dropFeatures.getHideAttributes().initAndUpdateItemParentEditor(this, 2);
        dropFeatures.getHidePotionEffects().initAndUpdateItemParentEditor(this, 3);
        dropFeatures.getHideUsage().initAndUpdateItemParentEditor(this, 4);

        // Back
        createItem(RED, 	1, 18, GUI.BACK, false, false);

        // Reset menu
        createItem(ORANGE, 			1, 19, GUI.RESET, false, false, "", "&c&oClick here to reset");

        // Save menu
        createItem(GREEN, 1, 26, GUI.SAVE, false, false, "", "&a&oClick here to save");
    }

    @Override
    public PotionSettingsFeature getParent() {
        return dropFeatures;
    }
}
