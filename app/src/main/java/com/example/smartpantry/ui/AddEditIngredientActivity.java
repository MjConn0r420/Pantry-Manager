package com.example.smartpantry.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartpantry.R;
import com.example.smartpantry.db.DatabaseHelper;
import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.util.ExpiryUtil;
import com.example.smartpantry.util.Prefs;
import com.example.smartpantry.util.Units;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Locale;

/** Screen 2 - Add / Edit ingredient (Create + Update) with input validation. */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";

    private DatabaseHelper db;
    private long itemId = -1;
    private TextInputLayout tilName, tilQty;
    private TextInputEditText etName, etQty, etExpiry;
    private Spinner spUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        tilName = findViewById(R.id.tilName);
        tilQty = findViewById(R.id.tilQty);
        etName = findViewById(R.id.etName);
        etQty = findViewById(R.id.etQty);
        etExpiry = findViewById(R.id.etExpiry);
        spUnit = findViewById(R.id.spUnit);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnClear = findViewById(R.id.btnClearExpiry);

        ArrayAdapter<String> unitAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Units.ALL);
        spUnit.setAdapter(unitAdapter);

        itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            PantryItem item = db.getItem(itemId);
            if (item == null) {
                finish();
                return;
            }
            setTitle(R.string.edit_ingredient);
            etName.setText(item.getName());
            etQty.setText(Units.formatQty(item.getQuantity()));
            etExpiry.setText(item.getExpiryDate() == null ? "" : item.getExpiryDate());
            spUnit.setSelection(indexOfUnit(Units.normalize(item.getUnit())));
        } else {
            setTitle(R.string.add_ingredient);
            spUnit.setSelection(indexOfUnit(Prefs.defaultUnit(this)));
        }

        etExpiry.setOnClickListener(v -> showDatePicker());
        btnClear.setOnClickListener(v -> etExpiry.setText(""));
        btnSave.setOnClickListener(v -> save());
    }

    private int indexOfUnit(String unit) {
        for (int i = 0; i < Units.ALL.length; i++) if (Units.ALL[i].equals(unit)) return i;
        return 0;
    }

    private void showDatePicker() {
        Calendar cal = Calendar.getInstance();
        String current = text(etExpiry);
        Integer days = ExpiryUtil.daysUntil(current);
        if (days != null) cal.add(Calendar.DAY_OF_YEAR, days);
        new DatePickerDialog(this, (view, y, m, d) ->
                etExpiry.setText(String.format(Locale.US, "%04d-%02d-%02d", y, m + 1, d)),
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private static String text(TextInputEditText et) {
        return et.getText() == null ? "" : et.getText().toString().trim();
    }

    private void save() {
        String name = text(etName);
        String qtyStr = text(etQty);
        String expiry = text(etExpiry);
        boolean ok = true;
        tilName.setError(null);
        tilQty.setError(null);

        // Name: required, must contain a letter, max 40 chars
        if (name.isEmpty()) {
            tilName.setError(getString(R.string.err_name_required));
            ok = false;
        } else if (!name.matches(".*[A-Za-z].*") || name.length() > 40) {
            tilName.setError(getString(R.string.err_name_invalid));
            ok = false;
        }

        // Quantity: required, number > 0 and <= 100000
        double qty = -1;
        if (qtyStr.isEmpty()) {
            tilQty.setError(getString(R.string.err_qty_required));
            ok = false;
        } else {
            try {
                qty = Double.parseDouble(qtyStr);
            } catch (NumberFormatException e) {
                qty = -1;
            }
            if (!(qty > 0 && qty <= 100000)) {
                tilQty.setError(getString(R.string.err_qty_invalid));
                ok = false;
            }
        }

        if (!ok) return;

        String unit = spUnit.getSelectedItem().toString();
        PantryItem item = new PantryItem(itemId, name, qty, unit, expiry.isEmpty() ? null : expiry);
        if (itemId == -1) {
            db.addItem(item);
            Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();
        } else {
            db.updateItem(item);
            Toast.makeText(this, R.string.item_updated, Toast.LENGTH_SHORT).show();
        }
        finish();
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
