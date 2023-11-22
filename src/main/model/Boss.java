package model;

import org.json.JSONObject;

import java.util.Random;
import java.util.List;

//Represents an arbitrary boss and includes base methods relevant to all bosses

public abstract class Boss {

    public static final int HORIZONTAL_HITBOX_INSET = 5;
    public static final int VERTICAL_HITBOX_INSET = 5;
    public static final int STARTING_HP = 40;
    public static final int WIDTH = 69 - (2 * HORIZONTAL_HITBOX_INSET);
    public static final int HEIGHT = 116 - (2 * VERTICAL_HITBOX_INSET);
    public static final double BASE_MOVE_SPEED = 3;
    protected static final int ATTACK_INTERVAL = 500;
    public static final int TELEGRAPH_DELAY = 75;

    //posX and posY represent the coordinates of the top left of the boss' hitbox
    protected double posX;
    protected double posY;
    protected double speedY;
    protected int facing;
    protected int hp;
    protected int attackTimer;
    protected Player player;
    protected double bonusMoveSpeed;
    protected int lastUsedAttack;
    protected boolean currentlyAttacking;
    protected boolean movementOverride;
    protected List<BossAttack> bossAttacks;

    abstract void attack1(Player p);

    abstract void attack2(Player p);

    abstract void attack3(Player p);

    abstract void handleLingeringAttacks(Player p);

    abstract JSONObject getData();

    //EFFECTS: moves the boss towards the player unless movement override is true,
    //         where it keeps moving in the same direction as before
    //MODIFIES: this
    void move(Player p) {
        if (movementOverride) {
            this.posX += (BASE_MOVE_SPEED + this.bonusMoveSpeed) * facing;
        } else {
            double playerCentre = p.getX() + (p.getWidth() / 2);
            double bossCentre = this.posX + ((double) WIDTH / 2);
            if (playerCentre < bossCentre) {
                this.posX -= BASE_MOVE_SPEED + this.bonusMoveSpeed;
                facing = -1;
            } else {
                this.posX += BASE_MOVE_SPEED + this.bonusMoveSpeed;
                facing = 1;
            }
        }
        this.posY += this.speedY;
        handleScreenBoundary();
    }

    //EFFECTS: moves the boss back onto the screen if it leaves the boundaries
    //MODIFIES: this
    void handleScreenBoundary() {
        if (this.posX < 0) {
            this.posX = 0;
        } else if (this.posX > Game.WIDTH - WIDTH) {
            this.posX = Game.WIDTH - WIDTH;
        }
        if (this.posY > Game.HEIGHT - HEIGHT) {
            this.posY = Game.HEIGHT - HEIGHT;
        } else if (this.posY < 0) {
            this.posY = 0;
        }
    }

    //EFFECTS: modifies the speed of the boss in the y direction by amount
    //MODIFIES: this
    void accelerateY(double amount) {
        this.speedY += amount;
    }

    //EFFECTS: returns whether the boss' hitbox is on the ground
    public boolean onGround() {
        return this.posY >= Game.HEIGHT - HEIGHT - VERTICAL_HITBOX_INSET;
    }

    //EFFECTS: issues an attack and resets the attack timer if the timer is 0, otherwise decrements the timer by 1
    //MODIFIES: this
    void handleAttackCycle() {
        if (this.attackTimer == TELEGRAPH_DELAY) {
            generateAttack();
        }
        if (this.attackTimer == 0) {
            attack(lastUsedAttack);
            this.attackTimer = ATTACK_INTERVAL;
        } else {
            attackTimer--;
        }
    }

    //EFFECTS: randomly selects one of the 3 available attacks for the boss to use next
    //MODIFIES: this
    void generateAttack() {
        Random r = new Random();
        int attackNumber = r.nextInt(3) + 1; //Generates a random number from 1-3 (inclusive)
        this.lastUsedAttack = attackNumber;
    }

    //EFFECTS: attacks the player with the attack specified by attackNumber
    //REQUIRES: 1 <= attackNumber <= 3
    void attack(int attackNumber) {
        switch (attackNumber) {
            case 1:
                attack1(this.player);
                break;
            case 2:
                attack2(this.player);
                break;
            case 3:
                attack3(this.player);
                break;
        }
    }

    //EFFECTS: reduces the HP of boss by amount if the boss has not taken damage recently
    //MODIFIES: this
    //REQUIRES: amount > 0
    void takeDamage(int amount) {
        this.hp -= amount;
    }

    public double getX() {
        return this.posX;
    }

    public double getY() {
        return this.posY;
    }

    public double getSpeedY() {
        return this.speedY;
    }

    public int getHP() {
        return this.hp;
    }

    public int getAttackTimer() {
        return this.attackTimer;
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getBonusMoveSpeed() {
        return this.bonusMoveSpeed;
    }

    public int getLastUsedAttack() {
        return this.lastUsedAttack;
    }

    public boolean getCurrentlyAttacking() {
        return this.currentlyAttacking;
    }

    public boolean getMovementOverride() {
        return this.movementOverride;
    }

    public List<BossAttack> getBossAttacks() {
        return this.bossAttacks;
    }

    public int getFacing() {
        return this.facing;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    //Setters for testing purposes

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public void setX(int posX) {
        this.posX = posX;
    }

    public void setY(int posY) {
        this.posY = posY;
    }

    public void setLastUsedAttack(int lastUsedAttack) {
        this.lastUsedAttack = lastUsedAttack;
    }

    public void setAttackTimer(int timer) {
        this.attackTimer = timer;
    }

    public void setMovementOverride(boolean mo) {
        this.movementOverride = mo;
    }

    public void setBonusMoveSpeed(int speed) {
        this.bonusMoveSpeed = speed;
    }

    public void addBossAttack(BossAttack ba) {
        this.bossAttacks.add(ba);
    }

}
