package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

//Represents a reader than can read stored data into a game state
//CITATION: largely based on the JSON serialization demo given as an example in class
public class JsonReader {
    private String filePath;

    //EFFECTS: creates a new JsonReader
    public JsonReader(String filePath) {
        this.filePath = filePath;
    }

    //EFFECTS: parses the file at the given path and returns the appropriate game state,
    //         throws IOException if file cannot be read
    public Game read() throws IOException {
        String json = parseFile(filePath);
        JSONObject jo = new JSONObject(json);
        return parseGame(jo);
    }

    //EFFECTS: returns the file at the given path as a string
    private String parseFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> sb.append(s));
        }
        return sb.toString();
    }

    //EFFECTS: returns the JSONObject as the appropriate game state created from it
    private Game parseGame(JSONObject jo) {
        Player player = parsePlayer(jo.getJSONObject("Player"));
        Boss boss1 = parseBoss1(jo.getJSONObject("Boss1"), player);
        return new Game(player, boss1);
    }

    //EFFECTS: returns a player parsed from the JSON object
    private Player parsePlayer(JSONObject jo) {
        double posX = jo.getDouble("posX");
        double posY = jo.getDouble("posY");
        double speedX = jo.getDouble("speedX");
        double speedY = jo.getDouble("speedY");
        int facing = jo.getInt("facing");
        boolean hasJump = jo.getBoolean("hasJump");
        int hp = jo.getInt("hp");
        int totalHits = jo.getInt("totalHits");
        List<PlayerAttack> playerAttacks = parsePlayerAttacks(jo.getJSONArray("playerAttacks"));
        boolean invincible = jo.getBoolean("invincible");
        int iframes = jo.getInt("iframes");
        boolean canAttack = jo.getBoolean("canAttack");
        int lockoutTicks = jo.getInt("lockoutTicks");
        boolean canSpell = jo.getBoolean("canSpell");
        int spellLockoutTicks = jo.getInt("spellLockoutTicks");
        return new Player(posX, posY, speedX, speedY, facing, hasJump, hp, totalHits, playerAttacks, invincible,
                iframes, canAttack, lockoutTicks, canSpell, spellLockoutTicks);
    }

    //EFFECTS: returns a list of player attacks parsed from a JSON array
    private List<PlayerAttack> parsePlayerAttacks(JSONArray ja) {
        List<PlayerAttack> playerAttacks = new ArrayList<>();
        for (Object json : ja) {
            JSONObject jo = (JSONObject) json;
            int width = jo.getInt("width");
            int height = jo.getInt("height");
            double posX = jo.getDouble("posX");
            double posY = jo.getDouble("posY");
            int lifespan = jo.getInt("lifespan");
            boolean moving = jo.getBoolean("moving");
            int facing = jo.getInt("facing");
            boolean hasHit = jo.getBoolean("hasHit");
            playerAttacks.add(new PlayerAttack(width, height, posX, posY, lifespan, moving, facing, hasHit));
        }
        return playerAttacks;
    }

    //EFFECTS: returns a Boss1 parsed from the given JSON Object that targets the given player
    private Boss parseBoss1(JSONObject jo, Player p) {
        double posX = jo.getDouble("posX");
        double posY = jo.getDouble("posY");
        int hp = jo.getInt("hp");
        double speedY = jo.getDouble("speedY");
        int facing = jo.getInt("facing");
        int attackTimer = jo.getInt("attackTimer");
        int bonusMoveSpeed = jo.getInt("bonusMoveSpeed");
        boolean currentlyAttacking = jo.getBoolean("currentlyAttacking");
        boolean movementOverride = jo.getBoolean("movementOverride");
        List<BossAttack> bossAttacks = parseBossAttacks(jo.getJSONArray("bossAttacks"));
        int lastUsedAttack = jo.getInt("lastUsedAttack");
        return new Boss1(posX, posY, p, hp, speedY, facing, attackTimer, bonusMoveSpeed,
                currentlyAttacking, movementOverride, bossAttacks, lastUsedAttack);
    }

    //EFFECTS: returns a list of boss attacks parsed from the given JSON array
    private List<BossAttack> parseBossAttacks(JSONArray ja) {
        List<BossAttack> bossAttacks = new ArrayList<>();
        for (Object json : ja) {
            JSONObject jo = (JSONObject) json;
            int width = jo.getInt("width");
            int height = jo.getInt("height");
            double posX = jo.getDouble("posX");
            double posY = jo.getDouble("posY");
            bossAttacks.add(new BossAttack(width, height, posX, posY));
        }
        return bossAttacks;
    }
}
