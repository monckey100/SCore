package com.ssomar.score.features.custom.conditions.entity.condition;

import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.FeatureSettingsSCore;
import com.ssomar.score.features.custom.conditions.entity.EntityConditionFeature;
import com.ssomar.score.features.custom.conditions.entity.EntityConditionRequest;
import com.ssomar.score.features.types.BooleanFeature;
import org.bukkit.entity.Entity;

public class IfNotFromSpawner extends EntityConditionFeature<BooleanFeature, IfNotFromSpawner> {

    public IfNotFromSpawner(FeatureParentInterface parent) {
        super(parent, FeatureSettingsSCore.ifNotFromSpawner);
    }

    @Override
    public void subReset() {
        setCondition(new BooleanFeature(getParent(),  false, FeatureSettingsSCore.ifNotFromSpawner));
    }

    @Override
    public boolean hasCondition() {
        return getCondition().isConfigured();
    }

    @Override
    public IfNotFromSpawner getNewInstance(FeatureParentInterface parent) {
        return new IfNotFromSpawner(parent);
    }

    @Override
    public boolean verifCondition(EntityConditionRequest request) {
        Entity entity = request.getEntity();
        if (getCondition().getValue(request.getSp()) && entity.hasMetadata("fromSpawner")) {
            runInvalidCondition(request);
            return false;
        }

        return true;
    }

    @Override
    public IfNotFromSpawner getValue() {
        return this;
    }
}
