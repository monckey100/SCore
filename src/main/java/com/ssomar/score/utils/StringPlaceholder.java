package com.ssomar.score.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.ssomar.score.SCore;

import me.clip.placeholderapi.PlaceholderAPI;

public class StringPlaceholder {

	/* placeholders of the player */
	private String player= "";
	private String playerUUID="";
	private String x="";
	private String y="";
	private String z="";
	private String world="";
	private String slot="";
	
	/* placeholders of the item */
	private String activator= "";
	private String item= "";
	private String quantity= "";
	private String usage="";
	private String maxUsePerDayItem= "";
	private String maxUsePerDayActivator= "";
	
	/* placeholders of the target entity */
	private String entityUUID="";
	private String entity="";
	private String entityX="";
	private String entityY="";
	private String entityZ="";
	
	/* placeholders of the target player */
	private String targetUUID="";
	private String target= "";
	
	/* placeholders of the target block */
	private String block= "";
	private String blockX= "";
	private String blockY= "";
	private String blockZ= "";
	private String blockXInt= "";
	private String blockYInt= "";
	private String blockZInt= "";
	
	/* placeholders tools */
	private String launcher="";
	private String blockface= "";
	
	/* placeholders of the cooldown */
	private String cooldown= "";
	private String time= "";
	
	/* placeholders of the projectile */
	private String projectileX="";
	private String projectileY="";
	private String projectileZ="";

	public String replacePlaceholder(String str) {
		String s = str;
		if(this.hasPlayer()) {
			s=s.replaceAll("%player%", this.getPlayer());
		}
		if(this.hasActivator()) {
			s=s.replaceAll("%activator%", this.getActivator());
		}
		if(this.hasItem()) {
			s=s.replaceAll("%item%", this.getItem());
		}
		if(this.hasQuantity()) {
			s=s.replaceAll("%quantity%", this.getQuantity());
		}
		if(this.hasCoolodwn()) {
			s=s.replaceAll("%cooldown%", this.getCooldown());
		}
		if(this.hasBlock()) {
			s=s.replaceAll("%block%", this.getBlock());
		}
		if(this.hasTarget()) {
			s=s.replaceAll("%target%", this.getTarget());
		}
		if(this.hasTime()) {
			s=s.replaceAll("%time%", this.getTime());
		}
		if(this.hasEntityUUID()) {
			s=s.replaceAll("%entity_uuid%", this.getEntityUUID());
		}
		if(this.hasPlayerUUID()) {
			s=s.replaceAll("%player_uuid%", this.getPlayerUUID());
		}
		if(this.hasTargetUUID()) {
			s=s.replaceAll("%target_uuid%", this.getTarget());
		}
		if(this.hasSlot()) {
			s=s.replaceAll("%slot%", this.getSlot());
		}
		if(this.hasEntity()) {
			s=s.replaceAll("%entity%", this.getEntity().replaceAll("%monster%", this.getEntity()));
		}
		if(this.hasEntityX()) {
			s=this.replaceCalculPlaceholder(s, "%entity_x%", entityX);
		}
		if(this.hasEntityY()) {
			s=this.replaceCalculPlaceholder(s, "%entity_y%", entityY);
		}
		if(this.hasEntityZ()) {
			s=this.replaceCalculPlaceholder(s, "%entity_z%", entityZ);
		}
		if(this.hasX()) {
			s=this.replaceCalculPlaceholder(s, "%x%", x);
		}
		if(this.hasY()) {
			s=this.replaceCalculPlaceholder(s, "%y%", y);
		}
		if(this.hasZ()) {
			s=this.replaceCalculPlaceholder(s, "%z%", z);
		}
		if(this.hasWorld()) {
			s=s.replaceAll("%world%", this.getZ());
		}
		if(this.hasBlockFace()) {
			s=s.replaceAll("%blockface%", this.getBlockface());
		}
		if(this.hasUsage()) {
			s=this.replaceCalculPlaceholder(s, "%usage%", usage);
		}
		if(this.hasProjectileX()) {
			s=this.replaceCalculPlaceholder(s, "%projectile_x%", this.getProjectileX());
		}
		if(this.hasProjectileY()) {
			s=this.replaceCalculPlaceholder(s, "%projectile_y%", this.getProjectileY());
		}
		if(this.hasProjectileZ()) {
			s=this.replaceCalculPlaceholder(s, "%projectile_z%", this.getProjectileZ());
		}
		if(this.hasBlockX()) {
			s=this.replaceCalculPlaceholder(s, "%block_x%", this.getBlockX());
		}
		if(this.hasBlockY()) {
			s=this.replaceCalculPlaceholder(s, "%block_y%", this.getBlockY());
		}
		if(this.hasBlockZ()) {
			s=this.replaceCalculPlaceholder(s, "%block_z%", this.getBlockZ());
		}
		if(this.hasBlockXInt()) {
			s=this.replaceCalculPlaceholder(s, "%block_x_int%", this.getBlockXInt());
		}
		if(this.hasBlockYInt()) {
			s=this.replaceCalculPlaceholder(s, "%block_y_int%", this.getBlockYInt());
		}
		if(this.hasBlockZInt()) {
			s=this.replaceCalculPlaceholder(s, "%block_z_int%", this.getBlockZInt());
		}
		if(this.hasMaxUsePerDayActivator()) {
			s=s.replaceAll("%max_use_per_day_activator%", this.getMaxUsePerDayActivator());
		}
		if(this.hasMaxUsePerDayItem()) {
			s=s.replaceAll("%max_use_per_day_item%", this.getMaxUsePerDayItem());
		}
		return replacePlaceholderOfPAPI(s);
	}

