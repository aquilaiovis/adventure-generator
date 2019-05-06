package ch.kbw.packet;

import java.nio.ByteBuffer;

public class PacketSeed extends Packet
{
    private double seed;
    private int id;

    public PacketSeed(double seed, int id)
    {
        this.seed = seed;
        this.id = id;
    }

    public double getSeed()
    {
        return seed;
    }

    public int getId()
    {
        return id;
    }

    @Override
    public void serialize(ByteBuffer buffer)
    {
        buffer.putDouble(seed);
        buffer.putInt(id);
    }

    @Override
    public void deserialize(ByteBuffer buffer)
    {
        seed = buffer.getDouble();
        id = buffer.getInt();
    }
}