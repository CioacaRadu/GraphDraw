package com.example.cioaca.graphdrawgame;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class PlayActivity extends ActionBarActivity {

    Level lvl = new Level();
    int score = 0 ;
    GraphView graph;
    TextView scoreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        int value = 0;
        score = sharedPref.getInt(getString(R.string.score_saved), value);


        setContentView(R.layout.activity_play);
        graph = (GraphView) findViewById(R.id.graph);
        scoreview = (TextView) findViewById(R.id.scoreview);
        if(score < 0) score = 0 ;
        scoreview.setText("Score:" + Integer.toString(score));
        lvl.start(this, Level.randInt(1,Level.NRLEVELS));
    }


    public void onDone(View view){

        if( lvl.check(graph.Matrix,graph.nnr,graph.mnr)  ){

            Toast toast = Toast.makeText(this, getResources().getString(R.string.level_done), Toast.LENGTH_SHORT);
            toast.show();
            recreate();

            score += 10;
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.score_saved), score);
            editor.apply();

        }
        else if (graph.nnr != 0) {

            Toast toast = Toast.makeText(this, getResources().getString(R.string.level_fail), Toast.LENGTH_SHORT);
            toast.show();
            recreate();
            score -= 10;
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.score_saved), score);
            editor.apply();

        }

    }

    public void restartmain(View view) {

        if (graph.nnr == 0) return;
        graph.clear();
        score -=5;
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.score_saved), score);
        editor.apply();
        if(score <0 ) score = 0 ;
        scoreview.setText("Score:" + Integer.toString(score));

    }










    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
