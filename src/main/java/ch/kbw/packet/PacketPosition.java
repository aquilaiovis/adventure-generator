package ch.kbw.packet;

import java.nio.ByteBuffer;

public class PacketPosition extends Packet
{
    private float positionX;
    private float positionY;
    private float positionZ;
    private float perspectiveX;
    private float perspectiveY;
    private float perspectiveZ;
    private int id;

    public PacketPosition(float positionX, float positionY, float positionZ, float perspectiveX, float perspectiveY, float perspectiveZ, int id)
    {
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.perspectiveX = perspectiveX;
        this.perspectiveY = perspectiveY;
        this.perspectiveZ = perspectiveZ;
        this.id = id;
    }

    public float getPositionX()
    {
        return positionX;
    }

    public float getPositionY()
    {
        return positionY;
    }

    public float getPositionZ()
    {
        return positionZ;
    }

    public float getPerspectiveX()
    {
        return perspectiveX;
    }

    public float getPerspectiveY()
    {
        return perspectiveY;
    }

    public float getPerspectiveZ()
    {
        return perspectiveZ;
    }

    public int getId()
    {
        return id;
    }

    @Override
    public void serialize(ByteBuffer buffer)
    {
        //TODO: Serialize
        buffer.putFloat(positionX);
        buffer.putFloat(positionY);
        buffer.putFloat(positionZ);
        buffer.putFloat(perspectiveX);
        buffer.putFloat(perspectiveY);
        buffer.putFloat(perspectiveZ);
        buffer.putInt(id);
    }

    @Override
    public void deserialize(ByteBuffer buffer)
    {
        //TODO: Deserialize
        positionX = buffer.getFloat();
        positionY = buffer.getFloat();
        positionZ = buffer.getFloat();
        perspectiveX = buffer.getFloat();
        perspectiveY = buffer.getFloat();
        perspectiveZ = buffer.getFloat();
        id = buffer.getInt();
    }
}