package me.ofearr.sbcore.Dwarven;

import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions.Location.*;
import me.ofearr.sbcore.Dwarven.Commissions.MiningCommissions.*;
import me.ofearr.sbcore.Dwarven.Commissions.MiscCommissions.IceWalkerSlayerCommission;
import me.ofearr.sbcore.Dwarven.Commissions.MiscCommissions.MonolithExaminerCommission;
import me.ofearr.sbcore.Dwarven.Commissions.MiscCommissions.PowderCollectorCommission;
import me.ofearr.sbcore.Dwarven.Upgrades.DwarvenUpgrade;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier1Upgrades.*;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier2Upgrades.*;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier3Upgrades.*;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier4Upgrades.*;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier5Upgrades.*;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier6Upgrades.*;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier7Upgrades.*;
import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.entity.Player;

import java.util.*;

public class DwarvenManager {
    private static LinkedHashMap<String, DwarvenUpgrade> registeredUpgrades = new LinkedHashMap<>();
    private static HashMap<Integer, List<DwarvenCommission>> dwarvenCommissionPools = new HashMap<>();
    private static HashMap<String, DwarvenCommission> registeredDwarvenCommissions = new HashMap<>();
    private static HashMap<UUID, Integer> dailyCommissionsCount = new HashMap<>();
    public static HashMap<UUID, List<Integer>> commissionCompletionAlertedPlayers = new HashMap<>();
    public static HashMap<UUID, List<Integer>> forgeCompletionAlertedPlayers = new HashMap<>();

    public static void setRegisteredUpgrades(){
        //T1 Upgrades
        new MiningSpeedUpgrade().setRegistered();

        //T2 Upgrades
        new MiningFortuneUpgrade().setRegistered();
        new QuickForgeUpgrade().setRegistered();
        new AutoSmeltUpgrade().setRegistered();
        new TitaniumInsaniumUpgrade().setRegistered();
        new PickobulusUpgrade().setRegistered();

        //T3 Upgrades
        new CrystallizedUpgrade().setRegistered();
        new DailyPowderUpgrade().setRegistered();
        new LuckOfTheCaveUpgrade().setRegistered();

        //T4 Upgrades
        new PrecisionMiningUpgrade().setRegistered();
        new FrontLoadedUpgrade().setRegistered();
        new OrbiterUpgrade().setRegistered();
        new EfficientMinerUpgrade().setRegistered();
        new SeasonedMinemanUpgrade().setRegistered();
        new MiningMadnessUpgrade().setRegistered();

        //T5 Upgrades
        new PeakOfTheMountainUpgrade().setRegistered();
        new GoblinKillerUpgrade().setRegistered();
        new StarPowderUpgrade().setRegistered();

        //T6 Upgrades
        new ArchaeologistUpgrade().setRegistered();
        new FortunateUpgrade().setRegistered();
        new ImmaculateUpgrade().setRegistered();
        new MoleUpgrade().setRegistered();
        new ProfessionalUpgrade().setRegistered();

        //T7 Upgrades
        new MiningFortuneIIUpgrade().setRegistered();
        new MiningSpeedIIUpgrade().setRegistered();
        new PowderBuffUpgrade().setRegistered();
    }

    public static void setRegisteredDwarvenCommissions(){
        new MithrilMinerCommission().registerCommission();
        new TitaniumMinerCommission().registerCommission();

        new CliffsideVeinsMithrilCommission().registerCommission();
        new CliffsideVeinsTitaniumCommission().registerCommission();
        new FarReserveMithrilCommission().registerCommission();
        new FarReserveTitaniumCommission().registerCommission();
        new LavaSpringsMithrilCommission().registerCommission();
        new LavaSpringsTitaniumCommission().registerCommission();
        new RampartsQuarryMithrilCommission().registerCommission();
        new RampartsQuarryTitaniumCommission().registerCommission();
        new RoyalMinesMithrilCommission().registerCommission();
        new RoyalMinesTitaniumCommission().registerCommission();
        new UpperMinesMithrilCommission().registerCommission();
        new UpperMinesTitaniumCommission().registerCommission();

        new MonolithExaminerCommission().registerCommission();
        new PowderCollectorCommission().registerCommission();
        new IceWalkerSlayerCommission().registerCommission();

    }

    public static void putUpgradeToMap(String id, DwarvenUpgrade upgrade){
        registeredUpgrades.put(id, upgrade);
    }

