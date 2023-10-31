package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    public static final String FILE_PATH = "./data/testGameData.json";

    Game g1;
    JsonWriter jw;
    JsonReader jr;

    @BeforeEach
    void runBefore() {
        g1 = new Game();
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
        g1.getPlayer().accelerateX(50);
        g1.getPlayer().accelerateY(-20);
        g1.getBoss().accelerateY(-30);
        g1.update("attack");
        assertEquals(Game.PLAYER_START_POS_X + 50, g1.getPlayer().getX());
        assertEquals(Game.PLAYER_START_POS_Y - 20, g1.getPlayer().getY());
        assertEquals(Game.BOSS_START_POS_Y - 30, g1.getBoss().getY());
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
        g1.handleUserInput("save");
        assertTrue(g1.getSave());
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
        g1.getBoss().setX((int) (Game.PLAYER_START_POS_X + Player.PLAYER_WIDTH / 2));
        g1.getBoss().setY((int) (Game.PLAYER_START_POS_Y + Player.PLAYER_HEIGHT / 2));
        g1.getBoss().addBossAttack(new BossAttack(50,50, Game.PLAYER_START_POS_X + Player.PLAYER_WIDTH / 2, Game.PLAYER_START_POS_Y + Player.PLAYER_HEIGHT / 2));
        g1.getPlayer().attack();
        assertEquals(Game.PLAYER_START_POS_X + Player.PLAYER_WIDTH, g1.getPlayer().getPlayerAttacks().get(0).getX());
        assertEquals(Game.PLAYER_START_POS_Y + (Player.PLAYER_HEIGHT / 2) - (Player.ATTACK_HEIGHT / 2), g1.getPlayer().getPlayerAttacks().get(0).getY());
        g1.checkCollisions();
        assertEquals(1, g1.getPlayer().getHP());
        assertEquals(29, g1.getBoss().getHP());
    }

    @Test
    void testCheckPlayerAttacks() {
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(0, 0, 1, 1);
        assertEquals(30, g1.getBoss().getHP()); //No hitbox overlap
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X + 1, Game.HEIGHT - Player.PLAYER_HEIGHT - Boss.HEIGHT / 2, Game.PLAYER_START_POS_X + 1000, Game.HEIGHT - Player.PLAYER_HEIGHT / 2);
        assertEquals(29, g1.getBoss().getHP()); //Overlap bottom left of boss
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X + 1, Game.HEIGHT - Player.PLAYER_HEIGHT / 2, Game.PLAYER_START_POS_X + 1000, Game.HEIGHT + Boss.HEIGHT / 2);
        assertEquals(28, g1.getBoss().getHP()); //Overlap top left of boss
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X - 25, Game.HEIGHT - Player.PLAYER_HEIGHT / 2, Game.PLAYER_START_POS_X + 50, Game.HEIGHT + Boss.HEIGHT / 2);
        assertEquals(27, g1.getBoss().getHP()); //Overlap top right of boss
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X - 25, Game.HEIGHT - Player.PLAYER_HEIGHT - Boss.HEIGHT / 2, Game.PLAYER_START_POS_X + 50, Game.HEIGHT - Player.PLAYER_HEIGHT / 2);
        assertEquals(26, g1.getBoss().getHP()); //Overlap bottom right of boss
        g1.getPlayer().attack();
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X - 25, Game.HEIGHT - Player.PLAYER_HEIGHT / 2, Game.PLAYER_START_POS_X + 50, Game.HEIGHT + Boss.HEIGHT / 2);
        assertEquals(24, g1.getBoss().getHP()); //Test multiple attacks
        assertEquals(6, g1.getPlayer().getTotalHits());
        g1.getPlayer().spellAttack();
        assertEquals(4, g1.getPlayer().getPlayerAttacks().size());
        g1.checkPlayerAttacks(Game.PLAYER_START_POS_X - 25, Game.HEIGHT - Boss.HEIGHT, Game.PLAYER_START_POS_X + 50, Game.HEIGHT);
        assertEquals(20, g1.getBoss().getHP());
        assertEquals(2, g1.getPlayer().getTotalHits());
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
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 3100, 3000, 1, false, 1));
        g1.checkPlayerAttacks(3000, 3000, 3050, 3050); //Fail first case
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 50, 4000, 4600, 1, false, 1));
        g1.checkPlayerAttacks(4050, 4000, 4100, 4050); //Fail third case
        g1.getPlayer().addPlayerAttack(new PlayerAttack(50, 25, 5000, 5000, 1, false, 1));
        g1.checkPlayerAttacks(5050, 5050, 5100, 5100); //Fail fourth case
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
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 3100, 3000));
        g1.checkBossAttacks(3000, 3000, 3050, 3050); //Fail first case
        g1.getBoss().addBossAttack(new BossAttack(50, 50, 4000, 4600));
        g1.checkBossAttacks(4050, 4000, 4100, 4050); //Fail third case
        g1.getBoss().addBossAttack(new BossAttack(50, 25, 5000, 5000));
        g1.checkBossAttacks(5050, 5050, 5100, 5100); //Fail fourth case
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
        g1.checkCollisionWithBoss(3100, 3000, 3150, 3050, 3000, 3000, 3050, 305); //Fail first case
        g1.checkCollisionWithBoss(6000, 6000, 6025, 6050, 6050, 6000, 6010, 6050); //Fail second case
        g1.checkCollisionWithBoss(4000, 4600, 4050, 4650, 4050, 4000, 4100, 4050); //Fail third case
        g1.checkCollisionWithBoss(5000, 5000, 5050, 5025, 5050, 5050, 5100, 5100); //Fail fourth case
        assertEquals(-5, g1.getPlayer().getHP());
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
