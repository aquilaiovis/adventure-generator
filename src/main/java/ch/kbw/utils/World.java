package ch.kbw.utils;

import ch.kbw.input.KeyInput;
import ch.kbw.input.MouseInput;
import ch.kbw.multiplayer.Client;
import ch.kbw.packet.PacketPosition;
import ch.kbw.render.RenderLoop;
import ch.kbw.render.Sprite;
import ch.kbw.update.NoiseGenerator;
import ch.kbw.update.Player;
import ch.kbw.update.UpdateLoop;
import ch.kbw.update.View;
import com.jogamp.newt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class World
{
    private UpdateLoop updateLoop;
    private RenderLoop renderLoop;
    private KeyInput keyInput;
    private MouseInput mouseInput;
    private View view;
    private Player controlledPlayer;
    private ArrayList<Player> players;
    private ArrayList<Point> allPoints;
    private int chunksX, chunksY;
    private int chunkPointsPerSide;
    private Sprite grass;
    private Sprite water;
    private NoiseGenerator noiseGenerator;
    private Client client;
    private boolean multiplayer;
    private double seed;

    public World(UpdateLoop updateLoop, boolean multiplayer)
    {
        this.updateLoop = updateLoop;
        this.multiplayer = multiplayer;

        renderLoop = updateLoop.getRenderLoop();
        players = new ArrayList<>();
        controlledPlayer = new Player(new Point(-50, -50, -50), new Point(-80, 0, -180));
        players.add(controlledPlayer);
        view = new View(controlledPlayer.getPosition(), controlledPlayer.getPerspective());
        keyInput = renderLoop.getKeyInput();
        mouseInput = renderLoop.getMouseInput();
        noiseGenerator = new NoiseGenerator();
        grass = new Sprite(renderLoop, "res/minegrass.jpg");
        water = new Sprite(renderLoop, "res/minewater.jpg");

        chunksX = 1;
        chunksY = 1;
        chunkPointsPerSide = 100;
    }

    public void addPlayer(int id)
    {
        System.out.println("Added Player with id: " + id);
        Player player = new Player(new Point(-50, -50, -50), new Point(-80, 0, -180));
        player.setId(id);
        players.add(player);
    }

    public void generate()
    {
        Random random = new Random();
        seed = random.nextDouble() + 1;
        ArrayList<Double> heights = noiseGenerator.getHeights(chunksX, chunksY, seed);
        allPoints = new ArrayList<>();

        for (int x = 0; x < chunksX * chunkPointsPerSide; x++)
        {
            for (int y = 0; y < chunksY * chunkPointsPerSide; y++)
            {
                allPoints.add(new Point((float) x, (float) y, Float.parseFloat(Double.toString(heights.get(x * chunksX * chunkPointsPerSide + y))) * 5));
            }
        }
    }

    public void checkPaused()
    {
        if (keyInput.isPressed(KeyEvent.VK_ESCAPE))
        {
            updateLoop.setPaused(!updateLoop.isPaused());
        }
    }

    public void update()
    {
        handlePlayerRotation();
        handlePlayerMovement();
        if (multiplayer)
        {
            Point position = controlledPlayer.getPosition();
            Point perspective = controlledPlayer.getPerspective();
            PacketPosition packetPosition = new PacketPosition(position.getX(),
                    position.getY(),
                    position.getZ(),
                    perspective.getX(),
                    perspective.getY(),
                    perspective.getZ(),
                    controlledPlayer.getId());
            client.queuePacket(packetPosition);
        }
        controlledPlayer.update();
    }

    private void handlePlayerRotation()
    {
        Point perspective = controlledPlayer.getPerspective();
        perspective.addToX(-(mouseInput.getvY() / 10));
        perspective.addToZ(-(mouseInput.getvX() / 10));
        float rotationSpeed = 0.5f;
        if (keyInput.isPressed(KeyEvent.VK_NUMPAD2))
        {
            perspective.addToX(rotationSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_NUMPAD8))
        {
            perspective.addToX(-rotationSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_NUMPAD7))
        {
            perspective.addToY(rotationSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_NUMPAD9))
        {
            perspective.addToY(-rotationSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_NUMPAD4))
        {
            perspective.addToZ(-rotationSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_NUMPAD6))
        {
            perspective.addToZ(rotationSpeed);
        }
    }

    private void handlePlayerMovement()
    {
        Point position = controlledPlayer.getPosition();
        float movementSpeed = 0.1f;
        if (controlledPlayer.getStamina() <= 20)
        {
            movementSpeed = movementSpeed / 5;
        }
        else if (controlledPlayer.getStamina() <= 50)
        {
            movementSpeed = movementSpeed / 2;
        }
        float floatingHeight = 5f;

        boolean resting = true;
        if (keyInput.isPressed(KeyEvent.VK_SHIFT) && controlledPlayer.getStamina() >= 60)
        {
            movementSpeed *= 2;
            controlledPlayer.setStamina(controlledPlayer.getStamina() - 0.1f);
            controlledPlayer.setHydration(controlledPlayer.getHydration() - 0.001f);
        }
        else if (keyInput.isPressed(KeyEvent.VK_CONTROL))
        {
            movementSpeed /= 2;
            controlledPlayer.setStamina(controlledPlayer.getStamina() + 0.02f);
            floatingHeight = 0.5f;
        }
        if (keyInput.isPressed(KeyEvent.VK_D))
        {
            resting = false;
            position.addToY(movementSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_A))
        {
            resting = false;
            position.addToY(-movementSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_W))
        {
            resting = false;
            position.addToX(-movementSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_S))
        {
            resting = false;
            position.addToX(movementSpeed);
        }
        if (resting
                && controlledPlayer.getSaturation() >= 40
                && controlledPlayer.getOxygen() >= 80
                && controlledPlayer.getHydration() >= 45
                && controlledPlayer.getHealth() >= 60)
        {
            controlledPlayer.setStamina(controlledPlayer.getStamina() + 0.12f);
        }
        controlledPlayer.setStamina(controlledPlayer.getStamina() - 0.0075f);

        for (Point point : allPoints)
        {
            if (point.getX() == ((int) position.getX() * -1) && point.getY() == ((int) position.getY() * -1))
            {
                position.setZ(point.getZ() * -10 - floatingHeight);
            }
        }
    }

    public ArrayList<Point> getAllPoints()
    {
        return allPoints;
    }

    public int getChunksX()
    {
        return chunksX;
    }

    public int getChunksY()
    {
        return chunksY;
    }

    public int getChunkPointsPerSide()
    {
        return chunkPointsPerSide;
    }

    public Sprite getGrass()
    {
        return grass;
    }

    public Sprite getWater()
    {
        return water;
    }

    public View getView()
    {
        return view;
    }

    public Player getControlledPlayer()
    {
        return controlledPlayer;
    }

    public void setClient(Client client)
    {
        this.client = client;
        client.getHandlePacket().setWorld(this);
    }

    public ArrayList<Player> getPlayers()
    {
        return players;
    }

    public double getSeed()
    {
        return seed;
    }

    public void setSeed(double seed)
    {
        this.seed = seed;
    }

    public UpdateLoop getUpdateLoop()
    {
        return updateLoop;
    }
}