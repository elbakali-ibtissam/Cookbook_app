package com.example.cookbook_app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Uri selectedImageUri;
    private String mParam1;
    private String mParam2;



    private int recipeId;
    private Recipe recipe;

    private EditText nameEditText;
    private EditText ingredientsEditText;
    private EditText methodEditText;
    private EditText timeEditText;
    private EditText caloriesEditText;
    private EditText proteinsEditText;
    private EditText lipidesEditText;
    private EditText glucidesEditText;
    private ImageView photoImageView;
    private Button saveButton;
    private ChipGroup chip_group;
    private ChipGroup chip_group1;

    private  Button btnImportImage;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView photo_placeholder;










    public EditRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditRecipeFragment newInstance(String param1, String param2) {
        EditRecipeFragment fragment = new EditRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_recipe, container, false);
    }









    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Récupérer l'identifiant de la recette à partir des arguments
        if (getArguments() != null) {
            recipeId = getArguments().getInt("recipe_id", -1);
        }

        // Initialiser les vues
        nameEditText = view.findViewById(R.id.name_add_new);
        ingredientsEditText = view.findViewById(R.id.ing_add);
        methodEditText = view.findViewById(R.id.methode_add);
        timeEditText = view.findViewById(R.id.time_add);
        caloriesEditText = view.findViewById(R.id.calo_add);
        proteinsEditText = view.findViewById(R.id.proteins_add);
        lipidesEditText = view.findViewById(R.id.lipides_add);
        glucidesEditText = view.findViewById(R.id.glucides);

        saveButton = view.findViewById(R.id.btn_save_recipe);
        chip_group =view.findViewById(R.id.chip_group);
        chip_group1 = view.findViewById(R.id.chip_group1);
        btnImportImage = view.findViewById(R.id.btn_scatta);
        photo_placeholder =view.findViewById(R.id.photo_placeholder);



        btnImportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        // Remplir les champs avec les détails de la recette à modifier
        fillFields();

        // Gérer le clic sur le bouton d'enregistrement
        saveButton.setOnClickListener(v -> saveChanges());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Méthode pour gérer le résultat de la sélection d'image



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();

            if (selectedImageUri != null) {
                // Afficher l'image dans l'ImageView
                Log.e("image", "image Not Null");
                photo_placeholder.setImageURI(selectedImageUri);
            }
        }
    }






    private void fillFields() {
        // Récupérer la recette à partir de la base de données
        Methodsdb methodsdb = new Methodsdb(getContext());
        recipe = methodsdb.getRecipeById(recipeId);

        // Remplir les champs avec les détails de la recette
        if (recipe != null) {
            nameEditText.setText(recipe.getTitle());
            ingredientsEditText.setText(recipe.getIngredients());
            methodEditText.setText(recipe.getInstructions());
            timeEditText.setText(String.valueOf(recipe.getPreparationTime()));
            caloriesEditText.setText(String.valueOf(recipe.getKcal()));
            proteinsEditText.setText(String.valueOf(recipe.getProteins()));
            lipidesEditText.setText(String.valueOf(recipe.getLipides()));
            glucidesEditText.setText(String.valueOf(recipe.getGlucides()));
            Picasso.get().load(recipe.getImageUrl()).into(photo_placeholder);
            selectedImageUri = Uri.parse(recipe.getImageUrl());


            String category = recipe.getCategory();
            if (category != null && !category.isEmpty()) {
                int chipId = getChipIdFromText(chip_group, category);
                if (chipId != View.NO_ID) {
                    chip_group.check(chipId);
                }
            }

            // Vérifier le chip de niveau de difficulté
            String difficulty = recipe.getDifficultyLevel();
            if (difficulty != null && !difficulty.isEmpty()) {
                int chipId = getChipIdFromText(chip_group1, difficulty);
                if (chipId != View.NO_ID) {
                    chip_group1.check(chipId);
                }
            }
        }



    }









    private int getChipIdFromText(ChipGroup chipGroup, String chipText) {
        int count = chipGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = chipGroup.getChildAt(i);
            if (child instanceof Chip) {
                Chip chip = (Chip) child;
                if (chip.getText().toString().equals(chipText)) {
                    return chip.getId();
                }
            }
        }
        return View.NO_ID;
    }
















    private void saveChanges() {
        Methodsdb methodsdb = new Methodsdb(getContext());
        // Récupérer les valeurs des champs
        String title = nameEditText.getText().toString();
        String ingredients = ingredientsEditText.getText().toString();
        String instructions = methodEditText.getText().toString();
        String category = ""; // Initialisez la catégorie
        String niv_difficulty = ""; // Initialisez le niveau de difficulté

        // Récupérer l'ID du Chip sélectionné pour la catégorie
        int categoryId = chip_group.getCheckedChipId();
        if (categoryId != View.NO_ID) {
            Chip categoryChip = chip_group.findViewById(categoryId);
            category = categoryChip.getText().toString();
        }

        // Récupérer l'ID du Chip sélectionné pour le niveau de difficulté
        int difficultyId = chip_group1.getCheckedChipId();
        if (difficultyId != View.NO_ID) {
            Chip difficultyChip = chip_group1.findViewById(difficultyId);
            niv_difficulty = difficultyChip.getText().toString();
        }

        // Si aucune nouvelle image n'est sélectionnée, utilisez l'URL de l'image existante
        String imageUrl = (selectedImageUri != null) ? selectedImageUri.toString() : recipe.getImageUrl();
        String userId = methodsdb.getRecipeUserId(recipeId);

        int time_coock = Integer.parseInt(timeEditText.getText().toString());

        int likes = methodsdb.getRecipeLikes(recipeId);
        boolean isFavorite = false;
        int kcal = Integer.parseInt(caloriesEditText.getText().toString());
        int proteins = Integer.parseInt(proteinsEditText.getText().toString());
        int lipides = Integer.parseInt(lipidesEditText.getText().toString());
        int glucides = Integer.parseInt(glucidesEditText.getText().toString());
        String vid = ""; // À remplacer par la valeur appropriée

        // Créer une nouvelle recette avec les valeurs des champs
        Recipe updatedRecipe = new Recipe(recipeId, title, ingredients, category, instructions, imageUrl, userId, time_coock, niv_difficulty, likes, isFavorite, kcal, lipides, proteins, glucides, vid);

        // Mettre à jour ou ajouter la recette dans la base de données
        if (methodsdb.updateRecipe(updatedRecipe)) {
            // La recette a été mise à jour avec succès
            Toast.makeText(getContext(), "Recette mise à jour avec succès", Toast.LENGTH_SHORT).show();
        } else {
            // Une erreur s'est produite lors de la mise à jour de la recette
            Toast.makeText(getContext(), "Erreur lors de la mise à jour de la recette", Toast.LENGTH_SHORT).show();
        }


    }
















}