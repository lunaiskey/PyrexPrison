package io.github.lunaiskey.pyrexprison.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Numbers {

    public static String formatDouble(double number) {
        long num = (long) number;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        if (number == num) {
            return String.valueOf(num);
        }
        return df.format(number);
    }

    public static String formattedNumber(long number) {
        if (number < 1000) {
            return number+"";
        } else if (number < 1000000L) {
            return formatDouble(number/1000D)+"K";
        } else if (number < 1000000000L) {
            return formatDouble(number/1000000D)+"M";
        } else if (number < 1000000000000L) {
            return formatDouble(number/1000000000D)+"B";
        } else if (number < 1000000000000000L) {
            return formatDouble(number/1000000000000D)+"T";
        } else {
            return formatDouble(number/1000000000000000D)+"Q";
        }
    }

}
