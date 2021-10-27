package me.ofearr.sbcore.Commands;

import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Tablist.DefaultTablist;
import me.ofearr.sbcore.Tablist.DwarvenTablist;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WarpCMD implements CommandExecutor {

    private SBCore plugin;

    public WarpCMD(SBCore instance){
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("warp")){
            if(sender instanceof Player){
                Player player = (Player) sender;

                if(args.length >= 1){

                    String warpName = args[0].toLowerCase();

                    if(plugin.getConfig().get("warps." + warpName + ".world") == null){
                        player.sendMessage(StringUtils.translate("&cInvalid warp name provided!"));

                    } else if(player.hasPermission("sbcore.warps." + warpName.replace("_", "").replace("-", ""))){

                        String worldName = plugin.getConfig().getString("warps." + warpName + ".world");
                        World world = Bukkit.getWorld(worldName);

                        int x = plugin.getConfig().getInt("warps." + warpName + ".x");
                        int y = plugin.getConfig().getInt("warps." + warpName + ".y");
                        int z = plugin.getConfig().getInt("warps." + warpName + ".z");

                        double pitch = plugin.getConfig().getDouble("warps." + warpName + ".pitch");
                        double yaw = plugin.getConfig().getDouble("warps." + warpName + ".yaw");

                        Location spawnLoc = new Location(world, x, y, z);

                        spawnLoc.setPitch((float) pitch);
                        spawnLoc.setYaw((float) yaw);

                        player.teleport(spawnLoc);

                        player.sendMessage(StringUtils.translate("&aSuccessfully warped to " + warpName + "!"));

                        if(warpName.equalsIgnoreCase("hub")){
                            new DefaultTablist().setCurrentTablist(player);
                        } else if(warpName.equalsIgnoreCase("dwarven")){
                            new DwarvenTablist().setCurrentTablist(player);
                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                plugin.areaManager.updatePlayerLocationInfo(player);
                            }
                        }.runTaskLater(plugin, 1L);

                    } else {
                        player.sendMessage(StringUtils.translate(plugin.pluginPrefix + "&cYou don't have permission to run this command!"));
                    }

                } else {
                    player.sendMessage(StringUtils.translate("&cInvalid warp name provided!"));
                }

            } else {
                sender.sendMessage(StringUtils.translate(plugin.pluginPrefix + " &cYou must be a player to execute this command!"));
            }
        }

        return false;
    }
}
