package com.example.a11;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editRollNo, editName;
    Button btnInsert, btnView, btnUpdate, btnDelete;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editRollNo = findViewById(R.id.rollNo);
        editName = findViewById(R.id.name);
        btnInsert = findViewById(R.id.btnInsert);
        btnView = findViewById(R.id.btnView);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Creating database and table
        db = openOrCreateDatabase("StudentDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR, name VARCHAR);");

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRollNo.getText().toString().trim().length() == 0 ||
                        editName.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Please enter all values", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.execSQL("INSERT INTO student VALUES('" + editRollNo.getText() + "','" + editName.getText() + "');");
                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                clearText();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRollNo.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Please enter Roll No", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollNo.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("DELETE FROM student WHERE rollno='" + editRollNo.getText() + "'");
                    Toast.makeText(MainActivity.this, "Record Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Roll No", Toast.LENGTH_SHORT).show();
                }
                c.close();
                clearText();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editRollNo.getText().toString().trim().length() == 0) {
                    Toast.makeText(MainActivity.this, "Please enter Roll No", Toast.LENGTH_SHORT).show();
                    return;
                }
                Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" + editRollNo.getText() + "'", null);
                if (c.moveToFirst()) {
                    db.execSQL("UPDATE student SET name='" + editName.getText() + "' WHERE rollno='" + editRollNo.getText() + "'");
                    Toast.makeText(MainActivity.this, "Record Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Roll No", Toast.LENGTH_SHORT).show();
                }
                c.close();
                clearText();
            }
        });

        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.rawQuery("SELECT * FROM student", null);
                if (c.getCount() == 0) {
                    showMessage("Error", "No records found");
                    return;
                }
                StringBuilder buffer = new StringBuilder();
                while (c.moveToNext()) {
                    buffer.append("Roll No: ").append(c.getString(0)).append("\n");
                    buffer.append("Name: ").append(c.getString(1)).append("\n\n");
                }
                showMessage("Student Details", buffer.toString());
                c.close();
            }
        });
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        editRollNo.setText("");
        editName.setText("");
        editRollNo.requestFocus();
    }
}
