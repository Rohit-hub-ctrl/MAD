package com.example.intent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextName);
        btn = findViewById(R.id.btnSend);

        btn.setOnClickListener(v -> {
            String name = editText.getText().toString();

            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("username", name);
            startActivity(intent);
        });
    }
}