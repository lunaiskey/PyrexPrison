package io.github.lunaiskey.pyrexprison.util;

public class TimeUtil {

    public static String countdown(long time) {
        long currentTime = System.currentTimeMillis();
        long difference = time - currentTime;

        int seconds = (int) (difference / 1000) % 60 ;
        int minutes = (int) ((difference / (1000*60)) % 60);
        int hours   = (int) ((difference / (1000*60*60)) % 24);

        return hours+"h "+minutes+"m "+seconds+"s";
    }
}
