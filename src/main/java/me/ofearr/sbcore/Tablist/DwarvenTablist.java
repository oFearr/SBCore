package me.ofearr.sbcore.Tablist;

import com.keenant.tabbed.Tabbed;
import com.keenant.tabbed.item.TextTabItem;
import com.keenant.tabbed.tablist.TableTabList;
import com.keenant.tabbed.util.Skins;
import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommission;
import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.Dwarven.PowderType;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

public class DwarvenTablist {

    private static SBCore plugin = SBCore.plugin;
    private static DwarvenManager dwarvenManager = plugin.dwarvenManager;

    public void updateTab(Player player){
        TableTabList tab = TablistHandler.getPlayerTablist(player);

        if(tab != null){
            DwarvenUtils dwarvenUtils = new DwarvenUtils();

            int playerMithrilPowder = dwarvenUtils.getPowderForPlayer(player, PowderType.MITHRIL);
            int playerGemstonePowder = dwarvenUtils.getPowderForPlayer(player, PowderType.GEMSTONE);
            int playerCommissionSlots = dwarvenUtils.getPlayerCommissionSlotCount(player);
            String commission1ID = dwarvenUtils.getPlayerCommissionID(player, 1);
            String commission2ID = dwarvenUtils.getPlayerCommissionID(player, 2);
            String commission3ID = dwarvenUtils.getPlayerCommissionID(player, 3);
            String commission4ID = dwarvenUtils.getPlayerCommissionID(player, 4);

            String commission1Name;
            String commission2Name;
            String commission3Name;
            String commission4Name;

            int forgeSlotCount = dwarvenUtils.getPlayerForgeSlotCount(player);

            DecimalFormat decimalFormat = new DecimalFormat();

            decimalFormat.applyPattern("#,###");

            tab.setHeader(StringUtils.translate("&e&lYou're playing on mc.BetterSB.net!"));
            tab.setFooter(StringUtils.translate("&bTest Footer"));

            tab.set(1, 0, new TextTabItem(StringUtils.translate("               &3&lServer Info       "), 1, Skins.getDot(ChatColor.DARK_AQUA)));
            tab.set(2, 0, new TextTabItem(StringUtils.translate("               &b&lPlayer Info       "), 1, Skins.getDot(ChatColor.AQUA)));

            tab.set(1, 3, new TextTabItem(StringUtils.translate("&9&l᠅ Powders"), 1));
            tab.set(1, 4, new TextTabItem(StringUtils.translate("&f Mithril Powder: &2" + decimalFormat.format(playerMithrilPowder)), 1));
            tab.set(1, 5, new TextTabItem(StringUtils.translate("&f Gemstone Powder: &d" + decimalFormat.format(playerGemstonePowder)), 1));

            tab.set(1, 7, new TextTabItem(StringUtils.translate("&9&lCommissions"), 1));

            DecimalFormat percentageFormat = new DecimalFormat();
            percentageFormat.applyPattern("#");


            if(playerCommissionSlots >= 2){
                if(plugin.dwarvenManager.getDwarvenCommission(commission1ID) == null){
                    commission1Name = StringUtils.translate("&fTalk to the &6&lKing");
                } else {

                    DwarvenCommission commission = DwarvenManager.getDwarvenCommission(commission1ID);
                    int requiredProgress = commission.completionProgress();
                    int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, 1);

                    double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                    if(percentFinishedCommission > 100){
                        percentFinishedCommission = 100;
                    }

                    commission1Name = commission.commissionName() + "&f: &b" + percentageFormat.format(percentFinishedCommission) + "%";
                }
                if(plugin.dwarvenManager.getDwarvenCommission(commission2ID) == null){
                    commission2Name = StringUtils.translate("&fTalk to the &6&lKing");
                } else {

                    DwarvenCommission commission = DwarvenManager.getDwarvenCommission(commission2ID);
                    int requiredProgress = commission.completionProgress();
                    int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, 2);

                    double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                    if(percentFinishedCommission > 100){
                        percentFinishedCommission = 100;
                    }

                    commission2Name = commission.commissionName() + "&f: &b" + percentageFormat.format(percentFinishedCommission) + "%";
                }


                tab.set(1, 8, new TextTabItem(StringUtils.translate(" " + commission1Name), 1));
                tab.set(1, 9, new TextTabItem(StringUtils.translate(" " + commission2Name), 1));

            }

            if(playerCommissionSlots >= 3){
                if(plugin.dwarvenManager.getDwarvenCommission(commission3ID) == null){
                    commission3Name = StringUtils.translate("&fTalk to the &6&lKing");
                } else {

                    DwarvenCommission commission = DwarvenManager.getDwarvenCommission(commission3ID);
                    int requiredProgress = commission.completionProgress();
                    int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, 3);

                    double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                    if(percentFinishedCommission > 100){
                        percentFinishedCommission = 100;
                    }

                    commission3Name = commission.commissionName() + "&f: &b" + percentageFormat.format(percentFinishedCommission) + "%";
                }

