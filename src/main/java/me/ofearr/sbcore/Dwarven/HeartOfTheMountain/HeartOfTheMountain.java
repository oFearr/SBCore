package me.ofearr.sbcore.Dwarven.HeartOfTheMountain;

import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HeartOfTheMountain {

    private ArrayList<ItemStack> displayItems = new ArrayList<>();

    public String name(){
        return "Heart of the Mountain";
    }

    public int maxLevel(){
        return 7;
    }

    public int getRequiredXPForLevel(int level){
        return 50 * level * ((100 * level) / 3);
    }

    public String getLevelUpMessage(int level){

        String message;
        switch (level){
            case 2:
                message = StringUtils.translate("&5========================================" +
                        "\n &5&lHEART OF THE MOUNTAIN &f&lTIER " + level + "" +
                        "\n" +
                        "\n &a&lRewards" +
                        "\n  &8+&51 Token of the Mountain" +
                        "\n  &8+&6Access to The Forge" +
                        "\n  &8+&6New Forgeable Items" +
                        "\n &5========================================");
                return message;
            case 3:
            case 4:
                message = StringUtils.translate("&5========================================" +
                        "\n &5&lHEART OF THE MOUNTAIN &f&lTIER " + level + "" +
                        "\n " +
                        "\n &a&lRewards" +
                        "\n  &8+&52 Token of the Mountain" +
                        "\n  &8+&a1 Forge Slot" +
                        "\n  &8+&6New Forgeable Items" +
                        "\n&5========================================");
                return message;
            case 5:
            case 6:
            case 7:
                message = StringUtils.translate("&5========================================" +
                        "\n &5&lHEART OF THE MOUNTAIN &f&lTIER " + level + "" +
                        "\n " +
                        "\n &a&lRewards" +
                        "\n  &8+&52 Token of the Mountain" +
                        "\n  &8+&6New Forgeable Items" +
                        "\n&5========================================");
                return message;
        }

        return "";
    }

    public ArrayList<ItemStack> getDisplayItems(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        int hotmLevel = dataManager.getConfig().getInt("dwarven-data.level");
        int hotmXP = dataManager.getConfig().getInt("dwarven-data.xp");

        for(int i = 1; i < maxLevel() +1; i++){
            ItemStack displayItem = new ItemStack(Material.STAINED_GLASS_PANE);

            ItemMeta itemMeta = displayItem.getItemMeta();
            List<String> itemLore = new ArrayList<>();

            double percentFinishedLevel = (double) hotmXP / (double) getRequiredXPForLevel(i) * 100;

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.applyPattern("#,###.#");

            DecimalFormat percentageFormat = new DecimalFormat();
            percentageFormat.applyPattern("#");

            if(percentFinishedLevel > 100){
                percentFinishedLevel = 100;
            }

            if(i <= hotmLevel){
                displayItem.setDurability((short) 5);
                itemMeta.setDisplayName(StringUtils.translate("&aTier " + i));

                itemLore.add(StringUtils.translate("&7You have unlocked this tier. All"));
                itemLore.add(StringUtils.translate("&7perks and abilities on this tier"));
                itemLore.add(StringUtils.translate("&7are available to be unlocked"));
                itemLore.add(StringUtils.translate("&7using a &5Token of the Mountain&7."));
            } else if(i == hotmLevel + 1){
                displayItem.setDurability((short) 4);
                itemMeta.setDisplayName(StringUtils.translate("&eTier " + i));

                itemLore.add(StringUtils.translate("&7Progress through your Heart of"));
                itemLore.add(StringUtils.translate("&7the Mountain by gaining &5HotM"));
                itemLore.add(StringUtils.translate("&5Exp&7, which is earned via"));
                itemLore.add(StringUtils.translate("&7completing &aCommissions."));
                itemLore.add(" ");
                itemLore.add(StringUtils.translate("&7Commissions are tasks given by"));
                itemLore.add(StringUtils.translate("&7the &6&lKing &7in the &bRoyal"));
                itemLore.add(StringUtils.translate("&bPalace&7. Complete them to earn"));
                itemLore.add(StringUtils.translate("&7bountiful rewards!"));

                itemLore.add(" ");
                itemLore.add(StringUtils.translate("&7Progress: &e" + percentageFormat.format(percentFinishedLevel) + "%"));
                itemLore.add(StringUtils.translate("&7Total Exp: &e" + decimalFormat.format(hotmXP) + "&6/&e" + StringUtils.formatNumber(getRequiredXPForLevel(i))));
            } else {
                displayItem.setDurability((short) 14);
                itemMeta.setDisplayName(StringUtils.translate("&cTier " + i));

                itemLore.add(StringUtils.translate("&7Progress through your Heart of"));
                itemLore.add(StringUtils.translate("&7the Mountain by gaining &5HotM"));
                itemLore.add(StringUtils.translate("&5Exp&7, which is earned via"));
                itemLore.add(StringUtils.translate("&7completing &aCommissions."));
                itemLore.add(" ");
                itemLore.add(StringUtils.translate("&7Commissions are tasks given by"));
                itemLore.add(StringUtils.translate("&7the &6&lKing &7in the &bRoyal"));
                itemLore.add(StringUtils.translate("&bPalace&7. Complete them to earn"));
                itemLore.add(StringUtils.translate("&7bountiful rewards!"));

                itemLore.add(" ");
                itemLore.add(StringUtils.translate("&7Progress: &e" + percentageFormat.format(percentFinishedLevel) + "%"));
                itemLore.add(StringUtils.translate("&7Total Exp: &e" + decimalFormat.format(hotmXP) + "&6/&e" + StringUtils.formatNumber(getRequiredXPForLevel(i))));
            }

            itemLore.add(" ");
            itemLore.add(StringUtils.translate("&7Rewards"));

            if(i == 1){
                itemLore.add(StringUtils.translate(" &8+&51 Token of the Mountain"));

            } else if(i == 2){
                itemLore.add(StringUtils.translate(" &8+&51 Token of the Mountain"));
                itemLore.add(StringUtils.translate(" &8+&6Access to The Forge"));
                itemLore.add(StringUtils.translate(" &8+&6New Forgeable Items"));

            } else if(i == 3){
                itemLore.add(StringUtils.translate(" &8+&52 Token of the Mountain"));
                itemLore.add(StringUtils.translate(" &8+&a1 Forge Slot"));
                itemLore.add(StringUtils.translate(" &8+&6New Forgeable Items"));

            } else if(i == 4){
                itemLore.add(StringUtils.translate(" &8+&52 Token of the Mountain"));
                itemLore.add(StringUtils.translate(" &8+&a1 Forge Slot"));
                itemLore.add(StringUtils.translate(" &8+&6New Forgeable Items"));

            } else if(i == 5){
                itemLore.add(StringUtils.translate(" &8+&53 Token of the Mountain"));
                itemLore.add(StringUtils.translate(" &8+&6New Forgeable Items"));

            } else if(i == 6){
                itemLore.add(StringUtils.translate(" &8+&53 Token of the Mountain"));
                itemLore.add(StringUtils.translate(" &8+&6New Forgeable Items"));

            } else if(i == 7){
                itemLore.add(StringUtils.translate(" &8+&53 Token of the Mountain"));
                itemLore.add(StringUtils.translate(" &8+&6New Forgeable Items"));
            }

            itemLore.add(StringUtils.translate(" "));

            if(i <= hotmLevel){
                itemLore.add(StringUtils.translate("&aUNLOCKED"));

            } else{
                itemLore.add(StringUtils.translate("&cLOCKED"));
            }

            itemMeta.setLore(itemLore);
            displayItem.setItemMeta(itemMeta);

            displayItems.add(displayItem);
        }

        return displayItems;
    }


}
