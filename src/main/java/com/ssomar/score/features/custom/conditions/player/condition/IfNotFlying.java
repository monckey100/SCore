package com.ssomar.score.features.custom.conditions.player.condition;

import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.FeatureSettingsSCore;
import com.ssomar.score.features.custom.conditions.player.PlayerConditionFeature;
import com.ssomar.score.features.custom.conditions.player.PlayerConditionRequest;
import com.ssomar.score.features.types.BooleanFeature;
import org.bukkit.entity.Player;

public class IfNotFlying extends PlayerConditionFeature<BooleanFeature, IfNotFlying> {

    public IfNotFlying(FeatureParentInterface parent) {
        super(parent, FeatureSettingsSCore.ifNotFlying);
    }

    @Override
    public boolean verifCondition(PlayerConditionRequest request) {
        Player player = request.getPlayer();
        if (getCondition().getValue(request.getSp()) && player.isFlying()) {
            runInvalidCondition(request);
            return false;
        }
        return true;
    }

    @Override
    public IfNotFlying getValue() {
        return this;
    }

    @Override
    public void subReset() {
        setCondition(new BooleanFeature(getParent(),  false, FeatureSettingsSCore.ifNotFlying));
    }

    @Override
    public boolean hasCondition() {
        return getCondition().isConfigured();
    }

    @Override
    public IfNotFlying getNewInstance(FeatureParentInterface parent) {
        return new IfNotFlying(parent);
    }
}
