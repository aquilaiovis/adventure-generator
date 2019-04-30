package ch.kbw.packet;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public class PacketPosition extends Packet {

    private float x;
    private float y;
    private float z;
    private float pX;
    private float pY;
    private float pZ;
    private int id;

    public PacketPosition() {

    }

    public PacketPosition(float x, float y, float z, float pX, float pY, float pZ, int id) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pX = pX;
        this.pY = pY;
        this.pZ = pZ;
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getpX() {
        return pX;
    }

    public float getpY() {
        return pY;
    }

    public float getpZ() {
        return pZ;
    }

    public int getId() {
        return id;
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        //TODO: Serialize
        buffer.putFloat(x);
        buffer.putFloat(y);
        buffer.putFloat(z);
        buffer.putFloat(pX);
        buffer.putFloat(pY);
        buffer.putFloat(pZ);
        buffer.putInt(id);
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        //TODO: Deserialize
        x = buffer.getFloat();
        y = buffer.getFloat();
        z = buffer.getFloat();
        pX = buffer.getFloat();
        pY = buffer.getFloat();
        pZ = buffer.getFloat();
        id = buffer.getInt();
    }
}
