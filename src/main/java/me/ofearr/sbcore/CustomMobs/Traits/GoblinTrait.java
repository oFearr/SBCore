package me.ofearr.sbcore.CustomMobs.Traits;

import me.ofearr.customitems.Items.Categories.ItemType;
import me.ofearr.customitems.Items.ItemManager;
import me.ofearr.sbcore.Dwarven.Commissions.DwarvenCommissionsHandler;
import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StatUtils;
import me.ofearr.sbcore.Utils.StringUtils;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.List;

public class GoblinTrait extends Trait {

    public GoblinTrait() {
        super("goblintrait");
    }

    private static SBCore plugin = SBCore.plugin;

    @Persist("maxHealth")
    private double maxHealth = 20.0;
    @Persist("health")
    private double health = 20.0;
    @Persist("minHealth")
    private double minHealth = 0.01;
    @Persist("healthRegen")
    private double healthRegen = health / 100;
    @Persist("strength")
    private int strength = 0;
    @Persist("defense")
    private int defense = 0;
    @Persist("ferocity")
    private int ferocity = 0;
    @Persist("weaponDamage")
    private int weaponDamage = 0;
    @Persist("isKnockbackImmune")
    private boolean isKnockbackImmune = false;

    @Persist("entityName")
    private String entityName = "";
    @Persist("entityLevel")
    private int entityLevel = 1;

    public void setMaxHealth(double maxHealth){
        this.maxHealth = maxHealth;
    }

    public void setHealth(double health){
        if(health > maxHealth) health = maxHealth;

        this.health = health;
    }
    public void setMinHealth(double minHealth){
        this.minHealth = minHealth;
    }

    public void setHealthRegen(double healthRegen){
        this.healthRegen = healthRegen;
    }

    public double getMaxHealth(){
        return maxHealth;
    }

    public double getHealth(){
        return health;
    }

    public double getHealthRegen(){
        return healthRegen;
    }

    public int getStrength(){
        return strength;
    }

    public void setStrength(int strength){
        this.strength = strength;
    }

    public int getDefense(){
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getFerocity() {
        return ferocity;
    }

    public void setFerocity(int ferocity) {
        this.ferocity = ferocity;
    }

    public int getWeaponDamage() {
        return weaponDamage;
    }

    public void setWeaponDamage(int weaponDamage) {
        this.weaponDamage = weaponDamage;
    }

    public boolean isKnockbackImmune() {
        return isKnockbackImmune;
    }

    public void setKnockbackImmune(boolean knockbackImmune) {
        isKnockbackImmune = knockbackImmune;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getEntityLevel() {
        return entityLevel;
    }

    public void setEntityLevel(int entityLevel) {
        this.entityLevel = entityLevel;
    }

    public void applyDamage(double damage){
        health -= damage;

        if(health <= minHealth){
            if(this.getNPC().isSpawned()){
                this.getNPC().destroy();

            }
        }
    }

    public void applyDamage(Player player, double damage){
        health -= damage;

        if(health <= minHealth){
            if(this.getNPC().isSpawned()){
                this.getNPC().destroy();

                new DwarvenCommissionsHandler().handleMobKilled(player, "goblin");

            }
        }
    }

    public void applyEntityName(){
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.applyPattern("#");

        if(this.getNPC().isSpawned()){
            this.getNPC().setName(StringUtils.translate("&8[&7Lv" + entityLevel + "&8] &c" + entityName + " &a" + decimalFormat.format(health) + "&7/&a" + decimalFormat.format(maxHealth)));
        }
    }

    @Persist("tickCounter")
    private int tickCounter = 1;
    @Override
    public void run() {
        if(tickCounter >= 20){
            if(health < maxHealth){

                if(health + healthRegen < maxHealth) {
                    health += healthRegen;
                } else {
                    health = maxHealth;
                }

                applyEntityName();

                tickCounter = 0;
            }
        }

        if(npc.getEntity() != null){
            Location loc = npc.getEntity().getLocation();
            loc.setPitch(90);
            npc.getEntity().teleport(loc);
        }

        tickCounter++;
    }

    private String criticalHitEffect(String s){
        char[] chars = new char[]{'f', 'e', '6', 'c', 'c'};
        int index = 0;
        String returnValue = "";
        for (char c : s.toCharArray()){
            returnValue += "&" + chars[index] + c;
            index++;
            if (index == chars.length){
                index = 0;
            }
        }
        return ChatColor.translateAlternateColorCodes('&', returnValue);
    }

    private void spawnDamageHologram(long damage, Location loc, boolean isCrit){
        World world = loc.getWorld();

        ArmorStand damageDisplay = world.spawn(loc, ArmorStand.class);

        damageDisplay.setSmall(true);
        damageDisplay.setCustomNameVisible(true);
        damageDisplay.setBasePlate(false);
        damageDisplay.setVisible(false);
        damageDisplay.setGravity(false);
        damageDisplay.setMarker(true);
        damageDisplay.setMetadata("DamageDisplay", new FixedMetadataValue(plugin, "DamageDisplay"));

        if(isCrit){
            damageDisplay.setCustomName(StringUtils.translate("&f✧" + criticalHitEffect(String.valueOf(damage)) + "&f✧"));
        }else{
            damageDisplay.setCustomName(StringUtils.translate("&7" + damage));
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                damageDisplay.remove();

            }
        }.runTaskLater(plugin, 30L);

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamage(NPCDamageByEntityEvent e){
        if(e.getNPC() == this.getNPC() && this.getNPC().isSpawned()){

            double rawDamage = e.getDamage();

            e.setDamage(0);

            if(!isKnockbackImmune){
                Location location = npc.getEntity().getLocation();
                npc.getEntity().setVelocity(location.getDirection().multiply(-0.5).setY(0.1));
            }


            double EHP = maxHealth * (1 + (defense / 100));

            double damage;

            Player arrowShooter = null;
            boolean isArrow = false;

            if(e.getDamager() instanceof Arrow){
                Arrow arrow = (Arrow) e.getDamager();

                if(arrow.getShooter() instanceof Player){
                    arrowShooter = (Player) arrow.getShooter();

                    isArrow = true;
                }
            }

            if(e.getDamager() instanceof Player || isArrow){
                Player player;

                if(isArrow){
                    player = arrowShooter;
                } else {
                    player = (Player) e.getDamager();
                }

                List<Object> damageInfo = StatUtils.calculateDamage(player);

                boolean isCrit = false;
                if(damageInfo.size() >= 2){
                    rawDamage = (double) damageInfo.get(0);
                    isCrit = (boolean) damageInfo.get(1);
                }

                damage = (rawDamage / EHP * 100) * (maxHealth / 100);

                if(isCrit){
                    spawnDamageHologram((long) damage, npc.getEntity().getLocation().add(0, 2, 0), true);

                } else {
                    spawnDamageHologram((long) damage, npc.getEntity().getLocation().add(0, 2, 0), false);
                }

                applyDamage(player, damage);

            } else {
                damage = (rawDamage / (EHP * 100)) * (maxHealth / 100);

                spawnDamageHologram((long) damage, npc.getEntity().getLocation().add(0, 2, 0), false);

                applyDamage(damage);
            }

            applyEntityName();

        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageByNPC(EntityDamageByEntityEvent e){
        if(npc != null && npc.isSpawned()){
            if(e.getDamager() != npc.getEntity()) return;

            double finalDamage = (5 + weaponDamage + (Math.round(strength / 5)) * (1 + strength / 100));

            e.setDamage(finalDamage);
        }


    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteractWithNPC(NPCClickEvent e){
        if(e.getNPC() == this.getNPC()) e.setCancelled(true);
    }
}
