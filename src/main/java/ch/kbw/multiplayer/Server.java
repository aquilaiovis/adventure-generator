package ch.kbw.multiplayer;

import ch.kbw.packet.Packet;
import ch.kbw.packet.PacketRegisterUser;
import ch.kbw.packet.PacketSeed;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    private ServerSocketChannel channel;
    private ArrayList<Packet> queue;

    private HashMap<SocketAddress, ServerMember> clients;

    private int port;
    private boolean running = false;

    private double seed;

    private HandlePacket handlePacket;

    public Server(int port) {
        this.port = port;
        running = true;
        handlePacket = new HandlePacket(this);
        clients = new HashMap<>();
        queue = new ArrayList<>();
        try {
            channel = ServerSocketChannel.open();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSeed(double seed) {
        this.seed = seed;
    }

    public HashMap<SocketAddress, ServerMember> getClients() {
        return clients;
    }

    public void queuePacket(Packet packet) {
        queue.add(packet);
    }

    @Override
    public void run() {
        try {
            channel.bind(new InetSocketAddress(InetAddress.getLocalHost(), port));
            channel.configureBlocking(false);

            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer readBuffer = ByteBuffer.allocate(2048);
            ByteBuffer writeBuffer = ByteBuffer.allocate(2048);
            System.out.println("Server is running now");

            Random r = new Random();
            seed = r.nextDouble() + 1;

            while (running) {
                if (selector.selectNow() > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            try {
                                System.out.println("User Found");

                                SocketChannel sChannel = channel.accept();
                                SocketAddress sender = sChannel.getRemoteAddress();

                                sChannel.configureBlocking(false);

                                sChannel.register(selector, SelectionKey.OP_READ);

                                //Seed versenden
                                PacketSeed ps = new PacketSeed(seed, r.nextInt());
                                PacketRegisterUser pru = new PacketRegisterUser(ps.getId());
                                for (SocketAddress a : clients.keySet()) {
                                    if (!a.equals(clients.get(sChannel))) {
                                        pru.addTarget(a);
                                    }
                                }
                                queuePacket(pru);
                                for(ServerMember sm : clients.values()) {
                                    pru = new PacketRegisterUser(sm.getId());
                                    ps.addTarget(sChannel.getRemoteAddress());
                                    queuePacket(pru);
                                }
                                clients.put(sChannel.getRemoteAddress(), new ServerMember(sChannel, ps.getId()));
                                ps.addTarget(sChannel.getRemoteAddress());
                                queuePacket(ps);
                            } catch (ClosedChannelException ex) {
                                System.out.println("User disconnected");
                            }
                        }
                        if (key.isReadable()) {
                            SocketChannel sChannel = (SocketChannel) key.channel();

                            readBuffer.position(0);
                            readBuffer.limit(readBuffer.capacity());
                            sChannel.read(readBuffer);
                            readBuffer.flip();

                            Packet packet = Packet.decompilePacket(readBuffer);
                            packet.clearTargets();
                            for (SocketAddress a : clients.keySet()) {
                                if (!a.equals(clients.get(sChannel))) {
                                    packet.addTarget(a);
                                }
                            }
                            queuePacket(packet);

                        }
                        iterator.remove();
                    }
                }

                if (!queue.isEmpty()) {
                    Iterator<Packet> packetIterator = queue.iterator();
                    while (packetIterator.hasNext()) {
                        Packet packet = packetIterator.next();
                        writeBuffer.position(0);
                        writeBuffer.limit(writeBuffer.capacity());
                        Packet.compilePacket(packet, writeBuffer);
                        writeBuffer.flip();

                        Iterator<SocketAddress> targetIterator = packet.getTargets().iterator();

                        while (targetIterator.hasNext()) {
                            SocketAddress target = targetIterator.next();
                            System.out.println(target);
                            clients.get(target).getSocketChannel().write(writeBuffer);
                            writeBuffer.flip();
                        }

                        packetIterator.remove();
                    }
                }
            }

        } catch (UnknownHostException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("User disconnected 2");
        }

    }
}
