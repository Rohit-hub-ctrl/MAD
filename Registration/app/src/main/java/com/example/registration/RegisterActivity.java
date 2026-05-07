package com.example.registration;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etRoll, etEmail, etCourse;
    Button btnSubmit;
    RadioGroup rgGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // EditTexts
        etName = findViewById(R.id.etName);
        etRoll = findViewById(R.id.etRoll);
        etEmail = findViewById(R.id.etEmail);
        etCourse = findViewById(R.id.etCourse);

        // RadioGroup
        rgGender = findViewById(R.id.rgGender);

        // Button
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString().trim();
                String roll = etRoll.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String course = etCourse.getText().toString().trim();

                int selectedGenderId = rgGender.getCheckedRadioButtonId();

                if (name.isEmpty() || roll.isEmpty() || email.isEmpty()
                        || course.isEmpty() || selectedGenderId == -1) {

                    Toast.makeText(RegisterActivity.this,
                            "Please fill all fields",
                            Toast.LENGTH_SHORT).show();

                } else {

                    RadioButton rbGender = findViewById(selectedGenderId);
                    String gender = rbGender.getText().toString();

                    Toast.makeText(RegisterActivity.this,
                            "Registered Successfully\n" +
                                    "Name: " + name +
                                    "\nRoll: " + roll +
                                    "\nEmail: " + email +
                                    "\nCourse: " + course +
                                    "\nGender: " + gender,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
