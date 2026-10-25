package com.example.smartpantry.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.util.ExpiryUtil;
import com.example.smartpantry.util.Prefs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/** Screen 1 - Pantry list (Read, plus entry points to Create / Update / Delete). */
public class PantryListActivity extends BaseActivity implements PantryAdapter.Listener {

    private DatabaseHelper db;
    private PantryAdapter adapter;
    private RecyclerView rv;
    private TextView tvEmpty, tvBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);
        setTitle(R.string.title_pantry);
        setupBottomNav(R.id.nav_pantry);

        db = new DatabaseHelper(this);
        rv = findViewById(R.id.rvPantry);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvBanner = findViewById(R.id.tvExpiringBanner);
        adapter = new PantryAdapter(this);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> startActivity(new Intent(this, AddEditIngredientActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load() {
        List<PantryItem> items = db.getAllItems();
        boolean alerts = Prefs.expiringAlerts(this);
        adapter.setItems(items, alerts);

        boolean empty = items.isEmpty();
        tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        rv.setVisibility(empty ? View.GONE : View.VISIBLE);

        int soon = 0;
        if (alerts) {
            for (PantryItem it : items) if (ExpiryUtil.isExpiringSoon(it.getExpiryDate())) soon++;
        }
        if (soon > 0) {
            tvBanner.setText(getString(R.string.expiring_banner, soon, ExpiryUtil.SOON_DAYS));
            tvBanner.setVisibility(View.VISIBLE);
        } else {
            tvBanner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEdit(PantryItem item) {
        Intent i = new Intent(this, AddEditIngredientActivity.class);
        i.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(i);
    }

    @Override
    public void onDelete(final PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_title)
                .setMessage(getString(R.string.delete_message, item.getName()))
                .setPositiveButton(R.string.delete, (d, w) -> {
                    db.deleteItem(item.getId());
                    Toast.makeText(this, R.string.item_deleted, Toast.LENGTH_SHORT).show();
                    load();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
