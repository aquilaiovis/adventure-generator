package ch.kbw.utils;

import ch.kbw.render.WindowRenderer;
import ch.kbw.update.UpdateLoop;

public class Launcher
{
    public static void main(String[] args)
    {
        WindowRenderer.getInstance().initialize();
        UpdateLoop.getInstance().start();
    }
}