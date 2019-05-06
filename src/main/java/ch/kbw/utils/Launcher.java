package ch.kbw.utils;

import ch.kbw.render.RenderLoop;

public class Launcher
{
    public static void main(String[] args)
    {
        RenderLoop renderLoop = new RenderLoop(60, 1080, 720, false, false, 100, 1);
    }
}