package me.ofearr.sbcore.Collections.Mining;

import me.ofearr.sbcore.Collections.CollectionCategories;
import me.ofearr.sbcore.Collections.CollectionsUtil;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.NBTEditor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class MiningCollectionsHandler implements Listener {

    private static SBCore plugin;

    public MiningCollectionsHandler(SBCore sbCore){
        this.plugin = sbCore;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMineBlockEvent(BlockBreakEvent e){
        Player player = e.getPlayer();

        if(e.isCancelled()) return;

        Collection<ItemStack> blockDrops = e.getBlock().getDrops();

        for(ItemStack dropStack : blockDrops){

            if(NBTEditor.contains(dropStack, "item_id")){

                String itemID = NBTEditor.getString(dropStack, "item_id");

                for(me.ofearr.sbcore.Collections.Collection collection : plugin.collectionsManager.getRegisteredCollections(CollectionCategories.MINING)){

                    String requiredIDForCollection = collection.requiredItemID();

                    if(itemID.equalsIgnoreCase(requiredIDForCollection)){
                        int dropAmt = dropStack.getAmount();

                        CollectionsUtil.increaseCollectionCount(player, collection, dropAmt);
                    }
                }
            }


        }



    }
}
