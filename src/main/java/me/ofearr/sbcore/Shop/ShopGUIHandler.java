package me.ofearr.sbcore.Shop;

import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.sbcore.CustomConfigs.ShopConfig;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.NBTEditor;
import me.ofearr.sbcore.Utils.StringUtils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ShopGUIHandler implements Listener {

    private static SBCore plugin;
    public ShopGUIHandler(SBCore sbCore){
        this.plugin = sbCore;
    }

    public void handleItemPurchase(Player player, String itemID) {

        if(ItemManager.getCustomItem(itemID) != null){

            if(player.getInventory().firstEmpty() == -1){
                player.sendMessage(StringUtils.translate("&cYou don't have enough space in your inventory to perform this trade!"));
                return;
            }
            ShopConfig shopConfig = new ShopConfig();
            shopConfig.load();

            String shopID = NBTEditor.getString(player.getOpenInventory().getTopInventory().getItem(0), "shop_id");

            int cost = shopConfig.getConfig().getInt("shops." + shopID + ".items." + itemID + ".coins");

            HashMap<String, Integer> requiredItems = new HashMap<>();

            for (String requiredItemId : shopConfig.getConfig().getConfigurationSection("shops." + shopID + ".items." + itemID + ".items").getKeys(false)) {
                int requiredAmt = shopConfig.getConfig().getInt("shops." + shopID + ".items." + itemID + ".items." + requiredItemId);

                if (ItemManager.getCustomItem(requiredItemId, requiredAmt) != null) {
                    requiredItems.put(requiredItemId, requiredAmt);

                }

            }

            Economy eco = plugin.getEconomy();

            boolean hasRequired = true;
            if (eco.getBalance(player) >= cost) {

                for (String requiredID : requiredItems.keySet()) {
                    if (ItemManager.getCustomItem(requiredID) != null) {

                        int requiredItemCount = requiredItems.get(requiredID);

                        HashMap<String, Integer> playerInventoryContents = new HashMap<>();

                        for (ItemStack inventoryItem : player.getInventory()) {
                            if (inventoryItem != null) {
                                if (NBTEditor.contains(inventoryItem, "item_id")) {
                                    String invItemID = NBTEditor.getString(inventoryItem, "item_id");
                                    int stackAmt = inventoryItem.getAmount();

                                    if (!playerInventoryContents.containsKey(invItemID)) {
                                        playerInventoryContents.put(invItemID, stackAmt);

                                    } else {
                                        int storedAmt = playerInventoryContents.get(invItemID);

                                        playerInventoryContents.put(invItemID, storedAmt + stackAmt);
                                    }
                                }
                            }

                        }

                        if (playerInventoryContents.containsKey(requiredID) && playerInventoryContents.get(requiredID) >= requiredItemCount) {
                            continue;
                        } else {
                            hasRequired = false;
                        }

                    }
                }

            } else {
                hasRequired = false;
            }

            if (hasRequired) {
                for (String requiredID : requiredItems.keySet()) {
                    if (ItemManager.getCustomItem(requiredID) != null) {

                        System.out.println(requiredID);

                        int requiredItemCount = requiredItems.get(requiredID);

                        int removedStackAmount = 0;

                        if (removedStackAmount >= requiredItemCount) {
                            break;
                        }

                        int currentSlot = 0;
                        for (ItemStack inventoryItem : player.getInventory()) {
                            int requiredDifference = requiredItemCount - removedStackAmount;

                            if(inventoryItem != null){
                                if(NBTEditor.contains(inventoryItem, "item_id")){
                                    String invItemID = NBTEditor.getString(inventoryItem, "item_id");
                                    int stackAmt = inventoryItem.getAmount();

                                    if(removedStackAmount >= requiredItemCount){
                                        break;
                                    }

                                    if(invItemID.equals(requiredID)){
                                        if(stackAmt > 1){

                                            if(stackAmt > requiredDifference){
                                                inventoryItem.setAmount(stackAmt - requiredDifference);
                                                player.getInventory().setItem(currentSlot, inventoryItem);

                                                removedStackAmount = removedStackAmount + requiredDifference;

                                            } else {
                                                player.getInventory().setItem(currentSlot, new ItemStack(Material.AIR));

                                                removedStackAmount = removedStackAmount + stackAmt;

                                            }

                                        } else {
                                            player.getInventory().remove(inventoryItem);

                                            removedStackAmount++;

                                        }

                                    }
                                }
                            }

                            currentSlot++;
                        }

                    }
                }

                eco.withdrawPlayer(player, cost);

                int itemAmtToGive = shopConfig.getConfig().getInt("shops." + shopID + ".items." + itemID + ".amount");

                ItemStack itemToGive = ItemManager.getCustomItem(itemID, itemAmtToGive);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.getInventory().addItem(itemToGive);
                    }
                }.runTaskLater(plugin, 1L);

            } else {
                player.sendMessage(StringUtils.translate("&cYou don't have the required item(s) to perform this trade!"));
            }
        }

    }

    public void handleItemSale(Player player, int slot, String itemID){
        ShopConfig shopConfig = new ShopConfig();
        shopConfig.load();

        if(shopConfig.getConfig().get("salable-items." + itemID) != null){
            int sellPrice = shopConfig.getConfig().getInt("salable-items." + itemID);

            int itemCount = player.getInventory().getItem(slot).getAmount();

            sellPrice = sellPrice * itemCount;

            Economy eco = plugin.getEconomy();

            player.getInventory().setItem(slot, new ItemStack(Material.AIR));
            eco.depositPlayer(player, sellPrice);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.updateInventory();
                }
            }.runTaskLater(plugin, 1l);

            if(ItemManager.getCustomItem(itemID) != null){
                ItemStack item = ItemManager.getCustomItem(itemID);

                player.sendMessage(StringUtils.translate("&aYou sold " + item.getItemMeta().getDisplayName() + " &a(&8x" + itemCount + "&a) for &6" + sellPrice + " Coins&a!"));
            }

        } else {
            player.sendMessage(StringUtils.translate("&cSorry! This item cannot be sold here!"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClickShopGUI(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();

        Inventory inv = e.getClickedInventory();
        ItemStack item = e.getCurrentItem();

        if(item == null) return;
        if(e.getSlotType() == InventoryType.SlotType.OUTSIDE);

        if(e.getView().getTopInventory().getTitle().contains(StringUtils.translate("'s Shop"))){
            if(item.hasItemMeta() && item.getItemMeta().hasLore()){
                if(NBTEditor.contains(item, "item_id")){
                    String itemID = NBTEditor.getString(item, "item_id");

                    if(inv == e.getView().getTopInventory()){
                        handleItemPurchase(player, itemID);

                    } else if(inv == e.getView().getBottomInventory()){
                        handleItemSale(player, e.getSlot(), itemID);
                    }
                }

            }

            e.setCancelled(true);
        }
    }
}
