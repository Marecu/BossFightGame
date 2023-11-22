package ui;

import model.*;

import javax.swing.*;
import java.awt.*;

import java.io.IOException;
import java.util.List;

import static java.lang.System.exit;
import static ui.BossFight.WINDOW;

//Represents a panel that shows the game when it is being actively played
//CITATION: largely based on B02-SpaceInvadersBase from lecture
public class GamePanel extends JPanel {

    private static final String SPRITE_SHEET_FILE = "spriteSheet.png";
    private static final int ANIMATION_FREQUENCY = 12;
    private static final int ANIMATION_FRAMES = 4;

    private JLabel manaLabel;

    private Game game;
    private SpriteSheet spriteSheet;
    private Sprite playerHP;
    private Sprite manaBarFrame;
    private Sprite manaBarFrameAlt;
    private Sprite manaBar;
    private Sprite bossBarFrame;
    private Sprite bossBar;
    private Sprite playerWalk1;
    private Sprite playerWalk2;
    private Sprite playerNeutral;
    private Sprite playerAttack;
    private Sprite basicAttack;
    private Sprite missile;
    private Sprite bossBeam;
    private Sprite bossWalk1;
    private Sprite bossWalk2;
    private Sprite bossNeutral;
    private Sprite bossVisor1;
    private Sprite bossVisor2;
    private Sprite bossVisor3;
    private Sprite background;
    private Sprite ground;

    private int animationCounter;
    private int animationFrame;

    //Creates a new GamePanel representing the game g
    public GamePanel(Game g) {
        setPreferredSize(WINDOW);
        setMinimumSize(WINDOW);
        setMaximumSize(WINDOW);
        setBackground(Color.WHITE);
        setLayout(null);
        loadSpriteSheet();
        addManaLabel();
        this.game = g;
        this.animationCounter = 0;
        this.animationFrame = 0;
    }

    //EFFECTS: returns the file path where the spreadsheet is located
    private String getSpriteSheetPath() {
        String separator = System.getProperty("file.separator");
        if (BossFight.JAR_COMPILE) {
            return SPRITE_SHEET_FILE;
        } else {
            return "." + separator + "data" + separator + SPRITE_SHEET_FILE;
        }
    }

    //EFFECTS: loads in the full sprite sheet from the file and loads the individual sprites
    //MODIFIES: this
    private void loadSpriteSheet() {
        try {
            this.spriteSheet = new SpriteSheet(getSpriteSheetPath());
            loadPlayerAssets();
            loadBossAssets();
            loadGuiAssets();
            loadAmbientArtAssets();
        } catch (IOException e) {
            System.out.println("Failed to load assets.");
            exit(0);
        }
    }

    //EFFECTS: loads all assets related to the player
    //MODIFIES: this
    private void loadPlayerAssets() {
        this.playerWalk1 = new Sprite(spriteSheet.splice(0, 0, 35, 69));
        this.playerNeutral = new Sprite(spriteSheet.splice(36, 0, 35, 69));
        this.playerWalk2 = new Sprite(spriteSheet.splice(72, 0, 35, 69));
        this.playerAttack = new Sprite(spriteSheet.splice(108, 0, 35, 69));
        this.basicAttack = new Sprite(spriteSheet.splice(0, 70, 75, 20));
        this.missile = new Sprite(spriteSheet.splice(76, 70, 30, 10));
    }

    //EFFECTS: loads all assets related to the boss
    //MODIFIES: this
    private void loadBossAssets() {
        this.bossBeam = new Sprite(spriteSheet.splice(144, 26, 1, 30));
        this.bossWalk1 = new Sprite(spriteSheet.splice(70, 396, 69, 116));
        this.bossWalk2 = new Sprite(spriteSheet.splice(0, 396, 69, 116));
        this.bossNeutral = new Sprite(spriteSheet.splice(140, 396, 69, 116));
        this.bossVisor1 = new Sprite(spriteSheet.splice(93, 81, 16, 4));
        this.bossVisor2 = new Sprite(spriteSheet.splice(110, 81, 16, 4));
        this.bossVisor3 = new Sprite(spriteSheet.splice(76, 81, 16, 4));
    }

