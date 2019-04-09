package ch.kbw.utils;

import ch.kbw.input.KeyInput;
import ch.kbw.input.MouseInput;
import ch.kbw.render.RenderLoop;
import ch.kbw.render.Sprite;
import ch.kbw.update.NoiseGenerator;
import ch.kbw.update.Player;
import ch.kbw.update.UpdateLoop;
import ch.kbw.update.View;
import com.jogamp.newt.event.KeyEvent;
import java.util.ArrayList;

public class World
{
    private UpdateLoop updateLoop;
    private RenderLoop renderLoop;
    private KeyInput keyInput;
    private MouseInput mouseInput;
    private View view;
    private Player player;
    private ArrayList<Point> allPoints;
    private int chunksX, chunksY;
    private int chunkPointsPerSide;
    private Sprite grass;
    private Sprite water;

    public World(UpdateLoop updateLoop)
    {
        this.updateLoop = updateLoop;
        renderLoop = updateLoop.getRenderLoop();
        player = new Player(new Point(-50, -50, -50), new Point(-80, 0, -180));
        view = new View(player.getPosition(), player.getPerspective());
        keyInput = renderLoop.getKeyInput();
        mouseInput = renderLoop.getMouseInput();
        NoiseGenerator noiseGenerator = new NoiseGenerator();
        grass = new Sprite(renderLoop, "res/minegrass.jpg");
        water = new Sprite(renderLoop, "res/minewater.jpg");

        chunksX = 1;
        chunksY = 1;
        chunkPointsPerSide = 100;

        // Todo: Fix arguments
        ArrayList<Double> heights = noiseGenerator.getHeights(chunksX, chunksY);
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
        player.update();
    }

    private void handlePlayerRotation()
    {
        Point perspective = player.getPerspective();
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
        Point position = player.getPosition();
        float movementSpeed = 0.1f;
        if(player.getStamina()<=20){
            movementSpeed = movementSpeed / 5;
        }else if(player.getStamina()<=50){
            movementSpeed = movementSpeed / 2;
        }
        float floatingHeight = 0.8f;
        float pZ = player.getPerspective().getZ();
        if (pZ < 0) {
            //Denn isch dumm gloffe...
            pZ = pZ * -1;
        }
        pZ=pZ%360;
        int sector = (int)(pZ/90);
        float vX = (pZ%90)/90;
        float vY = -vX+1;
        System.out.println(sector);

        if(sector==0){
            vX = vX*-1;
        }else if(sector==3){//Stimmt
            vX = vX*-1;
            vY = vY*-1;
        }else if(sector==2) {
            vY = vY * -1;
        }
        boolean isResting = true;
        if (keyInput.isPressed(KeyEvent.VK_SHIFT) && player.getStamina()>=60)
        {
            movementSpeed *= 2;
            player.setStamina(player.getStamina()-0.1f);
            player.setWater(player.getWater()-0.001f);
        }else if (keyInput.isPressed(KeyEvent.VK_CONTROL))
        {
            movementSpeed /= 2;
            player.setStamina(player.getStamina()+0.02f);
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
                player.getFood()>=40&&
                player.getOxygen()>=80&&
                player.getWater()>=45&&
                player.getHealth()>=60){
            player.setStamina(player.getStamina()+0.12f);
        }
        player.setStamina(player.getStamina()-0.0075f);

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

    public Player getPlayer()
    {
        return player;
    }
}