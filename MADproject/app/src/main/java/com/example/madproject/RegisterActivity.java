package com.example.madproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, confirmPassword;
    Button registerBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        confirmPassword = findViewById(R.id.regConfirmPassword);
        registerBtn = findViewById(R.id.registerBtn);

        dbHelper = new DatabaseHelper(this);

        registerBtn.setOnClickListener(view -> {

            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();

            if(userEmail.isEmpty() || userPassword.isEmpty() || confirmPass.isEmpty()){
                Toast.makeText(RegisterActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
            }
            else if(!userPassword.equals(confirmPass)){
                Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
            else{
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("email", userEmail);
                values.put("password", userPassword);

                db.insert("users", null, values);

                Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();

                // Navigate directly to HomeActivity
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}