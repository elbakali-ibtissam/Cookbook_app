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



public class PasswordForget extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password);


        Button rest_pass = findViewById(R.id.btn_reset_password);
        EditText user = findViewById(R.id.username_edit);
        TextInputEditText pass_reset = findViewById(R.id.password_reset);
        TextInputLayout text_pass_reset = findViewById(R.id.text_password_reset);
        Methodsdb dtbase = new Methodsdb(this);


        pass_reset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String passwordInput = s.toString();
                if (passwordInput.length() >= 8) {
                    Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                    Matcher matcher = pattern.matcher(passwordInput);
                    boolean passwordsMatch = matcher.find();
                    if (passwordsMatch) {
                        text_pass_reset.setHelperText("Your Password Are Strong");
                        text_pass_reset.setError("");
                    } else {
                        text_pass_reset.setError("mix of letters(upper and lower cases), number and symbols");
                    }
                } else {
                    text_pass_reset.setHelperText("Password Must 8 caracters long");
                    text_pass_reset.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rest_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernam = user.getText().toString();
                String newPassword = pass_reset.getText().toString();
                boolean isExist = dtbase.isUsernameExists(usernam);


                if (usernam.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(PasswordForget.this, "Please enter all fields", Toast.LENGTH_SHORT).show();

                } else {

                    if (isExist) {
                        //Intent to home page
                        boolean passwordUpdated = dtbase.updatePassword(usernam, newPassword);
                        if (passwordUpdated) {
                            Toast.makeText(PasswordForget.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            finish();

                            Intent l5 = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(l5);


                        } else {

                            Toast.makeText(PasswordForget.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        }


                    } else
                        Toast.makeText(PasswordForget.this, "This username doesn't exist", Toast.LENGTH_LONG).show();
                }


            }
        });

    };

}


