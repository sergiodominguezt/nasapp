package com.example.nasapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SignUpActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String FULL_NAME = "name_key";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";
    private Toolbar toolbar;

    SharedPreferences sharedPreferences;
    String fullName, email, password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        EditText fullNameEditText = findViewById(R.id.idEdtFullName);
        EditText emailEditText = findViewById(R.id.idEdtEmailSignUp);
        EditText passwordEditText = findViewById(R.id.idEdtPasswordSignUp);
        Button signUpBtn = findViewById(R.id.idBtnSignUp);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        fullName = sharedPreferences.getString("FULL_NAME", null);
        email = sharedPreferences.getString("EMAIL_KEY", null);
        password = sharedPreferences.getString("PASSWORD_KEY", null);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(fullNameEditText.getText().toString()) || TextUtils.isEmpty(emailEditText.getText().toString()) || TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Fill all", Toast.LENGTH_SHORT).show();
                } else {
                    UserModel userModel;
                    try {
                        userModel = new UserModel(-1, fullNameEditText.getText().toString(),emailEditText.getText().toString(),passwordEditText.getText().toString());

                    } catch (Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
                        userModel = new UserModel(-1,"error","error", "error");

                    }
                    DBHelper dbHelper = new DBHelper(SignUpActivity.this);
                    boolean success = dbHelper.addUser(userModel);

                    Toast.makeText(SignUpActivity.this, "Success" + success, Toast.LENGTH_SHORT).show();


                }



            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
