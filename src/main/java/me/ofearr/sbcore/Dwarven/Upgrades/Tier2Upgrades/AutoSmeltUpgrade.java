package me.ofearr.sbcore.Dwarven.Upgrades.Tier2Upgrades;

import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.Dwarven.PowderType;
import me.ofearr.sbcore.Dwarven.Upgrades.DwarvenUpgrade;
import me.ofearr.sbcore.Utils.NBTEditor;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AutoSmeltUpgrade implements DwarvenUpgrade {
    @Override
    public String name() {
        return "Auto Smelt";
    }

    @Override
    public String upgradeID() {
        return "auto_smelt";
    }

    @Override
    public int maxLevel() {
        return 1;
    }

    @Override
    public int requiredHOTMLevel() {
        return 2;
    }

    @Override
    public String description() {
        return "&6Pickaxe Ability: Auto Smelt\n&7When you mine an ore which can\n&7be smelted, this ore is automatically\n&7converted into it's smelted form!\n" +
                "\n&8Pickaxe Abilities apply to all\n&8of your pickaxes.";
    }

    @Override
    public double buffPerLevel() {
        return 0;
    }

    @Override
    public boolean isPickaxeAbility() {
        return true;
    }

    @Override
    public String primaryRequirement() {
        return "quick_forge";
    }

    @Override
    public String secondaryRequirement() {
        return "";
    }

    @Override
    public String thirdRequirement() {
        return "";
    }

    @Override
    public int baseCost() {
        return 2500;
    }

    @Override
    public PowderType powderType() {
        return PowderType.MITHRIL;
    }

    @Override
    public ItemStack upgradeIcon() {
        return new ItemStack(Material.EMERALD_BLOCK);
    }

    @Override
    public ItemStack getDisplayItem(Player player) {
        ItemStack displayItem = upgradeIcon();

        ItemMeta displayMeta = displayItem.getItemMeta();
        List<String> displayLore = new ArrayList<>();

        int upgradeLevel = DwarvenManager.getUpgradeLevel(player, this) + 1;
        int playerUpgradeLevel = upgradeLevel - 1;

        if(maxLevel() > 1){
            if(upgradeLevel < maxLevel()){
                displayLore.add(StringUtils.translate("&7Level " + upgradeLevel));
            } else {
                displayLore.add(StringUtils.translate("&7Level " + maxLevel()));
            }
        }

        displayLore.add(" ");


        String[] descriptionWords = description().split("\n");

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.applyPattern("#.##");
        for(String text : descriptionWords){
            if(text != ""){
                text = text.replace("{buff}", decimalFormat.format(buffPerLevel() * upgradeLevel) + "");
                displayLore.add(StringUtils.translate(text));
            }
        }



        if(playerUpgradeLevel >= maxLevel()){
            displayMeta.setDisplayName(StringUtils.translate("&a" + name()));

            displayLore.add(" ");
            displayLore.add(StringUtils.translate("&aUNLOCKED"));

            if(!isPickaxeAbility()){
                displayItem.setType(Material.DIAMOND);
            } else {
                displayItem.setType(Material.DIAMOND_BLOCK);
            }

        } else {
            displayMeta.setDisplayName(StringUtils.translate("&c" + name()));

            displayLore.add(" ");
            displayLore.add(StringUtils.translate("&cLOCKED"));

            if(DwarvenManager.getHOTMLevel(player) >= requiredHOTMLevel()){
                displayLore.add(" ");
                if(upgradeLevel == 1){
                    displayLore.add(StringUtils.translate("&aCost: &51 Token of the Mountain"));
                }

                displayLore.add(StringUtils.translate("&aCost: " + (baseCost() * upgradeLevel) + " (Mithril Powder)"));
            }

        }

        if(DwarvenManager.getHOTMLevel(player) < requiredHOTMLevel()){
            displayLore.add(" ");
            displayLore.add(StringUtils.translate("&5??? &cRequires HoTM Lvl " + requiredHOTMLevel()));

        } else if(playerUpgradeLevel >= 1){

            DwarvenUtils dwarvenUtils = new DwarvenUtils();
            displayLore.add(" ");

            if(dwarvenUtils.upgradeEnabled(player, this)){
                displayLore.add(StringUtils.translate("&aENABLED"));
                displayLore.add(StringUtils.translate("&eRight-click to &cDisable&e!"));
            } else {
                displayLore.add(StringUtils.translate("&cDISABLED"));
                displayLore.add(StringUtils.translate("&eRight-click to &aEnable&e!"));
            }

        }
        if(playerUpgradeLevel <= 0){
            if(isPickaxeAbility()){
                displayItem.setType(Material.COAL_BLOCK);
            } else {
                displayItem.setType(Material.COAL);
            }
        }

        displayMeta.setLore(displayLore);
        displayItem.setItemMeta(displayMeta);


        displayItem = NBTEditor.set(displayItem, upgradeID(), "hotm_menu_upgrade");
        return displayItem;
    }

    @Override
    public void setRegistered() {
        DwarvenManager.putUpgradeToMap(upgradeID(), this);
    }
}
