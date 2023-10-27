package persistence;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {

    JsonWriter jw;
    JsonWriter jw2;
    List<PlayerAttack> pAttacks;
    List<BossAttack> bAttacks;
    Player p;
    Boss1 b;
    Game g;
    JsonReader jr;

    @BeforeEach
    void runBefore() {
        pAttacks = new ArrayList<>();
        pAttacks.add(new PlayerAttack(20, 30, 400, 500, 3, true, 1));
        pAttacks.add(new PlayerAttack(30, 40, 200, 100, 6, true, -1));
        p = new Player(200, 300, 10, -20, -1, true, 2, 4, pAttacks);
        bAttacks = new ArrayList<>();
        bAttacks.add(new BossAttack(50, 40, 200, 100));
        b = new Boss1(400, 500, p, 20, -40, 1, 5, 40, true, true, bAttacks, 2);
        g = new Game(p, b);
        jw = new JsonWriter("./data/testDataWrite.json");
        jw2 = new JsonWriter("./dsjfhsdgf/ffd.exe");
        jr = new JsonReader("./data/testDataWrite.json");
    }

    @Test
    void testInvalidFile() {
        try {
            jw2.open();
            fail("Expected exception to be thrown");
        } catch (IOException e) {
            //expected
        }
    }

    @Test
    void testWriteSuccessful() {
        try {
            jw.open();
            jw.write(g);
            jw.close();

            Game readGame = jr.read();
            assertEquals(200, readGame.getPlayer().getX());
            assertEquals(300, readGame.getPlayer().getY());
            assertEquals(10, readGame.getPlayer().getSpeedX());
            assertEquals(-20, readGame.getPlayer().getSpeedY());
            assertEquals(2, readGame.getPlayer().getHP());
            assertEquals(400, readGame.getPlayer().getPlayerAttacks().get(0).getX());
            assertEquals(200, readGame.getPlayer().getPlayerAttacks().get(1).getX());

            assertEquals(400, readGame.getBoss().getX());
            assertEquals(500, readGame.getBoss().getY());
            assertTrue(readGame.getBoss().getCurrentlyAttacking());
            assertTrue(readGame.getBoss().getMovementOverride());
            assertEquals(5, readGame.getBoss().getAttackTimer());
            assertEquals(200, readGame.getBoss().getBossAttacks().get(0).getX());
            assertEquals(100, readGame.getBoss().getBossAttacks().get(0).getY());
        } catch (IOException e) {
            fail("Unexpected exception thrown");
        }
    }
}
