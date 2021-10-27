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

public class CategoryCollectionMenu {

    public Inventory GUI(Player player, CollectionCategories category) throws IllegalAccessException, InstantiationException {
        Inventory collectionInv = Bukkit.createInventory(null, 54, "Collections");

        List<Collection> categoryCollections = CollectionsManager.getRegisteredCollections(category);

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemStack closeItem = new ItemStack(Material.BARRIER);

        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(StringUtils.translate("&aGo Back"));

        List<String> backLore = new ArrayList<>();

        backLore.add(StringUtils.translate("&7To SkyBlock Menu"));
        backMeta.setLore(backLore);

        backItem.setItemMeta(backMeta);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));
        closeItem.setItemMeta(closeMeta);

        List<Integer> availableSlots = new ArrayList<>();

        for(int i = 10; i < 43; i++){
            if(i == 17) i = 19;
            else if(i == 26) i = 28;
            else if(i == 35) i = 37;

            availableSlots.add(i);
        }


        for(int i = 0; i < collectionInv.getSize(); i++){
            collectionInv.setItem(i, fillerItem);
        }

        List<ItemStack> playerCollectionItems = new ArrayList<>();
        CollectionsManager collectionsManager = new CollectionsManager();

        for(Collection collection : categoryCollections){

            int currentCollectionLevel = collectionsManager.getPlayerCollectionLevel(collection, player);

            Collection newCollection = collection.getClass().newInstance();
            newCollection.addDisplayItems(player);

            ArrayList<ItemStack> collectionItems = newCollection.getDisplayItems();

            if(currentCollectionLevel >= collectionItems.size()){
                currentCollectionLevel = collectionItems.size() -1;
            }

            ItemStack targetCollectionItem = collectionItems.get(currentCollectionLevel);

            ItemStack newCollectionItem = newCollection.collectionIcon();

            if(newCollectionItem.getItemMeta() instanceof SkullMeta){
                ItemMeta itemMeta = newCollectionItem.getItemMeta();

                itemMeta.setLore(targetCollectionItem.getItemMeta().getLore());
                itemMeta.setDisplayName(targetCollectionItem.getItemMeta().getDisplayName());

                newCollectionItem.setItemMeta(itemMeta);
            } else {
                newCollectionItem.setItemMeta(targetCollectionItem.getItemMeta());
            }


            newCollectionItem = NBTEditor.set(newCollectionItem, collection.collectionName(), "collection_name");
            playerCollectionItems.add(newCollectionItem);
        }

        for(int i = 0; i < availableSlots.size(); i++){

            if(playerCollectionItems.size() > 0){

                collectionInv.setItem(availableSlots.get(i), playerCollectionItems.get(0));

                playerCollectionItems.remove(0);
            }

        }

        collectionInv.setItem(48, backItem);
        collectionInv.setItem(49, closeItem);

        return collectionInv;
    }
}
