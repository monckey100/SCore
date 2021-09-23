package com.ssomar.score.projectiles.features;

import com.ssomar.score.menu.SimpleGUI;
import com.ssomar.score.projectiles.types.CustomProjectile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SilentFeature extends DecorateurCustomProjectiles {

    boolean isSilent;

    public SilentFeature(CustomProjectile cProj){
        super.cProj = cProj;
        isSilent = false;
    }

    @Override
    public boolean loadConfiguration(FileConfiguration projConfig) {
        isSilent = projConfig.getBoolean("silent", false);
        return cProj.loadConfiguration(projConfig) && true;
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        e.setSilent(isSilent);
        cProj.transformTheProjectile(e, launcher);
    }

    @Override
    public SimpleGUI getConfigGUI() {
        SimpleGUI gui = cProj.getConfigGUI();
        return gui;
    }
}