package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BossTest {

    Boss b1;
    Boss b2;
    Boss b3;
    Player p;

    @BeforeEach
    void runBefore() {
        p = new Player(100, 100);
        b1 = new Boss1(200, 300, p); //uses Boss1 to test since Boss cannot be constructed as it is abstract
        b2 = new Boss1(400, 400, p);
        b3 = new Boss1(100, 200, p);
    }

    @Test
    void testConstructor() {
        assertEquals(200, b1.getX());
        assertEquals(300, b1.getY());
        assertEquals(p, b1.getPlayer());
        assertEquals(30, b1.getHP());
    }

    @Test
    void testMove() {
        b1.move(p);
        assertEquals(190, b1.getX());
        b1.setBonusMoveSpeed(20);
        b1.move(p);
        assertEquals(160, b1.getX());
        b1.setX(25);
        b1.move(p);
        assertEquals(55, b1.getX());
        b1.setMovementOverride(true);
        b1.setX(200);
        b1.move(p);
        assertEquals(230, b1.getX());
    }

    @Test
    void testHandleScreenBoundary() {
        b1.setX(-10);
        b1.handleScreenBoundary();
        assertEquals(0, b1.getX());
        b1.setX(Game.WIDTH + 100);
        b1.handleScreenBoundary();
        assertEquals(Game.WIDTH - b1.getWidth(), b1.getX());
        b1.setY(Game.HEIGHT + 100);
        b1.handleScreenBoundary();
        assertEquals(Game.HEIGHT - b1.getHeight(), b1.getY());
        b1.setY(-10);
        b1.handleScreenBoundary();
        assertEquals(0, b1.getY());
    }

    @Test
    void testAccelerateY() {
        b1.accelerateY(30);
        assertEquals(30, b1.getSpeedY());
        b1.accelerateY(-50);
        assertEquals(-20, b1.getSpeedY());
    }

    @Test
    void testOnGround() {
        assertFalse(b1.onGround());
        b1.setY(Game.HEIGHT - b1.getHeight());
        assertTrue(b1.onGround());
    }

    @Test
    void testHandleAttackCycle() {
        b1.setAttackTimer(3);
        b1.handleAttackCycle();
        assertEquals(2, b1.getAttackTimer());
        b1.handleAttackCycle();
        assertEquals(1, b1.getAttackTimer());
        b1.handleAttackCycle();
        assertEquals(0, b1.getAttackTimer());
        b1.handleAttackCycle();
        assertEquals(10, b1.getAttackTimer());
    }

    @Test
    void testGenerateAttack() {
        b1.generateAttack();
        assertTrue(b1.getLastUsedAttack() <= 3 && b1.getLastUsedAttack() >= 1);
    }

    @Test
    void testAttack() {
        b1.attack(1);
        assertTrue(b1.getMovementOverride());
        assertTrue(b1.getCurrentlyAttacking());
        assertEquals(30, b1.getBonusMoveSpeed());
        b2.attack(2);
        assertTrue(b2.getCurrentlyAttacking());
        assertEquals(100, b2.getX());
        assertEquals(100, b2.getY());
        b3.attack(3);
        assertTrue(b3.getCurrentlyAttacking());
        BossAttack ba = b3.getBossAttacks().get(0);
        assertEquals(Game.WIDTH - (int)b3.getX() - b3.getWidth(), ba.getWidth());
        assertEquals(30, ba.getHeight());
        assertEquals(b3.getX() + b3.getWidth(), ba.getX());
        assertEquals(b3.getY(), ba.getY());
        b3.attack(4);
        assertTrue(b3.getCurrentlyAttacking());
        ba = b3.getBossAttacks().get(0);
        assertEquals(Game.WIDTH - (int)b3.getX() - b3.getWidth(), ba.getWidth());
        assertEquals(30, ba.getHeight());
        assertEquals(b3.getX() + b3.getWidth(), ba.getX());
        assertEquals(b3.getY(), ba.getY());
    }

    @Test
    void testTakeDamage() {
        b1.takeDamage(1);
        assertEquals(29, b1.getHP());
        b1.takeDamage(5);
        assertEquals(24, b1.getHP());
    }

}
