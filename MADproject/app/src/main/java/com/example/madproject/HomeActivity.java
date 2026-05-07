package com.example.madproject;

import android.content.Intent;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.*;

public class HomeActivity extends AppCompatActivity {

    ListView listView;
    TextView totalCalories, dateText;
    ExtendedFloatingActionButton addNewBtn;
    MaterialCardView calendarCard;
    ImageButton logoutBtn;

    ArrayList<Integer> ids;
    ArrayList<String> list;
    DatabaseHelper dbHelper;
    ArrayAdapter<String> adapter;

    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listView = findViewById(R.id.listView);
        totalCalories = findViewById(R.id.totalCalories);
        dateText = findViewById(R.id.dateText);
        addNewBtn = findViewById(R.id.addNewBtn);
        calendarCard = findViewById(R.id.calendarCard);
        logoutBtn = findViewById(R.id.logoutBtn);

        dbHelper = new DatabaseHelper(this);
        list = new ArrayList<>();
        ids = new ArrayList<>();

        selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        updateDateDisplay();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                list
        );
        listView.setAdapter(adapter);

        addNewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddEntryActivity.class);
            intent.putExtra("date", selectedDate);
            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position < ids.size()) {
                Intent intent = new Intent(HomeActivity.this, EditEntryActivity.class);
                intent.putExtra("id", ids.get(position));
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position < ids.size()) {
                showDeleteDialog(position);
            }
            return true;
        });

        calendarCard.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(
                    HomeActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar selected = Calendar.getInstance();
                        selected.set(year, month, dayOfMonth);
                        selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(selected.getTime());
                        updateDateDisplay();
                        loadData();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            dialog.show();
        });

        logoutBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        loadData();
    }

    private void updateDateDisplay() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        if (selectedDate.equals(today)) {
            dateText.setText("Today");
        } else {
            dateText.setText(selectedDate);
        }
    }

    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int entryId = ids.get(position);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete("entries", "id=?", new String[]{String.valueOf(entryId)});
                    Toast.makeText(HomeActivity.this, "Entry Deleted", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        list.clear();
        ids.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, item, quantity, calories FROM entries WHERE date=?",
                new String[]{selectedDate}
        );

        int total = 0;
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String item = cursor.getString(1);
            String qty = cursor.getString(2);
            int cal = cursor.getInt(3);

            ids.add(id);
            list.add(item + " (" + qty + ")\n" + cal + " kcal");
            total += cal;
        }
        cursor.close();

        totalCalories.setText(String.valueOf(total));
        adapter.notifyDataSetChanged();
    }
}