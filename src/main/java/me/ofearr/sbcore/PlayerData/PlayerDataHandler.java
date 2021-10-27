package me.ofearr.sbcore.PlayerData;

import me.ofearr.sbcore.Collections.Collection;
import me.ofearr.sbcore.Collections.CollectionsManager;
import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.Upgrades.DwarvenUpgrade;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerDataHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinCreateData(PlayerJoinEvent e){
        Player player = e.getPlayer();

        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.create(player);

        if(dataManager.getConfig().get("misc.logout-world") == null){
            dataManager.getConfig().set("misc.logout-world", "world");
        }

        for(List<Collection> collectionList : CollectionsManager.getAllRegisteredCollections().values()){

            for(Collection collection : collectionList){
                if(dataManager.getConfig().get("collections." + collection.collectionName() + ".value") == null){
                    dataManager.getConfig().set("collections." + collection.collectionName() + ".value", 0);
                }
            }
        }

        if(dataManager.getConfig().get("dwarven-data.level") == null){
            dataManager.getConfig().set("dwarven-data.level", 1);
            dataManager.getConfig().set("dwarven-data.xp", 0);
            dataManager.getConfig().set("dwarven-data.hotm-tokens", 0);
            dataManager.getConfig().set("dwarven-data.mithril-powder", 0);
            dataManager.getConfig().set("dwarven-data.gemstone-powder", 0);

            dataManager.getConfig().set("dwarven-data.commissions.slot-count", 2);
            dataManager.getConfig().set("dwarven-data.commissions.total-complete", 0);
            dataManager.getConfig().set("dwarven-data.commissions.current-milestone", 0);
            dataManager.getConfig().set("dwarven-data.commissions.milestone-1", false);
            dataManager.getConfig().set("dwarven-data.commissions.milestone-2", false);
            dataManager.getConfig().set("dwarven-data.commissions.milestone-3", false);
            dataManager.getConfig().set("dwarven-data.commissions.milestone-4", false);
            dataManager.getConfig().set("dwarven-data.commissions.milestone-5", false);
            dataManager.getConfig().set("dwarven-data.commissions.milestone-6", false);
            dataManager.getConfig().set("dwarven-data.commissions.slot-1.id", "");
            dataManager.getConfig().set("dwarven-data.commissions.slot-1.progress", 0);
            dataManager.getConfig().set("dwarven-data.commissions.slot-2.id", "");
            dataManager.getConfig().set("dwarven-data.commissions.slot-2.progress", 0);
            dataManager.getConfig().set("dwarven-data.commissions.slot-3.id", "");
            dataManager.getConfig().set("dwarven-data.commissions.slot-3.progress", 0);
            dataManager.getConfig().set("dwarven-data.commissions.slot-4.id", "");
            dataManager.getConfig().set("dwarven-data.commissions.slot-4.progress", 0);

            dataManager.getConfig().set("dwarven-data.forge.slot-count", 2);
            dataManager.getConfig().set("dwarven-data.forge.slot-1.item", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-1.finish", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-2.item", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-2.finish", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-3.item", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-3.finish", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-4.item", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-4.finish", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-5.item", "");
            dataManager.getConfig().set("dwarven-data.forge.slot-5.finish", "");

        }

        for(DwarvenUpgrade upgrade : DwarvenManager.getRegisteredUpgrades().values()){
            if(dataManager.getConfig().get("dwarven-data.upgrades." + upgrade.upgradeID() + ".level") == null){
                dataManager.getConfig().set("dwarven-data.upgrades." + upgrade.upgradeID() + ".level", 0);
                dataManager.getConfig().set("dwarven-data.upgrades." + upgrade.upgradeID() + ".enabled", true);
            }
        }

        dataManager.saveConfig();

    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLeaveSaveData(PlayerQuitEvent e){
        Player player = e.getPlayer();

        World world = player.getWorld();
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.create(player);

        dataManager.getConfig().set("misc.logout-world", world.getName());
        dataManager.saveConfig();

    }

}
