package model;

import org.json.JSONObject;
import persistence.Writable;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;

//Represents the overall game and manages the progression of the game via user interaction

public class Game implements Writable {

    public static final int WIDTH = 1250;
    public static final int HEIGHT = 550;

    public static final double BOSS_START_POS_X = WIDTH - Boss.WIDTH * 2;
    public static final double BOSS_START_POS_Y = HEIGHT - Boss.HEIGHT;

    public static final double PLAYER_START_POS_X = Player.PLAYER_WIDTH * 4;
    public static final double PLAYER_START_POS_Y = HEIGHT - Player.PLAYER_HEIGHT;

    private static final double GRAVITATIONAL_CONSTANT = 0.25;

    private Player player;
    private Boss boss;
    private boolean pause;

    //EFFECTS: constructs a game with the player and boss1 having specified data
    public Game(Player p, Boss b) {
        this.player = p;
        this.boss = b;
    }

    //EFFECTS: creates a new game with the player and boss1 in their starting positions
    public Game() {
        this.player = new Player(PLAYER_START_POS_X, PLAYER_START_POS_Y);
        this.boss = new Boss1(BOSS_START_POS_X, BOSS_START_POS_Y, this.player);
    }

    //EFFECTS: updates the game state - moves everything, handles the boss' attacks, and processes user input
    //MODIFIES: this.player, this.boss, this
    public void update(Set<Integer> keys) {
        for (Integer next : keys) {
            handleUserInput(next);
        }
        this.player.move();
        this.player.applyFriction();
        this.player.moveProjectiles();
        this.player.tickIFrames();
        this.player.tickAttackLockouts();
        this.boss.move(this.player);
        applyGravity();
        this.boss.handleAttackCycle();
        this.boss.handleLingeringAttacks(this.player);
        checkCollisions();
        reduceAttackLifespan();
    }

    //EFFECTS: applies the correct action based on the user's input
    //MODIFIES: this.player
    void handleUserInput(int input) {
        if (input == KeyEvent.VK_A) {
            this.player.moveL();
        }
        if (input == KeyEvent.VK_D) {
            this.player.moveR();
        }
        if (input == KeyEvent.VK_SPACE || input == KeyEvent.VK_W) {
            this.player.jump();
        }
        if (input == KeyEvent.VK_ENTER) {
            this.player.attack();
        }
        if (input == KeyEvent.VK_E) {
            this.player.spellAttack();
        }
        if (input == KeyEvent.VK_ESCAPE) {
            pause();
        }
    }

    //EFFECTS: applies a downward acceleration to the player and boss to emulate gravity
    //MODIFIES: this.player, this.boss
    void applyGravity() {
        if (!player.onGround()) {
            player.accelerateY(GRAVITATIONAL_CONSTANT);
        } else {
            player.accelerateY(player.getSpeedY() * -1);
            player.setHasJump(true);
        }

        if (!boss.onGround()) {
            boss.accelerateY(GRAVITATIONAL_CONSTANT);
        } else {
            boss.accelerateY(boss.getSpeedY() * -1);
        }
    }

    //EFFECTS: evaluates all hitbox overlaps and registers appropriate damage
    //MODIFIES: this.player, this.boss, this
    void checkCollisions() {
        double bossX = this.boss.getX();
        double bossY = this.boss.getY();
        double bossW = bossX + this.boss.getWidth();
        double bossH = bossY + this.boss.getHeight();
        double playerX = this.player.getX();
        double playerY = this.player.getY();
        double playerW = playerX + this.player.getWidth();
        double playerH = playerY + this.player.getHeight();
        checkPlayerAttacks(bossX, bossY, bossW, bossH);
        checkBossAttacks(playerX, playerY, playerW, playerH);
        checkCollisionWithBoss(bossX, bossY, bossW, bossH, playerX, playerY, playerW, playerH);
    }

    //EFFECTS: checks to see if any of the player's attacks have hit the boss
    //MODIFIES: this.player, this.boss
    void checkPlayerAttacks(double bossX, double bossY, double bossW, double bossH) {
        for (PlayerAttack next : this.player.getPlayerAttacks()) {
            double nextX = next.getX();
            double nextY = next.getY();
            double nextW = nextX + next.getWidth();
            double nextH = nextY + next.getHeight();
            if ((nextX <= bossW) && (nextW >= bossX) && (nextY <= bossH) && (nextH >= bossY) && !next.getHasHit()) {
                if (!next.getMoving()) { //If the attack isn't a projectile
                    player.incrementAttackCounter();
                    boss.takeDamage(1);
                } else {
                    boss.takeDamage(2);
                }
                next.registerHit();
            }
        }
    }

    //EFFECTS: checks to see if any of the boss' attacks have hit the player
    //MODIFIES: this.player
    void checkBossAttacks(double playerX, double playerY, double playerW, double playerH) {
        for (BossAttack next : this.boss.getBossAttacks()) {
            double nextX = next.getX();
            double nextY = next.getY();
            double nextW = nextX + next.getWidth();
            double nextH = nextY + next.getHeight();
            if ((nextX <= playerW) && (nextW >= playerX) && (nextY <= playerH) && (nextH >= playerY)) {
                this.player.takeDamage(1);
            }
        }
    }

    //EFFECTS: checks to see if the boss' hitbox overlaps with the player's
    //MODIFIES: this.player
    void checkCollisionWithBoss(double bossX, double bossY, double bossW, double bossH,
                                double playerX, double playerY, double playerW, double playerH) {
        if ((bossX <= playerW) && (bossW >= playerX) && (bossY <= playerH) && (bossH >= playerY)) {
            this.player.takeDamage(1);
        }
    }

    //EFFECTS: Subtracts 1 from the lifespan counter of each of the player's projectiles. Removes if lifespan = 0
    //MODIFIES: this.player
    void reduceAttackLifespan() {
        ArrayList<PlayerAttack> attacksToRemove = new ArrayList<>();
        for (PlayerAttack next : this.player.getPlayerAttacks()) {
            next.subtractLifespan();
            if (next.getLifespan() <= 0) {
                attacksToRemove.add(next);
            }
        }
        for (PlayerAttack next : attacksToRemove) {
            EventLog log = EventLog.getInstance();
            log.logEvent(new Event("Removed " + (next.getMoving() ? "spell" : "basic")
                    + " attack at x: " + next.getX() + ", y: " + next.getY()));
            this.player.removeAttack(next);
        }
    }

    //EFFECTS: returns true if the player or the boss has died
    public boolean isGameOver() {
        return (this.player.getHP() <= 0) || (this.boss.getHP() <= 0);
    }

    //EFFECTS: Returns the complete game state as a JSON object
    public JSONObject toJson() {
        JSONObject js = new JSONObject();
        js.put("Player", this.player.getData()); //includes player attacks
        js.put("Boss1", this.boss.getData()); //includes boss attacks
        return js;
    }

    //EFFECTS: pauses the game
    //MODIFIES: this
    public void pause() {
        this.pause = true;
    }

    //EFFECTS: unpauses the game
    //MODIFIES: this
    public void unpause() {
        this.pause = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Boss getBoss() {
        return this.boss;
    }

    public double getGravitationalConstant() {
        return this.GRAVITATIONAL_CONSTANT;
    }

    public boolean getPause() {
        return this.pause;
    }

}
