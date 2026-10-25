package com.example.smartpantry.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;
import com.example.smartpantry.util.RecipeMatcher;
import com.example.smartpantry.util.Units;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Screen 4 - Recipe detail: full ingredient list (with have / missing marks) and method. */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseHelper db = new DatabaseHelper(this);
        Recipe recipe = db.getRecipe(getIntent().getLongExtra(EXTRA_RECIPE_ID, -1));
        if (recipe == null) {
            finish();
            return;
        }
        setTitle(recipe.getName());

        List<PantryItem> pantry = db.getAllItems();
        List<RecipeIngredient> missing = RecipeMatcher.missing(recipe, pantry);
        Set<RecipeIngredient> missingSet = new HashSet<>(missing);

        ((TextView) findViewById(R.id.tvTitle)).setText(recipe.getName());
        ((TextView) findViewById(R.id.tvStatus)).setText(missing.isEmpty()
                ? getString(R.string.status_have_all)
                : getString(R.string.status_missing, missing.size()));

        StringBuilder sb = new StringBuilder();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            sb.append(missingSet.contains(ing) ? "\u2717  " : "\u2713  ")
              .append(Units.formatQty(ing.getQuantity())).append(' ')
              .append(Units.normalize(ing.getUnit())).append("  ")
              .append(ing.getName()).append('\n');
        }
        ((TextView) findViewById(R.id.tvIngredients)).setText(sb.toString().trim());
        ((TextView) findViewById(R.id.tvSteps)).setText(recipe.getSteps());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
