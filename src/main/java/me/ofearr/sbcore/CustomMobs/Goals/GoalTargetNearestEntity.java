package me.ofearr.sbcore.CustomMobs.Goals;

import net.citizensnpcs.api.ai.event.CancelReason;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.ai.event.NavigatorCallback;
import net.citizensnpcs.api.ai.tree.BehaviorGoalAdapter;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

public class GoalTargetNearestEntity extends BehaviorGoalAdapter implements Listener {
    private final boolean aggressive;
    private boolean finished;
    private final NPC npc;
    private final double radius;
    private CancelReason reason;
    private Entity target;
    private final Set<EntityType> targets;
    private final Location spawnLocation;

    public GoalTargetNearestEntity(NPC npc, Location spawnLocation, Set<EntityType> targets, boolean aggressive, double radius) {
        this.npc = npc;
        this.targets = targets;
        this.aggressive = aggressive;
        this.radius = radius;
        this.spawnLocation = spawnLocation;
    }

    public void reset() {
        this.npc.getNavigator().cancelNavigation();
        this.target = null;
        this.finished = false;
        this.reason = null;
    }

    public BehaviorStatus run() {
        if (this.finished) {
            return this.reason == null ? BehaviorStatus.SUCCESS : BehaviorStatus.FAILURE;
        } else {
            if(this.npc.isSpawned() && this.target != null){
                if(this.target.getLocation().distance(this.npc.getEntity().getLocation()) >= 15){
                    GoalTargetNearestEntity.this.reason = CancelReason.STUCK;
                    GoalTargetNearestEntity.this.finished = true;

                    this.npc.getNavigator().setTarget(this.spawnLocation);

                    return BehaviorStatus.FAILURE;
                }
            }

            return BehaviorStatus.RUNNING;
        }
    }

    public boolean shouldExecute() {
        if (this.targets.size() != 0 && this.npc.isSpawned()) {
            Collection<Entity> nearby = this.npc.getEntity().getNearbyEntities(this.radius, this.radius, this.radius);

            this.target = null;
            Iterator var2 = nearby.iterator();

            while(var2.hasNext()) {
                Entity entity = (Entity)var2.next();
                if (this.targets.contains(entity.getType())) {
                    this.target = entity;
                    break;
                }
            }

            if (this.target != null && !(target.hasMetadata("NPC")) && !(target.hasMetadata("CustomEntity"))) {
                this.npc.getNavigator().setTarget(this.target, this.aggressive);

                this.npc.getNavigator().getLocalParameters().addSingleUseCallback(new NavigatorCallback() {
                    public void onCompletion(CancelReason cancelReason) {
                        GoalTargetNearestEntity.this.reason = cancelReason;
                        GoalTargetNearestEntity.this.finished = true;
                    }
                });
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Builder builder(NPC npc) {
        return new Builder(npc);
    }

    @EventHandler
    public void onFinish(NavigationCompleteEvent e){
    }

    public static class Builder {
        private boolean aggressive;
        private final NPC npc;
        private Location spawnLocation;
        private double radius = 10.0D;
        private Set<EntityType> targetTypes = EnumSet.noneOf(EntityType.class);

        public Builder(NPC npc) {
            this.npc = npc;
        }

        public Builder aggressive(boolean aggressive) {
            this.aggressive = aggressive;
            return this;
        }

        public GoalTargetNearestEntity build() {
            return new GoalTargetNearestEntity(this.npc, this.spawnLocation, this.targetTypes, this.aggressive, this.radius);
        }

        public Builder radius(double radius) {
            this.radius = radius;
            return this;
        }

        public Builder targets(Set<EntityType> targetTypes) {
            this.targetTypes = targetTypes;
            return this;
        }

        public Builder spawnLocation(Location spawnLocation) {
            this.spawnLocation = spawnLocation;
            return this;
        }
    }
}
