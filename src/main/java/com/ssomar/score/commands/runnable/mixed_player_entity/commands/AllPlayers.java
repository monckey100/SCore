package com.ssomar.score.commands.runnable.mixed_player_entity.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.SsomarDev;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.CommandsExecutor;
import com.ssomar.score.commands.runnable.mixed_player_entity.MixedCommand;
import com.ssomar.score.commands.runnable.player.PlayerRunCommandsBuilder;
import com.ssomar.score.utils.placeholders.StringPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AllPlayers extends MixedCommand {

    public static void aroundExecution(Entity receiver, List<String> args, ActionInfo aInfo) {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {

                for (Entity e : Bukkit.getOnlinePlayers()) {
                    if (e instanceof Player) {
                        Player target = (Player) e;
                        if (target.hasMetadata("NPC")) continue;

                        ActionInfo aInfo2 = aInfo.clone();
                        aInfo2.setReceiverUUID(target.getUniqueId());

                        StringPlaceholder sp = new StringPlaceholder();
                        sp.setAroundTargetPlayerPlcHldr(target.getUniqueId());

                        /* regroup the last args that correspond to the commands */
                        StringBuilder prepareCommands = new StringBuilder();
                        for (String s : args) {
                            prepareCommands.append(s);
                            prepareCommands.append(" ");
                        }
                        prepareCommands.deleteCharAt(prepareCommands.length() - 1);

                        String buildCommands = prepareCommands.toString();
                        String[] tab;
                        SsomarDev.testMsg(" buildcommands contains : " + (buildCommands.contains("<+>")), true);
                        if (buildCommands.contains("<+>")) tab = buildCommands.split("<\\+>");
                        else {
                            tab = new String[1];
                            tab[0] = buildCommands;
                        }
                        List<String> commands = new ArrayList<>();
                        for (int m = 0; m < tab.length; m++) {
                            String s = tab[m];
                            while (s.startsWith(" ")) {
                                s = s.substring(1);
                            }
                            while (s.endsWith(" ")) {
                                s = s.substring(0, s.length() - 1);
                            }
                            if (s.startsWith("/")) s = s.substring(1);

                            commands.add(s);
                        }
                        commands = sp.replacePlaceholders(commands);
                        PlayerRunCommandsBuilder builder = new PlayerRunCommandsBuilder(commands, aInfo2);
                        CommandsExecutor.runCommands(builder);
                    }
                }

            }
        };
        SCore.schedulerHook.runTask(runnable, 0);
    }

    @Override
    public void run(Player p, LivingEntity receiver, List<String> args, ActionInfo aInfo) {
        aroundExecution(receiver, args, aInfo);
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {

        if (args.size() < 1) return Optional.of(notEnoughArgs + getTemplate());

        return Optional.empty();
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("ALL_PLAYERS");
        return names;
    }

    @Override
    public String getTemplate() {
        return "ALL_PLAYERS {Your commands here}";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public ChatColor getExtraColor() {
        return ChatColor.DARK_PURPLE;
    }

}