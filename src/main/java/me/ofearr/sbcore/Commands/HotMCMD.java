package me.ofearr.sbcore.Commands;

import me.ofearr.sbcore.CustomMobs.Mobs.IceWalker;
import me.ofearr.sbcore.Dwarven.HeartOfTheMountain.HotMTreeMenu;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HotMCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("hotm")){
            if(sender.hasPermission("sbcore.hotmmenu")){
                if(args.length >= 1){
                    String playerName = args[0];

                    if(Bukkit.getPlayer(playerName) != null){
                        Player player = Bukkit.getPlayer(playerName);

                        player.openInventory(new HotMTreeMenu().GUI(player, 1));

                        new IceWalker().spawn(player.getLocation());
                    } else {

                        sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cThat player does not exist!"));
                    }

                } else {

                    if(sender instanceof Player){

                        ((Player) sender).openInventory(new HotMTreeMenu().GUI((Player) sender, 1));
                    }
                }
            }else {
                sender.sendMessage(StringUtils.translate("&cInsufficient permissions!"));
            }
        }

        return false;
    }

}
