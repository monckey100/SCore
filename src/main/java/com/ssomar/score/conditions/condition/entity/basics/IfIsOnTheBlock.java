package com.ssomar.score.conditions.condition.entity.basics;

import com.ssomar.score.conditions.condition.conditiontype.ConditionType;
import com.ssomar.score.conditions.condition.entity.EntityCondition;
import com.ssomar.score.conditions.condition.player.PlayerCondition;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.scoretestrecode.features.FeatureParentInterface;
import com.ssomar.scoretestrecode.features.custom.conditions.entity.EntityConditionFeature;
import com.ssomar.scoretestrecode.features.custom.materialwithgroupsandtags.group.MaterialAndTagsGroupFeature;
import com.ssomar.scoretestrecode.features.custom.materialwithgroupsandtags.materialandtags.MaterialAndTagsFeature;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IfIsOnTheBlock extends EntityCondition<List<Material>, List<String>> {


    public IfIsOnTheBlock() {
        super(ConditionType.LIST_MATERIAL, "ifIsOnTheBlock", "If is on the block", new String[]{}, new ArrayList<>(), " &cYou are not on the good type of block to active the activator: &6%activator% &cof this item!");
    }

    @Override
    public boolean verifCondition(Entity entity, Optional<Player> playerOpt, SendMessage messageSender) {
        if (isDefined()) {
            Location pLoc = entity.getLocation();
            pLoc.subtract(0, 0.1, 0);

            Block block = pLoc.getBlock();
            Material type = block.getType();

            if (!getAllCondition(messageSender.getSp()).contains(type)) {
                sendErrorMsg(playerOpt, messageSender);
                return false;
            }
        }
        return true;
    }
}
