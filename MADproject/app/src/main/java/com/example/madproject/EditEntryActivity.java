package com.example.madproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class EditEntryActivity extends AppCompatActivity {

    EditText item, qty, cal;
    AutoCompleteTextView unitSpinner;
    Button updateBtn;
    DatabaseHelper dbHelper;
    int entryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        item = findViewById(R.id.editItem);
        qty = findViewById(R.id.editQty);
        cal = findViewById(R.id.editCal);
        unitSpinner = findViewById(R.id.editUnitSpinner);
        updateBtn = findViewById(R.id.updateBtn);

        dbHelper = new DatabaseHelper(this);

        // Setup Unit Spinner
        String[] units = {"pcs", "g", "kg", "ml", "l", "cup", "plate"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, units);
        unitSpinner.setAdapter(unitAdapter);

        entryId = getIntent().getIntExtra("id", -1);

        if (entryId != -1) {
            loadEntryData();
        }

        updateBtn.setOnClickListener(v -> {
            String newItem = item.getText().toString().trim();
            String qtyStr = qty.getText().toString().trim();
            String calStr = cal.getText().toString().trim();
            String unit = unitSpinner.getText().toString();

            if(newItem.isEmpty() || qtyStr.isEmpty() || calStr.isEmpty()){
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double newQty = Double.parseDouble(qtyStr);
                int calPerUnit = Integer.parseInt(calStr);
                int total = (int) (newQty * calPerUnit);

                SQLiteDatabase db = dbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("item", newItem);
                values.put("quantity", qtyStr + " " + unit);
                values.put("calories", total);

                db.update("entries", values, "id=?", new String[]{String.valueOf(entryId)});

                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
                finish();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadEntryData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT item, quantity, calories FROM entries WHERE id=?",
                new String[]{String.valueOf(entryId)}
        );

        if(cursor.moveToFirst()){
            String itemName = cursor.getString(0);
            String qtyFull = cursor.getString(1);
            int totalCalories = cursor.getInt(2);
            
            item.setText(itemName);
            
            // Split quantity and unit
            String[] parts = qtyFull.split(" ");
            if (parts.length >= 2) {
                qty.setText(parts[0]);
                unitSpinner.setText(parts[1], false);
            } else {
                qty.setText(qtyFull);
            }

            try {
                double quantityVal = Double.parseDouble(parts[0]);
                if (quantityVal > 0) {
                    cal.setText(String.valueOf((int)(totalCalories / quantityVal)));
                }
            } catch (Exception e) {
                cal.setText("0");
            }
        }
        cursor.close();
    }
}