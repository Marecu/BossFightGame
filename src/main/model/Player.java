package model;

import java.util.List;
import java.util.ArrayList;

//Represents the player that the user controls

public class Player {

    private static final double PLAYER_HEIGHT = 75;
    private static final double PLAYER_WIDTH = 35;
    private static final double JUMP_STRENGTH = -50;
    private static final int ACCEL_STRENGTH = 5;
    private static final int SPELL_REQUIRED_HITS = 3;
    private static final int ATTACK_WIDTH = 75;
    private static final int ATTACK_HEIGHT = 20;
    private static final int MISSILE_WIDTH = 30;
    private static final int MISSILE_HEIGHT = 10;
    private static final int MISSILE_SPEED = 20;
    private static final int MISSILE_LIFESPAN = 30;

    //posX and posY represent the coordinates of the top left of the player's hitbox
    private double posX;
    private double posY;
    private double speedX;
    private double speedY;
    private int facing; //-1 is left, 1 is right
    private boolean hasJump;
    private int hp;
    private int totalHits;
    private List<PlayerAttack> playerAttacks;

    //EFFECTS: creates a new player at the specified coordinates
    public Player(double x, double y) {
        this.posX = x;
        this.posY = y;
        this.speedX = 0;
        this.speedY = 0;
        this.facing = 1;
        this.hasJump = true;
        this.hp = 3;
        this.totalHits = 0;
        this.playerAttacks = new ArrayList<>();
    }

    //EFFECTS: modifies the speed of the player in the X direction by amount
    //MODIFIES: this
    void accelerateX(double amount) {
        this.speedX += amount;
    }

    //EFFECTS: modifies the speed of the player in the Y direction by amount
    //MODIFIES: this
    void accelerateY(double amount) {
        this.speedY += amount;
    }

    //EFFECTS: moves the player in the X direction by speedX and moves the player in the Y direction
    //         by speedY
    //MODIFIES: this
    void move() {
        if ((this.posX + speedX < (800 - this.PLAYER_WIDTH)) && (this.posX + speedX > 0)) {
            this.posX += speedX;
        } else {
            if (this.posX + speedX < 0) {
                this.posX = 0;
            } else {
                this.posX = (800 - this.PLAYER_WIDTH);
            }
            this.speedX = 0;
        }

        if (this.posY + this.speedY <= this.PLAYER_HEIGHT) {
            this.posY = this.PLAYER_HEIGHT;
        } else {
            this.posY += speedY;
        }
    }

    //EFFECTS: changes speedX by ACCEL_STRENGTH (facing left)
    //MODIFIES: this
    void moveL() {
        if (this.speedX > 0) {
            this.speedX = 0;
        }
        this.facing = -1;
        accelerateX(ACCEL_STRENGTH * this.facing);
    }

    //EFFECTS: changes speedX by ACCEL_STRENGTH (facing right)
    //MODIFIES: this
    void moveR() {
        if (this.speedX < 0) {
            this.speedX = 0;
        }
        this.facing = 1;
        accelerateX(ACCEL_STRENGTH);
    }

    //EFFECTS: increases speedY by JUMP_STRENGTH if the player is able to use their jump
    //MODIFIES: this
    void jump() {
        if (this.hasJump) {
            this.accelerateY(this.JUMP_STRENGTH);
        }
    }

    //EFFECTS: causes the player to issue a basic attack in the direction they are facing
    //MODIFIES: this
    void attack() {
        int posXOffset;
        if (facing == 1) {
            posXOffset = (int)this.posX + (int)this.PLAYER_WIDTH;
        } else {
            posXOffset = (int)this.posX - this.ATTACK_WIDTH;
        }
        PlayerAttack attack = new PlayerAttack(this.ATTACK_WIDTH,
                this.ATTACK_HEIGHT,
                posXOffset,
                this.posY + (this.PLAYER_HEIGHT / 2) - (this.ATTACK_HEIGHT / 2), 1, false, this.facing);
        playerAttacks.add(attack);
    }

    //EFFECTS: causes the player to issue a spell attack in the direction they are facing, creating a
    //         number of projectiles equal to totalHits / SPELL_REQUIRED_HITS
    //MODIFIES: this
    void spellAttack() {
        int posXOffset;
        if (facing == 1) {
            posXOffset = (int)this.posX + (int)this.PLAYER_WIDTH;
        } else {
            posXOffset = (int)this.posX - this.MISSILE_WIDTH;
        }
        if (totalHits >= SPELL_REQUIRED_HITS) {
            int missilesShot = totalHits / SPELL_REQUIRED_HITS;
            for (int i = 0; i < missilesShot; i++) {
                PlayerAttack projectile = new PlayerAttack(MISSILE_WIDTH, MISSILE_HEIGHT, posXOffset, this.posY
                        + (this.PLAYER_HEIGHT / 2) - (this.MISSILE_HEIGHT / 2),
                        MISSILE_LIFESPAN, true, this.facing);
                playerAttacks.add(projectile);
            }
            totalHits -= missilesShot * SPELL_REQUIRED_HITS;
        }
    }

    //EFFECTS: moves all projectiles and reduces their lifespan by 1
    //MODIFIES: this
    void moveProjectiles() {
        for (PlayerAttack next : playerAttacks) {
            if (next.getMoving()) {
                next.move(MISSILE_SPEED * next.getFacing(), 0);
            }
        }
    }

    //EFFECTS: removes pa from the list of active player attacks
    //MODIFIES: this
    void removeAttack(PlayerAttack pa) {
        this.playerAttacks.remove(pa);
    }

    //EFFECTS: returns whether or not the player's hitbox is touching the ground
    boolean onGround() {
        return this.posY >= 600 - this.PLAYER_HEIGHT;
    }

    //EFFECTS: reduces the player's hp by amount
    //MODIFIES: this
    //REQUIRES: amount > 0
    void takeDamage(int amount) {
        this.hp -= amount;
    }

    //EFFECTS: increases the player's attack counter by 1
    //MODIFIES: this
    void incrementAttackCounter() {
        this.totalHits++;
    }

    public double getX() {
        return this.posX;
    }

    public double getY() {
        return this.posY;
    }

    public double getSpeedX() {
        return this.speedX;
    }

    public double getSpeedY() {
        return this.speedY;
    }

    public int getFacing() {
        return this.facing;
    }

    public boolean getHasJump() {
        return this.hasJump;
    }

    public void setHasJump(boolean hasJump) {
        this.hasJump = hasJump;
    }

    public int getHP() {
        return this.hp;
    }

    public int getTotalHits() {
        return this.totalHits;
    }

    public List<PlayerAttack> getPlayerAttacks() {
        return this.playerAttacks;
    }

    public double getHeight() {
        return this.PLAYER_HEIGHT;
    }

    public double getWidth() {
        return this.PLAYER_WIDTH;
    }

    public double getJumpStrength() {
        return this.JUMP_STRENGTH;
    }

    //Setters for testing purposes:
    public void addPlayerAttack(PlayerAttack pa) {
        this.playerAttacks.add(pa);
    }
}
