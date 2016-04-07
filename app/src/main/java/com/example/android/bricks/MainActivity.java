package com.example.android.bricks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
//import android.widget.AdapterView;
import android.widget.Button;
//import android.widget.GridView;
//import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//   GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button myLevelsButton = (Button)findViewById(R.id.button_levels);
        myLevelsButton.setOnClickListener(this);

    /*    myLevelsButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, PlayButton.class);
                        startActivity(intent);
                    }
                }
        ); */

    //    gridView = (GridView)findViewById(R.id.activity_main_gridView);
    //    gridView.setAdapter(new ImageAdapter(this));
    //    gridView.setOnClickListener(this);
    }

/*

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId()==R.id.activity_main_gridView) {

            switch (position) {

                case 0: {
                    startActivity(new Intent(this,AnimationActivity.class));
                    break;
                }
                case 1: {
                    startActivity(new Intent(this, PlayButton.class));
                    break;
                }
                default: {
                    Toast.makeText(getApplicationContext(), "No action associated with this item.", Toast.LENGTH_LONG).show();
                    break;
                }
            }

        }
    } */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_rules) {
            Intent intentRules = new Intent(this, RulesActivity.class);
            intentRules.putExtra("Message", "Do not let the ball drop by bouncing it off your bar thing at the bottom. " +
                    "The different colors of the bricks means how many times you have to hit them.");
            startActivity(intentRules);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_levels: {
                Intent intent = new Intent(this, AnimationCanvasActivity.class);
                startActivity(intent);
            }
        }
    }



  public void sendMessage(View view) {
        switch(view.getId()){
            case R.id.button_play: {
                Intent intent = new Intent(this.getString(R.string.CUSTOM_ACTION_PLAYBUTTON));
                startActivity(intent);
                break;
            }
        }
    }



}
