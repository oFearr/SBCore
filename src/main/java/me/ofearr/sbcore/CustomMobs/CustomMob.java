package me.ofearr.sbcore.CustomMobs;

import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Location;


public interface CustomMob {
    NPCRegistry registry();
    String mobID();
    void spawn(Location location);

    void setRegistered();
}
