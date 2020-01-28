package com.example.g2048;

import org.junit.Assert;
import org.junit.Test;

public class BoardTest {
    @Test
    public void testSetGetXY(){
        Board board = new Board(4,4);
        board.setCellValue(0,0,4);
        board.setCellValue(3,3,8);
        board.setCellValue(0,3,16);
        board.setCellValue(3,0,32);
        Assert.assertEquals(4,board.getCellValue(0,0));
        Assert.assertEquals(8,board.getCellValue(3,3));
        Assert.assertEquals(16,board.getCellValue(0,3));
        Assert.assertEquals(32,board.getCellValue(3,0));
    }
    @Test
    public void testSetGetVector(){
        Board board = new Board(4,4);
        board.setCellValue(new Vector2d(0,0),4);
        board.setCellValue(new Vector2d(3,3),8);
        board.setCellValue(new Vector2d(0,3),16);
        board.setCellValue(new Vector2d(3,0),32);
        Assert.assertEquals(4,board.getCellValue(new Vector2d(0,0)));
        Assert.assertEquals(8,board.getCellValue(new Vector2d(3,3)));
        Assert.assertEquals(16,board.getCellValue(new Vector2d(0,3)));
        Assert.assertEquals(32,board.getCellValue(new Vector2d(3,0)));
    }
}
