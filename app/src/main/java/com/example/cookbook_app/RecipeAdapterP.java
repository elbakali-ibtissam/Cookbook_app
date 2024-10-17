package com.example.cookbook_app;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapterP extends ArrayAdapter<Recipe> {

    private List<Recipe> mRecipeList;
    private LayoutInflater inflater;

    public RecipeAdapterP(Context context, List<Recipe> recipes) {
        super(context, 0, recipes);
        mRecipeList = new ArrayList<>(recipes);
        this.inflater = LayoutInflater.from(context);
    }

    private static class ViewHolder {
        TextView recipeNameTextView;
        ImageView recipeImageView;
        TextView timeTextView;
        ImageView deleteButton;
        ImageView edit_button;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;


        Methodsdb methodsdb =new Methodsdb(getContext());


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_recipe_created, parent, false);
            holder = new ViewHolder();
            holder.recipeNameTextView = convertView.findViewById(R.id.txt1);
            holder.recipeImageView = convertView.findViewById(R.id.img);
            holder.timeTextView = convertView.findViewById(R.id.time);
            holder.deleteButton = convertView.findViewById(R.id.supp);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Recipe recipe = getItem(position);
        if (recipe != null) {
            holder.recipeNameTextView.setText(recipe.getTitle());
            holder.timeTextView.setText(recipe.getPreparationTime() + " mins");
            Picasso.get().load(recipe.getImageUrl()).into(holder.recipeImageView);
        }


        holder.deleteButton.setOnClickListener(v -> {

            LayoutInflater inflater = LayoutInflater.from(getContext());

            View dialogView = inflater.inflate(R.layout.dialog_confirmation, null);


            Button btnYes = dialogView.findViewById(R.id.btn_yes);
            Button btnNo = dialogView.findViewById(R.id.btn_no);

            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setView(dialogView)
                    .create();


            btnYes.setOnClickListener(v1 -> {
                methodsdb.deleteRecipe(recipe.getId());
                mRecipeList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(getContext(), "Recette supprimée", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            });


            btnNo.setOnClickListener(v2 -> alertDialog.dismiss());


            alertDialog.show();
        });


















        holder.edit_button = convertView.findViewById(R.id.card_btn);
        holder.edit_button.setOnClickListener(v -> {


            EditRecipeFragment editFragment = new EditRecipeFragment();

            // Passer l'ID de la recette en tant qu'argument au fragment
            Bundle bundle = new Bundle();
            bundle.putInt("recipe_id", recipe.getId());
            editFragment.setArguments(bundle);

            // Ouvrir le fragment RecipeEditFragment
            FragmentTransaction transaction = ((AppCompatActivity) getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.host_Frag, editFragment);
            transaction.addToBackStack(null);
            transaction.commit();

        });



        return convertView;
    }
}