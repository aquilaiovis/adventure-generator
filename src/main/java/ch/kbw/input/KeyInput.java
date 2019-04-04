package ch.kbw.input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyInput implements KeyListener
{
    private boolean[] keys;

    public KeyInput()
    {
        // As of 04.04.2019 there are 171 different key codes for the Newt KeyEvent
        keys = new boolean[256];
    }

    public void keyPressed(KeyEvent e)
    {
        if (0 == (KeyEvent.AUTOREPEAT_MASK & e.getModifiers()))
        {
            keys[e.getKeyCode()] = true;
        }
    }

    public void keyReleased(KeyEvent e)
    {
        if (0 == (KeyEvent.AUTOREPEAT_MASK & e.getModifiers()))
        {
            keys[e.getKeyCode()] = false;
        }
    }

    public boolean isPressed(int keyCode)
    {
        return keys[keyCode];
    }
}