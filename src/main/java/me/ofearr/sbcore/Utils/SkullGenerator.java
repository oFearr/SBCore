package me.ofearr.sbcore.Utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;

public class SkullGenerator {

    public static ItemStack getHeadFromString(String search){
        switch (search){
            case ("Void_Conqueror"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODMxNTJiMzhkYzE0MjU4OGQxNGZkZDM4YWFhMGI1NGU2MTM4NjBmN2QxNTM5NTM1YjMyYzAxZWIyMjBmZTY3YiJ9fX0=");
            case ("Enderman_Cortex"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTllMDFiM2EzNWFlM2JlNDQ4M2I2ZGY2OWI3MDcwYmQ2ZGM3NWIzOTlkN2UyZWJiYzdiODg0MDMzMmY3YjNhMCJ9fX0=");
            case ("Lesser_Soulflow_Engine"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2QwNDVjYTNlOGEyOWUxOTkzMTY1NjM2MmUxMjQ3NzYzM2E2ODljNDgwMWQ4ZTIxZjdkYTBmODBjYzU5ZTU2YiJ9fX0=");
            case ("Soulflow_Battery"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmFmZWVmYTI5YmZkYjMyMDIyMzhhMmM0YjhkY2I3NTA1MDZhMzBkMTIyNjAwZjg4NzI1ZTVhYTI0NzMzNTQ2MCJ9fX0=");
            case ("Soulflow_Pile"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDY4YzFmYTRhMjFiYTRmMjdkY2IzMGFiM2JmYWIwZGMyMmExZTRmY2Y5ZWQxMmMxZDdhNzBmZWIwN2Y4ZDdhZSJ9fX0=");
            case ("Endersnake_Rune"):
            case ("End_Rune"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzNhOWFjYmI3ZDNkNDliMWQ1NGQyNjExMTEwNGQwZGE1N2Q4YjRhYjM3ODg1YjRiYmQyNDBhYzcxMDc0Y2FkMiJ9fX0=");
            case ("Sinful_Dice"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFhMmMwODg2MzdmZWU5YWUzYTM2ZGQ0OTZlODc2ZTY1N2Y1MDlkZTU1OTcyZGQxN2MxODc2N2VhZTFmM2U5In19fQ==");
            case ("Pocket_Espresso"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjY2MDcwY2UwM2E1NDVlZTRkMjYzYmNmMjdmMzYzMzhkMjQ5ZDdjYjdhMjM3NmY5MmMxNjczYWUxMzRlMDRiNiJ9fX0=");
            case ("Soulflow"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmM4NWUyZmRmOWIxYjAyMGFjMjgyN2QxMWFlMDBkOTBmODFjNWM2YmQzNjFjYmQxYzhiOGU5MDg3NzU3ZTRiMCJ9fX0=");
            case ("Final_Destination_Helmet"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWFkMDhiMGU1NmEyYTcyNGU2MWEyZGJiMTc2MzA1ZTUyMDE1NTgzZTU4YTZhOTA2YTlkYTFkNzlhM2NhNDg5MyJ9fX0=");
            case ("End_Artifact_Upgrader"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI1OTIzMWE5NDY5ODdlYTUzMTQxNzg5YTA5NDk2ZjA5OGQ2ZWNhYzQxMmEwMWUwYTI0YzkwNmE5OWZkYmQ5YSJ9fX0=");
            case ("Blood_Chalice"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzA1MmU4NThkNjI3YzBmNDk1YmQyMmE2MjQzZjk5ZWJmYmQyZGRjMjRjNzBjYTE3MjUwN2NhN2Y4MGNmZGZhNCJ9fX0=");
            case ("Voidling_Minion"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWY4ODM2MmM2Yzk5NTFmN2RkMzExOWY5OWU1OGE3YzJhNzczNzA0NDhmNjFiMmY1MzAwOGIyMWU1ZjY1YWU5NyJ9fX0=");
            case ("Judgement_Core"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmYzZGRkN2Y4MTA4OWM4NWIyNmVkNTk3Njc1NTE5ZjAzYTFkY2Q2ZDE3MTNlMGNmYzY2YWZiODc0M2NiZTAifX19");
            case ("Enchant_Rune"):
                return getSkull("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTlmZmFjZWM2ZWU1YTIzZDljYjI0YTJmZTlkYzE1YjI0NDg4ZjVmNzEwMDY5MjQ1NjBiZjEyMTQ4NDIxYWU2ZCJ9fX0=");
            case ("Transmission_Tuner"):
                return getSkull("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTI2MjYxNzhiODRkYmQzOTRjZjI0MDk0NjcyMTAyODJlNTIxMDMyMTc2ZGIyMWIyODFmZGM4YWU5Y2JlMmIyZiJ9fX0=");
            case ("Etherwarp_Merger"):
                return getSkull("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2U1MzE0ZjQ5MTk2OTFjY2JmODA3NzQzZGFlNDdhZTQ1YWMyZTNmZjA4Zjc5ZWVjZGQ0NTJmZTYwMmVmZjdmNiJ9fX0=");
            case ("Etherwarp_Conduit"):
                return getSkull("e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2E3MGUwMjA2ZjYxNjYwNDg0NDFkZmUwOGUwNTNhNjAxN2Q5MTRmMzVjNmIxZmIwZjU1OGM1MDU3NGY5NzBkMCJ9fX0=");
            case ("Summoning_Eye"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGFhOGZjOGRlNjQxN2I0OGQ0OGM4MGI0NDNjZjUzMjZlM2Q5ZGE0ZGJlOWIyNWZjZDQ5NTQ5ZDk2MTY4ZmMwIn19fQ==");
            case ("Slayer_Eye_Summons"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDE2Yzk5MzQ0ZTM3ODNkYzVmOWM5ZGNkMzEyNWNiODhiN2RjNDliN2Q3YTg5ZTJmZTAyYzA4Y2IyMjZiMTVmMCJ9fX0=");
            case ("Globe"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzljODg4MWU0MjkxNWE5ZDI5YmI2MWExNmZiMjZkMDU5OTEzMjA0ZDI2NWRmNWI0MzliM2Q3OTJhY2Q1NiJ9fX0=");
            case ("Purple_Potion"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGNlZGIyZjRjOTcwMTZjYWU3Yjg5ZTRjNmQ2OTc4ZDIyYWMzNDc2YzgxNWM1YTA5YTY3OTI0NTBkZDkxOGI2YyJ9fX0=");
            case ("Talisman_Sack"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYxYTkxOGMwYzQ5YmE4ZDA1M2U1MjJjYjkxYWJjNzQ2ODkzNjdiNGQ4YWEwNmJmYzFiYTkxNTQ3MzA5ODVmZiJ9fX0=");
            case ("Ruby_Gemstone"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGVjNjVmYzg3YzUwNDQxNWVmZjczMGEyZjRmZTdkMDZkMmYxMTZlYTJhMzEzNDgxZjM2MmQwYTI1ZDY1ZTUwMCJ9fX0=");
            case ("Amber_Gemstone"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDc3NGU1ZWYzZDhiY2RlOWVhMjFjMzRiODQ4MjdkMzQ1MzFlNjhmMTExNTEwZjMzODMwNTVlY2FhNzRiZWJjYyJ9fX0=");
            case ("Sapphire_Gemstone"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU1MmY3OTYwZmYzY2VjMmY1MTlhNjM1MzY0OGM2ZTMzYmM1MWUxMzFjYzgwOTE3Y2YxMzA4MWRlY2JmZjI0ZCJ9fX0=");
            case ("Jade_Gemstone"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjg0MzgwYWQ0ZGMzODhmNTYxMjU0Yzg0MDlmYTcwNGQ0NDc0N2ViNmU1ZmVhY2JhMzQzZTdjMjQzY2ZhYzZhMSJ9fX0=");
            case ("Amethyst_Gemstone"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5ZjhjOTI3NzZkNjQyZDExOWY5ZTkyMzYwYjFlNWI5NzFlNjZlNjE0MjhhM2UxYjMxMWQ4YjYxODVlNiJ9fX0=");
            case ("Topaz_Gemstone"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjE0NjljZTY3ZGZjMjg0MzNhZWE4YTQ1YWI4MTZlNTFiZDM5YmE5ZWI4MTVkMjI1NzllNzc2OThkYTBiZjI5NSJ9fX0=");
            case ("Jasper_Gemstone"):
                return getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRiMzJiMTVkN2YzMjcwNGVkNjI2ZmE1MmQwNmZiMmI0MDcxZDMzNmZkYmZlNjFlNmU0MWM2NjlkNmUzN2Y0NyJ9fX0=");
            default:
                return null;
        }
    }

    public static ItemStack getSkull(Player player){
        ItemStack playerHead = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());

        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();
        meta.setOwner(player.getName());

        playerHead.setItemMeta(meta);

        return playerHead;
    }

    public static ItemStack getSkull(String texture){
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field field;
        try {
            field = meta.getClass().getDeclaredField("profile");
            field.setAccessible(true);
            field.set(meta, profile);
        }catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e){
            e.printStackTrace();
        }

        skull.setItemMeta(meta);

        return skull;
    }
}
