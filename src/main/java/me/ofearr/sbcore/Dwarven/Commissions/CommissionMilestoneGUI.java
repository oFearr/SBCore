package me.ofearr.sbcore.Dwarven.Commissions;

import me.ofearr.sbcore.Dwarven.DwarvenUtils;
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

public class CommissionMilestoneGUI {

    public int maxLevel = 6;

    public int getCurrentMileStone(int count){
        int mileStone1 = 5;
        int mileStone2 = 25;
        int mileStone3 = 100;
        int mileStone4 = 250;
        int mileStone5 = 500;
        int mileStone6 = 1000;

        if(count < mileStone1) return 0;
        if(count >= mileStone1 && count < mileStone2) return 1;
        if(count >= mileStone2 && count < mileStone3) return 2;
        if(count >= mileStone3 && count < mileStone4) return 3;
        if(count >= mileStone4 && count < mileStone5) return 4;
        if(count >= mileStone5 && count < mileStone6) return 5;

        return mileStone6;
    }

    public int getRequiredCount(int level){
        switch (level){
            case 1: return 5;
            case 2: return 25;
            case 3: return 100;
            case 4: return 250;
            case 5: return 500;
            case 6: return 1000;
            default: return 0;
        }
    }

    public String getMilestoneMessage(int level){
        String message;
        switch (level){
            case 1:
                message = StringUtils.translate("&d=====================================" +
                        "\n &9&lCOMMISSION MILESTONE &f&lTIER " + level + "" +
                        "\n " +
                        "\n &a&lRewards" +
                        "\n  &8+ &6Emissary Carlton &7- &bRampart's Quarry" +
                        "\n  &8+ &6Emissary Ceanna &7- &bCliffside Veins" +
                        "\n  &8+ &3100,000 &7Mining Exp" +
                        "\n &d=====================================");
                return message;
            case 2:
                message = StringUtils.translate("&d=====================================" +
                        "\n &9&lCOMMISSION MILESTONE &f&lTIER " + level + "" +
                        "\n " +
                        "\n &a&lRewards" +
                        "\n  8+ &6Emissary Wilson &7- &bRoyal Mines" +
                        "\n &8+ &6Emissary Ella &7- &bLava Springs" +
                        "\n  &8+ &3200,000 &7Mining Exp" +
                        "\n &d=====================================");
                return message;
            case 3:
                message = StringUtils.translate("&d=====================================" +
                        "\n &9&lCOMMISSION MILESTONE &f&lTIER " + level + "" +
                        "\n " +
                        "\n &a&lRewards" +
                        "\n  &8+ &6Emissary Fraiser &7- &bUpper Mines" +
                        "\n  &8+ &6Emissary Eliza &7- &bFar Reserve" +
                        "\n  &8+ &3400,000 &7Mining Exp" +
                        "\n &d=====================================");
                return message;
            case 4:
                message = StringUtils.translate("&d=====================================" +
                        "\n &9&lCOMMISSION MILESTONE &f&lTIER " + level + "" +
                        "\n " +
                        "\n &a&lRewards" +
                        "\n  &8+ &6Access to /warp Dwarven" +
                        "\n  &8+ &3800,000 &7Mining Exp" +
                        "\n &d=====================================");
                return message;
            case 5:
                message = StringUtils.translate("&d=====================================" +
                        "\n &9&lCOMMISSION MILESTONE &f&lTIER " + level + "" +
                        "\n " +
                        "\n &a&lRewards" +
                        "\n  &8+ &6Royal Pigeon" +
                        "\n  &8+ &31,600,000 &7Mining Exp" +
                        "\n &d=====================================");
                return message;
            case 6:
                message = StringUtils.translate("&5=====================================" +
                        "\n &5&lHEART OF THE MOUNTAIN &f&lTIER " + level + "" +
                        "\n " +
                        "\n &a&lRewards" +
                        "\n  &8+ &6Access to /warp CrystalHollows" +
                        "\n  &8+ &32,400,000 &7Mining Exp" +
                        "\n&5=====================================");
                return message;
        }

        return "";
    }

    public Inventory GUI(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, "Commission Milestones");

