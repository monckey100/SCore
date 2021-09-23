package com.ssomar.score.projectiles.features;

import com.ssomar.score.menu.SimpleGUI;
import com.ssomar.score.projectiles.types.CustomProjectile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class GravityFeature extends DecorateurCustomProjectiles {

    boolean isGravity;

    public GravityFeature(CustomProjectile cProj){
        super.cProj = cProj;
        isGravity = true;
    }

    @Override
    public boolean loadConfiguration(FileConfiguration projConfig) {
        isGravity = projConfig.getBoolean("gravity", true);
        return cProj.loadConfiguration(projConfig) && true;
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        e.setGravity(isGravity);
        cProj.transformTheProjectile(e, launcher);
    }

    @Override
    public SimpleGUI getConfigGUI() {
        SimpleGUI gui = cProj.getConfigGUI();
        return gui;
    }
}