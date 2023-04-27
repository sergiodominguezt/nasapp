package com.example.nasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String PASSWORD_KEY = "password_key";

    SharedPreferences sharedPreferences;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText emailEditText = findViewById(R.id.idEdtEmail);
        EditText passwordEditText = findViewById(R.id.idEdtPassword);
        Button loginButton = findViewById(R.id.idEdtBtnLogin);
        Button registerButton = findViewById(R.id.idBtnRegister);

        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        email = sharedPreferences.getString("EMAIL_KEY", null);
        password = sharedPreferences.getString("PASSWORD_KEY", null);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailEditText.getText().toString()) && TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString(EMAIL_KEY, emailEditText.getText().toString());
                    editor.putString(PASSWORD_KEY, passwordEditText.getText().toString());

                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (email != null && password != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }
}