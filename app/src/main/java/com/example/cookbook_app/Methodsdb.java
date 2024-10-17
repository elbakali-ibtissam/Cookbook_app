package com.example.cookbook_app;





import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.text.BoringLayout;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Methodsdb {
    DBhelper cn;
    Context context; // Ajout du membre context

    public Methodsdb(Context context) {
        this.context = context; // Affectation du contexte
        cn = new DBhelper(context);
    }


    public boolean insertUser(String username, String password, String email) {
        SQLiteDatabase db = cn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        long result = db.insert("Users", null, values);
        db.close();
        if (result == -1) return false;
        else return true;

    }

    public User getUserById(String userId) {
        SQLiteDatabase db = cn.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM Users WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        if (cursor.moveToFirst()) {
            user = new User();
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            user.setCreatedAt(cursor.getString(cursor.getColumnIndexOrThrow("createdAt")));
            user.setUpdatedAt(cursor.getString(cursor.getColumnIndexOrThrow("updatedAt")));
        }
        cursor.close();
        return user;
    }







    public String getUserImageUri(String username) {
        SQLiteDatabase db = cn.getReadableDatabase();
        String imageUri = null;

        String[] columns = { "image" };
        String selection = "username = ?";
        String[] selectionArgs = { username };

        Cursor cursor = db.query("Users", columns, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow("image"));
            }
            cursor.close();
        }

        return imageUri;
    }











    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = cn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ?", new String[]{username});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = cn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND password = ?", new String[]{username, password});
        boolean isValid = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return isValid;
    }

    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = cn.getWritableDatabase();
        ContentValues valus = new ContentValues();
        valus.put("password", newPassword);
        long result = db.update("Users", valus, "username=?", new String[]{username});
        if (result == -1) return false;
        else return true;


    }

    public void insertRecipe(String title, String ingredients, String category, String instructions,int time, String imageUrl, String userId, int kcal, int glucides, int lipides, int proteines,String level ) {
        SQLiteDatabase db = cn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("ingredients", ingredients);
        values.put("instructions", instructions);
        values.put("time_coock", time);
        values.put("niv_difficulty", level);
        values.put("categorie", category);
        values.put("imageUrl", imageUrl);
        values.put("userId", userId);
        values.put("Kcal", kcal);
        values.put("glucides", glucides);
        values.put("lipides", lipides);
        values.put("proteines", proteines);
        values.put("likes", 0); // Initialise le nombre de likes à 0
        values.put("isFavorite", 0); // Initialise la recette comme non favorite
        values.put("createdAt", getDateTime()); // Utilise une méthode pour obtenir la date et l'heure actuelles
        values.put("updatedAt", getDateTime());
        db.insert("Recipes", null, values);
        db.close();
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }







    public boolean updateRecipe(Recipe recipe) {
        SQLiteDatabase db = cn.getWritableDatabase(); // Assurez-vous d'utiliser le bon contexte ici
        ContentValues values = new ContentValues();
        values.put("title", recipe.getTitle());
        values.put("ingredients", recipe.getIngredients());
        values.put("categorie", recipe.getCategory()); // Change 'category' to 'categorie'
        values.put("instructions", recipe.getInstructions());
        values.put("imageUrl", recipe.getImageUrl());
        values.put("userId", recipe.getUserId());
        values.put("time_coock", recipe.getPreparationTime());
        values.put("niv_difficulty", recipe.getDifficultyLevel());
        values.put("likes", recipe.getLikes());
        values.put("isFavorite", recipe.isFavorite() ? 1 : 0);
        values.put("Kcal", recipe.getKcal());
        values.put("lipides", recipe.getLipides());
        values.put("proteines", recipe.getProteins());
        values.put("glucides", recipe.getGlucides());
        values.put("video", recipe.getVideo());

        int rowsAffected = db.update("Recipes", values, "_id = ?", new String[]{String.valueOf(recipe.getId())});
        db.close();

        return rowsAffected > 0;
    }













    public void deleteRecipe(int recipeId, int userId) {
        SQLiteDatabase db = cn.getWritableDatabase();
        db.delete("Recipes", "id=? AND userId=?", new String[]{String.valueOf(recipeId), String.valueOf(userId)});
        db.close();
    }


    public void addRecipeToFavorites(int recipeId) {
        SQLiteDatabase db = cn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isFavorite", 1); // 1 pour vrai, 0 pour faux
        db.update("Recipes", values, "id=?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public void updateRecipeLikes(int recipeId, int newLikesCount) {
        SQLiteDatabase db = cn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("likes", newLikesCount);
        db.update("Recipes", values, "id=?", new String[]{String.valueOf(recipeId)});
        db.close();
    }


    // Méthode pour ajouter une recette aux favoris d'un utilisateur
    public void addRecipeToFavorites(String userId, int recipeId) {
        SQLiteDatabase db = cn.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("recipeId", recipeId);
        db.insert("UserFavorites", null, values);
        db.close();
    }

    // Méthode pour modifier le nombre de likes par un utilisateur spécifique
    public void updateUserLikes(int userId, int recipeId, boolean like) {
        SQLiteDatabase db = cn.getWritableDatabase();
        if (like) {
            // Ajouter un like
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("recipeId", recipeId);
            db.insert("UserLikes", null, values);
        } else {
            // Retirer le like
            db.delete("UserLikes", "userId=? AND recipeId=?", new String[]{String.valueOf(userId), String.valueOf(recipeId)});
        }
        db.close();
    }

    // Dans votre méthode de recherche, par exemple lors du clic sur un bouton "Rechercher"
    public void searchRecipes(String name_search, String level_difficulty, String category) {
        // Ouvrez la base de données en mode lecture
        SQLiteDatabase db = cn.getReadableDatabase();

        // Construisez la requête SQL en fonction des filtres sélectionnés
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

        // Clause WHERE pour les filtres de catégorie et de niveau de difficulté
        String selection = "1=1"; // Sélectionne tout par défaut

        // Ajoutez la recherche textuelle à la clause WHERE si une recherche est spécifiée
        if (name_search != null && !name_search.isEmpty()) {
            selection += " AND title LIKE '%" + name_search + "%'";
        }

        // Ajoutez le filtre de niveau de difficulté à la clause WHERE
        if (level_difficulty != null && !level_difficulty.isEmpty()) {
            selection += " AND niv_difficulty = '" + level_difficulty + "'";
        }

        // Ajoutez le filtre de catégorie à la clause WHERE
        if (category != null && !category.isEmpty()) {
            selection += " AND categorie = '" + category + "'";
        }

        // Effectuez la requête dans la base de données
        Cursor cursor = db.query(
                "Recipes", // Table à interroger
                projection, // Colonnes à retourner
                selection, // Clause WHERE
                null, // Arguments de la clause WHERE
                null, // GROUP BY
                null, // HAVING
                null // ORDER BY
        );

        // Traitez les résultats de la requête
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String categorie = cursor.getString(cursor.getColumnIndexOrThrow("categorie"));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                int time_coock = cursor.getInt(cursor.getColumnIndexOrThrow("time_coock"));
                String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));

                // Affichez les informations de chaque recette dans la console
                Log.d("Recipe", "ID: " + id);
                Log.d("Recipe", "Title: " + title);
                Log.d("Recipe", "Categorie: " + categorie);
                Log.d("Recipe", "Ingredients: " + ingredients);
                Log.d("Recipe", "Instructions: " + instructions);
                Log.d("Recipe", "Time Coock: " + time_coock);
                Log.d("Recipe", "Niveau Difficulty: " + niv_difficulty);
                Log.d("Recipe", "Image URL: " + imageUrl);
                Log.d("Recipe", "User ID: " + userId);

                // Il y a des recettes correspondant aux critères de recherche
                // Traitez les résultats ici, par exemple, affichez-les dans un RecyclerView
            }
        } else {
            // Aucune recette correspondant aux critères de recherche n'a été trouvée
            Log.e(TAG, "Aucune recette correspondante trouvée");
        }

        // Fermez le curseur et la base de données
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    /*
        public Cursor getRecipesByCategorie(String category) {
            SQLiteDatabase db = cn.getReadableDatabase();

            String query = "SELECT title, categorie, ingredients, instructions, time_coock, niv_difficulty, imageUrl, userId, likes FROM Recipes WHERE categorie = ?";
            return db.rawQuery(query, new String[]{category});
        }
    */
    public List<Recipe> getRecipesByCategorie(String category) {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = cn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, title, categorie, ingredients, instructions, time_coock, niv_difficulty, imageUrl, userId, likes, isFavorite , Kcal, glucides, lipides, proteines,video FROM Recipes WHERE categorie = ?", new String[]{category});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                int time_coock = cursor.getInt(cursor.getColumnIndexOrThrow("time_coock"));
                String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
                int likes = cursor.getInt(cursor.getColumnIndexOrThrow("likes"));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1;
                int kcal = cursor.getInt(cursor.getColumnIndexOrThrow("Kcal"));

                String vid =cursor.getString(cursor.getColumnIndexOrThrow("video"));
                int glucides = cursor.getInt(cursor.getColumnIndexOrThrow("glucides"));
                int lipides = cursor.getInt(cursor.getColumnIndexOrThrow("lipides"));
                int proteins = cursor.getInt(cursor.getColumnIndexOrThrow("proteines"));
                // Créer une instance de Recipe et l'ajouter à la liste
                Recipe recipe = new Recipe(id, title, ingredients, category, instructions, imageUrl, userId, time_coock, niv_difficulty, likes, isFavorite, kcal, lipides, proteins, glucides, vid);
                recipes.add(recipe);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recipes;
    }


    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = cn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Recipes", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                int time_coock = cursor.getInt(cursor.getColumnIndexOrThrow("time_coock"));
                String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
                int likes = cursor.getInt(cursor.getColumnIndexOrThrow("likes"));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1;
                int kcal = cursor.getInt(cursor.getColumnIndexOrThrow("Kcal"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("categorie"));
                String vid =cursor.getString(cursor.getColumnIndexOrThrow("video"));
                int glucides = cursor.getInt(cursor.getColumnIndexOrThrow("glucides"));
                int lipides = cursor.getInt(cursor.getColumnIndexOrThrow("lipides"));
                int proteins = cursor.getInt(cursor.getColumnIndexOrThrow("proteines"));
                recipes.add(new Recipe(id, title, ingredients, category, instructions, imageUrl, userId, time_coock, niv_difficulty, likes, isFavorite, kcal, lipides, proteins, glucides, vid));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.i("TEST", String.valueOf(recipes.size()));
        return recipes;
    }


    public Recipe getRecipeById(int id) {
        Recipe recipe = null;
        SQLiteDatabase db = cn.getReadableDatabase();
        String query = "SELECT * FROM recipes WHERE _id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
            String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
            int time_coock = cursor.getInt(cursor.getColumnIndexOrThrow("time_coock"));
            String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
            String userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            int likes = cursor.getInt(cursor.getColumnIndexOrThrow("likes"));
            boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1;
            int kcal = cursor.getInt(cursor.getColumnIndexOrThrow("Kcal"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("categorie"));

            int glucides = cursor.getInt(cursor.getColumnIndexOrThrow("glucides"));
            int lipides = cursor.getInt(cursor.getColumnIndexOrThrow("lipides"));
            int proteins = cursor.getInt(cursor.getColumnIndexOrThrow("proteines"));
            String vid =cursor.getString(cursor.getColumnIndexOrThrow("video"));
            recipe = new Recipe(id, title, ingredients, category, instructions, imageUrl, userId, time_coock, niv_difficulty, likes, isFavorite, kcal, lipides, proteins, glucides, vid);
        }
        cursor.close();
        return recipe;
    }





    public String getRecipeVideo(int recipeId, String userId) {
        // Vérifiez d'abord si l'ID de l'utilisateur est vide
        if (userId.isEmpty()) {
            // Si l'ID de l'utilisateur est vide, la recette est ajoutée par l'utilisateur actuel
            // Vous pouvez gérer cela selon votre logique métier, par exemple, vous pouvez retourner null ou une chaîne vide
            return null;
        } else {
            // Si l'ID de l'utilisateur n'est pas vide, la recette est ajoutée par un autre utilisateur
            SQLiteDatabase db = cn.getReadableDatabase();
            String videoLink = null;
            Cursor cursor = db.rawQuery("SELECT video FROM Recipes WHERE _id = ?", new String[]{String.valueOf(recipeId)});
            if (cursor != null && cursor.moveToFirst()) {
                videoLink = cursor.getString(cursor.getColumnIndexOrThrow("video"));
                cursor.close();
            }
            return videoLink;
        }
    }


    public String getRecipeUserId(int recipeId) {
        SQLiteDatabase db = cn.getReadableDatabase();
        String userId = null;
        Cursor cursor = db.rawQuery("SELECT userId FROM Recipes WHERE _id = ?", new String[]{String.valueOf(recipeId)});
        if (cursor != null && cursor.moveToFirst()) {
            userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
            cursor.close();
        }
        return userId;
    }















    public void addUserFavorite(String userId, int recipeId) {
        SQLiteDatabase db = cn.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("recipeId", recipeId);
        db.insert("UserFavorites", null, values);
    }

    public void addUserLike(String userId, int recipeId) {
        SQLiteDatabase db = cn.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("recipeId", recipeId);
        db.insert("UserLikes", null, values);
    }
    public void addLikeToRecipe(int recipeId) {
        SQLiteDatabase db = cn.getWritableDatabase();
        db.execSQL("UPDATE Recipes SET likes = likes + 1 WHERE _id = " + recipeId);
        db.close();
    }



    public List<Integer> getFavoriteRecipeIds(String userId) {
        List<Integer> favoriteRecipeIds = new ArrayList<>();
        SQLiteDatabase db = cn.getReadableDatabase();
        Log.e("getFavoriteRecipeIds", "Querying for userId: " + userId);

        Cursor cursor = db.rawQuery("SELECT recipeId FROM UserFavorites WHERE userId = ?", new String[]{userId});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int recipeId = cursor.getInt(cursor.getColumnIndexOrThrow("recipeId"));
                    favoriteRecipeIds.add(recipeId);
                } while (cursor.moveToNext());
            } else {
                Log.e("getFavoriteRecipeIds", "Cursor is empty for userId: " + userId);
            }
            cursor.close();
        } else {
            Log.e("getFavoriteRecipeIds", "Cursor is null for userId: " + userId);
        }

        db.close();
        Log.e("REcipesid", "Favorite Recipe IDs: " + favoriteRecipeIds.toString());
        return favoriteRecipeIds;
    }
























    public List<Recipe> getFavoriteRecipes(String userId) {
        List<Recipe> favoriteRecipes = new ArrayList<>();
        List<Integer> favoriteRecipeIds = this.getFavoriteRecipeIds(userId);

        if (favoriteRecipeIds.isEmpty()) {
            Log.e("getFavoriteRecipes", "No favorite recipes found for userId: " + userId);
            return favoriteRecipes; // Return an empty list
        }

        SQLiteDatabase db = cn.getReadableDatabase();
        String recipeIds = TextUtils.join(",", favoriteRecipeIds); // Convertir la liste d'ID en une chaîne de caractères séparée par des virgules
        Log.e("getFavoriteRecipes", "Querying for recipeIds: " + recipeIds);

        Cursor cursor = db.rawQuery("SELECT * FROM Recipes WHERE _id IN (" + recipeIds + ")", null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                    String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                    int time_coock = cursor.getInt(cursor.getColumnIndexOrThrow("time_coock"));
                    String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                    String userid = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
                    int likes = cursor.getInt(cursor.getColumnIndexOrThrow("likes"));
                    boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1;
                    int kcal = cursor.getInt(cursor.getColumnIndexOrThrow("Kcal"));
                    String category = cursor.getString(cursor.getColumnIndexOrThrow("categorie"));
                    String vid =cursor.getString(cursor.getColumnIndexOrThrow("video"));
                    int glucides = cursor.getInt(cursor.getColumnIndexOrThrow("glucides"));
                    int lipides = cursor.getInt(cursor.getColumnIndexOrThrow("lipides"));
                    int proteins = cursor.getInt(cursor.getColumnIndexOrThrow("proteines"));
                    Recipe recipe = new Recipe(id, title, ingredients, category, instructions, imageUrl, userid, time_coock, niv_difficulty, likes, isFavorite, kcal, lipides, proteins, glucides, vid);

                    // Ajoutez cette recette à la liste des recettes favorites
                    favoriteRecipes.add(recipe);
                } while (cursor.moveToNext());
            } else {
                Log.e("getFavoriteRecipes", "Cursor is empty for recipeIds: " + recipeIds);
            }
            cursor.close();
        } else {
            Log.e("getFavoriteRecipes", "Cursor is null for recipeIds: " + recipeIds);
        }

        db.close();
        Log.e("REcipes", "Favorite Recipes: " + favoriteRecipes);
        return favoriteRecipes;
    }


    public boolean isRecipeFavorite(String userId, int recipeId) {
        SQLiteDatabase db = cn.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM UserFavorites WHERE userId = ? AND recipeId = ?", new String[]{userId, String.valueOf(recipeId)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public void removeFavoriteRecipe(int recipeId, String userId) {
        SQLiteDatabase db = cn.getWritableDatabase();
        db.delete("UserFavorites", "recipeId = ? AND userId = ?", new String[]{String.valueOf(recipeId), userId});
        db.close();
    }





    public List<Recipe> getRecipesByFilter(String glucides, String calories, String tempsPreparation, String difficulte) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        SQLiteDatabase db = cn.getReadableDatabase();

        // Définir la requête SQL avec les conditions de filtrage
        String selectQuery = "SELECT * FROM Recipes WHERE glucides <= ? AND calories <= ? AND temps_preparation <= ? AND difficulte = ?";

        // Exécuter la requête SQL avec les valeurs de filtrage
        Cursor cursor = db.rawQuery(selectQuery, new String[]{glucides, calories, tempsPreparation, difficulte});

        // Parcourir le curseur et ajouter les recettes filtrées à la liste
        if (cursor.moveToFirst()) {
            do {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                int time_coock = cursor.getInt(cursor.getColumnIndexOrThrow("time_coock"));
                String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
                int likes = cursor.getInt(cursor.getColumnIndexOrThrow("likes"));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1;
                int kcal = cursor.getInt(cursor.getColumnIndexOrThrow("Kcal"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("categorie"));
                String vid =cursor.getString(cursor.getColumnIndexOrThrow("video"));
                int glucide = cursor.getInt(cursor.getColumnIndexOrThrow("glucides"));
                int lipides = cursor.getInt(cursor.getColumnIndexOrThrow("lipides"));
                int proteins = cursor.getInt(cursor.getColumnIndexOrThrow("proteines"));
                Recipe recipe = new Recipe(id, title, ingredients, category, instructions, imageUrl, userId, time_coock, niv_difficulty, likes, isFavorite, kcal, lipides, proteins, glucide,vid);
                // Ajouter la recette à la liste filtrée
                filteredRecipes.add(recipe);
            } while (cursor.moveToNext());
        }

        // Fermer le curseur et retourner la liste filtrée de recettes
        cursor.close();
        db.close();
        Log.e("filtr", "db : "+filteredRecipes);
        return filteredRecipes;
    }




    public boolean isRecipeLiked( int recipeId) {
        SQLiteDatabase db = cn.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM UserLikes WHERE  recipeId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{ String.valueOf(recipeId)});
        boolean isLiked = false;
        if (cursor.moveToFirst()) {
            isLiked = cursor.getInt(0) > 0;
        }
        cursor.close();
        return isLiked;
    }

    public int getRecipeLikes(int recipeId) {
        SQLiteDatabase db = cn.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM UserLikes WHERE recipeId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(recipeId)});
        int likesCount = 0;
        if (cursor.moveToFirst()) {
            likesCount = cursor.getInt(0);
        }
        cursor.close();
        return likesCount;
    }









    public String getImagePath(String userId) {
        String imagePath = "";
        SQLiteDatabase db = cn.getReadableDatabase(); // Supposons que "cn" est votre instance de classe de connexion à la base de données

        // Exécuter la requête SQL pour récupérer le chemin d'accès à l'image de l'utilisateur
        String query = "SELECT image FROM Users WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        // Vérifier si le curseur contient des données
        if (cursor != null && cursor.moveToFirst()) {
            // Récupérer l'indice de la colonne "image_path"
            int imagePathIndex = cursor.getColumnIndex("image");

            // Vérifier si l'indice de la colonne est valide
            if (imagePathIndex != -1) {
                // Extraire le chemin d'accès à l'image à partir du curseur
                imagePath = cursor.getString(imagePathIndex);
            }
        }

        // Fermer le curseur et la base de données
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return imagePath;
    }










    // Add or remove a like for the user
    public void toggleUserLike(String userId, int recipeId) {
        SQLiteDatabase db = cn.getReadableDatabase();

        if (isRecipeLiked( recipeId)) {
            // Remove like
            String deleteQuery = "DELETE FROM UserLikes WHERE userId = ? AND recipeId = ?";
            db.execSQL(deleteQuery, new Object[]{userId, recipeId});

            // Update the number of likes in the Recipes table
            String updateQuery = "UPDATE Recipes SET likes = likes - 1 WHERE _id = ?";
            db.execSQL(updateQuery, new Object[]{recipeId});
        } else {
            // Add like
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("recipeId", recipeId);
            db.insert("UserLikes", null, values);

            // Update the number of likes in the Recipes table
            String updateQuery = "UPDATE Recipes SET likes = likes + 1 WHERE _id = ?";
            db.execSQL(updateQuery, new Object[]{recipeId});
        }
    }






    public List<Recipe> getLikedRecipesByUser(String userId) {
        List<Recipe> likedRecipes = new ArrayList<>();
        SQLiteDatabase db = cn.getReadableDatabase();

        String query = "SELECT Recipes.* FROM Recipes " +
                "INNER JOIN UserLikes ON Recipes._id = UserLikes.recipeId " +
                "WHERE UserLikes.userId = ?";

        Cursor cursor = db.rawQuery(query, new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                int time_coock = cursor.getInt(cursor.getColumnIndexOrThrow("time_coock"));
                String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                String userid = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
                int likes = cursor.getInt(cursor.getColumnIndexOrThrow("likes"));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1;
                int kcal = cursor.getInt(cursor.getColumnIndexOrThrow("Kcal"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("categorie"));
                String vid =cursor.getString(cursor.getColumnIndexOrThrow("video"));
                int glucide = cursor.getInt(cursor.getColumnIndexOrThrow("glucides"));
                int lipides = cursor.getInt(cursor.getColumnIndexOrThrow("lipides"));
                int proteins = cursor.getInt(cursor.getColumnIndexOrThrow("proteines"));
                Recipe recipe = new Recipe(id, title, ingredients, category, instructions, imageUrl, userid, time_coock, niv_difficulty, likes, isFavorite, kcal, lipides, proteins, glucide, vid);
                // Ajouter la recette à la liste filtrée

                likedRecipes.add(recipe);

            } while (cursor.moveToNext());
        }
        cursor.close();
        Log.e("liked recipes", "likes : "+likedRecipes);
        return likedRecipes;
    }

    public void deleteRecipe(int recipeId) {
        SQLiteDatabase db = cn.getWritableDatabase();
        db.delete("Recipes", "_id = ?", new String[]{String.valueOf(recipeId)});
        db.close();
    }

    public List<Recipe> getRecipesCreatedByUser(String userId) {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = cn.getReadableDatabase();
        Cursor cursor = db.query("Recipes", null, "userId=?", new String[]{userId}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                int time_coock = cursor.getInt(cursor.getColumnIndexOrThrow("time_coock"));
                String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                String userid = cursor.getString(cursor.getColumnIndexOrThrow("userId"));
                int likes = cursor.getInt(cursor.getColumnIndexOrThrow("likes"));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1;
                int kcal = cursor.getInt(cursor.getColumnIndexOrThrow("Kcal"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("categorie"));
                String vid =cursor.getString(cursor.getColumnIndexOrThrow("video"));
                int glucide = cursor.getInt(cursor.getColumnIndexOrThrow("glucides"));
                int lipides = cursor.getInt(cursor.getColumnIndexOrThrow("lipides"));
                int proteins = cursor.getInt(cursor.getColumnIndexOrThrow("proteines"));
                Recipe recipe = new Recipe(id, title, ingredients, category, instructions, imageUrl, userid, time_coock, niv_difficulty, likes, isFavorite, kcal, lipides, proteins, glucide, vid);





                recipeList.add(recipe);
            }
            cursor.close();
        }

        return recipeList;
    }












}





















