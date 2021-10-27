package me.ofearr.sbcore.MapManagement;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;

public class MapGeneration implements GameMap{
    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;

    public MapGeneration(File worldFolder, String worldName, boolean loadOnInit){
        this.sourceWorldFolder = new File(
                worldFolder,
                worldName
        );

        if(loadOnInit) load();
    }

    @Override
    public boolean load() {
        if(isLoaded()) return true;

        this.activeWorldFolder = new File(
                Bukkit.getWorldContainer().getParentFile(), sourceWorldFolder.getName());

        try {
            FileUtil.copy(sourceWorldFolder, activeWorldFolder);
        } catch (Exception e){
            return false;
        }

        this.bukkitWorld = Bukkit.createWorld(
                new WorldCreator(activeWorldFolder.getName())
        );

        if(bukkitWorld != null) this.bukkitWorld.setAutoSave(false);
        return isLoaded();
    }

    @Override
    public void unload() {
        if(bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
        if(activeWorldFolder != null) FileUtil.delete(activeWorldFolder);

        bukkitWorld = null;
        activeWorldFolder = null;
    }

    @Override
    public boolean restoreFromSource() {
        unload();

        return load();
    }

    @Override
    public boolean isLoaded() {
        return false;
    }

    @Override
    public World getWorld() {
        return bukkitWorld;
    }
}
