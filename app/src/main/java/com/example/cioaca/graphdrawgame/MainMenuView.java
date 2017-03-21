package com.example.cioaca.graphdrawgame;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MainMenuView extends View {

    float density;
    public final int NUMBER_BUTTONS = 3;
    public float RADIUS;

    int width,height;

    Paint Default_Paint = new Paint();
    Paint Text_Paint = new Paint();
    Paint PressedText_Paint = new Paint();
    Paint Pressed_Paint = new Paint();
    Paint Line_Paint = new Paint();


    ArrayList<MenuButton> ButtonList = new ArrayList<>();


    public class MenuButton {
        String text;
        public float x,y;
        public boolean Pressed = false;


    }


    public MainMenuView(Context context){
        super(context);
        Init(context);


    }


    public MainMenuView(Context context, AttributeSet attrs){
        super(context, attrs);
        Init(context);

    }


    public MainMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Init(context);

    }



    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(MenuButton b : ButtonList) {

            if(! b.Pressed ) {
                canvas.drawCircle(b.x, b.y, RADIUS, Default_Paint);
                canvas.drawText(b.text, b.x-18*density, b.y, Text_Paint);
            }
            else {



                for(MenuButton b1 : ButtonList) {
                    if(!b1.Pressed) {
                        canvas.drawLine(b1.x,b1.y,b.x-5,b.y,Line_Paint);
                    }

                }
                canvas.drawCircle(b.x, b.y, RADIUS, Pressed_Paint);
                //canvas.drawText(b.text, b.x, b.y, PressedText_Paint);




            }

        }

    }


    public boolean onTouchEvent(@NonNull MotionEvent event) {

        float eventX = event.getX();
        float eventY = event.getY();




        switch (event.getAction()) {




            case MotionEvent.ACTION_DOWN:

                if( OnButton(eventX,eventY) != null ) {

                    MenuButton b = OnButton(eventX,eventY);
                    b.Pressed = true;

                    invalidate();
                }




                break;

            case MotionEvent.ACTION_UP:

                if(ButtonList.get(0).Pressed) {


                    Intent intent = new Intent(getContext(), DrawMenu.class);
                    getContext().startActivity(intent);


                }

                if(ButtonList.get(1).Pressed) {

                    Intent intent = new Intent(getContext(), PlayActivity.class);
                    getContext().startActivity(intent);

                }





                if(ButtonList.get(2).Pressed) {

                    Intent intent = new Intent(getContext(), CreditsActivity.class);
                    getContext().startActivity(intent);

                }

                for(MenuButton b : ButtonList) {
                    if (b.Pressed) b.Pressed = false;
                }

                invalidate();
                break;


        }

        return true;


    }

    public void Init(Context context) {


        String text1 = getResources().getString(R.string.main_button1);
        String text2 = getResources().getString(R.string.main_button2);
        String text3 = getResources().getString(R.string.main_button3);

        //this.setBackgroundColor(Color.GREEN);
        density = context.getResources().getDisplayMetrics().density;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics(); //get metrics
        width = metrics.widthPixels;
        height = metrics.heightPixels;


        RADIUS = 50 * density;


        for(int i = 1 ; i <= NUMBER_BUTTONS ; i++) {

            MenuButton b = new MenuButton();
            ButtonList.add(b);

        }
        //metrics
        ButtonList.get(0).x = 0+width*0.2f; ButtonList.get(0).y = height*0.15f; ButtonList.get(0).text = text1;
        ButtonList.get(1).x = width-width*0.2f; ButtonList.get(1).y = height/2 - height*0.1f; ButtonList.get(1).text = text2;
        ButtonList.get(2).x = 0+width*0.2f; ButtonList.get(2).y = height-height*0.3f; ButtonList.get(2).text = text3;

        //paints

        Pressed_Paint.setColor(Color.WHITE);
        Text_Paint.setColor(Color.WHITE);
        Text_Paint.setTextSize(20*density);

        Line_Paint.setStrokeWidth(8*density);





    }

    public MenuButton OnButton(float x, float y) {

        for(MenuButton b : ButtonList) {
            if ( (x - b.x) * (x - b.x)  + (y - b.y)*(y - b.y) < (RADIUS)*(RADIUS) ) return b;
        }


        return null;
    }

}
