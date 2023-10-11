package model;

//Represents an arbitrary boss-issued attack with a given size and position

public class BossAttack {

    private int width;
    private int height;
    private double posX;
    private double posY;

    public BossAttack(int width, int height, double posX, double posY) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
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
}
