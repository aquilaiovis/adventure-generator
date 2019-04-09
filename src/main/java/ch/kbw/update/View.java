package ch.kbw.update;

import ch.kbw.utils.Point;

public class View
{
    private Point position;
    private Point perspective;

    public View(Point position, Point perspective)
    {
        this.position = position;
        this.perspective = perspective;
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