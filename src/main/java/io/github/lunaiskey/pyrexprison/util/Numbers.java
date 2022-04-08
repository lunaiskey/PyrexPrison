package io.github.lunaiskey.pyrexprison.util;

import java.math.BigDecimal;
import java.math.BigInteger;
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

    public static String formattedNumber(BigInteger num) {
        BigDecimal number = new BigDecimal(num);
        if (number.compareTo(BigDecimal.valueOf(1000L))<0) {
            return number+"";
        } else if (number.compareTo(BigDecimal.valueOf(1000000L))<0) {
            return number.divide(BigDecimal.valueOf(1000L),2,RoundingMode.FLOOR)+"K";
        } else if (number.compareTo(BigDecimal.valueOf(1000000000L))<0) {
            return number.divide(BigDecimal.valueOf(1000000L),2,RoundingMode.FLOOR)+"M";
        } else if (number.compareTo(BigDecimal.valueOf(1000000000000L))<0) {
            return number.divide(BigDecimal.valueOf(1000000000L),2,RoundingMode.FLOOR)+"B";
        } else if (number.compareTo(BigDecimal.valueOf(1000000000000000L))<0) {
            return number.divide(BigDecimal.valueOf(1000000000000L),2,RoundingMode.FLOOR)+"T";
        } else if (number.compareTo(BigDecimal.valueOf(1000000000000000000L))<0) {
            return number.divide(BigDecimal.valueOf(1000000000000000L),2,RoundingMode.FLOOR)+"Q";
        } else if (number.compareTo(new BigDecimal("1000000000000000000000"))<0) {
            return number.divide(new BigDecimal("1000000000000000000"),2,RoundingMode.FLOOR)+"QN";
        } else if (number.compareTo(new BigDecimal("1000000000000000000000000"))<0) {
            return number.divide(new BigDecimal("1000000000000000000000"),2,RoundingMode.FLOOR)+"S";
        } else if (number.compareTo(new BigDecimal("1000000000000000000000000000"))<0) {
            return number.divide(new BigDecimal("1000000000000000000000000"),2,RoundingMode.FLOOR)+"SP";
        } else if (number.compareTo(new BigDecimal("1000000000000000000000000000000"))<0) {
            return number.divide(new BigDecimal("1000000000000000000000000000"),2,RoundingMode.FLOOR)+"O";
        } else if (number.compareTo(new BigDecimal("1000000000000000000000000000000000"))<0) {
            return number.divide(new BigDecimal("1000000000000000000000000000000"),2,RoundingMode.FLOOR)+"N";
        } else if (number.compareTo(new BigDecimal("1000000000000000000000000000000000000"))<0) {
            return number.divide(new BigDecimal("1000000000000000000000000000000000"),2,RoundingMode.FLOOR) + "D";
        } else {
        //} else if (number.compareTo(new BigDecimal("1000000000000000000000000000000000000000"))<0) {
            return number.divide(new BigDecimal("1000000000000000000000000000000000000"),2,RoundingMode.FLOOR)+"DD";
        }
    }

}
