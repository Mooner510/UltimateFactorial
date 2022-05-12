package Threads;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
    public static String getTime(long duration) {
        int hour = 0;
        while(duration >= 3600000) {
            duration -= 3600000;
            hour++;
        }
        int m = 0;
        while(duration >= 60000) {
            duration -= 60000;
            m++;
        }
        int s = 0;
        while(duration >= 1000) {
            duration -= 1000;
            s++;
        }
        int ms = (int) duration;

        return hour + "h " + m + "m " + s + "s " + ms + "ms";
    }

    public static void send(long now, long max) {
        System.out.println("Calculating... " + Math.floor((double) now / max * 1000) / 10 + "%");
    }

    public static void send(long now, long max, long weight) {
        if(now % weight == 0 || now == max) System.out.println("Writing... " + Math.floor((double) now / max * 1000) / 10 + "%");
    }

    /**
     * @param d = String
     *
     *          ex. 3k / 2.6m / 12.56k
     *
     * @return = formatted duration
     */
    public static long numberFromString(String d) {
        long r = 0;
        try {
            r = Long.parseLong(d);
        } catch (Exception ignore) {
            try {
                final double v = Double.parseDouble(d.substring(0, d.length() - 1));
                if (d.contains("b") || d.contains("B")) {
                    r += v * 1000000000;
                } else if (d.contains("m") || d.contains("M")) {
                    r += v * 1000000;
                } else if (d.contains("k") || d.contains("K")) {
                    r += v * 1000;
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return r;
    }

    private static final ArrayList<String> suffix = new ArrayList<>(Arrays.asList("", "k", "M", "B", "T", "Q"));
    public static String numberTic(double value, int a) {
        if (value < 1) {
            return (int) Math.round(value) + "";
        }
        double amount = Math.floor(Math.floor(Math.log10(value)) / 3);
        if (amount != 0) {
            value = value * Math.pow(0.001, amount);
            return parseString(value, a, true) + suffix.get((int) Math.floor(amount));
        }
        return parseString(value, a, true);
    }

    public static String parseString(double value, int amount, boolean comma) {
        BigDecimal b = BigDecimal.valueOf(value);
        b = b.setScale(amount, RoundingMode.DOWN);
        return parseIfInt(Double.parseDouble(b.toString()), comma);
    }

    public static String parseIfInt(double value, boolean comma) {
        if(value >= Integer.MAX_VALUE) {
            return numberTic(value, 3);
        }
        if(comma) {
            if (Math.floor(value) == value) {
                return commaNumber((int) Math.floor(value));
            }
            return commaNumber(value);
        } else {
            if (Math.floor(value) == value) {
                return ((int) Math.floor(value)) + "";
            }
            return BigDecimal.valueOf(value).toPlainString();
        }
    }

    public static String commaNumber(int number) {
        return NumberFormat.getInstance().format(number);
    }

    public static String commaNumber(double number) {
        return NumberFormat.getInstance().format(number);
    }
}
