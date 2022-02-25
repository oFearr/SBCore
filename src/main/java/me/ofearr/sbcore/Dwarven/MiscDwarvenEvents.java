package me.ofearr.sbcore.Dwarven;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommissionsHandler;
import me.ofearr.sbcore.SBCore;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MiscDwarvenEvents implements Listener {

    private SBCore sbCore;

    public MiscDwarvenEvents(SBCore sbCore){
        this.sbCore = sbCore;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerClickMonolith(PlayerInteractEvent e){
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        DwarvenManager dwarvenManager = sbCore.dwarvenManager;

        if(block == null) return;
        if(!dwarvenManager.getSpawnedMonoliths().containsKey(block.getLocation())) return;
        if(dwarvenManager.getSpawnedMonoliths().get(block.getLocation()) == null) return;

        DwarvenMonolith dwarvenMonolith = dwarvenManager.getSpawnedMonoliths().get(block.getLocation());
        dwarvenMonolith.destroy();
        dwarvenMonolith.grantReward(player);

        DwarvenCommissionsHandler dwarvenCommissionsHandler = new DwarvenCommissionsHandler();
        dwarvenCommissionsHandler.handleMonolithCollected(player, dwarvenMonolith.getType());

        long spawnInterval = sbCore.getConfig().getInt("dwarven-settings.monoliths.spawn-interval");

        new BukkitRunnable() {
            @Override
            public void run() {
                dwarvenMonolith.spawn(dwarvenMonolith.getRandomSpawnLocation());
            }
        }.runTaskLater(sbCore, spawnInterval);

        e.setCancelled(true);
    }
}
