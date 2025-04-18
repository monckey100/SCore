package com.ssomar.score.features.custom.variables.real;

import com.ssomar.score.SCore;
import com.ssomar.score.SsomarDev;
import com.ssomar.score.features.custom.variables.base.variable.VariableFeature;
import com.ssomar.score.features.custom.variables.update.variable.VariableUpdateFeature;
import com.ssomar.score.utils.DynamicMeta;
import com.ssomar.score.utils.emums.VariableUpdateType;
import com.ssomar.score.utils.placeholders.StringPlaceholder;
import com.ssomar.score.utils.writer.NameSpaceKeyWriterReader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;

public class VariableRealList extends VariableReal<List<String>> implements Serializable {

    private static final boolean DEBUG = false;

    public VariableRealList(VariableFeature<List<String>> config, ItemStack item, @NotNull DynamicMeta dMeta) {
        super(config, item, dMeta);
    }

    public VariableRealList(VariableFeature<List<String>> config, PersistentDataContainer dataContainer) {
        super(config, dataContainer);
    }

    public VariableRealList(VariableFeature<List<String>> config, ConfigurationSection configurationSection) {
        super(config, configurationSection);
    }

    @Override
    public Optional<List<String>> readValue(ItemStack item, DynamicMeta dMeta) {
        getItemKeyWriterReader().writeListIfNull(SCore.plugin, item, dMeta, "SCORE-" + getConfig().getVariableName().getValue().get().toUpperCase(), (List<String>) getConfig().getDefaultValue());
        Optional<List<String>> value;
        value = getItemKeyWriterReader().readList(SCore.plugin, item, dMeta, "SCORE-" + getConfig().getVariableName().getValue().get().toUpperCase());
        return value;
    }

    @Override
    public Optional<List<String>> readValue(PersistentDataContainer dataContainer) {
        NameSpaceKeyWriterReader.writeListIfNull(SCore.plugin,dataContainer, "SCORE-" + getConfig().getVariableName().getValue().get().toUpperCase(), (List<String>) getConfig().getDefaultValue());
        Optional<List<String>> value;
        value = NameSpaceKeyWriterReader.readList(SCore.plugin, dataContainer,"SCORE-" + getConfig().getVariableName().getValue().get().toUpperCase());
        return value;
    }

    @Override
    public Optional<List<String>> readValue(ConfigurationSection configurationSection) {
        String varUpper = getConfig().getVariableName().getValue().get().toUpperCase();
        if (!configurationSection.contains(varUpper)) writeValue(configurationSection);
        return Optional.of(configurationSection.getStringList(varUpper));
    }

    @Override
    public void writeValue(ItemStack item, DynamicMeta dMeta) {
        getItemKeyWriterReader().writeList(SCore.plugin, item, dMeta, "SCORE-" + getConfig().getVariableName().getValue().get().toUpperCase(), getValue());
    }

    @Override
    public void writeValue(PersistentDataContainer dataContainer) {
        NameSpaceKeyWriterReader.writeList(SCore.plugin, dataContainer,"SCORE-" + getConfig().getVariableName().getValue().get().toUpperCase(), getValue());
    }

    @Override
    public void writeValue(ConfigurationSection configurationSection) {
        String varUpper = getConfig().getVariableName().getValue().get().toUpperCase();
        configurationSection.set(varUpper, getValue());
    }

    public void modifVariable(VariableUpdateFeature update, Player p, StringPlaceholder sp) {
        SsomarDev.testMsg("VariableRealList.modifVariable", DEBUG);

        Optional<String> optional = update.getStringUpdate().getValue();
        if (!optional.isPresent()) return;
        String modificationString = optional.get();
        modificationString = sp.replacePlaceholder(modificationString, true);

        if (update.getType().getValue().get().equals(VariableUpdateType.LIST_ADD)) {
            List<String> list = getValue();
            list.add(modificationString);

            setValue(list);
        }
        else if (update.getType().getValue().get().equals(VariableUpdateType.LIST_REMOVE)) {
            List<String> list = getValue();
            list.remove(modificationString);
            setValue(list);
        } else if (update.getType().getValue().get().equals(VariableUpdateType.LIST_CLEAR)) {
            setValue(new ArrayList<>());
        }
        SsomarDev.testMsg("VariableRealList.modifVariable: " + getValue(), DEBUG);
    }

    @Override
    public void modifVariable(ItemStack item, @NotNull DynamicMeta dMeta, VariableUpdateFeature update, @Nullable Player p, @Nullable StringPlaceholder sp) {
        modifVariable(update, p, sp);
        writeValue(item, dMeta);
    }

    @Override
    public void modifVariable(PersistentDataContainer dataContainer, VariableUpdateFeature update, @Nullable Player p, @Nullable StringPlaceholder sp) {
        modifVariable(update, p, sp);
        writeValue(dataContainer);
    }

    @Override
    public void modifVariable(@NotNull ConfigurationSection configurationSection, VariableUpdateFeature update, @Nullable Player p, @Nullable StringPlaceholder sp) {
        modifVariable(update, p, sp);
        writeValue(configurationSection);
    }

    @Override
    public String replaceVariablePlaceholder(String s, boolean includeRefreshTag) {

        boolean isRefreshable = includeRefreshTag && getConfig().getIsRefreshableClean().getValue();
        String optTag = isRefreshable ? getConfig().getRefreshTag().getValue().get() : "";

        String toReplace = "%var_" + getConfig().getVariableName().getValue().get() + "%";
        if (s.contains(toReplace)) {
            while (getValue().contains("")){
                getValue().remove("");
            }
            s = StringPlaceholder.replaceCalculPlaceholder(s, toReplace, optTag + getValue() +  (isRefreshable ? getPlaceholderTag(toReplace) : "") +optTag, false);
        }

        toReplace = "%var_" + getConfig().getVariableName().getValue().get() + "_contains_";
        if (s.contains(toReplace)) {
           String contains = s.split(toReplace)[1];
           if (contains.length() > 0 && contains.charAt(0) != '%'){
               contains = contains.split("%")[0];
                s = s.replace(toReplace+contains+"%", optTag + getValue().contains(contains) + optTag);
                //SsomarDev.testMsg("VariableRealList.replaceVariablePlaceholder:" + s+":", true);
           }
        }

        toReplace = "%var_" + getConfig().getVariableName().getValue().get() + "_size%";
        if (s.contains(toReplace)) {
            s = s.replace(toReplace, optTag + getValue().size() +  (isRefreshable ? getPlaceholderTag(toReplace) : "") +optTag);
        }

        return s;
    }

    @Override
    public String getPlaceholderWithTag(String s) {
        Map<String, String> tags = getTranscoPlaceholders();
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            if (s.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "";
    }

    public Map<String, String> getTranscoPlaceholders() {
        Map<String, String> tags = new HashMap<>();
        tags.put("§汉", "%var_" + getConfig().getVariableName().getValue().get() + "%");
        tags.put("§六", "%var_" + getConfig().getVariableName().getValue().get() + "_size%");
        return tags;
    }

    public String getPlaceholderTag(String placeholder) {
        Map<String, String> tags = getTranscoPlaceholders();
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            if (entry.getValue().equals(placeholder)) {
                return entry.getKey();
            }
        }
        return "";
    }

    @Override
    public String replaceVariablePlaceholder(String s) {
        return replaceVariablePlaceholder(s, false);
    }
}
