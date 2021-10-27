package me.ofearr.sbcore.Shop;

import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.sbcore.CustomConfigs.ShopConfig;
import me.ofearr.sbcore.Utils.NBTEditor;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ShopGUI {

    public Inventory GUI(String shopID){
        String title = "Shop";
        ShopConfig shopConfig = new ShopConfig();
        shopConfig.load();

        if(shopConfig.getConfig().get("shops." + shopID + ".title") != null){
            title = StringUtils.translate(shopConfig.getConfig().getString("shops." + shopID + ".title"));
        }

        Inventory inv = Bukkit.createInventory(null, 54, title);

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

        fillerItem = NBTEditor.set(fillerItem, shopID, "shop_id");

        List<Integer> availableSlots = new ArrayList<>();

        for(int i = 10; i < 43; i++){
            if(i == 17) i = 19;
            else if(i == 26) i = 28;
            else if(i == 35) i = 37;

            availableSlots.add(i);
        }

        for(int i = 0; i < inv.getSize(); i++){
            if(!availableSlots.contains(i)) inv.setItem(i, fillerItem);
        }

        //Ensures that it's not trying to access a null shop
        if(shopConfig.getConfig().get("shops." + shopID + ".title") != null){

            for(String itemID : shopConfig.getConfig().getConfigurationSection("shops." + shopID + ".items").getKeys(false)){
                int amount = shopConfig.getConfig().getInt("shops." + shopID + ".items." + itemID + ".amount");

                if(ItemManager.getCustomItem(itemID, amount) != null){
                    ItemStack physicalItem = ItemManager.getCustomItem(itemID, amount);

                    ItemMeta itemMeta = physicalItem.getItemMeta();
                    List<String> itemLore = itemMeta.getLore();

                    DecimalFormat decimalFormat = new DecimalFormat();
                    decimalFormat.applyPattern("#,###");

                    itemLore.add(" ");

                    int cost = shopConfig.getConfig().getInt("shops." + shopID + ".items." + itemID + ".coins");

                    itemLore.add(StringUtils.translate("&7Cost"));
                    if(cost > 0){
                        itemLore.add(StringUtils.translate("&6" + decimalFormat.format(cost) + " Coins"));
                    }

                    for(String requiredItemId :shopConfig.getConfig().getConfigurationSection("shops." + shopID + ".items." + itemID + ".items").getKeys(false)){
                        int requiredAmt = shopConfig.getConfig().getInt("shops." + shopID + ".items." + itemID + ".items." + requiredItemId);

                        if(ItemManager.getCustomItem(requiredItemId, requiredAmt) != null){
                            ItemStack requiredItem = ItemManager.getCustomItem(requiredItemId, requiredAmt);

                            String requiredItemName = requiredItem.getItemMeta().getDisplayName();

                            itemLore.add(StringUtils.translate(requiredItemName + " &8x" + decimalFormat.format(requiredAmt)));
                        }

                    }

                    itemMeta.setLore(itemLore);
                    physicalItem.setItemMeta(itemMeta);


                    if(inv.firstEmpty() != -1){
                        inv.setItem(inv.firstEmpty(), physicalItem);
                    }
                }

            }
        }

        ItemStack sellItem = new ItemStack(Material.HOPPER);
        ItemMeta sellMeta = sellItem.getItemMeta();
        sellMeta.setDisplayName(StringUtils.translate("&aSell Item"));

        List<String> sellLore = new ArrayList<>();

        sellLore.add(StringUtils.translate("&7Click items in your inventory to"));
        sellLore.add(StringUtils.translate("&7sell them to this Shop!"));

        sellMeta.setLore(sellLore);
        sellItem.setItemMeta(sellMeta);

        inv.setItem(49, sellItem);

        return inv;
    }
}
