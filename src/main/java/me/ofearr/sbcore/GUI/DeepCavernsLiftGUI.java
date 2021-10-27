package me.ofearr.sbcore.GUI;

import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DeepCavernsLiftGUI {

    public Inventory GUI(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, "Select A Destination!");

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack coalItem = new ItemStack(Material.COAL);
        ItemStack lapisItem = new ItemStack(Material.INK_SACK, 1, (short) 4);
        ItemStack redstoneItem = new ItemStack(Material.REDSTONE);
        ItemStack emeraldItem = new ItemStack(Material.EMERALD);
        ItemStack diamondItem = new ItemStack(Material.DIAMOND);
        ItemStack obsidianItem = new ItemStack(Material.OBSIDIAN);
        ItemStack dwarvenItem = new ItemStack(Material.PRISMARINE);


        ItemMeta templateMeta = coalItem.getItemMeta();
        templateMeta.setDisplayName(StringUtils.translate("&aGunpowder Mines"));

        List<String> templateLore = new ArrayList<>();

        templateLore.add(StringUtils.translate("&7Teleports you to the &bGunpowder"));
        templateLore.add(StringUtils.translate("&bMines&7!"));
        templateLore.add(" ");
        templateLore.add(StringUtils.translate("&eClick to travel!"));

        templateMeta.setLore(templateLore);

        coalItem.setItemMeta(templateMeta);
        templateLore.clear();

        templateMeta.setDisplayName(StringUtils.translate("&aLapis Quarry"));

        templateLore.add(StringUtils.translate("&7Teleports you to the &bLapis Quarry&7!"));
        templateLore.add(" ");
        templateLore.add(StringUtils.translate("&eClick to travel!"));

        templateMeta.setLore(templateLore);

        lapisItem.setItemMeta(templateMeta);
        templateLore.clear();

        templateMeta.setDisplayName(StringUtils.translate("&aPigman's Den"));

        templateLore.add(StringUtils.translate("&7Teleports you to the &bPigman's Den&7!"));
        templateLore.add(" ");
        templateLore.add(StringUtils.translate("&eClick to travel!"));

        templateMeta.setLore(templateLore);

        redstoneItem.setItemMeta(templateMeta);
        templateLore.clear();

        templateMeta.setDisplayName(StringUtils.translate("&aSlimehill"));

        templateLore.add(StringUtils.translate("&7Teleports you to &bSlimehill&7!"));
        templateLore.add(" ");
        templateLore.add(StringUtils.translate("&eClick to travel!"));

        templateMeta.setLore(templateLore);

        emeraldItem.setItemMeta(templateMeta);
        templateLore.clear();

        templateMeta.setDisplayName(StringUtils.translate("&aDiamond Reserve"));

        templateLore.add(StringUtils.translate("&7Teleports you to the &bDiamond"));
        templateLore.add(StringUtils.translate("&bReserve&7!"));
        templateLore.add(" ");
        templateLore.add(StringUtils.translate("&eClick to travel!"));

        templateMeta.setLore(templateLore);

        diamondItem.setItemMeta(templateMeta);
        templateLore.clear();

        templateMeta.setDisplayName(StringUtils.translate("&aObsidian Sanctuary"));

        templateLore.add(StringUtils.translate("&7Teleports you to the &bObsidian"));
        templateLore.add(StringUtils.translate("&bSanctuary&7!"));
        templateLore.add(" ");
        templateLore.add(StringUtils.translate("&eClick to travel!"));

        templateMeta.setLore(templateLore);

        obsidianItem.setItemMeta(templateMeta);
        templateLore.clear();

        templateMeta.setDisplayName(StringUtils.translate("&aDwarven Mines"));

        templateLore.add(StringUtils.translate("&7Teleports you to the &bDwarven Mines&7!"));
        templateLore.add(" ");
        templateLore.add(StringUtils.translate("&cComing soon!"));

        templateMeta.setLore(templateLore);

        dwarvenItem.setItemMeta(templateMeta);
        templateLore.clear();


        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, fillerItem);
        }

        inv.setItem(11, coalItem);

        if(player.hasPermission("sbcore.deepcaverns.lapis")){
            inv.setItem(13, lapisItem);
        }

        if(player.hasPermission("sbcore.deepcaverns.redstone")){
            inv.setItem(15, redstoneItem);
        }

        if(player.hasPermission("sbcore.deepcaverns.emerald")){
            inv.setItem(22, emeraldItem);
        }

        if(player.hasPermission("sbcore.deepcaverns.diamond")){
            inv.setItem(29, diamondItem);
        }

        if(player.hasPermission("sbcore.deepcaverns.obsidian")){
            inv.setItem(31, obsidianItem);
        }

        if(player.hasPermission("sbcore.deepcaverns.dwarven")){
            inv.setItem(33, dwarvenItem);
        }

        return inv;
    }
}
