package com.example.madproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class AddEntryActivity extends AppCompatActivity {

    EditText itemName, quantity, caloriesPerUnit;
    AutoCompleteTextView unitSpinner;
    Button addBtn;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        itemName = findViewById(R.id.itemName);
        quantity = findViewById(R.id.quantity);
        caloriesPerUnit = findViewById(R.id.caloriesPerUnit);
        unitSpinner = findViewById(R.id.unitSpinner);
        addBtn = findViewById(R.id.addBtn);

        dbHelper = new DatabaseHelper(this);

        // Setup Unit Spinner
        String[] units = {"pcs", "g", "kg", "ml", "l", "cup", "plate"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, units);
        unitSpinner.setAdapter(unitAdapter);
        unitSpinner.setText(units[0], false); // Default to 'pcs'

        addBtn.setOnClickListener(view -> {
            String item = itemName.getText().toString().trim();
            String qtyStr = quantity.getText().toString().trim();
            String calStr = caloriesPerUnit.getText().toString().trim();
            String unit = unitSpinner.getText().toString();

            if(item.isEmpty() || qtyStr.isEmpty() || calStr.isEmpty()){
                Toast.makeText(AddEntryActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double qty = Double.parseDouble(qtyStr);
                int cal = Integer.parseInt(calStr);
                int totalCalories = (int) (qty * cal);

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                String currentDate = getIntent().getStringExtra("date");
                if(currentDate == null){
                    currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                }

                ContentValues values = new ContentValues();
                values.put("item", item);
                values.put("quantity", qtyStr + " " + unit);
                values.put("calories", totalCalories);
                values.put("date", currentDate);

                db.insert("entries", null, values);

                Toast.makeText(AddEntryActivity.this, "Saved: " + totalCalories + " kcal", Toast.LENGTH_SHORT).show();
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(AddEntryActivity.this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        });
    }
}