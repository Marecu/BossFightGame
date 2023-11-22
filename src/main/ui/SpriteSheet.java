package ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

//Represents a sprite sheet - a grid-based collection of all sprites used in the game
public class SpriteSheet {

    private final BufferedImage sheet;

    //Creates a new sprite sheet from the file at the given file path
    public SpriteSheet(String filePath) throws IOException {
        if (BossFight.JAR_COMPILE) {
            InputStream in = getClass().getResourceAsStream(filePath);
            if (in == null) {
                throw new IOException("Unable to locate file");
            }
            this.sheet = ImageIO.read(in);
        } else {
            File file = new File(filePath);
            this.sheet = ImageIO.read(file);
        }
        if (this.sheet == null) {
            throw new IOException("Unable to read file.");
        }
    }

    //EFFECTS: returns a portion of the sprite sheet specified by the coordinates (in px)
    //         and width/height (in px) given
    public BufferedImage splice(int x, int y, int w, int h) {
        return sheet.getSubimage(x, y, w, h);
    }

}
