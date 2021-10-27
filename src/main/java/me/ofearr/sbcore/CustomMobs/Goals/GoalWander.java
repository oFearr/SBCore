package me.ofearr.sbcore.CustomMobs.Goals;

import ch.ethz.globis.phtree.PhTreeSolid;
import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.sk89q.worldedit.regions.Region;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.astar.pathfinder.MinecraftBlockExaminer;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Random;

public class GoalWander extends BehaviorGoalAdapter implements Listener {
    private int delay;
    private int delayedTicks;
    private final Function<NPC, Location> fallback;
    private boolean forceFinish;
    private final NPC npc;
    private boolean paused;
    private final Supplier<PhTreeSolid<Boolean>> tree;
    private Object worldguardRegion;
    private int xrange;
    private int yrange;
    private static final Location NPC_LOCATION = new Location((World)null, 0.0D, 0.0D, 0.0D);
    private static final Random RANDOM = new Random();

    public GoalWander(NPC npc, int xrange, int yrange, Supplier<PhTreeSolid<Boolean>> tree, Function<NPC, Location> fallback, Object worldguardRegion) {
        this.npc = npc;
        this.worldguardRegion = worldguardRegion;
        this.xrange = xrange;
        this.yrange = yrange;
        this.tree = tree;
        this.fallback = fallback;
    }

    private Location findRandomPosition() {
        Location found = MinecraftBlockExaminer.findRandomValidLocation(this.npc.getEntity().getLocation(NPC_LOCATION), this.xrange, this.yrange, new Function<Block, Boolean>() {
            public Boolean apply(Block block) {
                if ((block.getRelative(BlockFace.UP).isLiquid() || block.getRelative(0, 2, 0).isLiquid()) && GoalWander.this.npc.getNavigator().getDefaultParameters().avoidWater()) {
                    return false;
                } else {
                    if (GoalWander.this.worldguardRegion != null) {
                        try {
                            return false;
                        } catch (Throwable var3) {
                            var3.printStackTrace();
                        }
                    }

                    return true;
                }
            }
        }, RANDOM);
        return found == null && this.fallback != null ? (Location)this.fallback.apply(this.npc) : found;
    }

    @EventHandler
    public void onFinish(NavigationCompleteEvent event) {
        this.forceFinish = true;
    }

    public void pause() {
        this.paused = true;
    }

    public void reset() {
        this.delayedTicks = this.delay;
        this.forceFinish = false;
        HandlerList.unregisterAll(this);
    }

    public BehaviorStatus run() {
        return this.npc.getNavigator().isNavigating() && !this.forceFinish ? BehaviorStatus.RUNNING : BehaviorStatus.SUCCESS;
    }

    public void setDelay(int delay) {
        this.delay = delay;
        this.delayedTicks = delay;
    }

    public void setWorldGuardRegion(Object region) {
        this.worldguardRegion = region;
    }

    public void setXYRange(int xrange, int yrange) {
        this.xrange = xrange;
        this.yrange = yrange;
    }

    public boolean shouldExecute() {
        if (this.npc.isSpawned() && !this.npc.getNavigator().isNavigating() && !this.paused) {
            if (this.delayedTicks-- > 0) {
                return false;
            } else {
                Location dest = this.findRandomPosition();
                if (dest == null) {
                    return false;
                } else {
                    this.npc.getNavigator().setTarget(dest);
                    CitizensAPI.registerEvents(this);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public void unpause() {
        this.paused = false;
    }

    public static GoalWander createWithNPC(NPC npc) {
        return createWithNPCAndRange(npc, 10, 2);
    }

    public static GoalWander createWithNPCAndRange(NPC npc, int xrange, int yrange) {
        return createWithNPCAndRangeAndTree(npc, xrange, yrange, (Supplier)null);
    }

    public static GoalWander createWithNPCAndRangeAndTree(NPC npc, int xrange, int yrange, Supplier<PhTreeSolid<Boolean>> tree) {
        return createWithNPCAndRangeAndTreeAndFallback(npc, xrange, yrange, tree, (Function)null);
    }

    public static GoalWander createWithNPCAndRangeAndTreeAndFallback(NPC npc, int xrange, int yrange, Supplier<PhTreeSolid<Boolean>> tree, Function<NPC, Location> fallback) {
        return new GoalWander(npc, xrange, yrange, tree, fallback, (Object)null);
    }

    public static GoalWander createWithNPCAndRangeAndTreeAndFallbackAndRegion(NPC npc, int xrange, int yrange, Supplier<PhTreeSolid<Boolean>> tree, Function<NPC, Location> fallback, Object worldguardRegion) {
        return new GoalWander(npc, xrange, yrange, tree, fallback, worldguardRegion);
    }
}