package me.ofearr.sbcore.Dwarven.Forge.GUI;

import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ProcessForgeGUI {

    public Inventory GUI(){
        Inventory inv = Bukkit.createInventory(null, 54, "Select Process");

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemStack refineItem = new ItemStack(Material.LAVA_BUCKET);
        ItemStack castItem = new ItemStack(Material.FURNACE);

        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(StringUtils.translate("&aGo Back"));
        List<String> backLore = new ArrayList<>();

        backLore.add(StringUtils.translate("&7To The Forge"));
        backMeta.setLore(backLore);
        backItem.setItemMeta(backMeta);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));
        closeItem.setItemMeta(closeMeta);

        ItemMeta refineMeta = refineItem.getItemMeta();
        refineMeta.setDisplayName(StringUtils.translate("&aRefine Ore"));
        List<String> refineLore = new ArrayList<>();

        refineLore.add(StringUtils.translate("&7Refine your ores into more"));
        refineLore.add(StringUtils.translate("&7valuable materials."));
        refineLore.add(" ");
        refineLore.add(StringUtils.translate("&eClick to select!"));

        refineMeta.setLore(refineLore);
        refineItem.setItemMeta(refineMeta);

        ItemMeta castMeta = castItem.getItemMeta();
        castMeta.setDisplayName(StringUtils.translate("&aItem Casting"));
        List<String> castLore = new ArrayList<>();

        castLore.add(StringUtils.translate("&7Cast your ores into"));
        castLore.add(StringUtils.translate("&7powerful items with unique"));
        castLore.add(StringUtils.translate("&7properties."));
        castLore.add(" ");
        castLore.add(StringUtils.translate("&eClick to select!"));

        castMeta.setLore(refineLore);
        castItem.setItemMeta(castMeta);

        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, fillerItem);
        }

        inv.setItem(20, refineItem);
        inv.setItem(24, castItem);

        inv.setItem(49, closeItem);
        inv.setItem(48, backItem);

        return inv;
    }
}
