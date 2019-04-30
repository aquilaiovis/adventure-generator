package ch.kbw.utils;

import ch.kbw.input.KeyInput;
import ch.kbw.input.MouseInput;
import ch.kbw.multiplayer.Client;
import ch.kbw.packet.Packet;
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
    private double seed = 0;
    private boolean isServer = false;
    private boolean multiplayer = false;

    public World(UpdateLoop updateLoop, boolean multiplayer, boolean server)
    {
        this.updateLoop = updateLoop;
        renderLoop = updateLoop.getRenderLoop();
        players = new ArrayList<>();
        Player player1 = new Player(new Point(-50, -50, -50), new Point(-80, 0, -180));
        players.add(player1);
        controlledPlayer = player1;
        view = new View(controlledPlayer.getPosition(), controlledPlayer.getPerspective());
        keyInput = renderLoop.getKeyInput();
        mouseInput = renderLoop.getMouseInput();
        noiseGenerator = new NoiseGenerator();
        grass = new Sprite(renderLoop, "res/minegrass.jpg");
        water = new Sprite(renderLoop, "res/minewater.jpg");
        this.multiplayer = multiplayer;
        this.isServer = server;

        chunksX = 1;
        chunksY = 1;
        chunkPointsPerSide = 100;



        // Todo: Fix arguments
        if(!multiplayer) {
            Random r = new Random();
            seed = r.nextDouble() + 1;
            ArrayList<Double> heights = noiseGenerator.getHeights(chunksX, chunksY, seed);
            allPoints = new ArrayList<>();

            for (int x = 0; x < chunksX * chunkPointsPerSide; x++) {
                for (int y = 0; y < chunksY * chunkPointsPerSide; y++) {
                    allPoints.add(new Point((float) x, (float) y, Float.parseFloat(Double.toString(heights.get(x * chunksX * chunkPointsPerSide + y))) * 5));
                }
            }
        }
    }

    public void addPlayer(int id){
        System.out.println("added Player with id: " + id);
        Player p = new Player(new Point(-50, -50, -50), new Point(-80, 0, -180));
        p.setId(id);
        players.add(p);
    }

    public void generateWorld(){
        while(seed==0){}
        ArrayList<Double> heights = noiseGenerator.getHeights(chunksX, chunksY, seed);
        allPoints = new ArrayList<>();

        for (int x = 0; x < chunksX * chunkPointsPerSide; x++) {
            for (int y = 0; y < chunksY * chunkPointsPerSide; y++) {
                allPoints.add(new Point((float) x, (float) y, Float.parseFloat(Double.toString(heights.get(x * chunksX * chunkPointsPerSide + y))) * 5));
            }
        }
    }

    public void setSeed(double seed) {
        this.seed = seed;
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
        if(multiplayer) {
            Point pos = controlledPlayer.getPosition();
            Point per = controlledPlayer.getPerspective();
            PacketPosition pp = new PacketPosition(pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    per.getX(),
                    per.getY(),
                    per.getZ(),
                    controlledPlayer.getId());
            client.queuePacket(pp);
        }
        controlledPlayer.update();
    }

    private void handlePlayerRotation()
    {
        Point perspective = controlledPlayer.getPerspective();
        perspective.addToX(-(mouseInput.getvY()/10));
        perspective.addToZ(-(mouseInput.getvX()/10));
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
        if(controlledPlayer.getStamina()<=20){
            movementSpeed = movementSpeed / 5;
        }else if(controlledPlayer.getStamina()<=50){
            movementSpeed = movementSpeed / 2;
        }
        float floatingHeight = 0.8f;
        float pZ = controlledPlayer.getPerspective().getZ();
        if (pZ < 0) {
            //Denn isch dumm gloffe...
            pZ = pZ * -1;
        }
        pZ=pZ%360;
        int sector = (int)(pZ/90);
        float vX = (pZ%90)/90;
        float vY = -vX+1;

        if(sector==0){
            vX = vX*-1;
        }else if(sector==3){//Stimmt
            vX = vX*-1;
            vY = vY*-1;
        }else if(sector==2) {
            vY = vY * -1;
        }
        //System.out.println(sector);

        if(sector==0){
            vX = vX*-1;
        }else if(sector==3){//Stimmt
            vX = vX*-1;
            vY = vY*-1;
        }else if(sector==2) {
            vY = vY * -1;
        }
        boolean isResting = true;
        if (keyInput.isPressed(KeyEvent.VK_SHIFT) && controlledPlayer.getStamina()>=60)
        {
            movementSpeed *= 2;
            controlledPlayer.setStamina(controlledPlayer.getStamina()-0.1f);
            controlledPlayer.setWater(controlledPlayer.getWater()-0.001f);
        }else if (keyInput.isPressed(KeyEvent.VK_CONTROL))
        {
            movementSpeed /= 2;
            controlledPlayer.setStamina(controlledPlayer.getStamina()+0.02f);
            floatingHeight = 0.5f;
        }
        if (keyInput.isPressed(KeyEvent.VK_D))
        {
            isResting = false;
            position.addToX(movementSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_A))
        {
            isResting = false;
            position.addToX(-movementSpeed);
        }
        if (keyInput.isPressed(KeyEvent.VK_W))
        {
            isResting = false;
            position.addToX(movementSpeed*vY);
            position.addToY(movementSpeed*vX);
        }
        if (keyInput.isPressed(KeyEvent.VK_S))
        {
            isResting = false;
            position.addToX(-(movementSpeed*vY));
            position.addToY(-(movementSpeed*vX));
        }
        if(isResting&&
                controlledPlayer.getFood()>=40&&
                controlledPlayer.getOxygen()>=80&&
                controlledPlayer.getWater()>=45&&
                controlledPlayer.getHealth()>=60){
            controlledPlayer.setStamina(controlledPlayer.getStamina()+0.12f);
        }
        controlledPlayer.setStamina(controlledPlayer.getStamina()-0.0075f);

        for (Point point : allPoints)
        {
            if (point.getX() == ((int) position.getX() * -1) && point.getY() == ((int) position.getY() * -1))
            {
                position.setZ(-((point.getZ() + floatingHeight) * 10));
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

    public void setClient(Client client) {
        this.client = client;
        client.getHandlePacket().setWorld(this);
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    public boolean isMultiplayer() {
        return multiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.multiplayer = multiplayer;
    }

    public double getSeed() {
        return seed;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}