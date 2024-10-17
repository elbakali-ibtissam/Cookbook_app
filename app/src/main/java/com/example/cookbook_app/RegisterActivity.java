package com.example.cookbook_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;







public class RegisterActivity extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        Button sign =findViewById(R.id.register_btn);
        Button log =findViewById(R.id.btn_log);
        EditText nom =findViewById(R.id.name_text);
        EditText mail =findViewById(R.id.email_text);
        //EditText mot_passe =findViewById(R.id.password);
        TextInputEditText mot_passe_confirm = findViewById(R.id.password_confirm);
        TextInputEditText textInputEditText =findViewById(R.id.passw);

        TextInputLayout textInputLayout =findViewById(R.id.text_pass);



        Methodsdb database = new Methodsdb(this);






















        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordInput = s.toString();
                if(passwordInput.length()>=8){
                    Pattern pattern =Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher=pattern.matcher(passwordInput);
                    boolean passwordsMatch = matcher.find();
                    if(passwordsMatch){
                        textInputLayout.setHelperText("Your Password Are Strong");
                        textInputLayout.setError("");
                    }else{
                        textInputLayout.setError("mix of letters(upper and lower cases), number and symbols");
                    }
                }else{
                    textInputLayout.setHelperText("Password Must 8 caracters long");
                    textInputLayout.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });











        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(l1);
                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });





        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, password, password_confirm;
                user =nom.getText().toString();
                String email =mail.getText().toString();
                password =textInputEditText.getText().toString();


                password_confirm =mot_passe_confirm.getText().toString();
                if(user.equals("") || email.equals("") || password.equals("") || password_confirm.equals("")){
                    Toast.makeText(RegisterActivity.this, "please fill all the fields", Toast.LENGTH_LONG).show();
                }else{
                    if(password.equals(password_confirm)){
                        if(database.isUsernameExists(user)){
                            Toast.makeText(RegisterActivity.this, "User Already Exists", Toast.LENGTH_LONG).show();

                        }else {


                            boolean registration_result = database.insertUser(user, password, email);
                            if (registration_result) {
                                Toast.makeText(RegisterActivity.this, "User Registered Successfully", Toast.LENGTH_LONG).show();



                                Intent l3 = new Intent(getApplicationContext(), AcceuilActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("USERNAME", user); // Remplacez "USERNAME" par la clé correspondant à votre donnée
// Remplacez "EMAIL" par la clé correspondant à votre donnée
                                l3.putExtras(bundle);
                                startActivity(l3);




                                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

                            } else
                                Toast.makeText(RegisterActivity.this, "User Resistration Failed", Toast.LENGTH_LONG).show();

                        }


                    }else{
                        Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }










}


