package ch.kbw.render;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import ch.kbw.utils.World;

public class WorldRenderer implements GLEventListener
{
    // Singleton
    private static WorldRenderer instance;

    // Attributes
    private GL2 gl;
    private float noTurningToleranceDegrees;

    private WorldRenderer()
    {
        noTurningToleranceDegrees = 45;
    }

    public static WorldRenderer getInstance()
    {
        if (instance == null)
        {
            instance = new WorldRenderer();
        }
        return instance;
    }

    private void updateGL2(GLAutoDrawable drawable)
    {
        try
        {
            gl = drawable.getGL().getGL2();
        }
        catch (GLException e)
        {
            e.printStackTrace();
        }
    }

    public void init(GLAutoDrawable drawable)
    {
        updateGL2(drawable);

        gl.glClearColor(1, 1, 1, 1);

        gl.setSwapInterval(0);
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void dispose(GLAutoDrawable drawable)
    {
        System.exit(0);
    }

    public void display(GLAutoDrawable drawable)
    {
        updateGL2(drawable);

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);

        // gl.glRotatef(View.getInstance().getRotation(), 0, 0, 1);
        gl.glTranslatef(-View.getInstance().getPosition().getX(), -View.getInstance().getPosition().getY(), 0);
        World.getInstance().render();
        gl.glTranslatef(View.getInstance().getPosition().getX(), View.getInstance().getPosition().getY(), 0);
        // gl.glRotatef(-View.getInstance().getRotation(), 0, 0, 1);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    {
        updateGL2(drawable);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        gl.glOrtho(-WindowRenderer.getInstance().getWindowWidth() / 2, WindowRenderer.getInstance().getWindowWidth() / 2, 0, -WindowRenderer.getInstance().getWindowHeight(), -1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public GL2 getGL2()
    {
        return gl;
    }
}