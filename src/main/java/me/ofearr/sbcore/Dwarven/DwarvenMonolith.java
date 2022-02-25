package me.ofearr.sbcore.Dwarven;

import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

public class DwarvenMonolith {

    private Monolith type;
    private Location currentLocation;
    private SBCore plugin = SBCore.plugin;

    private boolean isSpawned = false;

    public DwarvenMonolith(Location spawnLocation){
        spawn(spawnLocation);
    }

    public DwarvenMonolith(){

        spawn(getRandomSpawnLocation());
    }

    public void setRandomType(){
        List<Monolith> monolithTypes = Arrays.asList(Monolith.values());

        Random random = new Random();
        type = monolithTypes.get(random.nextInt(monolithTypes.size()));
    }

    public void spawn(Location loc){

        if(isSpawned) destroy();
        DwarvenManager dwarvenManager = plugin.dwarvenManager;

        if(dwarvenManager.getSpawnedMonoliths().containsKey(loc)) return;
        Block block = loc.getBlock();

        block.setType(Material.DRAGON_EGG);

        DwarvenManager.registerMonolithAtLocation(loc, this);
        currentLocation = loc;

        isSpawned = true;
        setRandomType();

        for(Player p : Bukkit.getOnlinePlayers()){
            if(p.getLocation().distance(currentLocation) <= 10){
                p.sendMessage(StringUtils.translate(plugin.getConfig().getString("dwarven-settings.monoliths.nearby-spawn-message")));
            }
        }
    }

    public void grantReward(Player player){
        Random random = new Random();

        Set<String> rewards = plugin.getConfig().getConfigurationSection("dwarven-settings.monoliths." + type.name().toLowerCase() + ".rewards").getKeys(false);
        String selRewardKey = (String) rewards.toArray()[random.nextInt(rewards.size())];

        List<String> rewardMsgList = plugin.getConfig().getStringList("dwarven-settings.monoliths." + type.name().toLowerCase() + ".rewards." + selRewardKey + ".messages");
        List<String> rewardCmdList = plugin.getConfig().getStringList("dwarven-settings.monoliths." + type.name().toLowerCase() + ".rewards." + selRewardKey + ".commands");

        for(String msg : rewardMsgList){
            msg = msg.replace("<player>", player.getName());
            player.sendMessage(StringUtils.translate(msg));
        }

        for(String cmd : rewardCmdList){
            cmd = cmd.replace("<player>", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }

    public boolean destroy(){
        DwarvenManager dwarvenManager = plugin.dwarvenManager;
        boolean didDestroy = false;

        if(dwarvenManager.getSpawnedMonoliths().get(currentLocation) != null){
            dwarvenManager.removeMonolithFromMap(currentLocation);

            currentLocation.getBlock().setType(Material.AIR);

            isSpawned = false;
            didDestroy = true;
        }

        return didDestroy;
    }

    public Location getRandomSpawnLocation(){
        Set<String> spawnLocations = plugin.getConfig().getConfigurationSection("dwarven-settings.monoliths.spawn-locations").getKeys(false);

        Random random = new Random();
        String selSpawn = (String) spawnLocations.toArray()[random.nextInt(spawnLocations.size())];

        double x = plugin.getConfig().getDouble("dwarven-settings.monoliths.spawn-locations." + selSpawn + ".x");
        double y = plugin.getConfig().getDouble("dwarven-settings.monoliths.spawn-locations." + selSpawn + ".y");
        double z = plugin.getConfig().getDouble("dwarven-settings.monoliths.spawn-locations." + selSpawn + ".z");

        World world = Bukkit.getWorld(plugin.getConfig().getString("dwarven-settings.monoliths.spawn-locations." + selSpawn + ".world"));

        return new Location(world, x, y, z);
    }

    public Monolith getType(){
        return type;
    }

    public Location getCurrentLocation(){
        return currentLocation;
    }


}
