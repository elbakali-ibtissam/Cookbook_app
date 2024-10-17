package com.example.cookbook_app;

import static android.content.Intent.getIntent;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cookbook_app.RecipeAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcceuilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcceuilFragment extends Fragment {
    RecipeAdapter adapter;
    Dialog dialogfilter;

    private List<Recipe> recipelist = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    List<Recipe> recipes= new ArrayList<>();
    private List<Recipe> filteredRecipes = new ArrayList<>();




    public interface OnRecipeSelectedListener {
        void onRecipeSelected(int recipeId);
    }
    private OnRecipeSelectedListener mListener;



    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnRecipeSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeSelectedListener");
        }
    }











    public AcceuilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AcceuilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcceuilFragment newInstance(String param1, String param2) {
        AcceuilFragment fragment = new AcceuilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    String userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("USERNAME");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("AcceuilFragment", "onCreateView called");







        Methodsdb methodsdb = new Methodsdb(getContext());
        View view = inflater.inflate(R.layout.fragment_acceuil, container, false);
        ChipGroup chipGroup = view.findViewById(R.id.chip_group);
        ListView homeListView = view.findViewById(R.id.home_list);
        TextView user =view.findViewById(R.id.profle_name);
        ImageView back_btn =view.findViewById(R.id.img_back);
        user.setText((user.getText()) +" "+userId);
        recipes =  methodsdb.getAllRecipes();
        for (Recipe recipe : recipes) {
            Log.d("Recipe", "Title: " + recipe.getTitle() + ", Category: " + recipe.getCategory());
        }
        adapter = new RecipeAdapter(getContext(),recipes);

        load();

        homeListView.setAdapter(adapter);
        // adapter.notifyDataSetChanged();


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();
            }
        });
/*
        if (chipGroup.getChildCount() > 0) {
            Chip firstChip = (Chip) chipGroup.getChildAt(0);
            firstChip.setChecked(true); // Sélectionner le premier chip par défaut
            String defaultCategory = firstChip.getText().toString();
            updateRecipesByCategory(defaultCategory);
        }
*/
        // Ajouter un écouteur d'événements au ChipGroup
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = group.findViewById(checkedId);
                String category = chip.getText().toString();


                int[][] states = new int[][] {
                        new int[] { android.R.attr.state_checked }, // Sélectionné
                        new int[] { -android.R.attr.state_checked } // Non sélectionné
                };

                int[] backgroundColors = new int[] {
                        Color.parseColor("#a0410e"), // Couleur de fond sélectionnée
                        Color.WHITE  // Couleur de fond non sélectionnée
                };

                int[] textColors = new int[] {
                        Color.WHITE, // Couleur de texte sélectionnée
                        Color.BLACK // Couleur de texte non sélectionnée
                };


                ColorStateList backgroundColorStateList = new ColorStateList(states, backgroundColors);
                ColorStateList textColorStateList = new ColorStateList(states, textColors);


                chip.setChipBackgroundColor(backgroundColorStateList);
                chip.setTextColor(textColorStateList);


                if (chip != null ) {
                    updateRecipesByCategory(category);

                }

            }
        });




























        EditText searchView = view.findViewById(R.id.editTextText2);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                List<Recipe> filtered = adapter.filter(s.toString(), recipes);
                adapter.clear(); // Effacer les anciennes donnéesch
                adapter.addAll(filtered); // Ajouter les nouvelles données
                adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        Button clear =view.findViewById(R.id.button);








        Button buttonShowDialog = view.findViewById(R.id.button);

        // Initialize the dialog
        dialogfilter = new Dialog(getContext());
        dialogfilter.setContentView(R.layout.dialogue_filter);

        // Set onClickListener for the button to show the dialog
        buttonShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogfilter.show();
            }
        });





















        homeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = (Recipe) parent.getItemAtPosition(position);





                // Notifiez l'activité hôte de la sélection de la recette
                mListener.onRecipeSelected(selectedRecipe.getId());

                // Créer un intent pour démarrer RecipeDetailActivity

            }
        });









        // Set onClickListener for the button to apply filter
        MaterialButton applyButton = dialogfilter.findViewById(R.id.apply_btn);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérer les valeurs entrées par l'utilisateur
                EditText editTextGlucides = dialogfilter.findViewById(R.id.editTextText11);
                EditText editTextCalories = dialogfilter.findViewById(R.id.editTextText10);
                EditText editTextPreparation = dialogfilter.findViewById(R.id.editTextText4);

                String glucides = editTextGlucides.getText().toString();
                String calories = editTextCalories.getText().toString();
                String  preparation = editTextPreparation.getText().toString();


                ChipGroup chipGroupDifficulte = dialogfilter.findViewById(R.id.chip_grp);
                int checkedChipId = chipGroupDifficulte.getCheckedChipId();
                String difficulte = "";
                if (checkedChipId != View.NO_ID) {
                    Chip chip = dialogfilter.findViewById(checkedChipId);
                    difficulte = chip.getText().toString();
                }




                List<Recipe> recettesFiltrees = filterRecipesInView(adapter.getRecipes(), glucides, calories, preparation, difficulte);
                Log.e("filter" , "filtres : "+recettesFiltrees);
                adapter.clear();// Effacer les anciennes données
                adapter.addAll(recettesFiltrees); // Ajouter les nouvelles données
                adapter.notifyDataSetChanged();
                // Fermer le dialog
                dialogfilter.dismiss();
            }
        });


