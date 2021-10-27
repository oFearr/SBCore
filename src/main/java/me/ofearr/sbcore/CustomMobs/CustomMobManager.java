package me.ofearr.sbcore.CustomMobs;

import me.ofearr.sbcore.CustomMobs.Mobs.IceWalker;
import me.ofearr.sbcore.SBCore;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.MemoryNPCDataStore;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class CustomMobManager {

    private static SBCore plugin = SBCore.plugin;

    private static final NPCRegistry registry =  CitizensAPI.createAnonymousNPCRegistry(new MemoryNPCDataStore());

    private static HashMap<String, CustomMob> registeredCustomMobs = new HashMap<>();

    public static void setRegisteredCustomMobs(){
        new IceWalker().setRegistered();

        startMobSpawns();
    }

    public static void putCustomMobToMap(String mobID, CustomMob customMob){
        registeredCustomMobs.put(mobID, customMob);
    }

    public static CustomMob getCustomMob(String mobID){
        if(registeredCustomMobs.containsKey(mobID)){
            return registeredCustomMobs.get(mobID);
        }

        return null;
    }

    public static HashMap<String, CustomMob> getRegisteredMobs(){

        return registeredCustomMobs;
    }

    public static NPCRegistry getNPCRegistry(){
        return registry;
    }

    private static void startMobSpawns(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(String mobID : plugin.getConfig().getConfigurationSection("mob-spawns").getKeys(false)){

                    for(String spawnPointID : plugin.getConfig().getConfigurationSection("mob-spawns." + mobID + ".spawn-points").getKeys(false)){
                        World world = Bukkit.getWorld(plugin.getConfig().getString("mob-spawns." + mobID + ".spawn-points." + spawnPointID + ".world"));

                        double x = plugin.getConfig().getDouble("mob-spawns." + mobID + ".spawn-points." + spawnPointID + ".x");
                        double y = plugin.getConfig().getDouble("mob-spawns." + mobID + ".spawn-points." + spawnPointID + ".y");
                        double z = plugin.getConfig().getDouble("mob-spawns." + mobID + ".spawn-points." + spawnPointID + ".z");
                        float yaw = (float) plugin.getConfig().getDouble("mob-spawns." + mobID + ".spawn-points." + spawnPointID + ".yaw");
                        float pitch = (float) plugin.getConfig().getDouble("mob-spawns." + mobID + ".spawn-points." + spawnPointID + ".pitch");

                        Location location = new Location(world, x, y, z);
                        location.setPitch(pitch);
                        location.setYaw(yaw);

                        boolean locationOccupied = false;
                        for(Entity entity : world.getEntities()){
                            if(entity.getLocation().distance(location) <= 10){
                                if(entity.hasMetadata(mobID)){
                                    locationOccupied = true;
                                    break;
                                }
                            }
                        }

                        if(!locationOccupied){
                            if(getCustomMob(mobID) != null){
                                CustomMob customMob = getCustomMob(mobID);

                                customMob.spawn(location);
                            }
                        }

                    }
                }
            }
        }.runTaskTimer(plugin, 200L, 600L);
    }
}
