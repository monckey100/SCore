package com.ssomar.score.commands.runnable.player.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.player.PlayerCommand;

/* SENDMESSAGE {message} */
public class SendMessage extends PlayerCommand{

	@Override
	public void run(Player p, Player receiver, List<String> args, ActionInfo aInfo) {
		StringBuilder message = new StringBuilder();
		if(args.size() > 0) {
			for (String s : args) {
				//SsomarDev.testMsg("cmdarg> "+s);
				message.append(s).append(" ");
			}
			message = new StringBuilder(message.substring(0, message.length() - 1));
			sm.sendMessage(receiver, message.toString());
		}
	}

	@Override
	public String verify(List<String> args) {
		String error ="";

		String message= "SENDMESSAGE {message}";
		if(args.size()<1) error = notEnoughArgs+message;

		return error;
	}
	
	@Override
	public List<String> getNames() {
		List<String> names = new ArrayList<>();
		names.add("SENDMESSAGE");
		return names;
	}

	@Override
	public String getTemplate() {
		return "SENDMESSAGE {message}";
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
