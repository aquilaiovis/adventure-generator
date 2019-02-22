package ch.kbw.render;

import ch.kbw.utils.Point;
import com.jogamp.opengl.GL2;

public class ShapeRenderer
{
    // Singleton
    private static ShapeRenderer instance;

    // Attributes
    private GL2 gl;

    private ShapeRenderer()
    {
        gl = WorldRenderer.getInstance().getGL2();
    }

    public static ShapeRenderer getInstance()
    {
        if (instance == null)
        {
            instance = new ShapeRenderer();
        }
        return instance;
    }

    public void setColor(float red, float green, float blue, float alpha)
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

    public void drawTriangle(Point point1, Point point2, Point point3)
    {
        gl.glBegin(GL2.GL_TRIANGLES);

        gl.glColor3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.5f, 2.0f, -6.0f);

        gl.glColor3f(0.5f, 0.0f, 0.0f);
        gl.glVertex3f(-1.5f, -1.0f, -5.0f);

        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glVertex3f(0.5f, -1.0f, -5.0f);

        // Finnish drawing
        gl.glEnd();

        gl.glFlush();
    }
}
