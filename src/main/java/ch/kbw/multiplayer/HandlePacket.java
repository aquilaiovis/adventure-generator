package ch.kbw.multiplayer;

import ch.kbw.packet.Packet;
import ch.kbw.packet.PacketSeed;
import ch.kbw.utils.World;

import java.net.SocketAddress;

public class HandlePacket {

    private Server server;
    private World world;

    public HandlePacket() {
    }

    public HandlePacket(Server server) {
        this.server = server;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void handlePacket(SocketAddress address, Packet packet){
        if(packet instanceof PacketSeed){
            System.out.println("Seed received");
            PacketSeed ps = (PacketSeed) packet;
            world.setSeed(ps.getSeed());
        }
    }
}
