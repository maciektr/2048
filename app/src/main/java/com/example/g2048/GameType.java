package com.example.g2048;

import androidx.annotation.NonNull;

public class GameType {
    private int width;
    private int height;

    public GameType(int width,int height){
        this.width=width;
        this.height=height;

    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    @NonNull
    @Override
    public String toString() {
        return this.width+"x"+this.height;
    }
}
