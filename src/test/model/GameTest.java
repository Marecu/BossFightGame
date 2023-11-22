package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game g1;
    Game g2;

    @BeforeEach
    void runBefore() {
        g1 = new Game();
        g2 = new Game();
    }

    @Test
    void testConstructor() {
        assertEquals(Game.PLAYER_START_POS_X, g1.getPlayer().getX());
        assertEquals(Game.PLAYER_START_POS_Y, g1.getPlayer().getY());
        assertEquals(Game.BOSS_START_POS_X, g1.getBoss().getX());
        assertEquals(Game.BOSS_START_POS_Y, g1.getBoss().getY());
    }

    @Test
    void testUpdate() {
        Set keys = new HashSet<>();
        keys.add(KeyEvent.VK_ENTER);
        g1.getPlayer().accelerateX(5);
        g1.getPlayer().accelerateY(-20);
        g1.getBoss().accelerateY(-30);
        g1.update(keys);
        assertEquals(Game.PLAYER_START_POS_X + 5, g1.getPlayer().getX());
        assertEquals(Game.PLAYER_START_POS_Y - 20, g1.getPlayer().getY());
        assertEquals(Game.BOSS_START_POS_Y - 30, g1.getBoss().getY());
    }

    @Test
    void testHandleUserInput() {
        g1.handleUserInput(KeyEvent.VK_A);
        assertEquals(-1 * (Player.ACCEL_STRENGTH + Player.TURN_AROUND_SPEED), g1.getPlayer().getSpeedX());
        g1.handleUserInput(KeyEvent.VK_D);
        assertEquals(Player.ACCEL_STRENGTH + Player.TURN_AROUND_SPEED, g1.getPlayer().getSpeedX());
        g1.handleUserInput(KeyEvent.VK_SPACE);
        assertEquals(Player.JUMP_STRENGTH, g1.getPlayer().getSpeedY());
        g2.handleUserInput(KeyEvent.VK_W);
        assertEquals(Player.JUMP_STRENGTH, g2.getPlayer().getSpeedY());
        g1.handleUserInput(KeyEvent.VK_ENTER);
        assertEquals(1, g1.getPlayer().getPlayerAttacks().size());
        for (int i = 0; i < 3; i++) {
            g1.getPlayer().incrementAttackCounter();
        }
        g1.handleUserInput(KeyEvent.VK_E);
        assertEquals(2, g1.getPlayer().getPlayerAttacks().size());
        assertTrue(g1.getPlayer().getPlayerAttacks().get(1).getMoving());
        g1.handleUserInput(KeyEvent.VK_ESCAPE);
        assertTrue(g1.getPause());
        g1.handleUserInput(KeyEvent.VK_T);
        assertEquals(Player.ACCEL_STRENGTH + Player.TURN_AROUND_SPEED, g1.getPlayer().getSpeedX());
        assertEquals(Player.JUMP_STRENGTH, g1.getPlayer().getSpeedY());
        assertEquals(2, g1.getPlayer().getPlayerAttacks().size());
        assertTrue(g1.getPause());
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
        g1.getBoss().setX((int) (Game.PLAYER_START_POS_X + Player.PLAYER_WIDTH / 2));
        g1.getBoss().setY((int) (Game.PLAYER_START_POS_Y + Player.PLAYER_HEIGHT / 2));
        g1.checkCollisions();
        assertEquals(Player.STARTING_HP - 1, g1.getPlayer().getHP());
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50,50, Game.PLAYER_START_POS_X + Player.PLAYER_WIDTH / 2, Game.PLAYER_START_POS_Y + Player.PLAYER_HEIGHT / 2));
        g1.getPlayer().attack();
        assertEquals(Game.PLAYER_START_POS_X + Player.PLAYER_WIDTH, g1.getPlayer().getPlayerAttacks().get(0).getX());
        assertEquals(Game.PLAYER_START_POS_Y + (Player.PLAYER_HEIGHT / 2) - (Player.ATTACK_HEIGHT / 2), g1.getPlayer().getPlayerAttacks().get(0).getY());
        g1.checkCollisions();
        assertEquals(Player.STARTING_HP - 2, g1.getPlayer().getHP());
        assertEquals(Boss.STARTING_HP - 1, g1.getBoss().getHP());
    }

    @Test
    void testCheckPlayerAttacks() {
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(0, 0, 1, 1);
        assertEquals(Boss.STARTING_HP, g1.getBoss().getHP()); //No hitbox overlap
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X + 1, Game.HEIGHT - Player.PLAYER_HEIGHT - Boss.HEIGHT / 2, Game.PLAYER_START_POS_X + 1000, Game.HEIGHT - Player.PLAYER_HEIGHT / 2);
        assertEquals(Boss.STARTING_HP - 1, g1.getBoss().getHP()); //Overlap bottom left of boss
        resetAttackLockout();
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X + 1, Game.HEIGHT - Player.PLAYER_HEIGHT / 2, Game.PLAYER_START_POS_X + 1000, Game.HEIGHT + Boss.HEIGHT / 2);
        assertEquals(Boss.STARTING_HP - 2, g1.getBoss().getHP()); //Overlap top left of boss
        resetAttackLockout();
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X - 25, Game.HEIGHT - Player.PLAYER_HEIGHT / 2, Game.PLAYER_START_POS_X + 50, Game.HEIGHT + Boss.HEIGHT / 2);
        assertEquals(Boss.STARTING_HP - 3, g1.getBoss().getHP()); //Overlap top right of boss
        resetAttackLockout();
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X - 25, Game.HEIGHT - Player.PLAYER_HEIGHT - Boss.HEIGHT / 2, Game.PLAYER_START_POS_X + 50, Game.HEIGHT - Player.PLAYER_HEIGHT / 2);
        assertEquals(Boss.STARTING_HP - 4, g1.getBoss().getHP()); //Overlap bottom right of boss
        resetAttackLockout();
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X - 25, Game.HEIGHT - Player.PLAYER_HEIGHT / 2, Game.PLAYER_START_POS_X + 50, Game.HEIGHT + Boss.HEIGHT / 2);
        assertEquals(Boss.STARTING_HP - 5, g1.getBoss().getHP()); //Test multiple attacks
        g1.getPlayer().incrementAttackCounter();
        assertEquals(6, g1.getPlayer().getTotalHits());
        g1.getPlayer().spellAttack();
        resetAttackLockout();
        g1.getPlayer().spellAttack();
        assertEquals(7, g1.getPlayer().getPlayerAttacks().size());
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X - 25, Game.HEIGHT - Boss.HEIGHT, Game.PLAYER_START_POS_X + 50, Game.HEIGHT);
        assertEquals(Boss.STARTING_HP - 9, g1.getBoss().getHP());
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 2000, 2000, 1, false, 1, false));
        g1.checkPlayerAttacks(1950, 2025, 2025, 2030); //Enclose left side of PA in boss
        assertEquals(Boss.STARTING_HP - 10, g1.getBoss().getHP());
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 2000, 2000, 1, false, 1, false));
        g1.checkPlayerAttacks(2025, 2025, 2075, 2030); //Enclose right side of PA in boss
        assertEquals(Boss.STARTING_HP - 11, g1.getBoss().getHP());
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 2000, 2000, 1, false, 1, false));
        g1.checkPlayerAttacks(2025, 1950, 2030, 2025); //Enclose bottom side of PA in boss
        assertEquals(Boss.STARTING_HP - 12, g1.getBoss().getHP());
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 2000, 2000, 1, false, 1, false));
        g1.checkPlayerAttacks(2025, 2025, 2030, 2075); //Enclose top side of PA in boss
        assertEquals(Boss.STARTING_HP - 13, g1.getBoss().getHP());
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 3100, 3000, 1, false, 1, false));
        g1.checkPlayerAttacks(3000, 3000, 3050, 3050); //Fail first case
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 4000, 4600, 1, false, 1, false));
        g1.checkPlayerAttacks(4050, 4000, 4100, 4050); //Fail third case
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 25, 5000, 5000, 1, false, 1, false));
        g1.checkPlayerAttacks(5050, 5050, 5100, 5100); //Fail fourth case
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X + 1, Game.HEIGHT - Player.PLAYER_HEIGHT - Boss.HEIGHT / 2, Game.PLAYER_START_POS_X + 1000, Game.HEIGHT - Player.PLAYER_HEIGHT / 2);
        assertEquals(Boss.STARTING_HP - 13, g1.getBoss().getHP());
    }

    @Test
    void testCheckBossAttacks() {
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 300, 300));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(Player.STARTING_HP, g1.getPlayer().getHP()); //No hitbox overlap
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 160, 550));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(Player.STARTING_HP - 1, g1.getPlayer().getHP()); //Overlap bottom left of player
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 160, 485));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(Player.STARTING_HP - 2, g1.getPlayer().getHP()); //Overlap top left of player
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 220, 485));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(Player.STARTING_HP - 3, g1.getPlayer().getHP()); //Overlap top right of player
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 220, 550));
        g1.checkBossAttacks(200, 525, 235, 600);
        assertEquals(Player.STARTING_HP - 4, g1.getPlayer().getHP()); //Overlap bot right of player
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 1000, 1000));
        g1.checkBossAttacks(975, 975, 1075, 1075); //Enclose boss attack in player
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 1000, 1000));
        assertEquals(Player.STARTING_HP - 5, g1.getPlayer().getHP());
        resetPlayerIframes();
        g1.checkBossAttacks(1010, 1010, 1030, 1030); //Enclose player in boss attack
        assertEquals(Player.STARTING_HP - 6, g1.getPlayer().getHP());
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 2000, 2000));
        g1.checkBossAttacks(1950, 2025, 2025, 2030); //Enclose left side of BA in player
        assertEquals(Player.STARTING_HP - 7, g1.getPlayer().getHP());
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 2000, 2000));
        g1.checkBossAttacks(2025, 2025, 2075, 2030); //Enclose right side of BA in player
        assertEquals(Player.STARTING_HP - 8, g1.getPlayer().getHP());
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 2000, 2000));
        g1.checkBossAttacks(2025, 1950, 2030, 2025); //Enclose bottom side of BA in player
        assertEquals(Player.STARTING_HP - 9, g1.getPlayer().getHP());
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 2000, 2000));
        g1.checkBossAttacks(2025, 2025, 2030, 2075); //Enclose top side of BA in player
        assertEquals(Player.STARTING_HP - 10, g1.getPlayer().getHP());
        resetPlayerIframes();
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 3100, 3000));
        g1.checkBossAttacks(3000, 3000, 3050, 3050); //Fail first case
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 4000, 4600));
        g1.checkBossAttacks(4050, 4000, 4100, 4050); //Fail third case
        g1.getBoss().addBossAttack(new BossAttack(50, 25, 5000, 5000));
        g1.checkBossAttacks(5050, 5050, 5100, 5100); //Fail fourth case
        assertEquals(Player.STARTING_HP - 10, g1.getPlayer().getHP());
    }

    private void resetPlayerIframes() {
        for (int i = 0; i <= Player.MAX_IFRAMES; i++) {
            g1.getPlayer().tickIFrames();
        }
    }

    @Test
    void testCheckCollisionWithBoss() {
        g1.checkCollisionWithBoss(300, 500, 375, 600, 200, 525, 235, 600);
        assertEquals(Player.STARTING_HP, g1.getPlayer().getHP()); //No hitbox overlap
        g1.checkCollisionWithBoss(220, 450, 295, 550, 200, 525, 235, 600);
        assertEquals(Player.STARTING_HP - 1, g1.getPlayer().getHP()); //Overlap bottom left of boss
        resetPlayerIframes();
        g1.checkCollisionWithBoss(220, 550, 295, 650, 200, 525, 235, 600);
        assertEquals(Player.STARTING_HP - 2, g1.getPlayer().getHP()); //Overlap top left of boss
        resetPlayerIframes();
        g1.checkCollisionWithBoss(145, 550, 220, 650, 200, 525, 235, 600);
        assertEquals(Player.STARTING_HP - 3, g1.getPlayer().getHP()); //Overlap top right of boss
        resetPlayerIframes();
        g1.checkCollisionWithBoss(145, 450, 220, 550, 200, 525, 235, 600);
        assertEquals(Player.STARTING_HP - 4, g1.getPlayer().getHP()); //Overlap bottom right of boss
        resetPlayerIframes();
        g1.checkCollisionWithBoss(1950, 2025, 2025, 2030, 2000, 2000, 2050, 2050);
        assertEquals(Player.STARTING_HP - 5, g1.getPlayer().getHP()); //Overlap left side of boss
        resetPlayerIframes();
        g1.checkCollisionWithBoss(1950, 2025, 2025, 2030, 2000, 2000, 2050, 2050);
        assertEquals(Player.STARTING_HP - 6, g1.getPlayer().getHP()); //Overlap right side of boss
        resetPlayerIframes();
        g1.checkCollisionWithBoss(1950, 2025, 2025, 2030, 2000, 2000, 2050, 2050);
        assertEquals(Player.STARTING_HP - 7, g1.getPlayer().getHP()); //Overlap bottom side of boss
        resetPlayerIframes();
        g1.checkCollisionWithBoss(1950, 2025, 2025, 2030, 2000, 2000, 2050, 2050);
        assertEquals(Player.STARTING_HP - 8, g1.getPlayer().getHP()); //Overlap top side of boss
        resetPlayerIframes();
        g1.checkCollisionWithBoss(3100, 3000, 3150, 3050, 3000, 3000, 3050, 305); //Fail first case
        g1.checkCollisionWithBoss(6000, 6000, 6025, 6050, 6050, 6000, 6010, 6050); //Fail second case
        g1.checkCollisionWithBoss(4000, 4600, 4050, 4650, 4050, 4000, 4100, 4050); //Fail third case
        g1.checkCollisionWithBoss(5000, 5000, 5050, 5025, 5050, 5050, 5100, 5100); //Fail fourth case
        assertEquals(Player.STARTING_HP - 8, g1.getPlayer().getHP());
    }

    @Test
    void testReduceAttackLifespan() {
        g1.getPlayer().attack();
        resetAttackLockout();
        g1.getPlayer().attack();
        resetAttackLockout();
        PlayerAttack pa1 = g1.getPlayer().getPlayerAttacks().get(0);
        PlayerAttack pa2 = g1.getPlayer().getPlayerAttacks().get(1);
        for (int i = 0; i < 3; i++) {
            g1.getPlayer().incrementAttackCounter();
        }
        g1.getPlayer().spellAttack();
        resetAttackLockout();
        PlayerAttack pa3 = g1.getPlayer().getPlayerAttacks().get(2);
        int startingLifespan = pa1.getLifespan();
        int startingLifespan2 = pa3.getLifespan();
        g1.reduceAttackLifespan();
        assertEquals(startingLifespan - 1, pa1.getLifespan());
        assertEquals(startingLifespan - 1, pa2.getLifespan());
        assertEquals(startingLifespan2 - 1, pa3.getLifespan());
        g1.reduceAttackLifespan();
        assertEquals(startingLifespan2 - 2, pa3.getLifespan());
        for (int i = 0; i <= 10000; i++) {
            g1.reduceAttackLifespan();
        }
        assertEquals(0, pa1.getLifespan());
    }

    private void resetAttackLockout() {
        for (int i = 0; i <= Player.MAX_LOCKOUT_TICKS; i++) {
            g1.getPlayer().tickAttackLockouts();
        }
    }

    @Test
    void testIsGameOver() {
        assertFalse(g1.isGameOver());
        g1.getPlayer().takeDamage(g1.getPlayer().getHP());
        assertTrue(g1.isGameOver());
        g2.getBoss().takeDamage(g2.getBoss().getHP());
        assertTrue(g2.isGameOver());
    }

    @Test
    void testPause() {
        assertFalse(g1.getPause());
        g1.pause();
        assertTrue(g1.getPause());
    }

    @Test
    void testUnpause() {
        Set<Integer> keys = new HashSet<>();
        keys.add(KeyEvent.VK_ESCAPE);
        g1.update(keys);
        assertTrue(g1.getPause());
        g1.unpause();
        assertFalse(g1.getPause());
    }

}
