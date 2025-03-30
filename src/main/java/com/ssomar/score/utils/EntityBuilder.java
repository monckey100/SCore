package com.ssomar.score.utils;

import com.ssomar.score.usedapi.MythicMobsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class EntityBuilder {

    private String entityDefinitionToBuild;

    public EntityBuilder(String entityDefinitionToBuild) {
        this.entityDefinitionToBuild = entityDefinitionToBuild;
    }

    public Entity buildEntity(Location location) {
        Entity entity = null;

        String entityName = entityDefinitionToBuild;

        // Try MythicMobs
        entity = MythicMobsAPI.buildMythicMob(entityName, location);
        if(entity != null) return entity;

        // Basic entity
        try {
            EntityType entityType = EntityType.valueOf(entityName.toUpperCase());
            entity = location.getWorld().spawnEntity(location, entityType);
        } catch (Exception ignored) {
            // Try to create entity from snapshot
            try {
                entity = Bukkit.getEntityFactory().createEntitySnapshot(entityName).createEntity(location);
            } catch (Exception ignored2) {
                entity = location.getWorld().spawnEntity(location, EntityType.PIG);
            }
        }
        return entity;
    }

}