// Other methods...

// Check if the user has already liked the recipe



/*

    public List<Recipe> getRecipesByCategory(String category) {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = cn.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Colonnes à récupérer
            String[] projection = {
                    "_id",
                    "title",
                    "categorie",
                    "ingredients",
                    "instructions",
                    "time_coock",
                    "niv_difficulty",
                    "imageUrl",
                    "userId",
                    "likes",
                    "isFavorite"
            };

            // Condition de sélection : catégorie spécifiée
            String selection = "categorie = ?";
            String[] selectionArgs = { category };

            cursor = db.query(
                    "Recipes",      // Table à interroger
                    projection,     // Colonnes à retourner
                    selection,      // Clause WHERE : catégorie = ?
                    selectionArgs,  // Arguments de la clause WHERE
                    null,           // GROUP BY
                    null,           // HAVING
                    null            // ORDER BY
            );

            // Parcourir le curseur et ajouter les recettes à la liste
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                    String ingredients = cursor.getString(cursor.getColumnIndexOrThrow("ingredients"));
                    String instructions = cursor.getString(cursor.getColumnIndexOrThrow("instructions"));
                    String time_coock = cursor.getString(cursor.getColumnIndexOrThrow("time_coock"));
                    String niv_difficulty = cursor.getString(cursor.getColumnIndexOrThrow("niv_difficulty"));
                    String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                    int userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId"));
                    int likes = cursor.getInt(cursor.getColumnIndexOrThrow("likes"));
                    boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow("isFavorite")) == 1;

                    // Créer une instance de Recipe et l'ajouter à la liste
                    Recipe recipe = new Recipe(id, title, ingredients, category, instructions, imageUrl, userId, time_coock, niv_difficulty, likes, isFavorite);
                    recipes.add(recipe);

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


/*





}












































/*

    void insertDataIntoSQLite(String jsonString) {


        try {
            JSONArray jsonArray = new JSONArray(jsonString); // jsonString est le contenu de votre fichier JSON
            SQLiteDatabase db = cn.getWritableDatabase();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Récupérer les valeurs du JSON
                String id = jsonObject.getString("id");
                String title = jsonObject.getString("nom");
                String categorie = jsonObject.getString("categorie");
                String ingredients = jsonObject.getString("ingredients");
                String instructions = jsonObject.getString("preparation");
                String time_coock = jsonObject.getString("temps_de_preparation");
                String niv_difficulty = jsonObject.getString("niveau_de_difficulte");
                String imageUrl = jsonObject.getString("image");

                // Insérer les données dans la table Recipes
                ContentValues values = new ContentValues();
                values.put("_id", id);
                values.put("title", title);
                values.put("categorie", categorie);
                values.put("ingredients", ingredients);
                values.put("instructions", instructions);
                values.put("time_coock", time_coock);
                values.put("niv_difficulty", niv_difficulty);
                values.put("imageUrl", imageUrl);

                long newRowId = db.insert("Recipes", null, values); // db est votre instance de SQLiteDatabase
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String readJsonFromAssets(Context context, String filename) throws IOException {
        InputStream stream = context.getAssets().open(filename);
        int size = stream.available();
        byte[] buffer = new byte[size];
        stream.read(buffer);
        stream.close();
        return new String(buffer, "UTF-8");
    }


    */