        DwarvenUtils dwarvenUtils = new DwarvenUtils();

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack mileStoneTemplateItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemStack backItem = new ItemStack(Material.ARROW);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));
        closeItem.setItemMeta(closeMeta);

        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(StringUtils.translate("&aGo Back"));
        List<String> backLore = new ArrayList<>();
        backLore.add(StringUtils.translate("&7To Dwarven Commissions"));

        backMeta.setLore(backLore);
        backItem.setItemMeta(backMeta);

        int playerCompleteCommissions = dwarvenUtils.getPlayerCommissionTotal(player);

        int currentMilestone = getCurrentMileStone(playerCompleteCommissions);

        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, fillerItem);
        }

        inv.setItem(48, backItem);
        inv.setItem(49, closeItem);
        inv.setItem(20, new ItemStack(Material.AIR));
        inv.setItem(21, new ItemStack(Material.AIR));
        inv.setItem(22, new ItemStack(Material.AIR));
        inv.setItem(23, new ItemStack(Material.AIR));
        inv.setItem(24, new ItemStack(Material.AIR));
        inv.setItem(29, new ItemStack(Material.AIR));

        for(int i = 1; i < 7; i++){
            ItemStack mileStoneItem = mileStoneTemplateItem.clone();
            ItemMeta itemMeta = mileStoneItem.getItemMeta();

            List<String> itemLore = new ArrayList<>();

            itemLore.add(" ");

            double percentFinishedMilestone = (double) playerCompleteCommissions / (double) getRequiredCount(i) * 100;

            DecimalFormat percentageFormat = new DecimalFormat();
            percentageFormat.applyPattern("#.#");

            if(percentFinishedMilestone > 100){
                percentFinishedMilestone = 100;
            }

            if(i <= currentMilestone){
                itemMeta.setDisplayName(StringUtils.translate("&aMilestone " + i + " Rewards"));
                mileStoneItem.setDurability((short) 5);

                itemLore.add(StringUtils.translate("&7Progress: &a" + percentageFormat.format(percentFinishedMilestone) + "%"));
                itemLore.add(StringUtils.translate("&7Total Commissions Complete: &e" + playerCompleteCommissions + "&6/&e" + getRequiredCount(i)));

            } else {
                itemLore.add(StringUtils.translate("&7Progress: &e" + percentageFormat.format(percentFinishedMilestone) + "%"));
                itemLore.add(StringUtils.translate("&7Total Commissions Complete: &e" + playerCompleteCommissions + "&6/&e" + getRequiredCount(i)));

               if(i == currentMilestone + 1){
                    itemMeta.setDisplayName(StringUtils.translate("&eMilestone " + i + " Rewards"));


                    mileStoneItem.setDurability((short) 4);
                } else {
                    itemMeta.setDisplayName(StringUtils.translate("&cMilestone " + i + " Rewards"));
                }
            }

            itemLore.add(" ");
            itemLore.add(StringUtils.translate("&7Rewards: "));
            if(i == 1){
                itemLore.add(StringUtils.translate(" &8+ &6Emissary Carlton &7-"));
                itemLore.add(StringUtils.translate(" &bRampart's Quarry"));
                itemLore.add(StringUtils.translate(" &8+ &6Emissary Ceanna &7-"));
                itemLore.add(StringUtils.translate(" &bCliffside Veins"));
                itemLore.add(StringUtils.translate(" &8+ &3100,000 &7Mining Exp"));

            } else if(i == 2){
                itemLore.add(StringUtils.translate(" &8+ &6Emissary Wilson &7-"));
                itemLore.add(StringUtils.translate(" &bRoyal Mines"));
                itemLore.add(StringUtils.translate(" &8+ &6Emissary Ella &7-"));
                itemLore.add(StringUtils.translate(" &bLava Springs"));
                itemLore.add(StringUtils.translate(" &8+ &3200,000 &7Mining Exp"));

            } else if(i == 3){
                itemLore.add(StringUtils.translate(" &8+ &6Emissary Fraiser &7-"));
                itemLore.add(StringUtils.translate(" &bUpper Mines"));
                itemLore.add(StringUtils.translate(" &8+ &6Emissary Eliza &7-"));
                itemLore.add(StringUtils.translate(" &bFar Reserve"));
                itemLore.add(StringUtils.translate(" &8+ &a1 Commission Slot"));
                itemLore.add(StringUtils.translate(" &8+ &3400,000 &7Mining Exp"));

            } else if(i == 4){
                itemLore.add(StringUtils.translate(" &8+ &6Access to /warp Dwarven"));
                itemLore.add(StringUtils.translate(" &8+ &3800,000 &7Mining Exp"));

            } else if(i == 5){
                itemLore.add(StringUtils.translate(" &8+ &6Royal Pigeon"));
                itemLore.add(StringUtils.translate(" &8+ &31,600,000 &7Mining Exp"));

            } else if(i == 6){
                itemLore.add(StringUtils.translate(" &8+ &6Access to /warp CrystalHollows"));
                itemLore.add(StringUtils.translate(" &8+ &32,400,000 &7Mining Exp"));
            }

            itemLore.add(" ");

            if(dwarvenUtils.hasClaimedMilestone(player, i)){
                itemLore.add(StringUtils.translate("&aClaimed"));

            } else if(i <= currentMilestone){
                itemLore.add(StringUtils.translate("&eClick to claim!"));

            } else {
                itemLore.add(StringUtils.translate("&cLocked"));
            }

            itemMeta.setLore(itemLore);
            mileStoneItem.setItemMeta(itemMeta);

            inv.setItem(inv.firstEmpty(), mileStoneItem);

        }

        return inv;
    }
}
