package ch.kbw.input;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyInput implements KeyListener
{
    // Singleton
    private static KeyInput instance;

    // Attributes
    private boolean[] keys;

    private KeyInput()
    {
        // As of 01.03.2019 there are 171 different key codes for the Newt KeyEvent
        keys = new boolean[256];
    }

    public static KeyInput getInstance()
    {
        if (instance == null)
        {
            instance = new KeyInput();
        }
        return instance;
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