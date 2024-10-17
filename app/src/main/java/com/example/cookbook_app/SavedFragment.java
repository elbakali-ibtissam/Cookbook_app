package com.example.cookbook_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String userId;
    public SavedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SavedFragment newInstance(String param1, String param2, String userId) {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString("USERNAME", userId);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        View v = inflater.inflate(R.layout.fragment_saved, container, false);
        // Inflate the layout for this fragment
        //SharedPreferences sharedPreferences = getContext().getSharedPreferences("myFile", Context.MODE_PRIVATE);
        //String userId = sharedPreferences.getString("username", "cant");
        ListView listView = v.findViewById(R.id.rcv_list_saved_recipe);


        Methodsdb methodsdb = new Methodsdb(getContext());


        List<Recipe> favoriteRecipes = methodsdb.getFavoriteRecipes(userId);
        for (Recipe recipe : favoriteRecipes) {
            Log.d("Recipe", "ids: " + recipe);
        }
        Log.e("favorite recipes", "Fav :" + favoriteRecipes);
// Maintenant, vous pouvez utiliser likedRecipes pour peupler votre vue
        if (favoriteRecipes.isEmpty()) {

            // Afficher un message si aucune recette n'est sauvegardée
            LinearLayout layoutEmpty = v.findViewById(R.id.layout_empty_saved_recipe);
            layoutEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            // Adapter pour afficher les recettes sauvegardées
            RecipeAdapter adapter = new RecipeAdapter(getContext(), favoriteRecipes);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Recipe selectedRecipe = favoriteRecipes.get(position);
                    RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(selectedRecipe.getId(), userId);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.host_Frag, recipeDetailFragment)
                            .addToBackStack(null)
                            .commit();
                }


            });



        }
        return v;
    }
}