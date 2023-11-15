package ui;

import java.awt.image.BufferedImage;

//Represents a sprite
public class Sprite {

    private final BufferedImage sprite;

    //Creates a new sprite with a BufferedImage containing the sprite
    public Sprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

}
