package com.example.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.util.RecipeMatcher;
import com.example.smartpantry.util.RecipeMatcher.MatchResult;

import java.util.List;

/** Screen 3 - Suggested recipes: strict matches, plus a clearly separate "Almost There" list. */
public class SuggestedRecipesActivity extends BaseActivity implements RecipeAdapter.Listener {

    private DatabaseHelper db;
    private RecipeAdapter strictAdapter, almostAdapter;
    private TextView tvSummary, tvEmpty, tvAlmostHeader;
    private RecyclerView rvStrict, rvAlmost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle(R.string.title_suggestions);
        setupBottomNav(R.id.nav_suggestions);

        db = new DatabaseHelper(this);
        tvSummary = findViewById(R.id.tvSummary);
        tvEmpty = findViewById(R.id.tvEmptyStrict);
        tvAlmostHeader = findViewById(R.id.tvAlmostHeader);
        rvStrict = findViewById(R.id.rvStrict);
        rvAlmost = findViewById(R.id.rvAlmost);

        strictAdapter = new RecipeAdapter(false, this);
        almostAdapter = new RecipeAdapter(true, this);
        rvStrict.setLayoutManager(new LinearLayoutManager(this));
        rvAlmost.setLayoutManager(new LinearLayoutManager(this));
        rvStrict.setNestedScrollingEnabled(false);
        rvAlmost.setNestedScrollingEnabled(false);
        rvStrict.setAdapter(strictAdapter);
        rvAlmost.setAdapter(almostAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<PantryItem> pantry = db.getAllItems();
        List<Recipe> recipes = db.getAllRecipes();

        List<MatchResult> strict = RecipeMatcher.strictMatches(recipes, pantry);
        List<MatchResult> almost = RecipeMatcher.almostThere(recipes, pantry);

        strictAdapter.setData(strict);
        almostAdapter.setData(almost);

        boolean noMatches = strict.isEmpty();
        tvEmpty.setVisibility(noMatches ? View.VISIBLE : View.GONE);
        rvStrict.setVisibility(noMatches ? View.GONE : View.VISIBLE);
        tvSummary.setText(noMatches ? getString(R.string.summary_none)
                : getString(R.string.summary_count, strict.size()));

        boolean hasAlmost = !almost.isEmpty();
        tvAlmostHeader.setVisibility(hasAlmost ? View.VISIBLE : View.GONE);
        rvAlmost.setVisibility(hasAlmost ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent i = new Intent(this, RecipeDetailActivity.class);
        i.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(i);
    }
}
