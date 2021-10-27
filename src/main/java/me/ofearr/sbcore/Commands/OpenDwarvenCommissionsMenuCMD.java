package me.ofearr.sbcore.Commands;

import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommissionsGUI;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenDwarvenCommissionsMenuCMD implements CommandExecutor {
    private static SBCore plugin;

    public OpenDwarvenCommissionsMenuCMD(SBCore sbCore){
        this.plugin = sbCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("opendwarvencommissionsmenu")){
            if(sender.hasPermission("sbcore.dwarven.commissionsmenu")){
                if(args.length >= 1){
                    String playerName = args[0];

                    if(Bukkit.getPlayer(playerName) != null){
                        Player player = Bukkit.getPlayer(playerName);

                        player.openInventory(new DwarvenCommissionsGUI().GUI(player));
                    } else {

                        sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cThat player does not exist!"));
                    }

                } else {

                    if(sender instanceof Player){

                        Player player = (Player) sender;
                        player.openInventory(new DwarvenCommissionsGUI().GUI(player));
                    }
                }
            }else {
                sender.sendMessage(StringUtils.translate("&cInsufficient permissions!"));
            }
        }

        return false;
    }

}
