package me.ofearr.sbcore.Commands;

import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SBReloadCMD implements CommandExecutor {

    private SBCore plugin;

    public SBReloadCMD(SBCore instance){
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("sbreload")){
            if(sender instanceof Player){
                Player player = (Player) sender;

                if(player.hasPermission("sbcore.reload")){

                    player.sendMessage(StringUtils.translate("&aReloading config...."));
                    plugin.reloadConfig();

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
