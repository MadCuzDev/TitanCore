package me.madcuzdev.titancore;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PriceHandler {
    private static DecimalFormat formatter = new DecimalFormat("###,###,###,###,###,###,###,###,###.##");

    private HashMap<String, Long> rankPrices;
    {
        rankPrices = new HashMap<>();
        List<String> alphRanks = Arrays.asList("default", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
        for (int i = 0; alphRanks.size() > i; i++) {
            rankPrices.put(alphRanks.get(i), (long) (995 + 5 * Math.pow(i, 6)));
        }
    }

    private HashMap<String, Long> sellPrices;
    {
        sellPrices = new HashMap<>();
        List<String> alphRanks = Arrays.asList("default", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
        for (int i = 0; alphRanks.size() > i; i++) {
            sellPrices.put(alphRanks.get(i), (long)Math.pow(i + 1, 3));
        }
    }

    public HashMap<String, Long> getSellPrices() {
        return sellPrices;
    }

    public long getPrestigePrice(Integer prestige) {
        long num1 = prestige+1;
        long num2 = (long)Math.pow(num1, 2);
        long num3 = num1*1000;
        return num3 * 30000 * num2;
    }

    public HashMap<String, Long> getRankPrices() {
        return rankPrices;
    }

    public static String formatNumber(Double num) {
        return formatter.format(num);
    }

}
