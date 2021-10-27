package me.ofearr.sbcore.Utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;

public class EntityUtils {

    public void applyEntityStats(LivingEntity entity, String displayName, String entityID, double maxHealth, int level, boolean scaleHealthWithLevel){
        Location loc = entity.getLocation();
        World world = entity.getWorld();

        if(scaleHealthWithLevel){
            double bonusHealth = maxHealth * (level / 100);

            maxHealth = maxHealth + bonusHealth;
        }

        ArmorStand entityDisplay = world.spawn(loc, ArmorStand.class);

        entityDisplay.setMarker(true);
        entityDisplay.setVisible(false);
        entityDisplay.setSmall(true);
        entityDisplay.setBasePlate(false);
        entityDisplay.setGravity(false);
        entityDisplay.setCustomNameVisible(true);

        entityDisplay.setCustomName(StringUtils.translate("&8[&7Lv" + level + "&8] " + displayName + "&a" + maxHealth + "&7/&a" + maxHealth));

        //Disable item pickup
        entity.setCanPickupItems(false);
        entity.setMaxHealth(maxHealth);
        entity.setHealth(maxHealth);
        entity.setPassenger(entityDisplay);

        NBTEditor.set(entity, entityID, "entity_id");

    }
}
