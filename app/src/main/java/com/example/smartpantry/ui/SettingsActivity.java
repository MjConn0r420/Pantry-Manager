package com.example.smartpantry.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.smartpantry.R;
import com.example.smartpantry.util.Prefs;
import com.example.smartpantry.util.Units;
import com.google.android.material.switchmaterial.SwitchMaterial;

/** Screen 5 - Settings (SharedPreferences). */
public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.title_settings);
        setupBottomNav(R.id.nav_settings);

        SwitchMaterial sw = findViewById(R.id.swAlerts);
        sw.setChecked(Prefs.expiringAlerts(this));
        sw.setOnCheckedChangeListener((b, checked) -> Prefs.setExpiringAlerts(this, checked));

        Spinner sp = findViewById(R.id.spDefaultUnit);
        sp.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Units.ALL));
        String current = Prefs.defaultUnit(this);
        for (int i = 0; i < Units.ALL.length; i++) {
            if (Units.ALL[i].equals(current)) sp.setSelection(i);
        }
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Prefs.setDefaultUnit(SettingsActivity.this, Units.ALL[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
