package me.ofearr.sbcore.BlockManagement;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Date;

public class BrokenBlock {

    private static BlockPackets blockPackets = new BlockPackets();

    private int time;
    private int oldAnimation;
    private double damage = -1;
    private Block block;
    private Date lastDamage;

    public BrokenBlock(Block block, int time){
        this.block = block;
        this.time = time;
        lastDamage = new Date();
    }

    public void incrementDamage(Player from, double multiplier){
        if(isBroken()) return;

        damage += multiplier;
        int animation = getAnimation();

        if(animation != oldAnimation){
            if(animation < 10){
                sendBreakPacket(animation);
                lastDamage = new Date();
            } else {
                breakBlock(from);
                return;
            }
        }

        oldAnimation = animation;
    }

    public boolean isBroken(){
        return getAnimation() >= 10;
    }

    public void breakBlock(Player breaker){
        destroyBlockObject();
        blockPackets.playBlockSound(block);
        if(breaker == null) return;
        CustomBlockManager.handleBlockBreak(breaker, block);
    }

    public Date getLastDamage(){

        return lastDamage;
    }

    public void destroyBlockObject(){
        sendBreakPacket(-1);
    }

    public int getAnimation(){
        return (int) (damage / time*11) - 1;
    }

    public void sendBreakPacket(int animation){
        blockPackets.sendBreakPacket(animation, block);
    }
}
