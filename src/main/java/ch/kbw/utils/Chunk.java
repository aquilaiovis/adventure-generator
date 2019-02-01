package ch.kbw.utils;

import java.util.ArrayList;

public class Chunk
{
    private int x, y;
    private ArrayList<Point> points;

    public Chunk(int x, int y)
    {
        this.x = x;
        this.y = y;
        points = new ArrayList<>();
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public ArrayList<Point> getPoints()
    {
        return points;
    }

    public void setPoints(ArrayList<Point> points)
    {
        this.points = points;
    }
}