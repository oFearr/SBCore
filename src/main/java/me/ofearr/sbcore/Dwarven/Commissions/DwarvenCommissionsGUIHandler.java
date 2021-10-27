package me.ofearr.sbcore.Dwarven.Commissions;

import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.customitems.Utils.IssueItemUtil;
import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import me.ofearr.skillcore.Skills.SkillManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DwarvenCommissionsGUIHandler implements Listener {

    private static SBCore plugin;
    public DwarvenCommissionsGUIHandler(SBCore sbCore){
        this.plugin = sbCore;
    }

    private static ItemStack createNewCommissionItem(Player player, int commissionSlot){
        ItemStack commissionTemplateItem = new ItemStack(Material.BOOK_AND_QUILL);

        DwarvenManager dwarvenManager = plugin.dwarvenManager;
        DwarvenUtils dwarvenUtils = new DwarvenUtils();

        ItemStack commissionItem = commissionTemplateItem.clone();

        String commissionID = dwarvenUtils.getPlayerCommissionID(player, commissionSlot);

        if(commissionID.length() < 1 || commissionID == ""){
            commissionID = dwarvenManager.selectNewPlayerCommission(player, commissionSlot);

        }

        DwarvenCommission commission = dwarvenManager.getDwarvenCommission(commissionID);

        ItemMeta commissionMeta = commissionItem.getItemMeta();
        commissionMeta.setDisplayName(StringUtils.translate("&6Commission #" + commissionSlot));

        List<String> commissionLore = new ArrayList<>();

        commissionLore.add(StringUtils.translate("&7Commissions are tasks given"));
        commissionLore.add(StringUtils.translate("&7directly by the &6&lKing &7which give"));
        commissionLore.add(StringUtils.translate("&7bountiful rewards."));
        commissionLore.add(" ");
        commissionLore.add(StringUtils.translate("&9" + commission.commissionName()));

        String[] descriptionWords = commission.description().split("\n");

        for(String text : descriptionWords){
            if(text != ""){
                commissionLore.add(StringUtils.translate(text));
            }
        }

        commissionLore.add(" ");
        commissionLore.add(StringUtils.translate("&9Rewards"));
        if(dwarvenManager.getDailyCommissionsCount(player) < plugin.getConfig().getInt("dwarven-settings.commissions.max-daily-commission-bonus")){
            commissionLore.add(StringUtils.translate("&7- &5950 HotM Exp"));

        } else{
            commissionLore.add(StringUtils.translate("&7- &5100 HotM Exp"));
        }

        int minMithrilPowder = plugin.getConfig().getInt("dwarven-settings.commissions.min-powder-reward");
        int maxMithrilPowder = plugin.getConfig().getInt("dwarven-settings.commissions.max-powder-reward");

        int minXP = plugin.getConfig().getInt("dwarven-settings.commissions.min-xp-reward");
        int maxXP = plugin.getConfig().getInt("dwarven-settings.commissions.max-xp-reward");

        commissionLore.add(StringUtils.translate("&7- &2" + minMithrilPowder + " -> " + maxMithrilPowder + " Mithril Powder"));
        commissionLore.add(StringUtils.translate("&7- &3" + minXP + " -> " + maxXP + " Mining Exp"));

        commissionLore.add(" ");

        int requiredProgress = commission.completionProgress();
        int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, commissionSlot);

        if(currentProgress >= requiredProgress){
            commissionLore.add(StringUtils.translate("&a&lCLICK TO CLAIM"));
        } else {
            commissionLore.add(StringUtils.translate("&9Progress"));

            double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.applyPattern("#,###.#");

            DecimalFormat percentageFormat = new DecimalFormat();
            percentageFormat.applyPattern("#.#");

            if(percentFinishedCommission > 100){
                percentFinishedCommission = 100;
            }

            commissionLore.add(StringUtils.translate("&e" + decimalFormat.format(currentProgress) + "&6/&e" + StringUtils.formatNumber(requiredProgress) + " &7(&b" + percentageFormat.format(percentFinishedCommission) + "%&7)"));
        }

        commissionMeta.setLore(commissionLore);
        commissionItem.setItemMeta(commissionMeta);

        return commissionItem;

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractWithCommissionsMenu(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();

        ItemStack item = e.getCurrentItem();

        if(item == null) return;
        if(e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;

        if(e.getView().getTopInventory().getTitle().equalsIgnoreCase(StringUtils.translate("Dwarven Commissions"))){
            if(item.hasItemMeta()){
                if(item.getItemMeta().hasLore()){

                    String itemName = item.getItemMeta().getDisplayName();

                    if(itemName.equalsIgnoreCase(StringUtils.translate("&aYour Milestones"))){
                        player.openInventory(new CommissionMilestoneGUI().GUI(player));
                    }

                    else if(item.getItemMeta().getLore().contains(StringUtils.translate("&a&lCLICK TO CLAIM"))){
                        DwarvenUtils dwarvenUtils = new DwarvenUtils();
                        int commissionSlot = Integer.parseInt(ChatColor.stripColor(item.getItemMeta().getDisplayName().replace("Commission #", "")));

                        String commissionID = dwarvenUtils.getPlayerCommissionID(player, commissionSlot);
                        DwarvenManager dwarvenManager = plugin.dwarvenManager;

                        if(dwarvenManager.getDwarvenCommission(commissionID) != null){
                            DwarvenCommission dwarvenCommission = dwarvenManager.getDwarvenCommission(commissionID);

                            player.sendMessage(StringUtils.translate("&aSuccessfully claimed your rewards from completing the &b" + dwarvenCommission.commissionName() + " &acommission!"));
                        }

                        int minMithrilPowder = plugin.getConfig().getInt("dwarven-settings.commissions.min-powder-reward");
                        int maxMithrilPowder = plugin.getConfig().getInt("dwarven-settings.commissions.max-powder-reward");

                        int minXP = plugin.getConfig().getInt("dwarven-settings.commissions.min-xp-reward");
                        int maxXP = plugin.getConfig().getInt("dwarven-settings.commissions.max-xp-reward");

                        Random random = new Random();
                        int selectedPowderDropAmount;

                        if(minMithrilPowder == 1 && maxMithrilPowder == 1){
                            selectedPowderDropAmount = 1;

                        } else {
                            selectedPowderDropAmount = random.nextInt(maxMithrilPowder - minMithrilPowder) + minMithrilPowder;
                        }

                        int selectedXPAmount;

                        if(minXP == 1 && maxXP == 1){
                            selectedXPAmount = 1;

                        } else {
                            selectedXPAmount = random.nextInt(maxXP - minXP) + minXP;
                        }

                        int currentPowder = dwarvenUtils.getMithrilPowderForPlayer(player);
                        currentPowder = currentPowder + selectedPowderDropAmount;

                        dwarvenUtils.setMithrilPowder(player, currentPowder);

                        SkillManager skillManager = new SkillManager();
                        skillManager.addMiningSkillXP(player, selectedXPAmount);

                        int currentHotMXP = dwarvenUtils.getHOTMXPForPlayer(player);

                        if(dwarvenManager.getDailyCommissionsCount(player) < plugin.getConfig().getInt("dwarven-settings.commissions.max-daily-commission-bonus")){
                            currentHotMXP = currentHotMXP + 950;

                        } else{
                            currentHotMXP = currentHotMXP + 100;
                        }

                        dwarvenUtils.setHOTMXP(player, currentHotMXP);

                        int commissionsTotal = dwarvenUtils.getPlayerCommissionTotal(player);
                        dwarvenUtils.setPlayerCommissionTotal(player, commissionsTotal+1);

                        if(dwarvenManager.commissionCompletionAlertedPlayers.containsKey(player.getUniqueId()) && dwarvenManager.commissionCompletionAlertedPlayers.get(player.getUniqueId()).contains(commissionSlot)){
                            int removeIndex = dwarvenManager.commissionCompletionAlertedPlayers.get(player.getUniqueId()).indexOf(commissionSlot);

                            dwarvenManager.commissionCompletionAlertedPlayers.get(player.getUniqueId()).remove(removeIndex);
                        }

                        dwarvenManager.setDailyCommissionsCount(player, dwarvenManager.getDailyCommissionsCount(player) + 1);
                        dwarvenManager.selectNewPlayerCommission(player, commissionSlot);


                        int slot = e.getSlot();

                        ItemStack newItem = createNewCommissionItem(player, commissionSlot);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                e.getClickedInventory().setItem(slot, newItem);
                            }
                        }.runTaskLater(plugin, 1L);

                    }
                }
            }

            e.setCancelled(true);

        } else if(e.getView().getTopInventory().getTitle().equalsIgnoreCase(StringUtils.translate("Commission Milestones"))){
            if(item.hasItemMeta()) {
                if (item.getItemMeta().hasLore()) {

                    String itemName = item.getItemMeta().getDisplayName();

                    if(item.getItemMeta().getLore().contains(StringUtils.translate("&eClick to claim!"))){

                        SkillManager skillManager = new SkillManager();
                        DwarvenUtils dwarvenUtils = new DwarvenUtils();

                        if(itemName.equalsIgnoreCase("&aMilestone 1 Rewards")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.dwarven.emissarycarlton true");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.dwarven.emissaryceanna true");
                            skillManager.addMiningSkillXP(player, 100000);

                        } else if(itemName.equalsIgnoreCase("&aMilestone 2 Rewards")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.dwarven.emissarywilson true");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.dwarven.emissarylilith true");

                            skillManager.addMiningSkillXP(player, 200000);

                        } else if(itemName.equalsIgnoreCase("&aMilestone 3 Rewards")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.dwarven.emissaryfraiser true");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.dwarven.emissaryeliza true");

                            skillManager.addMiningSkillXP(player, 400000);

                            int currentCommissionSlots = dwarvenUtils.getPlayerCommissionSlotCount(player);
                            dwarvenUtils.setPlayerCommissionSlotCount(player, currentCommissionSlots+1);

                        } else if(itemName.equalsIgnoreCase("&aMilestone 4 Rewards")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.warps.dwarven");

                            skillManager.addMiningSkillXP(player, 800000);

                        } else if(itemName.equalsIgnoreCase("&aMilestone 5 Rewards")){

                            if(ItemManager.getCustomItem("royal_pigeon") != null){
                                IssueItemUtil.issueItemWithoutMessage(player, ItemManager.getCustomItem("royal_pigeon"));
                            }

                            skillManager.addMiningSkillXP(player, 1600000);

                        } else if(itemName.equalsIgnoreCase("&aMilestone 6 Rewards")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set sbcore.warps.crystalhollows");

                            skillManager.addMiningSkillXP(player, 2400000);

                        }

                        player.closeInventory();
                        player.sendMessage(StringUtils.translate("&aSuccessfully claimed your milestone rewards!"));
                    }
                }
            }

            e.setCancelled(true);
        }

    }
}
