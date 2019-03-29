package ch.kbw.render;

import ch.kbw.utils.Point;
import com.jogamp.opengl.GL2;
import java.util.ArrayList;

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

    public void drawTriangles(ArrayList<Point> points, int pointFieldWidth)
    {
        gl.glBindTexture(GL2.GL_TEXTURE_2D, WorldRenderer.getInstance().getTextureNumber());
        gl.glBegin(GL2.GL_TRIANGLES);

        int heightMultiplier = 5;
        for (int i = 0; i < points.size() - pointFieldWidth - 1; i++)
        {
            float heightinator = points.get(i).getZ() * 15;
            if(heightinator < 0)
            {
                heightinator = 0;
            }
            if (getLength(points.get(i).getX() - points.get(i + 1).getX(), points.get(i).getY() - points.get(i + 1).getY()) < 2
                    && getLength(points.get(i + 1).getX() - points.get(i + pointFieldWidth).getX(), points.get(i + 1).getY() - points.get(i + pointFieldWidth).getY()) < 2
                    && getLength(points.get(i + pointFieldWidth).getX() - points.get(i).getX(), points.get(i + pointFieldWidth).getY() - points.get(i).getY()) < 2)
            {
                setColor(0.30f + heightinator, 0.80f + heightinator, 0.30f + heightinator, 1);
                gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(points.get(i).getX(), points.get(i).getY(), points.get(i).getZ() * heightMultiplier);
                gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(points.get(i + 1).getX(), points.get(i + 1).getY(), points.get(i + 1).getZ() * heightMultiplier);
                gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(points.get(i + pointFieldWidth).getX(), points.get(i + pointFieldWidth).getY(), points.get(i + pointFieldWidth).getZ() * heightMultiplier);
            }

            if (getLength(points.get(i + 1).getX() - points.get(i + pointFieldWidth).getX(), points.get(i + 1).getY() - points.get(i + pointFieldWidth).getY()) < 2
                    && getLength(points.get(i + pointFieldWidth).getX() - points.get(i + pointFieldWidth + 1).getX(), points.get(i + pointFieldWidth).getY() - points.get(i + pointFieldWidth + 1).getY()) < 2
                    && getLength(points.get(i + pointFieldWidth + 1).getX() - points.get(i + 1).getX(), points.get(i + pointFieldWidth + 1).getY() - points.get(i + 1).getY()) < 2)
            {
                setColor(0.20f + heightinator, 0.60f + heightinator, 0.20f + heightinator, 1);
                gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(points.get(i + 1).getX(), points.get(i + 1).getY(), points.get(i + 1).getZ() * heightMultiplier);
                gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(points.get(i + pointFieldWidth).getX(), points.get(i + pointFieldWidth).getY(), points.get(i + pointFieldWidth).getZ() * heightMultiplier);
                gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(points.get(i + pointFieldWidth + 1).getX(), points.get(i + pointFieldWidth + 1).getY(), points.get(i + pointFieldWidth + 1).getZ() * heightMultiplier);
            }

        }

        // Finnish drawing
        gl.glEnd();

        gl.glFlush();

        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);
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
}