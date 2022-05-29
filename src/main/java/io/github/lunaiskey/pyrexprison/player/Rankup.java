package io.github.lunaiskey.pyrexprison.player;

import io.github.lunaiskey.pyrexprison.PyrexPrison;
import io.github.lunaiskey.pyrexprison.util.StringUtil;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Rankup {

    private static final List<Long> cost = new ArrayList<>();
    private static final int maxRankup = 2700;

    /*
    static {
        cost.add(5000L);
        cost.add(10000L);
        cost.add(25000L);
        cost.add(50000L);
        cost.add(75000L);
        cost.add(100000L);
        cost.add(150000L);
        cost.add(200000L);
        cost.add(250000L);
        cost.add(300000L);

        cost.add(400000L);
        cost.add(500000L);
        cost.add(750000L);
        cost.add(1000000L);
        cost.add(1500000L);
        cost.add(2000000L);
        cost.add(3000000L);
        cost.add(4000000L);
        cost.add(5000000L);
        cost.add(7500000L);

        cost.add(10000000L);
        cost.add(12500000L);
        cost.add(15000000L);
        cost.add(20000000L);
        cost.add(25000000L);
        cost.add(30000000L);
        cost.add(40000000L);
        cost.add(50000000L);
        cost.add(75000000L);
        cost.add(100000000L);

        cost.add(150000000L);
        cost.add(200000000L);
        cost.add(250000000L);
        cost.add(300000000L);
        cost.add(400000000L);
        cost.add(500000000L);
        cost.add(750000000L);
        cost.add(1000000000L);
        cost.add(2000000000L);
        cost.add(5000000000L);

        cost.add(10000000000L);
        cost.add(20000000000L);
        cost.add(30000000000L);
        cost.add(40000000000L);
        cost.add(50000000000L);
        cost.add(60000000000L);
        cost.add(70000000000L);
        cost.add(80000000000L);
        cost.add(90000000000L);
        cost.add(100000000000L);

        cost.add(125000000000L);
        cost.add(150000000000L);
        cost.add(175000000000L);
        cost.add(200000000000L);
        cost.add(250000000000L);
        cost.add(300000000000L);
        cost.add(350000000000L);
        cost.add(400000000000L);
        cost.add(450000000000L);
        cost.add(500000000000L);

        cost.add(600000000000L);
        cost.add(700000000000L);
        cost.add(800000000000L);
        cost.add(900000000000L);
        cost.add(1000000000000L);
        cost.add(1250000000000L);
        cost.add(1500000000000L);
        cost.add(1750000000000L);
        cost.add(2000000000000L);
        cost.add(2500000000000L);

        cost.add(3000000000000L);
        cost.add(4000000000000L);
        cost.add(5000000000000L);
        cost.add(7500000000000L);
        cost.add(10000000000000L);
        cost.add(15000000000000L);
        cost.add(20000000000000L);
        cost.add(25000000000000L);
        cost.add(30000000000000L);
        cost.add(40000000000000L);

        cost.add(50000000000000L);
        cost.add(60000000000000L);
        cost.add(70000000000000L);
        cost.add(80000000000000L);
        cost.add(90000000000000L);
        cost.add(100000000000000L);
        cost.add(110000000000000L);
        cost.add(120000000000000L);
        cost.add(130000000000000L);
        cost.add(140000000000000L);

        cost.add(150000000000000L);
        cost.add(200000000000000L);
        cost.add(300000000000000L);
        cost.add(400000000000000L);
        cost.add(500000000000000L);
        cost.add(600000000000000L);
        cost.add(800000000000000L);
        cost.add(1000000000000000L);
        cost.add(1200000000000000L);
        cost.add(1400000000000000L);
    }

     */
    static {
        cost.add(100L);
        cost.add(150L);
        cost.add(250L);
        cost.add(375L);
        cost.add(500L);
        cost.add(750L);
        cost.add(1000L);
        cost.add(1250L);
        cost.add(1750L);
        cost.add(2500L); //2.5k

        cost.add(3500L);
        cost.add(5000L);
        cost.add(7500L);
        cost.add(10000L);
        cost.add(12500L);
        cost.add(15000L);
        cost.add(20000L);
        cost.add(25000L);
        cost.add(37500L);
        cost.add(50000L); //50k

        cost.add(62500L);
        cost.add(75000L);
        cost.add(100000L);
        cost.add(150000L);
        cost.add(200000L);
        cost.add(250000L);
        cost.add(375000L);
        cost.add(500000L);
        cost.add(750000L);
        cost.add(1000000L); //1m

        cost.add(1500000L);
        cost.add(2000000L);
        cost.add(3000000L);
        cost.add(4000000L);
        cost.add(5000000L);
        cost.add(7500000L);
        cost.add(10000000L);
        cost.add(12500000L);
        cost.add(15000000L);
        cost.add(20000000L); //20m

        cost.add(25000000L);
        cost.add(37500000L);
        cost.add(50000000L);
        cost.add(62500000L);
        cost.add(75000000L);
        cost.add(87500000L);
        cost.add(100000000L);
        cost.add(125000000L);
        cost.add(175000000L);
        cost.add(250000000L); //250m

        cost.add(300000000L);
        cost.add(375000000L);
        cost.add(500000000L);
        cost.add(625000000L);
        cost.add(750000000L);
        cost.add(875000000L);
        cost.add(1000000000L);
        cost.add(1250000000L);
        cost.add(1500000000L);
        cost.add(1750000000L); //1.75b

        cost.add(2000000000L);
        cost.add(2500000000L);
        cost.add(3000000000L);
        cost.add(4000000000L);
        cost.add(5000000000L);
        cost.add(6250000000L);
        cost.add(7500000000L);
        cost.add(8750000000L);
        cost.add(10000000000L);
        cost.add(15000000000L); //15b

        cost.add(20000000000L);
        cost.add(25000000000L);
        cost.add(32500000000L);
        cost.add(40000000000L);
        cost.add(50000000000L);
        cost.add(62500000000L);
        cost.add(75000000000L);
        cost.add(87500000000L);
        cost.add(100000000000L);
        cost.add(150000000000L); //150b

        cost.add(200000000000L);
        cost.add(300000000000L);
        cost.add(400000000000L);
        cost.add(500000000000L);
        cost.add(625000000000L);
        cost.add(750000000000L);
        cost.add(875000000000L);
        cost.add(1000000000000L);
        cost.add(1250000000000L);
        cost.add(1500000000000L); //1.5T

        cost.add(1750000000000L);
        cost.add(2000000000000L);
        cost.add(3000000000000L);
        cost.add(4000000000000L);
        cost.add(5000000000000L);
        cost.add(6000000000000L);
        cost.add(7000000000000L);
        cost.add(8000000000000L);
        cost.add(9000000000000L);
        cost.add(10000000000000L); //10T
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
            return new BigInteger(cost.get(level-1).toString());
        } else {
            BigInteger everyRank = BigInteger.valueOf(1000000000000L);
            return BigInteger.valueOf(cost.get(100-1)).add(everyRank.multiply(BigInteger.valueOf((level-100))));
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
