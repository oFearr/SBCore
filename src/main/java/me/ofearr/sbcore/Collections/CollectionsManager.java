package me.ofearr.sbcore.Collections;

import me.ofearr.customitems.Items.Materials.Nether.Obsidian;
import me.ofearr.sbcore.Collections.Mining.*;
import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CollectionsManager {

    private static HashMap<CollectionCategories, List<Collection>> registeredCollections = new HashMap<>();

    private static SBCore plugin = SBCore.plugin;

    public static void setRegisteredCollections(){
        for(CollectionCategories collectionCategory : CollectionCategories.getCollectionCategories()){
            registeredCollections.put(collectionCategory, new ArrayList<Collection>());
        }

        new CobblestoneCollection().registerCollection();
        new CoalCollection().registerCollection();
        new IronCollection().registerCollection();
        new GoldCollection().registerCollection();
        new RedstoneCollection().registerCollection();
        new LapisCollection().registerCollection();
        new DiamondCollection().registerCollection();
        new EmeraldCollection().registerCollection();
        new Obsidian().registerItem();
        new MithrilCollection().registerCollection();
        new HardstoneCollection().registerCollection();
        new GemstoneCollection().registerCollection();

    }

    public static void putRegisteredCollection(CollectionCategories collectionCategory, Collection collection){
        registeredCollections.get(collectionCategory).add(collection);
    }

    public static List<Collection> getRegisteredCollections(CollectionCategories collectionCategory){

        return registeredCollections.get(collectionCategory);
    }

    public static HashMap<CollectionCategories, List<Collection>> getAllRegisteredCollections(){

        return registeredCollections;
    }

    public static void handlePlayerItemCollection(Player player, HashMap<String, Integer> items) {

        for(List<Collection> categoryCollections : registeredCollections.values()){

            for(Collection currentCollection : categoryCollections){
                String requiredItemID = currentCollection.requiredItemID();

                for(String itemID : items.keySet()){

                    if(itemID.equalsIgnoreCase(requiredItemID)){
                        int itemAmount = items.get(itemID);

                        int newCollectionCount = getPlayerCollectionCount(currentCollection, player) + itemAmount;
                        updateCollectionDatabase(player, currentCollection, newCollectionCount);

                        int collectionLevel = getCollectionLevelFromCount(currentCollection, newCollectionCount);

                        String levelPermission = currentCollection.getLevelPermissionReward(collectionLevel);

                        try {
                            if(!player.hasPermission(levelPermission) && !(currentCollection.maxCollectionLevel() >= collectionLevel)){

                                Collection clonedCollection = currentCollection.getClass().newInstance();

                                clonedCollection.addDisplayItems(player);
                                ArrayList<ItemStack> collectionItems = clonedCollection.getDisplayItems();

                                if(collectionLevel >= collectionItems.size()){
                                    collectionLevel = collectionItems.size() -1;
                                }

                                String collectionLevelItemName = collectionItems.get(collectionLevel).getItemMeta().getDisplayName();

                                String levelUpMessage = StringUtils.translate("&e==========================================\n" +
                                        "&6&lCOLLECTION LEVEL UP &e" + clonedCollection.collectionName() + " " + collectionLevel + "\n" +
                                        "\n&a&lREWARD\n" +
                                        collectionLevelItemName + "\n" +
                                        "&e==========================================");

                                player.sendMessage(levelUpMessage);

                                String rewardCMD = "lp user " + player.getName() + " permission set " + levelPermission + " true";
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), rewardCMD);


                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }

                        break;
                    }

                }

            }
        }

    }


    public int getPlayerCollectionLevel(Collection collection, Player player){

        int count = getPlayerCollectionCount(collection, player);

        int level = 0;

        for(int i = 1; i < collection.maxCollectionLevel() + 1; i++){
            if(count > getRequiredCountForCollection(i)){
                level = i;
            }
        }

        return level;
    }

    public static int getCollectionLevelFromCount(Collection collection, int collectionCount){

        int level = 0;

        for(int i = 1; i < collection.maxCollectionLevel() + 1; i++){
            if(collectionCount > getRequiredCountForCollection(i)){
                level = i;
            }
        }

        return level;
    }

    public static int getPlayerCollectionCount(Collection collection, Player player){

        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        if(dataManager.getConfig().get("collections." + collection.collectionName() + ".value") == null){
            return 0;

        } else {
            return (int) dataManager.getConfig().get("collections." + collection.collectionName() + ".value");
        }

    }

    public static int getRequiredCountForCollection(int collectionLevel){

        return 50 * collectionLevel * ((100 * collectionLevel) / 5);
    }

    public static void updateCollectionDatabase(Player player, Collection collection, int newCollectionCount){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        dataManager.getConfig().set("collections." + collection.collectionName() + ".value", newCollectionCount);
        dataManager.saveConfig();

    }
}
