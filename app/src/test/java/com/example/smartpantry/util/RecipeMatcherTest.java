package com.example.smartpantry.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/** Local unit tests for the strict-matching rule (run with ./gradlew test). */
public class RecipeMatcherTest {

    private static PantryItem p(String name, double q, String unit) {
        return new PantryItem(0, name, q, unit, null);
    }

    private static RecipeIngredient i(String name, double q, String unit) {
        return new RecipeIngredient(name, q, unit);
    }

    private static Recipe fiveIngredientRecipe() {
        return new Recipe(1, "Test Dish", "steps", Arrays.asList(
                i("pasta", 200, "g"), i("tomato", 4, "pcs"), i("garlic", 2, "pcs"),
                i("olive oil", 2, "tbsp"), i("salt", 1, "tsp")));
    }

    private static List<PantryItem> allFive() {
        return new ArrayList<>(Arrays.asList(
                p("Pasta", 500, "g"), p("Tomatoes", 6, "pcs"), p("Garlic", 3, "pcs"),
                p("Olive oil", 100, "ml"), p("Salt", 1, "tbsp")));
    }

    @Test
    public void allIngredientsPresent_isSuggested() {
        List<Recipe> r = Arrays.asList(fiveIngredientRecipe());
        assertEquals(1, RecipeMatcher.strictMatches(r, allFive()).size());
    }

    @Test
    public void fourOfFiveIngredients_isNotSuggested() {
        List<PantryItem> pantry = allFive();
        pantry.remove(4); // remove salt
        List<Recipe> r = Arrays.asList(fiveIngredientRecipe());
        assertTrue(RecipeMatcher.strictMatches(r, pantry).isEmpty());
        assertFalse(RecipeMatcher.canCook(r.get(0), pantry));
    }

    @Test
    public void insufficientQuantity_isNotSuggested() {
        List<PantryItem> pantry = allFive();
        pantry.set(1, p("tomato", 3, "pcs")); // needs 4
        assertTrue(RecipeMatcher.strictMatches(Arrays.asList(fiveIngredientRecipe()), pantry).isEmpty());
    }

    @Test
    public void pluralAndCaseDifferences_stillMatch() {
        assertEquals(Normalizer.name("tomato"), Normalizer.name("  TOMATOES "));
        assertEquals(Normalizer.name("egg"), Normalizer.name("Eggs"));
        assertEquals(Normalizer.name("berry"), Normalizer.name("berries"));
        assertEquals("hummus", Normalizer.name("Hummus"));
    }

    @Test
    public void unitConversion_kgSatisfiesGrams() {
        Recipe r = new Recipe(2, "Flour thing", "s", Arrays.asList(i("flour", 200, "g")));
        List<PantryItem> pantry = Arrays.asList(p("flour", 1, "kg"));
        assertEquals(1, RecipeMatcher.strictMatches(Arrays.asList(r), pantry).size());
    }

    @Test
    public void duplicateRowsAreSummed() {
        Recipe r = new Recipe(3, "Eggs", "s", Arrays.asList(i("egg", 3, "pcs")));
        List<PantryItem> pantry = Arrays.asList(p("egg", 2, "pcs"), p("Eggs", 1, "pcs"));
        assertEquals(1, RecipeMatcher.strictMatches(Arrays.asList(r), pantry).size());
    }

    @Test
    public void incompatibleDimensions_areNotAssumedToMatch() {
        Recipe r = new Recipe(4, "Tomato", "s", Arrays.asList(i("tomato", 2, "pcs")));
        List<PantryItem> pantry = Arrays.asList(p("tomato", 500, "g"));
        assertTrue(RecipeMatcher.strictMatches(Arrays.asList(r), pantry).isEmpty());
    }

    @Test
    public void almostThere_exactlyOneMissing_onlyInSeparateList() {
        List<PantryItem> pantry = allFive();
        pantry.remove(4); // salt missing
        List<Recipe> r = Arrays.asList(fiveIngredientRecipe());
        assertTrue(RecipeMatcher.strictMatches(r, pantry).isEmpty());
        List<RecipeMatcher.MatchResult> almost = RecipeMatcher.almostThere(r, pantry);
        assertEquals(1, almost.size());
        assertEquals("salt", almost.get(0).missing.get(0).getName());
    }

    @Test
    public void twoMissing_notInAlmostThere() {
        List<PantryItem> pantry = allFive();
        pantry.remove(4);
        pantry.remove(3);
        assertTrue(RecipeMatcher.almostThere(Arrays.asList(fiveIngredientRecipe()), pantry).isEmpty());
    }

    @Test
    public void emptyPantry_noSuggestions() {
        assertTrue(RecipeMatcher.strictMatches(Arrays.asList(fiveIngredientRecipe()),
                new ArrayList<PantryItem>()).isEmpty());
    }
}
