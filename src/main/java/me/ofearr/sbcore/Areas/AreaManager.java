package me.ofearr.sbcore.Areas;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Tablist.TablistHandler;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class AreaManager implements Listener {

    private static SBCore plugin;
    public static WorldGuardPlugin worldGuard;
    private static HashMap<UUID, String> lastKnownPlayerLocation = new HashMap<>();

    public AreaManager (SBCore sbCore, WorldGuardPlugin worldGuardPlugin){
        this.plugin = sbCore;
        this.worldGuard = worldGuardPlugin;

    }

    public static boolean regionContainsLoc(String regionID, Location loc){

        Set<ProtectedRegion> regionsAtLocation = getRegionsAtLocation(loc);

        for(ProtectedRegion region : regionsAtLocation){
            String currentRegionID = region.getId();

            if(currentRegionID.contains(regionID) || currentRegionID == regionID){

                return true;
            }
        }

        return false;
    }

    public static Set<ProtectedRegion> getRegionsAtLocation(Location loc){
        RegionManager regionManager = worldGuard.getRegionManager(loc.getWorld());
        ApplicableRegionSet regionSet = regionManager.getApplicableRegions(loc);

        return regionSet.getRegions();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void islandLeapHandler(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Location loc = e.getTo();

        RegionManager regionManager = worldGuard.getRegionManager(loc.getWorld());
        ApplicableRegionSet regions = regionManager.getApplicableRegions(loc);

        if(regions.size() > 0){

            for(ProtectedRegion region : regions){
                String regionName = region.getId();

                for(String key : plugin.getConfig().getConfigurationSection("island-pads").getKeys(false)){

                    if(regionName.contains(key)){
                        String destinationWarp = plugin.getConfig().getString("island-pads." + key + ".warp-to");

                        if(plugin.getConfig().get("warps." + destinationWarp + ".world") != null){
                            String worldName = plugin.getConfig().getString("warps." + destinationWarp + ".world");
                            World world = Bukkit.getWorld(worldName);

                            int x = plugin.getConfig().getInt("warps." + destinationWarp + ".x");
                            int y = plugin.getConfig().getInt("warps." + destinationWarp + ".y");
                            int z = plugin.getConfig().getInt("warps." + destinationWarp + ".z");

                            double pitch = plugin.getConfig().getDouble("warps." + destinationWarp + ".pitch");
                            double yaw = plugin.getConfig().getDouble("warps." + destinationWarp + ".yaw");

                            Location warpLoc = new Location(world, x, y, z);

                            warpLoc.setPitch((float) pitch);
                            warpLoc.setYaw((float) yaw);

                            player.teleport(warpLoc);

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    updatePlayerLocationInfo(player);
                                }
                            }.runTaskLater(plugin, 1L);

                        }


                        return;
                    }
                }
            }
        }

    }

    public static void updatePlayerLocationInfo(Player player){
        Location loc = player.getLocation();
        World world = loc.getWorld();

        Set<ProtectedRegion> regions = getRegionsAtLocation(loc);

        String locationString = StringUtils.translate("&bUnknown Location");

        List<String> regionNames = new ArrayList<>();

        for(ProtectedRegion region : regions){
            String rName = region.getId();

            if(rName.charAt(rName.length()-2) == '_'){
                rName = rName.substring(0, rName.length() - 2);
            }

            regionNames.add(rName);
        }

        if(world.getName().equalsIgnoreCase("world_dwarven_mines")){
            locationString = StringUtils.translate("&2Dwarven Mines");

            if(regionNames.contains("dwarven_village")){
                locationString = StringUtils.translate("&bDwarven Village");

            } else if(regionNames.contains("dwarven_upper_mines")){
                locationString = StringUtils.translate("&bUpper Mines");

            } else if(regionNames.contains("dwarven_lava_springs")){
                locationString = StringUtils.translate("&bLava Springs");

            }else if(regionNames.contains("dwarven_ramparts_quarry")){
                locationString = StringUtils.translate("&bRampart's Quarry");

            } else if(regionNames.contains("dwarven_cliffside_veins")){
                locationString = StringUtils.translate("&bCliffside Veins");

            } else if(regionNames.contains("dwarven_forge")){
                locationString = StringUtils.translate("&6Forge Basin");

            } else if(regionNames.contains("dwarven_royal_mines")){
                locationString = StringUtils.translate("&bRoyal Mines");

            } else if(regionNames.contains("dwarven_divan_gateway")){
                locationString = StringUtils.translate("&bDivan's Gateway");

            } else if(regionNames.contains("dwarven_far_reserve")){
                locationString = StringUtils.translate("&bFar Reserve");

            } else if(regionNames.contains("dwarven_goblin_burrows")){
                locationString = StringUtils.translate("&bGoblin Burrows");

            } else if(regionNames.contains("dwarven_ice_wall")){
                locationString = StringUtils.translate("&bThe Great Ice Wall");

            } else if(regionNames.contains("dwarven_gateway_to_mines")){
                locationString = StringUtils.translate("&bGateway To The Mines");

            } else if(regionNames.contains("dwarven_royal_bridge")){
                locationString = StringUtils.translate("&6Palace Bridge");

            } else if(regionNames.contains("dwarven_royal_palace")){
                locationString = StringUtils.translate("&6Royal Palace");

            } else if(regionNames.contains("dwarven_mist")){
                locationString = StringUtils.translate("&8The Mist");
            }

        } else if (world.getName().equalsIgnoreCase("world")){
            locationString = StringUtils.translate("&bSkyblock Hub");

            if(regionNames.contains("coal_mine")){
                locationString = StringUtils.translate("&7Coal Mines");

            }

        } else if (world.getName().equalsIgnoreCase("world_gold_mine")){
            locationString = StringUtils.translate("&6Gold Mines");

        } else if (world.getName().equalsIgnoreCase("world_deep_caverns")){
            locationString = StringUtils.translate("&bDeep Caverns");
        }

        locationString = StringUtils.translate("&7⏣ ") + locationString;
        lastKnownPlayerLocation.put(player.getUniqueId(), locationString);

        TablistHandler.updatePlayerLocationOnTab(player, locationString);
    }

    public static String getLocationName(Location loc){
        World world = loc.getWorld();

        Set<ProtectedRegion> regions = getRegionsAtLocation(loc);

        String locationString = StringUtils.translate("&bUnknown Location");

        List<String> regionNames = new ArrayList<>();

        for(ProtectedRegion region : regions){
            String rName = region.getId();

            if(rName.charAt(rName.length()-2) == '_'){
                rName = rName.substring(0, rName.length() - 2);
            }

            regionNames.add(rName);
        }

        if(world.getName().equalsIgnoreCase("world_dwarven_mines")){
            locationString = StringUtils.translate("&2Dwarven Mines");

            if(regionNames.contains("dwarven_village")){
                locationString = StringUtils.translate("&bDwarven Village");

            } else if(regionNames.contains("dwarven_upper_mines")){
                locationString = StringUtils.translate("&bUpper Mines");

            } else if(regionNames.contains("dwarven_lava_springs")){
                locationString = StringUtils.translate("&bLava Springs");

            }else if(regionNames.contains("dwarven_ramparts_quarry")){
                locationString = StringUtils.translate("&bRampart's Quarry");

            } else if(regionNames.contains("dwarven_cliffside_veins")){
                locationString = StringUtils.translate("&bCliffside Veins");

            } else if(regionNames.contains("dwarven_forge")){
                locationString = StringUtils.translate("&6Forge Basin");

            } else if(regionNames.contains("dwarven_royal_mines")){
                locationString = StringUtils.translate("&bRoyal Mines");

            } else if(regionNames.contains("dwarven_divan_gateway")){
                locationString = StringUtils.translate("&bDivan's Gateway");

            } else if(regionNames.contains("dwarven_far_reserve")){
                locationString = StringUtils.translate("&bFar Reserve");

            } else if(regionNames.contains("dwarven_goblin_burrows")){
                locationString = StringUtils.translate("&bGoblin Burrows");

            } else if(regionNames.contains("dwarven_ice_wall")){
                locationString = StringUtils.translate("&bThe Great Ice Wall");

            } else if(regionNames.contains("dwarven_gateway_to_mines")){
                locationString = StringUtils.translate("&bGateway To The Mines");

            } else if(regionNames.contains("dwarven_royal_bridge")){
                locationString = StringUtils.translate("&6Royal Bridge");

            } else if(regionNames.contains("dwarven_royal_palace")){
                locationString = StringUtils.translate("&6Royal Palace");

            } else if(regionNames.contains("dwarven_mist")){
                locationString = StringUtils.translate("&8The Mist");
            }

        } else if (world.getName().equalsIgnoreCase("world")){
            locationString = StringUtils.translate("&bSkyblock Hub");

            if(regionNames.contains("coal_mine")){
                locationString = StringUtils.translate("&7Coal Mines");

            }

        } else if (world.getName().equalsIgnoreCase("world_gold_mine")){
            locationString = StringUtils.translate("&6Gold Mines");

        } else if (world.getName().equalsIgnoreCase("world_deep_caverns")){
            locationString = StringUtils.translate("&bDeep Caverns");
        }

        return locationString;
    }

    public static String getLastKnownPlayerLocation(Player player){

        if(!lastKnownPlayerLocation.containsKey(player.getUniqueId())){
            return StringUtils.translate("&bUnknown Location");
        }

        return lastKnownPlayerLocation.get(player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void areaUnlockHandler(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Location loc = e.getTo();

        RegionManager regionManager = worldGuard.getRegionManager(loc.getWorld());
        ApplicableRegionSet regions = regionManager.getApplicableRegions(loc);

        updatePlayerLocationInfo(player);

        if(regions.size() > 0){

            for(ProtectedRegion region : regions){
                String regionName = region.getId();

                if(regionName.equalsIgnoreCase("deep_caverns_lapis_entry") && !player.hasPermission("sbcore.deepcaverns.lapis")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.deepcaverns.lapis true");

                    player.sendTitle(StringUtils.translate("&b&lLapis Quarry"), StringUtils.translate("&aNew zone discovered!"));

                } else if(regionName.equalsIgnoreCase("deep_caverns_redstone_entry") && !player.hasPermission("sbcore.deepcaverns.redstone")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.deepcaverns.redstone true");

                    player.sendTitle(StringUtils.translate("&b&lPigman's Den"), StringUtils.translate("&aNew zone discovered!"));

                } else if(regionName.equalsIgnoreCase("deep_caverns_emerald_entry") && !player.hasPermission("sbcore.deepcaverns.emerald")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.deepcaverns.emerald true");

                    player.sendTitle(StringUtils.translate("&b&lSlimehill"), StringUtils.translate("&aNew zone discovered!"));

                } else if(regionName.equalsIgnoreCase("deep_caverns_diamond_entry") && !player.hasPermission("sbcore.deepcaverns.diamond")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.deepcaverns.diamond true");

                    player.sendTitle(StringUtils.translate("&b&lDiamond Reserve"), StringUtils.translate("&aNew zone discovered!"));

                } else if(regionName.equalsIgnoreCase("deep_caverns_obsidian_entry") && !player.hasPermission("sbcore.deepcaverns.obsidian")){
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.deepcaverns.obsidian true");

                    player.sendTitle(StringUtils.translate("&b&lObsidian Sanctuary"), StringUtils.translate("&aNew zone discovered!"));

                }
            }
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinGetLocation(PlayerJoinEvent e){
        Player player = e.getPlayer();

        updatePlayerLocationInfo(player);
    }

}
