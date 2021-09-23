package com.ssomar.score.projectiles.features;

import com.ssomar.score.menu.SimpleGUI;
import com.ssomar.score.projectiles.types.CustomProjectile;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class BounceFeature extends DecorateurCustomProjectiles {

    boolean isBounce;

    public BounceFeature(CustomProjectile cProj){
        super.cProj = cProj;
        isBounce = false;
    }

    @Override
    public boolean loadConfiguration(FileConfiguration projConfig) {
        isBounce = projConfig.getBoolean("bounce", false);

        return cProj.loadConfiguration(projConfig) && true;
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        if (e instanceof Projectile) {
            ((Projectile) e).setBounce(isBounce);
        }
        cProj.transformTheProjectile(e, launcher);
    }

    @Override
    public SimpleGUI getConfigGUI() {
        SimpleGUI gui = cProj.getConfigGUI();
        gui.createItem(Material.SLIME_BLOCK, 1, 0, "BOUNCE", false, false, "Click to edit");
        return gui;
    }
}