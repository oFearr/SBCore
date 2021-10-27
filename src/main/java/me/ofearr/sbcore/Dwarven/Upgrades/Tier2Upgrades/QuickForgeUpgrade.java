package me.ofearr.sbcore.Dwarven.Upgrades.Tier2Upgrades;

import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class QuickForgeUpgrade implements DwarvenUpgrade {
    @Override
    public String name() {
        return "Quick Forge";
    }

    @Override
    public String upgradeID() {
        return "quick_forge";
    }

    @Override
    public int maxLevel() {
        return 20;
    }

    @Override
    public int requiredHOTMLevel() {
        return 2;
    }

    @Override
    public String description() {
        return "&7Decrease the time it takes to\n&7forge by &a{buff}%&7.";
    }

    @Override
    public double buffPerLevel() {
        return 1.5;
    }

    @Override
    public boolean isPickaxeAbility() {
        return false;
    }

    @Override
    public String primaryRequirement() {
        return "mining_fortune";
    }

    @Override
    public String thirdRequirement() {
        return "";
    }

    @Override
    public String secondaryRequirement() {
        return "";
    }

    @Override
    public int baseCost() {
        return 150;
    }

    @Override
    public PowderType powderType() {
        return PowderType.MITHRIL;
    }

    @Override
    public ItemStack upgradeIcon() {
        return new ItemStack(Material.EMERALD);
    }

    public Date calculateFinishDate(Player player, ForgeItem forgeItem){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        double hourScale = 0.041;
        double minuteScale = 0.016;
        //seconds * secondScale = 1 min
        double secondScale = 0.016;

        int days = forgeItem.durationDays();
        int hours = forgeItem.durationHours();
        int minutes = forgeItem.durationMins();
        int seconds = forgeItem.durationSeconds();

        DwarvenUtils dwarvenUtils = new DwarvenUtils();

        double playerBuff = (dwarvenUtils.getPlayerDwarvenUpgradeLevel(player, this) * buffPerLevel()) / 100;

        double remainingDays = days - (days * playerBuff);
        double remainingHours = hours - (hours * playerBuff);
        double remainingMinutes = minutes - (minutes * playerBuff);
        double remainingSeconds = seconds - (seconds * playerBuff);

        if(remainingDays < 1 && remainingDays > 0){
            remainingHours = remainingHours + (remainingDays / hourScale);
        }

        if(remainingHours < 1 && remainingHours > 0){
            remainingMinutes = remainingMinutes + (remainingHours / minuteScale);
        }

        if(remainingMinutes < 1 && remainingMinutes > 0){
            remainingSeconds = remainingSeconds + (remainingMinutes / secondScale);
        }

        if(remainingDays - (int) remainingDays > 0){
            remainingHours = remainingHours + ((remainingDays - (int) remainingDays) / hourScale);
        }

        if(remainingHours - (int) remainingHours > 0){
            remainingMinutes = remainingMinutes + ((remainingHours - (int) remainingHours) / minuteScale);
        }

        if(remainingMinutes - (int) remainingMinutes > 0){
            remainingSeconds = remainingSeconds + (remainingMinutes - (int) remainingMinutes / secondScale);
        }

        calendar.add(Calendar.DAY_OF_MONTH, (int) remainingDays);
        calendar.add(Calendar.HOUR, (int) remainingHours);
        calendar.add(Calendar.MINUTE, (int) remainingMinutes);
        calendar.add(Calendar.SECOND, (int) remainingSeconds);

        return calendar.getTime();
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
            displayLore.add(StringUtils.translate("&5᠅ &cRequires HoTM Lvl " + requiredHOTMLevel()));

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

        } if(playerUpgradeLevel <= 0){
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
