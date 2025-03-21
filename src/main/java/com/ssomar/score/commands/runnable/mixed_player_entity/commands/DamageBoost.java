package com.ssomar.score.commands.runnable.mixed_player_entity.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.SsomarDev;
import com.ssomar.score.commands.runnable.ArgumentChecker;
import com.ssomar.score.commands.runnable.SCommandToExec;
import com.ssomar.score.commands.runnable.mixed_player_entity.MixedCommand;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/* BURN {timeinsecs} */
public class DamageBoost extends MixedCommand {

    private static final Boolean DEBUG = false;
    private static DamageBoost instance;
    @Getter
    private final Map<UUID, List<Double>> activeBoosts;


    public DamageBoost() {
        activeBoosts = new HashMap<>();
    }

    public static DamageBoost getInstance() {
        if (instance == null) instance = new DamageBoost();
        return instance;
    }

    @Override
    public void run(Player p, Entity receiver, SCommandToExec sCommandToExec) {
        List<String> args = sCommandToExec.getOtherArgs();
        double boost = Double.valueOf(args.get(0));
        int time = Double.valueOf(args.get(1)).intValue();

        UUID uuid = receiver.getUniqueId();

        SsomarDev.testMsg("ADD receiver: " + receiver.getUniqueId() + " Damage Boost: " + boost + " for " + time + " ticks", DEBUG);
        if (activeBoosts.containsKey(uuid)) {
            activeBoosts.get(uuid).add(boost);
        } else activeBoosts.put(uuid, new ArrayList<>(Collections.singletonList(boost)));

        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                SsomarDev.testMsg("REMOVE receiver: " + uuid + " Damage Boost: " + boost + " for " + time + " ticks", DEBUG);
                if (activeBoosts.containsKey(uuid)) {
                    if (activeBoosts.get(uuid).size() > 1) {
                        activeBoosts.get(uuid).remove(boost);
                    } else activeBoosts.remove(uuid);
                }
            }
        };
        SCore.schedulerHook.runTask(runnable3, time);

    }

    public double getNewDamage(UUID uuid, double damage) {
        SsomarDev.testMsg("GET NEW DAMAGE FOR: " + uuid + " Damage: " + damage, DEBUG);
        if (DamageBoost.getInstance().getActiveBoosts().containsKey(uuid)) {
            SsomarDev.testMsg("DamageBoostEvent base: " + damage, DEBUG);
            double boost = 0;
            for (double d : DamageBoost.getInstance().getActiveBoosts().get(uuid)) {
                boost += d;
            }
            SsomarDev.testMsg("DamageBoostEvent bost: " + boost, DEBUG);

            double averagePercent = boost / 100;
            damage = damage + (damage * averagePercent);
            SsomarDev.testMsg("DamageBoostEvent modified " + damage, DEBUG);
        }
        return damage;
    }

    public Optional<String> onRequestPlaceholder(@NotNull OfflinePlayer player, String params) {
        if (params.startsWith("cmd-damage-boost")) {
            return Optional.of(String.valueOf(getNewDamage(player.getUniqueId(), 1)));
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        if (args.size() < 2) return Optional.of(notEnoughArgs + getTemplate());

        ArgumentChecker ac = checkDouble(args.get(0), isFinalVerification, getTemplate());
        if (!ac.isValid()) return Optional.of(ac.getError());

        ArgumentChecker ac2 = checkInteger(args.get(1), isFinalVerification, getTemplate());
        if (!ac2.isValid()) return Optional.of(ac2.getError());

        return Optional.empty();
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("DAMAGE_BOOST");
        return names;
    }

    @Override
    public String getTemplate() {
        return "DAMAGE_BOOST {modification in percentage example 100} {timeinticks}";
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
