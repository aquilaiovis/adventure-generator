package ch.kbw.utils;

import ch.kbw.input.KeyInput;
import ch.kbw.input.MouseInput;
import ch.kbw.render.RenderLoop;
import ch.kbw.update.NoiseGenerator;
import ch.kbw.update.UpdateLoop;
import com.jogamp.newt.event.KeyEvent;

import java.util.ArrayList;

public class World
{
    private UpdateLoop updateLoop;
    private RenderLoop renderLoop;
    private KeyInput keyInput;
    private MouseInput mouseInput;
    private ArrayList<Point> allPoints;
    private int chunksX, chunksY;
    private int chunkPointsPerSide;

    public World(UpdateLoop updateLoop)
    {
        this.updateLoop = updateLoop;
        renderLoop = updateLoop.getRenderLoop();
        keyInput = renderLoop.getKeyInput();
        mouseInput = renderLoop.getMouseInput();
        NoiseGenerator noiseGenerator = new NoiseGenerator();

        chunksX = 1;
        chunksY = 1;
        chunkPointsPerSide = 100;

        // Todo: Fix arguments
        ArrayList<Double> heights = noiseGenerator.getHeights(chunksX, chunksY);
        allPoints = new ArrayList<>();

        for (int x = 0; x < chunksX * chunkPointsPerSide; x++)
        {
            for (int y = 0; y < chunksY * chunkPointsPerSide; y++)
            {
                allPoints.add(new Point((float) x, (float) y, Float.parseFloat(Double.toString(heights.get(x * chunksX * chunkPointsPerSide + y))) * 5));
            }
        }
    }

    public void update()
    {
        if (keyInput.isPressed(KeyEvent.VK_ESCAPE))
        {
            updateLoop.setPaused(!updateLoop.isPaused());
        }
    }

    public ArrayList<Point> getAllPoints()
    {
        return allPoints;
    }

    public int getChunksX()
    {
        return chunksX;
    }

    public int getChunksY()
    {
        return chunksY;
    }

    public int getChunkPointsPerSide()
    {
        return chunkPointsPerSide;
    }
}