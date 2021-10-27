package me.ofearr.sbcore;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import com.keenant.tabbed.Tabbed;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.ofearr.sbcore.Areas.AreaManager;
import me.ofearr.sbcore.BlockManagement.BlockEvents;
import me.ofearr.sbcore.BlockManagement.CustomBlockManager;
import me.ofearr.sbcore.Collections.CollectionsManager;
import me.ofearr.sbcore.Collections.Mining.MiningCollectionsHandler;
import me.ofearr.sbcore.Commands.*;
import me.ofearr.sbcore.CustomConfigs.ShopConfig;
import me.ofearr.sbcore.CustomMobs.CustomMobManager;
import me.ofearr.sbcore.CustomMobs.Traits.IceWalkerTrait;
import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommissionsGUIHandler;
import me.ofearr.sbcore.Dwarven.DwarvenManager;
import me.ofearr.sbcore.Dwarven.Forge.ForgeGUIHandler;
import me.ofearr.sbcore.Dwarven.Forge.ForgeManager;
import me.ofearr.sbcore.Dwarven.HeartOfTheMountain.HotMMenuHandler;
import me.ofearr.sbcore.Events.ActionBarManager;
import me.ofearr.sbcore.Events.DamageCalculationEvent;
import me.ofearr.sbcore.Events.MiscEvents;
import me.ofearr.sbcore.Events.MiscGUIHandlers;
import me.ofearr.sbcore.MapManagement.GameMap;
import me.ofearr.sbcore.MapManagement.MapGeneration;
import me.ofearr.sbcore.PlayerData.PlayerDataHandler;
import me.ofearr.sbcore.Shop.ShopGUIHandler;
import me.ofearr.sbcore.Tablist.TablistHandler;
import me.ofearr.sbcore.Utils.StringUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class SBCore extends JavaPlugin {
    private static final Logger log = Logger.getLogger("Minecraft");

    private static GameMap dwarvenMap;

    public WorldGuardPlugin worldGuardPlugin;
    public static AreaManager areaManager;
    public CustomBlockManager customBlockManager;
    public CollectionsManager collectionsManager;
    public DwarvenManager dwarvenManager;
    public String pluginPrefix = getConfig().getString("prefix");
    private static Economy econ = null;
    private static Tabbed tabbed;

    public static SBCore plugin;

    @Override
    public void onEnable() {
        plugin = this;

        loadConfig();
        new ShopConfig().create();

        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(getWorldGuard() == null){
            System.out.println("Failed to get WorldGuard plugin, disabling!");
            this.getPluginLoader().disablePlugin(this);

        } else {
            worldGuardPlugin = getWorldGuard();
        }


        this.collectionsManager = new CollectionsManager();
        collectionsManager.setRegisteredCollections();

        this.dwarvenManager = new DwarvenManager();
        dwarvenManager.setRegisteredUpgrades();
        dwarvenManager.setRegisteredDwarvenCommissions();
        ForgeManager.setRegisteredForgeItems();

        handleMapInstances(true);
        areaManager = new AreaManager(this, worldGuardPlugin);
        customBlockManager = new CustomBlockManager(this, areaManager);
        tabbed = new Tabbed(this);

        if(Bukkit.getPluginManager().isPluginEnabled("MVdWPlaceholderAPI")){
            setRegisteredPlaceholders();
        }

        //Register Commands
        getCommand("sbmenu").setExecutor(new SBMenuCMD(this));
        getCommand("givecollectionprogress").setExecutor(new GiveCollectionProgressCMD(this));
        getCommand("opentelekinesisapplicationmenu").setExecutor(new OpenTelekinesisApplicationMenuCMD());
        getCommand("opendeepcavernsliftmenu").setExecutor(new OpenDeepCavernsLiftMenuCMD());
        getCommand("setcustomworldspawn").setExecutor(new SetWorldSpawnCMD(this));
        getCommand("addwarp").setExecutor(new AddWarpCMD(this));
        getCommand("warp").setExecutor(new WarpCMD(this));
        getCommand("sbreload").setExecutor(new SBReloadCMD(this));
        getCommand("hotm").setExecutor(new HotMCMD());
        getCommand("opendwarvencommissionsmenu").setExecutor(new OpenDwarvenCommissionsMenuCMD(this));
        getCommand("openforgemenu").setExecutor(new OpenForgeMenuCMD());
        getCommand("openshopmenu").setExecutor(new OpenShopMenuCMD(this));

        //Register Events
        registerEvents();
        registerEntityTraits();
        enableSBMenuItemRegen();
        enableCustomHealthRegen();

        CustomMobManager.setRegisteredCustomMobs();
        enableNightVisionPerk();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        handleMapInstances(false);
    }


    public WorldGuardPlugin getWorldGuard(){
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");

        if(plugin == null || !(plugin instanceof WorldGuardPlugin)){
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    public void enableCustomHealthRegen(){
        new BukkitRunnable() {
            @Override
            public void run() {

                for(Player p : Bukkit.getOnlinePlayers()){
                    if(p.getHealth() < p.getMaxHealth()){
                        double maxHealth = p.getMaxHealth();
                        double healtRegen = maxHealth * 0.025;

                        double currentPlayerHealth = p.getHealth();
                        double newHealth = currentPlayerHealth + healtRegen;

                        if(newHealth > maxHealth){
                            p.setHealth(maxHealth);
                        } else {
                            p.setHealth(newHealth);
                        }

                    }
                }
            }
        }.runTaskTimer(this, 0L, 40L);

    }

    public void handleMapInstances(boolean isEnabling){

        if(isEnabling){
            System.out.println("Creating map instances...");

            File gameMapsFolder = new File(getDataFolder(), "gameMaps");
            if (!gameMapsFolder.exists()){
                gameMapsFolder.mkdirs();
            }

            dwarvenMap = new MapGeneration(gameMapsFolder, "world_dwarven_mines", false);

        } else {
            dwarvenMap.restoreFromSource();

        }

    }

    public void enableSBMenuItemRegen(){
        new BukkitRunnable() {
            @Override
            public void run() {

                for(Player p : Bukkit.getOnlinePlayers()){
                    ItemStack sbMenuItem = new ItemStack(Material.NETHER_STAR);

                    ItemMeta sbMeta = sbMenuItem.getItemMeta();
                    sbMeta.setDisplayName(StringUtils.translate("&aSkyBlock Menu &7(Right Click)"));
                    List<String> sbLore = new ArrayList<>();

                    sbLore.add(StringUtils.translate("&7View all of your SkyBlock"));
                    sbLore.add(StringUtils.translate("&7progress, including your Skills,"));
                    sbLore.add(StringUtils.translate("&7Collections, Recipes, and more!"));

                    sbMeta.setLore(sbLore);
                    sbMenuItem.setItemMeta(sbMeta);

                    if(p.getInventory().getItem(8) != sbMenuItem){
                        p.getInventory().setItem(8, sbMenuItem);
                    }
                }
            }
        }.runTaskTimer(this, 40L, 40L);
    }

    public void loadConfig(){
        saveDefaultConfig();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void registerEntityTraits(){
        if(Bukkit.getPluginManager().isPluginEnabled("Citizens")){
            CitizensAPI.getTraitFactory().registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(IceWalkerTrait.class).withName("icewalkertrait"));
            Bukkit.getPluginManager().registerEvents(new IceWalkerTrait(), this);

        }
    }

    public void enableNightVisionPerk(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){

                    if(player.hasPermission("sbcore.nightvision")){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 65, 1, true, false));
                    }
                }
            }
        }.runTaskTimer(this, 0L, 60L);
    }

    private void setRegisteredPlaceholders(){
        PlaceholderAPI.registerPlaceholder(this, "sb_player_location",
                new PlaceholderReplacer() {
                    @Override
                    public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
                        Player player = e.getPlayer();

                        boolean isOnline = e.isOnline();

                        if(!isOnline){
                            return null;
                        }

                        if(player == null){
                            return null;
                        }

                        return areaManager.getLastKnownPlayerLocation(player);
                    }
                });
    }

    private void registerEvents(){
        Bukkit.getPluginManager().registerEvents(new PlayerDataHandler(), this);
        Bukkit.getPluginManager().registerEvents(new DamageCalculationEvent(this), this);
        Bukkit.getPluginManager().registerEvents(new ActionBarManager(this), this);
        Bukkit.getPluginManager().registerEvents(new MiscEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockEvents(this), this);
        Bukkit.getPluginManager().registerEvents(new HotMMenuHandler(this), this);
        Bukkit.getPluginManager().registerEvents(areaManager, this);
        Bukkit.getPluginManager().registerEvents(new TablistHandler(), this);
        Bukkit.getPluginManager().registerEvents(new DwarvenCommissionsGUIHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ForgeGUIHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new ShopGUIHandler(this), this);
        Bukkit.getPluginManager().registerEvents(new MiscGUIHandlers(this), this);

        Bukkit.getPluginManager().registerEvents(new MiningCollectionsHandler(this), this);
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static Tabbed getTabbed(){
        return tabbed;
    }

}
