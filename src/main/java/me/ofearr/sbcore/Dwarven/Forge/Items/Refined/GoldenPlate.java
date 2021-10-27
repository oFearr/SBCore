package me.ofearr.sbcore.Dwarven.Forge.Items.Refined;

import me.ofearr.sbcore.Dwarven.Forge.ForgeItemCategory;
import me.ofearr.sbcore.Dwarven.Forge.ForgeManager;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;

import java.util.HashMap;

public class GoldenPlate implements ForgeItem {

    @Override
    public String itemID() {
        return "golden_plate";
    }

    @Override
    public HashMap<String, Integer> requiredItems() {
        HashMap<String, Integer> requiredForgeItems = new HashMap<>();

        requiredForgeItems.put("enchanted_gold_block", 2);
        requiredForgeItems.put("glacite_jewel", 5);
        requiredForgeItems.put("refined_diamond", 1);
        return requiredForgeItems;
    }

    @Override
    public ForgeItemCategory forgeItemCategory() {
        return ForgeItemCategory.REFINED;
    }

    @Override
    public int requiredHotM() {
        return 3;
    }

    @Override
    public int durationDays() {
        return 6;
    }

    @Override
    public int durationHours() {
        return 0;
    }

    @Override
    public int durationMins() {
        return 0;
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
