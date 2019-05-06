package ch.kbw.render;

public class Button
{
    // Todo: Add font size
    private String label;
    private float leftX;
    private float topY;
    private float width;
    private float height;

    public Button(String label, float leftX, float topY, float width, float height)
    {
        this.label = label;
        this.leftX = leftX;
        this.topY = topY;
        this.width = width;
        this.height = height;
    }

    public boolean isHovering(float mouseX, float mouseY)
    {
        if (mouseX > leftX && mouseX < leftX + width && mouseY > topY && mouseY < topY + height) return true;
        return false;
    }

    public String getLabel()
    {
        return label;
    }

    public float getLeftX()
    {
        return leftX;
    }

    public float getTopY()
    {
        return topY;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }
}