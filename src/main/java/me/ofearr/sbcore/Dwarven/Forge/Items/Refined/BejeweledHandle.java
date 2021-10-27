package me.ofearr.sbcore.Dwarven.Forge.Items.Refined;

import me.ofearr.sbcore.Dwarven.Forge.ForgeItemCategory;
import me.ofearr.sbcore.Dwarven.Forge.ForgeManager;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;

import java.util.HashMap;

public class BejeweledHandle implements ForgeItem {

    @Override
    public String itemID() {
        return "bejeweled_handle";
    }

    @Override
    public HashMap<String, Integer> requiredItems() {
        HashMap<String, Integer> requiredForgeItems = new HashMap<>();

        requiredForgeItems.put("glacite_jewel", 3);
        return requiredForgeItems;
    }

    @Override
    public ForgeItemCategory forgeItemCategory() {
        return ForgeItemCategory.REFINED;
    }

    @Override
    public int requiredHotM() {
        return 2;
    }

    @Override
    public int durationDays() {
        return 0;
    }

    @Override
    public int durationHours() {
        return 0;
    }

    @Override
    public int durationMins() {
        return 30;
    }

    @Override
    public int durationSeconds() {
        return 0;
    }

    @Override
    public void setRegistered() {
        ForgeManager.registerItem(this);
    }
}
