package me.ofearr.sbcore.CustomConfigs;

import me.ofearr.sbcore.SBCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ShopConfig {

    private SBCore plugin = SBCore.plugin;
    private File cfile;
    private FileConfiguration config;
    private File df = plugin.getDataFolder();

    public void create() {
        cfile = new File(df, File.separator + "shops.yml");
        if (!df.exists()) df.mkdir();
        if (!cfile.exists()) {
            try {
                cfile.createNewFile();
                System.out.println("[SkyBlock] Successfully created shops file!");
            } catch(Exception e) {
            }
        }
        config = YamlConfiguration.loadConfiguration(cfile);
    }


    public File getfile() {
        return cfile;
    }

    public void load() {
        try{
            cfile = new File(df, File.separator + "shops.yml");
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
