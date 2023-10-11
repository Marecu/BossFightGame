package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Boss1Test {

    Boss1 b1;
    Player p;

    @BeforeEach
    void runBefore() {
        p = new Player(200, 525);
        b1 = new Boss1(600, 500, p);
    }

    @Test
    void testConstructor() {
        assertEquals(600, b1.getX());
        assertEquals(500, b1.getY());
        assertEquals(30, b1.getHP());
        assertEquals(0, b1.getSpeedY());
        assertEquals(1, b1.getFacing());
        assertEquals(10, b1.getAttackTimer());
        assertEquals(p, b1.getPlayer());
        assertEquals(0, b1.getBonusMoveSpeed());
        assertFalse(b1.getCurrentlyAttacking());
        assertFalse(b1.getMovementOverride());
        assertTrue(b1.getBossAttacks().isEmpty());
    }

    @Test
    void testAttack1() {
        b1.attack1(p);
        assertTrue(b1.getMovementOverride());
        assertTrue(b1.getCurrentlyAttacking());
        assertEquals(30, b1.getBonusMoveSpeed());
    }

    @Test
    void testAttack2() {
        b1.attack2(p);
        assertTrue(b1.getCurrentlyAttacking());
        assertEquals(200, b1.getX());
        assertEquals(100, b1.getY());
    }

    @Test
    void testAttack3FacingR() {
        b1.attack3(p);
        assertTrue(b1.getCurrentlyAttacking());
        BossAttack ba = b1.getBossAttacks().get(0);
        assertEquals(800 - (int)b1.getX() - b1.getWidth(), ba.getWidth());
        assertEquals(30, ba.getHeight());
        assertEquals(b1.getX() + b1.getWidth(), ba.getX());
        assertEquals(b1.getY(), ba.getY());
        assertEquals(3, b1.getBeamTimer());
    }

    @Test
    void testAttack3FacingL() {
        b1.setFacing(-1);
        b1.attack3(p);
        assertTrue(b1.getCurrentlyAttacking());
        BossAttack ba = b1.getBossAttacks().get(0);
        assertEquals((int)b1.getX(), ba.getWidth());
        assertEquals(30, ba.getHeight());
        assertEquals(0, ba.getX());
        assertEquals(b1.getY(), ba.getY());
    }

    @Test
    void testHandleLingeringAttacksA1() {
        b1.setLastUsedAttack(1);
        b1.handleLingeringAttacks(p);
        b1.setX(0);
        b1.handleAttack1();
        assertFalse(b1.getMovementOverride());
        assertFalse(b1.getCurrentlyAttacking());
        assertEquals(10, b1.getAttackTimer());
    }

    @Test
    void testHandleLingeringAttacksA2() {
        b1.setLastUsedAttack(1);
        b1.handleLingeringAttacks(p);
        b1.attack2(p);
        b1.handleAttack2();
        assertTrue(b1.getCurrentlyAttacking());
        b1.setY(600 - b1.getHeight());
        b1.handleAttack2();
        assertFalse(b1.getCurrentlyAttacking());
        assertEquals(10, b1.getAttackTimer());
    }

    @Test
    void testHandleLingeringAttacksA3() {
        b1.setLastUsedAttack(1);
        b1.handleLingeringAttacks(p);
        b1.attack3(p);
        b1.handleAttack3();
        assertEquals(2, b1.getBeamTimer());
        assertTrue(b1.getCurrentlyAttacking());
        assertFalse(b1.getBossAttacks().isEmpty());
        b1.handleAttack3();
        assertEquals(1, b1.getBeamTimer());
        assertTrue(b1.getCurrentlyAttacking());
        assertFalse(b1.getBossAttacks().isEmpty());
        b1.handleAttack3();
        assertEquals(0, b1.getBeamTimer());
        assertTrue(b1.getCurrentlyAttacking());
        assertFalse(b1.getBossAttacks().isEmpty());
        b1.handleAttack3();
        assertEquals(0, b1.getBeamTimer());
        assertFalse(b1.getCurrentlyAttacking());
        assertTrue(b1.getBossAttacks().isEmpty());
        assertEquals(10, b1.getAttackTimer());
    }

    @Test
    void testHandleAttack1EdgeL() {
        b1.attack1(p);
        b1.setX(0);
        b1.handleAttack1();
        assertFalse(b1.getMovementOverride());
        assertFalse(b1.getCurrentlyAttacking());
    }

    @Test
    void testHandleAttack1EdgeR() {
        b1.attack1(p);
        b1.setX(800 - b1.getWidth());
        b1.handleAttack1();
        assertFalse(b1.getMovementOverride());
        assertFalse(b1.getCurrentlyAttacking());
    }

    @Test
    void testHandleAttack2() {
        b1.attack2(p);
        b1.handleAttack2();
        assertTrue(b1.getCurrentlyAttacking());
        b1.setY(600 - b1.getHeight());
        b1.handleAttack2();
        assertFalse(b1.getCurrentlyAttacking());
    }

    @Test
    void testHandleAttack3() {
        b1.attack3(p);
        b1.handleAttack3();
        assertEquals(2, b1.getBeamTimer());
        assertTrue(b1.getCurrentlyAttacking());
        assertFalse(b1.getBossAttacks().isEmpty());
        b1.handleAttack3();
        assertEquals(1, b1.getBeamTimer());
        assertTrue(b1.getCurrentlyAttacking());
        assertFalse(b1.getBossAttacks().isEmpty());
        b1.handleAttack3();
        assertEquals(0, b1.getBeamTimer());
        assertTrue(b1.getCurrentlyAttacking());
        assertFalse(b1.getBossAttacks().isEmpty());
        b1.handleAttack3();
        assertEquals(0, b1.getBeamTimer());
        assertFalse(b1.getCurrentlyAttacking());
        assertTrue(b1.getBossAttacks().isEmpty());
    }

}
