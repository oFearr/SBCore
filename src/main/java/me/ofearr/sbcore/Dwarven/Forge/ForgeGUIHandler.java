package me.ofearr.sbcore.Dwarven.Forge;

import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.customitems.Utils.IssueItemUtil;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.Dwarven.Forge.GUI.CategoryForgeGUI;
import me.ofearr.sbcore.Dwarven.Forge.GUI.MainForgeGUI;
import me.ofearr.sbcore.Dwarven.Forge.GUI.ProcessForgeGUI;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier2Upgrades.QuickForgeUpgrade;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.NBTEditor;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ForgeGUIHandler implements Listener {

    private static SBCore plugin;
    public ForgeGUIHandler(SBCore sbCore){
        this.plugin = sbCore;
    }

    public int getClickedForgeSlot(int slot){
        switch (slot){
            case 11: return 1;
            case 12: return 2;
            case 13: return 3;
            case 14: return 4;
            case 15: return 5;
        }

        return 1;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractWithForgeGUI(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();

        if(item == null) return;
        if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;

        if(e.getView().getTopInventory().getTitle().equalsIgnoreCase("The Forge")){
            if(item.hasItemMeta() && item.getItemMeta().hasLore()){
                String itemName = item.getItemMeta().getDisplayName();
                List<String> itemLore = item.getItemMeta().getLore();

                if(itemLore.contains(StringUtils.translate("&eClick to select a process!"))){
                    player.openInventory(new ProcessForgeGUI().GUI());

                } else if(itemName.equalsIgnoreCase(StringUtils.translate("&cClose"))){
                    player.closeInventory();
                } else if(itemLore.contains(StringUtils.translate("&aClick to claim!"))){

                    if(NBTEditor.contains(item, "item_id")){
                        String itemID = NBTEditor.getString(item, "item_id");

                        DwarvenUtils dwarvenUtils = new DwarvenUtils();
                        dwarvenUtils.resetPlayerForgeSlot(player, getClickedForgeSlot(e.getSlot()));

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                new MainForgeGUI().updateGUI(player);

                                if(ItemManager.getCustomItem(itemID) != null){
                                    IssueItemUtil.issueItemWithoutMessage(player, ItemManager.getCustomItem(itemID));
                                }
                            }
                        }.runTaskLater(plugin, 1L);


                    }

                }

            }

            e.setCancelled(true);
        } else if(e.getView().getTopInventory().getTitle().equalsIgnoreCase("Select Process")){
            if(item.hasItemMeta() && item.getItemMeta().hasLore()){
                String itemName = item.getItemMeta().getDisplayName();

                if(itemName.equalsIgnoreCase(StringUtils.translate("&cClose"))){
                    player.closeInventory();

                } else if(itemName.equalsIgnoreCase(StringUtils.translate("&aGo Back"))){
                    player.openInventory(new MainForgeGUI().GUI(player));

                } else if(itemName.equalsIgnoreCase(StringUtils.translate("&aRefine Ore"))){
                    player.openInventory(new CategoryForgeGUI().GUI(player, ForgeItemCategory.REFINED, 1));

                } else if(itemName.equalsIgnoreCase(StringUtils.translate("&aItem Casting"))){
                    player.openInventory(new CategoryForgeGUI().GUI(player, ForgeItemCategory.CASTED, 1));
                }

            }

            e.setCancelled(true);

        } else if(e.getView().getTopInventory().getTitle().equalsIgnoreCase("Refine") ||
                e.getView().getTopInventory().getTitle().equalsIgnoreCase("Item Casting")){
            if(item.hasItemMeta() && item.getItemMeta().hasLore()){
                String itemName = item.getItemMeta().getDisplayName();
                List<String> itemLore = item.getItemMeta().getLore();

                if(itemName.equalsIgnoreCase(StringUtils.translate("&cClose"))){
                    player.closeInventory();

                } else if(itemName.equalsIgnoreCase(StringUtils.translate("&aGo Back"))){
                    player.openInventory(new ProcessForgeGUI().GUI());

                } else if(NBTEditor.contains(item, "item_id")){
                    if(itemLore.contains(StringUtils.translate("&eClick to start process!"))){

                        String itemID = NBTEditor.getString(item, "item_id");
                        boolean hasRequiredItems = true;

                        for(String requiredID : ForgeManager.getRegisteredForgeItems().get(itemID).requiredItems().keySet()){
                            if(ItemManager.getCustomItem(requiredID) != null){

                                int requiredItemCount = ForgeManager.getRegisteredForgeItems().get(itemID).requiredItems().get(requiredID);

                                HashMap<String, Integer> playerInventoryContents = new HashMap<>();

                                for(ItemStack inventoryItem : player.getInventory()){
                                    if(inventoryItem != null){
                                        if(NBTEditor.contains(inventoryItem, "item_id")){
                                            String invItemID = NBTEditor.getString(inventoryItem, "item_id");
                                            int stackAmt = inventoryItem.getAmount();

                                            if(!playerInventoryContents.containsKey(invItemID)){
                                                playerInventoryContents.put(invItemID, stackAmt);

                                            } else {
                                                int storedAmt = playerInventoryContents.get(invItemID);

                                                playerInventoryContents.put(invItemID, storedAmt + stackAmt);
                                            }
                                        }
                                    }

                                }

                                if(playerInventoryContents.containsKey(requiredID) && playerInventoryContents.get(requiredID) >= requiredItemCount){
                                    continue;
                                } else {
                                    hasRequiredItems = false;
                                }

                            }
                        }

                        if(hasRequiredItems){

                            for(String requiredID : ForgeManager.getRegisteredForgeItems().get(itemID).requiredItems().keySet()){
                                if(ItemManager.getCustomItem(requiredID) != null){

                                    int requiredItemCount = ForgeManager.getRegisteredForgeItems().get(itemID).requiredItems().get(requiredID);

                                    int removedStackAmount = 0;

                                    int currentSlot = 0;
                                    for(ItemStack inventoryItem : player.getInventory()){
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
                                                        player.getInventory().setItem(currentSlot, new ItemStack(Material.AIR));

                                                        removedStackAmount++;

                                                    }

                                                }
                                            }
                                        }

                                        currentSlot++;
                                    }

                                }
                            }

                            DwarvenUtils dwarvenUtils = new DwarvenUtils();

                            int firstEmptyForgeSlot = dwarvenUtils.getFirstEmptyForgeSlot(player);
                            ForgeItem forgeItem = ForgeManager.getRegisteredForgeItems().get(itemID);

                            dwarvenUtils.setPlayerForgeItemID(player, firstEmptyForgeSlot, itemID);

                            Date finishDate = new QuickForgeUpgrade().calculateFinishDate(player, forgeItem);

                            dwarvenUtils.setPlayerForgeFinishDate(player, firstEmptyForgeSlot, finishDate);

                            player.sendMessage(StringUtils.translate("&aSuccessfully started a forge process for creation of " + ItemManager.getCustomItem(itemID).getItemMeta().getDisplayName() + "&a!"));

                            player.updateInventory();
                            player.openInventory(new MainForgeGUI().GUI(player));

                        } else {

                            player.sendMessage(StringUtils.translate("&cYou don't have the required items to start this process!"));

                        }


                    }
                }

            }

            e.setCancelled(true);
        }
    }
}
