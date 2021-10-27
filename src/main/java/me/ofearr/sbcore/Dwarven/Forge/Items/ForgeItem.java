package me.ofearr.sbcore.Dwarven.Forge.Items;

import me.ofearr.sbcore.Dwarven.Forge.ForgeItemCategory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public interface ForgeItem {
    String itemID();
    HashMap<String, Integer> requiredItems();
    ForgeItemCategory forgeItemCategory();

    int requiredHotM();

    int durationDays();
    int durationHours();
    int durationMins();
    int durationSeconds();

    void setRegistered();

}
