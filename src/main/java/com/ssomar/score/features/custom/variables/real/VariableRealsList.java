package com.ssomar.score.features.custom.variables.real;

import com.ssomar.score.SsomarDev;
import com.ssomar.score.utils.DynamicMeta;
import com.ssomar.score.utils.placeholders.StringPlaceholder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class VariableRealsList extends ArrayList<VariableReal> {

    private final static boolean DEBUG = false;

    public HashMap<String, String> getFlatValues(){
        HashMap<String, String> flatValues = new HashMap<>();
        for(VariableReal vR : this){
            flatValues.put(vR.getConfig().getVariableName().getValue().get().toUpperCase(), vR.getValue().toString());
        }
        return flatValues;
    }

    public VariableReal getVariableReal(String variableName){
        for(VariableReal vR : this){
            Optional<String> name = vR.getConfig().getVariableName().getValue();
            if (!name.isPresent()) continue;
            if(name.get().equalsIgnoreCase(variableName)) return vR;
        }
        return null;
    }

    public void save(ConfigurationSection configurationSection) {
        for(VariableReal vR : this){
            vR.writeValue(configurationSection);
        }
    }

    public void save(PersistentDataContainer dataContainer) {
        for(VariableReal vR : this){
            vR.writeValue(dataContainer);
        }
    }

    public void buildWithCustomValues(Map<String, String> variables, ConfigurationSection configurationSection, @Nullable UUID owner){
        if(!this.isEmpty()) {
            //SsomarDev.testMsg("NOT EMPTY", true);
            for(String key : variables.keySet()){

                for(VariableReal vR : this){
                    //SsomarDev.testMsg("key: "+key + " vR: "+vR.getConfig().getVariableName().getValue().get(), true);
                    if(vR.getConfig().getVariableName().getValue().get().equalsIgnoreCase(key)){
                        if(vR instanceof VariableRealDouble){
                            VariableRealDouble vRD = (VariableRealDouble) vR;
                            try {
                                vRD.setValue(Double.parseDouble(variables.get(key)));
                            }catch (NumberFormatException ignored){}
                        }
                        else if(vR instanceof VariableRealList){
                            VariableRealList vRD = (VariableRealList) vR;
                            try {
                                String s = variables.get(key);
                                if (s != null) {
                                    vRD.setValue(new ArrayList<>(Arrays.asList(s.substring(1, s.length() - 1).split(", "))));
                                }
                            }catch (NumberFormatException ignored){}
                        }
                        else if(vR instanceof VariableRealString){
                            VariableRealString vRS = (VariableRealString) vR;
                            StringPlaceholder sp = new StringPlaceholder();
                            sp.setPlayerPlcHldr(owner);
                            String value = sp.replacePlaceholder(variables.get(key), true);
                            vRS.setValue(value);
                        }
                        vR.writeValue(configurationSection);
                    }
                }
            }
        }
    }

    public void buildWithCustomValues(Map<String, String> variables, @NotNull ItemStack item, @Nullable UUID owner){

        if(!this.isEmpty()) {
            SsomarDev.testMsg("NOT EMPTY", DEBUG);
            DynamicMeta dynamicMeta = new DynamicMeta(item.getItemMeta());

            for(String key : variables.keySet()){
                SsomarDev.testMsg("KEY: "+key, DEBUG);

                for(VariableReal vR : this){
                    SsomarDev.testMsg("vR: "+vR.getConfig().getVariableName().getValue().get(), DEBUG);
                    if(vR.getConfig().getVariableName().getValue().get().equalsIgnoreCase(key)){
                        SsomarDev.testMsg("FOUND", DEBUG);
                        if(vR instanceof VariableRealDouble){
                            VariableRealDouble vRD = (VariableRealDouble) vR;
                            try {
                                vRD.setValue(Double.parseDouble(variables.get(key)));
                            }catch (NumberFormatException ignored){}
                        }
                        else if(vR instanceof VariableRealList){
                            VariableRealList vRD = (VariableRealList) vR;
                            try {
                                String s = variables.get(key);
                                if (s != null) {
                                    vRD.setValue(new ArrayList<>(Arrays.asList(s.substring(1, s.length() - 1).split(", "))));
                                }
                            }catch (NumberFormatException ignored){}
                        }
                        else if(vR instanceof VariableRealString){
                            VariableRealString vRS = (VariableRealString) vR;
                            StringPlaceholder sp = new StringPlaceholder();
                            sp.setPlayerPlcHldr(owner);
                            String value = sp.replacePlaceholder(variables.get(key), true);
                            vRS.setValue(value);
                        }
                        SsomarDev.testMsg("WRITE VALUE for "+key +">>>>" + vR.toString(), DEBUG);
                        vR.writeValue(item, dynamicMeta);
                    }
                }
            }
            item.setItemMeta(dynamicMeta.getMeta());
        }
        else SsomarDev.testMsg("EMPTY", DEBUG);
    }

    public void buildWithCustomValues(Map<String, String> variables, PersistentDataContainer dataContainer, @Nullable UUID owner){

        if(!this.isEmpty()) {
            SsomarDev.testMsg("NOT EMPTY", DEBUG);

            for(String key : variables.keySet()){

                for(VariableReal vR : this){
                    if(vR.getConfig().getVariableName().getValue().get().equalsIgnoreCase(key)){
                        if(vR instanceof VariableRealDouble){
                            VariableRealDouble vRD = (VariableRealDouble) vR;
                            try {
                                vRD.setValue(Double.parseDouble(variables.get(key)));
                            }catch (NumberFormatException ignored){}
                        }
                        else if(vR instanceof VariableRealList){
                            VariableRealList vRD = (VariableRealList) vR;
                            try {
                                String s = variables.get(key);
                                if (s != null) {
                                    vRD.setValue(new ArrayList<>(Arrays.asList(s.substring(1, s.length() - 1).split(", "))));
                                }
                            }catch (NumberFormatException ignored){}
                        }
                        else if(vR instanceof VariableRealString){
                            VariableRealString vRS = (VariableRealString) vR;
                            StringPlaceholder sp = new StringPlaceholder();
                            sp.setPlayerPlcHldr(owner);
                            String value = sp.replacePlaceholder(variables.get(key), true);
                            vRS.setValue(value);
                        }
                        SsomarDev.testMsg("WRITE VALUE for "+key +">>>>" + vR.toString(), DEBUG);
                        vR.writeValue(dataContainer);
                    }
                }
            }
        }
    }

}
