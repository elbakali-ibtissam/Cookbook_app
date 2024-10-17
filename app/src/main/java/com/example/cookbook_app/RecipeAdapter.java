package com.example.cookbook_app;




import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.recipe_item_layout, parent, false);

        }
 */











public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private List<Recipe> mRecipeList;
    private LayoutInflater inflater;
    private List<Recipe> filteredRecipes;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
        mRecipeList = recipes;
        this.filteredRecipes = new ArrayList<>(recipes);
        this.inflater = LayoutInflater.from(context);
    }
    public List<Recipe> getRecipes() {
        return mRecipeList;
    }

    private static class ViewHolder {
        TextView recipeNameTextView;
        ImageView recipeImageView;
        TextView likesTextView;
        TextView timeTextView;
        TextView levelTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.from(getContext()).inflate(R.layout.recipe_item_layout, parent, false);
            holder = new ViewHolder();
            holder.recipeNameTextView = convertView.findViewById(R.id.custom_item_name);
            holder.recipeImageView = convertView.findViewById(R.id.bg);
            holder.likesTextView = convertView.findViewById(R.id.txt_like);
            holder.timeTextView = convertView.findViewById(R.id.txt_time);
            holder.levelTextView = convertView.findViewById(R.id.txt_level);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recipe currentRecipe = getItem(position);
        if (currentRecipe != null) {
            holder.recipeNameTextView.setText(currentRecipe.getTitle());
            holder.likesTextView.setText(String.valueOf(currentRecipe.getLikes()));
            holder.timeTextView.setText((String.valueOf(currentRecipe.getPreparationTime())) +" min");
            holder.levelTextView.setText(currentRecipe.getDifficultyLevel());

            Picasso.get().load(currentRecipe.getImageUrl()).into(holder.recipeImageView);
            Log.e("RecipeAdapter", "Title: " + currentRecipe.getTitle() + ", Position: " + position);
        } else {
            Log.e("RecipeAdapter", "currentRecipe est null pour la position " + position);
        }

        return convertView;
    }

    public List<Recipe> filter(String searchText, List<Recipe> recipes) {
        searchText = searchText.toLowerCase(Locale.getDefault());
        filteredRecipes.clear();
        if (searchText.length() == 0) {
            filteredRecipes.addAll(recipes);
        } else {
            for (Recipe recipe : recipes) {
                if (recipe.getTitle().toLowerCase(Locale.getDefault()).contains(searchText)) {
                    filteredRecipes.add(recipe);
                }
            }
        }

        notifyDataSetChanged(); // Mettre à jour la vue après le filtrage
        return filteredRecipes;
    }

}

