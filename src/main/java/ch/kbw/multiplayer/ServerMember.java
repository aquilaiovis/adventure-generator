package ch.kbw.multiplayer;

import java.nio.channels.SocketChannel;

public class ServerMember {
    private SocketChannel socketChannel;
    private int id;

    public ServerMember(SocketChannel socketChannel, int id) {
        this.socketChannel = socketChannel;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
}
