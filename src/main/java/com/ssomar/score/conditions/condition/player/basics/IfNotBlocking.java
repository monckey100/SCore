package com.ssomar.score.conditions.condition.player.basics;

import com.ssomar.score.conditions.condition.conditiontype.ConditionType;
import com.ssomar.score.conditions.condition.player.PlayerCondition;
import com.ssomar.score.utils.SendMessage;
import org.bukkit.entity.Player;

import java.util.Optional;

public class IfNotBlocking extends PlayerCondition<Boolean, String> {


    public IfNotBlocking() {
        super(ConditionType.BOOLEAN, "ifNotBlocking", "If not blocking", new String[]{}, false, " &cYou must not block damage with shield to active the activator: &6%activator% &cof this item!");
    }

    @Override
    public boolean verifCondition(Player player, Optional<Player> playerOpt, SendMessage messageSender) {
        if (getAllCondition(messageSender.getSp()) && player.isBlocking()) {
            sendErrorMsg(playerOpt, messageSender);
            return false;
        }
        return true;
    }
}
