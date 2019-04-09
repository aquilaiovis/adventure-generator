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
        handleWater();
    }

    private void handleStamina(){

    }

    private void handleWater(){
        setWater(water-0.0005f);
    }

    private void handleOxygen()
    {
        // z is reversed
        if (position.getZ() < 0.0)
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
            setWater(water+2.5f);
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
                setHealth(health-0.1f);
            }
            if(water<=30){
                setHealth(health-0.3f);
            }
        }
        if(health < 100){
            if(food >= 80){
                health += 0.3;
                setFood(food-0.025f);
            }else if(food >= 50){
                health += 0.05;
                setFood(food-0.045f);
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
        if (stamina > 100){
            this.stamina = 100;
        }else if (stamina < 0){
            this.stamina = 0;
        }else{
            this.stamina = stamina;
        }
    }

    public float getFood() {
        return food;
    }

    public void setFood(float food) {
        if (food > 100){
            this.food = 100;
        }else if (food < 0){
            this.food = 0;
        }else{
            this.food = food;
        }
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        if (water > 100){
            this.water = 100;
        }else if (water < 0){
            this.water = 0;
        }else{
            this.water = water;
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