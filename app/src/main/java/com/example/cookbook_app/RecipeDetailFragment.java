package com.example.cookbook_app;



import static androidx.core.app.NotificationCompat.getColor;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeDetailFragment extends Fragment {
    private static final String ARG_RECIPE_ID = "recipe_id";
    private Recipe mRecipe;
    private int mRecipeId;
    private String mUserId;

    public RecipeDetailFragment() {

    }

    public static RecipeDetailFragment newInstance(int recipeId, String userId) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putString("USERNAME", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserId = getArguments().getString("USERNAME");

            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
            Methodsdb dbHelper = new Methodsdb(getContext());
            mRecipe = dbHelper.getRecipeById(mRecipeId);
        }
        // SharedPreferences sharedPref = getActivity().getSharedPreferences("myFile", Context.MODE_PRIVATE);
        // mUserId = sharedPref.getString("username", "cant");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        initializeUI(view);
        return view;
    }

    private void initializeUI(View view) {
        ImageView saveButton = view.findViewById(R.id.zoom_image);
        ImageView backButton = view.findViewById(R.id.back_btn);
        Methodsdb methodsdb = new Methodsdb(getContext());
        ImageView like =view.findViewById(R.id.like);
        String userId = mUserId;
        int recipeId = mRecipeId;

        TextView auteur = view.findViewById(R.id.auteur);

        TextView likesTextView = view.findViewById(R.id.count_likes);
        likesTextView.setText(String.valueOf(methodsdb.getRecipeLikes(recipeId)));
// Check if the recipe is already liked by the user

        boolean isLiked = methodsdb.isRecipeLiked( recipeId);

// Change the drawable color based on the like status
        if (isLiked) {
            like.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite));
        } else {
            like.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_unfavorite));
        }

    // Toggle the like status when the like button is clicked
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the like status
                methodsdb.toggleUserLike(userId, recipeId);

                // Get the updated like status
                boolean isLiked = methodsdb.isRecipeLiked(recipeId);

                // Change the drawable color based on the new like status
                if (isLiked) {
                    like.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_favorite));
                    Toast.makeText(getContext(), "Recipe Liked", Toast.LENGTH_SHORT).show();



                    likesTextView.setText(String.valueOf(methodsdb.getRecipeLikes(recipeId)));
                } else {
                    like.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_unfavorite));
                    Toast.makeText(getContext(), "Like removed!", Toast.LENGTH_SHORT).show();



                    likesTextView.setText(String.valueOf(methodsdb.getRecipeLikes(recipeId)));
                }


            }
        });




        boolean isFavorite = methodsdb.isRecipeFavorite(mUserId, mRecipe.getId());


        if (isFavorite) {
            saveButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.saved));
        } else {
            saveButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.outline_bookmark_border_24));
        }


        saveButton.setOnClickListener(v -> {



            if (isFavorite) {
                saveButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.outline_bookmark_border_24));
                methodsdb.removeFavoriteRecipe(mRecipe.getId(), mUserId);

            } else {
                saveButton.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.saved));
                saveRecipe(mUserId, mRecipeId);

            }
        });





        ImageView img_share_recipe_video =view.findViewById(R.id.img_share_recipe_video);
        String userid_recipe =methodsdb.getRecipeUserId(recipeId);
        // Récupérer le lien de la colonne vidéo de la table Recipes pour la recette actuellement affichée
        String videoUrl = methodsdb.getRecipeVideo(recipeId, userId);

        if(userid_recipe.equals("")){
            img_share_recipe_video.setVisibility(View.VISIBLE);
            img_share_recipe_video.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userid_recipe.equals("")){
                        openYouTubeVideo(videoUrl);
                    }
                    // Remplacez VIDEO_ID_HERE par l'ID de la vidéo YouTube

                }
            });
        }else{
            img_share_recipe_video.setVisibility(View.GONE);
        }
























        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        if (mRecipe != null) {
            TextView titleTextView = view.findViewById(R.id.tittle);
            TextView levelDiffTextView = view.findViewById(R.id.time5);
            TextView authorTextView = view.findViewById(R.id.auteur);
            TextView category = view.findViewById(R.id.time4);
            TextView timeTextView = view.findViewById(R.id.time3);
            //TextView caloriTextView = view.findViewById(R.id.time7);

            titleTextView.setText(mRecipe.getTitle());


            category.setText(String.valueOf(mRecipe.getCategory()));
            timeTextView.setText(mRecipe.getPreparationTime() + " min");
            levelDiffTextView.setText(mRecipe.getDifficultyLevel());
            //.setText(String.valueOf(mRecipe.getKcal()));

            ImageView imageView = view.findViewById(R.id.recipe_img);
            Picasso.get().load(mRecipe.getImageUrl()).into(imageView);

            Button stepBtn = view.findViewById(R.id.steps_btn);
            Button ingBtn = view.findViewById(R.id.ing_btn);
            ScrollView scrollViewIngredients = view.findViewById(R.id.ing_scroll);
            ScrollView scrollViewSteps = view.findViewById(R.id.steps);

            String createur =mRecipe.getUserId();
            if(!createur.equals("")){
                authorTextView.setText("by " + mRecipe.getUserId());
            }



            ScrollView ingredientsScrollView = view.findViewById(R.id.ing_scroll);
            TextView ingredientsTextView = view.findViewById(R.id.ing);
            ScrollView stepsScrollView = view.findViewById(R.id.steps);
            TextView stepsTextView = view.findViewById(R.id.steps_txt);

            // Récupérer les ingrédients et les étapes de la recette
            String ingredients = mRecipe.getIngredients();
            String steps = mRecipe.getInstructions();


            ingredientsTextView.setText(ingredients);
            stepsTextView.setText(steps);


            stepsScrollView.setVisibility(View.GONE);
            ingredientsScrollView.setVisibility(View.VISIBLE);
            ImageView img_share_recipe = view.findViewById(R.id.img_share_recipe);
            img_share_recipe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareRecipe(recipeId);
                }
            });

















            stepBtn.setOnClickListener(v -> {



                stepBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.chibi));
                ingBtn.setBackground(null);
                ingBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

                scrollViewIngredients.setVisibility(View.GONE);
                scrollViewSteps.setVisibility(View.VISIBLE);
            });

            ingBtn.setOnClickListener(v -> {

               // ingBtn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.brown));
                ingBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.chibi));
                stepBtn.setBackground(null);
                stepBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));


                scrollViewIngredients.setVisibility(View.VISIBLE);
                scrollViewSteps.setVisibility(View.GONE);
            });
        } else {
            Log.e("RecipeDetailFragment", "Recipe is null");
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void saveRecipe(String userId, int recipeId) {
        Methodsdb methodsdb = new Methodsdb(getContext());
        methodsdb.addUserFavorite(userId, recipeId);
    }

    private void likeRecipe(String userId, int recipeId) {
        Methodsdb methodsdb = new Methodsdb(getContext());
        methodsdb.addUserLike(userId, recipeId);
        methodsdb.addLikeToRecipe(recipeId);
    }














    private void openYouTubeVideo(String videoId) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(videoId));
        try {
            startActivity(appIntent); // Ouvrir dans l'application YouTube s'il est installé
        } catch (Exception ex) {
            startActivity(webIntent); // Ouvrir dans un navigateur Web si l'application YouTube n'est pas installée
        }
    }











    private void shareRecipe(int recipeid) {
        Methodsdb methodsdb = new Methodsdb(getContext());
        // Récupérer la recette à partir de la base de données en utilisant son ID
        Recipe recipe = methodsdb.getRecipeById(recipeid);

        if (recipe != null) {
            // Construire le texte à partager en utilisant les informations de la recette
            StringBuilder recipeText = new StringBuilder();
            recipeText.append("Titre: ").append(recipe.getTitle()).append("\n\n");
            recipeText.append("Ingrédients: ").append(recipe.getIngredients()).append("\n\n");
            recipeText.append("Méthode: ").append(recipe.getInstructions());

            // Créer l'intent de partage
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, recipeText.toString());
            sendIntent.setType("text/plain");

            // Vérifier s'il y a une application disponible pour gérer le partage
            if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Afficher la boîte de dialogue de choix de l'application de partage
                startActivity(Intent.createChooser(sendIntent, "Partager la recette"));
            } else {
                // Aucune application disponible pour gérer le partage
                //Toast.makeText(getContext(), "Aucune application disponible pour gérer le partage", Toast.LENGTH_SHORT).show();
            }
        } else {
            // La recette n'a pas été trouvée dans la base de données
            // Toast.makeText(getContext(), "Recette introuvable", Toast.LENGTH_SHORT).show();
        }
    }









}
