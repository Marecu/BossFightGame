package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BossAttackTest {

    BossAttack ba1;

    @BeforeEach
    void runBefore() {
        ba1 = new BossAttack(50, 100, 150, 200);
    }

    @Test
    void testConstructor() {
        assertEquals(50, ba1.getWidth());
        assertEquals(100, ba1.getHeight());
        assertEquals(150, ba1.getX());
        assertEquals(200, ba1.getY());
    }
}
