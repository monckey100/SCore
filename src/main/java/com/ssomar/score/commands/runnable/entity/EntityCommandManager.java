package com.ssomar.score.commands.runnable.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.entity.commands.*;
import org.bukkit.ChatColor;

import com.ssomar.score.commands.runnable.CommandManager;
import com.ssomar.score.commands.runnable.SCommand;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringConverter;


public class EntityCommandManager implements CommandManager{

	private static EntityCommandManager instance;
	
	private List<EntityCommand> commands;
	
	private EntityCommandManager() {
		commands = new ArrayList<>();
		commands.add(new TeleportPosition());
		commands.add(new TeleportEntityToPlayer());
		commands.add(new TeleportPlayerToEntity());
		commands.add(new SendMessage());
		commands.add(new Kill());
		commands.add(new ChangeTo());
		commands.add(new DropItem());
		commands.add(new DropExecutableItem());
		commands.add(new Heal());
		commands.add(new Damage());
		commands.add(new SetBaby());
		commands.add(new SetAdult());
		commands.add(new SetName());
		commands.add(new Burn());
		commands.add(new BackDash());
		commands.add(new CustomDash1());
		commands.add(new SetGlow());
		commands.add(new Around());
		commands.add(new MobAround());
		commands.add(new RemoveGlow());
		commands.add(new StrikeLightning());
		commands.add(new StunEnable());
		commands.add(new StunDisable());
		commands.add(new StunDisable());
		commands.add(new PlayerRideOnEntity());
		if(!SCore.is1v11Less()) {
			commands.add(new ParticleCommand());
		}
	}

	/*
	 *  return "" if no error else return the error
	 */
	public String verifArgs(EntityCommand eC, List<String> args) {
		return eC.verify(args);
	}


	public boolean isValidEntityCommand(String entry) {
		for(EntityCommand cmd : commands) {
			for(String name: cmd.getNames()) {
				if(entry.toUpperCase().startsWith(name.toUpperCase())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<String> getEntityCommands(SPlugin sPlugin, List<String> commands, List<String> errorList, String id) {

		List<String> result = new ArrayList<>();


		for (String s : commands) {

			String command = StringConverter.coloredString(s);

			/*
			 * if (command.contains("\\{")) command= command.replaceAll("\\{", ""); if
			 * (command.contains("\\}")) command= command.replaceAll("\\}", "");
			 */

			if (EntityCommandManager.getInstance().isValidEntityCommand(s) && !s.contains("//") && !s.contains("+++")) {
				EntityCommand eC = (EntityCommand) this.getCommand(command);
				List<String> args = this.getArgs(command);

				String error = "";
				if (!(error = this.verifArgs(eC, args)).isEmpty()) {
					errorList.add(sPlugin + " " + error + " for item: " + id);
					continue;
				}
			}
			result.add(command);
		}
		return result;
	}
	
	public static EntityCommandManager getInstance() {
		if(instance == null) instance = new EntityCommandManager();
		return instance;
	}

	public List<EntityCommand> getCommands() {
		return commands;
	}
	
	public Map<String, String> getCommandsDisplay() {
		Map<String, String> result = new HashMap<>();
		for(SCommand c : this.commands) {

			ChatColor extra = c.getExtraColor();
			if(extra == null) extra = ChatColor.DARK_PURPLE;

			ChatColor color = c.getColor();
			if(color == null) color = ChatColor.LIGHT_PURPLE;

			result.put(extra+"["+color+"&l"+c.getNames().get(0)+extra+"]", c.getTemplate());
		}
		return result;
	}

	public void setCommands(List<EntityCommand> commands) {
		this.commands = commands;
	}

	@Override
	public SCommand getCommand(String brutCommand) {
		for(EntityCommand cmd : commands) {
			for(String name: cmd.getNames()) {
				if(brutCommand.toUpperCase().startsWith(name.toUpperCase())) {
					return cmd;
				}
			}
		}
		return null;
	}

	@Override
	public List<String> getArgs(String command) {
		List<String> args = new ArrayList<>();
		boolean first= true;
		boolean second= false;
		boolean third= false;
		if(command.toUpperCase().startsWith("TELEPORT POSITION")) second=true;
		else if(command.toUpperCase().startsWith("TELEPORT ENTITY TO PLAYER")
				|| command.toUpperCase().startsWith("TELEPORT PLAYER TO ENTITY")) {
			second=true;
			third=true;
		}
		for(String s : command.split(" ")) {
			if(first) {
				first=false;
				continue;
			}
			if(second) {
				second=false;
				continue;
			}
			if(third) {
				third=false;
				continue;
			}
			args.add(s);
		}
		return args;
	}

}
