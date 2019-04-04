package ch.kbw.update;

import ch.kbw.render.RenderLoop;
import ch.kbw.utils.World;

public class UpdateLoop
{
    private RenderLoop renderLoop;
    private boolean running, paused;
    private int targetUpdateInterval, renderedFrames;
    private final int MAX_UPDATES = 5;
    private World world;

    public UpdateLoop(RenderLoop renderLoop)
    {
        this.renderLoop = renderLoop;
        running = true;
        paused = false;
        renderedFrames = 0;
        world = new World(this);
    }

    public void startUpdateLoop()
    {
        targetUpdateInterval = 1000000000 / renderLoop.getTargetFps();

        Thread gameLoopThread = new Thread(() ->
        {
            long lastUpdateTime = System.nanoTime();
            long lastFpsCheck = System.nanoTime();
            int updates = 0;

            while (running)
            {
                if (!paused)
                {
                    long currentTime = System.nanoTime();

                    while (updates <= MAX_UPDATES && currentTime - lastUpdateTime >= targetUpdateInterval)
                    {
                        world.update();

                        // Not "lastUpdateTime = System.nanoTime()", so updates can catch up if they lack behind
                        lastUpdateTime += targetUpdateInterval;

                        updates++;
                    }
                    updates = 0;

                    renderLoop.renderWindow();
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
            }
        });
        gameLoopThread.setName("UpdateLoop");
        gameLoopThread.start();
    }

    public float updateDifference()
    {
        return 1.0f / 1000000000 * targetUpdateInterval;
    }

    public boolean isPaused()
    {
        return paused;
    }

    public void setPaused(boolean paused)
    {
        this.paused = paused;
    }

    public RenderLoop getRenderLoop()
    {
        return renderLoop;
    }

    public World getWorld()
    {
        return world;
    }
}