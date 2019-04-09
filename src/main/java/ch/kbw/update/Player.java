package ch.kbw.update;

import ch.kbw.utils.Point;

public class Player
{
    private Point position;
    private Point perspective;
    private float oxygen, health;

    public Player(Point position, Point perspective)
    {
        this.position = position;
        this.perspective = perspective;

        oxygen = 100;
        health = 100;
    }

    public void update()
    {
        handleOxygen();
        handleHealth();
    }

    private void handleOxygen()
    {
        // z is reversed
        if (position.getZ() < 0)
        {
            if (oxygen < 100)
            {
                oxygen += 1f;
            }
        }
        else
        {
            if (oxygen > 0.0)
            {
                oxygen -= 0.1f;
            }
        }
    }

    private void handleHealth()
    {
        if (health > 0)
        {
            if (oxygen < 20.0)
            {
                health -= 1;
            }
            else if (oxygen < 50.0)
            {
                health -= 0.5;
            }
        }
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

    public float getHealth()
    {
        return health;
    }

    public void setHealth(float health)
    {
        if (health > 100)
        {
            this.oxygen = 100;
        }
        else if (oxygen < 0)
        {
            this.oxygen = 0;
        }
        else
        {
            this.health = health;
        }

    }

    public float getOxygen()
    {
        return oxygen;
    }

    public void setOxygen(float oxygen)
    {
        if (oxygen > 100)
        {
            this.oxygen = 100;
        }
        else if (oxygen < 0)
        {
            this.oxygen = 0;
        }
        else
        {
            this.oxygen = oxygen;
        }
    }
}