    public static String selectNewPlayerCommission(Player player, int commissionPool){
        DwarvenUtils dwarvenUtils = new DwarvenUtils();

        if(commissionPool < 1){
            commissionPool = 1;
        } else if(commissionPool > 4){
            commissionPool = 4;
        }

        int parallelCommission;

        switch (commissionPool){
            case 1: parallelCommission = 3; break;
            case 2: parallelCommission = 4; break;
            case 4: parallelCommission = 2; break;
            default: parallelCommission = 1;
        }

        List<DwarvenCommission> poolCommissions = dwarvenCommissionPools.get(commissionPool);

        Random random = new Random();

        String newCommissionID = poolCommissions.get(random.nextInt(poolCommissions.size())).commissionID();
        final String parallelCommissionID = dwarvenUtils.getPlayerCommissionID(player, parallelCommission);


        while (newCommissionID == parallelCommissionID){

            DwarvenCommission dwarvenCommission = poolCommissions.get(random.nextInt(poolCommissions.size()));

            newCommissionID = dwarvenCommission.commissionID();

        }

        dwarvenUtils.setPlayerCommissionID(player, commissionPool, newCommissionID);
        dwarvenUtils.setPlayerCommissionProgress(player, commissionPool, 0);

        return newCommissionID;
    }

    public static void putDwarvenCommissionToMap(String id, DwarvenCommission commission){
        registeredDwarvenCommissions.put(id, commission);

        int commissionPool1 = commission.commissionPool1();
        int commissionPool2 = commission.commissionPool2();

        if(!dwarvenCommissionPools.containsKey(commissionPool1)){
            List<DwarvenCommission> commissionList = new ArrayList<>();

            dwarvenCommissionPools.put(commissionPool1, commissionList);
        }

        if(!dwarvenCommissionPools.containsKey(commissionPool2)){
            List<DwarvenCommission> commissionList = new ArrayList<>();

            dwarvenCommissionPools.put(commissionPool2, commissionList);
        }

        dwarvenCommissionPools.get(commissionPool1).add(commission);
        dwarvenCommissionPools.get(commissionPool2).add(commission);
    }

    public static LinkedHashMap<String, DwarvenUpgrade> getRegisteredUpgrades(){

        return registeredUpgrades;
    }

    public static int getUpgradeLevel(Player player, DwarvenUpgrade dwarvenUpgrade){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        int upgradeLevel = dataManager.getConfig().getInt("dwarven-data.upgrades." + dwarvenUpgrade.upgradeID() + ".level");

        return upgradeLevel;
    }

    public static DwarvenUpgrade getDwarvenUpgrade(String upgradeID){
        if(getRegisteredUpgrades().containsKey(upgradeID)){
            return getRegisteredUpgrades().get(upgradeID);
        }

        return null;
    }

    public static DwarvenCommission getDwarvenCommission(String commissionID){
        if(registeredDwarvenCommissions.containsKey(commissionID)){
            return registeredDwarvenCommissions.get(commissionID);
        }

        return null;
    }

    public static int getDailyCommissionsCount(Player player){
        if(dailyCommissionsCount.containsKey(player.getUniqueId())){

            return dailyCommissionsCount.get(player.getUniqueId());
        }

        return 0;
    }

    public static void setDailyCommissionsCount(Player player, int count){
        dailyCommissionsCount.put(player.getUniqueId(), count);
    }

    public void sendForgeCompletionNotif(Player player, int slot){
        if(!forgeCompletionAlertedPlayers.containsKey(player.getUniqueId())){
            List<Integer> slotList = new ArrayList<>();

            forgeCompletionAlertedPlayers.put(player.getUniqueId(), slotList);
        }

        if(!forgeCompletionAlertedPlayers.get(player.getUniqueId()).contains(slot)){
            DwarvenUtils dwarvenUtils = new DwarvenUtils();
            String forgeItemID = dwarvenUtils.getPlayerForgeItemID(player, slot);

            if(ItemManager.getCustomItem(forgeItemID) != null){
                String itemName = ItemManager.getCustomItem(forgeItemID).getItemMeta().getDisplayName();

                player.sendMessage(StringUtils.translate("&aYour process for creation of " + itemName + " &ahas finished and is ready for collection at the forge!"));
            }

            forgeCompletionAlertedPlayers.get(player.getUniqueId()).add(slot);
        }
    }

    public static int getHOTMLevel(Player player){
        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        int upgradeLevel = dataManager.getConfig().getInt("dwarven-data.level");

        return upgradeLevel;
    }
}
