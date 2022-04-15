package com.ssomar.score.conditions.condition.player.basics;

import com.ssomar.score.conditions.condition.conditiontype.ConditionType;
import com.ssomar.score.conditions.condition.player.PlayerCondition;
import com.ssomar.score.utils.SendMessage;
import com.ssomar.score.utils.StringCalculation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Optional;

public class IfCursorDistance extends PlayerCondition<String, String> {


    public IfCursorDistance() {
        super(ConditionType.NUMBER_CONDITION, "ifCursorDistance", "If cursor distance", new String[]{}, "",  " &cCursor distance is not valid to active the activator: &6%activator% &cof this item!");
    }

    @Override
    public boolean verifCondition(Player player, Optional<Player> playerOpt, SendMessage messageSender) {
        if (isDefined()){
            Block block = player.getTargetBlock(null, 200);
            if(block.getType().equals(Material.AIR)) return false;

            if(!StringCalculation.calculation(getAllCondition(messageSender.getSp()), player.getLocation().distance(block.getLocation()))){
                sendErrorMsg(playerOpt, messageSender);
                return false;
            }
        }
        return true;
    }
}
