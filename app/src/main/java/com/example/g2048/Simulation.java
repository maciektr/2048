package com.example.g2048;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

public class Simulation {
    private final int width = 4;
    private final int height = 4;

    private Board board;
    private TextView[][] visualisation;
    private Activity activity;

    public Simulation(Context context) {
        this.board = new Board(width, height);

        this.activity = (Activity) context;
        this.visualisation = new TextView[width][height];
        this.refreshVisualisation();
//        TextView cell1 = (TextView)findViewById(R.id.cell1);
    }

    private void refreshVisualisation(){
        for (int i = 0; i < width; i++)
            for (int k = 0; k < height; k++) {
                this.visualisation[i][k] = (TextView) this.activity.findViewById(this.activity.getResources().getIdentifier("cell" + Integer.toString(k * width + i +1), "id", this.activity.getPackageName()));
                if(this.visualisation[i][k] == null)
                    throw new IllegalArgumentException("There is no such TextView cell defined "+i+", "+k);
                if(this.board.getCellValue(i,k) > 0)
                    this.visualisation[i][k].setText(Integer.toString(this.board.getCellValue(i,k)));
                else
                    this.visualisation[i][k].setText(" ");
            }
    }

    public void registerMove(Direction direction){
        switch(direction){
            case UP:

                break;
            case DOWN:
                break;
            case LEFT:
                break;
            case RIGHT:
                break;
        }
        this.board.placeRandomLowest();
        this.refreshVisualisation();
    }
}
