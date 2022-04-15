package com.ssomar.score.menu.conditions.placeholdercdt;

import com.ssomar.score.conditions.condition.placeholders.PlaceholdersCondition;
import com.ssomar.score.linkedplugins.LinkedPlugins;
import com.ssomar.score.menu.conditions.general.ConditionsGUIManager;
import com.ssomar.score.menu.conditions.home.ConditionsHomeGUIManager;
import com.ssomar.score.sobject.SObject;
import com.ssomar.score.sobject.sactivator.SActivator;
import com.ssomar.score.splugin.SPlugin;
import com.ssomar.score.utils.StringConverter;
import org.bukkit.entity.Player;

import com.ssomar.score.menu.score.GUIManagerSCore;
import com.ssomar.score.menu.score.InteractionClickedGUIManager;

import java.util.List;


public class PlaceholdersConditionsGUIManager extends GUIManagerSCore<PlaceholdersConditionsGUI>{

	private static PlaceholdersConditionsGUIManager instance;

	public void startEditing(Player p, SPlugin sPlugin, SObject sObject, SActivator sActivator, List<PlaceholdersCondition> list, String detail) {
		cache.put(p, new PlaceholdersConditionsGUI(sPlugin, sObject, sActivator, list, detail));
		cache.get(p).openGUISync(p);
	}

	public static PlaceholdersConditionsGUIManager getInstance() {
		if(instance == null) instance = new PlaceholdersConditionsGUIManager();
		return instance;
	}

	@Override
	public boolean allClicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> i) {
		String cPage  = StringConverter.decoloredString(i.title);
		
		if (i.name.contains("Next page")) {
			cache.replace(i.player, new PlaceholdersConditionsGUI(Integer.parseInt(cPage.split("Page ")[1]) + 1, i.sPlugin, i.sObject, i.sActivator, cache.get(i.player).getList(), cache.get(i.player).getDetail()));
			cache.get(i.player).openGUISync(i.player);
		} else if (i.name.contains("Previous page")) {
			cache.replace(i.player, new PlaceholdersConditionsGUI(Integer.parseInt(cPage.split("Page ")[1]) - 1, i.sPlugin, i.sObject, i.sActivator, cache.get(i.player).getList(), cache.get(i.player).getDetail()));
			cache.get(i.player).openGUISync(i.player);
		}
		else if (i.name.contains("New Placeholders cdt")) {
			i.player.closeInventory();
			PlaceholdersConditionGUIManager.getInstance().startEditing(i.player, i.sPlugin, i.sObject, i.sActivator, cache.get(i.player).getList(), cache.get(i.player).getDetail());
		} 
		else if(i.name.contains("Back")) {
			ConditionsHomeGUIManager.getInstance().startEditing(i.player, i.sPlugin, i.sObject, i.sActivator);
		}
		else if(!i.name.isEmpty()){
			PlaceholdersCondition pC = null;
			for (PlaceholdersCondition place :  cache.get(i.player).getList()) {
				if (place.getId().equals(StringConverter.decoloredString(i.name).split("✦ ID: ")[1]))
					pC = place;
			}
			if (pC != null)
				PlaceholdersConditionGUIManager.getInstance().startEditing(i.player, i.sPlugin, i.sObject, i.sActivator, pC, cache.get(i.player).getDetail());
			else {
				i.player.sendMessage(StringConverter.coloredString(
						"&4&l"+i.sPlugin.getNameDesign()+" &cCan't load this placeholder cdt, pls contact the developper on discord if you see this message"));
				PlaceholdersConditionGUIManager.getInstance().startEditing(i.player, i.sPlugin, i.sObject, i.sActivator, cache.get(i.player).getList(), cache.get(i.player).getDetail());
			}
		}

		cache.remove(i.player);
		return true;
	}

	@Override
	public boolean noShiftclicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> i) {
		return false;
	}

	@Override
	public boolean noShiftLeftclicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> i) {
		return false;
	}

	@Override
	public boolean noShiftRightclicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> i) {
		return false;
	}

	@Override
	public boolean shiftClicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> i) {
		return false;
	}

	@Override
	public boolean shiftLeftClicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> i) {
		String cPage  = StringConverter.decoloredString(i.title);
		
		try {
			String id = i.name.split("✦ ID: ")[1];
			PlaceholdersCondition.deletePlaceholdersCdt(i.sPlugin, i.sObject, i.sActivator, id, cache.get(i.player).getDetail());
			LinkedPlugins.reloadSObject(i.sPlugin, i.sObject.getId());
			i.sObject = LinkedPlugins.getSObject(i.sPlugin, i.sObject.getId());
			i.sActivator = i.sObject.getActivator(i.sActivator.getID());
			cache.replace(i.player, new PlaceholdersConditionsGUI(Integer.parseInt(cPage.split("Page ")[1]), i.sPlugin, i.sObject, i.sActivator, i.sActivator.getPlaceholdersConditions(), cache.get(i.player).getDetail()));
			cache.get(i.player).openGUISync(i.player);
			return true;
		}
		catch(Exception ignored) {

		}
		return false;
	}

	@Override
	public boolean shiftRightClicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> i) {
		return false;
	}

	@Override
	public boolean leftClicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> i) {
		return false;
	}

	@Override
	public boolean rightClicked(InteractionClickedGUIManager<PlaceholdersConditionsGUI> interact) {
		return false;
	}

	@Override
	public void saveTheConfiguration(Player p) {

	}
}
