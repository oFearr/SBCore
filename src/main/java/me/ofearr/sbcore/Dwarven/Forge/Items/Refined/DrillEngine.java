package me.ofearr.sbcore.Dwarven.Forge.Items.Refined;

import me.ofearr.sbcore.Dwarven.Forge.ForgeItemCategory;
import me.ofearr.sbcore.Dwarven.Forge.ForgeManager;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;

import java.util.HashMap;

public class DrillEngine implements ForgeItem {

    @Override
    public String itemID() {
        return "drill_engine";
    }

    @Override
    public HashMap<String, Integer> requiredItems() {
        HashMap<String, Integer> requiredForgeItems = new HashMap<>();

        requiredForgeItems.put("enchanted_iron_block", 1);
        requiredForgeItems.put("enchanted_redstone_block", 3);
        requiredForgeItems.put("golden_plate", 1);
        requiredForgeItems.put("treasurite", 10);
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
        return 1;
    }

    @Override
    public int durationHours() {
        return 6;
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
