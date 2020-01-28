package com.example.g2048;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
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

    private LinkedList<Integer> getAllInAxis(int index, boolean reversed) {
        LinkedList<Integer> queue = new LinkedList<Integer>();

        int countInAxis = 0;
        for (int k = 0; k < this.height; k++) {
            if (this.board.getCellValue(index, k) > 0)
                countInAxis++;
        }

        for (int k = 0; k < this.height; k++) {
            if (this.board.getCellValue(index, k) > 0)
                queue.add(this.board.getCellValue(index, k));
            else if (countInAxis - queue.size() > 0)
                this.changed = true;
            this.board.setCellValue(index, k, 0);
        }

        if (reversed) {
            Stack<Integer> stack = new Stack<Integer>();
            while (!queue.isEmpty())
                stack.push(queue.poll());
            while (!stack.isEmpty())
                queue.add(stack.pop());
        }
        return queue;
    }

    private void setAxis(LinkedList<Integer> queue, int index, boolean reversed) {
        if (queue.size() > this.width)
            throw new IllegalArgumentException("There is too many objects in the queue " + queue.size());
        if (queue.size() == 0)
            return;

        int k = (reversed ? this.height - 1 : 0);
        this.board.setCellValue(index, k, queue.poll());

        while (!queue.isEmpty()) {
            int m = queue.poll();
            if (this.board.getCellValue(index, k) == m) {
                this.board.setCellValue(index, k, m * 2);
                changed = true;
                k += (reversed ? -1 : 1);
            } else {
                if (this.board.getCellValue(index, k) != 0)
                    k += (reversed ? -1 : 1);
                this.board.setCellValue(index, k, m);
            }
        }
    }

    private LinkedList<Integer> getAllInAxisHorizontal(int index, boolean reversed) {
        LinkedList<Integer> queue = new LinkedList<Integer>();

        int countInAxis = 0;
        for (int k = 0; k < this.width; k++) {
            if (this.board.getCellValue(k, index) > 0)
                countInAxis++;
        }

        int k = (reversed ? this.width - 1 : 0);
        while ((reversed && k >= 0) || (!reversed && k < this.width)) {

            if (this.board.getCellValue(k, index) > 0)
                queue.add(this.board.getCellValue(k, index));
            else if (countInAxis - queue.size() > 0)
                this.changed = true;

            this.board.setCellValue(k, index, 0);
            k += (reversed ? -1 : 1);
        }
        return queue;
    }

    private void setAxisHorizontal(LinkedList<Integer> queue, int index, boolean reversed) {
        if (queue.size() > this.height)
            throw new IllegalArgumentException("There is too many objects in the queue " + queue.size());
        if (queue.size() == 0)
            return;

        int k = (reversed ? this.width - 1 : 0);
        this.board.setCellValue(k, index, queue.poll());

        while (!queue.isEmpty()) {
            int m = queue.poll();
            if (this.board.getCellValue(k, index) == m) {
                this.board.setCellValue(k, index, m * 2);
                changed = true;
                k += (reversed ? -1 : 1);
            } else {
                if (this.board.getCellValue(k, index) != 0)
                    k += (reversed ? -1 : 1);
                this.board.setCellValue(k, index, m);
            }
        }
    }

    public void registerMove(Direction direction) {
        this.changed = false;
        switch (direction) {
            case UP:
                for (int i = 0; i < this.width; i++) {
                    LinkedList<Integer> queue = this.getAllInAxis(i, false);
                    if (queue.isEmpty())
                        continue;
                    this.setAxis(queue, i, false);
                }
                break;
            case DOWN:
                for (int i = 0; i < this.width; i++) {
                    LinkedList<Integer> queue = this.getAllInAxis(i, true);
                    if (queue.isEmpty())
                        continue;
                    this.setAxis(queue, i, true);
                }
                break;
            case RIGHT:
                for (int i = 0; i < this.height; i++) {
                    LinkedList<Integer> queue = this.getAllInAxisHorizontal(i, true);
                    if (queue.isEmpty())
                        continue;
                    this.setAxisHorizontal(queue, i, true);
                }
                break;
            case LEFT:
                for (int i = 0; i < this.height; i++) {
                    LinkedList<Integer> queue = this.getAllInAxisHorizontal(i, false);
                    if (queue.isEmpty())
                        continue;
                    this.setAxisHorizontal(queue, i, false);
                }
                break;
        }
        if (changed)
            this.board.placeRandomLowest();
        this.refreshVisualisation();
    }
}
