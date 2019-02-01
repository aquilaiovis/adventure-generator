package ch.kbw.update;

import ch.kbw.utils.World;
import ch.kbw.render.WindowRenderer;

public class UpdateLoop
{
    private static UpdateLoop instance;
    private boolean running;
    private int targetUpdateInterval, renderedFrames;
    private final int MAX_UPDATES = 5;

    private UpdateLoop()
    {
        running = true;
        renderedFrames = 0;
    }

    public static UpdateLoop getInstance()
    {
        if (instance == null)
        {
            instance = new UpdateLoop();
        }
        return instance;
    }

    public void start()
    {
        targetUpdateInterval = 1000000000 / WindowRenderer.getInstance().getTargetFps();

        Thread gameLoopThread = new Thread(() ->
        {
            long lastUpdateTime = System.nanoTime();
            long lastFpsCheck = System.nanoTime();
            int updates = 0;

            while (running)
            {
                long currentTime = System.nanoTime();

                while (updates <= MAX_UPDATES && currentTime - lastUpdateTime >= targetUpdateInterval)
                {
                    World.getInstance().update();

                    // Not "lastUpdateTime = System.nanoTime()", so updates can catch up if they lack behind
                    lastUpdateTime += targetUpdateInterval;

                    updates++;
                }
                updates = 0;

                WindowRenderer.getInstance().render();
                renderedFrames++;
                if (System.nanoTime() >= lastFpsCheck + 1000000000)
                {
                    System.out.println("\nFps: " + renderedFrames);

                    renderedFrames = 0;
                    lastFpsCheck = System.nanoTime();
                }

                long timeTaken = System.nanoTime() - currentTime;
                if (targetUpdateInterval > timeTaken)
                {
                    try
                    {
                        Thread.sleep((targetUpdateInterval - timeTaken) / 1000000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        gameLoopThread.setName("UpdateLoop");
        gameLoopThread.start();
    }

    public float updateDifference()
    {
        return 1.0f / 1000000000 * targetUpdateInterval;
    }
}