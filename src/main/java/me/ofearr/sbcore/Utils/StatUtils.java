package me.ofearr.sbcore.Utils;

import me.ofearr.playerstatscore.API.StatAPIManager;
import me.ofearr.skillcore.Skills.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class StatUtils {

    private static StatAPIManager statAPIManager = new StatAPIManager();

    public static List<Object> calculateDamage(Player player){
        List<Object> damageInfo = new ArrayList<>();

        HashMap<String, Integer> playerStats = statAPIManager.getAllPlayerStats(player);

        int weaponDamage = statAPIManager.getPlayerWeaponDamage(player);
        int strength = playerStats.get("Strength");

        int critChance = playerStats.get("Crit Chance");

        int playerCombatLevel = SkillManager.getCombatLevel(player);

        double finalDamage = (5 + weaponDamage + (Math.round(strength / 5)) * (1 + strength / 100));
        double combatSkillBuff = finalDamage * ((playerCombatLevel * 4) / 100);

        finalDamage = finalDamage + combatSkillBuff;

        if(player.getItemInHand() != null && player.getItemInHand().hasItemMeta()){
            ItemStack heldItem = player.getItemInHand();

            if(NBTEditor.contains(heldItem, "ability_scaling")){
                double abilityScaling = NBTEditor.getDouble(heldItem, "ability_scaling");
                int baseAbilityDamage = playerStats.get("Ability Damage");
                int intelligence = playerStats.get("Intelligence");

                double abilityDamage = baseAbilityDamage * (1 + (intelligence / 100) * abilityScaling);

                finalDamage = finalDamage + abilityDamage;
            }

            //TODO: APPLY CUSTOM ENCHANT BUFFS

            if(rollCrit(critChance)){
                int critDamage = playerStats.get("Crit Damage");

                double criticalDamage = finalDamage * (1 + critDamage / 100);

                damageInfo.add(criticalDamage);
                damageInfo.add(true);
            } else {
                damageInfo.add(finalDamage);
                damageInfo.add(false);
            }


        }

        return damageInfo;
    }

    public static boolean rollCrit(int critChance){
        if(ThreadLocalRandom.current().nextDouble() <= (double)  critChance / 100){
            return true;
        }

        return false;
    }
}
