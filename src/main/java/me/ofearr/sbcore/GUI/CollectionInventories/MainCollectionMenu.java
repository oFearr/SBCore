package me.ofearr.sbcore.GUI.CollectionInventories;

import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MainCollectionMenu {

    public Inventory GUI(){
        Inventory collectionInv = Bukkit.createInventory(null, 54, "Collections");

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);

        ItemStack farmingCollectionItem = new ItemStack(Material.GOLD_HOE);
        ItemStack miningCollectionItem = new ItemStack(Material.STONE_PICKAXE);
        ItemStack combatCollectionItem = new ItemStack(Material.STONE_SWORD);
        ItemStack foragingCollectionItem = new ItemStack(Material.SAPLING, 1, (short) 3);
        ItemStack fishingCollectionItem = new ItemStack(Material.FISHING_ROD);
        ItemStack bossCollectionItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);

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

        ItemMeta collectionTemplateMeta = farmingCollectionItem.getItemMeta();
        collectionTemplateMeta.setDisplayName(StringUtils.translate("&aFarming Collections"));

        collectionTemplateMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES);

        List<String> collectionTemplateLore = new ArrayList<>();

        collectionTemplateLore.add(" ");
        collectionTemplateLore.add(StringUtils.translate("&eClick to view!"));
        collectionTemplateMeta.setLore(collectionTemplateLore);

        farmingCollectionItem.setItemMeta(collectionTemplateMeta);

        collectionTemplateMeta.setDisplayName(StringUtils.translate("&aMining Collections"));
        miningCollectionItem.setItemMeta(collectionTemplateMeta);

        collectionTemplateMeta.setDisplayName(StringUtils.translate("&aCombat Collections"));
        combatCollectionItem.setItemMeta(collectionTemplateMeta);

        collectionTemplateMeta.setDisplayName(StringUtils.translate("&aForaging Collections"));
        foragingCollectionItem.setItemMeta(collectionTemplateMeta);

        collectionTemplateMeta.setDisplayName(StringUtils.translate("&aFishing Collections"));
        fishingCollectionItem.setItemMeta(collectionTemplateMeta);

        collectionTemplateMeta.setDisplayName(StringUtils.translate("&cBoss Collections"));
        bossCollectionItem.setItemMeta(collectionTemplateMeta);

        for(int i = 0; i < collectionInv.getSize(); i++){
            collectionInv.setItem(i, fillerItem);
        }

        collectionInv.setItem(20, farmingCollectionItem);
        collectionInv.setItem(21, miningCollectionItem);
        collectionInv.setItem(22, combatCollectionItem);
        collectionInv.setItem(23, foragingCollectionItem);
        collectionInv.setItem(24, fishingCollectionItem);
        collectionInv.setItem(31, bossCollectionItem);

        collectionInv.setItem(48, backItem);
        collectionInv.setItem(49, closeItem);

        return collectionInv;
    }
}
