package ch.kbw.render;

import ch.kbw.input.KeyInput;
import ch.kbw.input.MouseInput;
import ch.kbw.multiplayer.Client;
import ch.kbw.multiplayer.Server;
import ch.kbw.update.Player;
import ch.kbw.update.UpdateLoop;
import ch.kbw.update.View;
import ch.kbw.utils.Point;
import ch.kbw.utils.World;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

import java.awt.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class RenderLoop implements GLEventListener
{
    private GLProfile profile;
    private GL2 gl;
    private GLU glu;

    // Todo: Read target-fps, fullscreen and size preferences from user settings
    private GLWindow window;
    private boolean resizable;
    private int targetFps, windowWidthInPixels, windowHeightInPixels;
    private boolean fullscreen;
    private float windowWidthInUnits, windowHeightInUnits, fontSizeInUnits;
    private TextRenderer mainMenuTitleRenderer, buttonLabelRenderer;
    private String mainMenuTitle;
    private Button[] buttons;
    private boolean isPlaying;

    private World world;
    private View view;

    private KeyInput keyInput;
    private MouseInput mouseInput;

    public RenderLoop(int targetFps, int windowWidthInPixels, int windowHeightInPixels, boolean fullscreen, boolean resizable, float windowWidthInUnits, float fontSizeInUnits)
    {
        isPlaying = false;

        this.targetFps = targetFps;
        this.windowWidthInPixels = windowWidthInPixels;
        this.windowHeightInPixels = windowHeightInPixels;
        this.fullscreen = fullscreen;
        this.resizable = resizable;

        // Increase flexibility by measuring in units instead of pixels
        this.windowWidthInUnits = windowWidthInUnits;
        this.fontSizeInUnits = fontSizeInUnits;

        initMainMenu();
        initWindow();
    }

    private void initMainMenu()
    {
        mainMenuTitle = "Main Menu";
        mainMenuTitleRenderer = new TextRenderer(new Font("Times New Roman", Font.BOLD, (int) (2 * fontSizeInUnits * getPixelsPerUnit())));
        buttonLabelRenderer = new TextRenderer(new Font("Verdana", Font.BOLD, (int) (fontSizeInUnits * getPixelsPerUnit())));

        // In units, not pixels
        float buttonWidth = 15;
        float buttonLeftX = windowWidthInUnits / 2 - buttonWidth / 2;

        float buttonHeight = 5;
        float buttonSpaceBetween = 3;
        float buttonTopY = buttonHeight + buttonSpaceBetween;
        float buttonSelectionTopMargin = 10;
        buttons = new Button[]
                {
                        new Button("Singleplayer", buttonLeftX, 1 * buttonTopY + buttonSelectionTopMargin, buttonWidth, buttonHeight),
                        new Button("Host Server", buttonLeftX, 2 * buttonTopY + buttonSelectionTopMargin, buttonWidth, buttonHeight),
                        new Button("Join Server", buttonLeftX, 3 * buttonTopY + buttonSelectionTopMargin, buttonWidth, buttonHeight),
                        new Button("Settings", buttonLeftX, 4 * buttonTopY + buttonSelectionTopMargin, buttonWidth, buttonHeight),
                        new Button("Exit", buttonLeftX, 5 * buttonTopY + buttonSelectionTopMargin, buttonWidth, buttonHeight)
                };
    }

    private void initWindow()
    {
        profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        window = GLWindow.create(capabilities);

        window.setFullscreen(fullscreen);
        window.setSize(windowWidthInPixels, windowHeightInPixels);

        window.addGLEventListener(this);
        keyInput = new KeyInput();
        window.addKeyListener(keyInput);
        mouseInput = new MouseInput(getPixelsPerUnit(), windowWidthInUnits, windowHeightInUnits);
        window.addMouseListener(mouseInput);

        FPSAnimator animator = new FPSAnimator(window, targetFps);
        animator.start();

        // Disable resizability on default to make users stick with preset sizes, which they may set in an upcoming settings menu
        resizable = false;
        window.setResizable(resizable);

        // Show screen only after all settings are properly initialized
        window.setVisible(true);

        window.setTitle("Adventure Generator");

        renderWindow();
    }

    private void initRenderer()
    {
        glu = new GLU();

        gl.setSwapInterval(0);

        // Enable transparency of sprites
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glShadeModel(GL2.GL_SMOOTH);

        gl.glClearColor(0.4f, 0.6f, 1.0f, 1.0f);
        gl.glClearDepth(1.0f);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);

        // Enable textures
        gl.glEnable(GL2.GL_TEXTURE_2D);
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

        initRenderer();
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

        // Clear the screen and the depth buffer
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        // Reset the View
        gl.glLoadIdentity();

        if (isPlaying)
        {
            if (world != null)
            {
                view = world.getView();
                renderWorld();
                renderUserInterface();
            }
        }
        else
        {
            renderMainMenu();
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    {
        updateGL2(drawable);

        if (isPlaying)
        {
            final GL2 gl = drawable.getGL().getGL2();
            if (height <= 0)
            {
                height = 1;
            }

            final float screenRatio = (float) width / (float) height;
            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            // Draw/Render distance
            glu.gluPerspective(60.0f, screenRatio, 1.0f, 120.0f);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();
        }
        else
        {
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            windowWidthInPixels = window.getWidth();
            windowHeightInPixels = window.getHeight();
            windowHeightInUnits = (float) windowHeightInPixels / getPixelsPerUnit();
            gl.glOrtho(0, windowWidthInUnits, windowHeightInUnits, 0, -1, 1);
            gl.glMatrixMode(GL2.GL_MODELVIEW);
        }
    }

    public void renderWindow()
    {
        if (window != null)
        {
            window.display();
        }
    }

    private void renderMainMenu()
    {
        mainMenuTitleRenderer.beginRendering(windowWidthInPixels, windowHeightInPixels);
        mainMenuTitleRenderer.setColor(1f, 1f, 1f, 1f);
        mainMenuTitleRenderer.draw(mainMenuTitle, (int) ((windowWidthInUnits / 2 - mainMenuTitle.length() * fontSizeInUnits / 1.8f) * getPixelsPerUnit()),
                (int) ((windowHeightInUnits - 10) * getPixelsPerUnit()));
        mainMenuTitleRenderer.endRendering();

        for (Button button : buttons)
        {
            String buttonLabel = button.getLabel();
            float buttonLeftX = button.getLeftX();
            float buttonTopY = button.getTopY();
            float buttonWidth = button.getWidth();
            float buttonHeight = button.getHeight();

            setColor(0.2f, 0.3f, 0.5f, 1.0f);
            gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(buttonLeftX, buttonTopY);
            gl.glVertex2f(buttonLeftX + buttonWidth, buttonTopY);
            gl.glVertex2f(buttonLeftX + buttonWidth, buttonTopY + buttonHeight);
            gl.glVertex2f(buttonLeftX, buttonTopY + buttonHeight);
            gl.glEnd();
            gl.glFlush();

            buttonLabelRenderer.beginRendering(windowWidthInPixels, windowHeightInPixels);
            buttonLabelRenderer.setColor(1f, 1f, 1f, 1f);
            buttonLabelRenderer.setSmoothing(true);
            // Put y to top with windowHeightInPixels in the beginning, then minus instead of plus
            buttonLabelRenderer.draw(buttonLabel, (int) ((buttonLeftX + buttonWidth / 2 - buttonLabel.length() * fontSizeInUnits / 4) * getPixelsPerUnit()),
                    (int) ((windowHeightInUnits - buttonTopY - buttonHeight / 2 - fontSizeInUnits / 2.4f) * getPixelsPerUnit()));
            buttonLabelRenderer.endRendering();
            if (mouseInput.isPressed(MouseEvent.BUTTON1)
                    && button.isHovering(mouseInput.getMouseX() / getPixelsPerUnit(), mouseInput.getMouseY() / getPixelsPerUnit()))
            {
                switch (button.getLabel())
                {
                    case "Singleplayer":
                    {
                        gl.glClearColor(0f, 0f, 0f, 1.0f);
                        UpdateLoop updateLoop = new UpdateLoop(this);
                        world = new World(updateLoop, false, false);
                        world.generate();

                        updateLoop.startUpdateLoop(world);
                        isPlaying = true;
                        break;
                    }
                    case "Host Server":
                    {
                        gl.glClearColor(0f, 0f, 0f, 1.0f);
                        UpdateLoop updateLoop = new UpdateLoop(this);
                        world = new World(updateLoop, true, true);
                        world.generate();

                        Server server = new Server(6069);
                        new Thread(server).start();
                        try
                        {
                            Thread.sleep(100);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }

                        Client client = null;
                        try
                        {
                            client = new Client(new InetSocketAddress(InetAddress.getLocalHost(), 6069));
                        }
                        catch (UnknownHostException e)
                        {
                            e.printStackTrace();
                        }
                        new Thread(client).start();
                        world.setClient(client);

                        updateLoop.startUpdateLoop(world);
                        isPlaying = true;
                        break;
                    }
                    case "Join Server":
                    {
                        gl.glClearColor(0f, 0f, 0f, 1.0f);
                        UpdateLoop updateLoop = new UpdateLoop(this);
                        world = new World(updateLoop, true, false);
                        world.generate();

                        Client client = null;
                        try
                        {
                            client = new Client(new InetSocketAddress(InetAddress.getLocalHost(), 6069));
                        }
                        catch (UnknownHostException e)
                        {
                            e.printStackTrace();
                        }
                        new Thread(client).start();
                        world.setClient(client);

                        updateLoop.startUpdateLoop(world);
                        isPlaying = true;
                        break;
                    }
                    case "Settings":
                    {
                        System.out.println("Initiate hacking");
                        break;
                    }
                    case "Exit":
                    {
                        System.out.println("Goodbye!");
                        System.exit(0);
                        break;
                    }
                }
            }
        }
    }

    private void renderWorld()
    {
        Point perspective = view.getPerspective();
        gl.glRotatef(perspective.getX(), 1, 0, 0);
        gl.glRotatef(perspective.getY(), 0, 1, 0);
        gl.glRotatef(perspective.getZ(), 0, 0, 1);
        Point viewPosition = view.getPosition();
        gl.glTranslatef(viewPosition.getX(), viewPosition.getY(), viewPosition.getZ());

        if (world.getSeed() == 0) return;
        ArrayList<Point> points = world.getAllPoints();
        int pointFieldWidth = world.getChunkPointsPerSide() * world.getChunksX();
        int pointFieldHeight = world.getChunkPointsPerSide() * world.getChunksY();

        Texture grassTexture = world.getGrass().getTexture();
        if (grassTexture != null)
        {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, grassTexture.getTextureObject());
        }
        gl.glBegin(GL2.GL_TRIANGLES);
        int heightMultiplier = 10;
        if (points == null) return;
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
        gl.glEnd();
        gl.glFlush();

        Texture waterTexture = world.getWater().getTexture();
        if (waterTexture != null) gl.glBindTexture(GL2.GL_TEXTURE_2D, waterTexture.getTextureObject());

        setColor(0, 0.5f, 1, 0.9f);
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

        gl.glBegin(GL2.GL_TRIANGLES);
        for (Player player : world.getPlayers())
        {
            setColor(1f, 0f, 0f, 1f);
            Point position = player.getPosition();
            gl.glVertex3f(-position.getX(), -position.getY(), -position.getZ() + 10);
            gl.glVertex3f(-position.getX() - 10, -position.getY(), position.getZ());
            gl.glVertex3f(-position.getX(), -position.getY() - 10, position.getZ() + 5);
        }
        gl.glEnd();
        gl.glFlush();
    }

    private void renderUserInterface()
    {
        TextRenderer textRenderer = new TextRenderer(new Font("Verdana", Font.BOLD, 16));
        textRenderer.beginRendering(windowWidthInPixels, windowHeightInPixels);
        textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        textRenderer.setSmoothing(true);
        textRenderer.draw("O2: " + (int) world.getControlledPlayer().getOxygen()
                + "%          Food: " + (int) world.getControlledPlayer().getFood()
                + "%          Water: " + (int) world.getControlledPlayer().getWater()
                + "%          Health: " + (int) world.getControlledPlayer().getHealth()
                + "%          Stamina: " + (int) world.getControlledPlayer().getStamina() + "%", 10, 10);
        textRenderer.endRendering();
    }

    private float getLength(double xDifference, double yDifference)
    {
        if (xDifference < 0) xDifference *= -1;
        if (yDifference < 0) yDifference *= -1;
        return (float) Math.sqrt(xDifference * xDifference + yDifference * yDifference);
    }

    private float getPixelsPerUnit()
    {
        return windowWidthInPixels / windowWidthInUnits;
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
        if (color < 0f) return 0f;
        else if (color > 1f) return 1f;
        else return color;
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
}