package me.ofearr.sbcore.BlockManagement;

import me.ofearr.customitems.Enchants.EnchantManager;
import me.ofearr.customitems.Enchants.General.TelekinesisEnchant;
import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.customitems.Utils.IssueItemUtil;
import me.ofearr.playerstatscore.API.StatAPIManager;
import me.ofearr.sbcore.Areas.AreaManager;
import me.ofearr.sbcore.Collections.CollectionsManager;
import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommissionsHandler;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.Dwarven.Upgrades.Tier2Upgrades.AutoSmeltUpgrade;
import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.SBCore;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CustomBlockManager {

    private static SBCore plugin;
    private static AreaManager areaManager;

    public CustomBlockManager(SBCore sbCore, AreaManager aM) {
        this.plugin = sbCore;
        this.areaManager = aM;
    }

    //Used to store the replacement state of titanium
    private static HashMap<Location, Material> titaniumBlockTypeTracker = new HashMap<>();
    private static HashMap<Location, Byte> titaniumBlockDataTracker = new HashMap<>();

    public static String getCustomBlockID(Location location) {
        Block block = location.getBlock();

        Set<String> customBlockRegions = plugin.getConfig().getConfigurationSection("custom-block-settings").getKeys(false);

        for (String regionID : customBlockRegions) {
            Set<String> regionDefinedBlocks = plugin.getConfig().getConfigurationSection("custom-block-settings." + regionID).getKeys(false);

            for (String configBlockID : regionDefinedBlocks) {

                if (block.getType().toString().toLowerCase().equalsIgnoreCase(configBlockID)) {
                    if (areaManager.regionContainsLoc(regionID, location)) {

                        String customBlockID = plugin.getConfig().getString("custom-block-settings." + regionID + "." + configBlockID + ".id");

                        byte blockData = block.getData();
                        byte configData = (byte) plugin.getConfig().getInt("custom-block-settings." + regionID + "." + configBlockID + ".data");

                        if(configData == 0 || (blockData == configData)){
                            return customBlockID;

                        } else{
                            return block.getType().toString().toLowerCase();
                        }

                    }
                }
            }

        }

        return block.getType().toString().toLowerCase();
    }

    public static String getCustomBlockRegion(Block block) {

        Set<String> customBlockRegions = plugin.getConfig().getConfigurationSection("custom-block-settings").getKeys(false);

        for (String regionID : customBlockRegions) {
            Set<String> regionDefinedBlocks = plugin.getConfig().getConfigurationSection("custom-block-settings." + regionID).getKeys(false);

            for (String configBlockID : regionDefinedBlocks) {

                if (block.getType().toString().toLowerCase().equalsIgnoreCase(configBlockID)) {
                    if (areaManager.regionContainsLoc(regionID, block.getLocation())) {

                        return regionID;
                    }
                }
            }

        }

        return block.getType().toString().toLowerCase();
    }

    public static void handleBlockBreak(Player player, final Block block) {
        BlockEvents.getBrokenBlocksService().removeBrokenBlock(block.getLocation());
        String blockID = getCustomBlockID(block.getLocation());
        Location loc = block.getLocation();

        DwarvenCommissionsHandler dwarvenCommissionsHandler = new DwarvenCommissionsHandler();

        Material blockMaterial = block.getType();
        byte blockData = block.getData();

        String replaceWithBlockID = plugin.getConfig().getString("block-hardness-settings." + blockID + ".replace-with").toUpperCase();
        String regionID = getCustomBlockRegion(block);
        Material replacementMaterial = Material.getMaterial(replaceWithBlockID);

        String vanillaBlockID = block.getType().toString().toLowerCase();

        if(blockID.equalsIgnoreCase("mithril")){

            DwarvenUtils dwarvenUtils = new DwarvenUtils();
            int powderToGive = dwarvenUtils.calculatePowder(player);
            int currentPowder = dwarvenUtils.getMithrilPowderForPlayer(player);

            int totalPowder = currentPowder + powderToGive;

            dwarvenUtils.setMithrilPowder(player, totalPowder);

            dwarvenCommissionsHandler.handleMithrilMined(player, block.getLocation());
            dwarvenCommissionsHandler.handleMithrilPowderCollected(player, powderToGive);
        }

        if(blockID.equalsIgnoreCase("mithril")){
            if(new DwarvenUtils().rolledTitaniumSpawn(player)){
                replaceWithBlockID = "titanium";
            }
        }


        World world = loc.getWorld();

        if(getCustomBlockID(loc).equalsIgnoreCase("titanium")){

            if(!titaniumBlockDataTracker.containsKey(loc)){
                world.getBlockAt(loc).setType(Material.PRISMARINE);
                world.getBlockAt(loc).setData((byte) 1);

            } else {
                handleBlockRegeneration(block, titaniumBlockTypeTracker.get(loc), titaniumBlockDataTracker.get(loc), "mithril");
                world.getBlockAt(loc).setType(replacementMaterial);
                world.getBlockAt(loc).setData((byte) 0);
            }


            dwarvenCommissionsHandler.handleTitaniumMined(player, loc);
        } else {

            if(replaceWithBlockID.equalsIgnoreCase("titanium")){
                titaniumBlockTypeTracker.put(block.getLocation(), block.getType());
                titaniumBlockDataTracker.put(block.getLocation(), block.getData());

               // world.getBlockAt(loc).setType(Material.STONE);
               // world.getBlockAt(loc).setData((byte) 4);

                world.getBlockAt(loc).setType(Material.IRON_BLOCK);

            }else {
                world.getBlockAt(loc).setType(replacementMaterial);
                world.getBlockAt(loc).setData((byte) 0);
            }

        }


        Set<String> dropIDs = plugin.getConfig().getConfigurationSection("custom-block-settings." + regionID + "." + vanillaBlockID + ".drops").getKeys(false);
        HashMap<String, Integer> itemDrops = new HashMap<>();

        StatAPIManager statAPIManager = new StatAPIManager();

        HashMap<String, Integer> playerStats = statAPIManager.getAllPlayerStats(player);

        int playerBonusFortune = playerStats.get("Bonus Fortune") / 100;

        for (String dropID : dropIDs) {
            double dropChance = plugin.getConfig().getDouble("custom-block-settings." + regionID + "." + vanillaBlockID + ".drops." + dropID + ".chance");

            if (ThreadLocalRandom.current().nextDouble() <= dropChance) {
                int minDropAmount = plugin.getConfig().getInt("custom-block-settings." + regionID + "." + vanillaBlockID + ".drops." + dropID + ".min-amount");
                int maxDropAmount = plugin.getConfig().getInt("custom-block-settings." + regionID + "." + vanillaBlockID + ".drops." + dropID + ".max-amount");

                Random random = new Random();

                int selectedDropAmount;

                if(minDropAmount == 1 && maxDropAmount == 1){
                    selectedDropAmount = 1;

                } else {
                    selectedDropAmount = random.nextInt(maxDropAmount - minDropAmount) + minDropAmount;
                }

                boolean fortuneEffected = plugin.getConfig().getBoolean("custom-block-settings." + regionID + "." + vanillaBlockID + ".drops." + dropID + ".fortune-effected");

                if(fortuneEffected){
                    if (playerBonusFortune < 1) {
                        if (ThreadLocalRandom.current().nextDouble() <= playerBonusFortune) {

                            selectedDropAmount++;
                        }
                    } else {
                        while (playerBonusFortune >= 1) {
                            playerBonusFortune--;

                            selectedDropAmount++;
                        }

                        if (playerBonusFortune > 0) {
                            if (ThreadLocalRandom.current().nextDouble() <= playerBonusFortune) {

                                selectedDropAmount++;
                            }
                        }

                    }
                }

                itemDrops.put(dropID, selectedDropAmount);

            }


        }

        ItemStack heldItem = player.getItemInHand();

        boolean hasTelekinesis = false;

        if(heldItem != null && heldItem.getType() != Material.AIR){

            if(EnchantManager.checkForEnchantOnItem(heldItem, new TelekinesisEnchant())) hasTelekinesis = true;
        }

        PlayerDataManager dataManager = new PlayerDataManager();

        dataManager.load(player);

        boolean hasSmeltingTouch = false;
        if(dataManager.getConfig().getBoolean("dwarven-data.upgrades." + new AutoSmeltUpgrade().upgradeID() + ".enabled")){
            if(dataManager.getConfig().getInt("dwarven-data.upgrades." + new AutoSmeltUpgrade().upgradeID() + ".level") >= 1){
                hasSmeltingTouch = true;
            }
        }

        for(String itemID : itemDrops.keySet()){

            int dropAmount = itemDrops.get(itemID);

            if(hasSmeltingTouch){
                switch (itemID){
                    case "iron_ore":
                        itemID = "iron_ingot";
                        break;
                    case "gold_ore":
                        itemID = "gold_ingot";
                        break;
                }
            }

            ItemStack item = ItemManager.getCustomItem(itemID, dropAmount);

            if(item != null){

                if(hasTelekinesis && player.getInventory().firstEmpty() != -1){
                    player.getInventory().addItem(item);
                }else {
                    IssueItemUtil.dropItemAtLocation(block.getLocation(), item);
                }

            }

        }

        CollectionsManager.handlePlayerItemCollection(player, itemDrops);
        if(!getCustomBlockID(loc).equalsIgnoreCase("titanium")){
            handleBlockRegeneration(block, blockMaterial, blockData, blockID);
        }

    }

    public static void handleBlockRegeneration(Block block, Material regenMaterial, byte regenData, String blockID){
        int blockRegenTime = plugin.getConfig().getInt("block-hardness-settings." + blockID + ".regen-time") * 20;


        //Block currentBlockAtLocation = block.getLocation().getBlock();

        if(blockRegenTime > 0){

            new BukkitRunnable() {
                @Override
                public void run() {

                    if(AreaManager.regionContainsLoc("dwarven_mines", block.getLocation())){
                       // if(block.getType() == Material.STONE && block.getData() == 4) return;
                        if(block.getType() == Material.IRON_BLOCK) return;
                    }

                    if(block != null && block.getType() != Material.AIR){

                        block.setType(regenMaterial);
                        block.setData(regenData);

                        if(regenMaterial == Material.CROPS){
                            block.setData(CropState.RIPE.getData());
                        }

                    }
                }
            }.runTaskLater(plugin, blockRegenTime);
        }
    }

}
