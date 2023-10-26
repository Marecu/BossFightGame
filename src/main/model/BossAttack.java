package model;

//Represents an arbitrary boss-issued attack with a given size and position

import org.json.JSONObject;

public class BossAttack {

    private int width;
    private int height;
    private double posX;
    private double posY;


    //EFFECTS: creates a new boss attack with the specified position and dimensions
    public BossAttack(int width, int height, double posX, double posY) {
        this.width = width;
        this.height = height;
        this.posX = posX;
        this.posY = posY;
    }

    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("width", this.width);
        data.put("height", this.height);
        data.put("posX", this.posX);
        data.put("posY", this.posY);
        return data;
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
