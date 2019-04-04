package ch.kbw.utils;

import ch.kbw.render.RenderLoop;
import ch.kbw.update.UpdateLoop;

public class Launcher
{
    public static void main(String[] args)
    {
        RenderLoop renderLoop = new RenderLoop(60, 1080, 720, false, false, 100);
        UpdateLoop updateLoop = new UpdateLoop(renderLoop);
        renderLoop.setWorld(updateLoop.getWorld());

        updateLoop.startUpdateLoop();
    }
}