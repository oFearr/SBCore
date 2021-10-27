package me.ofearr.sbcore.BlockManagement;

import me.ofearr.playerstatscore.API.StatAPIManager;
import me.ofearr.sbcore.Areas.AreaManager;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.NBTEditor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class BlockEvents implements Listener {

    private static SBCore plugin;

    public BlockEvents(SBCore sbCore){
        this.plugin = sbCore;
    }

    private static HashSet<Material> transparentBlocks = new HashSet<>();
    private static BrokenBlocksService brokenBlocksService = new BrokenBlocksService();

    static {
        transparentBlocks.add(Material.WATER);
        transparentBlocks.add(Material.AIR);
    }

    public static void addSlowDig(Player player, int duration){
        if(player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) removeSlowDig(player);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, duration, -1, false, false), true);
    }

    public static void removeSlowDig(Player player){
        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
    }

    public static BrokenBlocksService getBrokenBlocksService(){
        return brokenBlocksService;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockDamageEvent e){

        if(e.isCancelled()) return;

        Player player = e.getPlayer();

        int toolHardness = 1;
        ItemStack heldItem = player.getItemInHand();

        if(heldItem != null && heldItem.getType() != Material.AIR){
            if(NBTEditor.contains(heldItem, "tool_hardness")){
                toolHardness = NBTEditor.getInt(heldItem, "tool_hardness");

            }
        }

        String blockID = CustomBlockManager.getCustomBlockID(e.getBlock().getLocation());

        if(plugin.getConfig().get("block-hardness-settings." + blockID + ".hardness") == null ||
                plugin.getConfig().getInt("block-hardness-settings." + blockID + ".hardness") > toolHardness){
            e.setCancelled(true);

            return;
        }

        int blockStrength = plugin.getConfig().getInt("block-hardness-settings." + blockID + ".strength");

        StatAPIManager statAPIManager = new StatAPIManager();

        HashMap<String, Integer> playerStats = statAPIManager.getAllPlayerStats(player);

        int playerMiningSpeed = playerStats.get("Mining Speed");

        if(playerMiningSpeed <= 0) playerMiningSpeed = 1;
        int breakTime = (blockStrength * 30) / playerMiningSpeed;


        brokenBlocksService.createBrokenBlock(e.getBlock(), breakTime);


        if(breakTime <= 0){
            brokenBlocksService.getBrokenBlock(e.getBlock().getLocation()).breakBlock(player);

            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreakingBlock(PlayerAnimationEvent e){

        if(e.isCancelled()) return;

        Player player = e.getPlayer();

        Block block = player.getTargetBlock(transparentBlocks, 5);
        Location blockPosition = block.getLocation();

        if(!brokenBlocksService.isBrokenBlock(blockPosition)) {
            return;
        }

        int toolHardness = 1;
        ItemStack heldItem = player.getItemInHand();

        if(heldItem != null && heldItem.getType() != Material.AIR){
            if(NBTEditor.contains(heldItem, "tool_hardness")){
                toolHardness = NBTEditor.getInt(heldItem, "tool_hardness");

            }
        }

        String blockID = CustomBlockManager.getCustomBlockID(block.getLocation());

        if(plugin.getConfig().get("block-hardness-settings." + blockID + ".hardness") == null ||
                plugin.getConfig().getInt("block-hardness-settings." + blockID + ".hardness") > toolHardness){
            e.setCancelled(true);

            return;
        }

        double distanceX = blockPosition.getX() - player.getLocation().getX();
        double distanceY = blockPosition.getY() - player.getLocation().getY();
        double distanceZ = blockPosition.getZ() - player.getLocation().getZ();

        if(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ >= 1024.0D) return;

        addSlowDig(player, 200);
        brokenBlocksService.getBrokenBlock(blockPosition).incrementDamage(player, 1);
    }

}
