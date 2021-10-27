package me.ofearr.sbcore.GUI.CollectionInventories;

import me.ofearr.sbcore.Collections.Collection;
import me.ofearr.sbcore.Collections.CollectionCategories;
import me.ofearr.sbcore.Collections.CollectionsManager;
import me.ofearr.sbcore.Utils.NBTEditor;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class CollectionSpecificMenu {

    public Inventory GUI(Player player, Collection collection){

        Inventory inv = Bukkit.createInventory(null, 54, collection.collectionName() + " Collection");

        CollectionCategories collectionCategory = collection.collectionCategory();

        collection.addDisplayItems(player);
        ArrayList<ItemStack> displayItems = new ArrayList<>(collection.getDisplayItems());

        CollectionsManager collectionsManager = new CollectionsManager();
        int playerCollectionLevel = collectionsManager.getPlayerCollectionLevel(collection, player);

        if(playerCollectionLevel >= displayItems.size()){
            playerCollectionLevel = displayItems.size() -1;
        }

        ItemStack tempCollectionItem = displayItems.get(playerCollectionLevel);

        ItemStack collectionItem = collection.collectionIcon();

        if(collectionItem.getItemMeta() instanceof SkullMeta){
            ItemMeta itemMeta = collectionItem.getItemMeta();

            itemMeta.setLore(tempCollectionItem.getItemMeta().getLore());
            itemMeta.setDisplayName(tempCollectionItem.getItemMeta().getDisplayName());

            collectionItem.setItemMeta(itemMeta);
        } else {
            collectionItem.setItemMeta(tempCollectionItem.getItemMeta());
        }

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack categoryItem = new ItemStack(Material.ARROW);
        ItemStack closeItem = new ItemStack(Material.BARRIER);

        ItemMeta skillsMeta = categoryItem.getItemMeta();
        skillsMeta.setDisplayName(StringUtils.translate("&aGo Back"));
        List<String> skillsLore = new ArrayList<>();

        skillsLore.add(StringUtils.translate("&7To " + collectionCategory.toString() + " Collections"));
        skillsMeta.setLore(skillsLore);
        categoryItem.setItemMeta(skillsMeta);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));
        closeItem.setItemMeta(closeMeta);

        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, fillerItem);
        }

        int[] trackSlots = {19, 20, 21, 22, 23, 24,
                25, 28, 29, 30, 31, 32, 33, 34,
                37, 38, 39, 40, 41, 42, 43};

        for(int i = 0; i < trackSlots.length; i++){
            inv.setItem(trackSlots[i], new ItemStack(Material.AIR));
        }

        inv.setItem(4, collectionItem);
        inv.setItem(48, categoryItem);
        inv.setItem(50, closeItem);

        for(int i = 0; i < displayItems.size(); i++){

            if(inv.firstEmpty() != -1){
                inv.setItem(inv.firstEmpty(), displayItems.get(i));
            }

        }

        for(int i = 0; i < inv.getSize(); i++){
            ItemStack item = inv.getItem(i);

            if(item == null || item.getType() == Material.AIR){
                inv.setItem(i, fillerItem);
            }
        }

        return inv;
    }
}
