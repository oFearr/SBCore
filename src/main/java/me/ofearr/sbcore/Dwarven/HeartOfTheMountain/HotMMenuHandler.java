package me.ofearr.sbcore.Dwarven.HeartOfTheMountain;


import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.Dwarven.PowderType;
import me.ofearr.sbcore.Dwarven.Upgrades.DwarvenUpgrade;
import me.ofearr.sbcore.GUI.SBMenuGUI;
import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.NBTEditor;
import me.ofearr.sbcore.Utils.SkullGenerator;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class HotMMenuHandler implements Listener {

    private static SBCore plugin;

    public HotMMenuHandler(SBCore sbCore){
        this.plugin = sbCore;
    }

    private ItemStack getHotmItem(Player player){

        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        int hotmTokens = dataManager.getConfig().getInt("dwarven-data.hotm-tokens");
        int mithrilPowder = dataManager.getConfig().getInt("dwarven-data.mithril-powder");
        int gemstonePowder = dataManager.getConfig().getInt("dwarven-data.gemstone-powder");

        ItemStack hotmItem = SkullGenerator.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZmMDZlYWEzMDA0YWVlZDA5YjNkNWI0NWQ5NzZkZTU4NGU2OTFjMGU5Y2FkZTEzMzYzNWRlOTNkMjNiOWVkYiJ9fX0=");

        ItemMeta hotmMeta = hotmItem.getItemMeta();
        List<String> hotmLore = new ArrayList<>();

        hotmMeta.setDisplayName(StringUtils.translate("&5Heart of the Mountain"));
        hotmLore.add(StringUtils.translate("&7Token of the Mountain: &5" + hotmTokens));
        hotmLore.add(" ");
        hotmLore.add(StringUtils.translate("&8Unlock more &5Token of the"));
        hotmLore.add(StringUtils.translate("&5Mountain &8by leveling up your"));
        hotmLore.add(StringUtils.translate("&8HotM tiers."));
        hotmLore.add(StringUtils.translate(" "));
        hotmLore.add(StringUtils.translate("&9᠅ Powder"));
        hotmLore.add(StringUtils.translate("&9Powders &8are dropped from"));
        hotmLore.add(StringUtils.translate("&8mining ores in the &2Dwarven"));
        hotmLore.add(StringUtils.translate("&2Mines &8and are used to upgrade"));
        hotmLore.add(StringUtils.translate("&8the perks you've unlocked!"));
        hotmLore.add(StringUtils.translate(" "));
        hotmLore.add(StringUtils.translate("&7Mithril Powder: &2" + mithrilPowder));
        hotmLore.add(StringUtils.translate("&7Gemstone Powder: &d" + gemstonePowder));
        hotmLore.add(StringUtils.translate(" "));
        hotmLore.add(StringUtils.translate("&8Increase your chance to gain"));
        hotmLore.add(StringUtils.translate("&8extra Powder by unlocking perks,"));
        hotmLore.add(StringUtils.translate("&8equipping the &2Mithril Golem"));
        hotmLore.add(StringUtils.translate("&2Pet&8, and more!"));

        hotmMeta.setLore(hotmLore);
        hotmItem.setItemMeta(hotmMeta);

        return hotmItem;
    }

    @EventHandler
    public void onClickHotMMenu(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if (item == null) return;
        if(item.getItemMeta() == null) return;
        if(item.getType() == Material.AIR) return;

        Inventory inv = e.getClickedInventory();

        if(e.getView().getTitle().equalsIgnoreCase(StringUtils.translate("Heart of the Mountain"))){

            if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()){
                String displayName = item.getItemMeta().getDisplayName();

                if(displayName.equalsIgnoreCase(StringUtils.translate("&aScroll Up"))){
                    player.openInventory(new HotMTreeMenu().GUI(player, 2));

                } else if(displayName.equalsIgnoreCase(StringUtils.translate("&aScroll Down"))){
                    player.openInventory(new HotMTreeMenu().GUI(player, 1));

                } else if(displayName.equalsIgnoreCase(StringUtils.translate("&cClose"))){
                    player.closeInventory();

                } else if(displayName.equalsIgnoreCase(StringUtils.translate("&aGo Back"))){
                    player.openInventory(new SBMenuGUI().openGUI(player));

                }else if(NBTEditor.contains(item, "hotm_menu_upgrade")){
                    String upgradeID = NBTEditor.getString(item, "hotm_menu_upgrade");

                    DwarvenUpgrade dwarvenUpgrade = DwarvenManager.getDwarvenUpgrade(upgradeID);

                    if(dwarvenUpgrade != null){
                        int requiredHOTM = dwarvenUpgrade.requiredHOTMLevel();
                        int playerHOTM = DwarvenManager.getHOTMLevel(player);

                        if(playerHOTM >= requiredHOTM){
                            String[] requiredUpgrades = {dwarvenUpgrade.primaryRequirement(), dwarvenUpgrade.secondaryRequirement(), dwarvenUpgrade.thirdRequirement()};
                            DwarvenUtils dwarvenUtils = new DwarvenUtils();

                            boolean meetsRequirement = false;

                            if(requiredUpgrades[0].equals("")){
                                meetsRequirement = true;

                            } else {
                                for(String requirement : requiredUpgrades){

                                    if(requirement == "") continue;

                                    int requirementLevel = dwarvenUtils.getPlayerDwarvenUpgradeLevel(player, requirement);

                                    if(requirementLevel >= 1){
                                        meetsRequirement = true;
                                        break;
                                    }
                                }
                            }

                            if(meetsRequirement){
                                int playerUpgradeLevel = dwarvenUtils.getPlayerDwarvenUpgradeLevel(player, dwarvenUpgrade);

                                if(e.getClick() == ClickType.RIGHT){
                                    dwarvenUtils.toggleUpgradeStatus(player, dwarvenUpgrade);

                                    ItemStack newItem = dwarvenUpgrade.getDisplayItem(player);

                                    int slot = e.getSlot();
                                    inv.setItem(slot, newItem);

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            player.updateInventory();
                                        }
                                    }.runTaskLater(plugin, 1L);

                                    e.setCancelled(true);
                                    return;
                                }

                                int newUpgradeLevel = playerUpgradeLevel + 1;

                                int maxUpgradeLevel = dwarvenUpgrade.maxLevel();

                                if(newUpgradeLevel > maxUpgradeLevel){
                                    player.sendMessage(StringUtils.translate("&cYou've already got the max level for this upgrade!"));
                                } else {

                                    int playerTokenCount = dwarvenUtils.getAvailableHOTMTokensForPlayer(player);

                                    if((playerUpgradeLevel == 0) && (playerTokenCount < 1)){
                                        player.sendMessage(StringUtils.translate("&cYou don't have enough &5Token of the Mountain &cto do this!"));
                                    } else {

                                        PowderType powderType = dwarvenUpgrade.powderType();
                                        int playerPowder = dwarvenUtils.getPowderForPlayer(player, powderType);
                                        int powderCost = dwarvenUpgrade.baseCost() * (newUpgradeLevel);

                                        if(playerPowder >= powderCost){

                                            if(dwarvenUtils.setPlayerDwarvenUpgradeLevel(player, dwarvenUpgrade, newUpgradeLevel)){

                                                if(dwarvenUpgrade.upgradeID() == "peak_of_the_mountain"){
                                                    if(newUpgradeLevel == 1){
                                                        dwarvenUtils.setHOTMTokensForPlayer(player, playerTokenCount + 1);

                                                    } else if(newUpgradeLevel == 2){
                                                        int playerForgeSlots = dwarvenUtils.getPlayerForgeSlotCount(player);

                                                        dwarvenUtils.setPlayerForgeSlotCount(player, playerForgeSlots + 1);

                                                    } if(newUpgradeLevel == 3){
                                                        int playerCommissionSlots = dwarvenUtils.getPlayerCommissionSlotCount(player);

                                                        dwarvenUtils.setPlayerCommissionSlotCount(player, playerCommissionSlots + 1);
                                                    }
                                                    if(newUpgradeLevel == 4){

                                                        //Needs implemented

                                                    } if(newUpgradeLevel == 5){
                                                        dwarvenUtils.setHOTMTokensForPlayer(player, playerTokenCount + 1);
                                                    }
                                                }

                                                int newPowderCount = playerPowder - powderCost;
                                                dwarvenUtils.setHOTMTokensForPlayer(player, playerTokenCount -1);

                                                switch (powderType){
                                                    case MITHRIL: dwarvenUtils.setMithrilPowder(player, newPowderCount);
                                                    case GEMSTONE: dwarvenUtils.setGemstonePowder(player, newPowderCount);
                                                }

                                                int slot = e.getSlot();
                                                ItemStack newItem = dwarvenUpgrade.getDisplayItem(player);

                                                inv.setItem(slot, newItem);
                                                inv.setItem(49, getHotmItem(player));


                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        player.updateInventory();
                                                    }
                                                }.runTaskLater(plugin, 1L);
                                                player.sendMessage(StringUtils.translate("&aSuccessfully upgraded " + dwarvenUpgrade.name() + " to level " + newUpgradeLevel + "!"));
                                            }
                                        } else {

                                            player.sendMessage(StringUtils.translate("&cYou don't have enough powder to perform this action!"));
                                        }
                                    }

                                }

                            } else {
                                player.sendMessage(StringUtils.translate("&cYou have not yet unlocked this section of the upgrade tree!"));
                            }

                        } else {
                            player.sendMessage(StringUtils.translate("&cYou need &5Heart of the Mountain " + requiredHOTM + " &cfor this upgrade!"));
                        }



                    }
                }



            }
            e.setCancelled(true);

        }
    }
}
