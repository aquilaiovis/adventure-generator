package ch.kbw.render;

import ch.kbw.input.KeyInput;
import ch.kbw.input.MouseInput;
import ch.kbw.utils.Point;
import ch.kbw.utils.World;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.util.ArrayList;

public class RenderLoop implements GLEventListener
{
    private World world;
    private GLWindow window;
    private GLProfile profile;
    private boolean resizable;
    // Todo: Read target-fps, fullscreen and size preferences from user settings
    private int targetFps, windowWidthInPixels, windowHeightInPixels;
    private boolean fullscreen;
    private float windowWidthInUnits;

    private KeyInput keyInput;
    private MouseInput mouseInput;

    private GLU glu;
    private int textureNumber;
    private GL2 gl;

    private View view;

    public RenderLoop(int targetFps, int windowWidthInPixels, int windowHeightInPixels, boolean fullscreen, boolean resizable, float windowWidthInUnits)
    {
        this.targetFps = targetFps;
        this.windowWidthInPixels = windowWidthInPixels;
        this.windowHeightInPixels = windowHeightInPixels;
        this.fullscreen = fullscreen;
        this.resizable = resizable;

        // Todo: Combine with triangle rendering
        // Increase flexibility by measuring in units instead of pixels
        this.windowWidthInUnits = windowWidthInUnits;

        this.view = new View();

        initWindowRenderer();
    }

    private void initWindowRenderer()
    {
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        window = GLWindow.create(capabilities);

        window.setFullscreen(fullscreen);
        // Todo: Make this work
        window.setSize(windowWidthInPixels, windowHeightInPixels);

        window.addGLEventListener(this);
        keyInput = new KeyInput();
        window.addKeyListener(keyInput);
        mouseInput = new MouseInput(getPixelsPerUnit(), getWindowWidthInUnits(), getWindowHeight());
        window.addMouseListener(mouseInput);

        FPSAnimator animator = new FPSAnimator(window, targetFps);
        animator.start();

        // Disable resizability on default to make users stick with preset sizes, which they may set in an upcoming settings menu
        resizable = false;
        window.setResizable(resizable);

        // Show screen only after all settings are properly initialized
        window.setVisible(true);

        window.setTitle("Adventure Generator");
    }

    private void initWorldRenderer()
    {
        glu = new GLU();

        gl.setSwapInterval(0);

        // For transparency of sprites
        // gl.glEnable(GL2.GL_BLEND);
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
            BufferedImage image = ImageIO.read(new FileInputStream("res/minegrass.jpg"));
            Texture texture = AWTTextureIO.newTexture(this.getProfile(), image, true);
            textureNumber = texture.getTextureObject(gl);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

    @Override
    public void init(GLAutoDrawable drawable)
    {
        updateGL2(drawable);

        initWorldRenderer();
    }

    @Override
    public void dispose(GLAutoDrawable drawable)
    {
        System.exit(0);
    }

    @Override
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

        renderWorld();
    }

    @Override
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

        // Draw/Render distance
        glu.gluPerspective(60.0f, h, 1.0f, 120.0f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    public void renderWindow()
    {
        if (window != null)
        {
            window.display();
        }
    }

    private void renderWorld()
    {
        ArrayList<Point> points = world.getAllPoints();
        int pointFieldWidth = world.getChunkPointsPerSide() * world.getChunksX();
        int pointFieldHeight = world.getChunkPointsPerSide() * world.getChunksY();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, this.getTextureNumber());
        gl.glBegin(GL2.GL_TRIANGLES);

        int heightMultiplier = 10;
        for (int i = 0; i < points.size() - pointFieldWidth - 1; i++)
        {
            float colourModifier = 0.30f + points.get(i).getZ();
            if (colourModifier < 0)
            {
                colourModifier = 0;
            }
            if (getLength(points.get(i).getX() - points.get(i + 1).getX(), points.get(i).getY() - points.get(i + 1).getY()) < 2
                    && getLength(points.get(i + 1).getX() - points.get(i + pointFieldWidth).getX(), points.get(i + 1).getY() - points.get(i + pointFieldWidth).getY()) < 2
                    && getLength(points.get(i + pointFieldWidth).getX() - points.get(i).getX(), points.get(i + pointFieldWidth).getY() - points.get(i).getY()) < 2)
            {
                setColor(0.30f + colourModifier, 0.80f + colourModifier, 0.30f + colourModifier, 1);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(points.get(i).getX(), points.get(i).getY(), points.get(i).getZ() * heightMultiplier);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(points.get(i + 1).getX(), points.get(i + 1).getY(), points.get(i + 1).getZ() * heightMultiplier);
                gl.glTexCoord2f(0.5f, 1.0f);
                gl.glVertex3f(points.get(i + pointFieldWidth).getX(), points.get(i + pointFieldWidth).getY(), points.get(i + pointFieldWidth).getZ() * heightMultiplier);
            }

            if (getLength(points.get(i + 1).getX() - points.get(i + pointFieldWidth).getX(), points.get(i + 1).getY() - points.get(i + pointFieldWidth).getY()) < 2
                    && getLength(points.get(i + pointFieldWidth).getX() - points.get(i + pointFieldWidth + 1).getX(), points.get(i + pointFieldWidth).getY() - points.get(i + pointFieldWidth + 1).getY()) < 2
                    && getLength(points.get(i + pointFieldWidth + 1).getX() - points.get(i + 1).getX(), points.get(i + pointFieldWidth + 1).getY() - points.get(i + 1).getY()) < 2)
            {
                setColor(0.25f + colourModifier, 0.70f + colourModifier, 0.25f + colourModifier, 1);
                gl.glTexCoord2f(0.0f, 0.0f);
                gl.glVertex3f(points.get(i + 1).getX(), points.get(i + 1).getY(), points.get(i + 1).getZ() * heightMultiplier);
                gl.glTexCoord2f(1.0f, 0.0f);
                gl.glVertex3f(points.get(i + pointFieldWidth).getX(), points.get(i + pointFieldWidth).getY(), points.get(i + pointFieldWidth).getZ() * heightMultiplier);
                gl.glTexCoord2f(0.5f, 1.0f);
                gl.glVertex3f(points.get(i + pointFieldWidth + 1).getX(), points.get(i + pointFieldWidth + 1).getY(), points.get(i + pointFieldWidth + 1).getZ() * heightMultiplier);
            }

        }

        // Finish drawing
        gl.glEnd();

        gl.glFlush();


        /*Sprite sprite = new Sprite(this, "res/minewater.jpg");
        Texture texture = sprite.getTexture();

        if (texture != null)
        {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        }

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);*/

        // Todo: Make water transparent
        setColor(0, 0.5f, 1, 1);
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(0, 0);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(100, 0);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(100, 100);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(0, 100);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
    }

