package com.example.cioaca.graphdrawgame;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.io.OutputStream;
import java.util.ArrayList;

public class GraphView extends View {

    public ArrayList<Node> NodeList = new ArrayList<>();
    public ArrayList<Edge> EdgeList = new ArrayList<>();
    
    
    
    public Paint PaintNode = new Paint();
    public Paint PaintID = new Paint();
    public Paint PaintEdge = new Paint();
    public Paint PaintBitmap = new Paint();
    public Paint PaintCopy = new Paint();

    public Bitmap mBitmap;
    public Canvas mCanvas;
    public int View_w,View_h; // marimi view
    public float density;


    public float DIST_NODES;
    public float NODE_RADIUS;
    public float EDGE_TOLERANCE;
    public float OUTSIDE_SCREEN_TOLERANCE;
    public float EDGE_WIDTH;
    public float TEXT_SIZE;




    public int[][] Matrix = new int[51][51];

    
    
    public int nnr,mnr; // numar noduri si muchii


    public GraphView(Context context){
        super(context);
        Init(context);
    }

    
    public GraphView(Context context, AttributeSet attrs){
        super(context, attrs);
        Init(context);
    }
    
    
    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Init(context);
    }


    public void clear() {


        NodeList = new ArrayList<>();
        EdgeList = new ArrayList<>();
        mBitmap.eraseColor(Color.WHITE);

        for(int i = 1 ; i <= nnr ; i++)
            for(int j = 1 ; j<= nnr ; j++) Matrix[i][j] =0;

        nnr = 0;
        mnr = 0;

        invalidate();

    }


    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        View_w = w;
        View_h = h;

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mBitmap.eraseColor(Color.WHITE);

        String s = String.format("GraphView resized at %d %d", w, h);
        Log.w("Touch",s);
    }

    
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(Node nod : NodeList) {
            if (nod.ID > 9)
                mCanvas.drawText(Integer.toString(nod.ID), nod.x - 6 * density, nod.y + 3 * density, PaintID);
            else
                mCanvas.drawText(Integer.toString(nod.ID), nod.x, nod.y, PaintID);
        }


        canvas.drawBitmap(mBitmap, 0, 0, PaintBitmap);

    }



    boolean firstFinger = false ,secondFinger = false;
    int firstFingerNode,secondFingerNode;
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        int action_index = event.getActionIndex();
        int id = event.getPointerId(action_index);

        //Log.e("Touch","Number of pointers:" + event.getPointerCount());
        
        switch (event.getActionMasked()) {




            case MotionEvent.ACTION_DOWN:


                float eventX = event.getX(action_index);
                float eventY = event.getY(action_index);


                if ( NotNearNode(eventX, eventY) ) {
                    addNode(eventX, eventY);
                    invalidate();
                }

                else if (OnNode(eventX,eventY) != 0) {
                    firstFinger = true;
                    firstFingerNode = OnNode(eventX,eventY);
                    String s = String.format("First finger on(id:%d)",id);
                    Log.w("Touch", s);


                }

            break;


            case MotionEvent.ACTION_POINTER_DOWN:
               // int action_index = event.getActionIndex();
               // int id = event.getPointerId(action_index);
                float eventX_new = event.getX(action_index);
                float eventY_new = event.getY(action_index);


                if (NotNearNode(eventX_new, eventY_new)) {
                    addNode(eventX_new, eventY_new);
                    invalidate();
                }
                else if( OnNode(eventX_new,eventY_new) != 0) {
                    secondFinger = true;
                    secondFingerNode = OnNode(eventX_new,eventY_new);
                    String s = String.format("Second finger on(id:%d)",id);
                    Log.w("Touch", s);


                }

                if(event.getPointerCount() == 2 && secondFinger && firstFinger){

                    if( Matrix[firstFingerNode][secondFingerNode] == 0) {
                        addEdge(firstFingerNode, secondFingerNode);
                        secondFinger = false;
                        secondFingerNode = 0 ;
                        invalidate();
                    }

                }



                break;

            case MotionEvent.ACTION_POINTER_UP:
                secondFinger = false;
                secondFingerNode = 0;

                break;

            case MotionEvent.ACTION_UP:
                firstFinger = false;
                firstFingerNode = 0;

                break;


        }

        return true;


    }



    public void addNode(float x, float y){
        nnr++;
        Node nod = new Node();
        nod.x = x;
        nod.y = y;
        nod.ID = nnr;
        NodeList.add(nod);



        mCanvas.drawCircle(nod.x,nod.y,NODE_RADIUS,PaintNode);

        String s = String.format("New node added (%d)",nod.ID);
        Log.w("Touch", s);

    }

    public void addEdge(int ID1,int ID2) {
        Node nA,nB;
        nA = new Node();
        nB = new Node();
        for(Node nod: NodeList) {
            if(nod.ID == ID1 ) nA = nod;
            if(nod.ID == ID2)  nB = nod;
        }
        mnr++;

        Matrix[ID1][ID2] = 1;
        Matrix[ID2][ID1] = 1;

        Edge e = new Edge();
        e.start_x = nA.x;
        e.start_y = nA.y;
        e.end_x = nB.x;
        e.end_y = nB.y;
        EdgeList.add(e);




        mCanvas.drawLine(e.start_x, e.start_y, e.end_x, e.end_y,PaintEdge);
        String s = String.format("Edged drew between %d and %d", ID1, ID2);
        Log.w("Touch", s);


    }

    public void Init(Context context) {

        
        DisplayMetrics metrics = context.getResources().getDisplayMetrics(); //get metrics
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        density = context.getResources().getDisplayMetrics().density;


        String s = String.format("Screen size %d x %d and denisity %f", width,height,density);
        Log.w("Touch",s);

        //global sizes
        DIST_NODES = 50                 *density;
        NODE_RADIUS = 20                *density;
        EDGE_TOLERANCE = 15             *density;
        OUTSIDE_SCREEN_TOLERANCE = 30   *density;
        EDGE_WIDTH = 5                  *density;
        TEXT_SIZE = 20                  *density;

        s = String.format("Public constants init : %f %f %f %f", DIST_NODES,NODE_RADIUS,EDGE_TOLERANCE,OUTSIDE_SCREEN_TOLERANCE);
        Log.w("Touch",s);



        PaintNode.setColor(Color.BLACK); // init paints

        PaintID.setColor(Color.WHITE);
        PaintID.setTextSize(TEXT_SIZE);

        PaintEdge.setColor(Color.BLACK);
        PaintEdge.setStrokeWidth(EDGE_WIDTH);

        PaintCopy.setColor(Color.RED);
        PaintCopy.setTextSize(TEXT_SIZE*1.5f);
        PaintCopy.setAlpha(50);






    }

    public boolean NotNearNode(float x, float y) {
        for(Node nod : NodeList) {
            if ( (x - nod.x) * (x - nod.x)  + (y - nod.y)*(y - nod.y) < (NODE_RADIUS+DIST_NODES)*(NODE_RADIUS+DIST_NODES) ) return false;
        }

        if( !(x+NODE_RADIUS < View_w && x+NODE_RADIUS > OUTSIDE_SCREEN_TOLERANCE) || !(y+NODE_RADIUS < View_h && y+NODE_RADIUS > OUTSIDE_SCREEN_TOLERANCE) ) return false;





        return true;
    }

    public int OnNode(float x, float y) {
        for(Node nod : NodeList) {
            if ( (x - nod.x) * (x - nod.x)  + (y - nod.y)*(y - nod.y) < (NODE_RADIUS+EDGE_TOLERANCE)*(NODE_RADIUS+EDGE_TOLERANCE) ) return nod.ID;
        }


        return 0;
    }

    public void share(Context context){


        Bitmap icon = mBitmap; // sa clonez icon!

        Canvas canvas = new Canvas(icon);
        canvas.drawText(context.getResources().getString(R.string.copyright_share_text),20,40,PaintCopy);


        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);


        OutputStream outstream;
        try {
            outstream = context.getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(share, "Share Image"));
    }

}

