package com.ssomar.score.commands.runnable.entity.commands;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.entity.EntityCommandTemplate;
import com.ssomar.score.utils.StringConverter;

/* SETNAME {name} */
public class SetName extends EntityCommandTemplate{

	@Override
	public void run(Player p, Entity entity, List<String> args, ActionInfo aInfo, boolean silenceOutput) {
		if(!entity.isDead()) {
			String name ="";
			for(String s: args) {
				name= name+s+" ";
			}
			name = name.substring(0, name.length()-1);
			try {
				entity.setCustomName(StringConverter.coloredString(name));
			}catch(Exception e) {}
		}
	}

	@Override
	public String verify(List<String> args) {
		String error = "";
		
		String setname= "SETNAME {name}";
		if(args.size()<1) error = notEnoughArgs+setname;
		
		return error;
	}

}