// Set onClickListener for the button to reset filter
        MaterialButton resetButton = dialogfilter.findViewById(R.id.reset_btn);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Réinitialiser les champs du filtre
                EditText editTextGlucides = dialogfilter.findViewById(R.id.editTextText11);
                EditText editTextCalories = dialogfilter.findViewById(R.id.editTextText10);
                EditText editTextPreparation = dialogfilter.findViewById(R.id.editTextText4);

                editTextGlucides.setText("");
                editTextCalories.setText("");
                editTextPreparation.setText("");

                // Charger à nouveau la liste des recettes initiales
                load();

                // Si aucune puce n'est sélectionnée, réinitialiser la liste des recettes dans l'adaptateur
                if (chipGroup.getCheckedChipId() == View.NO_ID) {
                    adapter.clear();
                    adapter.addAll(recipes); // Utilisez la liste initiale des recettes
                    adapter.notifyDataSetChanged();
                } else {
                    // Si une puce est sélectionnée, mettre à jour la liste des recettes en fonction de la catégorie
                    Chip selectedChip = dialogfilter.findViewById(chipGroup.getCheckedChipId());
                    String selectedCategory = selectedChip.getText().toString();
                    updateRecipesByCategory(selectedCategory);
                }

                // Décocher tous les chips dans le ChipGroup
                chipGroup.clearCheck();
                dialogfilter.dismiss();
            }
        });





















        return view;
    }


    private void load() {
        Methodsdb dbHelper = new Methodsdb(getContext());
        recipes = dbHelper.getAllRecipes();
        adapter.clear(); // Effacer les anciennes données
        adapter.addAll(recipes); // Ajouter les nouvelles données
        adapter.notifyDataSetChanged(); // Notifier l'adaptateur des changements
    }




    private void updateRecipesByCategory(String category) {
        Methodsdb dbHelper = new Methodsdb(getContext());


        recipes = dbHelper.getRecipesByCategorie(category);
        adapter.clear(); // Effacer les anciennes données
        adapter.addAll(recipes); // Ajouter les nouvelles données
        adapter.notifyDataSetChanged(); // Notifier l'adaptateur des changements
    }



    // Méthode pour afficher la liste des recettes sur l'écran



    // Méthode pour filtrer les recettes en fonction de la catégorie sélectionnée
    private List<Recipe> filterRecipesByCategory(List<Recipe> recipes, String category) {
        List<Recipe> filteredRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getCategory().equals(category)) {
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;




    }
    // Méthode pour filtrer les recettes déjà affichées dans le ListView en fonction des critères spécifiés
    private List<Recipe> filterRecipesInView(List<Recipe> recipesInView, String glucides, String calories, String tempsPreparation, String difficulte) {
        List<Recipe> filteredRecipesInView = new ArrayList<>();
        for (Recipe recipe : recipesInView) {
            if (verifierCriteresFiltrage(recipe, glucides, calories, tempsPreparation, difficulte)) {
                filteredRecipesInView.add(recipe);
            }
        }
        return filteredRecipesInView;
    }


    // Méthode pour récupérer les recettes filtrées en fonction des paramètres spécifiés par l'utilisateur
    private List<Recipe> getRecettesFiltrees(String glucides, String calories, String tempsPreparation, String difficulte) {
        // Initialiser la liste de recettes filtrées
        List<Recipe> filteredRecipes = new ArrayList<>();

        // Récupérer toutes les recettes de la base de données
        Methodsdb dbHelper = new Methodsdb(getContext());
        List<Recipe> allRecipes = dbHelper.getAllRecipes();

        // Parcourir toutes les recettes pour les filtrer en fonction des paramètres spécifiés
        for (Recipe recipe : allRecipes) {
            // Vérifier si la recette satisfait aux critères de filtrage
            if (verifierCriteresFiltrage(recipe, glucides, calories, tempsPreparation, difficulte)) {
                filteredRecipes.add(recipe);
            }
        }

        // Retourner la liste des recettes filtrées
        return filteredRecipes;
    }

    // Méthode pour vérifier si une recette satisfait aux critères de filtrage spécifiés par l'utilisateur
    private boolean verifierCriteresFiltrage(Recipe recipe, String glucides, String calories, String tempsPreparation, String difficulte) {
        // Vérifier les critères de filtrage un par un
        if (!glucides.isEmpty() && recipe.getGlucides() > Integer.parseInt(glucides)) {
            return false; // La recette ne satisfait pas le critère de glucides
        }

        if (!calories.isEmpty() && recipe.getKcal() > Integer.parseInt(calories)) {
            return false; // La recette ne satisfait pas le critère de calories
        }

        if (!tempsPreparation.isEmpty() && recipe.getPreparationTime() > Integer.parseInt(tempsPreparation)) {
            return false; // La recette ne satisfait pas le critère de temps de préparation
        }

        if (!difficulte.isEmpty() && !recipe.getDifficultyLevel().equalsIgnoreCase(difficulte)) {
            return false; // La recette ne satisfait pas le critère de difficulté
        }

        // La recette satisfait tous les critères de filtrage
        return true;
    }

}



