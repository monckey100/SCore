package com.ssomar.score.projectiles.features;

import com.ssomar.score.SCore;
import com.ssomar.score.menu.SimpleGUI;
import com.ssomar.score.projectiles.types.CustomProjectile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class KnockbackStrengthFeature extends DecorateurCustomProjectiles {

    int knockbackStrength;

    public KnockbackStrengthFeature(CustomProjectile cProj){
        super.cProj = cProj;
        knockbackStrength = -1;
    }

    @Override
    public boolean loadConfiguration(FileConfiguration projConfig) {
        knockbackStrength = projConfig.getInt("knockbackStrength", -1);
        return cProj.loadConfiguration(projConfig) && true;
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        if (!SCore.is1v12() && e instanceof AbstractArrow) {
            AbstractArrow aA = (AbstractArrow) e;
            if (knockbackStrength != -1)
                aA.setKnockbackStrength(knockbackStrength);
        }
        cProj.transformTheProjectile(e, launcher);
    }

    @Override
    public SimpleGUI getConfigGUI() {
        SimpleGUI gui = cProj.getConfigGUI();
        return gui;
    }
}