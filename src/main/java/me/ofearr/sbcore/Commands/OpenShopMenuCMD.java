package me.ofearr.sbcore.Commands;

import me.ofearr.sbcore.CustomConfigs.ShopConfig;
import me.ofearr.sbcore.Dwarven.Forge.GUI.MainForgeGUI;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Shop.ShopGUI;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenShopMenuCMD implements CommandExecutor {

    private static SBCore plugin;
    public OpenShopMenuCMD(SBCore sbCore){
        this.plugin = sbCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("openshopmenu")){
            if(sender.hasPermission("sbcore.shopmenu")){
                if(args.length >= 1){
                    String shopID = args[0].toLowerCase();

                    ShopConfig shopConfig = new ShopConfig();
                    shopConfig.load();

                    if(shopConfig.getConfig().get("shops." + shopID + ".title") == null){
                        sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cYou must specify a valid shop ID!"));
                    } else {
                        if(args.length >= 2){
                            String playerName = args[1];

                            if(Bukkit.getPlayer(playerName) != null){

                                Player player = Bukkit.getPlayer(playerName);

                                player.openInventory(new ShopGUI().GUI(shopID));
                            } else {

                                sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cThat player does not exist!"));
                            }

                        } else {

                            if(sender instanceof Player){
                                Player player = (Player) sender;
                                player.openInventory(new ShopGUI().GUI(shopID));
                            }
                        }
                    }

                } else {
                    sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cYou must specify a valid shop ID!"));
                }

            }else {
                sender.sendMessage(StringUtils.translate("&cInsufficient permissions!"));
            }
        }

        return false;
    }

}
