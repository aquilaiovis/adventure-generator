package ch.kbw.update;

import ch.kbw.utils.Point;

public class Player
{
    private Point position;
    private Point perspective;
    private int id;
    private float health, oxygen, saturation, hydration, stamina;

    public Player(Point position, Point perspective)
    {
        this.position = position;
        this.perspective = perspective;

        health = 100f;
        oxygen = 100f;
        saturation = 100f;
        hydration = 100f;
        stamina = 100f;
    }

    public void update()
    {
        handleHealth();
        handleOxygen();
        handleHydration();
        handleStamina();
    }

    private void handleStamina()
    {

    }

    private void handleHydration()
    {
        setHydration(hydration - 0.0005f);
    }

    private void handleOxygen()
    {
        // z is reversed
        if (position.getZ() < 0.0)
        {
            if (oxygen < 100) oxygen += 1f;
        }
        else
        {
            if (oxygen > 0.0) oxygen -= 0.1f;
            hydration += 2.5f;
        }
    }

    private void handleHealth()
    {
        if (health > 0f)
        {
            if (oxygen < 20f) health -= 0.75f;
            else if (oxygen < 50f) health -= 0.5f;

            if (saturation <= 20f) health -= 0.1f;
            if (hydration <= 30f) health -= 0.3f;
        }
        if (health < 100f)
        {
            if (saturation >= 80f)
            {
                health += 0.3f;
                saturation -= 0.025f;
            }
            else if (saturation >= 50f)
            {
                health += 0.05f;
                saturation -= 0.045f;
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
        this.health = getValidatedPercentage(health);
    }

    public float getStamina()
    {
        return stamina;
    }

    public void setStamina(float stamina)
    {
        this.stamina = getValidatedPercentage(stamina);
    }

    public float getSaturation()
    {
        return saturation;
    }

    public void setSaturation(float saturation)
    {
        this.saturation = getValidatedPercentage(saturation);
    }

    public float getHydration()
    {
        return hydration;
    }

    public void setHydration(float hydration)
    {
        this.hydration = getValidatedPercentage(hydration);
    }

    public float getOxygen()
    {
        return oxygen;
    }

    public void setOxygen(float oxygen)
    {
        this.oxygen = getValidatedPercentage(oxygen);
    }

    private float getValidatedPercentage(float value)
    {
        if (value > 100) return 100;
        else if (value < 0) return 0;
        else return value;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}