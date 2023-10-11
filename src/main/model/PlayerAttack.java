package model;

//Represents an arbitrary player-issued attack with a given size, position, and lifespan.

public class PlayerAttack {

    private int width;
    private int height;
    private double posX;
    private double posY;
    private int lifespan;
    private boolean moving;
    private int facing;

    //EFFECTS: creates a new player attack with the specified position, dimensions, and other misc. properties
    public PlayerAttack(int width, int height, double posX, double posY, int lifespan, boolean moving, int facing) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
        this.lifespan = lifespan;
        this.moving = moving;
        this.facing = facing;
    }

    //EFFECTS: moves the position of the attack by amountX in the x direction and amountY in the y direction
    //MODIFIES: this
    void move(double amountX, double amountY) {
        this.posX += amountX;
        this.posY += amountY;
    }

    //EFFECTS: reduces the lifespan of the player attack by 1
    //MODIFIES: this
    void subtractLifespan() {
        this.lifespan -= 1;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public double getX() {
        return this.posX;
    }

    public double getY() {
        return this.posY;
    }

    public int getLifespan() {
        return this.lifespan;
    }

    public boolean getMoving() {
        return this.moving;
    }

    public int getFacing() {
        return this.facing;
    }
}
