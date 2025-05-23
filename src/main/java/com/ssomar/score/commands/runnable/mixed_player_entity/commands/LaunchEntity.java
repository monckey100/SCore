package com.ssomar.score.commands.runnable.mixed_player_entity.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.SCommandToExec;
import com.ssomar.score.commands.runnable.mixed_player_entity.MixedCommand;
import com.ssomar.score.events.PlayerCustomLaunchEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* LAUNCHENTITY {entityType} */
public class LaunchEntity extends MixedCommand {

    public LaunchEntity() {
        super();
        setPriority(2);
    }

    @Override
    public void run(Player p, Entity receiver, SCommandToExec sCommandToExec) {
        List<String> args = sCommandToExec.getOtherArgs();

        if(!(receiver instanceof LivingEntity)) return;
        LivingEntity livingReceiver = (LivingEntity) receiver;

        Location loc = livingReceiver.getEyeLocation();
        //loc.setY(loc.getY()-1);
        double speed = 1;
        double rotation = 0;
        Entity entity = null;
        //SsomarDev.testMsg("args: "+args.get(0), true);
        if (!args.isEmpty()) {
            String entityName = args.get(0);
            try {
                EntityType entityType = EntityType.valueOf(entityName.toUpperCase());
                entity = receiver.getWorld().spawnEntity(loc, entityType);
            } catch (Exception ignored) {
                try{
                    //SsomarDev.testMsg("PASSE 2 LAUNCHENTITY: ", true);
                    entity = Bukkit.getEntityFactory().createEntitySnapshot(entityName).createEntity(loc);
                }catch (Exception ignored2){
                    entity = receiver.getWorld().spawnEntity(loc, EntityType.PIG);
                }
            }
        }
        if (entity == null) return;

        if (args.size() >= 2) {
            try {
                speed = Double.parseDouble(args.get(1));
            } catch (Exception ignored) {
            }
        }

        if (args.size() >= 3) {
            try {
                rotation = Double.parseDouble(args.get(2));
                rotation = rotation * Math.PI / 180;
            } catch (Exception ignored) {
            }
        }
        Vector v = livingReceiver.getEyeLocation().getDirection();
        v.multiply(speed);
        if (!SCore.is1v13Less()) v.rotateAroundY(rotation);
        entity.setVelocity(v);

        if(receiver instanceof Player) {
            PlayerCustomLaunchEntityEvent playerCustomLaunchProjectileEvent = new PlayerCustomLaunchEntityEvent((Player) receiver, entity);
            Bukkit.getServer().getPluginManager().callEvent(playerCustomLaunchProjectileEvent);
        }
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        String launch = "LAUNCHENTITY {entityType} {speed} [angle rotation y]";
        if (args.size() < 1) return Optional.of(notEnoughArgs + launch);

        return Optional.empty();
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("LAUNCHENTITY");
        return names;
    }

    @Override
    public String getTemplate() {
        return "LAUNCHENTITY {entityType} {speed} [angle rotation y]";
    }

    @Override
    public ChatColor getColor() {
        return null;
    }

    @Override
    public ChatColor getExtraColor() {
        return null;
    }
}
