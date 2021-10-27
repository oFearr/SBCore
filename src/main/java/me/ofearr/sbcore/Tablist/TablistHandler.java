package me.ofearr.sbcore.Tablist;

import com.keenant.tabbed.item.TextTabItem;
import com.keenant.tabbed.tablist.TableTabList;
import me.ofearr.sbcore.SBCore;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class TablistHandler implements Listener {

    private static HashMap<UUID, TableTabList> playerTabLists = new HashMap<>();
    private static SBCore plugin = SBCore.plugin;

    public static void updatePlayerLocationOnTab(Player player, String locationString){
        if(player == null || !playerTabLists.containsKey(player.getUniqueId())) return;

        TableTabList tab = playerTabLists.get(player.getUniqueId());

        tab.set(1, 1, new TextTabItem(locationString, 1));
    }

    public static void setPlayerTabList(Player player, TableTabList tabList){
        playerTabLists.put(player.getUniqueId(), tabList);
    }

    public static TableTabList getPlayerTablist(Player player){

        if(playerTabLists.containsKey(player.getUniqueId())){
            return playerTabLists.get(player.getUniqueId());
        }

        return null;
    }

    @EventHandler
    public void onPlayerJoinAssignTab(PlayerJoinEvent e){
        Player player = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                World world = player.getWorld();

                if(world.getName().equalsIgnoreCase("world")){
                    new DefaultTablist().setCurrentTablist(player);
                } else if(world.getName().equalsIgnoreCase("world_dwarven_mines")){
                    new DwarvenTablist().setCurrentTablist(player);
                }

            }
        }.runTaskLater(plugin, 1L);


    }

}
