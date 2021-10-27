package me.ofearr.sbcore.Events;

import me.ofearr.customitems.Enchants.EnchantManager;
import me.ofearr.customitems.Enchants.EnchantUtils;
import me.ofearr.customitems.Enchants.General.TelekinesisEnchant;
import me.ofearr.petcore.GUI.PetMenuGUI;
import me.ofearr.sbcore.Collections.Collection;
import me.ofearr.sbcore.Collections.CollectionCategories;
import me.ofearr.sbcore.Collections.CollectionsUtil;
import me.ofearr.sbcore.GUI.CollectionInventories.CategoryCollectionMenu;
import me.ofearr.sbcore.GUI.CollectionInventories.CollectionSpecificMenu;
import me.ofearr.sbcore.GUI.CollectionInventories.MainCollectionMenu;
import me.ofearr.sbcore.GUI.SBMenuGUI;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.NBTEditor;
import me.ofearr.sbcore.Utils.StringUtils;
import me.ofearr.skillcore.GUI.MainSkillMenu;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class MiscGUIHandlers implements Listener {

    private SBCore plugin;

    public MiscGUIHandlers(SBCore sbCore){
        this.plugin = sbCore;
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
                SBMenuGUI sbMenuGUI = new SBMenuGUI();

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

        } else if(e.getView().getTitle().equalsIgnoreCase(StringUtils.translate("Collections")) || e.getView().getTitle().contains(StringUtils.translate("Collection"))){

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
}
