package me.ofearr.sbcore.Commands;

import me.ofearr.sbcore.Collections.Collection;
import me.ofearr.sbcore.Collections.CollectionsManager;
import me.ofearr.sbcore.Collections.CollectionsUtil;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.List;

public class GiveCollectionProgressCMD implements CommandExecutor {

    private static SBCore plugin;

    public GiveCollectionProgressCMD(SBCore sbCore){
        this.plugin = sbCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("givecollectionprogress")){
            if(sender.hasPermission("sbcore.collections.update")){
                if(args.length >= 3){
                    if(Double.parseDouble(args[2]) % 1 == 0 || Double.parseDouble(args[2]) < 1){

                        int xpToGive;

                        if(Long.parseLong(args[2]) > Integer.MAX_VALUE){
                            xpToGive = Integer.MAX_VALUE;
                        } else {
                            xpToGive = Integer.parseInt(args[2]);
                        }

                        String playerName = args[0];
                        String skillName = args[1].toLowerCase();

                        if(Bukkit.getPlayer(playerName) != null){
                            boolean containsCollection = false;

                            for(List<Collection> collectionCat : CollectionsManager.getAllRegisteredCollections().values()){
                                for(Collection collection : collectionCat){

                                    if(collection.collectionName().equalsIgnoreCase(skillName)){
                                        containsCollection = true;
                                        break;
                                    }
                                }
                            }

                            if(containsCollection){
                                Player player = Bukkit.getPlayer(playerName);

                                Collection collection = CollectionsUtil.getCollectionFromName(skillName);

                                if(collection != null){

                                    CollectionsUtil.increaseCollectionCount(player, collection, xpToGive);
                                }

                                DecimalFormat decimalFormat = new DecimalFormat();
                                decimalFormat.applyPattern("#,###");

                                sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &aSuccessfully added " + decimalFormat.format(xpToGive) + " count to " + player.getName() + "'s "+ skillName + " collectio!"));
                            } else{
                                sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cNo collection with that name exists!"));
                            }

                        } else {
                            sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cThat player does not exist!"));
                        }

                    } else {
                        sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cYou must enter a positive integer value for collection count to increase by!"));
                    }
                } else {
                    sender.sendMessage(StringUtils.translate("&b&lSkyBlock &8>> &cIncorrect arguments! /givecollectionprogress <player> <collection> <count>"));
                }
            }else {
                sender.sendMessage(StringUtils.translate("&cInsufficient permissions!"));
            }
        }

        return false;
    }

}
