package persistence;

import model.Game;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

//Represents a JSON writer that can write a game state into a stored file
//CITATION: largely based on the JSON serialization demo given as an example in class
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String file;

    //EFFECTS: creates a new JsonWriter with a given file path
    public JsonWriter(String file) {
        this.file = file;
    }

    //EFFECTS: opens the file at the given file path, throws FileNotFoundException if no file is found
    //MODIFIES: this
    public void open() throws FileNotFoundException {
        this.writer = new PrintWriter(file);
    }

    //EFFECTS: writes the JSON data pulled from the game into the file
    //MODIFIES: this
    public void write(Game g) {
        JSONObject json = g.toJson();
        save(json.toString(TAB));
    }

    //EFFECTS: closes the writer
    //MODIFIES: this
    public void close() {
        writer.close();
    }

    //EFFECTS: writes the given JSON string to the file
    //MODIFIES: this
    public void save(String json) {
        writer.print(json);
    }

}
