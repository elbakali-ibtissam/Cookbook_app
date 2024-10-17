package com.example.cookbook_app;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView img;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private String userId;
    private  String imagePath;

    public ProfileFragment() {
        // Required empty public constructor
    }

    private static final int REQUEST_CODE_PICK_IMAGE = 2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2, String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString("USERNAME", userId);
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

    TextView user;
    TextView email;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Methodsdb methodsdb =new Methodsdb(getContext());

        user =view.findViewById(R.id.user);
        email = view.findViewById(R.id.txt_email);
        img = view.findViewById(R.id.img);

        loadUserDetails(userId);



        //String imagePath = methodsdb.getImagePath(userId);
        loadUserImageFromDatabase(userId);
// Charger l'image dans ImageView avec Picasso
        //if (!imagePath.isEmpty()) {
        //  Picasso.get().load(new File(imagePath)).into(img);

        //}






        /*
        // Récupérer l'URI de l'image de la base de données
        String profil_img = methodsdb.getUserImageUri(userId);

        // Charger l'image dans ImageView
        if (profil_img != null && !profil_img.isEmpty()) {
            Log.e("user image ", " Not NULL IMAGE");
            Picasso.get().load(profil_img).into(img);
        } else {
            Log.e("user image ", " NULL IMAGE");
            // Charger une image par défaut si aucun URI n'est trouvé
            //img.setImageResource(R.drawable.default_image);
        }

*/





        ListView list_recipes = view.findViewById(R.id.list_myrecipes);
        ListView list_liked = view.findViewById(R.id.list_liked);


        Button stepBtn = view.findViewById(R.id.steps_btn);
        Button ingBtn = view.findViewById(R.id.ing_btn);
        //ScrollView scrollViewIngredients = view.findViewById(R.id.ing_scroll);
        //ScrollView scrollViewSteps = view.findViewById(R.id.steps);


        stepBtn.setOnClickListener(v -> {

            stepBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.chibi));

            ingBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

            list_recipes.setVisibility(View.GONE);
            list_liked.setVisibility(View.VISIBLE);
        });

        ingBtn.setOnClickListener(v -> {

            ingBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.chibi));

            stepBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));

            list_recipes.setVisibility(View.VISIBLE);
            list_liked.setVisibility(View.GONE);
        });


        List<Recipe> likedRecipes = methodsdb.getLikedRecipesByUser(userId);
        for (Recipe recipe : likedRecipes) {
            Log.d("Recipe", "ids: " + recipe);
        }
        Log.e("favorite recipes", "Fav :" + likedRecipes);
// Maintenant, vous pouvez utiliser likedRecipes pour peupler votre vue


        // Adapter pour afficher les recettes sauvegardées
        RecipeAdapter adapter = new RecipeAdapter(getContext(), likedRecipes);
        list_liked.setAdapter(adapter);
        list_liked.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = likedRecipes.get(position);
                RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(selectedRecipe.getId(), userId);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_Frag, recipeDetailFragment)
                        .addToBackStack(null)
                        .commit();



            }




        });
        // Récupérer les recettes de la base de données
        List<Recipe> recipeList = methodsdb.getRecipesCreatedByUser(userId);

        // Créer et définir l'adaptateur personnalisé
        RecipeAdapterP adapterprofil = new RecipeAdapterP(getContext(), recipeList);
        list_recipes.setAdapter(adapterprofil);

        list_recipes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = recipeList.get(position);
                RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(selectedRecipe.getId(), userId);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.host_Frag, recipeDetailFragment)
                        .addToBackStack(null)
                        .commit();



            }



        });













        Button edit_btn = view.findViewById(R.id.button3);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProfilFragment editFragment = EditProfilFragment.newInstance(userId);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.host_Frag, editFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });


        return view;


    }

    private void loadUserDetails(String userId) {
        Methodsdb methodsdb =new Methodsdb(getContext());
        User utilisateur = methodsdb.getUserById(userId);
        if (userId != null) {
            user.setText(utilisateur.getUsername());
            email.setText(utilisateur.getEmail());
        } else {
            // Handle the case where user details are not found
            Log.e("UserProfileFragment", "User not found");
        }
    }







    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE);
        } else {
            openImagePicker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                // Gérer le cas où la permission est refusée
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            // Utilisez l'URI sélectionné pour afficher ou télécharger l'image
            loadImage(selectedImageUri);
        }
    }



    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }



    private void loadImage(Uri imageUri) {
        Picasso.get().load(imageUri).into(img); // img est votre ImageView
    }

    private void loadUserImageFromDatabase(String userId) {
        Methodsdb methodsdb = new Methodsdb(getContext());
        String imagePath = methodsdb.getUserImageUri(userId);

        if (imagePath != null && !imagePath.isEmpty()) {


            Log.d("URI", "URI de l'image : " + imagePath);


            Picasso.get().load(Uri.parse(imagePath)).into(img, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {

                    e.printStackTrace();

                }
            });
        } else {

            //Toast.makeText(getContext(), "Aucune image trouvée pour cet utilisateur", Toast.LENGTH_SHORT).show();
        }
    }






}