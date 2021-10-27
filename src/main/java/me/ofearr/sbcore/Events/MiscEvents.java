package me.ofearr.sbcore.Events;

import me.ofearr.sbcore.CustomMobs.CustomMobManager;
import me.ofearr.sbcore.GUI.SBMenuGUI;
import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class MiscEvents implements Listener {

    private SBCore plugin;

    public MiscEvents(SBCore sbCore){
        this.plugin = sbCore;
    }

    private SBMenuGUI sbMenuGUI = new SBMenuGUI();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        ItemStack sbMenuItem = new ItemStack(Material.NETHER_STAR);

        ItemMeta sbMeta = sbMenuItem.getItemMeta();
        sbMeta.setDisplayName(StringUtils.translate("&aSkyBlock Menu &7(Right Click)"));
        List<String> sbLore = new ArrayList<>();

        sbLore.add(StringUtils.translate("&7View all of your SkyBlock"));
        sbLore.add(StringUtils.translate("&7progress, including your Skills,"));
        sbLore.add(StringUtils.translate("&7Collections, Recipes, and more!"));

        sbMeta.setLore(sbLore);
        sbMenuItem.setItemMeta(sbMeta);

        player.getInventory().setItem(8, sbMenuItem);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDropEvent(PlayerDropItemEvent e){
        Player player = e.getPlayer();
        if(!e.getItemDrop().getItemStack().hasItemMeta()) return;
        if(!e.getItemDrop().getItemStack().getItemMeta().hasDisplayName()) return;

        if(e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aSkyBlock Menu &7(Right Click)"))){
            e.setCancelled(true);
            player.openInventory(sbMenuGUI.openGUI(player));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractEvent(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Action action = e.getAction();

        if(player.getItemInHand() == null) return;
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK || action == Action.PHYSICAL){
            if(player.getItemInHand().getItemMeta() == null) return;
            if(!player.getItemInHand().getItemMeta().hasDisplayName()) return;

             if(player.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aSkyBlock Menu &7(Right Click)"))){
                player.openInventory(sbMenuGUI.openGUI(player));
            }

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent e){
        Player player = e.getPlayer();
        if(e.getMessage().equalsIgnoreCase("/pl") || e.getMessage().equalsIgnoreCase("/plugins") || e.getMessage().equalsIgnoreCase("/bukkit:pl") || e.getMessage().equalsIgnoreCase("/bukkit:plugins")){

            if(!player.hasPermission("sbcore.admin")){
                e.setCancelled(true);

                String serverVer = plugin.getConfig().getString("server-version");
                player.sendMessage(me.ofearr.sbcore.Utils.StringUtils.translate("&aThis server is running &b&lSkyBlock &aver " + serverVer + "!"));
            }
        } else if(e.getMessage().equalsIgnoreCase("/bukkit:ver") || e.getMessage().equalsIgnoreCase("/bukkit:version") || e.getMessage().equalsIgnoreCase("/icanhasbukkit")){

            if(!player.hasPermission("sbcore.admin")){
                e.setCancelled(true);

                String serverVer = plugin.getConfig().getString("server-version");
                player.sendMessage(me.ofearr.sbcore.Utils.StringUtils.translate("&aThis server is running &b&lSkyBlock &aver " + serverVer + "!"));
            }
        }
    }

    //Prevent mobs from spawning unless they're supposed to
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawnEvent(CreatureSpawnEvent e){
        if(e.getSpawnReason() != CreatureSpawnEvent.SpawnReason.CUSTOM){
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEndermanTeleport(EntityTeleportEvent e){
        Entity entity = e.getEntity();

        if(entity.getType().equals(EntityType.ENDERMAN)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void commandBlackListEvents(PlayerCommandPreprocessEvent e){
        String[] args = e.getMessage().split(" ");
        Player player = e.getPlayer();

        if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")){
            e.setCancelled(true);

            player.sendMessage("Unknown command. Type '/help' for help.");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerHunger(FoodLevelChangeEvent e){
        e.setFoodLevel(20);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRegenHealth(EntityRegainHealthEvent e){
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDamagePlayerEvent(EntityDamageByEntityEvent e){


        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {

            for(String mobID : CustomMobManager.getRegisteredMobs().keySet())
            if(e.getDamager().hasMetadata(mobID) || e.getEntity().hasMetadata(mobID)){
                e.setCancelled(false);
                return;
            }

            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDamageEvent(EntityDamageEvent e){

        if(e.getEntity() instanceof Player){

            Player player = (Player) e.getEntity();

            if(player.getHealth() <= 1){

                e.setCancelled(true);

                boolean isPrivateIsland = false;

                if(!isPrivateIsland){
                    //Withdraw half of player's balance
                }

                player.setHealth(player.getMaxHealth());

                World world = player.getWorld();
                Location worldSpawn = world.getSpawnLocation();

                player.teleport(worldSpawn);

                for(Player p : Bukkit.getOnlinePlayers()){
                    p.sendMessage(StringUtils.translate("&c☠ " + player.getName() + " passed away!"));
                }
            }

        }

    }

    //If the server lags and a player somehow dies
    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDeathEvent(PlayerDeathEvent e){

        e.setKeepInventory(true);
    }

    //Remove arrows if they're on the ground
    @EventHandler
    public void onArrowHitTarget(ProjectileHitEvent e)
    {
        Projectile projectile = e.getEntity();
        if(projectile instanceof Arrow)
        {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(projectile.isOnGround()){
                        projectile.remove();
                    }
                }
            }.runTaskLater(plugin, 1L);
        }
        return;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        World world;

        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(dataManager.getConfig().get("misc.logout-world") == null){
            world = Bukkit.getWorld("world");

        } else {
            String worldName = (String) dataManager.getConfig().get("misc.logout-world");

            world = Bukkit.getWorld(worldName);
        }

        if(plugin.getConfig().get("worlds." + world.getName().toLowerCase() + ".spawn-point.x") == null){
            player.teleport(world.getSpawnLocation());

        } else {

            int x = plugin.getConfig().getInt("worlds." + world.getName().toLowerCase() + ".spawn-point.x");
            int y = plugin.getConfig().getInt("worlds." + world.getName().toLowerCase() + ".spawn-point.y");
            int z = plugin.getConfig().getInt("worlds." + world.getName().toLowerCase() + ".spawn-point.z");

            double pitch = plugin.getConfig().getDouble("worlds." + world.getName().toLowerCase() + ".spawn-point.pitch");
            double yaw = plugin.getConfig().getDouble("worlds." + world.getName().toLowerCase() + ".spawn-point.yaw");

            Location spawnLoc = new Location(world, x, y, z);

            spawnLoc.setPitch((float) pitch);
            spawnLoc.setYaw((float) yaw);

            player.teleport(spawnLoc);

        }


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChunkUnload(ChunkUnloadEvent e){
        World world = e.getWorld();
        if(world.getName().equalsIgnoreCase("world_dwarven_mines")){
            e.setCancelled(true);
        }


    }
}
