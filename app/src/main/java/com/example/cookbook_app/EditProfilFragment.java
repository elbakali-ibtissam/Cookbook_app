package com.example.cookbook_app;




import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfilFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean isPasswordVisible = false;
    private String userId;

    private EditText usernameText;
    private EditText emailEditText;
    private Button saveButton;
    private Uri selectedImageUri;

    private EditText password;
    private EditText newpassword;
    ImageView togglePasswordVisibilityImageView;
    private ImageView photoPlaceholder;
    private Button uploadButton;

    ImageView togglePasswordVisibilityImageView2;

    public EditProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment EditProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfilFragment newInstance(String userId) {
        EditProfilFragment fragment = new EditProfilFragment();
        Bundle args = new Bundle();

        args.putString("USERNAME", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            userId = getArguments().getString("USERNAME");
        }
    }

    private static final int PICK_IMAGE_RQST = 1;

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_edit_profil, container, false);

        usernameText = view.findViewById(R.id.textView3);
        emailEditText = view.findViewById(R.id.textView2);

        usernameText.setEnabled(false);
        password   =view.findViewById(R.id.TextPassword);
        newpassword =view.findViewById(R.id.editTextTextPassword2);

        loadUserDetails(userId);

        ImageView back_btn = view.findViewById(R.id.back_btn);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });





        photoPlaceholder = view.findViewById(R.id.img);
        uploadButton = view.findViewById(R.id.upload);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });













        togglePasswordVisibilityImageView  = view.findViewById(R.id.imgTogglePasswordVisibility);
        togglePasswordVisibilityImageView.setImageResource(R.drawable.ic_visible);
        togglePasswordVisibilityImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide the password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePasswordVisibilityImageView.setImageResource(R.drawable.ic_visible);
                } else {
                    // Show the password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    togglePasswordVisibilityImageView.setImageResource(R.drawable.ic_unvisible);
                }
                isPasswordVisible = !isPasswordVisible;
                // Move the cursor to the end of the text
                password.setSelection(password.length());
            }
        });


        password.setEnabled(false);
        togglePasswordVisibilityImageView2  = view.findViewById(R.id.imgTogglePasswordVisibility2);
        togglePasswordVisibilityImageView2.setImageResource(R.drawable.ic_visible);
        togglePasswordVisibilityImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide the password
                    newpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    togglePasswordVisibilityImageView.setImageResource(R.drawable.ic_visible);
                } else {
                    // Show the password
                    newpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    togglePasswordVisibilityImageView.setImageResource(R.drawable.ic_unvisible);
                }
                // Toggle the boolean flag
                isPasswordVisible = !isPasswordVisible;
                // Move the cursor to the end of the text
                newpassword.setSelection(newpassword.length());
            }
        });










        Button save_edit = view.findViewById(R.id.button4);
        save_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                updateProfile();



            }
        });






        Button logoutButton = view.findViewById(R.id.button5);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSessionData(); // Effacer les données de session
                redirectToLogin(); // Rediriger vers l'écran de connexion
            }
        });













        return view;
    }




    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_RQST);
    }










    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_RQST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            // Afficher l'image sélectionnée dans ImageView (par exemple)
            photoPlaceholder.setImageURI(selectedImageUri);
        }
    }






    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }














    private void updateProfile() {
        String email = emailEditText.getText().toString().trim();
        String newPassword = newpassword.getText().toString().trim();

        // Vérifiez si les champs obligatoires sont vides
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getContext(), "Vous avez laissé le champs d'email vide", Toast.LENGTH_SHORT).show();
            return;
        }







        // Récupérer l'URI de l'image sélectionnée par l'utilisateur
        String imageUri = "";
        if (selectedImageUri != null) {
            imageUri = selectedImageUri.toString();
        }

        // Mettez à jour le profil dans la base de données
        updateProfileInDatabase(email, newPassword, imageUri);
    }








    private void updateProfileInDatabase(String email, String newPassword, String imageUri) {

        SQLiteDatabase db = new DBhelper(getContext()).getWritableDatabase();



        if(!newPassword.equals("")){
            ContentValues values = new ContentValues();
            values.put("email", email);
            values.put("password", newPassword);
            values.put("image", imageUri);
            String whereClause = "username = ?";
            String[] whereArgs = { userId }; // Assurez-vous que `currentUsername` est défini correctement

            int rowsAffected = db.update("Users", values, whereClause, whereArgs);
            if (rowsAffected > 0) {
                Toast.makeText(getContext(), "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Échec de la mise à jour du profil", Toast.LENGTH_SHORT).show();
            }
        }else{
            ContentValues values = new ContentValues();
            values.put("email", email);

            values.put("image", imageUri);
            String whereClause = "username = ?";
            String[] whereArgs = { userId }; // Assurez-vous que `currentUsername` est défini correctement

            int rowsAffected = db.update("Users", values, whereClause, whereArgs);
            if (rowsAffected > 0) {
                Toast.makeText(getContext(), "Profil mis à jour avec succès", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Échec de la mise à jour du profil", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void saveImagePathToDatabase(String imagePath) {

        SQLiteDatabase db = new DBhelper(getContext()).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imagePath);
        int rows = db.update("Users", values, "username=?", new String[]{userId});
        db.close();
        if (rows > 0) {
            Toast.makeText(getContext(), "Profile image updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to update profile image", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentUserId() {
        // Return the current user's ID, this is a placeholder
        return "current_user_id";
    }














    private void loadUserDetails(String userId) {
        Methodsdb methodsdb =new Methodsdb(getContext());
        User utilisateur = methodsdb.getUserById(userId);
        if (userId != null) {
            usernameText.setText(utilisateur.getUsername());
            emailEditText.setText(utilisateur.getEmail());
            password.setText(utilisateur.getPassword());

        } else {
            // Handle the case where user details are not found
            Log.e("UserProfileFragment", "User not found");
        }








    }






    private void clearSessionData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }


    private void redirectToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Empêche l'utilisateur de revenir à l'activité précédente
        startActivity(intent);
        getActivity().finish(); // Termine l'activité actuelle
    }


}