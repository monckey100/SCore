package com.ssomar.score.projectiles.features;

import com.ssomar.score.SCore;
import com.ssomar.score.features.FeatureParentInterface;
import com.ssomar.score.features.FeatureSettingsSCore;
import com.ssomar.score.features.types.DoubleFeature;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Optional;

public class DespawnFeature extends DoubleFeature implements SProjectileFeatureInterface {

    public DespawnFeature(FeatureParentInterface parent) {
        super(parent, Optional.of(-1.0), FeatureSettingsSCore.despawnDelay);
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher, Material materialLaunched) {
        if (getValue().isPresent() && getValue().get() != -1) {
            Runnable runnable = new Runnable() {
                public void run() {
                    if (e != null)
                        e.remove();
                }
            };
            SCore.schedulerHook.runEntityTask(runnable, null, e, (int)(getValue().get() * 20));
        }
    }

    @Override
    public DespawnFeature clone(FeatureParentInterface newParent) {
        DespawnFeature clone = new DespawnFeature(newParent);
        clone.setValue(getValue());
        return clone;
    }
}
