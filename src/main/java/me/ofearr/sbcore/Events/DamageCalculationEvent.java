package me.ofearr.sbcore.Events;

import me.ofearr.sbcore.SBCore;
import me.ofearr.sbcore.Utils.StatUtils;
import me.ofearr.sbcore.Utils.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class DamageCalculationEvent implements Listener {

    private SBCore plugin;

    public DamageCalculationEvent(SBCore instance){
        this.plugin = instance;
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

    public double getEntityHeightModifier(Entity entity){
        EntityType entityType = entity.getType();

        if(entityType.equals(EntityType.ENDERMAN)){
            return 2.5;
        } else if(entityType.equals(EntityType.CAVE_SPIDER) || entityType.equals(EntityType.SILVERFISH) || entityType.equals(EntityType.SPIDER)
        || entityType.equals(EntityType.BAT) || entityType.equals(EntityType.CHICKEN) || entityType.equals(EntityType.SHEEP) || entityType.equals(EntityType.PIG)
        || entityType.equals(EntityType.WOLF) || entityType.equals(EntityType.GUARDIAN)){
            return 1;
        } else {
            return 1.5;
        }
    }

    public double applySharpnessModifier(Player player, double damage){
        ItemStack heldItem = player.getInventory().getItemInHand();

        double modifier;
        if(heldItem != null){
            System.out.println("Held item not null");
            if(heldItem.getItemMeta() == null){
                System.out.println("Item meta null");
                return damage;
            }
             if(heldItem.getItemMeta().hasEnchants()){
                 System.out.println("Has enchants");

                 for(Enchantment enchantment : heldItem.getItemMeta().getEnchants().keySet()){
                     if(enchantment.getName().equalsIgnoreCase("damage_all")){
                         System.out.println("Has sharpness enchantment!");
                         int sharpnessLevel = heldItem.getItemMeta().getEnchants().get(enchantment);
                         System.out.println(sharpnessLevel);

                         modifier = (sharpnessLevel * 5) / 100;
                         double damageIncrease = damage * modifier;

                         damage = damage + damageIncrease;

                         break;
                     }
                 }
             }

        }

        return damage;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void calculateDamageFromStats(EntityDamageByEntityEvent e){

        Player arrowShooter = null;
        boolean isArrow = false;

        if(e.getDamager() instanceof Arrow){
            Arrow arrow = (Arrow) e.getDamager();

            if(arrow.getShooter() instanceof Player){
                arrowShooter = (Player) arrow.getShooter();

                isArrow = true;
            }
        }

        if(e.getDamager() instanceof Player || arrowShooter != null){
            Player player;

            if(arrowShooter != null){
                player = arrowShooter;
            } else {
                player = (Player) e.getDamager();
            }

            if(e.getEntity() instanceof ArmorStand) return;
            if(e.getEntity() instanceof ItemFrame) return;
            if(!(e.getEntity() instanceof LivingEntity)) return;
            if(e.getEntity() instanceof Player){
                e.setCancelled(true);
                return;
            }

            List<Object> damageInfo = StatUtils.calculateDamage(player);

            double damage = (double) damageInfo.get(0);
            boolean isCrit = (boolean) damageInfo.get(1);

            e.setDamage(damage);

            if(isCrit){
                spawnDamageHologram((long) damage, e.getEntity().getLocation().add(0, ((LivingEntity) e.getEntity()).getEyeHeight(), 0), true);
            } else {
                spawnDamageHologram((long) damage, e.getEntity().getLocation().add(0, ((LivingEntity) e.getEntity()).getEyeHeight(), 0), false);
            }

     }

    }

}
