package com.ssomar.score.projectiles.features;

import com.ssomar.score.SCore;
import com.ssomar.score.menu.SimpleGUI;
import com.ssomar.score.projectiles.types.CustomProjectile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DespawnFeature extends DecorateurCustomProjectiles {

    int despawnDelay;

    public DespawnFeature(CustomProjectile cProj){
        super.cProj = cProj;
        despawnDelay = -1;
    }

    @Override
    public boolean loadConfiguration(FileConfiguration projConfig) {
        despawnDelay = projConfig.getInt("despawnDelay", -1);
        return cProj.loadConfiguration(projConfig) && true;
    }

    @Override
    public void transformTheProjectile(Entity e, Player launcher) {
        if (despawnDelay != -1) {
            BukkitRunnable runnable = new BukkitRunnable() {
                public void run() {
                    e.remove();
                }
            };
            runnable.runTaskLater(SCore.plugin, despawnDelay * 20);
        }
        cProj.transformTheProjectile(e, launcher);
    }

    @Override
    public SimpleGUI getConfigGUI() {
        SimpleGUI gui = cProj.getConfigGUI();
        return gui;
    }

}