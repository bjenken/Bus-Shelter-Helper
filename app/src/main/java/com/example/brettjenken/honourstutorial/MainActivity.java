package com.example.brettjenken.honourstutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.example.brettjenken.honourstutorial.Ui.Stop.StopsListActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //immediately switch show the stop list
        Intent intent = new Intent(MainActivity.this, StopsListActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
