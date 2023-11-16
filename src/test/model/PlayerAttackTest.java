package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerAttackTest {

    PlayerAttack pa1;
    PlayerAttack pa2;

    @BeforeEach
    void runBefore() {
        pa1 = new PlayerAttack(20, 30, 100, 75, 1, false, 1, false);
        pa2 = new PlayerAttack(20, 30, 100, 75, 1, true, 1, false);
    }

    @Test
    void testConstructor() {
        assertEquals(20, pa1.getWidth());
        assertEquals(30, pa1.getHeight());
        assertEquals(100, pa1.getX());
        assertEquals(75, pa1.getY());
        assertEquals(1, pa1.getLifespan());
        assertFalse(pa1.getMoving());
        assertEquals(1, pa1.getFacing());
    }

    @Test
    void testMove() {
        pa2.move(25, 100);
        assertEquals(125, pa2.getX());
        assertEquals(175, pa2.getY());
    }

    @Test
    void testSubtractLifespan() {
        pa1.subtractLifespan();
        assertEquals(0, pa1.getLifespan());
    }
}
