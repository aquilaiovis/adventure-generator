package ch.kbw.render;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import ch.kbw.utils.Point;

public class SpriteRenderer
{
    // Singleton
    private static SpriteRenderer instance;

    // Attributes
    private GL2 gl;

    private SpriteRenderer()
    {
        gl = WorldRenderer.getInstance().getGL2();
    }

    public static SpriteRenderer getInstance()
    {
        if (instance == null)
        {
            instance = new SpriteRenderer();
        }
        return instance;
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
}