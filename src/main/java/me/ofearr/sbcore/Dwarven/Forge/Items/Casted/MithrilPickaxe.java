package me.ofearr.sbcore.Dwarven.Forge.Items.Casted;

import me.ofearr.sbcore.Dwarven.Forge.ForgeItemCategory;
import me.ofearr.sbcore.Dwarven.Forge.ForgeManager;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;

import java.util.HashMap;

public class MithrilPickaxe implements ForgeItem {
    @Override
    public String itemID() {
        return "mithril_pickaxe";
    }

    @Override
    public HashMap<String, Integer> requiredItems() {
        HashMap<String, Integer> requiredForgeItems = new HashMap<>();

        requiredForgeItems.put("enchanted_mithril", 30);
        requiredForgeItems.put("bejeweled_handle", 1);
        requiredForgeItems.put("enchanted_gold_ingot", 10);

        return requiredForgeItems;
    }

    @Override
    public ForgeItemCategory forgeItemCategory() {
        return ForgeItemCategory.CASTED;
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
        return 45;
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
