package ch.kbw.packet;

import java.nio.ByteBuffer;

public class PacketSeed extends Packet {
    private double seed;

    public PacketSeed(){}

    public PacketSeed(double seed) {
        this.seed = seed;
    }

    public double getSeed() {
        return seed;
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.putDouble(seed);
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        seed = buffer.getDouble();
    }
}
