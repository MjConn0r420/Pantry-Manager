package com.example.smartpantry.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartpantry.model.PantryItem;
import com.example.smartpantry.model.Recipe;
import com.example.smartpantry.model.RecipeIngredient;
import com.example.smartpantry.util.Normalizer;

import java.util.ArrayList;
import java.util.List;

/**
 * Local SQLite database (SQLiteOpenHelper).
 * Tables: pantry_item (full CRUD), recipe + recipe_ingredient (seeded on first run).
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry_item ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "quantity REAL NOT NULL,"
                + "unit TEXT NOT NULL,"
                + "expiry_date TEXT)");
        db.execSQL("CREATE TABLE recipe ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT NOT NULL,"
                + "steps TEXT NOT NULL)");
        db.execSQL("CREATE TABLE recipe_ingredient ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "recipe_id INTEGER NOT NULL,"
                + "ingredient_name TEXT NOT NULL,"
                + "quantity REAL NOT NULL,"
                + "unit TEXT NOT NULL,"
                + "FOREIGN KEY(recipe_id) REFERENCES recipe(id) ON DELETE CASCADE)");
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Recipes are re-seedable; pantry data would need a real migration in a future version.
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredient");
        db.execSQL("DROP TABLE IF EXISTS recipe");
        db.execSQL("DROP TABLE IF EXISTS pantry_item");
        onCreate(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        for (String[] r : SeedData.RECIPES) {
            ContentValues rv = new ContentValues();
            rv.put("name", r[0]);
            rv.put("steps", r[1]);
            long recipeId = db.insert("recipe", null, rv);
            for (String part : r[2].split(";")) {
                String[] f = part.split(":");
                ContentValues iv = new ContentValues();
                iv.put("recipe_id", recipeId);
                iv.put("ingredient_name", Normalizer.name(f[0]));
                iv.put("quantity", Double.parseDouble(f[1]));
                iv.put("unit", f[2]);
                db.insert("recipe_ingredient", null, iv);
            }
        }
    }

    // ---------------------------------------------------------------- pantry CRUD

    /** CREATE */
    public long addItem(PantryItem item) {
        return getWritableDatabase().insert("pantry_item", null, toValues(item));
    }

    /** READ (all) */
    public List<PantryItem> getAllItems() {
        List<PantryItem> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query("pantry_item", null, null, null, null, null,
                "name COLLATE NOCASE ASC");
        try {
            while (c.moveToNext()) list.add(fromCursor(c));
        } finally {
            c.close();
        }
        return list;
    }

    /** READ (one) */
    public PantryItem getItem(long id) {
        Cursor c = getReadableDatabase().query("pantry_item", null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        try {
            return c.moveToFirst() ? fromCursor(c) : null;
        } finally {
            c.close();
        }
    }

    /** UPDATE */
    public int updateItem(PantryItem item) {
        return getWritableDatabase().update("pantry_item", toValues(item), "id = ?",
                new String[]{String.valueOf(item.getId())});
    }

    /** DELETE */
    public int deleteItem(long id) {
        return getWritableDatabase().delete("pantry_item", "id = ?", new String[]{String.valueOf(id)});
    }

    private ContentValues toValues(PantryItem item) {
        ContentValues v = new ContentValues();
        v.put("name", item.getName());
        v.put("quantity", item.getQuantity());
        v.put("unit", item.getUnit());
        v.put("expiry_date", item.getExpiryDate());
        return v;
    }

    private PantryItem fromCursor(Cursor c) {
        return new PantryItem(
                c.getLong(c.getColumnIndexOrThrow("id")),
                c.getString(c.getColumnIndexOrThrow("name")),
                c.getDouble(c.getColumnIndexOrThrow("quantity")),
                c.getString(c.getColumnIndexOrThrow("unit")),
                c.isNull(c.getColumnIndexOrThrow("expiry_date")) ? null
                        : c.getString(c.getColumnIndexOrThrow("expiry_date")));
    }

    // ---------------------------------------------------------------- recipes (read only)

    public List<Recipe> getAllRecipes() {
        List<Recipe> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("recipe", null, null, null, null, null, "name COLLATE NOCASE ASC");
        try {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow("id"));
                list.add(new Recipe(id,
                        c.getString(c.getColumnIndexOrThrow("name")),
                        c.getString(c.getColumnIndexOrThrow("steps")),
                        getIngredients(db, id)));
            }
        } finally {
            c.close();
        }
        return list;
    }

    public Recipe getRecipe(long id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("recipe", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        try {
            if (!c.moveToFirst()) return null;
            return new Recipe(id,
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("steps")),
                    getIngredients(db, id));
        } finally {
            c.close();
        }
    }

    private List<RecipeIngredient> getIngredients(SQLiteDatabase db, long recipeId) {
        List<RecipeIngredient> list = new ArrayList<>();
        Cursor c = db.query("recipe_ingredient", null, "recipe_id = ?",
                new String[]{String.valueOf(recipeId)}, null, null, "id ASC");
        try {
            while (c.moveToNext()) {
                list.add(new RecipeIngredient(
                        c.getString(c.getColumnIndexOrThrow("ingredient_name")),
                        c.getDouble(c.getColumnIndexOrThrow("quantity")),
                        c.getString(c.getColumnIndexOrThrow("unit"))));
            }
        } finally {
            c.close();
        }
        return list;
    }
}
