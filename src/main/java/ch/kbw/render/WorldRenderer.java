package ch.kbw.render;

import ch.kbw.input.KeyInput;
import ch.kbw.utils.Point;
import ch.kbw.utils.World;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;

public class WorldRenderer implements GLEventListener
{
    // Singleton
    private static WorldRenderer instance;
    private GLU glu = new GLU();
    private int textureNumber;

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

        gl.glEnable(GL2.GL_TEXTURE_2D);
        try
        {
            BufferedImage image = ImageIO.read(new FileInputStream("res/grass.png"));
            Texture texture = AWTTextureIO.newTexture(WindowRenderer.getInstance().getProfile(), image, true);
            textureNumber = texture.getTextureObject(gl);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        gl.glTranslatef(0, 100, 0);

        // Clear the screen and the depth buffer
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Reset the View
        gl.glLoadIdentity();

        moveView();

        World.getInstance().render();
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

    public int getTextureNumber()
    {
        return textureNumber;
    }

    private void moveView()
    {
        Point perspective = View.getInstance().getPerspective();
        gl.glRotatef(perspective.getX(), 1, 0, 0);
        gl.glRotatef(perspective.getY(), 0, 1, 0);
        gl.glRotatef(perspective.getZ(), 0, 0, 1);
        float rotationSpeed = 0.5f;
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_S))
        {
            perspective.addToX(rotationSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_W))
        {
            perspective.addToX(-rotationSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_Q))
        {
            perspective.addToY(rotationSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_E))
        {
            perspective.addToY(-rotationSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_A))
        {
            perspective.addToZ(-rotationSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_D))
        {
            perspective.addToZ(rotationSpeed);
        }

        Point position = View.getInstance().getPosition();
        gl.glTranslatef(position.getX(), position.getY(), position.getZ());
        float movementSpeed = 0.01f;
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_NUMPAD2))
        {
            position.addToX(movementSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_NUMPAD5))
        {
            position.addToX(-movementSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_NUMPAD6))
        {
            position.addToY(movementSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_NUMPAD4))
        {
            position.addToY(-movementSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_NUMPAD8))
        {
            position.addToZ(-movementSpeed);
        }
        if(KeyInput.getInstance().isPressed(KeyEvent.VK_NUMPAD0))
        {
            position.addToZ(movementSpeed);
        }
    }
}