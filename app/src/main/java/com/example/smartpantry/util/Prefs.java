package com.example.smartpantry.util;

import android.content.Context;
import android.content.SharedPreferences;

/** Settings screen values (SharedPreferences). */
public final class Prefs {
    private static final String FILE = "settings";
    private static final String KEY_ALERTS = "expiring_alerts";
    private static final String KEY_UNIT = "default_unit";

    private Prefs() {}

    private static SharedPreferences sp(Context c) {
        return c.getSharedPreferences(FILE, Context.MODE_PRIVATE);
    }

    public static boolean expiringAlerts(Context c) { return sp(c).getBoolean(KEY_ALERTS, true); }
    public static void setExpiringAlerts(Context c, boolean on) { sp(c).edit().putBoolean(KEY_ALERTS, on).apply(); }

    public static String defaultUnit(Context c) { return sp(c).getString(KEY_UNIT, "g"); }
    public static void setDefaultUnit(Context c, String unit) { sp(c).edit().putString(KEY_UNIT, unit).apply(); }
}
