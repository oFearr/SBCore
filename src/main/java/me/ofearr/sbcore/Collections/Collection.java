package me.ofearr.sbcore.Collections;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public interface Collection {
    String collectionName();
    int maxCollectionLevel();
    //ID of the item which players must collect to advance this collection
    String requiredItemID();
    CollectionCategories collectionCategory();

    ItemStack collectionIcon();

    ArrayList<ItemStack> getDisplayItems();

    void addDisplayItems(Player player);
    void registerCollection();

    String getLevelPermissionReward(int collectionLevel);

}
