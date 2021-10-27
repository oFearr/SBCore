package me.ofearr.sbcore.GUI;

import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TelekinesisApplicationGUI {

    public Inventory GUI(){
        Inventory inv = Bukkit.createInventory(null, 54, "Apply Telekinesis Enchant");

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack applyItem = new ItemStack(Material.ANVIL);
        ItemStack closeItem = new ItemStack(Material.BARRIER);

        ItemMeta applyMeta = applyItem.getItemMeta();
        applyMeta.setDisplayName(StringUtils.translate("&aClick to apply Telekinesis!"));
        List<String> applyLore = new ArrayList<>();

        applyLore.add(" ");
        applyLore.add(StringUtils.translate("&7Click to apply the"));
        applyLore.add(StringUtils.translate("&aTelekinesis &7Enchantment to"));
        applyLore.add(StringUtils.translate("&7the item in the above slot."));
        applyLore.add(" ");
        applyLore.add(StringUtils.translate("&aCost: &6100 Coins"));

        applyMeta.setLore(applyLore);
        applyItem.setItemMeta(applyMeta);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));

        closeItem.setItemMeta(closeMeta);

        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, fillerItem);
        }

        inv.setItem(22, new ItemStack(Material.AIR));
        inv.setItem(31, applyItem);
        inv.setItem(49, closeItem);

        return inv;
    }
}
