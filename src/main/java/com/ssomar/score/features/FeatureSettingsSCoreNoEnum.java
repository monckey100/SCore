package com.ssomar.score.features;

import org.bukkit.Material;

public class FeatureSettingsSCoreNoEnum implements FeatureSettingsInterface {

    private String configName;
    private String editorName;
    private String[] editorDescription;
    private Material editorMaterial;
    private boolean requirePremium;
    private SavingVerbosityLevel savingVerbosityLevel;

    public FeatureSettingsSCoreNoEnum(String name, String editorName, String[] editorDescription, Material editorMaterial, boolean requirePremium, SavingVerbosityLevel savingVerbosityLevel) {
        this.configName = name;
        this.editorName = editorName;
        this.editorDescription = editorDescription;
        this.editorMaterial = editorMaterial;
        this.requirePremium = requirePremium;
        this.savingVerbosityLevel = savingVerbosityLevel;
    }

    @Override
    public String getIdentifier() {
        return configName;
    }

    @Override
    public String getName() {
        return configName;
    }

    @Override
    public void setName(String name) {
        this.configName = name;
    }

    @Override
    public String getEditorName() {
        return editorName;
    }

    @Override
    public String[] getEditorDescription() {
        return editorDescription;
    }

    @Override
    public String[] getEditorDescriptionBrut() {
        return editorDescription;
    }

    @Override
    public Material getEditorMaterial() {
        return editorMaterial;
    }

    @Override
    public boolean isRequirePremium() {
        return requirePremium;
    }

    @Override
    public void setRequirePremium(boolean requirePremium) {
        this.requirePremium = requirePremium;
    }

    @Override
    public SavingVerbosityLevel getSavingVerbosityLevel() {
        return savingVerbosityLevel;
    }

    @Override
    public void setSavingVerbosityLevel(SavingVerbosityLevel savingVerbosityLevel) {
        this.savingVerbosityLevel = savingVerbosityLevel;
    }
}
