package ch.kbw.render;

import ch.kbw.utils.World;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;

public class WorldRenderer implements GLEventListener
{
    // Singleton
    private static WorldRenderer instance;
    private GLU glu = new GLU();
    private float rotateAdvance = 0;

    // Attributes
    private GL2 gl;

    private WorldRenderer()
    {

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

        gl.setSwapInterval(0);
        // For sprites
        // gl.glEnable(GL2.GL_TEXTURE_3D);
        // gl.glEnable(GL2.GL_BLEND);

        // For transparency of sprites
        // gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
    }

    public void dispose(GLAutoDrawable drawable)
    {
        System.exit(0);
    }

    public void display(GLAutoDrawable drawable)
    {
        updateGL2(drawable);

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearColor(0, 0, 0, 0);
        gl.glClearDepth(1);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        // Clear the screen and the depth buffer
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Reset the View
        gl.glLoadIdentity();

        gl.glRotatef(rotateAdvance, 0, 1, 0);

        World.getInstance().render();

        rotateAdvance += 0.5f;
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    {
        updateGL2(drawable);

        final GL2 gl = drawable.getGL().getGL2();
        if (height <= 0)
        {
            height = 1;
        }

        final float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        glu.gluPerspective(45.0f, h, 1.0, 20.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public GL2 getGL2()
    {
        return gl;
    }
}