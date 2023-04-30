package com.example.nasapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.example.nasapp.R;
import com.example.nasapp.database.DBUsersHelper;
import com.example.nasapp.models.UserModel;

public class SignUpActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        EditText fullNameEditText = findViewById(R.id.idEdtFullName);
        EditText emailEditText = findViewById(R.id.idEdtEmailSignUp);
        EditText passwordEditText = findViewById(R.id.idEdtPasswordSignUp);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button signUpBtn = findViewById(R.id.idBtnSignUp);

        signUpBtn.setOnClickListener(v -> {
            boolean isValidInput = true;
            String fullName = fullNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            DBUsersHelper dbUsersHelper = new DBUsersHelper(SignUpActivity.this);
            if (dbUsersHelper.checkEmail(email)) {
                emailEditText.setError("Email already exists");
                isValidInput = false;
            }

            if (!fullName.matches("[a-zA-Z]+")) {
                fullNameEditText.setError("Invalid name");
                isValidInput = false;
            }

            if (fullName.length() < 3) {
                fullNameEditText.setError("Name must be at least 3 characters");
                isValidInput = false;
            }

            if (!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                emailEditText.setError("Invalid email");
                isValidInput = false;
            }

            if (password.length() < 8) {
                passwordEditText.setError("Password must be at least 8 characters");
                isValidInput = false;
            }

            if (TextUtils.isEmpty(fullNameEditText.getText().toString()) || TextUtils.isEmpty(emailEditText.getText().toString()) || TextUtils.isEmpty(passwordEditText.getText().toString())) {
                Toast.makeText(SignUpActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                isValidInput = false;
            }
            UserModel userModel;
            try {
                userModel = new UserModel(-1, fullName,email,password);
            } catch (Exception e) {
                Toast.makeText(SignUpActivity.this, "Error creating customer", Toast.LENGTH_SHORT).show();
                userModel = new UserModel(-1,"error","error", "error");
            }
            if (isValidInput == true) {
                boolean success = dbUsersHelper.addUser(userModel);
                Toast.makeText(SignUpActivity.this, "User created" + success, Toast.LENGTH_SHORT).show();
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
