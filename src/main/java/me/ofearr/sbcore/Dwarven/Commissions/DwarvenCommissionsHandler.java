package me.ofearr.sbcore.Dwarven.Commissions;

import me.ofearr.sbcore.Areas.AreaManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class DwarvenCommissionsHandler {

    private DwarvenUtils dwarvenUtils = new DwarvenUtils();

    public void handleMithrilMined(Player player, Location location){
        List<String> commissionIDs = dwarvenUtils.getAllPlayerCommissionIDs(player);
        String locationName = AreaManager.getLocationName(location);

        int slot = 1;
        for(String id : commissionIDs){
            int progress = dwarvenUtils.getPlayerCommissionProgress(player, slot);
            progress++;

            if(id.equalsIgnoreCase("mithril_miner")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);

            } else if(id.equalsIgnoreCase("cliffside_veins_mithril") && locationName.contains("Cliffside Veins")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);

            } else if(id.equalsIgnoreCase("far_reserve_mithril") && locationName.contains("Far Reserve")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);

            } else if(id.equalsIgnoreCase("lava_springs_mithril") && locationName.contains("Lava Springs")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);

            } else if(id.equalsIgnoreCase("ramparts_quarry_mithril") && locationName.contains("Rampart's Quarry")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);

            } else if(id.equalsIgnoreCase("royal_mines_mithril") && locationName.contains("Royal Mines")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);

            } else if(id.equalsIgnoreCase("upper_mines_mithril") && locationName.contains("Upper Mines")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);
            }

            slot++;
        }

    }

    public void handleTitaniumMined(Player player, Location location){
        List<String> commissionIDs = dwarvenUtils.getAllPlayerCommissionIDs(player);
        String locationName = AreaManager.getLocationName(location);

        int slot = 1;
        for(String id : commissionIDs){
            int progress = dwarvenUtils.getPlayerCommissionProgress(player, slot);
            progress++;

            if(id.equalsIgnoreCase("titanium_miner")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);


            } else if(id.equalsIgnoreCase("cliffside_veins_titanium") && locationName.contains("Cliffside Veins")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);


            } else if(id.equalsIgnoreCase("far_reserve_titanium") && locationName.contains("Far Reserve")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);


            } else if(id.equalsIgnoreCase("lava_springs_titanium") && locationName.contains("Lava Springs")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);


            } else if(id.equalsIgnoreCase("ramparts_quarry_titanium") && locationName.contains("Rampart's Quarry")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);


            } else if(id.equalsIgnoreCase("royal_mines_titanium") && locationName.contains("Royal Mines")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);


            } else if(id.equalsIgnoreCase("upper_mines_titanium") && locationName.contains("Upper Mines")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);

            }

            slot++;
        }
    }

    public void handleMithrilPowderCollected(Player player, int powder){
        List<String> commissionIDs = dwarvenUtils.getAllPlayerCommissionIDs(player);

        int slot = 1;
        for(String id : commissionIDs){
            int progress = dwarvenUtils.getPlayerCommissionProgress(player, slot);

            progress = progress + powder;

            if(id.equalsIgnoreCase("powder_collector")){
                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);
                break;
            }

            slot++;
        }
    }

    public void handleMobKilled(Player player, String mobID){
        List<String> commissionIDs = dwarvenUtils.getAllPlayerCommissionIDs(player);

        int slot = 1;
        for(String id : commissionIDs){
            int progress = dwarvenUtils.getPlayerCommissionProgress(player, slot);

            progress ++;

            if(id.equalsIgnoreCase("ice_walker_slayer") && mobID.equalsIgnoreCase("ice_walker")){

                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);
                break;

            } else if(id.equalsIgnoreCase("goblin_slayer") && mobID.equalsIgnoreCase("goblin")){

                dwarvenUtils.setPlayerCommissionProgress(player, slot, progress);
                break;
            }

            slot++;
        }
    }

}
