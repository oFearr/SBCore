package me.ofearr.sbcore.CustomMobs.Mobs;

import me.ofearr.sbcore.CustomMobs.CustomMob;
import me.ofearr.sbcore.CustomMobs.CustomMobManager;
import me.ofearr.sbcore.CustomMobs.Goals.GoalTargetNearestEntity;
import me.ofearr.sbcore.CustomMobs.Goals.GoalWander;
import me.ofearr.sbcore.CustomMobs.Traits.GoblinTrait;
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

public class Goblin implements CustomMob {

    @Override
    public NPCRegistry registry() {
        return CustomMobManager.getNPCRegistry();
    }

    @Override
    public String mobID() {
        return "goblin";
    }

    public void spawn(Location location){
       NPC npc = registry().createNPC(EntityType.PLAYER, UUID.randomUUID(), 999, "");

       Set<EntityType> set = new HashSet<>();
       set.add(EntityType.PLAYER);

        GoblinTrait iCustomEntity = new GoblinTrait();
        SkinTrait skinTrait = npc.getOrAddTrait(SkinTrait.class);

        skinTrait.setSkinPersistent("", "xmx2Q+NlVky7Cfg6MqH4oDxtYCyTC4cqwIGTgbrirKFkNKxnxRwE7IbyYb2gw8VTdhdzdjdYYU3YxTnpcz0j1BR0YX3IweZHJvZcsSHnAETuVu/aSHytXafz4GwD3O1CxywlhfXYHKS3oS4ejAU8H9LsXje+yyIhAT1gEb/T81ySi/TZWBgBWWeTBRrSV4WdXpDmOnuPssscdgvDVCmu/xcDznuoNYo6umWQO9zWvI7xpERt8rJgOeOip+yOtRjleLa4/IciRkYb83Bd9ILspmHD0BiMaW3iMzN01Yd1PuksQK2H5MaKIQlwBaR2CcNO/f0woCZV6et6uSHZFWQrERXtj43sgzqclmwA50E3a1MLqfcA+3RBe1tw1fwysUDvHz7OBlVEvvaZKOyuWsq55deJpi+N8Ef24Flu5oZzqeWDLDHUBUInz4n11ireRKy7YVaH1HDjb/MYiCs/wxpr6O5WBcyIB4dKBGxneeHLlMRgxanR0ckZXBGTIWGF0MpdDirTlXevKz7Yu/C/d/vzLHL82BnPsdxwvp2dRZwO2B9fZIJNJvvqcb6QgLerwLtmMrNeL9Ec2BXLSorTwtNRRiCKcW/LXEVeR+kFjrGhT7n4De0L/UBQeDPOnHAPPz6NDJiMT+aLDLaYeuIxny5I9qHfh9wloT4sqviL+vx4/jc=",
                "ewogICJ0aW1lc3RhbXAiIDogMTYxMDg0MTI1OTY5NywKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mYjQ2ZDU2ZTZhYjM2YzNmNDA3Y2IxZjRmODkyN2U0YjA0ZDU5YTgyZTJhZTgyMGRiZGUxODI0ODYxZWQzN2JhIgogICAgfQogIH0KfQ==");

        String entityName = "Goblin";

       iCustomEntity.setEntityName(entityName);
       iCustomEntity.setEntityLevel(45);
       iCustomEntity.setMaxHealth(800);
       iCustomEntity.setHealth(800);
       iCustomEntity.setDefense(850);
       iCustomEntity.setStrength(10);
       iCustomEntity.setWeaponDamage(300);

       npc.addTrait(iCustomEntity);

       npc.getDefaultGoalController().addGoal(new GoalTargetNearestEntity(npc, location, set, true, 8), 1);
       npc.getDefaultGoalController().addGoal(new GoalWander(npc, 2, 1, null, null, null), 2);

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
