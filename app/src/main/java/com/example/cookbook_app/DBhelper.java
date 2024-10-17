package com.example.cookbook_app;









import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DBhelper extends SQLiteOpenHelper {

    Context context;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "recipe_app.db";

    public DBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables with appropriate columns and data types
        db.execSQL("CREATE TABLE Users (" +

                "username TEXT PRIMARY KEY, " +
                "password TEXT NOT NULL, " + // Store securely (hashed and salted)
                "email TEXT NOT NULL UNIQUE, " +
                "image TEXT , " +
                "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")");

        db.execSQL("CREATE TABLE Recipes (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "categorie TEXT, " +
                "ingredients TEXT, " +
                "instructions TEXT, " +
                "time_coock TEXT, " +
                "niv_difficulty TEXT, " +
                "Kcal INTEGER, " +
                "glucides INTEGER, " +
                "lipides INTEGER, " +
                "proteines INTEGER, " +
                "imageUrl TEXT, " +
                "userId TEXT DEFAULT NULL, " +
                "likes INTEGER DEFAULT 0, " + // Counter for recipe likes
                "isFavorite INTEGER DEFAULT 0, " +
                "video TEXT, " +
                "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "updatedAt DATETIME DEFAULT CURRENT_TIMESTAMP" +


                // Remove the comma here
                ")");// No ON UPDATE CURRENT_TIMESTAMP








        // Table des likes des utilisateurs pour les recettes
        db.execSQL("CREATE TABLE UserLikes (" +
                "userId TEXT, " +
                "recipeId INTEGER, " +
                "PRIMARY KEY (userId, recipeId), " +
                "FOREIGN KEY (userId) REFERENCES Users(username) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (recipeId) REFERENCES Recipes(_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");

        db.execSQL("CREATE TABLE UserFavorites (" +
                "userId TEXT, " +
                "recipeId INTEGER, " +
                "PRIMARY KEY (userId, recipeId), " +
                "FOREIGN KEY (userId) REFERENCES Users(username) ON DELETE CASCADE ON UPDATE CASCADE, " +
                "FOREIGN KEY (recipeId) REFERENCES Recipes(_id) ON DELETE CASCADE ON UPDATE CASCADE" +
                ")");
















    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database upgrade logic if needed
        db.execSQL("DROP TABLE IF EXISTS Recipes");
        onCreate(db);
    }













    public void insertDataFromJSON(InputStream inputStream) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            String jsonString = stringBuilder.toString();

            // Analyser le JSON
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String key1 = jsonObject.getString("nom");
                String key2 = jsonObject.getString("categorie");

                String key3 = jsonObject.getString("ingredients");
                String key4 = jsonObject.getString("preparation");
                String key5 = jsonObject.getString("temps_de_preparation");
                String key6 = jsonObject.getString("niveau_de_difficulte");
                int key7 = jsonObject.getInt("kcal");
                int key8 = jsonObject.getInt("glucides");
                int key9 = jsonObject.getInt("lipides");
                int key10 = jsonObject.getInt("proteines");
                String key11 = jsonObject.getString("image");
                String key12 = jsonObject.getString("auteur");
                String key13 = jsonObject.getString("video");


                Cursor cursor = db.rawQuery("SELECT * FROM Recipes WHERE title = ? AND categorie = ?", new String[]{key1, key2});
                if (cursor.getCount() == 0) {
                    // Insérez la recette si elle n'existe pas

                    db.execSQL("INSERT INTO Recipes (title, categorie, ingredients, instructions, time_coock, niv_difficulty,Kcal,glucides, lipides, proteines, imageUrl, userId, video) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?, ?)",
                            new Object[]{key1, key2, key3, key4, key5, key6, key7, key8, key9, key10, key11, key12, key13});
                }
                cursor.close();




            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }






    /*









    public void insertDataFromJSON(InputStream inputStream) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();

            String jsonString = stringBuilder.toString();

            // Analyser le JSON
            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("nom");
                String category = jsonObject.getString("categorie");

                // Récupérer les ingrédients
                JSONArray ingredientsArray = jsonObject.getJSONArray("ingredients");
                StringBuilder ingredientsBuilder = new StringBuilder();
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    JSONObject ingredientObject = ingredientsArray.getJSONObject(j);
                    String ingredientName = ingredientObject.getString("name");
                    double ingredientQuantity = ingredientObject.optDouble("quantity", 1); // Si la quantité n'est pas fournie, utilisez 1 par défaut
                    ingredientsBuilder.append(ingredientName).append(": ").append(ingredientQuantity).append("\n");
                }
                String ingredients = ingredientsBuilder.toString().trim();

                // Récupérer les étapes de préparation
                JSONArray preparationArray = jsonObject.getJSONArray("preparation");
                StringBuilder preparationBuilder = new StringBuilder();
                for (int k = 0; k < preparationArray.length(); k++) {
                    preparationBuilder.append(preparationArray.getString(k)).append("\n");
                }
                String preparation = preparationBuilder.toString().trim();

                // Récupérer les autres données
                String cookingTime = jsonObject.getString("temps_de_preparation");
                String difficulty = jsonObject.getString("niveau_de_difficulte");
                int kcal = jsonObject.getInt("kcal");
                int glucides = jsonObject.getInt("glucides");
                int lipides = jsonObject.getInt("lipides");
                int proteines = jsonObject.getInt("proteines");
                String imageUrl = jsonObject.getString("image");
                String userId = jsonObject.getString("auteur");

                // Insérer uniquement si la recette n'existe pas déjà
                Cursor cursor = db.rawQuery("SELECT * FROM Recipes WHERE title = ? AND categorie = ?", new String[]{title, category});
                if (cursor.getCount() == 0) {
                    ContentValues values = new ContentValues();
                    values.put("title", title);
                    values.put("categorie", category);
                    values.put("ingredients", ingredients);
                    values.put("instructions", preparation);
                    values.put("time_coock", cookingTime);
                    values.put("niv_difficulty", difficulty);
                    values.put("Kcal", kcal);
                    values.put("glucides", glucides);
                    values.put("lipides", lipides);
                    values.put("proteines", proteines);
                    values.put("imageUrl", imageUrl);
                    values.put("userId", userId);

                    db.insert("Recipes", null, values);
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }












    public List<Recipe> getRecipesByCategory(String category) {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] projection = {
                    "_id",
                    "title",
                    "categorie",
                    "ingredients",
                    "instructions",
                    "time_coock",
                    "niv_difficulty",
                    "imageUrl",
                    "userId"
            };

            String selection = "categorie = ?";
            String[] selectionArgs = {category};

            cursor = db.query(
                    "Recipes",
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Parsez les données du curseur et créez des objets Recipe
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                    String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                    String time_coock = cursor.getString(cursor.getColumnIndexOrThrow("time_coock"));
                    String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));

                    // Ajoutez l'objet Recipe à la liste
                    recipes.add(new Recipe(id, title, ingredients, category, instructions, imageUrl, userId, time_coock, niv_difficulty, 0, false));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return recipes;
    }

*/














}



