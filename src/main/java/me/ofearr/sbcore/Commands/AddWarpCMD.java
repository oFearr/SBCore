package me.ofearr.sbcore.Commands;

import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddWarpCMD implements CommandExecutor {

    private SBCore plugin;

    public AddWarpCMD(SBCore instance){
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("addwarp")){
            if(sender instanceof Player){
                Player player = (Player) sender;

                if(player.hasPermission("sbcore.addwarp")){
                    if(args.length >= 1){

                        String warpName = args[0].toLowerCase();
                        Location location = player.getLocation();

                        World world = location.getWorld();

                        String worldName = world.getName().toLowerCase();

                        int x = (int) location.getX();
                        int y = (int) location.getY();
                        int z = (int) location.getZ();

                        float yaw = location.getYaw();
                        float pitch = location.getPitch();

                        plugin.getConfig().set("warps." + warpName + ".world", worldName);
                        plugin.getConfig().set("warps." + warpName + ".x", x);
                        plugin.getConfig().set("warps." + warpName + ".y", y);
                        plugin.getConfig().set("warps." + warpName + ".z", z);

                        plugin.getConfig().set("warps." + warpName + ".pitch", pitch);
                        plugin.getConfig().set("warps." + warpName + ".yaw", yaw);

                        plugin.saveConfig();
                        plugin.reloadConfig();

                        player.sendMessage(StringUtils.translate("&aSuccessfully added " + warpName + " as a warp (" + x + "x, " + y + "y, " + z + "z)!"));
                    } else {
                        player.sendMessage(StringUtils.translate(plugin.pluginPrefix + "&cYou must specify a name for this warp!"));
                    }


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
