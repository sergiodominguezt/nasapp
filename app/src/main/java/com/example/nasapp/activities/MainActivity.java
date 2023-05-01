package com.example.nasapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nasapp.R;
import com.example.nasapp.database.DBUsersHelper;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String ID_KEY = "id_key";
    public static final String FIRST_NAME_KEY = "first_name_key";
    public static final String LAST_NAME_KEY = "last_name_key";

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

        String email = emailEditText.getText().toString().trim();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(emailEditText.getText().toString()) && TextUtils.isEmpty(passwordEditText.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                } else {
                    DBUsersHelper dbUsersHelper = new DBUsersHelper(MainActivity.this);
                    if (dbUsersHelper.checkUser(emailEditText.getText().toString().trim(), passwordEditText.getText().toString().trim())) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(EMAIL_KEY, emailEditText.getText().toString());
                        editor.putString(FIRST_NAME_KEY, dbUsersHelper.getFirstName(emailEditText.getText().toString()));
                        editor.putString(LAST_NAME_KEY, dbUsersHelper.getLastName(emailEditText.getText().toString()));
                        editor.putInt(ID_KEY, dbUsersHelper.getUserById(emailEditText.getText().toString()));
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("email", email);

                        MainActivity.this.startActivity(intent);
                        MainActivity.this.finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
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