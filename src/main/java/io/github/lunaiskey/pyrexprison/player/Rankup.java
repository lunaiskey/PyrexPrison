package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Rankup {

    private static final List<Long> set = new ArrayList<>();
    private static final int maxRankup = 2700;

    static {
        set.add(5000L);
        set.add(10000L);
        set.add(25000L);
        set.add(50000L);
        set.add(75000L);

        set.add(100000L);
        set.add(150000L);
        set.add(200000L);
        set.add(250000L);
        set.add(300000L);

        set.add(400000L);
        set.add(500000L);
        set.add(750000L);
        set.add(1000000L);
        set.add(1500000L);

        set.add(2000000L);
        set.add(3000000L);
        set.add(4000000L);
        set.add(5000000L);
        set.add(7500000L);

        set.add(10000000L);
        set.add(12500000L);
        set.add(15000000L);
        set.add(20000000L);
        set.add(25000000L);

        set.add(30000000L);
        set.add(40000000L);
        set.add(50000000L);
        set.add(75000000L);
        set.add(100000000L);

        set.add(150000000L);
        set.add(200000000L);
        set.add(250000000L);
        set.add(300000000L);
        set.add(400000000L);

        set.add(500000000L);
        set.add(750000000L);
        set.add(1000000000L);
        set.add(2000000000L);
        set.add(5000000000L);

        set.add(10000000000L);
        set.add(20000000000L);
        set.add(30000000000L);
        set.add(40000000000L);
        set.add(50000000000L);

        set.add(60000000000L);
        set.add(70000000000L);
        set.add(80000000000L);
        set.add(90000000000L);
        set.add(100000000000L);

        set.add(125000000000L);
        set.add(150000000000L);
        set.add(175000000000L);
        set.add(200000000000L);
        set.add(250000000000L);

        set.add(300000000000L);
        set.add(350000000000L);
        set.add(400000000000L);
        set.add(450000000000L);
        set.add(500000000000L);

        set.add(600000000000L);
        set.add(700000000000L);
        set.add(800000000000L);
        set.add(900000000000L);
        set.add(1000000000000L);

        set.add(1250000000000L);
        set.add(1500000000000L);
        set.add(1750000000000L);
        set.add(2000000000000L);
        set.add(2500000000000L);

        set.add(3000000000000L);
        set.add(4000000000000L);
        set.add(5000000000000L);
        set.add(7500000000000L);
        set.add(10000000000000L);

        set.add(15000000000000L);
        set.add(20000000000000L);
        set.add(25000000000000L);
        set.add(30000000000000L);
        set.add(40000000000000L);

        set.add(50000000000000L);
        set.add(60000000000000L);
        set.add(70000000000000L);
        set.add(80000000000000L);
        set.add(90000000000000L);

        set.add(100000000000000L);
        set.add(110000000000000L);
        set.add(120000000000000L);
        set.add(130000000000000L);
        set.add(140000000000000L);

        set.add(150000000000000L);
        set.add(200000000000000L);
        set.add(300000000000000L);
        set.add(400000000000000L);
        set.add(500000000000000L);

        set.add(600000000000000L);
        set.add(800000000000000L);
        set.add(1000000000000000L);
        set.add(1200000000000000L);
        set.add(1400000000000000L);
    }



    public int rankup(Player p) {
        return rankup(p,1);
    }

    public int rankup(Player p, int amount) {
        PyrexPlayer player = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        int newRank = player.getRank()+amount;
        player.setRank(newRank);
        return newRank;
    }

    public static int getMaxRankup() {
        return maxRankup;
    }

    public static BigInteger getLevelCost(int level) {
        if (level <= 100) {
            return new BigInteger(set.get(level-1).toString());
        } else {
            BigInteger T200 = BigInteger.valueOf(200000000000000L);
            return BigInteger.valueOf(set.get(100-1)).add(T200.multiply(BigInteger.valueOf((level-100))));
        }
    }

    public String getRankString(int level) {
        return String.valueOf(level);
    }



    public static double getRankUpPercentage(Player p) {
        PyrexPlayer player = PyrexPrison.getPlugin().getPlayerManager().getPlayerMap().get(p.getUniqueId());
        double percentage = new BigDecimal(player.getTokens()).divide(new BigDecimal(getLevelCost(player.getRank()+1)),4,RoundingMode.HALF_UP).doubleValue() * 100;
        return percentage > 100 ? 100 : percentage;
    }

    public static String getRankUpProgressBar(Player p) {
        double percentage = getRankUpPercentage(p);
        if (percentage >= 100) {
            return StringUtil.color("&a&l||||||||||");
        } else if (percentage >= 90) {
            return StringUtil.color("&a&l|||||||||&c&l|");
        } else if (percentage >= 80) {
            return StringUtil.color("&a&l||||||||&c&l||");
        } else if (percentage >= 70) {
            return StringUtil.color("&a&l|||||||&c&l|||");
        } else if (percentage >= 60) {
            return StringUtil.color("&a&l||||||&c&l||||");
        } else if (percentage >= 50) {
            return StringUtil.color("&a&l|||||&c&l|||||");
        } else if (percentage >= 40) {
            return StringUtil.color("&a&l||||&c&l||||||");
        } else if (percentage >= 30) {
            return StringUtil.color("&a&l|||&c&l|||||||");
        } else if (percentage >= 20) {
            return StringUtil.color("&a&l||&c&l||||||||");
        } else if (percentage >= 10) {
            return StringUtil.color("&a&l|&c&l|||||||||");
        } else {
            return StringUtil.color("&c&l||||||||||");
        }
    }
}
