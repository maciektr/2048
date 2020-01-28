package com.example.g2048;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import java.util.LinkedList;

public class Simulation {
    private final int width;
    private final int height;

    private Board board;
    private TextView[][] visualisation;
    private Activity activity;
//    private int result = 0;
    private int record;
    private TextView resultView;
    private TextView recordView;
    private SharedPreferences preferences;
    private Board lastBoard;
    private boolean undoLegal = false;

    private final Drawable darkCellColor;
    private final Drawable brightCellColor;

    public Simulation(Context context, GameType type) {
        this.width = type.getWidth();
        this.height = type.getHeight();

        this.board = new Board(width, height);
        this.lastBoard = new Board(this.board);
        this.activity = (Activity) context;
        this.brightCellColor = this.activity.getResources().getDrawable(R.drawable.back_bright);
        this.darkCellColor = this.activity.getResources().getDrawable(R.drawable.back_dark);

        this.visualisation = new TextView[width][height];
        for (int i = 0; i < width; i++)
            for (int k = 0; k < height; k++)
                this.visualisation[i][k] = (TextView) this.activity.findViewById(this.activity.getResources().getIdentifier(Integer.toString(k * width + i + 1), "id", this.activity.getPackageName()));
        this.resultView = (TextView) this.activity.findViewById(R.id.result);

        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.record = this.preferences.getInt("highest_score", 0);
        this.recordView = (TextView) this.activity.findViewById(R.id.highest_score);

        this.refreshVisualisation();
    }

    private void updateRecord(int newRec) {
        if (newRec > this.record) {
            this.record = newRec;
            SharedPreferences.Editor editor = this.preferences.edit();
            editor.putInt("highest_score", this.record);
            editor.apply();
        }
    }

    private void refreshVisualisation() {
        for (int i = 0; i < width; i++)
            for (int k = 0; k < height; k++) {
//                this.visualisation[i][k] = (TextView) this.activity.findViewById(this.activity.getResources().getIdentifier("cell" + Integer.toString(k * width + i + 1), "id", this.activity.getPackageName()));
                if (this.visualisation[i][k] == null)
                    throw new IllegalArgumentException("There is no such TextView cell defined " + i + ", " + k);
                if (this.board.getCellValue(i, k) > 0) {
                    this.visualisation[i][k].setText(Integer.toString(this.board.getCellValue(i, k)));
                    this.visualisation[i][k].setBackground(this.brightCellColor);
                }else {
                    this.visualisation[i][k].setText(" ");
                    this.visualisation[i][k].setBackground(this.darkCellColor);
                }
            }
        this.resultView.setText("Wynik:\n" + Integer.toString(this.board.getResult()));
        this.updateRecord(this.board.getResult());
        this.recordView.setText("Rekord:\n" + Integer.toString(this.record));
    }

    private boolean changed = false;

    private LinkedList<Integer> getAllInAxis(int index) {
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
        return queue;
    }

    private void setAxis(LinkedList<Integer> queue, int index, boolean reversed) {
        if (queue.size() > this.width)
            throw new IllegalArgumentException("There is too many objects in the queue " + queue.size());
        if (queue.size() == 0)
            return;

        int k = (reversed ? this.height - 1 : 0);
        int m;
        if (reversed)
            m = queue.removeLast();
        else
            m = queue.poll();
        this.board.setCellValue(index, k, m);

        while (!queue.isEmpty()) {
            if (reversed)
                m = queue.removeLast();
            else
                m = queue.poll();
            if (this.board.getCellValue(index, k) == m) {
                this.board.setCellValue(index, k, m * 2);
                this.board.updateResult(m * 2);
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
                this.board.updateResult(m * 2);
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
        Board boardBefore = new Board(this.board);
        switch (direction) {
            case UP:
                for (int i = 0; i < this.width; i++) {
                    LinkedList<Integer> queue = this.getAllInAxis(i);
                    if (queue.isEmpty())
                        continue;
                    this.setAxis(queue, i, false);
                }
                break;
            case DOWN:
                for (int i = 0; i < this.width; i++) {
                    LinkedList<Integer> queue = this.getAllInAxis(i);
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
        if (changed) {
            this.board.placeRandomLowest();
            this.lastBoard = boardBefore;
            this.undoLegal = true;
        }
        this.refreshVisualisation();
    }

    public void undo(){
        if(!this.undoLegal)
            return;
        this.board = this.lastBoard;
        this.undoLegal = false;
        this.refreshVisualisation();
    }
}
