package com.example.a7;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Spinner spinnerCourse;
    private Button btnAlert, btnPopup;
    private boolean isInitialSelection = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerCourse = findViewById(R.id.spinnerCourse);
        btnAlert = findViewById(R.id.btnAlert);
        btnPopup = findViewById(R.id.btnPopup);

        setupSpinner();
        setupAlertButton();
        setupPopupButton();
    }

    private void setupSpinner() {
        String[] courses = {"Java", "Android", "Python", "C++"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                courses
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(adapter);

        spinnerCourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Prevent Toast on first automated selection during initialization
                if (isInitialSelection) {
                    isInitialSelection = false;
                    return;
                }
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Selected: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupAlertButton() {
        btnAlert.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Alert Dialog")
                    .setMessage("Do you want to continue?")
                    .setPositiveButton("Yes", (dialog, which) ->
                            Toast.makeText(this, "You clicked YES", Toast.LENGTH_SHORT).show())
                    .setNegativeButton("No", (dialog, which) ->
                            Toast.makeText(this, "You clicked NO", Toast.LENGTH_SHORT).show())
                    .show();
        });
    }

    private void setupPopupButton() {
        btnPopup.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, btnPopup);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });

            popup.show();
        });
    }
}
