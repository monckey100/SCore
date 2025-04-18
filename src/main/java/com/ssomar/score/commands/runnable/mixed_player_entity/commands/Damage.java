package com.ssomar.score.commands.runnable.mixed_player_entity.commands;

import com.ssomar.score.SCore;
import com.ssomar.score.commands.runnable.ActionInfo;
import com.ssomar.score.commands.runnable.ArgumentChecker;
import com.ssomar.score.commands.runnable.SCommandToExec;
import com.ssomar.score.commands.runnable.mixed_player_entity.MixedCommand;
import com.ssomar.score.usedapi.WorldGuardAPI;
import com.ssomar.score.utils.backward_compatibility.AttributeUtils;
import com.ssomar.score.utils.numbers.NTools;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Damage extends MixedCommand {

    public void run(Player p, Entity receiver, SCommandToExec sCommandToExec) {
        List<String> args = sCommandToExec.getOtherArgs();
        ActionInfo aInfo = sCommandToExec.getActionInfo();

        if(!(receiver instanceof LivingEntity)) return;
        LivingEntity livingReceiver = (LivingEntity) receiver;

        /* When target a NPC it can occurs */
        if (receiver == null) return;

        double damage = getDamage(p, livingReceiver, args, aInfo);

        Object damageSource = null;
        if(SCore.is1v20v5Plus()) {
            DamageType damageType = DamageType.INDIRECT_MAGIC;
            // To display the good death message
            if(p != null) damageType = DamageType.PLAYER_ATTACK;
            try {
                damageType = Registry.DAMAGE_TYPE.get(NamespacedKey.minecraft(args.get(3).toLowerCase()));
            } catch (Exception e) {}

            try {
                if (p != null) damageSource = DamageSource.builder(damageType).withDirectEntity(p).withCausingEntity(p).build();
                else damageSource = DamageSource.builder(damageType).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        damage(p, livingReceiver, damage, damageSource, aInfo);
    }

    public static void damage(Player p, LivingEntity receiver, double damage, Object damageSource, ActionInfo actionInfo) {

        if (damage > 0 && !receiver.isDead()) {
            int maximumNoDmg = receiver.getNoDamageTicks();
            receiver.setNoDamageTicks(0);
            boolean doDamage = true;
            if (SCore.hasWorldGuard && receiver instanceof Player) doDamage = WorldGuardAPI.isInPvpZone((Player) receiver, receiver.getLocation());
            // to prevent double kill https://discord.com/channels/701066025516531753/1301172655616950294/1301172655616950294
            if(receiver.isDead()) return;
            if (doDamage) {
                if (p != null) {
                    /* To avoid looping damage */
                    if(actionInfo.isActionRelatedToDamageEvent()) p.setMetadata("cancelDamageEvent", (MetadataValue) new FixedMetadataValue((Plugin) SCore.plugin, Integer.valueOf(7772)));
                    p.setMetadata("damageFromCustomCommand", (MetadataValue) new FixedMetadataValue((Plugin) SCore.plugin, Integer.valueOf(7773)));
                    if(SCore.is1v20v5Plus() && damageSource != null) receiver.damage(damage, (DamageSource) damageSource);
                    else receiver.damage(damage, p);
                } else {
                    if(SCore.is1v20v5Plus() && damageSource != null) receiver.damage(damage, (DamageSource) damageSource);
                    else receiver.damage(damage);
                }
            }
            if(!receiver.isDead()) receiver.setNoDamageTicks(maximumNoDmg);
        }
    }

    @SuppressWarnings("deprecation")
    public static double getDamage(Player launcher, LivingEntity receiver, List<String> args, ActionInfo actionInfo) {
        double amount;
        String damage = args.get(0);

        boolean potionAmplification = false;
        if (args.size() >= 2) {
            potionAmplification = Boolean.valueOf(args.get(1));
        }
        boolean attributeAmplification = false;
        if (args.size() >= 3) {
            attributeAmplification = Boolean.valueOf(args.get(2));
        }

        /* percentage damage */
        if (damage.contains("%")) {
            String[] decomp = damage.split("%");
            damage = decomp[0];
            damage = damage.trim();
            if (damage.length() == 1) {
                damage = "0" + damage;
            }

            double percentage = damage.equals("100") ? 1 : Double.parseDouble("0." + damage);
            amount = receiver.getMaxHealth() * percentage;
            amount = NTools.reduceDouble(amount, 2);
        } else amount = Double.parseDouble(damage);

        if (launcher != null) {
            if (potionAmplification) {
                PotionEffectType incDamage = SCore.is1v20v5Plus() ? PotionEffectType.STRENGTH : PotionEffectType.getByName("INCREASE_DAMAGE");
                PotionEffect pE = launcher.getPotionEffect(incDamage);
                if (pE != null) {
                    amount = amount + (pE.getAmplifier() + 1) * 3;
                }
            }

            //SsomarDev.testMsg("boost attribute: "+ attributeAmplification);
            if (attributeAmplification) {
                Attribute att = null;
                if(SCore.is1v21v2Plus()) att = Attribute.ATTACK_DAMAGE;
                else att = AttributeUtils.getAttribute("GENERIC_ATTACK_DAMAGE");
                AttributeInstance aI = launcher.getAttribute(att);
                double bonusAmount = 0;
                if (aI != null) {
                    //SsomarDev.testMsg("damage value: "+aI.getValue());
                    for (AttributeModifier aM : aI.getModifiers()) {
                        //SsomarDev.testMsg("passe 2:  "+aM.getOperation());
                        if (aM.getOperation().equals(AttributeModifier.Operation.MULTIPLY_SCALAR_1)) {
                            //SsomarDev.testMsg("passe 3: "+(amount * aM.getAmount())+ " >> "+aM.getAmount());
                            bonusAmount = bonusAmount + amount * aM.getAmount();
                        }
                    }
                }
                //SsomarDev.testMsg("boost attribute bonus: "+ bonusAmount);
                amount = amount + bonusAmount;
            }
        }
        return amount;
    }

    @Override
    public Optional<String> verify(List<String> args, boolean isFinalVerification) {
        return staticVerif(args, isFinalVerification, getTemplate());
    }

    public static Optional<String> staticVerif(List<String> args, boolean isFinalVerification, String template) {
        if (args.size() < 1) return Optional.of(notEnoughArgs + template);

        ArgumentChecker ac = checkDouble(args.get(0), isFinalVerification, template, true);
        if (!ac.isValid()) return Optional.of(ac.getError());

        if (args.size() >= 2) {
            String value = args.get(1);
            ArgumentChecker ac2 = checkBoolean(value, isFinalVerification, template);
            if (!ac2.isValid()) return Optional.of(ac2.getError());
        }

        if (args.size() >= 3) {
            String value = args.get(2);
            ArgumentChecker ac2 = checkBoolean(value, isFinalVerification, template);
            if (!ac2.isValid()) return Optional.of(ac2.getError());
        }

        return Optional.empty();
    }

    @Override
    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        names.add("DAMAGE");
        return names;
    }

    @Override
    public String getTemplate() {
        return "DAMAGE {number} [amplified If Strength Effect, true or false] [amplified with attack attribute, true or false] [damageType]";
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