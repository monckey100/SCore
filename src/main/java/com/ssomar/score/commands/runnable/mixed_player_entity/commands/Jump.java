package com.ssomar.score.commands.runnable.mixed_player_entity.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ArgumentChecker;
import com.ssomar.score.commands.runnable.SCommandToExec;
import com.ssomar.score.commands.runnable.mixed_player_entity.MixedCommand;
import com.ssomar.score.events.NoFallDamageListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/* JUMP {amount} [fall damage]*/
public class Jump extends MixedCommand {


    @Override
    public void run(Player p, Entity receiver, SCommandToExec sCommandToExec) {
        List<String> args = sCommandToExec.getOtherArgs();

        double jump = Double.parseDouble(args.get(0));

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                Vector v = receiver.getVelocity();
                v.setX(0);
                v.setY(jump);
                v.setZ(0);
                receiver.setVelocity(v);

                if(args.size() >= 2){
                    String falldamage = args.get(1);

                    if(falldamage.equalsIgnoreCase("true")){

                    }else {
                        NoFallDamageListener.getInstance().addNoFallDamage(receiver);
                    }

                }else{
                    NoFallDamageListener.getInstance().addNoFallDamage(receiver);
                }
            }
        };
        /* Delayed of one tick Because when a player hit another player the velocity of the hit override the jump*/
        SCore.schedulerHook.runTask(task, 1);

    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        if (args.size() < 1) return Optional.of(notEnoughArgs + getTemplate());

        ArgumentChecker ac1 = checkDouble(args.get(0), isFinalVerification, getTemplate());
        if (!ac1.isValid()) return Optional.of(ac1.getError());

        if(args.size() >= 2){
            ArgumentChecker ac2 = checkBoolean(args.get(1), isFinalVerification, getTemplate());
            if (!ac2.isValid()) return Optional.of(ac2.getError());
        }

        return Optional.empty();
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("JUMP");
        return names;
    }

    @Override
    public String getTemplate() {
        return "JUMP {number (max 5)} [fallDamageDefaultFalse]";
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