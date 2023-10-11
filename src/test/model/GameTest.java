package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game g1;

    @BeforeEach
    void runBefore() {
        g1 = new Game();
    }

    @Test
    void testConstructor() {
        assertEquals(200, g1.getPlayer().getX());
        assertEquals(525, g1.getPlayer().getY());
        assertEquals(600, g1.getBoss().getX());
        assertEquals(500, g1.getBoss().getY());
    }

    @Test
    void testUpdate() {
        g1.getPlayer().accelerateX(50);
        g1.getPlayer().accelerateY(-20);
        g1.getBoss().accelerateY(-30);
        g1.update("attack");
        assertEquals(250, g1.getPlayer().getX());
        assertEquals(505, g1.getPlayer().getY());
        assertEquals(470, g1.getBoss().getY());
    }

    @Test
    void testHandleUserInput() {
        g1.handleUserInput("xyz");
        assertEquals(0, g1.getPlayer().getSpeedX());
        assertEquals(0, g1.getPlayer().getSpeedY());
        assertEquals(0, g1.getPlayer().getPlayerAttacks().size());
        g1.handleUserInput("a");
        assertEquals(-5, g1.getPlayer().getSpeedX());
        g1.handleUserInput("d");
        assertEquals(5, g1.getPlayer().getSpeedX());
        g1.handleUserInput("jump");
        assertEquals(g1.getPlayer().getJumpStrength(), g1.getPlayer().getSpeedY());
        g1.handleUserInput("attack");
        assertEquals(1, g1.getPlayer().getPlayerAttacks().size());
        for (int i = 0; i < 3; i++) {
            g1.getPlayer().incrementAttackCounter();
        }
        g1.handleUserInput("spell");
        assertEquals(2, g1.getPlayer().getPlayerAttacks().size());
        g1.handleUserInput("abcdef");
        assertEquals(5, g1.getPlayer().getSpeedX());
        assertEquals(g1.getPlayer().getJumpStrength(), g1.getPlayer().getSpeedY());
        assertEquals(2, g1.getPlayer().getPlayerAttacks().size());
    }

    @Test
    void testApplyGravity() {
        g1.applyGravity();
        assertEquals(0, g1.getPlayer().getSpeedY());
        assertEquals(0, g1.getBoss().getSpeedY());
        g1.getBoss().accelerateY(50);
        g1.applyGravity();
        assertEquals(0, g1.getBoss().getSpeedY());
        g1.getBoss().setY(100);
        g1.getPlayer().jump();
        g1.getPlayer().move();
        g1.applyGravity();
        assertEquals(g1.getPlayer().getJumpStrength() + g1.getGravitationalConstant(),
                g1.getPlayer().getSpeedY());
        assertEquals(g1.getGravitationalConstant(), g1.getBoss().getSpeedY());
        g1.getPlayer().accelerateY(1000);
        for (int i = 0; i < 2; i++) {
            g1.getPlayer().move();
        }
        g1.applyGravity();
        assertEquals(0, g1.getPlayer().getSpeedY());
        assertTrue(g1.getPlayer().getHasJump());
    }

    @Test
    void testCheckCollisions() {
        g1.getBoss().setX(220);
        g1.getBoss().setY(500);
        g1.getBoss().addBossAttack(new BossAttack(50,50, 160, 550));
        g1.getPlayer().attack();
        assertEquals(235, g1.getPlayer().getPlayerAttacks().get(0).getX());
        assertEquals(552.5, g1.getPlayer().getPlayerAttacks().get(0).getY());
        g1.checkCollisions();
        assertEquals(1, g1.getPlayer().getHP());
        assertEquals(29, g1.getBoss().getHP());
    }

    @Test
    void testCheckPlayerAttacks() {
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(400, 400, 475, 500);
        assertEquals(30, g1.getBoss().getHP()); //No hitbox overlap
        g1.checkPlayerAttacks(250, 475, 325, 572.5);
        assertEquals(29, g1.getBoss().getHP()); //Overlap bottom left of boss
        g1.checkPlayerAttacks(250, 572.5, 325, 675);
        assertEquals(28, g1.getBoss().getHP()); //Overlap top left of boss
        g1.checkPlayerAttacks(175, 572.5, 250, 675);
        assertEquals(27, g1.getBoss().getHP()); //Overlap top right of boss
        g1.checkPlayerAttacks(175, 475, 250, 572.5);
        assertEquals(26, g1.getBoss().getHP()); //Overlap bottom right of boss
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(175, 475, 250, 572.5);
        assertEquals(24, g1.getBoss().getHP()); //Test multiple attacks
        assertEquals(6, g1.getPlayer().getTotalHits());
        g1.getPlayer().spellAttack();
        assertEquals(4, g1.getPlayer().getPlayerAttacks().size());
        g1.checkPlayerAttacks(175, 500, 250, 600);
        assertEquals(20, g1.getBoss().getHP());
        assertEquals(2, g1.getPlayer().getTotalHits());
        //
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 2000, 2000, 1, false, 1));
        g1.checkPlayerAttacks(1950, 2025, 2025, 2030); //Enclose left side of BA in player
        assertEquals(19, g1.getBoss().getHP());
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 2000, 2000, 1, false, 1));
        g1.checkPlayerAttacks(2025, 2025, 2075, 2030); //Enclose right side of BA in player
        assertEquals(17, g1.getBoss().getHP());
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 2000, 2000, 1, false, 1));
        g1.checkPlayerAttacks(2025, 1950, 2030, 2025); //Enclose bottom side of BA in player
        assertEquals(14, g1.getBoss().getHP());
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 2000, 2000, 1, false, 1));
        g1.checkPlayerAttacks(2025, 2025, 2030, 2075); //Enclose top side of BA in player
        assertEquals(10, g1.getBoss().getHP());
    }

    @Test
    void testCheckBossAttacks() {
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 300, 300));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(3, g1.getPlayer().getHP()); //No hitbox overlap
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 160, 550));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(2, g1.getPlayer().getHP()); //Overlap bottom left of player
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 160, 485));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(0, g1.getPlayer().getHP()); //Overlap top left of player
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 220, 485));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(-3, g1.getPlayer().getHP()); //Overlap top right of player
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 220, 550));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(-7, g1.getPlayer().getHP()); //Overlap bot right of player
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 1000, 1000));
        g1.checkBossAttacks(975, 975, 1075, 1075); //Enclose boss attack in player
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 1000, 1000));
        assertEquals(-8, g1.getPlayer().getHP());
        g1.checkBossAttacks(1010, 1010, 1030, 1030); //Enclose player in boss attack
        assertEquals(-10, g1.getPlayer().getHP());
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 2000, 2000));
        g1.checkBossAttacks(1950, 2025, 2025, 2030); //Enclose left side of BA in player
        assertEquals(-11, g1.getPlayer().getHP());
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 2000, 2000));
        g1.checkBossAttacks(2025, 2025, 2075, 2030); //Enclose right side of BA in player
        assertEquals(-13, g1.getPlayer().getHP());
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 2000, 2000));
        g1.checkBossAttacks(2025, 1950, 2030, 2025); //Enclose bottom side of BA in player
        assertEquals(-16, g1.getPlayer().getHP());
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 2000, 2000));
        g1.checkBossAttacks(2025, 2025, 2030, 2075); //Enclose top side of BA in player
        assertEquals(-20, g1.getPlayer().getHP());
    }

    @Test
    void testCheckCollisionWithBoss() {
        g1.checkCollisionWithBoss(300, 500, 375, 600, 200, 525, 235, 600);
        assertEquals(3, g1.getPlayer().getHP()); //No hitbox overlap
        g1.checkCollisionWithBoss(220, 450, 295, 550, 200, 525, 235, 600);
        assertEquals(2, g1.getPlayer().getHP()); //Overlap bottom left of boss
        g1.checkCollisionWithBoss(220, 550, 295, 650, 200, 525, 235, 600);
        assertEquals(1, g1.getPlayer().getHP()); //Overlap top left of boss
        g1.checkCollisionWithBoss(145, 550, 220, 650, 200, 525, 235, 600);
        assertEquals(0, g1.getPlayer().getHP()); //Overlap top right of boss
        g1.checkCollisionWithBoss(145, 450, 220, 550, 200, 525, 235, 600);
        assertEquals(-1, g1.getPlayer().getHP()); //Overlap bottom right of boss
        g1.checkCollisionWithBoss(1950, 2025, 2025, 2030, 2000, 2000, 2050, 2050);
        assertEquals(-2, g1.getPlayer().getHP()); //Overlap left side of boss
        g1.checkCollisionWithBoss(1950, 2025, 2025, 2030, 2000, 2000, 2050, 2050);
        assertEquals(-3, g1.getPlayer().getHP()); //Overlap right side of boss
        g1.checkCollisionWithBoss(1950, 2025, 2025, 2030, 2000, 2000, 2050, 2050);
        assertEquals(-4, g1.getPlayer().getHP()); //Overlap bottom side of boss
        g1.checkCollisionWithBoss(1950, 2025, 2025, 2030, 2000, 2000, 2050, 2050);
        assertEquals(-5, g1.getPlayer().getHP()); //Overlap top side of boss
    }

    @Test
    void testReduceAttackLifespan() {
        g1.getPlayer().attack();
        g1.getPlayer().attack();
        PlayerAttack pa1 = g1.getPlayer().getPlayerAttacks().get(0);
        PlayerAttack pa2 = g1.getPlayer().getPlayerAttacks().get(1);
        for (int i = 0; i < 3; i++) {
            g1.getPlayer().incrementAttackCounter();
        }
        g1.getPlayer().spellAttack();
        PlayerAttack pa3 = g1.getPlayer().getPlayerAttacks().get(2);
        int startingLifespan = pa1.getLifespan();
        int startingLifespan2 = pa3.getLifespan();
        g1.reduceAttackLifespan();
        assertEquals(startingLifespan - 1, pa1.getLifespan());
        assertEquals(startingLifespan - 1, pa2.getLifespan());
        assertEquals(startingLifespan2 - 1, pa3.getLifespan());
        g1.reduceAttackLifespan();
        assertEquals(startingLifespan2 - 2, pa3.getLifespan());
    }

    @Test
    void testIsGameOver() {
        assertFalse(g1.isGameOver());
        g1.getPlayer().takeDamage(g1.getPlayer().getHP());
        assertTrue(g1.isGameOver());
        g1.getPlayer().takeDamage(-1);
        g1.getBoss().takeDamage(g1.getBoss().getHP());
        assertTrue(g1.isGameOver());
    }

}
