package com.example.cioaca.graphdrawgame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class DrawMenu extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_draw_menu, menu);
        return true;
    }



    public void graph(View view) {
        Intent intent = new Intent(this, GraphViewActivity.class);
        startActivity(intent);

    }

    public void graphcost(View view) {
        Intent intent = new Intent(this, GraphViewCActivity.class);
        startActivity(intent);

    }
}
