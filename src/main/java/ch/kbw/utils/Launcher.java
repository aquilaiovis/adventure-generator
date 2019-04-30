package ch.kbw.utils;

import ch.kbw.multiplayer.Client;
import ch.kbw.multiplayer.Server;
import ch.kbw.render.RenderLoop;
import ch.kbw.update.UpdateLoop;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Launcher {
    public static void main(String[] args) {
        RenderLoop renderLoop = new RenderLoop(60, 1080, 720, false, false, 100);
        boolean multiplayer = false;
        boolean server = false;
        if(args.length>0){
            multiplayer = Boolean.parseBoolean(args[0]);
            server = Boolean.parseBoolean(args[1]);
        }
        UpdateLoop updateLoop = new UpdateLoop(renderLoop, multiplayer,server);

        if (updateLoop.getWorld().isMultiplayer()) {
            if (updateLoop.getWorld().isServer()) {
                Server s = new Server(6069);
                new Thread(s).start();
            }
            Client c = null;
            try {
                c = new Client(new InetSocketAddress(InetAddress.getLocalHost(), 6069));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            new Thread(c).start();
            updateLoop.getWorld().setClient(c);

            updateLoop.getWorld().generateWorld();
        }
        renderLoop.setWorld(updateLoop.getWorld());
        updateLoop.startUpdateLoop();
    }
}