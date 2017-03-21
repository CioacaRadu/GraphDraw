package com.example.cioaca.graphdrawgame;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import java.util.Random;

public class Level {

    int nr_nodes;
    int nr_edges;
    int nr_conex;
    int type;
    final static int NRLEVELS = 3;

    public void start(Activity activity , int type){

        TextView level_message = (TextView) activity.findViewById(R.id.level_message);
        String message="NO MESSAGE LOADED <DEVELOPER ERROR>";
        this.type = type;



        switch(type) {

            case 1: { // nr noduri si nr muchii

                nr_nodes = randInt(2, 15);
                nr_edges = nr_nodes - randInt(1, nr_nodes);

                String text1 = activity.getResources().getString(R.string.level1_text1);
                String text2 = activity.getResources().getString(R.string.level1_text2);
                String text3 = activity.getResources().getString(R.string.level1_text3);

                message = String.format("%s %d %s %d %s", text1, nr_nodes, text2, nr_edges, text3);

                break;
            }
            case 2 : { //arbore cu nr noduri

                nr_nodes = randInt(2, 11);

                String text1 = activity.getResources().getString(R.string.level2_text1);
                String text2 = activity.getResources().getString(R.string.level2_text2);


                message = String.format("%s %d %s", text1, nr_nodes, text2);


                break;
            }

            case 3: { // nr noduri de grad impar

                nr_nodes = 1;
                while(nr_nodes %2 == 1) { //in orice graf exista un nr par de noduri de grad impar

                    nr_nodes = randInt(1,8);
                }

                String text1 = activity.getResources().getString(R.string.level3_text1);
                String text2 = activity.getResources().getString(R.string.level3_text2);



                message= String.format("%s %d %s",text1,nr_nodes,text2);


            }


        }

        level_message.setText(message);


    }


    public static int randInt(int min, int max) {


        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }


    public boolean check(int a[][],int n,int m) {
        switch(type) {

            case 1: //nr noduri si nr muchii

                if(n == nr_nodes && m == nr_edges) return true;

                break;


            case 2: //arbore cu nr noduri

                if(n==nr_nodes && m == nr_nodes-1) return true;


                break;

            case 3: // nr noduri de grad impar

                int g,nr=0;
                for(int i = 1 ; i <=n ; i++) {
                    g = 0;
                    for (int j = 1; j <= n; j++) g +=a[i][j];
                    if( g%2 == 1 ) nr++;
                }

                if(nr == nr_nodes) return true;


                break;




        }


        return false;
    }
}
