package ch.kbw.packet;

import java.nio.ByteBuffer;

public class PacketRegisterUser extends Packet {
    private int id;

    public PacketRegisterUser(){}

    public PacketRegisterUser(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void serialize(ByteBuffer buffer) {
        buffer.putInt(id);
    }

    @Override
    public void deserialize(ByteBuffer buffer) {
        id = buffer.getInt();
    }
}
