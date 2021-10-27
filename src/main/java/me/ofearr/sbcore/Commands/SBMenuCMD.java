package me.ofearr.sbcore.Commands;

import me.ofearr.playerstatscore.API.StatAPIManager;
import me.ofearr.sbcore.GUI.SBMenuGUI;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SBMenuCMD implements CommandExecutor {

    private SBCore plugin;
    private SBMenuGUI sbMenuGUI = new SBMenuGUI();
    private StatAPIManager apiManager = new StatAPIManager();

    public SBMenuCMD(SBCore instance){
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("sbmenu")){
            if(sender instanceof Player){
                Player player = (Player) sender;

                if(player.hasPermission("sbcore.sbmenu")){
                    player.openInventory(sbMenuGUI.openGUI(player));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(player.getOpenInventory().getTitle().equalsIgnoreCase("SkyBlock Menu")){
                                InventoryView inv = player.getOpenInventory();
                                int statsItemSlot = 13;

                                ItemStack statsItem = inv.getItem(statsItemSlot);

                                ItemMeta statsMeta = statsItem.getItemMeta();
                                statsMeta.setDisplayName(StringUtils.translate("&aYour SkyBlock Profile"));
                                List<String> statsLore = apiManager.getStatVisuals(player);

                                statsMeta.setLore(statsLore);
                                statsItem.setItemMeta(statsMeta);

                                inv.setItem(statsItemSlot, statsItem);
                                player.updateInventory();

                            } else{
                                this.cancel();
                                return;
                            }
                        }
                    }.runTaskTimer(plugin, 1L, 10L);
                } else {
                    player.sendMessage(StringUtils.translate(plugin.pluginPrefix + "&cYou don't have permission to run this command!"));
                }

            } else {
                sender.sendMessage(StringUtils.translate(plugin.pluginPrefix + " &cYou must be a player to execute this command!"));
            }
        }

        return false;
    }
}
