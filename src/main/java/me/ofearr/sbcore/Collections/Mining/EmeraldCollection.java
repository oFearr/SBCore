package me.ofearr.sbcore.Collections.Mining;

import me.ofearr.sbcore.Collections.Collection;
import me.ofearr.sbcore.Collections.CollectionCategories;
import me.ofearr.sbcore.Collections.CollectionsManager;
import me.ofearr.sbcore.Utils.NumbAbbreviation;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EmeraldCollection implements Collection {

    public ArrayList<ItemStack> displayItems = new ArrayList<>();

    @Override
    public String collectionName() {
        return "Emerald";
    }

    @Override
    public int maxCollectionLevel() {
        return 7;
    }

    @Override
    public String requiredItemID() {
        return "emerald";
    }

    @Override
    public CollectionCategories collectionCategory() {
        return CollectionCategories.MINING;
    }

    @Override
    public ItemStack collectionIcon() {
        return new ItemStack(Material.EMERALD);
    }

    @Override
    public ArrayList<ItemStack> getDisplayItems() {
        return this.displayItems;
    }

    @Override
    public void addDisplayItems(Player player) {

        displayItems.clear();

        CollectionsManager collectionsManager = new CollectionsManager();
        int playerCollectionLevel = collectionsManager.getPlayerCollectionLevel(this, player);
        int playerCollectionCount = collectionsManager.getPlayerCollectionCount(this, player);

        for(int i = 1; i <= maxCollectionLevel(); i++){
            ItemStack levelStack = new ItemStack(Material.STAINED_GLASS_PANE, i, (short) 14);

            ItemMeta levelMeta = levelStack.getItemMeta();
            if(i <= playerCollectionLevel){

                levelStack = new ItemStack(Material.STAINED_GLASS_PANE, i, (short) 5);
                levelMeta = levelStack.getItemMeta();
                levelMeta.setDisplayName(StringUtils.translate("&a") + collectionName() + " Level " + i);

            } else if(i == playerCollectionLevel + 1){

                levelStack = new ItemStack(Material.STAINED_GLASS_PANE, i, (short) 4);
                levelMeta = levelStack.getItemMeta();
                levelMeta.setDisplayName(StringUtils.translate("&e") + collectionName() + " Level " + i);
            } else {

                levelMeta.setDisplayName(StringUtils.translate("&c") + collectionName() + " Level " + i);
            }

            List<String> levelLore = new ArrayList<>();

            levelLore.add(" ");
            double percentFinishedCollectionLevel = (double) playerCollectionCount / (double) collectionsManager.getRequiredCountForCollection(i) * 100;

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.applyPattern("#,###.#");

            DecimalFormat percentageFormat = new DecimalFormat();
            percentageFormat.applyPattern("#.#");

            if(percentFinishedCollectionLevel > 100){
                percentFinishedCollectionLevel = 100;
            }

            if(playerCollectionLevel >= i){
                levelLore.add(StringUtils.translate("&7Progress to " + collectionName() + " " + i + ": &a" + percentageFormat.format(percentFinishedCollectionLevel) + "%"));

            } else {
                levelLore.add(StringUtils.translate("&7Progress to " + collectionName() + " " + i + ": &e" + percentageFormat.format(percentFinishedCollectionLevel) + "%"));

            }

            levelLore.add(StringUtils.translate("&7Total Obtained: &e" + decimalFormat.format(playerCollectionCount) + "&6/&a" + NumbAbbreviation.formatNumber(collectionsManager.getRequiredCountForCollection(i))));

            levelLore.add(" ");

            levelLore.add(StringUtils.translate("&7Reward(s):"));

            if(i == 1){
                levelLore.add(StringUtils.translate(" &9Emerald Minion &7Recipes"));

            } else if(i == 2){
                levelLore.add(StringUtils.translate(" &fTalisman of Coins &7Recipe"));

            } else if(i == 3){
                levelLore.add(StringUtils.translate(" &aMagnetic Talisman &7Recipe"));

            } else if(i == 4){
                levelLore.add(StringUtils.translate(" &aEnchanted Emerald &7Recipe"));

            } else if(i == 5){
                levelLore.add(StringUtils.translate(" &aEmerald Ring &7Recipe"));

            } else if(i == 6){
                levelLore.add(StringUtils.translate(" &9Enchanted Emerald Block &7Recipe"));

            } else if(i == 7){
                levelLore.add(StringUtils.translate(" &5Emerald Blade &7Recipe"));

            }

            levelMeta.setLore(levelLore);

            levelMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            levelMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            levelStack.setItemMeta(levelMeta);

            displayItems.add(levelStack);

        }
    }

    @Override
    public void registerCollection() {
        CollectionsManager.putRegisteredCollection(collectionCategory(), this);
    }

    @Override
    public String getLevelPermissionReward(int collectionLevel) {

        if(collectionLevel == 1){
            return "sbcore.crafts.minion.emerald";

        } else if(collectionLevel == 2){
            return "sbcore.crafts.accessories.cointalisman";

        } else if(collectionLevel == 3){
            return "sbcore.crafts.accessories.magnetictalisman";

        } else if(collectionLevel == 4){
            return "sbcore.crafts.materials.enchantedemerald";

        } else if(collectionLevel == 5){
            return "sbcore.crafts.accessories.emeraldring";

        } else if(collectionLevel == 6){
            return "sbcore.crafts.materials.enchantedemeraldblock";

        } else if(collectionLevel == 7){
            return "sbcore.crafts.weapons.emeraldblade";

        }

        return "";
    }
}
