package com.example.a8;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Create Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(
                R.menu.menu_main,
                menu
        );

        return true;
    }

    // Handle Menu Click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item1) {

            Toast.makeText(
                    this,
                    "Item 1 Selected",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }

        else if (id == R.id.item2) {

            Toast.makeText(
                    this,
                    "Item 2 Selected",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }

        else if (id == R.id.item3) {

            Toast.makeText(
                    this,
                    "Item 3 Selected",
                    Toast.LENGTH_SHORT
            ).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}