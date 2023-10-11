package model;

import java.util.ArrayList;

//Represents the overall game and manages the progression of the game via user interaction

public class Game {

    private static final double BOSS_START_POS_X = 600;
    private static final double BOSS_START_POS_Y = 500;

    private static final double PLAYER_START_POS_X = 200;
    private static final double PLAYER_START_POS_Y = 525;

    private static final double GRAVITATIONAL_CONSTANT = 9.81;

    private Player player;
    private Boss boss;

    public Game() {
        this.player = new Player(PLAYER_START_POS_X, PLAYER_START_POS_Y);
        this.boss = new Boss1(BOSS_START_POS_X, BOSS_START_POS_Y, this.player);
    }

    //EFFECTS: updates the game state - moves everything, handles the boss' attacks, and processes user input
    //MODIFIES: this.player, this.boss, this
    public void update(String input) {
        handleUserInput(input);
        this.player.move();
        this.player.moveProjectiles();
        this.boss.move(this.player);
        applyGravity();
        this.boss.handleAttackCycle();
        this.boss.handleLingeringAttacks(this.player);
        checkCollisions();
        reduceAttackLifespan();
    }

    //EFFECTS: applies the correct action based on the user's input
    //MODIFIES: this.player
    void handleUserInput(String input) {
        switch (input) {
            case "a":
                this.player.moveL();
                break;
            case "d":
                this.player.moveR();
                break;
            case "jump":
                this.player.jump();
                break;
            case "attack":
                this.player.attack();
                break;
            case "spell":
                this.player.spellAttack();
                break;
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
            if (((bossX >= nextX && bossX <= nextW) && (bossY >= nextY && bossY <= nextH)) //Top left
                    || ((bossW >= nextX && bossW <= nextW) && (bossY >= nextY && bossY <= nextH)) //Top right
                    || ((bossX >= nextX && bossX <= nextW) && (bossH >= nextY && bossH <= nextH)) //Bot left
                    || ((bossW >= nextX && bossW <= nextW) && (bossH >= nextY && bossH <= nextH)) //Bot right
                    || ((nextX >= bossX && nextX <= bossW) && (nextY >= bossY && nextY <= bossH)) //Top left
                    || ((nextW >= bossX && nextW <= bossW) && (nextY >= bossY && nextY <= bossH)) //Top right
                    || ((nextX >= bossX && nextX <= bossW) && (nextH >= bossY && nextH <= bossH)) //Bot left
                    || ((nextW >= bossX && nextW <= bossW) && (nextH >= bossY && nextH <= bossH))) { //Bot right
                System.out.println("Attack hit!");
                boss.takeDamage(1);
                if (!next.getMoving()) { //If the attack isn't a basic attack
                    player.incrementAttackCounter();
                }
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
            if (((playerX >= nextX && playerX <= nextW) && (playerY >= nextY && playerY <= nextH)) //Top left
                    || ((playerW >= nextX && playerW <= nextW) && (playerY >= nextY && playerY <= nextH)) //Top right
                    || ((playerX >= nextX && playerX <= nextW) && (playerH >= nextY && playerH <= nextH)) //Bot left
                    || ((playerW >= nextX && playerW <= nextW) && (playerH >= nextY && playerH <= nextH)) //Bot right
                    || ((nextX >= playerX && nextX <= playerW) && (nextY >= playerY && nextY <= playerH)) //Top left
                    || ((nextW >= playerX && nextW <= playerW) && (nextY >= playerY && nextY <= playerH)) //Top right
                    || ((nextX >= playerX && nextX <= playerW) && (nextH >= playerY && nextH <= playerH)) //Bot left
                    || ((nextW >= playerX && nextW <= playerW) && (nextH >= playerY && nextH <= playerH))) { //Bot right
                this.player.takeDamage(1);
            }
        }
    }

    //EFFECTS: checks to see if the boss' hitbox overlaps with the player's
    //MODIFIES: this.player
    void checkCollisionWithBoss(double bossX, double bossY, double bossW, double bossH,
                                double playerX, double playerY, double playerW, double playerH) {
        if (((bossX >= playerX && bossX <= playerW) && (bossY >= playerY && bossY <= playerH)) //Top left
                || ((bossW >= playerX && bossW <= playerW) && (bossY >= playerY && bossY <= playerH)) //Top right
                || ((bossX >= playerX && bossX <= playerW) && (bossH >= playerY && bossH <= playerH)) //Bot left
                || ((bossW >= playerX && bossW <= playerW) && (bossH >= playerY && bossH <= playerH)) // Bot right
                || ((playerX >= bossX && playerX <= bossW) && (playerY >= bossY && playerY <= bossH)) //Top left
                || ((playerW >= bossX && playerW <= bossW) && (playerY >= bossY && playerY <= bossH)) //Top right
                || ((playerX >= bossX && playerX <= bossW) && (playerH >= bossY && playerH <= bossH)) //Bot left
                || ((playerW >= bossX && playerW <= bossW) && (playerH >= bossY && playerH <= bossH))) { //Bot right
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
            this.player.removeAttack(next);
        }
    }

    //EFFECTS: returns true if the player or the boss has died
    public boolean isGameOver() {
        return (this.player.getHP() <= 0) || (this.boss.getHP() <= 0);
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

}