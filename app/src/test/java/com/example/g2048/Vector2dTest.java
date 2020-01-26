package com.example.g2048;

import com.example.g2048.Vector2d;
import org.junit.Assert;
import org.junit.Test;

public class Vector2dTest {
    private final Vector2d vector_1_1 = new Vector2d(1,1);
    private final Vector2d vector_1_2 = new Vector2d(1,2);
    private final Vector2d vector_2_1 = new Vector2d(2,1);
    private final Vector2d vector_0_0 = new Vector2d(0,0);
    private final Vector2d vector_0_2 = new Vector2d(0,2);
    private final Vector2d vector_1_0 = new Vector2d(1,0);
    private final Vector2d vector_2_2 = new Vector2d(2,2);

    @Test
    public void testEquals(){
        Assert.assertEquals(vector_1_2,vector_0_2.add(vector_1_0));
        Assert.assertNotEquals(vector_1_2, new Vector2d(-1,-2));
    }

    @Test
    public void testToString(){
        Assert.assertEquals("(1,2)", vector_1_2.toString());
        Assert.assertNotEquals("(1,2)", new Vector2d(-1,-2).toString());
    }

    @Test
    public void testPrecedes(){
        Assert.assertTrue(new Vector2d(-1,-2).precedes(vector_1_2));
        Assert.assertFalse(vector_0_0.precedes(vector_0_0));
        Assert.assertFalse(vector_1_2.precedes(new Vector2d(-1,-2)));
    }

    @Test
    public void testFollows(){
        Assert.assertFalse(new Vector2d(-1,-2).follows(vector_1_2));
        Assert.assertTrue(vector_0_0.follows(vector_0_0));
        Assert.assertTrue(vector_1_2.follows(new Vector2d(-1,-2)));
    }

    @Test
    public void testUpperRight(){
        Assert.assertEquals(vector_2_2,vector_1_2.upperRight(vector_2_1));
        Assert.assertNotEquals(vector_1_1,vector_1_2.upperRight(vector_2_1));
    }

    @Test
    public void testLowerLeft(){
        Assert.assertNotEquals(vector_2_2,vector_1_2.lowerLeft(vector_2_1));
        Assert.assertEquals(vector_1_1,vector_1_2.lowerLeft(vector_2_1));
    }

    @Test
    public void testAdd(){
        Assert.assertNotEquals(vector_2_2,vector_1_2.add(vector_2_1));
        Assert.assertEquals(vector_1_1,new Vector2d(-1,2).add(new Vector2d(2,-1)));
        Assert.assertEquals(vector_0_0,vector_0_0.add(vector_0_0));
    }

    @Test
    public void testSubtract(){
        Assert.assertEquals(new Vector2d(-1,1),vector_1_2.substract(vector_2_1));
        Assert.assertNotEquals(vector_1_1,new Vector2d(-1,2).substract(new Vector2d(2,-1)));
        Assert.assertEquals(vector_0_0,vector_0_0.substract(vector_0_0));
    }

    @Test
    public void testOpposite(){
        Assert.assertEquals(new Vector2d(-1,-1), vector_1_1.opposite());
        Assert.assertEquals(vector_0_0, vector_0_0.opposite());
        Assert.assertNotEquals(vector_1_1, vector_1_1.opposite());
    }

    @Test
    public void testHashCode(){
        Assert.assertEquals(0, vector_0_0.hashCode());
        Assert.assertNotEquals(0, vector_0_2.hashCode());
        Assert.assertNotEquals(0, vector_1_0.hashCode());
        Assert.assertNotEquals(0, vector_1_1.hashCode());

    }
}

