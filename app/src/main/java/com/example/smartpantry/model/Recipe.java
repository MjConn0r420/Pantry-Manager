package com.example.smartpantry.model;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private final long id;
    private final String name;
    private final String steps;
    private final List<RecipeIngredient> ingredients;

    public Recipe(long id, String name, String steps, List<RecipeIngredient> ingredients) {
        this.id = id;
        this.name = name;
        this.steps = steps;
        this.ingredients = ingredients == null ? new ArrayList<RecipeIngredient>() : ingredients;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getSteps() { return steps; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
}
