package ch.kbw.render;

import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import ch.kbw.input.KeyInput;
import ch.kbw.input.MouseInput;

public class WindowRenderer
{
    // Singleton
    private static WindowRenderer instance;

    // Attributes
    private GLWindow window;
    private GLProfile profile;

    // TODO: Read target-fps, fullscreen and size preferences from user settings
    private int windowWidthInPixels, windowHeightInPixels, targetFps;
    private boolean fullscreen;
    private float windowWidth;

    private WindowRenderer()
    {
        windowWidthInPixels = 1080;
        windowHeightInPixels = 720;
        fullscreen = false;
        targetFps = 60;

        // Increase flexibility by measuring in units instead of pixels
        windowWidth = 100;
    }

    public static WindowRenderer getInstance()
    {
        if (instance == null)
        {
            instance = new WindowRenderer();
        }
        return instance;
    }

    public void initialize()
    {
        GLProfile.initSingleton();
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        window = GLWindow.create(capabilities);

        window.setFullscreen(fullscreen);
        window.setSize(windowWidthInPixels, windowHeightInPixels);

        window.addGLEventListener(WorldRenderer.getInstance());
        window.addKeyListener(KeyInput.getInstance());
        window.addMouseListener(MouseInput.getInstance());

        FPSAnimator animator = new FPSAnimator(window, targetFps);
        animator.start();

        // Disable resizability to make users stick with preset sizes, which they may set in an upcoming settings menu
        window.setResizable(false);

        // Show screen only after all settings are properly initialized
        window.setVisible(true);
    }

    public void render()
    {
        if (window != null)
        {
            window.display();
        }
    }

    public GLProfile getProfile()
    {
        return profile;
    }

    public float getWindowWidth()
    {
        return windowWidth;
    }

    public float getWindowHeight()
    {
        return window.getHeight() / getPixelsPerUnit();
    }

    public int getTargetFps()
    {
        return targetFps;
    }

    public float getPixelsPerUnit()
    {
        return window.getWidth() / windowWidth;
    }
}