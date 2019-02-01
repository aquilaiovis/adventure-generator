package ch.kbw.utils;

public class World
{
    // Singleton
    private static World instance;

    private World()
    {

    }

    public static World getInstance()
    {
        if (instance == null)
        {
            instance = new World();
        }
        return instance;
    }

    public void update()
    {

    }

    public void render()
    {

    }
}