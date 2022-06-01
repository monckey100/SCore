package com.ssomar.score.usedapi;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.ssomar.score.SCore;
import org.bukkit.Particle;

import java.lang.reflect.Field;

public class ProtocolibAPI {

    public static void reduceDamageIndicator(){
        if(SCore.hasProtocolLib) {
            try {
                /* LIMIT the particles of damage */
                SCore.protocolManager.addPacketListener(
                        new PacketAdapter(SCore.plugin, ListenerPriority.NORMAL,
                                PacketType.Play.Server.WORLD_PARTICLES) {
                            @Override
                            public void onPacketReceiving(PacketEvent event) {

                            }

                            @Override
                            public void onPacketSending(PacketEvent event) {

                                //SsomarDev.testMsg("Packet sending >> " + event.getPacketType().toString());
                                if (event.getPacketType() == PacketType.Play.Server.WORLD_PARTICLES) {
                                    Particle type = event.getPacket().getNewParticles().read(0).getParticle();
                                    // Item packets (id: 0x29)
                                    if (type.equals(Particle.DAMAGE_INDICATOR)) {
                                        try {
                                            Field privateField = event.getPacket().getHandle().getClass().getDeclaredField("h");

                                            // Set the accessibility as true
                                            privateField.setAccessible(true);
                                            int amount = +privateField.getInt(event.getPacket().getHandle());

                                            if (amount > 10) {
                                                privateField.setInt(event.getPacket().getHandle(), 10);
                                            }
                                        } catch (NoSuchFieldException | IllegalAccessException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }
                        });
            }
            catch (Exception | Error e){}
        }
    }
}
