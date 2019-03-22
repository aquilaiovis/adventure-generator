package ch.kbw.utils;

import ch.kbw.render.ShapeRenderer;
import ch.kbw.update.NoiseGenerator;

import java.util.ArrayList;

public class World
{
    // Singleton
    private static World instance;

    private ArrayList<Point> allPoints;
    private int chunkWidth, chunkHeight;

    private World()
    {
        NoiseGenerator noiseGenerator = new NoiseGenerator();

        chunkWidth = 1;
        chunkHeight = 1;

        ArrayList<Double> heights = noiseGenerator.getHeights(chunkWidth, chunkHeight);
        allPoints = new ArrayList<>();

        for(int x=0; x<chunkWidth*100; x++)
        {
            for(int y=0; y<chunkHeight*100; y++)
            {
                allPoints.add(new Point(((float)x)/10, ((float)y)/10, Float.parseFloat(Double.toString(heights.get(x*chunkWidth*100 + y)))/10));
            }
        }
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
        ShapeRenderer.getInstance().drawTriangles(allPoints, chunkWidth*100);
    }
}