                tab.set(1, 10, new TextTabItem(StringUtils.translate(" " + commission3Name), 1));


            }

            if(playerCommissionSlots >= 4){
                if(plugin.dwarvenManager.getDwarvenCommission(commission4ID) == null){
                    commission4Name = StringUtils.translate("&fTalk to the &6&lKing");
                } else {
                    DwarvenCommission commission = DwarvenManager.getDwarvenCommission(commission4ID);
                    int requiredProgress = commission.completionProgress();
                    int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, 4);

                    double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                    if(percentFinishedCommission > 100){
                        percentFinishedCommission = 100;
                    }

                    commission4Name = commission.commissionName() + "&f: &b" + percentageFormat.format(percentFinishedCommission) + "%";
                }

                tab.set(1, 11, new TextTabItem(StringUtils.translate(" " + commission4Name), 1));

            }

            tab.set(1, 13, new TextTabItem(StringUtils.translate("&9&lForges"), 1));

            int tabSlot = 14;
            try {
                for(int i = 1; i < 6; i++){
                    if(i > forgeSlotCount){
                        tab.set(1, tabSlot, new TextTabItem(StringUtils.translate(" &f" + i + ") &cLOCKED"), 1));
                    } else {
                        String forgeItemID = dwarvenUtils.getPlayerForgeItemID(player, i);

                        if(forgeItemID.length() < 1){
                            tab.set(1, tabSlot, new TextTabItem(StringUtils.translate(" &f" + i + ") &7EMPTY"), 1));

                        } else {

                            String displayName = forgeItemID;

                            if(ItemManager.getCustomItem(forgeItemID) != null){
                                displayName = ItemManager.getCustomItem(forgeItemID).getItemMeta().getDisplayName();
                            }

                            Date finishDate = dwarvenUtils.getPlayerForgeFinishDate(player, i);

                            Date now = new Date();

                            long difference = finishDate.getTime() - now.getTime();

                            String durationString = "";

                            long days = (difference / (1000 * 60 * 60 * 24)) % 365;
                            long hours = (difference / (1000 * 60 * 60)) % 24;
                            long minutes = (difference / (1000 * 60)) % 60;
                            long seconds = (difference / 1000) % 60;

                            if(days >= 1){
                                durationString = durationString + days + "d, ";
                            }

                            if(hours >= 1){
                                durationString = durationString + hours + "h, ";
                            }

                            if(minutes >= 1){
                                durationString = durationString + minutes + "m, ";
                            }

                            if(seconds >= 1){
                                durationString = durationString + seconds + "s";
                            }

                            if(durationString.equals("")){
                                tab.set(1, tabSlot, new TextTabItem(StringUtils.translate(" &f" + i + ") " + displayName + "&7: &aDONE"), 1));

                                dwarvenManager.sendForgeCompletionNotif(player, i);
                            } else {
                                tab.set(1, tabSlot, new TextTabItem(StringUtils.translate(" &f" + i + ") " + displayName + "&7: &b" + durationString), 1));
                            }

                        }
                    }

                    tabSlot++;
                }
            }catch (ParseException e){
            }


            new BukkitRunnable() {
                @Override
                public void run() {
                    tab.set(1, 1, new TextTabItem(plugin.areaManager.getLastKnownPlayerLocation(player), 1));
                }
            }.runTaskLater(plugin, 1L);
        }
    }

    public void setCurrentTablist(Player player){
        DwarvenUtils dwarvenUtils = new DwarvenUtils();

        int playerMithrilPowder = dwarvenUtils.getPowderForPlayer(player, PowderType.MITHRIL);
        int playerGemstonePowder = dwarvenUtils.getPowderForPlayer(player, PowderType.GEMSTONE);
        int playerCommissionSlots = dwarvenUtils.getPlayerCommissionSlotCount(player);
        String commission1ID = dwarvenUtils.getPlayerCommissionID(player, 1);
        String commission2ID = dwarvenUtils.getPlayerCommissionID(player, 2);
        String commission3ID = dwarvenUtils.getPlayerCommissionID(player, 3);
        String commission4ID = dwarvenUtils.getPlayerCommissionID(player, 4);

        String commission1Name;
        String commission2Name;
        String commission3Name;
        String commission4Name;

        int forgeSlotCount = dwarvenUtils.getPlayerForgeSlotCount(player);

        DecimalFormat decimalFormat = new DecimalFormat();

        decimalFormat.applyPattern("#,###");

        Tabbed tabbed = SBCore.getTabbed();

        tabbed.destroyTabList(player);

        TableTabList tab = tabbed.newTableTabList(player);

        tab.setHeader(StringUtils.translate("&e&lYou're playing on mc.BetterSB.net!"));
        tab.setFooter(StringUtils.translate("&bTest Footer"));

        tab.set(1, 0, new TextTabItem(StringUtils.translate("               &3&lServer Info       "), 1, Skins.getDot(ChatColor.DARK_AQUA)));
        tab.set(2, 0, new TextTabItem(StringUtils.translate("               &b&lPlayer Info       "), 1, Skins.getDot(ChatColor.AQUA)));

        tab.set(1, 3, new TextTabItem(StringUtils.translate("&9&l᠅ Powders"), 1));
        tab.set(1, 4, new TextTabItem(StringUtils.translate("&f Mithril Powder: &2" + decimalFormat.format(playerMithrilPowder)), 1));
        tab.set(1, 5, new TextTabItem(StringUtils.translate("&f Gemstone Powder: &d" + decimalFormat.format(playerGemstonePowder)), 1));

        tab.set(1, 7, new TextTabItem(StringUtils.translate("&9&lCommissions"), 1));

        DecimalFormat percentageFormat = new DecimalFormat();
        percentageFormat.applyPattern("#");


        if(playerCommissionSlots >= 2){
            if(plugin.dwarvenManager.getDwarvenCommission(commission1ID) == null){
                commission1Name = StringUtils.translate("&fTalk to the &6&lKing");
            } else {

                DwarvenCommission commission = DwarvenManager.getDwarvenCommission(commission1ID);
                int requiredProgress = commission.completionProgress();
                int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, 1);

                double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                commission1Name = commission.commissionName() + "&f: &b" + percentageFormat.format(percentFinishedCommission) + "%";
            }
            if(plugin.dwarvenManager.getDwarvenCommission(commission2ID) == null){
                commission2Name = StringUtils.translate("&fTalk to the &6&lKing");
            } else {

                DwarvenCommission commission = DwarvenManager.getDwarvenCommission(commission2ID);
                int requiredProgress = commission.completionProgress();
                int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, 2);

                double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                commission2Name = commission.commissionName() + "&f: &b" + percentageFormat.format(percentFinishedCommission) + "%";
            }


            tab.set(1, 8, new TextTabItem(StringUtils.translate(" " + commission1Name), 1));
            tab.set(1, 9, new TextTabItem(StringUtils.translate(" " + commission2Name), 1));

        }

        if(playerCommissionSlots >= 3){
            if(plugin.dwarvenManager.getDwarvenCommission(commission3ID) == null){
                commission3Name = StringUtils.translate("&fTalk to the &6&lKing");
            } else {

                DwarvenCommission commission = DwarvenManager.getDwarvenCommission(commission3ID);
                int requiredProgress = commission.completionProgress();
                int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, 3);

                double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                commission3Name = commission.commissionName() + "&f: &b" + percentageFormat.format(percentFinishedCommission) + "%";
            }

            tab.set(1, 10, new TextTabItem(StringUtils.translate(" " + commission3Name), 1));


        }

        if(playerCommissionSlots >= 4){
            if(plugin.dwarvenManager.getDwarvenCommission(commission4ID) == null){
                commission4Name = StringUtils.translate("&fTalk to the &6&lKing");
            } else {
                DwarvenCommission commission = DwarvenManager.getDwarvenCommission(commission4ID);
                int requiredProgress = commission.completionProgress();
                int currentProgress = dwarvenUtils.getPlayerCommissionProgress(player, 4);

                double percentFinishedCommission = (double) currentProgress / (double) requiredProgress * 100;

                commission4Name = commission.commissionName() + "&f: &b" + percentageFormat.format(percentFinishedCommission) + "%";
            }

            tab.set(1, 11, new TextTabItem(StringUtils.translate(" " + commission4Name), 1));

        }

        tab.set(1, 13, new TextTabItem(StringUtils.translate("&9&lForges"), 1));

        int tabSlot = 14;
        for(int i = 1; i < 6; i++){
            if(i > forgeSlotCount){
                tab.set(1, tabSlot, new TextTabItem(StringUtils.translate(" &f" + i + ") &cLOCKED"), 1));
            } else {
                String forgeItemID = dwarvenUtils.getPlayerForgeItemID(player, i);

                if(forgeItemID.length() < 1){
                    tab.set(1, tabSlot, new TextTabItem(StringUtils.translate(" &f" + i + ") &7EMPTY"), 1));

                } else {
                    //TEMP NEEDS CHANGED
                    tab.set(1, tabSlot, new TextTabItem(StringUtils.translate(" &f" + i + ") &7" + forgeItemID + ": &b?"), 1));
                }
            }

            tabSlot++;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                tab.set(1, 1, new TextTabItem(plugin.areaManager.getLastKnownPlayerLocation(player), 1));
            }
        }.runTaskLater(plugin, 1L);

        TablistHandler.setPlayerTabList(player, tab);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(player.getLocation().getWorld().getName().equalsIgnoreCase("world_dwarven_mines")){
                    updateTab(player);

                } else {
                    this.cancel();
                    return;
                }
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }
}
