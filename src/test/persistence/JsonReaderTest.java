package persistence;

import model.Game;
import model.Player;
import model.Boss1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {

    private static final String FILE_PATH = "./data/testData.json";
    private static final String BAD_FILE_PATH = "./data/asdf.json";

    private JsonReader jr;
    private JsonReader jr2;

    @BeforeEach
    void runBefore() {
        jr = new JsonReader(FILE_PATH);
        jr2 = new JsonReader(BAD_FILE_PATH);
    }

    @Test
    void testSuccessfulRead() {
        try {
            Game g = jr.read();
            //Player
            assertEquals(200, g.getPlayer().getX());
            assertEquals(500, g.getPlayer().getY());
            assertEquals(-60, g.getPlayer().getSpeedY());
            assertEquals(-20, g.getPlayer().getSpeedX());
            assertEquals(4, g.getPlayer().getTotalHits());
            assertEquals(2, g.getPlayer().getHP());
            assertEquals(-1, g.getPlayer().getFacing());
            assertFalse(g.getPlayer().getPlayerAttacks().isEmpty());
            assertFalse(g.getPlayer().getHasJump());
            assertEquals(400, g.getPlayer().getPlayerAttacks().get(0).getX());
            assertFalse(g.getPlayer().getPlayerAttacks().get(0).getMoving());
            //Boss1
            assertEquals(200, g.getBoss().getX());
            assertEquals(300, g.getBoss().getY());
            assertEquals(-10, g.getBoss().getSpeedY());
            assertEquals(40, g.getBoss().getBonusMoveSpeed());
            assertTrue(g.getBoss().getMovementOverride());
            assertEquals(22, g.getBoss().getHP());
            assertEquals(-1, g.getBoss().getFacing());
            assertEquals(5, g.getBoss().getAttackTimer());
            assertFalse(g.getBoss().getBossAttacks().isEmpty());
            assertTrue(g.getBoss().getCurrentlyAttacking());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    @Test
    void testInvalidFilePath() {
        try {
            Game g = jr2.read();
            fail("Should have thrown exception");
        } catch (IOException e) {
            //expected
        }
    }

}
