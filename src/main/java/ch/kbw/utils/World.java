package ch.kbw.utils;

import ch.kbw.render.ShapeRenderer;
import ch.kbw.render.SpriteRenderer;

import java.util.ArrayList;

public class World
{
    // Singleton
    private static World instance;

    private ArrayList<Point> points;

    private World()
    {
        points = new ArrayList<>();

        points.add(new Point(0.5f, 2.0f, -6.0f));
        points.add(new Point(-1.5f, -1.0f, -5.0f));
        points.add(new Point(0.5f, -1.0f, -5.0f));
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
        ShapeRenderer.getInstance().drawTriangle(points.get(0), points.get(1), points.get(2));
    }
}