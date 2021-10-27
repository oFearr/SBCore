package me.ofearr.sbcore.Dwarven.Forge.GUI;

import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainForgeGUI {

    private static SBCore plugin = SBCore.plugin;

    public void updateGUI(Player player){
        if(player.getOpenInventory().getTopInventory().getTitle().equalsIgnoreCase("The Forge")){

            Inventory inv = player.getOpenInventory().getTopInventory();
            ItemStack innerFillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);

            DwarvenUtils dwarvenUtils = new DwarvenUtils();

            int playerForgeSlots = dwarvenUtils.getPlayerForgeSlotCount(player);

            final int[] forgeInventorySlots = {11,12,13,14,15};

            for(int i = 1; i < 6; i++){
                ItemStack forgeItem = null;

                int forgeInventorySlot = forgeInventorySlots[i-1];

                String forgeItemID = dwarvenUtils.getPlayerForgeItemID(player, i);

                if(forgeItemID == null) forgeItemID = "";

                if(forgeItemID == "" || ItemManager.getCustomItem(forgeItemID) == null){
                    forgeItem = new ItemStack(Material.FURNACE);

                    ItemMeta itemMeta = forgeItem.getItemMeta();
                    itemMeta.setDisplayName(StringUtils.translate("&aSlot #" + i));

                    List<String> itemLore = new ArrayList<>();
                    itemLore.add(StringUtils.translate("&7View and start forge"));
                    itemLore.add(StringUtils.translate("&7processes using the"));
                    itemLore.add(StringUtils.translate("&7materials that you have."));
                    itemLore.add(" ");

                    if(playerForgeSlots >= i){
                        itemLore.add(StringUtils.translate("&eClick to select a process!"));
                    } else {
                        itemLore.add(StringUtils.translate("&cLOCKED"));
                    }


                    itemMeta.setLore(itemLore);
                    forgeItem.setItemMeta(itemMeta);

                    inv.setItem(forgeInventorySlot + 9, innerFillerItem);
                    inv.setItem(forgeInventorySlot + 18, innerFillerItem);
                    inv.setItem(forgeInventorySlot + 27, innerFillerItem);

                } else {
                    try {
                        forgeItem = ItemManager.getCustomItem(forgeItemID).clone();


                        Date finishDate = dwarvenUtils.getPlayerForgeFinishDate(player, i);

                        Date now = new Date();

                        long difference = finishDate.getTime() - now.getTime();

                        String durationString = "";

                        long days = (difference / (1000 * 60 * 60 * 24)) % 365;
                        long hours = (difference / (1000 * 60 * 60)) % 24;
                        long minutes = (difference / (1000 * 60)) % 60;
                        long seconds = (difference / 1000) % 60;

                        if(days >= 1){
                            durationString = durationString + days + "d, ";
                        }

                        if(hours >= 1){
                            durationString = durationString + hours + "h, ";
                        }

                        if(minutes >= 1){
                            durationString = durationString + minutes + "m, ";
                        }

                        if(seconds >= 1){
                            durationString = durationString + seconds + "s";
                        }

                        ItemMeta itemMeta = forgeItem.getItemMeta();
                        itemMeta.setDisplayName(StringUtils.translate("&aSlot #" + i + " (" + itemMeta.getDisplayName() + "&a)"));

                        List<String> itemLore = new ArrayList<>();

                        itemLore.add(" ");
                        itemLore.add(StringUtils.translate("&7You're currently forging this"));
                        itemLore.add(StringUtils.translate("&7item."));
                        itemLore.add(" ");

                        if(finishDate.before(new Date())){
                            itemLore.add(StringUtils.translate("&aClick to claim!"));

                            ItemStack finishedInnerFiller = innerFillerItem.clone();
                            finishedInnerFiller.setDurability((short) 5);

                            inv.setItem(forgeInventorySlot + 9, finishedInnerFiller);
                            inv.setItem(forgeInventorySlot + 18, finishedInnerFiller);
                            inv.setItem(forgeInventorySlot + 27, finishedInnerFiller);
                        } else {

                            itemLore.add(StringUtils.translate("&aTime Left: " + durationString));

                            ItemStack unfinishedInnerFiller = innerFillerItem.clone();
                            unfinishedInnerFiller.setDurability((short) 4);

                            inv.setItem(forgeInventorySlot + 9, unfinishedInnerFiller);
                            inv.setItem(forgeInventorySlot + 18, unfinishedInnerFiller);
                            inv.setItem(forgeInventorySlot + 27, unfinishedInnerFiller);
                        }

                        itemMeta.setLore(itemLore);
                        forgeItem.setItemMeta(itemMeta);


                    }catch (ParseException e){
                        e.printStackTrace();
                    }

                }

                inv.setItem(forgeInventorySlot, forgeItem);

            }
        }
    }

    public Inventory GUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, "The Forge");

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack innerFillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemStack closeItem = new ItemStack(Material.BARRIER);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));

        closeItem.setItemMeta(closeMeta);

        DwarvenUtils dwarvenUtils = new DwarvenUtils();

        int playerForgeSlots = dwarvenUtils.getPlayerForgeSlotCount(player);

       final int[] forgeInventorySlots = {11,12,13,14,15};

        for(int i = 1; i < 6; i++){
            ItemStack forgeItem = null;

            int forgeInventorySlot = forgeInventorySlots[i-1];

            String forgeItemID = dwarvenUtils.getPlayerForgeItemID(player, i);

            if(forgeItemID == null) forgeItemID = "";

            if(forgeItemID == "" || ItemManager.getCustomItem(forgeItemID) == null){
                forgeItem = new ItemStack(Material.FURNACE);

                ItemMeta itemMeta = forgeItem.getItemMeta();
                itemMeta.setDisplayName(StringUtils.translate("&aSlot #" + i));

                List<String> itemLore = new ArrayList<>();
                itemLore.add(StringUtils.translate("&7View and start forge"));
                itemLore.add(StringUtils.translate("&7processes using the"));
                itemLore.add(StringUtils.translate("&7materials that you have."));
                itemLore.add(" ");

                if(playerForgeSlots >= i){
                    itemLore.add(StringUtils.translate("&eClick to select a process!"));
                } else {
                    itemLore.add(StringUtils.translate("&cLOCKED"));
                }


                itemMeta.setLore(itemLore);
                forgeItem.setItemMeta(itemMeta);

                inv.setItem(forgeInventorySlot + 9, innerFillerItem);
                inv.setItem(forgeInventorySlot + 18, innerFillerItem);
                inv.setItem(forgeInventorySlot + 27, innerFillerItem);

            } else {
                try {
                    forgeItem = ItemManager.getCustomItem(forgeItemID).clone();


                    Date finishDate = dwarvenUtils.getPlayerForgeFinishDate(player, i);

                    Date now = new Date();

                    long difference = finishDate.getTime() - now.getTime();

                    String durationString = "";

                    long days = (difference / (1000 * 60 * 60 * 24)) % 365;
                    long hours = (difference / (1000 * 60 * 60)) % 24;
                    long minutes = (difference / (1000 * 60)) % 60;
                    long seconds = (difference / 1000) % 60;

                    if(days >= 1){
                        durationString = durationString + days + "d, ";
                    }

                    if(hours >= 1){
                        durationString = durationString + hours + "h, ";
                    }

                    if(minutes >= 1){
                        durationString = durationString + minutes + "m, ";
                    }

                    if(seconds >= 1){
                        durationString = durationString + seconds + "s";
                    }

                    ItemMeta itemMeta = forgeItem.getItemMeta();
                    itemMeta.setDisplayName(StringUtils.translate("&aSlot #" + i + " (" + itemMeta.getDisplayName() + "&a)"));

                    List<String> itemLore = new ArrayList<>();

                    itemLore.add(" ");
                    itemLore.add(StringUtils.translate("&7You're currently forging this"));
                    itemLore.add(StringUtils.translate("&7item."));
                    itemLore.add(" ");

                    if(finishDate.before(new Date())){
                        itemLore.add(StringUtils.translate("&aClick to claim!"));

                        ItemStack finishedInnerFiller = innerFillerItem.clone();
                        finishedInnerFiller.setDurability((short) 5);

                        inv.setItem(forgeInventorySlot + 9, finishedInnerFiller);
                        inv.setItem(forgeInventorySlot + 18, finishedInnerFiller);
                        inv.setItem(forgeInventorySlot + 27, finishedInnerFiller);
                    } else {

                        itemLore.add(StringUtils.translate("&aTime Left: " + durationString));

                        ItemStack unfinishedInnerFiller = innerFillerItem.clone();
                        unfinishedInnerFiller.setDurability((short) 4);

                        inv.setItem(forgeInventorySlot + 9, unfinishedInnerFiller);
                        inv.setItem(forgeInventorySlot + 18, unfinishedInnerFiller);
                        inv.setItem(forgeInventorySlot + 27, unfinishedInnerFiller);
                    }

                    itemMeta.setLore(itemLore);
                    forgeItem.setItemMeta(itemMeta);


                }catch (ParseException e){
                    e.printStackTrace();
                }

            }

            inv.setItem(forgeInventorySlot, forgeItem);

        }

        for(int i = 0; i < inv.getSize(); i++){
            if(inv.getItem(i) == null){
                inv.setItem(i, fillerItem);
            }
        }

        inv.setItem(49, closeItem);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(player.getOpenInventory().getTopInventory().getTitle().equalsIgnoreCase("The Forge")){
                    new MainForgeGUI().updateGUI(player);
                }else {
                    this.cancel();
                    return;
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);

        return inv;
    }
}
