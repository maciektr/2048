package com.example.g2048;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
//        TextView cell1 = (TextView)findViewById(R.id.cell1);
        for (int i = 0; i < width; i++)
            for (int k = 0; k < height; k++)
                this.visualisation[i][k] = (TextView) this.activity.findViewById(this.activity.getResources().getIdentifier("cell" + Integer.toString(k * width + i + 1), "id", this.activity.getPackageName()));
        this.refreshVisualisation();
    }

    private void refreshVisualisation() {
        for (int i = 0; i < width; i++)
            for (int k = 0; k < height; k++) {
//                this.visualisation[i][k] = (TextView) this.activity.findViewById(this.activity.getResources().getIdentifier("cell" + Integer.toString(k * width + i + 1), "id", this.activity.getPackageName()));
                if (this.visualisation[i][k] == null)
                    throw new IllegalArgumentException("There is no such TextView cell defined " + i + ", " + k);
                if (this.board.getCellValue(i, k) > 0)
                    this.visualisation[i][k].setText(Integer.toString(this.board.getCellValue(i, k)));
                else
                    this.visualisation[i][k].setText(" ");
            }
    }

    private boolean changed = false;

    private LinkedList<Integer> getAllInAxis(int index, boolean vertical, boolean reversed) {
        LinkedList<Integer> queue = new LinkedList<Integer>();

        int countInAxis = 0;
        for (int k = 0; k < this.height; k++) {
            if (this.board.getCellValue(index, k) > 0)
                countInAxis++;
        }

        int k = (reversed ? this.height-1: 0);
        while ((reversed && k >= 0) || (!reversed && k < this.height)) {

            if (this.board.getCellValue(index, k) > 0)
                queue.add(this.board.getCellValue(index, k));
            else if (countInAxis - queue.size() > 0)
                this.changed = true;

            this.board.setCellValue(index, k, 0);
            k += (reversed ? -1:1);
        }
        return queue;
    }

    private void setAxis(LinkedList<Integer> queue, int index, boolean reversed){
        if(queue.size() > 4)
             throw new IllegalArgumentException("There is too many objects in the queue " + queue.size());
        if(queue.size() == 0)
            return;

        int k = (reversed ? this.height -1 : 0);
        this.board.setCellValue(index, k, queue.poll());

        while (!queue.isEmpty()) {
            int m = queue.poll();
            if (this.board.getCellValue(index, k) == m) {
                this.board.setCellValue(index, k, m * 2);
                changed = true;
                k += (reversed ? -1:1);
            }else{
                k += (reversed ? -1:1);
                this.board.setCellValue(index, k, m);
            }
        }
    }

    private void setAll(boolean vertical, boolean reversed){
        for(int i = 0;  i<this.width; i++){
            LinkedList<Integer> queue = this.getAllInAxis(i, vertical, reversed);
            if (queue.isEmpty())
                continue;
            this.setAxis(queue,i,reversed);
        }
    }

    public void registerMove(Direction direction) {
        this.changed = false;
        switch (direction) {
            case UP:
                this.setAll(true, false);
                break;
            case DOWN:
                this.setAll(true, true);
            case LEFT:
                break;
            case RIGHT:
                break;
        }
        if (changed)
            this.board.placeRandomLowest();
        this.refreshVisualisation();
    }
}
