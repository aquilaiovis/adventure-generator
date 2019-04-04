package ch.kbw.render;

import ch.kbw.utils.Point;

public class View
{
    private Point position;
    private Point perspective;

    public View()
    {
        // For testing
        position = new Point(-50, -50, -50);
        perspective = new Point(-80, 0, -180);
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