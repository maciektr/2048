package com.example.g2048;

import java.util.Random;

public class Board {
    private final int lowest = 2;
    private final Random rand = new Random();
    private int[][] board;
    private int width;
    private int height;
    private int result=0;

    public Board(int width, int height) {
        if (width * height < 4 || width <= 1 || height <= 1) {
            throw new IllegalArgumentException("Board size too small! " + width + "x" + height);
        }

        this.board = new int[width][height];
        this.width = width;
        this.height = height;
        this.placeRandomLowest();
        this.placeRandomLowest();
    }
    public Board(Board other){
        this.width = other.width;
        this.height = other.height;
        this.result = other.result;
        this.board = new int[other.board.length][];
        for(int i =0; i<other.board.length; i++)
            this.board[i] = other.board[i].clone();

    }

    public int getResult(){
        return this.result;
    }
    public void updateResult(int val){
        this.result+=val;
    }

    public void setCellValue(Vector2d pos, int value) {
        this.board[pos.x][pos.y] = value;
    }

    public void setCellValue(int x, int y, int value) {
        this.board[x][y] = value;
    }

    public void placeRandomLowest() {
        Vector2d pos;
        do {
            pos = this.randomPos();
        } while (this.getCellValue(pos) != 0);
        this.setCellValue(pos, this.lowest);
    }

    private Vector2d randomPos() {
        return new Vector2d(this.rand.nextInt(this.width), this.rand.nextInt(this.height));
    }

    public int getCellValue(int x, int y) {
        return this.board[x][y];
    }

    public int getCellValue(Vector2d pos) {
        return this.board[pos.x][pos.y];
    }


}
