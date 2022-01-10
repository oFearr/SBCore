package me.ofearr.sbcore.Dwarven;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DwarvenMonolith {

    private Monolith type = Monolith.ERODED;

    public Monolith drawType(){
        List<Monolith> monolithTypes = Arrays.asList(Monolith.values());

        Random random = new Random();
        type = monolithTypes.get(random.nextInt(monolithTypes.size()));

        return type;
    }

    public void spawn(Location loc){
        Block block = loc.getBlock();

        block.setType(Material.DRAGON_EGG);

        DwarvenManager.registerMonolithAtLocation(loc, this);
    }

    public void grantReward(Player player){
        Random random = new Random();

    }
}
