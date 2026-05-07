package com.example.a10;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private EditText editTextData;
    private Button buttonSave;
    private TextView textViewStatus;
    private final String filename = "myInternalFile.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextData = findViewById(R.id.editTextData);
        buttonSave = findViewById(R.id.buttonSave);
        textViewStatus = findViewById(R.id.textViewStatus);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editTextData.getText().toString();
                saveData(data);
            }
        });
    }

    private void saveData(String data) {
        try {
            FileOutputStream fOut = openFileOutput(filename, Context.MODE_PRIVATE);
            fOut.write(data.getBytes());
            fOut.close();
            
            textViewStatus.setText("Data saved to " + filename);
            Toast.makeText(getBaseContext(), "File saved successfully!", Toast.LENGTH_SHORT).show();
            editTextData.setText(""); // Clear input after saving
        } catch (Exception e) {
            e.printStackTrace();
            textViewStatus.setText("Error saving data: " + e.getMessage());
        }
    }
}
