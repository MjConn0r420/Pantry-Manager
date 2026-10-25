<<<<<<< HEAD
# Smart Pantry Manager (Android, Java)

Tracks pantry ingredients and suggests ONLY recipes you can cook right now (strict matching).

## Open & run
1. Android Studio (Koala 2024.1.1 or newer) -> File > Open -> select the `SmartPantryManager` folder.
2. Let Gradle sync (needs internet the first time; the wrapper downloads Gradle 8.7).
3. Pick an emulator or device (API 24+) and press Run.

If your Android Studio is older, update it, or lower the AGP version in the root `build.gradle` when prompted.

## Structure
- `db/DatabaseHelper` - SQLiteOpenHelper; full CRUD on `pantry_item`; seeds 20 recipes on first run
- `util/RecipeMatcher` - strict matching (+ optional "Almost There" list), `Normalizer` (plurals/case), `Units` (kg->g etc.)
- `ui/` - PantryList, AddEditIngredient, SuggestedRecipes, RecipeDetail, Settings (+ RecyclerView adapters, bottom navigation)
- `app/src/test/.../RecipeMatcherTest` - unit tests (`./gradlew test`)

## Notes for your report / demo
- Database: local SQLite via SQLiteOpenHelper. Data persists after closing the app.
- Strict rule: every ingredient must be present in at least the required quantity, otherwise the recipe is excluded. 4 of 5 ingredients = not suggested.
- Matching handles case, spacing, plurals (tomato/tomatoes), unit conversion (kg/g, l/ml, tsp/tbsp/cup) and sums duplicate pantry rows.
- Limitation: count vs weight/volume (e.g. pantry "tomato 500 g" vs recipe "4 pcs") is deliberately not guessed.
- No Google Maps, no location/GPS, no payments.
=======
# Pantry-Manager
An Android software called Smart Pantry Manager assists users in tracking food supplies, organizing kitchen goods, and finding recipes depending on what they have at home. It makes meal planning simpler, cuts down on food waste, saves time, and prevents needless grocery expenditures.
>>>>>>> 072d6793c55f06ce47ca781c6c0d59ae2c136060
