package me.ofearr.sbcore.Dwarven.Forge.Items.Casted;

import me.ofearr.sbcore.Dwarven.Forge.ForgeItemCategory;
import me.ofearr.sbcore.Dwarven.Forge.ForgeManager;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;

import java.util.HashMap;

public class RefinedMithrilPickaxe implements ForgeItem {
    @Override
    public String itemID() {
        return "refined_mithril_pickaxe";
    }

    @Override
    public HashMap<String, Integer> requiredItems() {
        HashMap<String, Integer> requiredForgeItems = new HashMap<>();

        requiredForgeItems.put("refined_mithril", 3);
        requiredForgeItems.put("bejeweled_handle", 2);
        requiredForgeItems.put("refined_diamond", 1);
        requiredForgeItems.put("enchanted_gold_ingot", 30);

        return requiredForgeItems;
    }

    @Override
    public ForgeItemCategory forgeItemCategory() {
        return ForgeItemCategory.CASTED;
    }

    @Override
    public int requiredHotM() {
        return 3;
    }

    @Override
    public int durationDays() {
        return 0;
    }

    @Override
    public int durationHours() {
        return 22;
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
