package com.example.smartpantry.ui;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/** Shared bottom-navigation wiring for the three top-level screens. */
public abstract class BaseActivity extends AppCompatActivity {

    protected void setupBottomNav(final int selectedId) {
        BottomNavigationView nav = findViewById(R.id.bottomNav);
        nav.setSelectedItemId(selectedId);
        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedId) return true;
            Class<?> target;
            if (id == R.id.nav_pantry) target = PantryListActivity.class;
            else if (id == R.id.nav_suggestions) target = SuggestedRecipesActivity.class;
            else target = SettingsActivity.class;

            Intent intent = new Intent(this, target);
            if (id == R.id.nav_pantry) {
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            startActivity(intent);
            overridePendingTransition(0, 0);
            if (!(this instanceof PantryListActivity)) finish();
            return true;
        });
    }
}
