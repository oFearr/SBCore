package me.ofearr.sbcore.Events;

import me.ofearr.playerstatscore.API.StatAPIManager;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ActionBarManager implements Listener {
    private static HashMap<UUID, String> additionalActionBarMessage = new HashMap<>();
    private StatAPIManager statAPIManager = new StatAPIManager();
    private static SBCore plugin;

    public ActionBarManager(SBCore instance){
        this.plugin = instance;
    }

    public static void addAdditionalBarMessage(Player player, String message){
        additionalActionBarMessage.put(player.getUniqueId(), message);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!additionalActionBarMessage.containsKey(player.getUniqueId())){
                    this.cancel();
                    return;
                }

                additionalActionBarMessage.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, 60L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        new BukkitRunnable() {
            @Override
            public void run() {
                if(player != null){
                    HashMap<String, Integer> playerStats = statAPIManager.getAllPlayerStats(player);

                    int currentHP = (int) Math.round(player.getHealth());
                    int maxHP = playerStats.get("Health");
                    int defense = playerStats.get("Defense");
                    int currentIntelligence = statAPIManager.getPlayerManaLevel(player);
                    int maxIntelligence = playerStats.get("Intelligence");

                    if(additionalActionBarMessage.containsKey(player.getUniqueId())){
                        String additional = additionalActionBarMessage.get(player.getUniqueId());

                        String combined = StringUtils.translate("&c" + currentHP + "/" + maxHP + "❤" + "     " + additional + "     " + "&b" + currentIntelligence + "/" + maxIntelligence + "✎ Mana");

                        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(combined), (byte)2);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                    } else {
                        String combined = StringUtils.translate("&c" + currentHP + "/" + maxHP + "❤" + "     " + "&a" + defense + "❈" + " Defense" + "     " + "&b" + currentIntelligence + "/" + maxIntelligence + "✎ Mana");

                        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(combined), (byte)2);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                    }
                } else {
                    this.cancel();
                }

            }
        }.runTaskTimer(plugin, 5L,5L);

    }
}
