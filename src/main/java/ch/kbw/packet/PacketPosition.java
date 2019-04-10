package ch.kbw.packet;

import java.nio.ByteBuffer;

public class PacketPosition extends Packet{

    private float x;
    private float y;
    private float z;
    private float pX;
    private float pY;
    private float pZ;

    public PacketPosition(){

    }
    public PacketPosition(float x, float y, float z, float pX, float pY, float pZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pX = pX;
        this.pY = pY;
        this.pZ = pZ;
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

    @Override
    public void serialize(ByteBuffer buffer) {
        //TODO: Serialize
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        //TODO: Deserialize
    }
}
