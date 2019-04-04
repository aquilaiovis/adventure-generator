package ch.kbw.input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class MouseInput implements MouseListener
{
    private float mouseX, mouseY;
    private boolean[] buttons;
    private float pixelsPerUnit, windowWidth, windowHeight;

    public MouseInput(float pixelsPerUnit, float windowWidth, float windowHeight)
    {
        this.pixelsPerUnit = pixelsPerUnit;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        mouseX = 0;
        mouseY = 0;

        buttons = new boolean[9];
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
        // Todo: Update this
        mouseX = e.getX() / pixelsPerUnit - windowWidth / 2;
        mouseY = -(e.getY() / pixelsPerUnit) + windowHeight;
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