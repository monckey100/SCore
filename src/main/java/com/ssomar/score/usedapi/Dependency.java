package com.ssomar.score.usedapi;

import com.ssomar.score.SCore;
import com.ssomar.score.utils.logging.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public enum Dependency {

    EXECUTABLE_ITEMS("ExecutableItems"),

    EXECUTABLE_BLOCKS("ExecutableBlocks"),

    EXECUTABLE_EVENTS("ExecutableEvents"),

    CUSTOM_PIGLINS_TRADES("CustomPiglinsTrades"),

    SPARKOUR("SParkour"),

    PLACEHOLDER_API("PlaceholderAPI"),

    WORLD_GUARD("WorldGuard"),

    VAULT("Vault"),

    IRIDIUM_SKYBLOCK("IridiumSkyblock"),

    SUPERIOR_SKYBLOCK2("SuperiorSkyblock2"),

    BENTO_BOX("BentoBox"),

    MULTIVERSE_CORE("Multiverse-Core"),

    LANDS("Lands"),

    TOWNY("Towny"),

    GRIEF_PREVENTION("GriefPrevention"),

    GRIEF_DEFENDER("GriefDefender"),

    CORE_PROTECT("CoreProtect"),

    FACTIONS_UUID("Factions"),

    PROTOCOL_LIB("ProtocolLib"),
    NBTAPI("NBTAPI"),

    RESIDENCE("Residence"),
    PLOT_SQUARED("PlotSquared"),


    HEAD_DATABASE("HeadDatabase"),

    HEAD_DB("HeadDB"),

   MYTHIC_MOBS("MythicMobs"),

    DECENT_HOLOGRAMS("DecentHolograms"),

    HOLOGRAPHIC_DISPLAYS("HolographicDisplays"),

    CMI("CMI"),

    AURELIUM_SKILLS("AureliumSkills"),

    ITEMS_ADDER("ItemsAdder"),

    ORAXEN("Oraxen"),

    SHOP_GUI_PLUS("ShopGUIPlus"),

    ROSE_LOOT("RoseLoot"),

    ROSE_STACKER("RoseStacker"),

    MMO_CORE("MMOCore"),

    PROTECTION_STONES("ProtectionStones"),

    TAB("TAB"),

    TERRA("Terra"),

    JETS_MINIONS("JetsMinions"),

    ECO_SKILLS("EcoSkills"),

    WILD_STACKER("WildStacker"),

    CUSTOM_CRAFTING("CustomCrafting"),

    WORLD_EDIT("WorldEdit");

    private final String name;

    Dependency(String name) {
        this.name = name;
    }

    public boolean hookSoftDependency() {
        Plugin softDepend;
        if ((softDepend = Bukkit.getPluginManager().getPlugin(name)) != null) {
            String when = " &8&oLoad Before";
            if (!softDepend.isEnabled()) {
                when = "&8&oLoad After";
            }
            Utils.sendConsoleMsg(SCore.NAME_COLOR + " &7" + name + " hooked !  &6(" + softDepend.getDescription().getVersion() + "&6) " + when);
            return true;
        }
        return false;
    }

    public boolean isInstalled() {
        return  Bukkit.getPluginManager().getPlugin(name) != null;
    }

    public boolean isEnabled() {
        Plugin softDepend;
        if ((softDepend = Bukkit.getPluginManager().getPlugin(name)) != null) {
            return softDepend.isEnabled();
        }
        return false;
    }
}
