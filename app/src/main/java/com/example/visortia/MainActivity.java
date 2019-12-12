package com.example.visortia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public SharedPreferences pref;
    public int cursor;
    public static int nDel;
    Button bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // https://stackoverflow.com/a/22228198
        //note that you can use getPreferences(MODE_PRIVATE), but this is easier to use from Fragments.
        // SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences prefs =  getApplicationContext().getSharedPreferences("mPref", 0);
        // SharedPreferences.Editor editor = prefs.edit();
        nDel = prefs.getInt("num_del", 0);

        // https://stackoverflow.com/a/21810308
        prefs.edit().putInt("num_del", nDel).commit();

        prefs =  getApplicationContext().getSharedPreferences("cursor", 0);
        // SharedPreferences.Editor editor = prefs.edit();
        cursor = prefs.getInt("cursor", 0);

        // https://stackoverflow.com/a/21810308
        prefs.edit().putInt("cursor", cursor).commit();
        start();
    }

    void start() {
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Successfully pushed button!",
                        Toast.LENGTH_SHORT).show();
//                Sorter s = new Sorter();
                Intent i = new Intent(MainActivity.this, Sorter.class);
                startActivity(i);
            }
        });
    }
}
