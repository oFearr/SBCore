package me.ofearr.sbcore.Collections;

import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.SBCore;
import org.bukkit.entity.Player;

import java.util.List;

public class CollectionsUtil {

    private static SBCore plugin = SBCore.plugin;

    private static CollectionsManager collectionsManager = plugin.collectionsManager;

    public static Collection getCollectionFromName(String collectionName){
        Collection storedCollection;

        for(List<Collection> collectionCategoryList : collectionsManager.getAllRegisteredCollections().values()){

            for(Collection collection : collectionCategoryList){

                if(collection.collectionName().equalsIgnoreCase(collectionName)){
                    storedCollection = collection;

                    return storedCollection;
                }
            }
        }

        return null;
    }

    public static void increaseCollectionCount(Player player, Collection collection, int amountToIncreaseBy){

        int newCollectionCount = amountToIncreaseBy + plugin.collectionsManager.getPlayerCollectionCount(collection, player);

        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        dataManager.getConfig().set("collections." + collection.collectionName() + ".value", newCollectionCount);
        dataManager.saveConfig();

    }

}
