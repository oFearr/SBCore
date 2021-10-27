package me.ofearr.sbcore.Dwarven.Forge.GUI;

import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.customitems.Utils.GUIPagingUtil;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.Dwarven.Forge.ForgeItemCategory;
import me.ofearr.sbcore.Dwarven.Forge.ForgeManager;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier2Upgrades.QuickForgeUpgrade;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CategoryForgeGUI {

    public Inventory GUI(Player player, ForgeItemCategory category, int page){
        Inventory inv = null;

        if(category == ForgeItemCategory.REFINED){
            inv = Bukkit.createInventory(null, 54, "Refine");
        } else {
            inv = Bukkit.createInventory(null, 54, "Item Casting");
        }

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemStack backPageItem = new ItemStack(Material.ARROW);
        ItemStack forwardItem = new ItemStack(Material.ARROW);
        ItemStack closeItem = new ItemStack(Material.BARRIER);

        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(StringUtils.translate("&aGo Back"));
        List<String> backLore = new ArrayList<>();

        backLore.add(StringUtils.translate("&7To Select Process"));
        backMeta.setLore(backLore);
        backItem.setItemMeta(backMeta);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));
        closeItem.setItemMeta(closeMeta);

        ItemMeta backPageMeta = backItem.getItemMeta();
        backMeta.setDisplayName(StringUtils.translate("&aClick to go back a page."));
        List<String> backPageLore = new ArrayList<>();
        backPageLore.add(" ");
        backPageLore.add(StringUtils.translate("&bGo to page " + (page - 1)));
        backPageMeta.setLore(backPageLore);
        backPageItem.setItemMeta(backPageMeta);

        ItemMeta forwardMeta = forwardItem.getItemMeta();
        forwardMeta.setDisplayName(StringUtils.translate("&aClick to go forward a page."));
        List<String> forwardLore = new ArrayList<>();
        forwardLore.add(" ");
        forwardLore.add(StringUtils.translate("&bGo to page " + (page + 1)));
        forwardMeta.setLore(forwardLore);
        forwardItem.setItemMeta(forwardMeta);

        List<ItemStack> categoryForgeItems = new ArrayList<>();

        DwarvenUtils dwarvenUtils = new DwarvenUtils();

        int playerHotMLevel = dwarvenUtils.getHOTMLevel(player);

        for(ForgeItem forgeItem : ForgeManager.getRegisteredForgeItems().values()){
            if(forgeItem.forgeItemCategory() == category) {

                String forgeItemID = forgeItem.itemID();

                if(ItemManager.getCustomItem(forgeItemID) != null){
                    ItemStack forgableItem = ItemManager.getCustomItem(forgeItemID);

                    ItemMeta itemMeta = forgableItem.getItemMeta();
                    List<String> itemLore = itemMeta.getLore();

                    itemLore.add(StringUtils.translate(" "));
                    itemLore.add(StringUtils.translate("&eItems Required"));

                    for(String requiredId : forgeItem.requiredItems().keySet()){
                        if(ItemManager.getCustomItem(requiredId) != null){
                            ItemStack requiredItem = ItemManager.getCustomItem(requiredId);

                            itemLore.add(StringUtils.translate(requiredItem.getItemMeta().getDisplayName() + " &8x" + forgeItem.requiredItems().get(requiredId)));
                        }
                    }

                    int days = forgeItem.durationDays();
                    int hours = forgeItem.durationHours();
                    int minutes = forgeItem.durationMins();
                    int seconds = forgeItem.durationSeconds();

                    int requiredHotM = forgeItem.requiredHotM();

                    String durationString = "";

                    if(days >= 1){
                        durationString = durationString + days + "d, ";
                    }

                    if(hours >= 1){
                        durationString = durationString + hours + "h, ";
                    }

                    if(minutes >= 1){
                        durationString = durationString + minutes + "m, ";
                    }

                    if(seconds >= 1){
                        durationString = durationString + seconds + "s";
                    }

                    if(durationString.charAt(durationString.length()-2) == ','){
                        durationString = durationString.substring(0, durationString.length() - 2);
                    }

                    itemLore.add(" ");
                    itemLore.add(StringUtils.translate("&7Duration: &b" + durationString));
                    itemLore.add(" ");

                    if(playerHotMLevel >= requiredHotM){
                        itemLore.add(StringUtils.translate("&eClick to start process!"));
                    } else {
                        itemLore.add(StringUtils.translate("&cRequires HotM Lvl " + requiredHotM));
                    }


                    itemMeta.setLore(itemLore);
                    forgableItem.setItemMeta(itemMeta);

                    categoryForgeItems.add(forgableItem);
                }

            }
        }

        if(GUIPagingUtil.isPageValid(categoryForgeItems, page -1, 28)){
            inv.setItem(45, backItem);
        }

        if(GUIPagingUtil.isPageValid(categoryForgeItems, page +1, 28)){
            inv.setItem(53, forwardItem);
        }

        List<Integer> availableSlots = new ArrayList<>();

        for(int i = 10; i < 43; i++){
            if(i == 17) i = 19;
            else if(i == 26) i = 28;
            else if(i == 35) i = 37;

            availableSlots.add(i);
        }

        int slot = 0;
        for(ItemStack customItem : GUIPagingUtil.getPageItems(categoryForgeItems, page, 45)){
            if(customItem.getType() != Material.AIR){
                inv.setItem(availableSlots.get(slot), customItem);

                slot++;
            }

        }

        for(int i = 0; i < inv.getSize(); i++){
            if(inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR){
                inv.setItem(i, fillerItem);
            }
        }

        inv.setItem(48, backItem);
        inv.setItem(49,closeItem);

        return inv;
    }
}
