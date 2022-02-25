package me.ofearr.sbcore.Dwarven;

import me.ofearr.sbcore.Dwarven.Commissions.CommissionMilestoneGUI;
import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.HeartOfTheMountain.HeartOfTheMountain;
import me.ofearr.sbcore.Dwarven.Upgrades.DwarvenUpgrade;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier2Upgrades.TitaniumInsaniumUpgrade;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier7Upgrades.PowderBuffUpgrade;
import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Tablist.DwarvenTablist;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DwarvenUtils {

    private SBCore plugin = SBCore.plugin;

    public int getMithrilPowderForPlayer(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        return dataManager.getConfig().getInt("dwarven-data.mithril-powder");
    }

    public int getPowderForPlayer(Player player, PowderType powderType){

        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        switch (powderType){
            case GEMSTONE: return dataManager.getConfig().getInt("dwarven-data.gemstone-powder");
            default: return dataManager.getConfig().getInt("dwarven-data.mithril-powder");
        }
    }

    public int getGemstonePowderForPlayer(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        return dataManager.getConfig().getInt("dwarven-data.gemstone-powder");
    }

    public int getAvailableHOTMTokensForPlayer(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        return dataManager.getConfig().getInt("dwarven-data.hotm-tokens");
    }

    public void setHOTMTokensForPlayer(Player player, int tokens){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        if(tokens < 0){
            tokens = 0;
        }

        dataManager.getConfig().set("dwarven-data.hotm-tokens", tokens);

        dataManager.saveConfig();
    }

    public int getHOTMXPForPlayer(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        return dataManager.getConfig().getInt("dwarven-data.xp");
    }

    public void setHOTMLevel(Player player, int level){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        dataManager.getConfig().set("dwarven-data.level", level);

        dataManager.saveConfig();

        int currentTokens = getAvailableHOTMTokensForPlayer(player);
        switch (level){
            case 2:
                setHOTMTokensForPlayer(player, currentTokens +1);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.forgemenu true");
                break;
            case 3:
            case 4:
                setHOTMTokensForPlayer(player, currentTokens +2);
                setPlayerForgeSlotCount(player, getPlayerForgeSlotCount(player) +1);
                break;
            case 5:
            case 6:
            case 7:
                setHOTMTokensForPlayer(player, currentTokens +2);
                break;

        }
    }

    public void setHOTMXP(Player player, int xp){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        dataManager.getConfig().set("dwarven-data.xp", xp);

        dataManager.saveConfig();

        HeartOfTheMountain heartOfTheMountain = new HeartOfTheMountain();

        int hotmLevel = DwarvenManager.getHOTMLevel(player);
        int nextHOTMLevel = hotmLevel + 1;
        int requiredXP = heartOfTheMountain.getRequiredXPForLevel(nextHOTMLevel);
        int currentXP = xp;

        int maxLevel = heartOfTheMountain.maxLevel();

        if(hotmLevel < maxLevel){
            if(currentXP >= requiredXP){
                setHOTMLevel(player, nextHOTMLevel);

                player.sendMessage(heartOfTheMountain.getLevelUpMessage(nextHOTMLevel));
            }
        }
    }

    public void setMithrilPowder(Player player, int powder){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        dataManager.getConfig().set("dwarven-data.mithril-powder", powder);

        dataManager.saveConfig();

        new DwarvenTablist().updateTab(player);
    }

    public void setGemstonePowder(Player player, int powder){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);
        dataManager.getConfig().set("dwarven-data.mithril-powder", powder);

        dataManager.saveConfig();

        new DwarvenTablist().updateTab(player);
    }

    public int getPlayerDwarvenUpgradeLevel(Player player, DwarvenUpgrade dwarvenUpgrade){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(dataManager.getConfig().get("dwarven-data.upgrades." + dwarvenUpgrade.upgradeID() + ".level") == null){
            return 0;
        } else {

            return dataManager.getConfig().getInt("dwarven-data.upgrades." + dwarvenUpgrade.upgradeID() + ".level");
        }
    }

    public int getPlayerDwarvenUpgradeLevel(Player player, String upgradeID){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(dataManager.getConfig().get("dwarven-data.upgrades." + upgradeID + ".level") == null){
            return 0;
        } else {

            return dataManager.getConfig().getInt("dwarven-data.upgrades." + upgradeID + ".level");
        }
    }

    public boolean setPlayerDwarvenUpgradeLevel(Player player, DwarvenUpgrade dwarvenUpgrade, int newLevel){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(dataManager.getConfig().get("dwarven-data.upgrades." + dwarvenUpgrade.upgradeID() + ".level") == null){

            return false;
        } else {

            dataManager.getConfig().set("dwarven-data.upgrades." + dwarvenUpgrade.upgradeID() + ".level", newLevel);
            dataManager.saveConfig();
            return true;
        }
    }

    public boolean setPlayerDwarvenUpgradeLevel(Player player, String upgradeID, int newLevel){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(dataManager.getConfig().get("dwarven-data.upgrades." + upgradeID + ".level") == null){
            return false;
        } else {

            dataManager.getConfig().set("dwarven-data.upgrades." + upgradeID + ".level", newLevel);
            dataManager.saveConfig();
            return true;
        }
    }

    public int calculatePowder(Player player){

        double basePowder = 0.5 + Math.random() * (4 - 0.5);
        double totalPowder = basePowder;

        PowderBuffUpgrade powderBuffUpgrade = new PowderBuffUpgrade();

        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        int buffLevel = dataManager.getConfig().getInt("dwarven-data.upgrades." + powderBuffUpgrade.upgradeID() + ".level");
        boolean buffEnabled = dataManager.getConfig().getBoolean("dwarven-data.upgrades." + powderBuffUpgrade.upgradeID() +".enabled");

        double powderBuffModifier = (buffLevel * new PowderBuffUpgrade().buffPerLevel()) / 100;

        if(buffEnabled){
            double increasedBy = basePowder * powderBuffModifier;

            totalPowder = totalPowder + increasedBy;
        }

        return (int) Math.round(totalPowder);
    }

    public boolean upgradeEnabled(Player player, DwarvenUpgrade dwarvenUpgrade){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        boolean buffEnabled = dataManager.getConfig().getBoolean("dwarven-data.upgrades." + dwarvenUpgrade.upgradeID() + ".enabled");

        return buffEnabled;
    }


    public void toggleUpgradeStatus(Player player, DwarvenUpgrade dwarvenUpgrade){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(upgradeEnabled(player, dwarvenUpgrade)){
            dataManager.getConfig().set("dwarven-data.upgrades." + dwarvenUpgrade.upgradeID() + ".enabled", false);
        } else {
            dataManager.getConfig().set("dwarven-data.upgrades." + dwarvenUpgrade.upgradeID() + ".enabled", true);
        }

        dataManager.saveConfig();
    }


    public boolean rolledTitaniumSpawn(Player player){
        double chance = plugin.getConfig().getDouble("dwarven-settings.base-titanium-chance");

        TitaniumInsaniumUpgrade titaniumInsaniumUpgrade = new TitaniumInsaniumUpgrade();

        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        boolean buffEnabled = upgradeEnabled(player, titaniumInsaniumUpgrade);
        if(buffEnabled){
            int buffLevel = dataManager.getConfig().getInt("dwarven-data.upgrades." + titaniumInsaniumUpgrade.upgradeID() + ".level");

            double buffChance = (buffLevel * titaniumInsaniumUpgrade.buffPerLevel()) / 100;

            chance = chance + buffChance;

        }


        if(ThreadLocalRandom.current().nextDouble() <= chance){
            return true;
        }

        return false;
    }

    public int getPlayerForgeSlotCount(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        return dataManager.getConfig().getInt("dwarven-data.forge.slot-count");
    }

    public void setPlayerForgeSlotCount(Player player, int amount){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        dataManager.getConfig().set("dwarven-data.forge.slot-count", amount);

        dataManager.saveConfig();

        new DwarvenTablist().updateTab(player);
    }

    public int getPlayerCommissionSlotCount(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        return dataManager.getConfig().getInt("dwarven-data.commissions.slot-count");
    }

    public void setPlayerCommissionSlotCount(Player player, int amount){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        dataManager.getConfig().set("dwarven-data.commissions.slot-count", amount);

        dataManager.saveConfig();

        new DwarvenTablist().updateTab(player);
    }

    public int getPlayerCommissionTotal(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        return dataManager.getConfig().getInt("dwarven-data.commissions.total-complete");
    }

    public int getPlayerCommissionMilestone(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        return dataManager.getConfig().getInt("dwarven-data.commissions.current-milestone");
    }

    public void setPlayerCommissionMilestone(Player player, int level){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        dataManager.getConfig().set("dwarven-data.commissions.current-milestone", level);

        dataManager.saveConfig();
    }

    public void setPlayerCommissionTotal(Player player, int amount){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        dataManager.getConfig().set("dwarven-data.commissions.total-complete", amount);

        dataManager.saveConfig();

        CommissionMilestoneGUI commissionMilestoneGUI = new CommissionMilestoneGUI();

        int milestone = getPlayerCommissionMilestone(player);

        if(milestone < commissionMilestoneGUI.maxLevel){
            int required = commissionMilestoneGUI.getRequiredCount(milestone + 1);

            if(amount >= required){
                player.sendMessage(commissionMilestoneGUI.getMilestoneMessage(milestone + 1));
            }
        }


        new DwarvenTablist().updateTab(player);
    }

    public String getPlayerCommissionID(Player player, int slot){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 4) slot = 4;

        return dataManager.getConfig().getString("dwarven-data.commissions.slot-" + slot + ".id");
    }

    public void setPlayerCommissionID(Player player, int slot, String id){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 4) slot = 4;

        dataManager.getConfig().set("dwarven-data.commissions.slot-" + slot + ".id", id);

        dataManager.saveConfig();

        new DwarvenTablist().updateTab(player);
    }

    public void setPlayerCommissionProgress(Player player, int slot, int progress){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 4) slot = 4;

        dataManager.getConfig().set("dwarven-data.commissions.slot-" + slot  + ".progress", progress);

        DwarvenManager dwarvenManager = plugin.dwarvenManager;
        if(!dwarvenManager.commissionCompletionAlertedPlayers.containsKey(player.getUniqueId())){
            List<Integer> slotList = new ArrayList<>();

            dwarvenManager.commissionCompletionAlertedPlayers.put(player.getUniqueId(), slotList);

        }

        if(!dwarvenManager.commissionCompletionAlertedPlayers.get(player.getUniqueId()).contains(slot)){
            String commissionID = getPlayerCommissionID(player, slot);

            DwarvenCommission dwarvenCommission = plugin.dwarvenManager.getDwarvenCommission(commissionID);

            if(dwarvenCommission != null){
                int required = dwarvenCommission.requiredCountForCompletion();

                if(progress >= required){
                    player.sendMessage(StringUtils.translate("&aYou've finished the " + dwarvenCommission.commissionName() + " &acommission, visit the &6&lKing &aor one of his &6Emissaries &ato claim your rewards!"));

                    dwarvenManager.commissionCompletionAlertedPlayers.get(player.getUniqueId()).add(slot);
                }
            }
        }

        dataManager.saveConfig();

        new DwarvenTablist().updateTab(player);
    }

    public int getPlayerCommissionProgress(Player player, int slot){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 4) slot = 4;

        return dataManager.getConfig().getInt("dwarven-data.commissions.slot-" + slot + ".progress");
    }

    public boolean hasClaimedMilestone(Player player, int milestone){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(milestone > 6) milestone = 6;

        return dataManager.getConfig().getBoolean("dwarven-data.commissions.milestone-" + milestone);
    }

    public void setPlayerForgeItemID(Player player, int slot, String id){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 5) slot = 5;

        dataManager.getConfig().set("dwarven-data.forge.slot-" + slot + ".item", id);

        dataManager.saveConfig();

        new DwarvenTablist().updateTab(player);
    }

    public String getPlayerForgeItemID(Player player, int slot){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 5) slot = 5;

        return dataManager.getConfig().getString("dwarven-data.forge.slot-" + slot + ".item");
    }

    public int getHOTMLevel(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        int upgradeLevel = dataManager.getConfig().getInt("dwarven-data.level");

        return upgradeLevel;
    }

    public Date getPlayerForgeFinishDate(Player player, int slot) throws ParseException {
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 5) slot = 5;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(Calendar.getInstance().getTimeZone());

        return dateFormat.parse(dataManager.getConfig().getString("dwarven-data.forge.slot-" + slot + ".finish"));
    }

    public void setPlayerForgeFinishDate(Player player, int slot, Date date){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 5) slot = 5;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dataManager.getConfig().set("dwarven-data.forge.slot-" + slot + ".finish", dateFormat.format(date));

        dataManager.saveConfig();

        new DwarvenTablist().updateTab(player);
    }

    public void resetPlayerForgeSlot(Player player, int slot){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        if(slot > 5) slot = 5;

        dataManager.getConfig().set("dwarven-data.forge.slot-" + slot + ".finish", "");
        dataManager.getConfig().set("dwarven-data.forge.slot-" + slot + ".item", "");

        dataManager.saveConfig();

    }

    public int getFirstEmptyForgeSlot(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        int emptySlot = 0;
        for(int i = 1; i < 6; i++){
            String itemID = getPlayerForgeItemID(player, i);

            if(itemID.equals("") || itemID.length() < 1){
                emptySlot = i;

                break;
            }
        }

        return emptySlot;
    }

    public List<String> getAllPlayerCommissionIDs(Player player){
        List<String> ids = new ArrayList<>();

        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        ids.add(dataManager.getConfig().getString("dwarven-data.commissions.slot-1.id"));
        ids.add(dataManager.getConfig().getString("dwarven-data.commissions.slot-2.id"));
        ids.add(dataManager.getConfig().getString("dwarven-data.commissions.slot-3.id"));
        ids.add(dataManager.getConfig().getString("dwarven-data.commissions.slot-4.id"));

        return ids;
    }

}
