package ch.kbw.input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import ch.kbw.render.WindowRenderer;

public class MouseInput implements MouseListener
{
    // Singleton
    private static MouseInput instance;

    // Attributes
    private float mouseX, mouseY;
    private boolean[] buttons;

    private MouseInput()
    {
        mouseX = 0;
        mouseY = 0;

        buttons = new boolean[9];
    }

    public static MouseInput getInstance()
    {
        if (instance == null)
        {
            instance = new MouseInput();
        }
        return instance;
    }

    public void mouseClicked(MouseEvent e)
    {

    }

    public void mouseEntered(MouseEvent e)
    {

    }

    public void mouseExited(MouseEvent e)
    {

    }

    public void mousePressed(MouseEvent e)
    {
        if (0 == (KeyEvent.AUTOREPEAT_MASK & e.getModifiers()))
        {
            buttons[e.getButton()] = true;
        }
    }

    public void mouseReleased(MouseEvent e)
    {
        if (0 == (KeyEvent.AUTOREPEAT_MASK & e.getModifiers()))
        {
            buttons[e.getButton()] = false;
        }
    }

    public void mouseMoved(MouseEvent e)
    {
        mouseX = e.getX() / WindowRenderer.getInstance().getPixelsPerUnit() - WindowRenderer.getInstance().getWindowWidth() / 2;
        mouseY = -(e.getY() / WindowRenderer.getInstance().getPixelsPerUnit()) + WindowRenderer.getInstance().getWindowHeight();
    }

    public void mouseDragged(MouseEvent e)
    {

    }

    public void mouseWheelMoved(MouseEvent e)
    {

    }

    public float getMouseX()
    {
        return mouseX;
    }

    public float getMouseY()
    {
        return mouseY;
    }

    public boolean isPressed(int keyCode)
    {
        return buttons[keyCode];
    }
}