package me.ofearr.sbcore.Dwarven.HeartOfTheMountain;

import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.Upgrades.DwarvenUpgrade;
import me.ofearr.sbcore.PlayerData.PlayerDataManager;
import me.ofearr.sbcore.Utils.SkullGenerator;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public class HotMTreeMenu {

    public Inventory GUI(Player player, int page){
        Inventory inv = Bukkit.createInventory(null, 54, "Heart of the Mountain");

        PlayerDataManager dataManager = new PlayerDataManager();
        dataManager.load(player);

        int hotmTokens = dataManager.getConfig().getInt("dwarven-data.hotm-tokens");
        int mithrilPowder = dataManager.getConfig().getInt("dwarven-data.mithril-powder");
        int gemstonePowder = dataManager.getConfig().getInt("dwarven-data.gemstone-powder");

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemStack backItem = new ItemStack(Material.ARROW);
        ItemStack upItem = new ItemStack(Material.ARROW);
        ItemStack downItem = new ItemStack(Material.ARROW);
        ItemStack hotmItem = SkullGenerator.getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODZmMDZlYWEzMDA0YWVlZDA5YjNkNWI0NWQ5NzZkZTU4NGU2OTFjMGU5Y2FkZTEzMzYzNWRlOTNkMjNiOWVkYiJ9fX0=");

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(StringUtils.translate("&cClose"));
        closeItem.setItemMeta(closeMeta);

        ItemMeta backMeta = backItem.getItemMeta();
        backMeta.setDisplayName(StringUtils.translate("&aGo Back"));

        List<String> backLore = new ArrayList<>();

        backLore.add(StringUtils.translate("&7To SkyBlock Menu"));
        backMeta.setLore(backLore);
        backItem.setItemMeta(backMeta);

        ItemMeta hotmMeta = hotmItem.getItemMeta();
        List<String> hotmLore = new ArrayList<>();

        hotmMeta.setDisplayName(StringUtils.translate("&5Heart of the Mountain"));
        hotmLore.add(StringUtils.translate("&7Token of the Mountain: &5" + hotmTokens));
        hotmLore.add(" ");
        hotmLore.add(StringUtils.translate("&8Unlock more &5Token of the"));
        hotmLore.add(StringUtils.translate("&5Mountain &8by leveling up your"));
        hotmLore.add(StringUtils.translate("&8HotM tiers."));
        hotmLore.add(StringUtils.translate(" "));
        hotmLore.add(StringUtils.translate("&9᠅ Powder"));
        hotmLore.add(StringUtils.translate("&9Powders &8are dropped from"));
        hotmLore.add(StringUtils.translate("&8mining ores in the &2Dwarven"));
        hotmLore.add(StringUtils.translate("&2Mines &8and are used to upgrade"));
        hotmLore.add(StringUtils.translate("&8the perks you've unlocked!"));
        hotmLore.add(StringUtils.translate(" "));
        hotmLore.add(StringUtils.translate("&7Mithril Powder: &2" + mithrilPowder));
        hotmLore.add(StringUtils.translate("&7Gemstone Powder: &d" + gemstonePowder));
        hotmLore.add(StringUtils.translate(" "));
        hotmLore.add(StringUtils.translate("&8Increase your chance to gain"));
        hotmLore.add(StringUtils.translate("&8extra Powder by unlocking perks,"));
        hotmLore.add(StringUtils.translate("&8equipping the &2Mithril Golem"));
        hotmLore.add(StringUtils.translate("&2Pet&8, and more!"));

        hotmMeta.setLore(hotmLore);
        hotmItem.setItemMeta(hotmMeta);

        ItemMeta upMeta = upItem.getItemMeta();
        upMeta.setDisplayName(StringUtils.translate("&aScroll Up"));
        upItem.setItemMeta(upMeta);

        ItemMeta downMeta = upItem.getItemMeta();
        downMeta.setDisplayName(StringUtils.translate("&aScroll Down"));
        downItem.setItemMeta(downMeta);


        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, fillerItem);
        }

        ArrayList<ItemStack> hotmLevelItems = new HeartOfTheMountain().getDisplayItems(player);
        Collection<DwarvenUpgrade> dwarvenUpgrades = DwarvenManager.getRegisteredUpgrades().values();

        LinkedHashMap<Integer, List<DwarvenUpgrade>> levelUpgrades = new LinkedHashMap<>();

        for(DwarvenUpgrade dwarvenUpgrade : dwarvenUpgrades){
            int requirement = dwarvenUpgrade.requiredHOTMLevel();

            if(!levelUpgrades.containsKey(requirement)){
                List<DwarvenUpgrade> newUpgradeList = new ArrayList<>();

                levelUpgrades.put(requirement, newUpgradeList);
            }

            levelUpgrades.get(requirement).add(dwarvenUpgrade);

        }

        inv.setItem(45, closeItem);
        inv.setItem(48, backItem);
        inv.setItem(49, hotmItem);


        int[] tier1Slots = {40};
        int[] tier2Slots = {31,30,29,32,33};
        int[] tier3Slots = {22,20,24};
        int[] tier4Slots = {13,12,11,14,15,16};
        int[] tier5Slots = {4,2,6};

        int[] tier52Slots = {40,38,42};
        int[] tier6Slots = {31,30,29,32,33};
        int[] tier7Slots = {20,22,24};

        if(page <= 1){
            inv.setItem(36, hotmLevelItems.get(0));
            inv.setItem(27, hotmLevelItems.get(1));
            inv.setItem(18, hotmLevelItems.get(2));
            inv.setItem(9, hotmLevelItems.get(3));
            inv.setItem(8, upItem);
            inv.setItem(0, hotmLevelItems.get(4));

            for(int i = 0; i < tier1Slots.length; i++){
                int slot = tier1Slots[i];

                DwarvenUpgrade dwarvenUpgrade = levelUpgrades.get(1).get(i);

                inv.setItem(slot, dwarvenUpgrade.getDisplayItem(player));
            }

            for(int i = 0; i < tier2Slots.length; i++){
                int slot = tier2Slots[i];

                inv.setItem(slot, levelUpgrades.get(2).get(i).getDisplayItem(player));
            }

            for(int i = 0; i < tier3Slots.length; i++){
                int slot = tier3Slots[i];

                inv.setItem(slot, levelUpgrades.get(3).get(i).getDisplayItem(player));
            }

            for(int i = 0; i < tier4Slots.length; i++){
                int slot = tier4Slots[i];

                inv.setItem(slot, levelUpgrades.get(4).get(i).getDisplayItem(player));
            }

            for(int i = 0; i < tier5Slots.length; i++){
                int slot = tier5Slots[i];

                inv.setItem(slot, levelUpgrades.get(5).get(i).getDisplayItem(player));
            }
        } else if(page >= 2){
            inv.setItem(36, hotmLevelItems.get(4));
            inv.setItem(27, hotmLevelItems.get(5));
            inv.setItem(18, hotmLevelItems.get(6));

            inv.setItem(53, downItem);

            for(int i = 0; i < tier52Slots.length; i++){
                int slot = tier52Slots[i];

                inv.setItem(slot, levelUpgrades.get(5).get(i).getDisplayItem(player));
            }

            for(int i = 0; i < tier6Slots.length; i++){
                int slot = tier6Slots[i];

                inv.setItem(slot, levelUpgrades.get(6).get(i).getDisplayItem(player));
            }

            for(int i = 0; i < tier7Slots.length; i++){
                int slot = tier7Slots[i];

                inv.setItem(slot, levelUpgrades.get(7).get(i).getDisplayItem(player));
            }

        }


        return inv;
    }
}
