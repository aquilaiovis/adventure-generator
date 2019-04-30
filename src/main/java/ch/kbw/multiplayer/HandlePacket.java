package ch.kbw.multiplayer;

import ch.kbw.packet.Packet;
import ch.kbw.packet.PacketPosition;
import ch.kbw.packet.PacketRegisterUser;
import ch.kbw.packet.PacketSeed;
import ch.kbw.update.Player;
import ch.kbw.utils.Point;
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
            world.getControlledPlayer().setId(ps.getId());
        }else if(packet instanceof PacketPosition){
            PacketPosition pp = (PacketPosition) packet;
            for(Player p : world.getPlayers()){
                if(pp.getId() == p.getId()&&p!=world.getControlledPlayer()){
                    p.setPosition(new Point(pp.getX(),pp.getY(),pp.getZ()));
                    p.setPerspective(new Point(pp.getpX(),pp.getpY(),pp.getpZ()));
                }
            }
        }else if(packet instanceof PacketRegisterUser){
            PacketRegisterUser pru = (PacketRegisterUser) packet;
            world.addPlayer(pru.getId());
        }
    }
}
