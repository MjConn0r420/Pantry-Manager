package com.example.smartpantry.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class ExpiryUtil {

    public static final int SOON_DAYS = 3;
    public static final String FORMAT = "yyyy-MM-dd";

    private ExpiryUtil() {}

    /** Days from today until the date (negative = already expired). Null if no/invalid date. */
    public static Integer daysUntil(String isoDate) {
        if (isoDate == null || isoDate.trim().isEmpty()) return null;
        try {
            SimpleDateFormat f = new SimpleDateFormat(FORMAT, Locale.US);
            f.setLenient(false);
            Date d = f.parse(isoDate.trim());
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            return (int) Math.round((d.getTime() - today.getTimeInMillis()) / 86400000.0);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isExpiringSoon(String isoDate) {
        Integer d = daysUntil(isoDate);
        return d != null && d <= SOON_DAYS;
    }
}
