package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;

//Represents the player that the user controls

public class Player {

    public static final double PLAYER_HEIGHT = 75;
    public static final double PLAYER_WIDTH = 35;
    public static final double JUMP_STRENGTH = -10;
    public static final double ACCEL_STRENGTH = 0.1;
    public static final int SPELL_REQUIRED_HITS = 3;
    public static final int ATTACK_WIDTH = 75;
    public static final int ATTACK_HEIGHT = 20;
    public static final int MISSILE_WIDTH = 30;
    public static final int MISSILE_HEIGHT = 10;
    private static final double MISSILE_SPEED = 20;
    private static final int MISSILE_LIFESPAN = 200;
    public static final int MAX_IFRAMES = 50;
    public static final int MAX_LOCKOUT_TICKS = 25;

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
    private boolean invincible;
    private int iframes;
    private boolean canAttack;
    private int lockoutTicks;
    private boolean canSpell;
    private int spellLockoutTicks;

    //EFFECTS: creates a player with the specified stats
    public Player(double x, double y, double speedX, double speedY, int facing,
                  boolean hasJump, int hp, int totalHits, List<PlayerAttack> playerAttacks, boolean invincible,
                  int iframes, boolean canAttack, int lockoutTicks, boolean canSpell, int spellLockoutTicks) {
        this.posX = x;
        this.posY = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.facing = facing;
        this.hasJump = hasJump;
        this.hp = hp;
        this.totalHits = totalHits;
        this.playerAttacks = playerAttacks;
        this.invincible = invincible;
        this.iframes = iframes;
        this.canAttack = canAttack;
        this.lockoutTicks = lockoutTicks;
        this.canSpell = canSpell;
        this.spellLockoutTicks = spellLockoutTicks;
    }

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
        this.invincible = false;
        this.iframes = 0;
        this.canAttack = true;
        this.lockoutTicks = 0;
        this.canSpell = true;
        this.spellLockoutTicks = 0;
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
        if ((this.posX + speedX < (Game.WIDTH - this.PLAYER_WIDTH)) && (this.posX + speedX > 0)) {
            this.posX += speedX;
        } else {
            if (this.posX + speedX <= 0) {
                this.posX = 0;
            } else {
                this.posX = (Game.WIDTH - this.PLAYER_WIDTH);
            }
            this.speedX = 0;
        }

        if (this.posY + this.speedY <= this.PLAYER_HEIGHT) {
            this.posY = this.PLAYER_HEIGHT;
        } else if (this.posY + this.speedY >= Game.HEIGHT - this.PLAYER_HEIGHT) {
            this.posY = Game.HEIGHT - this.PLAYER_HEIGHT;
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
            this.hasJump = false;
        }
    }

    void setHasJump(boolean jump) {
        this.hasJump = jump;
    }

    //EFFECTS: causes the player to issue a basic attack in the direction they are facing if they can attack
    //MODIFIES: this
    void attack() {
        if (canAttack) {
            int posXOffset;
            if (facing == 1) {
                posXOffset = (int)this.posX + (int)this.PLAYER_WIDTH;
            } else {
                posXOffset = (int)this.posX - this.ATTACK_WIDTH;
            }
            PlayerAttack attack = new PlayerAttack(this.ATTACK_WIDTH,
                    this.ATTACK_HEIGHT,
                    posXOffset,
                    this.posY + (this.PLAYER_HEIGHT / 2) - (this.ATTACK_HEIGHT / 2),
                    MAX_LOCKOUT_TICKS / 2, false, this.facing);
            playerAttacks.add(attack);
            this.canAttack = false;
            this.lockoutTicks = MAX_LOCKOUT_TICKS;
        }
    }

    //EFFECTS: causes the player to issue a spell attack in the direction they are facing, creating a
    //         number of projectiles equal to totalHits / SPELL_REQUIRED_HITS if they are able to cast
    //         a spell
    //MODIFIES: this
    void spellAttack() {
        if (canSpell) {
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
            this.canSpell = false;
            this.spellLockoutTicks = MAX_LOCKOUT_TICKS;
        }
    }

    void tickAttackLockouts() {
        if (this.lockoutTicks > 0) {
            this.lockoutTicks -= 1;
        } else {
            this.canAttack = true;
        }
        if (this.spellLockoutTicks > 0) {
            this.spellLockoutTicks -= 1;
        } else {
            this.canSpell = true;
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
        return this.posY >= Game.HEIGHT - this.PLAYER_HEIGHT;
    }

    //EFFECTS: reduces the player's hp by amount if the player has not been hit recently
    //MODIFIES: this
    //REQUIRES: amount > 0
    void takeDamage(int amount) {
        if (!this.invincible) {
            this.hp -= amount;
            this.iframes = MAX_IFRAMES;
            this.invincible = true;
        }
    }

    //EFFECTS: reduces the player's invincibility period after being hit
    //MODIFIES: this
    void tickIFrames() {
        if (this.iframes > 0) {
            this.iframes -= 1;
        } else {
            this.invincible = false;
        }
    }

    //EFFECTS: increases the player's attack counter by 1
    //MODIFIES: this
    void incrementAttackCounter() {
        this.totalHits++;
    }

    //EFFECTS: returns a JSON object containing all the data about the player
    public JSONObject getData() {
        JSONObject playerData = new JSONObject();
        playerData.put("posX", this.posX);
        playerData.put("posY", this.posY);
        playerData.put("speedX", this.speedX);
        playerData.put("speedY", this.speedY);
        playerData.put("facing", this.facing);
        playerData.put("hasJump", this.hasJump);
        playerData.put("hp", this.hp);
        playerData.put("totalHits", this.totalHits);
        playerData.put("playerAttacks", parseAttacksJson());
        playerData.put("invincible", this.invincible);
        playerData.put("iframes", this.iframes);
        playerData.put("canAttack", this.canAttack);
        playerData.put("lockoutTicks", this.lockoutTicks);
        playerData.put("canSpell", this.canSpell);
        playerData.put("spellLockoutTicks", this.spellLockoutTicks);
        return playerData;
    }

    //EFFECTS: parses the playerAttacks array into a JSON array with the appropriate data
    public JSONArray parseAttacksJson() {
        JSONArray attacksJson = new JSONArray();
        for (PlayerAttack next : this.playerAttacks) {
            JSONObject data = next.getData();
            attacksJson.put(data);
        }
        return attacksJson;
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

    public boolean getInvincible() {
        return this.invincible;
    }

    public int getIframes() {
        return this.iframes;
    }

    public boolean getCanAttack() {
        return this.canAttack;
    }

    public int getLockoutTicks() {
        return this.lockoutTicks;
    }

    public boolean getCanSpell() {
        return this.canSpell;
    }

    public int getSpellLockoutTicks() {
        return this.spellLockoutTicks;
    }

    //Setters for testing purposes:
    public void addPlayerAttack(PlayerAttack pa) {
        this.playerAttacks.add(pa);
    }
}
