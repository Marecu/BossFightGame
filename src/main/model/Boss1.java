package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//Represents the first boss the player will fight with specified attacks and attributes

public class Boss1 extends Boss {

    private static final int BEAM_HEIGHT = 30;
    private static final int TP_HEIGHT = 100;

    private int beamTimer;

    //Creates a boss1 with the specified data
    public Boss1(double x, double y, Player p, int hp, double speedY, int facing, int attackTimer, int bonusMoveSpeed,
                 boolean currentlyAttacking, boolean movementOverride, List<BossAttack> bossAttacks) {
        this.posX = x;
        this.posY = y;
        this.player = p;
        this.hp = hp;
        this.speedY = speedY;
        this.facing = facing;
        this.attackTimer = attackTimer;
        this.bonusMoveSpeed = bonusMoveSpeed;
        this.currentlyAttacking = currentlyAttacking;
        this.movementOverride = movementOverride;
        this.bossAttacks = bossAttacks;
    }

    //Creates a new boss1 at the specified coordinates that attacks the specified player p
    public Boss1(double x, double y, Player p) {
        this.posX = x;
        this.posY = y;
        this.player = p;
        this.hp = this.STARTING_HP;
        this.speedY = 0;
        this.facing = 1;
        this.attackTimer = this.ATTACK_INTERVAL;
        this.bonusMoveSpeed = 0;
        this.currentlyAttacking = false;
        this.movementOverride = false;
        this.bossAttacks = new ArrayList<>();
    }

    @Override
    //EFFECTS: causes the boss to issue Attack 1: Run fast across the screen towards the player
    //MODIFIES: this
    void attack1(Player p) {
        this.currentlyAttacking = true;
        this.movementOverride = true;
        this.bonusMoveSpeed = 30;
        System.out.println("The boss is using Attack 1: Charge!");
    }

    @Override
    //EFFECTS: causes the boss to issue Attack 2: Teleport above the player and fall down on them
    //MODIFIES: this
    void attack2(Player p) {
        this.currentlyAttacking = true;
        this.posX = p.getX();
        this.posY = this.TP_HEIGHT;
        System.out.println("The boss is using Attack 2: Plunge!");
    }

    @Override
    //EFFECTS: causes this boss to issue Attack 3: Create a laser that goes across the screen
    //MODIFIES: this
    void attack3(Player p) {
        this.currentlyAttacking = true;
        int width;
        double posXOffset;
        if (facing == 1) {
            width = 800 - (int)this.posX - this.WIDTH;
            posXOffset = this.posX + this.WIDTH;
        } else {
            width = (int)this.posX;
            posXOffset = 0;
        }
        BossAttack beam = new BossAttack(width, this.BEAM_HEIGHT, posXOffset, this.posY);
        this.bossAttacks.add(beam);
        this.beamTimer = 3;
        System.out.println("The boss is using Attack 3: Beam!");
    }

    @Override
    //EFFECTS: handles attacks that still need to be finished
    //MODIFIES: this
    //REQUIRES: beamTimer >= 0, 1 <= lastUsedAttack <= 3
    void handleLingeringAttacks(Player p) {
        if (this.currentlyAttacking) {
            switch (this.lastUsedAttack) {
                case 1:
                    handleAttack1();
                    break;
                case 2:
                    handleAttack2();
                    break;
                case 3:
                    handleAttack3();
                    break;
            }
            this.attackTimer = this.ATTACK_INTERVAL;
        }
    }

    @Override
    //returns a JSON object containing all the data about the boss
    public JSONObject getData() {
        JSONObject bossData = new JSONObject();
        bossData.put("posX", this.posX);
        bossData.put("posY", this.posY);
        bossData.put("hp", this.hp);
        bossData.put("speedY", this.speedY);
        bossData.put("facing", this.facing);
        bossData.put("attackTimer", this.attackTimer);
        bossData.put("bonusMoveSpeed", this.bonusMoveSpeed);
        bossData.put("currentlyAttacking", this.currentlyAttacking);
        bossData.put("movementOverride", this.movementOverride);
        bossData.put("bossAttacks", parseAttacksJson());
        return bossData;
    }

    //EFFECTS: parses the playerAttacks array into a JSON array with the appropriate data
    private JSONArray parseAttacksJson() {
        JSONArray attacksJson = new JSONArray();
        for (BossAttack next : this.bossAttacks) {
            JSONObject data = next.getData();
            attacksJson.put(data);
        }
        return attacksJson;
    }

    //EFFECTS: if the boss has hit the edge of the screen, removes bonus speed and resumes normal movement
    //MODIFIES: this
    void handleAttack1() {
        if (this.posX <= 0 || this.posX >= (800 - Math.min(this.WIDTH, 700))) {
            this.bonusMoveSpeed = 0;
            this.movementOverride = false;
            this.currentlyAttacking = false;
        }
    }

    //EFFECTS: resumes the attack cycle if the boss has returned to the ground
    //MODIFIES: this
    void handleAttack2() {
        if (this.posY == 600 - this.HEIGHT) {
            this.currentlyAttacking = false;
        }
    }

    //EFFECTS: if the beam has expired, removes the beam, otherwise decrements the beam timer
    //MODIFIES: this
    void handleAttack3() {
        if (this.beamTimer == 0) {
            this.bossAttacks.remove(0);
            this.currentlyAttacking = false;
        } else {
            this.beamTimer--;
        }
    }

    public int getBeamTimer() {
        return this.beamTimer;
    }
}
