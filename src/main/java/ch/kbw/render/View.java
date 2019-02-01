package ch.kbw.render;

import ch.kbw.utils.Point;

public class View
{
    // Singleton
    private static View instance;

    // Attributes
    private Point position;
    private Point perspective;

    private View()
    {
        position = new Point(0, 0, 0);
        perspective = new Point(1, 0, 0);
    }

    public static View getInstance()
    {
        if (instance == null)
        {
            instance = new View();
        }
        return instance;
    }

    public Point getPosition()
    {
        return position;
    }

    public void setPosition(Point position)
    {
        this.position = position;
    }

    public Point getPerspective()
    {
        return perspective;
    }

    public void setPerspective(Point perspective)
    {
        this.perspective = perspective;
    }
}