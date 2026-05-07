package com.example.a6;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editName;
    CheckBox checkJava, checkPython;
    RadioGroup radioGroupGender;
    ToggleButton toggleStatus;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        checkJava = findViewById(R.id.checkJava);
        checkPython = findViewById(R.id.checkPython);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        toggleStatus = findViewById(R.id.toggleStatus);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String name = editName.getText().toString();

                String languages = "";

                if (checkJava.isChecked())
                    languages += "Java ";

                if (checkPython.isChecked())
                    languages += "Python ";

                int selectedId =
                        radioGroupGender.getCheckedRadioButtonId();

                String gender = "";
                if (selectedId != -1) {
                    RadioButton radioGender = findViewById(selectedId);
                    gender = radioGender.getText().toString();
                } else {
                    gender = "Not Selected";
                }

                String status;

                if (toggleStatus.isChecked())
                    status = "ON";
                else
                    status = "OFF";

                String result =
                        "Name: " + name +
                                "\nLanguages: " + languages +
                                "\nGender: " + gender +
                                "\nToggle: " + status;

                Toast.makeText(
                        MainActivity.this,
                        result,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
