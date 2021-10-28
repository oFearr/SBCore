package me.ofearr.sbcore.PlayerData;

import me.ofearr.sbcore.SBCore;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class PlayerDataManager {

    private SBCore plugin = SBCore.plugin;
    private File cfile;
    private FileConfiguration config;
    private File df = plugin.getDataFolder();

    public void create(Player p) {
        cfile = new File(df, "UserData" + File.separator + p.getUniqueId() + ".yml");
        if (!df.exists()) df.mkdir();
        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
                System.out.println("[SkyBlock] Successfully created userdata file for " + p.getName() + "!");
            } catch(Exception e) {
            }
        }
        config = YamlConfiguration.loadConfiguration(cfile);
    }

    public void create(OfflinePlayer p) {
        cfile = new File(df, "UserData" + File.separator + p.getUniqueId() + ".yml");
        if (!df.exists()) df.mkdir();
        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
                System.out.println("[SkyBlock] Successfully created userdata file for " + p.getName() + "!");
            } catch(Exception e) {
            }
        }
        config = YamlConfiguration.loadConfiguration(cfile);
    }


    public File getfile() {
        return cfile;
    }

    public void load(Player p) {
        try{
            cfile = new File(df, "UserData" + File.separator + p.getUniqueId() + ".yml");
            config = YamlConfiguration.loadConfiguration(cfile);
        } catch (Exception e){
        }

    }

    public void load(UUID playerUUID) {
        try{
            cfile = new File(df, "UserData" + File.separator + playerUUID + ".yml");
            
            config = YamlConfiguration.loadConfiguration(cfile);

        } catch (Exception e){
        }

    }


    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(cfile);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
