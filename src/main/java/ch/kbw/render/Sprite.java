package ch.kbw.render;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class Sprite
{
    private Texture texture;
    private BufferedImage image;

    public Sprite(String path)
    {
        image = null;
        texture = null;
        URL url = Sprite.class.getResource(path);

        try
        {
            image = ImageIO.read(new FileInputStream(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (image != null)
        {
            image.flush();
        }
    }

    public Texture getTexture()
    {
        if (image == null)
        {
            return null;
        }

        if (texture == null)
        {
            texture = AWTTextureIO.newTexture(WindowRenderer.getInstance().getProfile(), image, true);
        }

        return texture;
    }
}