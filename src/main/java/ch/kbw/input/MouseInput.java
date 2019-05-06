package ch.kbw.input;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;

public class MouseInput implements MouseListener
{
    private float mouseX, mouseY;
    private boolean[] buttons;
    private float pixelsPerUnit, windowWidth, windowHeight;
    private float vX, vY;

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
        buttons[e.getButton()] = true;
    }

    public void mouseReleased(MouseEvent e)
    {
        buttons[e.getButton()] = false;
    }

    public void mouseMoved(MouseEvent e)
    {
        vX += mouseX - e.getX();
        vY += mouseY - e.getY();
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void mouseDragged(MouseEvent e)
    {

    }

    public void mouseWheelMoved(MouseEvent e)
    {

    }

    public float getvX()
    {
        float x = vX;
        vX = 0;
        return x;
    }

    public float getvY()
    {
        float y = vY;
        vY = 0;
        return y;
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