package me.ofearr.sbcore.Dwarven.Commissions;

import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DwarvenCommissionsGUI {

    private static SBCore plugin = SBCore.plugin;

    public Inventory GUI(Player player){
        Inventory inv = Bukkit.createInventory(null, 36, "Dwarven Commissions");

        DwarvenManager dwarvenManager = plugin.dwarvenManager;
        DwarvenUtils dwarvenUtils = new DwarvenUtils();

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemStack milestoneItem = new ItemStack(Material.EMPTY_MAP);

        ItemStack commissionTemplateItem = new ItemStack(Material.BOOK_AND_QUILL);

        int commissionSlots = dwarvenUtils.getPlayerCommissionSlotCount(player);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));
        closeItem.setItemMeta(closeMeta);

        ItemMeta milestoneMeta = milestoneItem.getItemMeta();
        milestoneMeta.setDisplayName(StringUtils.translate("&aYour Milestones"));
        List<String> milestoneLore = new ArrayList<>();

        milestoneLore.add(StringUtils.translate("&7Click to view your"));
        milestoneLore.add(StringUtils.translate("&7commission milestones progress"));
        milestoneLore.add(StringUtils.translate("&7and claim your &arewards&7!"));

        milestoneMeta.setLore(milestoneLore);
        milestoneItem.setItemMeta(milestoneMeta);

        List<ItemStack> commissionItems = new ArrayList<>();

        for(int i = 1; i < commissionSlots + 1; i++){
            ItemStack commissionItem = commissionTemplateItem.clone();

            String commissionID = dwarvenUtils.getPlayerCommissionID(player, i);

            if(commissionID.length() < 1 || commissionID == ""){
                commissionID = dwarvenManager.selectNewPlayerCommission(player, i);

            }

            DwarvenCommission commission = dwarvenManager.getDwarvenCommission(commissionID);

            ItemMeta commissionMeta = commissionItem.getItemMeta();
            commissionMeta.setDisplayName(StringUtils.translate("&6Commission #" + i));

            List<String> commissionLore = new ArrayList<>();

            commissionLore.add(StringUtils.translate("&7Commissions are tasks given"));
            commissionLore.add(StringUtils.translate("&7directly by the &6&lKing &7which give"));
            commissionLore.add(StringUtils.translate("&7bountiful rewards."));
            commissionLore.add(" ");
            commissionLore.add(StringUtils.translate("&9" + commission.commissionName()));

            String[] descriptionWords = commission.description().split("\n");

            for(String text : descriptionWords){
                if(text != ""){
                    commissionLore.add(StringUtils.translate(text));
                }
            }

            commissionLore.add(" ");
            commissionLore.add(StringUtils.translate("&9Rewards"));
            if(dwarvenManager.getDailyCommissionsCount(player) < plugin.getConfig().getInt("dwarven-settings.commissions.max-daily-commission-bonus")){
                commissionLore.add(StringUtils.translate("&7- &5950 HotM Exp"));

            } else{
                commissionLore.add(StringUtils.translate("&7- &5100 HotM Exp"));
            }

            int minMithrilPowder = plugin.getConfig().getInt("dwarven-settings.commissions.min-powder-reward");
            int maxMithrilPowder = plugin.getConfig().getInt("dwarven-settings.commissions.max-powder-reward");

            int minXP = plugin.getConfig().getInt("dwarven-settings.commissions.min-xp-reward");
            int maxXP = plugin.getConfig().getInt("dwarven-settings.commissions.max-xp-reward");

            commissionLore.add(StringUtils.translate("&7- &2" + minMithrilPowder + " -> " + maxMithrilPowder + " Mithril Powder"));
            commissionLore.add(StringUtils.translate("&7- &3" + minXP + " -> " + maxXP + " Mining Exp"));

            commissionLore.add(" ");

            int requiredProgress = commission.completionProgress();
            int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, i);

            if(currentProgress >= requiredProgress){
                commissionLore.add(StringUtils.translate("&a&lCLICK TO CLAIM"));
            } else {
                commissionLore.add(StringUtils.translate("&9Progress"));

                double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                DecimalFormat decimalFormat = new DecimalFormat();
                decimalFormat.applyPattern("#,###.#");

                DecimalFormat percentageFormat = new DecimalFormat();
                percentageFormat.applyPattern("#.#");

                if(percentFinishedCommission > 100){
                    percentFinishedCommission = 100;
                }

                commissionLore.add(StringUtils.translate("&e" + decimalFormat.format(currentProgress) + "&6/&e" + StringUtils.formatNumber(requiredProgress) + " &7(&b" + percentageFormat.format(percentFinishedCommission) + "%&7)"));
            }

            commissionMeta.setLore(commissionLore);
            commissionItem.setItemMeta(commissionMeta);

            commissionItems.add(commissionItem);

        }

        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, fillerItem);
        }

        if(commissionSlots <= 1){
            inv.setItem(13, commissionItems.get(0));

        } else if(commissionSlots == 2){
            inv.setItem(11, commissionItems.get(0));
            inv.setItem(15, commissionItems.get(1));

        } else if(commissionSlots == 3){
            inv.setItem(10, commissionItems.get(0));
            inv.setItem(13, commissionItems.get(1));
            inv.setItem(16, commissionItems.get(2));

        } else if(commissionSlots >= 4){
            inv.setItem(10, commissionItems.get(0));
            inv.setItem(12, commissionItems.get(1));
            inv.setItem(14, commissionItems.get(2));
            inv.setItem(16, commissionItems.get(3));
        }

        inv.setItem(30, milestoneItem);
        inv.setItem(31, closeItem);

        return inv;
    }
}
