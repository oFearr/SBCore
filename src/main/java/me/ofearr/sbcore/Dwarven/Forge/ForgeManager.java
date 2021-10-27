package me.ofearr.sbcore.Dwarven.Forge;

import me.ofearr.sbcore.Dwarven.Forge.Items.Casted.MithrilPickaxe;
import me.ofearr.sbcore.Dwarven.Forge.Items.Casted.RefinedMithrilPickaxe;
import me.ofearr.sbcore.Dwarven.Forge.Items.ForgeItem;
import me.ofearr.sbcore.Dwarven.Forge.Items.Refined.*;

import java.util.HashMap;

public class ForgeManager {
    private static HashMap<String, ForgeItem> registeredForgeItems = new HashMap<>();

    public static void setRegisteredForgeItems(){
        new RefinedDiamond().setRegistered();
        new RefinedMithril().setRegistered();
        new RefinedTitanium().setRegistered();
        new MithrilPlate().setRegistered();
        new GoldenPlate().setRegistered();
        new FuelTank().setRegistered();
        new DrillEngine().setRegistered();
        new BejeweledHandle().setRegistered();

        new MithrilPickaxe().setRegistered();
        new RefinedMithrilPickaxe().setRegistered();
    }

    public static HashMap<String, ForgeItem> getRegisteredForgeItems(){

        return registeredForgeItems;
    }

    public static void registerItem(ForgeItem forgeItem){
        registeredForgeItems.put(forgeItem.itemID(), forgeItem);
    }
}
