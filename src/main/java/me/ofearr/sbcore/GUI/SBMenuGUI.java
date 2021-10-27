package me.ofearr.sbcore.GUI;

import me.ofearr.playerstatscore.API.StatAPIManager;
import me.ofearr.sbcore.Utils.SkullGenerator;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SBMenuGUI {

    private StatAPIManager apiManager = new StatAPIManager();
    private static String TranslateColour(String s){

        String translated = ChatColor.translateAlternateColorCodes('&', s);

        return translated;
    }

    public Inventory openGUI(Player player){
        Inventory inv = Bukkit.createInventory(null, 54, "SkyBlock Menu");

        ItemStack fillerItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemStack statsItem = SkullGenerator.getSkull(player);
        ItemStack skillsItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack collectionsItem = new ItemStack(Material.PAINTING);
        ItemStack recipesItem = new ItemStack(Material.BOOK);
        ItemStack petsItem = new ItemStack(Material.BONE);
        ItemStack tradesItem = new ItemStack(Material.EMERALD);
        ItemStack questsItem = new ItemStack(Material.BOOK_AND_QUILL);
        ItemStack eventsItem = new ItemStack(Material.WATCH);
        ItemStack enderChestItem = new ItemStack(Material.ENDER_CHEST);
        ItemStack effectsItem = new ItemStack(Material.GLASS_BOTTLE);
        ItemStack craftingItem = new ItemStack(Material.WORKBENCH);
        ItemStack travelItem = SkullGenerator.getHeadFromString("Globe");
        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemStack settingsItem = new ItemStack(Material.REDSTONE_TORCH_ON);
        ItemStack potionBagItem = SkullGenerator.getHeadFromString("Purple_Potion");
        ItemStack talismanBagItem = SkullGenerator.getHeadFromString("Talisman_Sack");

        ItemMeta statsMeta = statsItem.getItemMeta();
        statsMeta.setDisplayName(TranslateColour("&aYour SkyBlock Profile"));
        List<String> statsLore = apiManager.getStatVisuals(player);

        statsLore.add(" ");
        statsLore.add(TranslateColour("&eClick to view your profile!"));
        statsMeta.setLore(statsLore);
        statsItem.setItemMeta(statsMeta);

        ItemMeta skillsMeta = skillsItem.getItemMeta();
        skillsMeta.setDisplayName(TranslateColour("&aYour Skills"));
        List<String> skillsLore = new ArrayList<>();

        skillsLore.add(TranslateColour("&7Your skill progression and"));
        skillsLore.add(TranslateColour("&7rewards."));
        skillsLore.add(" ");
        skillsLore.add(StringUtils.translate("&eClick to view!"));
        skillsMeta.setLore(skillsLore);
        skillsMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        skillsItem.setItemMeta(skillsMeta);

        ItemMeta collectionsMeta = collectionsItem.getItemMeta();
        collectionsMeta.setDisplayName(TranslateColour("&aCollections"));
        List<String> collectionsLore = new ArrayList<>();

        collectionsLore.add(TranslateColour("&7View all of the items available"));
        collectionsLore.add(TranslateColour("&7in SkyBlock. Collect more of an"));
        collectionsLore.add(TranslateColour("&7item to unlock rewards on your"));
        collectionsLore.add(TranslateColour("&7way to becoming a master of"));
        collectionsLore.add(TranslateColour("&7SkyBlock"));
        collectionsMeta.setLore(collectionsLore);
        collectionsItem.setItemMeta(collectionsMeta);

        ItemMeta recipeMeta = recipesItem.getItemMeta();
        recipeMeta.setDisplayName(TranslateColour("&aRecipe Book"));
        List<String> recipeLore = new ArrayList<>();

        recipeLore.add(TranslateColour("&7Through your adventure, you will"));
        recipeLore.add(TranslateColour("&7unlock recipes for all kinds of"));
        recipeLore.add(TranslateColour("&7special items! You can view how"));
        recipeLore.add(TranslateColour("&7to craft these items here."));
        recipeLore.add(" ");
        recipeLore.add(TranslateColour("&eClick to view!"));
        recipeMeta.setLore(recipeLore);
        recipesItem.setItemMeta(recipeMeta);

        ItemMeta petsMeta = recipesItem.getItemMeta();
        petsMeta.setDisplayName(TranslateColour("&aPets"));
        List<String> petsLore = new ArrayList<>();

        petsLore.add(TranslateColour("&7View and manage all of your"));
        petsLore.add(TranslateColour("&7Pets."));
        petsLore.add(TranslateColour(" "));
        petsLore.add(TranslateColour("&eClick to view!"));
        petsMeta.setLore(petsLore);
        petsItem.setItemMeta(petsMeta);

        ItemMeta tradesMeta = tradesItem.getItemMeta();
        tradesMeta.setDisplayName(TranslateColour("&aTrades"));
        List<String> tradesLore = new ArrayList<>();

        tradesLore.add(TranslateColour("&7View your available trades."));
        tradesLore.add(TranslateColour("&7These trades are always"));
        tradesLore.add(TranslateColour("&7available and accessible through"));
        tradesLore.add(TranslateColour("&7the SkyBlock Menu."));
        tradesLore.add(" ");
        tradesLore.add(TranslateColour("&eClick to view!"));
        tradesMeta.setLore(tradesLore);
        tradesItem.setItemMeta(tradesMeta);

        ItemMeta questMeta = questsItem.getItemMeta();
        questMeta.setDisplayName(TranslateColour("&aQuest Log"));
        List<String> questLore = new ArrayList<>();

        questLore.add(TranslateColour("&7View your active quests,"));
        questLore.add(TranslateColour("&7progress, and rewards."));
        questLore.add(" ");
        questLore.add(TranslateColour("&eClick to view!"));
        questMeta.setLore(questLore);
        questsItem.setItemMeta(questMeta);

        ItemMeta eventsMeta = eventsItem.getItemMeta();
        eventsMeta.setDisplayName(TranslateColour("&aCalendar and Events"));
        List<String> eventsLore = new ArrayList<>();

        eventsLore.add(TranslateColour("&7View the SkyBlock Calendar,"));
        eventsLore.add(TranslateColour("&7upcoming events, and event"));
        eventsLore.add(TranslateColour("&7rewards!"));
        eventsLore.add(" ");
        eventsLore.add(TranslateColour("&eClick to view!"));
        eventsMeta.setLore(eventsLore);
        eventsItem.setItemMeta(eventsMeta);

        ItemMeta enderChestMeta = enderChestItem.getItemMeta();
        enderChestMeta.setDisplayName(TranslateColour("&aEnder Chest"));
        List<String> enderChestLore = new ArrayList<>();

        enderChestLore.add(TranslateColour("&7Store global items that you want"));
        enderChestLore.add(TranslateColour("&7to access at any time from"));
        enderChestLore.add(TranslateColour("&7anywhere here."));
        enderChestLore.add(" ");
        enderChestLore.add(TranslateColour("&eClick to view!"));
        enderChestMeta.setLore(enderChestLore);
        enderChestItem.setItemMeta(enderChestMeta);

        ItemMeta effectsMeta = effectsItem.getItemMeta();
        effectsMeta.setDisplayName(TranslateColour("&aActive Effects"));
        List<String> effectsLore = new ArrayList<>();

        effectsLore.add(TranslateColour("&7View and manage all of your"));
        effectsLore.add(TranslateColour("&7active potion effects."));
        effectsLore.add(" ");
        effectsLore.add(TranslateColour("&7Drink Potions or splash them"));
        effectsLore.add(TranslateColour("&7on the ground to buff yourself!"));
        effectsLore.add(" ");
        effectsLore.add(TranslateColour("&eClick to view!"));
        effectsMeta.setLore(effectsLore);
        effectsItem.setItemMeta(effectsMeta);

        ItemMeta craftingMeta = craftingItem.getItemMeta();
        craftingMeta.setDisplayName(TranslateColour("&aCrafting Table"));
        List<String> craftingLore = new ArrayList<>();

        craftingLore.add(TranslateColour("&7Opens the crafting grid."));
        craftingLore.add(" ");
        craftingLore.add(TranslateColour("&eClick to open!"));

        craftingMeta.setLore(craftingLore);
        craftingItem.setItemMeta(craftingMeta);

        ItemMeta travelMeta = travelItem.getItemMeta();
        travelMeta.setDisplayName(TranslateColour("&bFast Travel"));
        List<String> travelLore = new ArrayList<>();

        travelLore.add(TranslateColour("&7Teleport to islands you've"));
        travelLore.add(TranslateColour("&7already visited."));
        travelLore.add(" ");
        travelLore.add(TranslateColour("&eClick to pick location!"));
        travelMeta.setLore(travelLore);
        travelItem.setItemMeta(travelMeta);

        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName(TranslateColour("&cClose"));
        closeItem.setItemMeta(closeMeta);

        ItemMeta settingsMeta = settingsItem.getItemMeta();
        settingsMeta.setDisplayName(TranslateColour("&aSettings"));
        List<String> settingsLore = new ArrayList<>();

        settingsLore.add(TranslateColour("&7View and edit your SkyBlock"));
        settingsLore.add(TranslateColour("&7settings."));
        settingsLore.add(" ");
        settingsLore.add(TranslateColour("&eClick to view!"));
        settingsMeta.setLore(settingsLore);
        settingsItem.setItemMeta(settingsMeta);

        ItemMeta potionBagMeta = potionBagItem.getItemMeta();
        potionBagMeta.setDisplayName(TranslateColour("&aPotion Bag"));
        List<String> potionBagLore = new ArrayList<>();

        potionBagLore.add(TranslateColour("&7A handy bag for holding your"));
        potionBagLore.add(TranslateColour("&7Potions in."));
        potionBagLore.add(" ");
        potionBagLore.add(TranslateColour("&eClick to open!"));
        potionBagMeta.setLore(potionBagLore);
        potionBagItem.setItemMeta(potionBagMeta);

        ItemMeta talismanBagMeta = talismanBagItem.getItemMeta();
        talismanBagMeta.setDisplayName(TranslateColour("&aAccessory Bag"));
        List<String> talismanBagLore = new ArrayList<>();

        talismanBagLore.add(TranslateColour("&7A special bag which can"));
        talismanBagLore.add(TranslateColour("&7hold Talismans, Rings,"));
        talismanBagLore.add(TranslateColour("&7Artifacts and Orbs within"));
        talismanBagLore.add(TranslateColour("&7it. All will still work"));
        talismanBagLore.add(TranslateColour("&7while in this bag!"));
        talismanBagLore.add(" ");
        talismanBagLore.add(TranslateColour("&eClick to open!"));
        talismanBagMeta.setLore(talismanBagLore);
        talismanBagItem.setItemMeta(talismanBagMeta);

        for(int i = 0; i < inv.getSize(); i++){
            inv.setItem(i, fillerItem);
        }

        inv.setItem(13, statsItem);
        inv.setItem(19, skillsItem);
        inv.setItem(20, collectionsItem);
        inv.setItem(21, recipesItem);
        inv.setItem(22, petsItem);
        inv.setItem(23, tradesItem);
        inv.setItem(24, questsItem);
        inv.setItem(25, eventsItem);
        inv.setItem(26, enderChestItem);
        inv.setItem(29, effectsItem);
        inv.setItem(33, craftingItem);
        inv.setItem(45, potionBagItem);
        inv.setItem(48, travelItem);
        inv.setItem(49, closeItem);
        inv.setItem(50, settingsItem);
        inv.setItem(53, talismanBagItem);

        return inv;
    }

}
