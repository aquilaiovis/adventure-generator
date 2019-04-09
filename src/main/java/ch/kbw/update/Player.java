package ch.kbw.update;

import ch.kbw.utils.Point;

public class Player
{
    private Point position;
    private Point perspective;
    private float oxygen, health, food, water, stamina;

    public Player(Point position, Point perspective)
    {
        this.position = position;
        this.perspective = perspective;

        oxygen = 100;
        health = 100;
        food = 100;
        water = 100;
        stamina = 100;
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
                health -= 0.75;
            }
            else if (oxygen < 50.0)
            {
                health -= 0.5;
            }
            if(food<=20){
                health -= 0.1;
            }
            if(water<=30){
                health -= 0.3;
            }
        }
        if(health < 100){
            if(food >= 80){
                health += 0.3;
            }else if(food >= 50){
                health += 0.05;
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

    public float getStamina() {
        return stamina;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public float getFood() {
        return food;
    }

    public void setFood(float food) {
        this.food = food;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
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