    public void drawSprite(Sprite sprite, Point center, float rotation, float width, float height)
    {
        Texture texture = sprite.getTexture();

        if (texture != null)
        {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, texture.getTextureObject());
        }

        // Make the center the reference point for further transformations
        gl.glTranslatef(center.getX(), center.getY(), 0);

        // Make rotation negative so it is as one is used from mathematics
        gl.glRotatef(-rotation, 0, 0, 1);

        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0, 0);
        gl.glVertex2f(-width / 2, -height / 2);
        gl.glTexCoord2f(1, 0);
        gl.glVertex2f(width / 2, -height / 2);
        gl.glTexCoord2f(1, 1);
        gl.glVertex2f(width / 2, height / 2);
        gl.glTexCoord2f(0, 1);
        gl.glVertex2f(-width / 2, height / 2);
        gl.glEnd();
        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);

        // Reset rotation as it will rotate again whenever this method is called
        gl.glRotatef(rotation, 0, 0, 1);

        // Reset the reference point as it will set it where it was again whenever this method is called
        gl.glTranslatef(-center.getX(), -center.getY(), 0);
    }

    private float getLength(double xDifference, double yDifference)
    {
        if (xDifference < 0)
        {
            xDifference *= -1;
        }
        if (yDifference < 0)
        {
            yDifference *= -1;
        }
        return (float) Math.sqrt(xDifference * xDifference + yDifference * yDifference);
    }

    private void moveView()
    {
        Point perspective = view.getPerspective();
        gl.glRotatef(perspective.getX(), 1, 0, 0);
        gl.glRotatef(perspective.getY(), 0, 1, 0);
        gl.glRotatef(perspective.getZ(), 0, 0, 1);
        float rotationSpeed = 0.5f;
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_NUMPAD2))
        {
            perspective.addToX(rotationSpeed);
        }
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_NUMPAD8))
        {
            perspective.addToX(-rotationSpeed);
        }
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_NUMPAD7))
        {
            perspective.addToY(rotationSpeed);
        }
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_NUMPAD9))
        {
            perspective.addToY(-rotationSpeed);
        }
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_NUMPAD4))
        {
            perspective.addToZ(-rotationSpeed);
        }
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_NUMPAD6))
        {
            perspective.addToZ(rotationSpeed);
        }

        Point position = view.getPosition();

        float movementSpeed = 0.1f;
        float floatingHeight = 1f;
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_D))
        {
            position.addToX(movementSpeed);
        }
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_A))
        {
            position.addToX(-movementSpeed);
        }
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_W))
        {
            position.addToY(movementSpeed);
        }
        if (keyInput.isPressed(com.jogamp.newt.event.KeyEvent.VK_S))
        {
            position.addToY(-movementSpeed);
        }

        for (Point point : world.getAllPoints())
        {
            if (point.getX() == ((int) position.getX() * -1) && point.getY() == ((int) position.getY() * -1))
            {
                position.setZ(-((point.getZ() + floatingHeight) * 10));
            }
        }

        gl.glTranslatef(position.getX(), position.getY(), position.getZ());
    }

    private void setColor(float red, float green, float blue, float alpha)
    {
        gl.glColor4f(validateColor(red), validateColor(green), validateColor(blue), validateColor(alpha));
    }

    /**
     * Makes sure the value is not outside 0 to 1
     */
    private float validateColor(float color)
    {
        if (color < 0)
        {
            return 0;
        }
        else if (color > 1)
        {
            return 1;
        }
        else
        {
            return color;
        }
    }

    private float getWindowWidthInUnits()
    {
        return windowWidthInUnits;
    }

    private float getWindowHeight()
    {
        return window.getHeight() / getPixelsPerUnit();
    }

    private float getPixelsPerUnit()
    {
        return window.getWidth() / windowWidthInUnits;
    }

    private int getTextureNumber()
    {
        return textureNumber;
    }

    public GLProfile getProfile()
    {
        return profile;
    }

    public KeyInput getKeyInput()
    {
        return keyInput;
    }

    public MouseInput getMouseInput()
    {
        return mouseInput;
    }

    public int getTargetFps()
    {
        return targetFps;
    }

    public void setWorld(World world)
    {
        this.world = world;
    }
}