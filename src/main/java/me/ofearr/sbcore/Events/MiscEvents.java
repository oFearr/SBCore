package me.ofearr.sbcore.Events;

import me.ofearr.customitems.Enchants.EnchantManager;
import me.ofearr.customitems.Enchants.EnchantUtils;
import me.ofearr.customitems.Enchants.General.TelekinesisEnchant;
import me.ofearr.petcore.GUI.PetMenuGUI;
import me.ofearr.sbcore.Collections.Collection;
import me.ofearr.sbcore.Collections.CollectionCategories;
import me.ofearr.sbcore.Collections.CollectionsUtil;
import me.ofearr.sbcore.CustomMobs.CustomMobManager;
import me.ofearr.sbcore.GUI.CollectionInventories.CategoryCollectionMenu;
import me.ofearr.sbcore.GUI.CollectionInventories.CollectionSpecificMenu;
import me.ofearr.sbcore.GUI.CollectionInventories.MainCollectionMenu;
import me.ofearr.sbcore.GUI.SBMenuGUI;
import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.NBTEditor;
import me.ofearr.sbcore.Utils.StringUtils;
import me.ofearr.skillcore.GUI.MainSkillMenu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
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
    public void onInvInteract(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null) return;
        if(item.getItemMeta() == null) return;
        if(item.getType() == Material.AIR) return;
        if(item.getItemMeta().hasDisplayName()){
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aSkyBlock Menu &7(Right Click)"))) {
                e.setCancelled(true);
                player.openInventory(sbMenuGUI.openGUI(player));

            }
        }

        if(e.getView().getTitle().equalsIgnoreCase(StringUtils.translate("SkyBlock Menu"))){

            if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aPets"))){
                PetMenuGUI petMenuGUI = new PetMenuGUI();
                player.openInventory(petMenuGUI.openGUI(player, 1));

            } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aYour Skills"))){
                MainSkillMenu mainSkillMenu = new MainSkillMenu();
                player.openInventory(mainSkillMenu.openGUI(player));

            } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aCollections"))){
                MainCollectionMenu mainCollectionMenu = new MainCollectionMenu();
                player.openInventory(mainCollectionMenu.GUI());

            } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&cClose"))){
                player.closeInventory();
            }
            e.setCancelled(true);

        } else if(e.getView().getTitle().equalsIgnoreCase(StringUtils.translate("Collections"))){

            try {
                CategoryCollectionMenu categoryCollectionMenu = new CategoryCollectionMenu();

                if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aFarming Collections"))){
                    player.openInventory(categoryCollectionMenu.GUI(player, CollectionCategories.FARMING));

                } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aMining Collections"))){
                    player.openInventory(categoryCollectionMenu.GUI(player, CollectionCategories.MINING));

                } else if(NBTEditor.contains(item, "collection_name")){

                    String collectionName = NBTEditor.getString(item, "collection_name");
                    Collection collection = CollectionsUtil.getCollectionFromName(collectionName);

                    if(collection != null){
                        player.openInventory(new CollectionSpecificMenu().GUI(player, collection));
                    }

                }else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&cClose"))){
                    player.closeInventory();
                }
            } catch (Exception ex){
                ex.printStackTrace();
            }

            e.setCancelled(true);

        } else if(e.getView().getTitle().equalsIgnoreCase(StringUtils.translate("Apply Telekinesis Enchant"))){

            if(e.getClickedInventory() == e.getView().getBottomInventory()) return;
            if(e.getSlot() == 22) return;

            if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aClick to apply Telekinesis!"))){

                ItemStack targetItem = e.getClickedInventory().getItem(22);

                TelekinesisEnchant telekinesisEnchant = new TelekinesisEnchant();

                if(targetItem == null || targetItem.getType() == Material.AIR){
                    player.sendMessage(StringUtils.translate("&cYou must put an item in the empty slot!"));

                } else if(EnchantManager.canEnchantItem(new TelekinesisEnchant(), targetItem) && !(EnchantManager.checkForEnchantOnItem(targetItem, telekinesisEnchant))){

                    Economy eco = plugin.getEconomy();

                    if(!(eco.getBalance(player) >= 100)){
                        player.sendMessage(StringUtils.translate("&cYou don't have enough coins to do this!"));

                        Location loc = player.getLocation();
                        World world = loc.getWorld();

                        world.playSound(loc, Sound.ANVIL_BREAK, 1f, 1f);

                    } else {

                        eco.withdrawPlayer(player, 100);

                        ItemMeta itemMeta = targetItem.getItemMeta();

                        itemMeta.addEnchant(telekinesisEnchant, 1, false);
                        targetItem.setItemMeta(itemMeta);

                        targetItem = EnchantUtils.addEnchantmentDisplay(targetItem);

                        e.getView().getTopInventory().setItem(22, targetItem);

                        Location loc = player.getLocation();
                        World world = loc.getWorld();

                        world.playSound(loc, Sound.ANVIL_USE, 1f, 1f);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.updateInventory();
                            }
                        }.runTaskLater(plugin, 1L);

                        player.sendMessage(StringUtils.translate("&aSuccessfully applied the Telekinesis enchantment to your " + targetItem.getItemMeta().getDisplayName() + " &a!"));

                    }

                }
            }

            e.setCancelled(true);

        } else if(e.getView().getTitle().equalsIgnoreCase(StringUtils.translate("Select A Destination!"))){


            if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aGunpowder Mines"))){

                String worldName = plugin.getConfig().getString("warps.deepcaverns_coal.world");
                World world = Bukkit.getWorld(worldName);

                int x = plugin.getConfig().getInt("warps.deepcaverns_coal.x");
                int y = plugin.getConfig().getInt("warps.deepcaverns_coal.y");
                int z = plugin.getConfig().getInt("warps.deepcaverns_coal.z");

                double pitch = plugin.getConfig().getDouble("warps.deepcaverns_coal.pitch");
                double yaw = plugin.getConfig().getDouble("warps.deepcaverns_coal.yaw");

                Location spawnLoc = new Location(world, x, y, z);

                spawnLoc.setPitch((float) pitch);
                spawnLoc.setYaw((float) yaw);


                if(player.getLocation().distance(spawnLoc) <= 14){
                    player.closeInventory();
                    player.sendMessage(StringUtils.translate("&cYou are already at that level!"));
                } else {
                    player.teleport(spawnLoc);
                }

            } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aLapis Quarry"))){

                String worldName = plugin.getConfig().getString("warps.deepcaverns_lapis.world");
                World world = Bukkit.getWorld(worldName);

                int x = plugin.getConfig().getInt("warps.deepcaverns_lapis.x");
                int y = plugin.getConfig().getInt("warps.deepcaverns_lapis.y");
                int z = plugin.getConfig().getInt("warps.deepcaverns_lapis.z");

                double pitch = plugin.getConfig().getDouble("warps.deepcaverns_lapis.pitch");
                double yaw = plugin.getConfig().getDouble("warps.deepcaverns_lapis.yaw");

                Location spawnLoc = new Location(world, x, y, z);

                spawnLoc.setPitch((float) pitch);
                spawnLoc.setYaw((float) yaw);


                if(player.getLocation().distance(spawnLoc) <= 14){
                    player.closeInventory();
                    player.sendMessage(StringUtils.translate("&cYou are already at that level!"));
                } else {
                    player.teleport(spawnLoc);
                }

            } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aPigman's Den"))){

                String worldName = plugin.getConfig().getString("warps.deepcaverns_redstone.world");
                World world = Bukkit.getWorld(worldName);

                int x = plugin.getConfig().getInt("warps.deepcaverns_redstone.x");
                int y = plugin.getConfig().getInt("warps.deepcaverns_redstone.y");
                int z = plugin.getConfig().getInt("warps.deepcaverns_redstone.z");

                double pitch = plugin.getConfig().getDouble("warps.deepcaverns_redstone.pitch");
                double yaw = plugin.getConfig().getDouble("warps.deepcaverns_redstone.yaw");

                Location spawnLoc = new Location(world, x, y, z);

                spawnLoc.setPitch((float) pitch);
                spawnLoc.setYaw((float) yaw);


                if(player.getLocation().distance(spawnLoc) <= 14){
                    player.closeInventory();
                    player.sendMessage(StringUtils.translate("&cYou are already at that level!"));
                } else {
                    player.teleport(spawnLoc);
                }

            } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aSlimehill"))){

                String worldName = plugin.getConfig().getString("warps.deepcaverns_emerald.world");
                World world = Bukkit.getWorld(worldName);

                int x = plugin.getConfig().getInt("warps.deepcaverns_emerald.x");
                int y = plugin.getConfig().getInt("warps.deepcaverns_emerald.y");
                int z = plugin.getConfig().getInt("warps.deepcaverns_emerald.z");

                double pitch = plugin.getConfig().getDouble("warps.deepcaverns_emerald.pitch");
                double yaw = plugin.getConfig().getDouble("warps.deepcaverns_emerald.yaw");

                Location spawnLoc = new Location(world, x, y, z);

                spawnLoc.setPitch((float) pitch);
                spawnLoc.setYaw((float) yaw);


                if(player.getLocation().distance(spawnLoc) <= 14){
                    player.closeInventory();
                    player.sendMessage(StringUtils.translate("&cYou are already at that level!"));
                } else {
                    player.teleport(spawnLoc);
                }

            } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aDiamond Reserve"))){

                String worldName = plugin.getConfig().getString("warps.deepcaverns_diamond.world");
                World world = Bukkit.getWorld(worldName);

                int x = plugin.getConfig().getInt("warps.deepcaverns_diamond.x");
                int y = plugin.getConfig().getInt("warps.deepcaverns_diamond.y");
                int z = plugin.getConfig().getInt("warps.deepcaverns_diamond.z");

                double pitch = plugin.getConfig().getDouble("warps.deepcaverns_diamond.pitch");
                double yaw = plugin.getConfig().getDouble("warps.deepcaverns_diamond.yaw");

                Location spawnLoc = new Location(world, x, y, z);

                spawnLoc.setPitch((float) pitch);
                spawnLoc.setYaw((float) yaw);


                if(player.getLocation().distance(spawnLoc) <= 14){
                    player.closeInventory();
                    player.sendMessage(StringUtils.translate("&cYou are already at that level!"));
                } else {
                    player.teleport(spawnLoc);
                }

            } else if(item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equalsIgnoreCase(StringUtils.translate("&aObsidian Sanctuary"))){

                String worldName = plugin.getConfig().getString("warps.deepcaverns_obsidian.world");
                World world = Bukkit.getWorld(worldName);

                int x = plugin.getConfig().getInt("warps.deepcaverns_obsidian.x");
                int y = plugin.getConfig().getInt("warps.deepcaverns_obsidian.y");
                int z = plugin.getConfig().getInt("warps.deepcaverns_obsidian.z");

                double pitch = plugin.getConfig().getDouble("warps.deepcaverns_obsidian.pitch");
                double yaw = plugin.getConfig().getDouble("warps.deepcaverns_obsidian.yaw");

                Location spawnLoc = new Location(world, x, y, z);

                spawnLoc.setPitch((float) pitch);
                spawnLoc.setYaw((float) yaw);

                if(player.getLocation().distance(spawnLoc) <= 14){
                    player.closeInventory();
                    player.sendMessage(StringUtils.translate("&cYou are already at that level!"));
                } else {
                    player.teleport(spawnLoc);
                }

            }

            e.setCancelled(true);
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
