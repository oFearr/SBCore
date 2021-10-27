package me.ofearr.sbcore.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GUIPagingUtil {

    public static List<ItemStack> getPageItems(List<ItemStack> items, int page, int spaces){
        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        List<ItemStack> pageItems = new ArrayList<>();
        ItemStack filler = new ItemStack(Material.AIR);
        for(int i = lowerBound; i < upperBound; i++){
            try{
                pageItems.add(items.get(i));
            } catch (IndexOutOfBoundsException e){
                pageItems.add(filler);
            }

        }

        return pageItems;
    }

    public static boolean isPageValid(List<ItemStack> items, int page, int spaces){
        if(page <= 0){
            return false;
        }
        int upperBound = page * spaces;
        int lowerBound = upperBound - spaces;

        return items.size() > lowerBound;
    }
}