	public String replacePlaceholderOfPAPI(String s) {
		String replace= s;
		if(this.hasPlayerUUID()) {
			Player p;
			//SsomarDev.testMsg("REPLACE PLACE 2 : "+((p = Bukkit.getPlayer(UUID.fromString(playerUUID)))!=null)+ " &&&&&&& "+ExecutableItems.hasPlaceholderAPI());
			if((p = Bukkit.getPlayer(UUID.fromString(playerUUID)))!=null && SCore.hasPlaceholderAPI) replace = PlaceholderAPI.setPlaceholders(p, replace);
		}
		return replace;
	}

	public static boolean isNumeric(String strNum) {
		if (strNum == null)
			return false; 
		try {
			@SuppressWarnings("unused")
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		} 
		return true;
	}
	
	public String replaceCalculPlaceholder(String s, String placeholder, String value) {
		
		String result = s;
		
		if (result.contains(placeholder+"+")) {
			String suit = result.split(placeholder+"\\+")[1];
			StringBuilder sb = new StringBuilder();
			for (char c : suit.toCharArray()) {
				if (c == ' ')
					break; 
				sb.append(c);
			} 
			if (isNumeric(sb.toString())) {
				int d = (int) (Double.parseDouble(sb.toString()) + Double.valueOf(value));
				result = result.replaceAll(placeholder+"\\+" + sb.toString(), "" + d);
			} else {
				result = result.replaceAll(placeholder+"\\+" + sb.toString(), value);
			} 
		}
		else if (result.contains(placeholder+"-")) {
			String suit = result.split(placeholder+"\\-")[1];
			StringBuilder sb = new StringBuilder();
			for (char c : suit.toCharArray()) {
				if (c == ' ')
					break; 
				sb.append(c);
			} 
			if (isNumeric(sb.toString())) {
				int d = (int) (Double.valueOf(value) - Double.parseDouble(sb.toString()));
				result = result.replaceAll(placeholder+"\\-" + sb.toString(), "" + d);
			} else {
				result = result.replaceAll(placeholder+"\\-" + sb.toString(), value);
			} 
		}
		else if (result.contains(placeholder)) {
			result = result.replaceAll(placeholder, value);
		} 
		return result;
	}


	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public boolean hasPlayer() {
		return player.length()!=0;
	}
	public String getLauncher() {
		return launcher;
	}
	public void setLauncher(String launcher) {
		this.launcher = launcher;
	}
	public boolean hasLauncher() {
		return launcher.length()!=0;
	}
	public String getActivator() {
		return activator;
	}
	public void setActivator(String activator) {
		this.activator = activator;
	}
	public boolean hasActivator() {
		return activator.length()!=0;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public boolean hasItem() {
		return item.length()!=0;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public boolean hasQuantity() {
		return quantity.length()!=0;
	}
	public String getCooldown() {
		return cooldown;
	}
	public void setCooldown(String cooldown) {
		this.cooldown = cooldown;
	}
	public boolean hasCoolodwn() {
		return cooldown.length()!=0;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public boolean hasBlock() {
		return block.length()!=0;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public boolean hasTarget() {
		return target.length()!=0;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public boolean hasTime() {
		return time.length()!=0;
	}

	public String getEntityUUID() {
		return entityUUID;
	}
	public void setEntityUUID(String entityUUID) {
		this.entityUUID = entityUUID;
	}
	public boolean hasEntityUUID() {
		return this.entityUUID.length()!=0;
	}
	public String getPlayerUUID() {
		return playerUUID;
	}
	public void setPlayerUUID(String playerUUID) {
		this.playerUUID = playerUUID;
	}
	public boolean hasPlayerUUID() {
		return this.playerUUID.length()!=0;
	}
	public String getTargetUUID() {
		return targetUUID;
	}
	public void setTargetUUID(String targetUUID) {
		this.targetUUID = targetUUID;
	}
	public boolean hasTargetUUID() {
		return this.targetUUID.length()!=0;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	public boolean hasSlot() {
		return this.slot.length()!=0;
	}
	public String getEntity() {
		return entity;
	}
	public void setEntity(String entity) {
		this.entity = entity;
	}
	public boolean hasEntity() {
		return this.entity.length()!=0;
	}
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public boolean hasX() {
		return this.x.length()!=0;
	}
	public String getY() {
		return y;
	}
	public void setY(String y) {
		this.y = y;
	}
	public boolean hasY() {
		return this.y.length()!=0;
	}
	public String getZ() {
		return z;
	}
	public void setZ(String z) {
		this.z = z;
	}
	public boolean hasZ() {
		return this.z.length()!=0;
	}
	public String getWorld() {
		return world;
	}
	public void setWorld(String world) {
		this.world = world;
	}
	public boolean hasWorld() {
		return this.world.length()!=0;
	}
	public String getBlockface() {
		return blockface;
	}
	public void setBlockface(String blockface) {
		this.blockface = blockface;
	}
	public boolean hasBlockFace() {
		return this.blockface.length()!=0;
	}

	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public boolean hasUsage() {
		return this.usage.length()!=0;
	}

	public String getProjectileX() {
		return projectileX;
	}

	public void setProjectileX(String projectileX) {
		this.projectileX = projectileX;
	}

	public boolean hasProjectileX() {
		return projectileX.length()!=0;
	}

	public String getProjectileY() {
		return projectileY;
	}

	public void setProjectileY(String projectileY) {
		this.projectileY = projectileY;
	}

	public boolean hasProjectileY() {
		return projectileY.length()!=0;
	}

	public String getProjectileZ() {
		return projectileZ;
	}

	public void setProjectileZ(String projectileZ) {
		this.projectileZ = projectileZ;
	}

	public boolean hasProjectileZ() {
		return projectileZ.length()!=0;
	}
	
	public void setBlockX(String BlockX) {
		this.blockX = BlockX;
	}
	
	public String getBlockX() {
		return blockX;
	}

	public boolean hasBlockX() {
		return blockX.length()!=0;
	}

	public String getBlockY() {
		return blockY;
	}

	public void setBlockY(String BlockY) {
		this.blockY = BlockY;
	}

	public boolean hasBlockY() {
		return blockY.length()!=0;
	}

	public String getBlockZ() {
		return blockZ;
	}

	public void setBlockZ(String BlockZ) {
		this.blockZ = BlockZ;
	}

	public boolean hasBlockZ() {
		return blockZ.length()!=0;
	}

	public String getMaxUsePerDayItem() {
		return maxUsePerDayItem;
	}

	public void setMaxUsePerDayItem(String maxUsePerDayItem) {
		this.maxUsePerDayItem = maxUsePerDayItem;
	}
	
	public boolean hasMaxUsePerDayItem() {
		return maxUsePerDayItem.length()!=0;
	}

	public String getMaxUsePerDayActivator() {
		return maxUsePerDayActivator;
	}

	public void setMaxUsePerDayActivator(String maxUsePerDayActivator) {
		this.maxUsePerDayActivator = maxUsePerDayActivator;
	}
	
	public boolean hasMaxUsePerDayActivator() {
		return maxUsePerDayActivator.length()!=0;
	}

	public boolean hasEntityX() {
		return entityX.length()!=0;
	}
	
	public String getEntityX() {
		return entityX;
	}

	public void setEntityX(String entityX) {
		this.entityX = entityX;
	}
	
	public boolean hasEntityY() {
		return entityY.length()!=0;
	}

	public String getEntityY() {
		return entityY;
	}

	public void setEntityY(String entityY) {
		this.entityY = entityY;
	}

	public boolean hasEntityZ() {
		return entityZ.length()!=0;
	}
	
	public String getEntityZ() {
		return entityZ;
	}

	public void setEntityZ(String entityZ) {
		this.entityZ = entityZ;
	}
	
	public boolean hasBlockXInt() {
		return blockXInt.length()!=0;
	}

	public String getBlockXInt() {
		return blockXInt;
	}

	public void setBlockXInt(String blockXInt) {
		this.blockXInt = blockXInt;
	}
	
	public boolean hasBlockYInt() {
		return blockXInt.length()!=0;
	}

	public String getBlockYInt() {
		return blockYInt;
	}

	public void setBlockYInt(String blockYInt) {
		this.blockYInt = blockYInt;
	}
	
	public boolean hasBlockZInt() {
		return blockXInt.length()!=0;
	}

	public String getBlockZInt() {
		return blockZInt;
	}

	public void setBlockZInt(String blockZInt) {
		this.blockZInt = blockZInt;
	}
		
	
}