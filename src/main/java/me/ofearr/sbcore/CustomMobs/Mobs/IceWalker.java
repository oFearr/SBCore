package me.ofearr.sbcore.CustomMobs.Mobs;

import me.ofearr.sbcore.CustomMobs.CustomMob;
import me.ofearr.sbcore.CustomMobs.CustomMobManager;
import me.ofearr.sbcore.CustomMobs.Goals.GoalTargetNearestEntity;
import me.ofearr.sbcore.CustomMobs.Goals.GoalWander;
import me.ofearr.sbcore.CustomMobs.Traits.GoblinTrait;
import me.ofearr.sbcore.CustomMobs.Traits.IceWalkerTrait;
import me.ofearr.sbcore.Utils.NBTEditor;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class IceWalker implements CustomMob {

    @Override
    public NPCRegistry registry() {
        return CustomMobManager.getNPCRegistry();
    }

    @Override
    public String mobID() {
        return "ice_walker";
    }

    public void spawn(Location location){
       NPC npc = registry().createNPC(EntityType.PLAYER, UUID.randomUUID(), 999, "");

       Set<EntityType> set = new HashSet<>();
       set.add(EntityType.PLAYER);

      IceWalkerTrait iCustomEntity = new IceWalkerTrait();
       SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);

       skinTrait.setSkinPersistent("", "qstEcsfvDEOyB+aIXjEvbnWxfSdHYK5q6VQMR5GtolxHPhIQnLDwoi8UlLrQXZsbhGbiDuTIfDS4vIzCFqIJEmoFwJ+29RihYX6e8/G4NgLKncSKjfCgxDjRyoyajtlDvaxAih15kYMVCrLNAHcJGbz5Wm8jxhcuuyVhfxgTlyuNWeB3URCZiDiEb+nh9KIPKRCfoxATflBNzcIe2x4zVdXaeLJdmECipCtjaoe8SvhtBa1sOgDmBTvUCl4H/OnYOxpSa1Nrt+IApglNXB5BmfoU6ym74AliLTIJ4JnfohnHYYS6rrabakJfu20YvQ1mRQ3sK2TvHpBxNJUTk51Abl4HPK+fIcOy74BFY4gItz3vO1K9xPs6WQ54O94iciKERZCd3K2rtgLuSWD6PT0ziUosSIy/Ln7AnNaYcwQZogRm5FmnSzKwDGeeprXrloU9fN/pBQK1z2kvFAleME4uPPi8XoX0MpIRywvcBFXYqjJf2EB/P56Ogt2HRlJTwDF5UuUbgy1ytKcrDBX5IcZ+qeTSXUxq8W0yTxZOWHXmsYjrJJBvqp8VbKENTDDjv8AaXBNAt0Jk+9g23LlvsIlRwlnMZAPae2vtXdIrjElX6E9T1FdbjOftNyjWG9qSLmYtPhw/pvBgk8o5ejSRRQbddppVlrTDVEOy5h8L5fMOVI4=",
               "ewogICJ0aW1lc3RhbXAiIDogMTYzNDgyNjkxMDY5NywKICAicHJvZmlsZUlkIiA6ICIxN2Q0ODA1ZDRmMTA0YTA5OWRiYzJmNzYzMDNjYmRkZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJnaWZ0bWV0b25uZXMiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGIzZWJiODdjNzk3ODg1MDc2ODMxNTliMGQxMjBhNDU5NWNjYTdlNDkzMjI5M2YwM2NlYmM2M2M0YzIzNGYzZCIKICAgIH0KICB9Cn0=");


       String entityName = "Ice Walker";

       iCustomEntity.setEntityName(entityName);
       iCustomEntity.setEntityLevel(45);
       iCustomEntity.setMaxHealth(888);
       iCustomEntity.setHealth(888);
       iCustomEntity.setDefense(5000);
       iCustomEntity.setStrength(50);
       iCustomEntity.setWeaponDamage(10);

       npc.addTrait(iCustomEntity);

       npc.getDefaultGoalController().addGoal(new GoalTargetNearestEntity(npc, location, set, true, 8), 1);
       npc.getDefaultGoalController().addGoal(new GoalWander(npc, 2, 1, null, null, null), 2);

       ItemStack helmet = new ItemStack(Material.PACKED_ICE);
       ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
       ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
       ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

       LeatherArmorMeta colourMeta = (LeatherArmorMeta) chestplate.getItemMeta();
       colourMeta.setColor(Color.fromRGB(0, 255, 234));

       chestplate.setItemMeta(colourMeta);
       leggings.setItemMeta(colourMeta);
       boots.setItemMeta(colourMeta);

        Equipment equipmentTrait = npc.getOrAddTrait(Equipment.class);
        equipmentTrait.set(Equipment.EquipmentSlot.HAND, new ItemStack(Material.STONE_SWORD));
        equipmentTrait.set(Equipment.EquipmentSlot.HELMET, helmet);
        equipmentTrait.set(Equipment.EquipmentSlot.CHESTPLATE, chestplate);
        equipmentTrait.set(Equipment.EquipmentSlot.LEGGINGS, leggings);
        equipmentTrait.set(Equipment.EquipmentSlot.BOOTS, boots);

       npc.spawn(location);

       if(npc.isSpawned()){
           npc.getEntity().setMetadata(mobID(), new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SBCore"), mobID()));

           iCustomEntity.applyEntityName();
       }



    }

    @Override
    public void setRegistered() {
        CustomMobManager.putCustomMobToMap(mobID(), this);
    }
}