    //EFFECTS: Loads all assets related to the GUI
    //MODIFIES: this
    private void loadGuiAssets() {
        this.playerHP = new Sprite(spriteSheet.splice(144, 0, 25, 25));
        this.manaBarFrame = new Sprite(spriteSheet.splice(0, 91, 150, 60));
        this.manaBarFrameAlt = new Sprite(spriteSheet.splice(0, 152, 150, 60));
        this.bossBarFrame = new Sprite(spriteSheet.splice(0, 254, 550, 95));
    }

    //EFFECTS: Loads the background and ground images
    //MODIFIES: this
    private void loadAmbientArtAssets() {
        this.background = new Sprite(spriteSheet.splice(0, 513, 1250, 550));
        this.ground = new Sprite(spriteSheet.splice(0, 1064, 1250, 75));
    }

    @Override
    //EFFECTS: paints the frame
    //MODIFIES: this
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        drawGame(gr);
    }

    //EFFECTS: draws all game-relevant things onto the frame
    //MODIFIES: this
    private void drawGame(Graphics gr) {
        drawBackground(gr);
        drawGround(gr);
        drawPlayer(gr);
        drawPlayerAttacks(gr);
        drawBoss(gr);
        drawBossAttacks(gr);
        drawGUI(gr);
    }

    //EFFECTS: advances the timer the animations run on
    //MODIFIES: this
    public void tickAnimation() {
        if (this.animationCounter == ANIMATION_FREQUENCY) {
            this.animationCounter = 0;
            updateAnimation();
        } else {
            this.animationCounter += 1;
        }
    }

    //EFFECTS: advances the animation by a frame
    //MODIFIES: this
    private void updateAnimation() {
        if (this.animationFrame == ANIMATION_FRAMES) {
            this.animationFrame = 1;
        } else {
            this.animationFrame += 1;
        }
    }

    //EFFECTS: draws the player
    //MODIFIES: this
    private void drawPlayer(Graphics gr) {
        Player p = game.getPlayer();
        Sprite sprite = getPlayerFrame(p);
        if (p.getFacing() == 1) {
            gr.drawImage(sprite.getSprite(), (int) p.getX() - Player.HORIZONAL_HITBOX_INSET,
                    (int) p.getY() - Player.VERTICAL_HITBOX_INSET, this);
        } else {
            gr.drawImage(sprite.getSprite(), (int) p.getX() + (int) p.getWidth() + Player.HORIZONAL_HITBOX_INSET,
                    (int) p.getY(), -1 * ((int) p.getWidth() + (2 * Player.HORIZONAL_HITBOX_INSET)),
                    (int) p.getHeight() + (2 * Player.VERTICAL_HITBOX_INSET), this);
        }
    }

    //EFFECTS: returns the correct frame in the animation cycle for the player
    private Sprite getPlayerFrame(Player p) {
        if (!p.getCanAttack() || !p.getCanSpell()) {
            return this.playerAttack;
        }
        if (!p.onGround()) {
            return this.playerWalk2;
        }
        if (Math.abs(p.getSpeedX()) < 1) {
            return this.playerNeutral;
        }
        switch (this.animationFrame) {
            case 1:
                return this.playerWalk1;
            case 3:
                return this.playerWalk2;
            default:
                return this.playerNeutral;
        }
    }

    //EFFECTS: draws the player's attacks
    //MODIFIES: this
    private void drawPlayerAttacks(Graphics gr) {
        List<PlayerAttack> playerAttacks = game.getPlayer().getPlayerAttacks();
        for (PlayerAttack next : playerAttacks) {
            drawPlayerAttack(gr, next);
        }
    }

    //EFFECTS: draws an individual player attack
    //MODIFIES: this
    private void drawPlayerAttack(Graphics gr, PlayerAttack pa) {
        if (pa.getMoving()) {
            gr.drawImage(this.missile.getSprite(), (int) pa.getX(), (int) pa.getY(), this);
        } else {
            if (pa.getFacing() == 1) {
                gr.drawImage(this.basicAttack.getSprite(), (int) pa.getX(), (int) pa.getY(), this);
            } else {
                gr.drawImage(this.basicAttack.getSprite(), (int) pa.getX() + pa.getWidth(), (int) pa.getY(),
                        -1 * pa.getWidth(), pa.getHeight(), this);
            }
        }
    }

    //EFFECTS: draws the boss
    //MODIFIES: this
    private void drawBoss(Graphics gr) {
        Boss b = game.getBoss();
        Sprite sprite = getBossFrame(b);
        if (b.getFacing() == 1) {
            gr.drawImage(sprite.getSprite(), (int) b.getX() - Boss.HORIZONTAL_HITBOX_INSET, (int) b.getY(), this);
        } else {
            gr.drawImage(sprite.getSprite(), (int) b.getX() + b.getWidth() + Boss.HORIZONTAL_HITBOX_INSET,
                    (int) b.getY(), -1 * (b.getWidth() + (2 * Boss.HORIZONTAL_HITBOX_INSET)), b.getHeight(), this);
        }
        if (b.getAttackTimer() <= Boss.TELEGRAPH_DELAY) {
            drawBossTelegraph(gr, b);
        }
    }

    //EFFECTS: returns the correct frame in the animation cycle for the boss
    private Sprite getBossFrame(Boss b) {
        if (!b.onGround()) {
            return this.bossWalk2;
        }
        if (b.getBonusMoveSpeed() == -1 * Boss.BASE_MOVE_SPEED) {
            return this.bossNeutral;
        }
        switch (this.animationFrame) {
            case 1:
                return this.bossWalk1;
            case 3:
                return this.bossWalk2;
            default:
                return this.bossNeutral;
        }
    }

    //EFFECTS: draws the appropriate telegraph if the boss is about to attack
    //MODIFIES: this
    private void drawBossTelegraph(Graphics gr, Boss b) {
        Sprite telegraph;
        switch (b.getLastUsedAttack()) {
            case 1:
                telegraph = this.bossVisor1;
                break;
            case 2:
                telegraph = this.bossVisor2;
                break;
            default:
                telegraph = this.bossVisor3;
                break;
        }
        int frameOffset = getFrameOffset(b);
        if (b.getFacing() == 1) {
            gr.drawImage(telegraph.getSprite(), (int) b.getX() + 24 - Boss.HORIZONTAL_HITBOX_INSET + frameOffset,
                    (int) b.getY() + 11, this);
        } else {
            gr.drawImage(telegraph.getSprite(), (int) b.getX() + 24 - Boss.HORIZONTAL_HITBOX_INSET - frameOffset,
                    (int) b.getY() + 11, -1 * telegraph.getSprite().getWidth(), telegraph.getSprite().getHeight(),
                    this);
        }
    }

    //EFFECTS: returns the offset in pixels to correctly place the boss' telegraph crystal
    private int getFrameOffset(Boss b) {
        int facingOffset = 0;
        if (b.getFacing() == -1) {
            facingOffset = -22;
        }
        if (getBossFrame(b) == this.bossNeutral) {
            return 2 + facingOffset;
        } else if (getBossFrame(b) == this.bossWalk2) {
            return 1 + facingOffset;
        } else {
            return 0 + facingOffset;
        }
    }

    //EFFECTS: draws the boss' attacks
    //MODIFIES: this
    private void drawBossAttacks(Graphics gr) {
        List<BossAttack> bossAttacks = this.game.getBoss().getBossAttacks();
        for (BossAttack next : bossAttacks) {
            drawBossAttack(gr, next);
        }
    }

    //EFFECTS: draws an individual boss attack
    //MODIFIES: this
    private void drawBossAttack(Graphics gr, BossAttack ba) {
        for (int i = 0; i < ba.getWidth(); i++) {
            gr.drawImage(this.bossBeam.getSprite(), (int) ba.getX() + i, (int) ba.getY(), this);
        }
    }

    //EFFECTS: draws the background
    //MODIFIES: this
    private void drawBackground(Graphics gr) {
        gr.drawImage(this.background.getSprite(), 0, 0, this);
    }

    //EFFECTS: draws the ground of the arena
    //MODIFIES: this
    private void drawGround(Graphics gr) {
        gr.drawImage(this.ground.getSprite(), 0, Game.HEIGHT, this);
    }

    //EFFECTS: draws the player HP, player mana, and boss HP
    //MODIFIES: this
    private void drawGUI(Graphics gr) {
        drawBossBar(gr);
        drawHP(gr);
        drawManaBar(gr);
        revalidate();
        repaint();
    }

    //EFFECTS: draws a boss bar representing the boss' health onto the panel
    //MODIFIES: this
    private void drawBossBar(Graphics gr) {
        gr.drawImage(bossBarFrame.getSprite(), (Game.WIDTH - bossBarFrame.getSprite().getWidth()) / 2, 25, this);
        double hpPercentage = (double) this.game.getBoss().getHP() / (double) Boss.STARTING_HP;
        this.bossBar = new Sprite(spriteSheet.splice(0, 350, (int) (500 * hpPercentage), 45));
        gr.drawImage(bossBar.getSprite(), (Game.WIDTH - bossBarFrame.getSprite().getWidth()) / 2 + 25, 50, this);
    }

    //EFFECTS: draws a number of hearts equal to the player's remaining HP onto the panel
    //MODIFIES: this
    private void drawHP(Graphics gr) {
        int hp = game.getPlayer().getHP();
        for (int i = 0; i < hp; i++) {
            gr.drawImage(playerHP.getSprite(), (25 * (i + 1)) + (i * (playerHP.getSprite().getWidth() + 9)), 20,
                    (int) (playerHP.getSprite().getWidth() * 1.5), (int) (playerHP.getSprite().getHeight() * 1.5),
                    this);
        }
    }

    //EFFECTS: draws the mana bar onto the panel
    //MODIFIES: this
    private void drawManaBar(Graphics gr) {
        double manaPercentage = (double) this.game.getPlayer().getTotalHits() / (double) Player.SPELL_REQUIRED_HITS;
        if (manaPercentage >= 1) {
            gr.drawImage(manaBarFrameAlt.getSprite(), 25, 60, this);
        } else {
            gr.drawImage(manaBarFrame.getSprite(), 25, 60, this);
        }
        if (manaPercentage > 0) {
            this.manaBar = new Sprite(spriteSheet.splice(0, 213, Math.min((int) (130 * manaPercentage), 130), 40));
            gr.drawImage(manaBar.getSprite(), 35, 70, this);
        }
        if (manaPercentage >= 2) {
            int numSpells = (int) Math.floor(manaPercentage);
            this.manaLabel.setText("x" + numSpells);
        } else {
            this.manaLabel.setText("");
        }
    }

    //EFFECTS: adds a label beside the mana bar indicating if the player can cast more than one spell
    //MODIFIES: this
    private void addManaLabel() {
        this.manaLabel = new JLabel("");
        this.manaLabel.setPreferredSize(new Dimension(50, 25));
        this.manaLabel.setFont(new Font("Arial", Font.BOLD, 25));
        this.manaLabel.setForeground(Color.WHITE);
        this.manaLabel.setBounds(25 + this.manaBarFrame.getSprite().getWidth() + 10,
                60 + (this.manaBarFrame.getSprite().getHeight() - 20) / 2, 50, 20);
        add(this.manaLabel);
    }

}
