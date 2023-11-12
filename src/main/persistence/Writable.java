package persistence;

import org.json.JSONObject;

//Represents a class that can be written into a JSON object for the purpose of data persistence
public interface Writable {

    //EFFECTS: returns the object as a JSON object
    JSONObject toJson();
}
