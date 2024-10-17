package com.example.cookbook_app;



import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class AcceuilActivity extends AppCompatActivity implements AcceuilFragment.OnRecipeSelectedListener{
    String username;
    RecipeAdapter adapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acceuil_page);


        Intent l3 = getIntent();
        if (l3 != null) {
            username = l3.getStringExtra("USERNAME");
        }

        // Ajouter ce code pour passer le username au fragment
        Bundle bundle = new Bundle();
        bundle.putString("USERNAME", username);

        AcceuilFragment fragment = new AcceuilFragment();
        fragment.setArguments(bundle);

        // Remplacer le fragment initial avec les arguments passés
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.host_Frag, fragment)
                .commit();
        // Assurez-vous de définir userId correctement ici
















        // Trouver la BottomNavigationView
        BottomNavigationView navigationView = findViewById(R.id.btn_navigation);

        // Configure le gestionnaire de sélection pour la BottomNavigationView
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id_item = item.getItemId();
                if(id_item == R.id.acceuil_Fragment) {
                    item.setChecked(true);
                    AcceuilFragment fragment = new AcceuilFragment();
                    fragment.setArguments(bundle);

                    // Remplacer le fragment initial avec les arguments passés
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_Frag, fragment)
                            .commit();


                }



                //getSupportFragmentManager().beginTransaction().replace(R.id.host_Frag, new AcceuilFragment()).commit();}

                else if (id_item == R.id.savedFragment) {
                    SavedFragment fragment1 = new SavedFragment();
                    fragment1.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_Frag, fragment1)
                            .commit();
                    item.setChecked(true);
                }
                else if (id_item == R.id.profileFragment) {
                    ProfileFragment fragment3 = new ProfileFragment();

                    fragment3.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_Frag, fragment3)
                            .commit();
                    item.setChecked(true);
                }
                else if (id_item == R.id.addFragment) {
                    AddFragment fragment2 = new AddFragment();
                    fragment2.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_Frag, fragment2)
                            .commit();
                    item.setChecked(true);
                }
                return false;
            }
        });






    }





    public void onRecipeSelected(int recipeId) {
        Methodsdb dbHelper = new Methodsdb(this);
        Recipe selectedRecipe = dbHelper.getRecipeById(recipeId);


        Intent l3 = getIntent();
        if (l3 != null) {
            username = l3.getStringExtra("USERNAME");
        }

        // Ajouter ce code pour passer le username au fragment
        Bundle bundle = new Bundle();
        bundle.putString("USERNAME", username);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);






        // Remplacez le fragment actuel par RecipeDetailFragment avec l'ID de la recette sélectionnée
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.host_Frag, RecipeDetailFragment.newInstance(recipeId, username))
                .addToBackStack(null)
                .commit();
    }

}

