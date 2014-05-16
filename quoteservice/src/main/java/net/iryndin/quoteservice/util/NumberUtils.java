package net.iryndin.quoteservice.util;

import java.math.BigDecimal;

/**
 * Created by iryndin on 15.05.14.
 */
public class NumberUtils {

    /**
     * Return true if a<b
     * @param a
     * @param b
     * @return
     */
    public static boolean lt(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0;
    }

    public static boolean le(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) <= 0;
    }

    public static boolean gt(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) > 0;
    }

    public static boolean ge(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) >= 0;
    }
}
