package com.example.smartpantry.db;

/**
 * 20 starter recipes, inserted on first run.
 * Each row = { name, method steps, ingredients as "name:qty:unit;name:qty:unit" }.
 */
final class SeedData {

    private SeedData() {}

    static final String[][] RECIPES = {
        {"Tomato Pasta",
         "1. Boil the pasta in salted water until al dente.\n2. Fry the garlic in olive oil for 30 seconds.\n3. Add chopped tomatoes and simmer for 10 minutes.\n4. Toss the pasta through the sauce and serve.",
         "pasta:200:g;tomato:4:pcs;garlic:2:pcs;olive oil:2:tbsp;salt:1:tsp"},
        {"Scrambled Eggs on Toast",
         "1. Toast the bread.\n2. Whisk the eggs with salt.\n3. Melt butter in a pan and stir the eggs gently over low heat until just set.\n4. Pile onto the toast.",
         "egg:3:pcs;bread:2:pcs;butter:10:g;salt:1:tsp"},
        {"Cheese Omelette",
         "1. Whisk eggs with salt.\n2. Melt butter in a pan and pour in the eggs.\n3. When almost set, add grated cheese and fold the omelette over.\n4. Serve hot.",
         "egg:3:pcs;cheese:50:g;butter:10:g;salt:1:tsp"},
        {"Fluffy Pancakes",
         "1. Whisk flour, sugar, milk and eggs into a smooth batter.\n2. Melt a little butter in a pan.\n3. Pour in small rounds of batter and cook until bubbles form.\n4. Flip and cook until golden.",
         "flour:200:g;milk:300:ml;egg:2:pcs;sugar:2:tbsp;butter:20:g"},
        {"Egg Fried Rice",
         "1. Cook the rice and let it cool.\n2. Fry chopped onion in oil until soft.\n3. Push aside, scramble the eggs in the pan.\n4. Add the rice and soy sauce and stir-fry for 3 minutes.",
         "rice:200:g;egg:2:pcs;onion:1:pcs;soy sauce:2:tbsp;vegetable oil:1:tbsp"},
        {"Creamy Mashed Potatoes",
         "1. Peel and cube the potatoes.\n2. Boil in salted water for 15-20 minutes until soft.\n3. Drain and mash with butter and warm milk.\n4. Season with salt.",
         "potato:4:pcs;butter:30:g;milk:100:ml;salt:1:tsp"},
        {"Garlic Bread",
         "1. Mash soft butter with crushed garlic.\n2. Spread on the bread slices.\n3. Bake at 200C for 8-10 minutes until golden.",
         "bread:4:pcs;butter:40:g;garlic:2:pcs"},
        {"Simple Tomato Soup",
         "1. Soften chopped onion and garlic in olive oil.\n2. Add chopped tomatoes and stock.\n3. Simmer for 20 minutes.\n4. Blend until smooth and season.",
         "tomato:6:pcs;onion:1:pcs;garlic:2:pcs;vegetable stock:500:ml;olive oil:1:tbsp"},
        {"Chicken Rice Bowl",
         "1. Cook the rice.\n2. Slice the chicken and fry in oil with onion until cooked through.\n3. Add soy sauce and toss.\n4. Serve over the rice.",
         "chicken breast:2:pcs;rice:200:g;onion:1:pcs;soy sauce:2:tbsp;olive oil:1:tbsp"},
        {"Veggie Stir Fry",
         "1. Slice the carrots, onion and pepper into strips.\n2. Heat oil in a wok and fry the garlic for 30 seconds.\n3. Add the vegetables and stir-fry for 5-6 minutes.\n4. Finish with soy sauce.",
         "carrot:2:pcs;onion:1:pcs;bell pepper:1:pcs;soy sauce:2:tbsp;vegetable oil:1:tbsp;garlic:2:pcs"},
        {"Banana Pancakes",
         "1. Mash the bananas.\n2. Mix with eggs, flour and milk.\n3. Cook spoonfuls in a lightly oiled pan until golden on both sides.",
         "banana:2:pcs;egg:2:pcs;flour:100:g;milk:100:ml"},
        {"Cheese Toastie",
         "1. Butter the outside of the bread.\n2. Fill with sliced or grated cheese.\n3. Fry on both sides until golden and the cheese melts.",
         "bread:2:pcs;cheese:60:g;butter:10:g"},
        {"Hearty Lentil Soup",
         "1. Fry onion, carrot and garlic in olive oil.\n2. Add rinsed lentils and stock.\n3. Simmer for 25-30 minutes until the lentils are soft.\n4. Season and serve.",
         "lentil:200:g;onion:1:pcs;carrot:2:pcs;garlic:2:pcs;vegetable stock:750:ml;olive oil:1:tbsp"},
        {"Spaghetti Aglio e Olio",
         "1. Cook the spaghetti.\n2. Gently warm sliced garlic in olive oil until fragrant (do not brown).\n3. Toss the drained spaghetti in the oil with a splash of pasta water.\n4. Season with salt.",
         "spaghetti:200:g;garlic:4:pcs;olive oil:4:tbsp;salt:1:tsp"},
        {"Macaroni Cheese",
         "1. Boil the macaroni.\n2. Melt butter, stir in flour, then whisk in milk to make a sauce.\n3. Stir in grated cheese until melted.\n4. Mix with the macaroni and serve.",
         "macaroni:250:g;cheese:150:g;milk:300:ml;butter:30:g;flour:2:tbsp"},
        {"Egg Fried Noodles",
         "1. Cook the noodles and drain.\n2. Fry sliced onion in oil, then scramble in the eggs.\n3. Add noodles and soy sauce and toss for 2-3 minutes.",
         "noodle:200:g;egg:2:pcs;soy sauce:2:tbsp;vegetable oil:1:tbsp;onion:1:pcs"},
        {"Cheesy Baked Potato",
         "1. Prick the potatoes and bake at 200C for about 1 hour.\n2. Split open and mash in the butter.\n3. Top with grated cheese and salt, then return to the oven for 5 minutes.",
         "potato:2:pcs;cheese:60:g;butter:20:g;salt:1:tsp"},
        {"Creamy Rice Pudding",
         "1. Combine rice, milk and sugar in a pot.\n2. Simmer gently for 30-35 minutes, stirring often.\n3. Stir in butter and serve warm.",
         "rice:100:g;milk:500:ml;sugar:3:tbsp;butter:10:g"},
        {"French Toast",
         "1. Whisk eggs, milk and sugar.\n2. Dip the bread slices in the mixture.\n3. Fry in butter until golden on both sides.",
         "bread:4:pcs;egg:2:pcs;milk:100:ml;sugar:1:tbsp;butter:20:g"},
        {"Chicken and Tomato Stew",
         "1. Brown the chicken in olive oil, then set aside.\n2. Fry onion and garlic, add chopped tomatoes.\n3. Return the chicken and simmer for 30 minutes.\n4. Season with salt.",
         "chicken breast:2:pcs;tomato:4:pcs;onion:1:pcs;garlic:2:pcs;olive oil:1:tbsp;salt:1:tsp"}
    };
}
