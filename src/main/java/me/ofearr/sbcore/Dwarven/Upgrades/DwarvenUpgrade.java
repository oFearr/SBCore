package me.ofearr.sbcore.Dwarven.Upgrades;

import me.ofearr.sbcore.Dwarven.PowderType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface DwarvenUpgrade {
    String name();
    String upgradeID();
    int maxLevel();
    int requiredHOTMLevel();
    String description();
    double buffPerLevel();
    boolean isPickaxeAbility();
    String primaryRequirement();
    //Can be used as a requirement instead of the primary
    String secondaryRequirement();
    //Can be used as a requirement instead of the primary or the secondary
    String thirdRequirement();

    //Cost per level is calculated (baseCost * level)
    int baseCost();
    PowderType powderType();
    ItemStack upgradeIcon();

    ItemStack getDisplayItem(Player player);

    void setRegistered();
}
