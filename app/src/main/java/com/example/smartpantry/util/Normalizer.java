package com.example.smartpantry.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** Normalises ingredient names so "Tomatoes", " tomato " and "TOMATO" all compare equal. */
public final class Normalizer {

    private static final Set<String> KEEP = new HashSet<>(Arrays.asList(
            "hummus", "couscous", "asparagus", "molasses", "grits", "citrus", "swiss", "watercress"));

    private Normalizer() {}

    public static String name(String raw) {
        if (raw == null) return "";
        String s = raw.trim().toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        if (s.isEmpty()) return s;
        int sp = s.lastIndexOf(' ');
        String head = sp < 0 ? "" : s.substring(0, sp + 1);
        return head + singular(s.substring(sp + 1));
    }

    private static String singular(String w) {
        int n = w.length();
        if (n <= 3 || KEEP.contains(w)) return w;
        if (w.endsWith("ies") && n > 4) return w.substring(0, n - 3) + "y";   // berries -> berry
        if (w.endsWith("oes")) return w.substring(0, n - 2);                   // tomatoes -> tomato
        if (w.endsWith("ches") || w.endsWith("shes") || w.endsWith("sses") || w.endsWith("xes"))
            return w.substring(0, n - 2);                                       // radishes -> radish
        if (w.endsWith("ss") || w.endsWith("us")) return w;                    // cress, hummus
        if (w.endsWith("s")) return w.substring(0, n - 1);                     // eggs -> egg
        return w;
    }
}
