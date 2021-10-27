package me.ofearr.sbcore.Utils;

import org.bukkit.ChatColor;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class StringUtils {

    public static String hideString(String s){
        StringBuilder builder = new StringBuilder();

        for(char c : s.toCharArray()){
            builder.append(ChatColor.COLOR_CHAR).append(c);
        }

        String hidden = builder.toString();

        return hidden;
    }

    private static final NavigableMap<Long, String> suffixes = new TreeMap<>();
    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "B");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");
    }

    public static String formatNumber(long value) {
        if (value == Long.MIN_VALUE) return formatNumber(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatNumber(-value);
        if (value < 1000) return Long.toString(value); //deal with easy case

        Map.Entry<Long, String> e = suffixes.floorEntry(value);
        Long divideBy = e.getKey();
        String suffix = e.getValue();

        long truncated = value / (divideBy / 10);
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public static String translate(String s){
        String translated = ChatColor.translateAlternateColorCodes('&', s);

        return translated;
    }
}
