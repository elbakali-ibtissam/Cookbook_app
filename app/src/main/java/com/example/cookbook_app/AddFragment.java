package com.example.cookbook_app;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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




public class AddFragment extends Fragment {
    private Uri selectedImageUri;

    // Déclarer les vues
    private EditText nameAddNew, ingAdd, methodeAdd, timeAdd, caloAdd, proteinsAdd, lipidesAdd, glucidesAdd;
    private ChipGroup chipGroup, chipGroup1;
    private Button btnSaveRecipe, btnImportImage;
    private ImageView photoPlaceholder;

    // Constante pour le code de demande de sélection d'image
    private static final int PICK_IMAGE_REQUEST = 1;
    String userId;

    public static AddFragment newInstance(String userId) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        args.putString("USERNAME", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("USERNAME");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Initialiser les vues
        nameAddNew = view.findViewById(R.id.name_add_new);
        ingAdd = view.findViewById(R.id.ing_add);
        methodeAdd = view.findViewById(R.id.methode_add);
        timeAdd = view.findViewById(R.id.time_add);
        caloAdd = view.findViewById(R.id.calo_add);
        proteinsAdd = view.findViewById(R.id.proteins_add);
        lipidesAdd = view.findViewById(R.id.lipides_add);
        glucidesAdd = view.findViewById(R.id.glucides);
        chipGroup = view.findViewById(R.id.chip_group);
        chipGroup1 = view.findViewById(R.id.chip_group1);
        btnSaveRecipe = view.findViewById(R.id.btn_save_recipe);
        btnImportImage = view.findViewById(R.id.btn_scatta);
        photoPlaceholder = view.findViewById(R.id.photo_placeholder);

        // Définir le comportement du bouton "Enregistrer"
        btnSaveRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe();
            }
        });

        // Définir le comportement du bouton "Importer une image"
        btnImportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        return view;
    }

    // Méthode pour ouvrir la galerie de médias et sélectionner une image
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
                photoPlaceholder.setImageURI(selectedImageUri);
            }
        }
    }

    // Méthode pour enregistrer la recette
    private void saveRecipe() {
        // Récupérer les valeurs des champs de texte
        String name = nameAddNew.getText().toString().trim();
        String ingredients = ingAdd.getText().toString().trim();
        String method = methodeAdd.getText().toString().trim();
        String timeString = timeAdd.getText().toString().trim();
        String caloString = caloAdd.getText().toString().trim();
        String proteinsString = proteinsAdd.getText().toString().trim();
        String lipidesString = lipidesAdd.getText().toString().trim();
        String glucidesString = glucidesAdd.getText().toString().trim();

        // Récupérer la valeur sélectionnée dans le ChipGroup pour la catégorie
        String category = getSelectedChipText(chipGroup);

        // Récupérer la valeur sélectionnée dans le ChipGroup pour le niveau de difficulté
        String difficulty = getSelectedChipText(chipGroup1);

        // Vérifier si tous les champs obligatoires sont remplis
        if (name.isEmpty() || ingredients.isEmpty() || method.isEmpty() || timeString.isEmpty() || category.isEmpty() || difficulty.isEmpty()) {
            // Afficher un message d'erreur pour indiquer les champs obligatoires manquants
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs obligatoires", Toast.LENGTH_SHORT).show();

            // Modifier la couleur du hint pour les champs obligatoires non remplis
            if (name.isEmpty()) {
                nameAddNew.setHintTextColor(Color.RED);
                nameAddNew.setHint("Nom (champ obligatoire)");
            }
            if (ingredients.isEmpty()) {
                ingAdd.setHintTextColor(Color.RED);
                ingAdd.setHint("Ingrédients (champ obligatoire)");
            }
            if (method.isEmpty()) {
                methodeAdd.setHintTextColor(Color.RED);
                methodeAdd.setHint("Méthode (champ obligatoire)");
            }
            if (timeString.isEmpty()) {
                timeAdd.setHintTextColor(Color.RED);
                timeAdd.setHint("Temps (champ obligatoire)");
            }
            if (category.isEmpty()) {
                // Changer la couleur du hint pour les chips non sélectionnés en rouge
                changeChipGroupHintColor(chipGroup, Color.RED);
            }
            if (difficulty.isEmpty()) {
                // Changer la couleur du hint pour les chips non sélectionnés en rouge
                changeChipGroupHintColor(chipGroup1, Color.RED);
            }

            // Vérifier si une image a été sélectionnée

        } else {
            // Tous les champs sont remplis, procédez à l'enregistrement
            // Convertir les valeurs en entiers uniquement si les champs ne sont pas vides
            int time = Integer.parseInt(timeString);
            int calories = caloString.isEmpty() ? 0 : Integer.parseInt(caloString);
            int proteins = proteinsString.isEmpty() ? 0 : Integer.parseInt(proteinsString);
            int lipides = lipidesString.isEmpty() ? 0 : Integer.parseInt(lipidesString);
            int glucides = glucidesString.isEmpty() ? 0 : Integer.parseInt(glucidesString);

            // Récupérer l'URL de l'image sélectionnée
            String imageUrl = (selectedImageUri != null) ? selectedImageUri.toString() : "";
            if(imageUrl.equals("")){

                // Afficher un message d'erreur pour indiquer qu'une image est requise
                Toast.makeText(getActivity(), "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show();


            }else {
                // Enregistrer la recette
                insertRecipe(name, ingredients, category, method, time, imageUrl, calories, glucides, lipides, proteins, difficulty);
            }
        }

    }

    private void changeChipGroupHintColor(ChipGroup chipGroup, int color) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            chip.setHintTextColor(color);
        }
    }

    private String getSelectedChipText(ChipGroup chipGroup) {
        int checkedChipId = chipGroup.getCheckedChipId();
        if (checkedChipId != View.NO_ID) {
            Chip chip = chipGroup.findViewById(checkedChipId);
            if (chip != null) {
                return chip.getText().toString();
            }
        }
        return "";
    }

    // Méthode pour insérer la recette dans la base de données
    private void insertRecipe(String name, String ingredients, String category, String method, int time, String imageUrl,
                              int calories, int glucides, int lipides, int proteins, String difficulty) {
        // Récupérer l'identifiant de l'utilisateur (exemple avec SharedPreferences)
        //SharedPreferences sharedPreferences = getContext().getSharedPreferences("myFile", Context.MODE_PRIVATE);
        // String userId = sharedPreferences.getString("username", "cant");

        Methodsdb methodsdb = new Methodsdb(getContext());
        methodsdb.insertRecipe(name, ingredients, category, method, time, imageUrl, userId, calories, glucides, lipides, proteins, difficulty);

        // Afficher un message de succès à l'utilisateur
        Toast.makeText(getActivity(), "Recette enregistrée avec succès", Toast.LENGTH_SHORT).show();

        // Réinitialiser les champs après l'enregistrement
        resetFields();
    }

    // Méthode pour réinitialiser les champs après l'enregistrement
    private void resetFields() {
        nameAddNew.setText("");
        ingAdd.setText("");
        methodeAdd.setText("");
        timeAdd.setText("");
        caloAdd.setText("");
        proteinsAdd.setText("");
        lipidesAdd.setText("");
        glucidesAdd.setText("");
        chipGroup.clearCheck();
        chipGroup1.clearCheck();
        photoPlaceholder.setImageResource(0);
    }
}










