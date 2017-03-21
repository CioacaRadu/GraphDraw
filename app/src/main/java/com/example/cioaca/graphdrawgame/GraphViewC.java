package com.example.cioaca.graphdrawgame;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class GraphViewC extends GraphView{

    public Paint PaintCost = new Paint();
    public Paint PaintRect = new Paint();
    Activity activity;



    public GraphViewC(Context context){
        super(context);
        PaintCost.setColor(Color.RED);
        PaintCost.setTextSize(TEXT_SIZE -5);
        PaintCost.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        PaintRect.setColor(Color.YELLOW);
        activity = (Activity) context;

    }


    public GraphViewC(Context context, AttributeSet attrs){
        super(context, attrs);
        PaintCost.setColor(Color.RED);
        PaintCost.setTextSize(TEXT_SIZE -5);
        PaintCost.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        PaintRect.setColor(Color.YELLOW);
        activity = (Activity) context;

    }


    public GraphViewC(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs,defStyle);
        PaintCost.setColor(Color.RED);
        PaintCost.setTextSize(TEXT_SIZE -5);
        PaintCost.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        PaintRect.setColor(Color.YELLOW);
        activity = (Activity) context;

    }


    @Override
    public void addEdge(final int ID1,final int ID2) {


       // CostDialog costdialog = new CostDialog();
        // costdialog.show(activity.getFragmentManager(),"CostDialog");

        LayoutInflater i = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View costdialog_view = i.inflate(R.layout.costdialog,null);
        String title = getResources().getString(R.string.cost_dialog_title);

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setView(costdialog_view)
                .setNegativeButton(R.string.cost_dialog_negative, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.cost_dialog_positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        EditText edittext = (EditText) costdialog_view.findViewById(R.id.input_cost);

                        if (edittext.getText().toString().length() != 0) {
                            int input;
                            try{
                                input = Integer.parseInt(edittext.getText().toString());
                            }catch(Exception ex){
                                input = 0;
                            }

                            if (input < 999 && input > 0) {
                                Node nA, nB;
                                nA = new Node();
                                nB = new Node();
                                for (Node nod : NodeList) {
                                    if (nod.ID == ID1) nA = nod;
                                    if (nod.ID == ID2) nB = nod;
                                }
                                mnr++;
                                Matrix[ID1][ID2] = input;
                                Matrix[ID2][ID1] = input;

                                Edge e = new Edge();
                                e.cost = input;
                                e.start_x = nA.x;
                                e.start_y = nA.y;
                                e.end_x = nB.x;
                                e.end_y = nB.y;
                                EdgeList.add(e);


                                mCanvas.drawLine(e.start_x, e.start_y, e.end_x, e.end_y, PaintEdge);
                                float A = (e.start_x + e.end_x) / 2 - 20 * density;
                                float B = (e.start_y + e.end_y) / 2 - 20 * density;
                                float C = (e.start_x + e.end_x) / 2 + 10 * density;
                                float D = (e.start_y + e.end_y) / 2 + 10 * density;


                                mCanvas.drawRect(A, B, C, D, PaintRect);
                                if (e.cost < 100) {
                                    mCanvas.drawText(Integer.toString(e.cost), (e.start_x + e.end_x) / 2 - 10 * density, (e.start_y + e.end_y) / 2, PaintCost);
                                } else
                                    mCanvas.drawText(Integer.toString(e.cost), (e.start_x + e.end_x) / 2 - 17 * density, (e.start_y + e.end_y) / 2, PaintCost);


                                String s = String.format("Edged drew between %d and %d with cost %d", ID1, ID2, e.cost);
                                Log.w("Touch", s);
                            } else {

                                Toast toast = Toast.makeText(activity, getResources().getString(R.string.cost_dialog_toobig), Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();







    }

}
