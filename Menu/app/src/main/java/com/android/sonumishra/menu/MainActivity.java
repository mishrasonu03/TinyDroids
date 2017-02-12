package com.android.sonumishra.menu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void printToLogs(View view) {
        // Find first menu item TextView and print the text to the logs
        TextView menuItem1TextView = (TextView) findViewById(R.id.menu_item_1);
        Log.i("MainActivity.java", menuItem1TextView.getText().toString());

        // Find second menu item TextView and print the text to the logs
        TextView menuItem2TextView = (TextView) findViewById(R.id.menu_item_2);
        Log.i("MainActivity.java", menuItem2TextView.getText().toString());

        // Find third menu item TextView and print the text to the logs
        TextView menuItem3TextView = (TextView) findViewById(R.id.menu_item_3);
        Log.i("MainActivity.java", menuItem3TextView.getText().toString());

    }
}