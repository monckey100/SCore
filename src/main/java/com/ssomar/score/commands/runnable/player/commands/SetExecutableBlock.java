package com.ssomar.score.commands.runnable.player.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ssomar.executableblocks.blocks.ExecutableBlock;
import com.ssomar.score.usedapi.MultiverseAPI;
import com.ssomar.score.utils.safeplace.SafePlace;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.ssomar.executableblocks.ExecutableBlocks;
import com.ssomar.executableblocks.blocks.ExecutableBlockManager;
import com.ssomar.executableblocks.blocks.placedblocks.ExecutableBlockPlacedManager;
import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.player.PlayerCommand;

public class SetExecutableBlock extends PlayerCommand{

	@Override
	public String verify(List<String> args) {
		String error = "";

		String setEB = this.getTemplate();

		if(args.size()>7) {
			error = tooManyArgs+setEB;
			return error;
		}
		else if(args.size()<6) {
			error = notEnoughArgs+setEB;
		}
		else {

			/* Ne pas verif car ca peut bloquer si on veut poser le block qui est associé à l'activator */
			
			/*
			 if(SCore.hasExecutableBlocks) {
				if(!ExecutableBlockManager.getInstance().containsBlockWithID(args.get(0))) {
					error = "There is no ExecutableBlock associate with the ID: "+args.get(0)+" for the command"+setEB;
					return error;
				}
			}
			else {
				error = "You must have ExecutableBlock for the command"+setEB;
				return error;
			}
			*/

			if(!args.get(1).contains("%")) {
				try {
					Double.valueOf(args.get(1));
				}catch(Exception e) {
					error = invalidCoordinate+args.get(1)+" for command: "+setEB;
					return error;
				}
			}
			if(!args.get(2).contains("%")) {
				try {
					Double.valueOf(args.get(2));
				}catch(Exception e) {
					error = invalidCoordinate+args.get(2)+" for command: "+setEB;
					return error;
				}
			}
			if(!args.get(3).contains("%")) {
				try {
					Double.valueOf(args.get(3));
				}catch(Exception e) {
					error = invalidCoordinate+args.get(3)+" for command: "+setEB;
					return error;
				}
			}

			String worldStr = args.get(4);
			if(!args.get(4).contains("%")) {
				if(worldStr.isEmpty()) return invalidWorld+args.get(4)+" for the command: "+setEB;
				else {
					if(SCore.hasMultiverse) {
						if(MultiverseAPI.getWorld(worldStr) == null) {
							return invalidWorld+args.get(4)+" for the command: "+setEB;
						}
					}
					else {
						if(Bukkit.getWorld(worldStr) == null) return invalidWorld+args.get(4)+" for the command: "+setEB;
					}
				}	
			}

			try {
				Boolean.valueOf(args.get(5));
			}
			catch(Exception e) {
				return invalidBoolean+args.get(5)+" for the command: "+setEB;
			}
		}

		return error;
	}

	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add("SETEXECUTABLEBLOCK");
		return names;
	}

	@Override
	public String getTemplate() {
		return "SETEXECUTABLEBLOCK {id} {x} {y} {z} {world} {replace true or false} [ownerUUID]";
	}

	@Override
	public ChatColor getColor() {
		return null;
	}

	@Override
	public ChatColor getExtraColor() {
		return null;
	}

	@Override
	public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {
		if(SCore.hasExecutableBlocks) {

			Optional<ExecutableBlock> oOpt = ExecutableBlockManager.getInstance().getLoadedObjectWithID(args.get(0));
			if(!oOpt.isPresent()) {
				ExecutableBlocks.plugin.getLogger().severe("There is no ExecutableBlock associate with the ID: "+args.get(0)+" for the command SETEXECUTABLEBLOCK (object: "+aInfo.getName()+")");
				return;
			}

			double x;
			double y;
			double z;
			try {
				x = Double.parseDouble(args.get(1));
			}catch(Exception e) {
				return ;
			}

			try {
				y = Double.parseDouble(args.get(2));
			}catch(Exception e) {

				return;
			}

			try {
				z = Double.parseDouble(args.get(3));
			}catch(Exception e) {
				return;
			}

			World world = null;
			String worldStr = args.get(4);
			if(worldStr.isEmpty()) return;
			else {
				if(SCore.hasMultiverse) {
					world = MultiverseAPI.getWorld(worldStr);
				}
				else {
					if((world = Bukkit.getWorld(worldStr)) == null) return;
				}
			}	

			boolean replace = false;
			try {
				replace = Boolean.parseBoolean(args.get(5));
			}
			catch(Exception ignored) {}

			UUID ownerUUID = null;
			try {
				ownerUUID = UUID.fromString(args.get(6));
			}
			catch(Exception ignored) {}

			Location loc = new Location(world, x, y , z);

			if(!replace && !loc.getBlock().isEmpty()) return;

			UUID uuid = null;
			if(p != null) uuid = p.getUniqueId();

			if(uuid != null && !SafePlace.verifSafePlace(uuid, loc.getBlock())) return;

			ExecutableBlock eB = oOpt.get();

			eB.place(Optional.ofNullable(Bukkit.getPlayer(ownerUUID)), loc, true);
		}
	}

}
