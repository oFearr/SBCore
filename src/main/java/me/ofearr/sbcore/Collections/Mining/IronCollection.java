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

public class IronCollection implements Collection {

    public ArrayList<ItemStack> displayItems = new ArrayList<>();

    @Override
    public String collectionName() {
        return "Iron";
    }

    @Override
    public int maxCollectionLevel() {
        return 9;
    }

    @Override
    public String requiredItemID() {
        return "iron_ore";
    }

    @Override
    public CollectionCategories collectionCategory() {
        return CollectionCategories.MINING;
    }

    @Override
    public ItemStack collectionIcon() {
        return new ItemStack(Material.IRON_ORE);
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
                levelLore.add(StringUtils.translate(" &9Iron Minion &7Recipes"));

            } else if(i == 2){
                levelLore.add(StringUtils.translate(" &fEnchanted Book (Protection IV) &7Recipe"));

            } else if(i == 3){
                levelLore.add(StringUtils.translate(" &aEnchanted Iron Ingot &7Recipe"));

            } else if(i == 4){
                levelLore.add(StringUtils.translate(" &9Enchanted Hopper &7Recipe"));

            } else if(i == 5){
                levelLore.add(StringUtils.translate(" &9Enchanted Iron Block &7Recipe"));

            } else if(i == 6){
                levelLore.add(StringUtils.translate(" &aPersonal Deletor 4000 &7Recipe"));

            } else if(i == 7){
                levelLore.add(StringUtils.translate(" &9Personal Deletor 5000 &7Recipe"));

            } else if(i == 8){
                levelLore.add(StringUtils.translate(" &5Personal Deletor 6000 &7Recipe"));

            } else if(i == 9){
                levelLore.add(StringUtils.translate(" &6Personal Deletor 7000 &7Recipe"));

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
            return "sbcore.crafts.minion.iron";

        } else if(collectionLevel == 2){
            return "sbcore.crafts.enchants.protection";

        } else if(collectionLevel == 3){
            return "sbcore.crafts.materials.enchantediron";

        } else if(collectionLevel == 4){
            return "sbcore.crafts.minion.upgrades.enchantedhopper";

        } else if(collectionLevel == 5){
            return "sbcore.crafts.materials.enchantedironblock";

        } else if(collectionLevel == 6){
            return "sbcore.crafts.misc.personaldeletor4";

        } else if(collectionLevel == 7){
            return "sbcore.crafts.misc.personaldeletor5";

        } else if(collectionLevel == 8){
            return "sbcore.crafts.misc.personaldeletor6";

        } else if(collectionLevel == 9){
            return "sbcore.crafts.misc.personaldeletor7";

        }

        return "";
    }
}
