package com.example.smartpantry.util;

import java.util.Locale;

/** Unit handling: aliases, dimensions (mass / volume / count) and conversion to base units. */
public final class Units {

    public enum Dimension { MASS, VOLUME, COUNT }

    /** Units offered in the UI. */
    public static final String[] ALL = {"g", "kg", "ml", "l", "tsp", "tbsp", "cup", "pcs"};

    private Units() {}

    /** Maps spelling variants ("grams", "Litres", "piece") to a canonical unit. */
    public static String normalize(String unit) {
        String u = unit == null ? "" : unit.trim().toLowerCase(Locale.ROOT);
        switch (u) {
            case "g": case "gram": case "grams": case "gr": return "g";
            case "kg": case "kgs": case "kilogram": case "kilograms": return "kg";
            case "ml": case "millilitre": case "millilitres": case "milliliter": case "milliliters": return "ml";
            case "l": case "litre": case "litres": case "liter": case "liters": return "l";
            case "tsp": case "teaspoon": case "teaspoons": return "tsp";
            case "tbsp": case "tablespoon": case "tablespoons": return "tbsp";
            case "cup": case "cups": return "cup";
            default: return "pcs"; // pcs, piece(s), pc, whole, each, empty ...
        }
    }

    public static Dimension dimensionOf(String unit) {
        switch (normalize(unit)) {
            case "g": case "kg": return Dimension.MASS;
            case "ml": case "l": case "tsp": case "tbsp": case "cup": return Dimension.VOLUME;
            default: return Dimension.COUNT;
        }
    }

    /** Converts to grams, millilitres or pieces depending on the unit's dimension. */
    public static double toBase(double quantity, String unit) {
        switch (normalize(unit)) {
            case "kg": return quantity * 1000.0;
            case "l": return quantity * 1000.0;
            case "tsp": return quantity * 5.0;
            case "tbsp": return quantity * 15.0;
            case "cup": return quantity * 240.0;
            default: return quantity; // g, ml, pcs
        }
    }

    /** 2.0 -> "2", 2.5 -> "2.5", 0.333 -> "0.33". */
    public static String formatQty(double q) {
        if (Math.abs(q - Math.rint(q)) < 1e-9) return String.valueOf((long) Math.rint(q));
        String s = String.format(Locale.US, "%.2f", q);
        if (s.contains(".")) s = s.replaceAll("0+$", "").replaceAll("\\.$", "");
        return s;
    }
}
