package me.ofearr.sbcore.Tablist;

import com.keenant.tabbed.Tabbed;
import com.keenant.tabbed.item.TextTabItem;
import com.keenant.tabbed.tablist.TableTabList;
import com.keenant.tabbed.util.Skins;
import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.DwarvenUtils;
import me.ofearr.sbcore.Dwarven.PowderType;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class DefaultTablist {

    private static SBCore plugin = SBCore.plugin;

    public void setCurrentTablist(Player player){
        Tabbed tabbed = SBCore.getTabbed();

        tabbed.destroyTabList(player);

        TableTabList tab = tabbed.newTableTabList(player);

        tab.setHeader(StringUtils.translate("&e&lYou're playing on mc.BetterSB.net!"));
        tab.setFooter(StringUtils.translate("&bTest Footer"));

        tab.set(1, 0, new TextTabItem(StringUtils.translate("       &3&lServer Info       "), 1, Skins.getDot(ChatColor.DARK_AQUA)));
        tab.set(2, 0, new TextTabItem(StringUtils.translate("       &b&lPlayer Info       "), 1, Skins.getDot(ChatColor.AQUA)));

        new BukkitRunnable() {
            @Override
            public void run() {
                tab.set(1, 1, new TextTabItem(plugin.areaManager.getLastKnownPlayerLocation(player), 1));
            }
        }.runTaskLater(plugin, 1L);

        TablistHandler.setPlayerTabList(player, tab);
    }
}
