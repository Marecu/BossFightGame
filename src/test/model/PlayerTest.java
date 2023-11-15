package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player p1;
    Player p2;
    Player p3;
    Player p4;
    Player p5;
    Player p6;
    Player p7;
    Player p8;

    @BeforeEach
    void runBefore() {
        p1 = new Player(5, 10);
        p2 = new Player( 300, 200);
        p3 = new Player(689, 400);
        p4 = new Player(30, 0);
        p5 = new Player(400, 150);
        p6 = new Player(400, Game.HEIGHT - p2.getHeight() - Player.HITBOX_INSET);
        p7 = new Player(300, 510);
        p8 = new Player(Game.WIDTH - p2.getWidth() - 10, Game.HEIGHT - p2.getHeight() - 10);
    }

    @Test
    void testConstructor() {
        assertEquals(5, p1.getX());
        assertEquals(10, p1.getY());
        assertEquals(0, p1.getSpeedX());
        assertEquals(0, p1.getSpeedY());
        assertEquals(1, p1.getFacing());
        assertEquals(Player.STARTING_HP, p1.getHP());
        assertEquals(0, p1.getTotalHits());
        assertTrue(p1.getPlayerAttacks().isEmpty());
        assertFalse(p1.getInvincible());
        assertEquals(0, p1.getIframes());
        assertTrue(p1.getCanAttack());
        assertEquals(0, p1.getLockoutTicks());
        assertTrue(p1.getCanSpell());
        assertEquals(0, p1.getSpellLockoutTicks());
    }

    @Test
    void testAccelerateX() {
        p1.accelerateX(1.5);
        assertEquals(1.5, p1.getSpeedX());
        p1.accelerateX(-2.25);
        assertEquals(-0.75, p1.getSpeedX());
    }

    @Test
    void testAccelerateY() {
        p1.accelerateY(1.5);
        assertEquals(1.5, p1.getSpeedY());
        p1.accelerateY(-2.25);
        assertEquals(-0.75, p1.getSpeedY());
    }

    @Test
    void testMove() {
        p8.accelerateX(7);
        p8.move();
        assertEquals(Game.WIDTH - p8.getWidth() - 3, p8.getX());
        p8.move();
        assertEquals(Game.WIDTH - p8.getWidth(), p8.getX());
        p4.accelerateX(-50);
        p4.move();
        assertEquals(0, p4.getX());
        p5.accelerateY(-50);
        p5.move();
        assertEquals(100, p5.getY());
        p5.move();
        assertEquals(p5.getHeight(), p5.getY());
        p8.accelerateY(6);
        p8.move();
        assertEquals(Game.HEIGHT - p8.getHeight() - 4, p8.getY());
        p8.move();
        assertEquals(Game.HEIGHT - p8.getHeight() - Player.HITBOX_INSET, p8.getY());
    }

    @Test
    void testMoveL() {
        p2.moveL();
        assertEquals(-1 * Player.ACCEL_STRENGTH, p2.getSpeedX());
        p2.accelerateX(p2.getSpeedX() * -2);
        p2.moveL();
        assertEquals(-1 * Player.ACCEL_STRENGTH, p2.getSpeedX());
    }

    @Test
    void testMoveR() {
        p2.moveR();
        assertEquals(Player.ACCEL_STRENGTH, p2.getSpeedX());
        p2.accelerateX(p2.getSpeedX() * -1);
        p2.moveR();
        assertEquals(Player.ACCEL_STRENGTH, p2.getSpeedX());
    }

    @Test
    void testJump() {
        p1.jump();
        assertEquals(Player.JUMP_STRENGTH, p1.getSpeedY());
        p1.jump();
        assertEquals(Player.JUMP_STRENGTH, p1.getSpeedY());
    }

    @Test
    void testAttackL() {
        p1.moveL();
        p1.attack();
        assertEquals(1, p1.getPlayerAttacks().size());
        PlayerAttack attack = p1.getPlayerAttacks().get(0);
        assertEquals((int)p1.getX() - attack.getWidth(), attack.getX());
        p1.attack();
        assertEquals(1, p1.getPlayerAttacks().size());
    }

    @Test
    void testAttackR() {
        p1.attack();
        assertEquals(1, p1.getPlayerAttacks().size());
        PlayerAttack attack = p1.getPlayerAttacks().get(0);
        assertEquals((int)p1.getX() + p1.getWidth(), attack.getX());
        p1.attack();
        assertEquals(1, p1.getPlayerAttacks().size());
    }

    @Test
    void testSpellAttack() {
        for (int i = 0; i < Player.SPELL_REQUIRED_HITS; i++) {
            p1.incrementAttackCounter();
        }
        p1.spellAttack();
        resetAttacks(p1);
        assertEquals(1, p1.getPlayerAttacks().size());
        p1.moveL();
        for (int i = 0; i < (Player.SPELL_REQUIRED_HITS * 2) + 1; i++) {
            p1.incrementAttackCounter();
        }
        p1.spellAttack();
        assertEquals(2, p1.getPlayerAttacks().size());
        resetAttacks(p1);
        p1.spellAttack();
        assertEquals(3, p1.getPlayerAttacks().size());
        resetAttacks(p1);
        p1.spellAttack();
        assertEquals(3, p1.getPlayerAttacks().size());
        p1.spellAttack();
        assertEquals(3, p1.getPlayerAttacks().size());
    }

    @Test
    void testMoveProjectiles() {
        for (int i = 0; i < 6; i++) {
            p1.incrementAttackCounter();
        }
        p1.spellAttack();
        resetAttacks(p1);
        p1.spellAttack();
        p1.moveProjectiles();
        assertEquals(p1.getX() + p1.getWidth() + Player.MISSILE_SPEED, p1.getPlayerAttacks().get(0).getX());
        assertEquals(p1.getX() + p1.getWidth() + Player.MISSILE_SPEED, p1.getPlayerAttacks().get(1).getX());
        p2.moveL();
        for (int i = 0; i < 6; i++) {
            p2.incrementAttackCounter();
        }
        p2.spellAttack();
        resetAttacks(p2);
        p2.spellAttack();
        p2.moveProjectiles();
        assertEquals(250, p2.getPlayerAttacks().get(0).getX());
        assertEquals(250, p2.getPlayerAttacks().get(1).getX());
    }

    @Test
    void testRemoveProjectile() {
        p1.attack();
        resetAttacks(p1);
        p1.attack();
        assertEquals(2, p1.getPlayerAttacks().size());
        p1.removeAttack(p1.getPlayerAttacks().get(0));
        assertEquals(1, p1.getPlayerAttacks().size());
        p1.removeAttack(p1.getPlayerAttacks().get(0));
        assertTrue(p1.getPlayerAttacks().isEmpty());
    }

    private void resetAttacks(Player p) {
        for (int i = 0; i <= Player.MAX_LOCKOUT_TICKS; i++) {
            p.tickAttackLockouts();
        }
    }

    @Test
    void testOnGround() {
        assertTrue(p6.onGround());
        assertFalse(p4.onGround());
    }

    @Test
    void testTakeDamage() {
        assertEquals(Player.STARTING_HP, p1.getHP());
        p1.takeDamage(1);
        assertEquals(Player.STARTING_HP - 1, p1.getHP());
        for (int i = 0; i <= Player.MAX_IFRAMES; i++) {
            p1.tickIFrames();
        }
        p1.takeDamage(Player.STARTING_HP - 1);
        assertEquals(0, p1.getHP());
    }

    @Test
    void testIncrementAttackCounter() {
        p1.incrementAttackCounter();
        assertEquals(1, p1.getTotalHits());
        p1.incrementAttackCounter();
        assertEquals(2, p1.getTotalHits());
    }
}