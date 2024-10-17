package com.example.cookbook_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;



public class LoginActivity extends AppCompatActivity {



    private static final String FILE_NAME = "myFile";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Button log = findViewById(R.id.button2);

        EditText name= findViewById(R.id.editTextText);
        TextInputLayout textInputLayout = findViewById(R.id.text_password_log);
        TextInputEditText psswrd = findViewById(R.id.password_log);
        CheckBox checkBox =findViewById(R.id.checkBox);


        Methodsdb database =new Methodsdb(this);

        Button rest_password =findViewById(R.id.passwrd_btn);





        DBhelper dBhelper=new DBhelper(this);



        try {
            //  InputStream inputStream = getResources().openRawResource(R.raw.recettes);
            // InputStream inputStream = getAssets().open("file:///android_asset/recettes.json");
            //dBhelper.insertDataFromJSON(inputStream);
            AssetManager assetManager = this.getAssets();
            InputStream inputStream = assetManager.open("recettes.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            dBhelper.insertDataFromJSON(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }













        rest_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l3 = new Intent(getApplicationContext(),    PasswordForget.class);
                startActivity(l3);
                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });


        SharedPreferences sharedPreferences1=getSharedPreferences(FILE_NAME,MODE_PRIVATE);
        String recuperer_username =sharedPreferences1.getString("username","");
        String recuperer_password =sharedPreferences1.getString("password","");
        name.setText(recuperer_username);
        psswrd.setText(recuperer_password);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user =name.getText().toString();
                String mot_passe = psswrd.getText().toString();











                boolean isLogged = database.checkLogin( user,mot_passe);
                if (isLogged) {
                    Toast.makeText(LoginActivity.this, "Connexion Réussie", Toast.LENGTH_LONG).show();




                    Intent l3 = new Intent(getApplicationContext(), AcceuilActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("USERNAME", user); // Remplacez "USERNAME" par la clé correspondant à votre donnée
// Remplacez "EMAIL" par la clé correspondant à votre donnée
                    l3.putExtras(bundle);
                    startActivity(l3);

                    // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    if(checkBox.isChecked()) {
                        StoredDataUsingSharedPref(user, mot_passe);

                    }
                } else
                    Toast.makeText(LoginActivity.this, "Connexion echoué", Toast.LENGTH_LONG).show();


            }
        });

    }

    private void StoredDataUsingSharedPref(String user, String motPasse) {
        SharedPreferences.Editor editor =getSharedPreferences(FILE_NAME, MODE_PRIVATE).edit();
        editor.putString("username",user);
        editor.putString("password",motPasse);
        editor.apply();
    }






}

