package com.example.smartpantry.util;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * STRICT recipe matching - the core business logic of the app.
 *
 * A recipe is "makeable" only if EVERY ingredient is in the pantry in at least the
 * required quantity. One missing or insufficient ingredient excludes the recipe.
 *
 * Robustness: names are normalised (case / spacing / plural), units are converted to a
 * base unit (g, ml or pieces), and several pantry rows of the same ingredient are summed.
 * Ingredients with incompatible dimensions (e.g. pantry has tomato in grams, recipe needs
 * pieces) are NOT assumed to match - that keeps the strict rule honest.
 */
public final class RecipeMatcher {

    private static final double EPS = 1e-9;

    private RecipeMatcher() {}

    public static class MatchResult {
        public final Recipe recipe;
        public final List<RecipeIngredient> missing;

        public MatchResult(Recipe recipe, List<RecipeIngredient> missing) {
            this.recipe = recipe;
            this.missing = missing;
        }

        /** e.g. "Missing: garlic (need 2 pcs)". */
        public String missingText() {
            StringBuilder sb = new StringBuilder("Missing: ");
            for (int i = 0; i < missing.size(); i++) {
                RecipeIngredient m = missing.get(i);
                if (i > 0) sb.append(", ");
                sb.append(m.getName()).append(" (need ")
                  .append(Units.formatQty(m.getQuantity())).append(' ')
                  .append(Units.normalize(m.getUnit())).append(')');
            }
            return sb.toString();
        }
    }

    private static String key(String name, String unit) {
        return Normalizer.name(name) + "|" + Units.dimensionOf(unit).name();
    }

    /** Total pantry quantity per (normalised name, dimension), in base units. */
    public static Map<String, Double> pantryTotals(List<PantryItem> items) {
        Map<String, Double> totals = new HashMap<>();
        for (PantryItem p : items) {
            String k = key(p.getName(), p.getUnit());
            Double cur = totals.get(k);
            totals.put(k, (cur == null ? 0.0 : cur) + Units.toBase(p.getQuantity(), p.getUnit()));
        }
        return totals;
    }

    /** Ingredients of the recipe that the pantry cannot cover. Empty list = fully makeable. */
    public static List<RecipeIngredient> missing(Recipe recipe, List<PantryItem> items) {
        return missing(recipe, pantryTotals(items));
    }

    private static List<RecipeIngredient> missing(Recipe recipe, Map<String, Double> totals) {
        List<RecipeIngredient> out = new ArrayList<>();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            Double have = totals.get(key(ing.getName(), ing.getUnit()));
            double need = Units.toBase(ing.getQuantity(), ing.getUnit());
            if (have == null || have + EPS < need) out.add(ing);
        }
        return out;
    }

    public static boolean canCook(Recipe recipe, List<PantryItem> items) {
        return !recipe.getIngredients().isEmpty() && missing(recipe, items).isEmpty();
    }

    /** Main suggestions list: recipes where nothing is missing. */
    public static List<MatchResult> strictMatches(List<Recipe> recipes, List<PantryItem> items) {
        Map<String, Double> totals = pantryTotals(items);
        List<MatchResult> out = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.getIngredients().isEmpty()) continue;
            List<RecipeIngredient> miss = missing(r, totals);
            if (miss.isEmpty()) out.add(new MatchResult(r, miss));
        }
        return out;
    }

    /** Bonus list: recipes missing exactly ONE ingredient (kept separate from strict matches). */
    public static List<MatchResult> almostThere(List<Recipe> recipes, List<PantryItem> items) {
        Map<String, Double> totals = pantryTotals(items);
        List<MatchResult> out = new ArrayList<>();
        for (Recipe r : recipes) {
            if (r.getIngredients().isEmpty()) continue;
            List<RecipeIngredient> miss = missing(r, totals);
            if (miss.size() == 1) out.add(new MatchResult(r, miss));
        }
        return out;
    }
}
