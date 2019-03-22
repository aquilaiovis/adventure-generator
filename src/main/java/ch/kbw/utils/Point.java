package ch.kbw.utils;

public class Point
{
    private float x, y, z;

    public Point(float xPosition, float yPosition, float zPosition)
    {
        x = xPosition;
        y = yPosition;
        z = zPosition;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float newX)
    {
        x = newX;
    }

    public void addToX(float additionalX)
    {
        x += additionalX;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float newY)
    {
        y = newY;
    }

    public void addToY(float additionalY)
    {
        y += additionalY;
    }

    public float getZ()
    {
        return z;
    }

    public void setZ(float z)
    {
        this.z = z;
    }

    public void addToZ(float additionalZ)
    {
        z += additionalZ;
    }
}