package ui;

import model.*;

import javax.swing.*;
import java.awt.*;

import java.util.List;

import static ui.BossFight.WINDOW;

//Represents a panel that shows the game when it is being actively played
public class GamePanel extends JPanel {

    private static final Color PLAYER_COLOR = new Color(140, 133, 166);
    private static final Color PLAYER_INVINCIBLE_COLOR = new Color(171, 173, 14);
    private static final Color BOSS_COLOR = new Color(237, 133, 50);
    private static final Color BOSS_INVINCIBLE_COLOR = new Color(250, 100, 150);
    private static final Color PLAYER_ATTACK_COLOR = new Color(150, 170, 250);
    private static final Color BOSS_ATTACK_COLOR = new Color(250, 50, 80);

    private JLabel hpLabel;
    private JLabel bossHPLabel;
    private JLabel hitsLabel;

    private Game game;

    public GamePanel(Game g) {
        setPreferredSize(WINDOW);
        setMinimumSize(WINDOW);
        setMaximumSize(WINDOW);
        setBackground(Color.GRAY);
        this.game = g;
    }

    @Override
    protected void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        drawGame(gr);
    }

    private void drawGame(Graphics gr) {
        drawPlayer(gr);
        drawPlayerAttacks(gr);
        drawBoss(gr);
        drawBossAttacks(gr);
        drawGround(gr);
        drawGUI();
    }

    private void drawPlayer(Graphics gr) {
        Player p = game.getPlayer();
        gr.setColor(game.getPlayer().getInvincible() ? PLAYER_INVINCIBLE_COLOR : PLAYER_COLOR);
        gr.fillRect((int) p.getX(), (int) p.getY(), (int) p.PLAYER_WIDTH, (int) p.PLAYER_HEIGHT);
    }

    private void drawPlayerAttacks(Graphics gr) {
        List<PlayerAttack> playerAttacks = game.getPlayer().getPlayerAttacks();
        for (PlayerAttack next : playerAttacks) {
            drawPlayerAttack(gr, next);
        }
    }

    private void drawPlayerAttack(Graphics gr, PlayerAttack pa) {
        gr.setColor(PLAYER_ATTACK_COLOR);
        gr.fillRect((int) pa.getX(), (int) pa.getY(), pa.getWidth(), pa.getHeight());
    }

    private void drawBoss(Graphics gr) {
        Boss b = game.getBoss();
        gr.setColor(game.getBoss().getInvincible() ? BOSS_INVINCIBLE_COLOR : BOSS_COLOR);
        gr.fillRect((int) b.getX(), (int) b.getY(), b.getWidth(), b.getHeight());
    }

    private void drawBossAttacks(Graphics gr) {
        List<BossAttack> bossAttacks = game.getBoss().getBossAttacks();
        for (BossAttack next : bossAttacks) {
            drawBossAttack(gr, next);
        }
    }

    private void drawBossAttack(Graphics gr, BossAttack ba) {
        gr.setColor(BOSS_ATTACK_COLOR);
        gr.fillRect((int) ba.getX(), (int) ba.getY(), ba.getWidth(), ba.getHeight());
    }

    private void drawGround(Graphics gr) {
        gr.setColor(new Color(0, 0, 0));
        gr.fillRect(0, Game.HEIGHT, Game.WIDTH, BossFight.FLOOR_HEIGHT);
    }

    private void drawGUI() {
        if (hpLabel == null) {
            instantiateGUI();
        }
        hpLabel.setText("HP: " + game.getPlayer().getHP());
        bossHPLabel.setText("Boss HP: " + game.getBoss().getHP());
        hitsLabel.setText("Mana: " + game.getPlayer().getTotalHits());
        revalidate();
        repaint();
    }

    private void instantiateGUI() {
        hpLabel = new JLabel("HP:" + game.getPlayer().getHP());
        hpLabel.setPreferredSize(new Dimension(200, 30));
        hpLabel.setFont(new Font("Arial", Font.BOLD, 30));
        hpLabel.setForeground(Color.WHITE);
        hpLabel.setHorizontalAlignment(JLabel.LEFT);
        add(hpLabel);
        bossHPLabel = new JLabel("Boss HP: " + game.getBoss().getHP());
        bossHPLabel.setPreferredSize(new Dimension(200, 30));
        bossHPLabel.setFont(new Font("Arial", Font.BOLD, 30));
        bossHPLabel.setForeground(Color.WHITE);
        bossHPLabel.setHorizontalAlignment(JLabel.CENTER);
        add(bossHPLabel);
        hitsLabel = new JLabel("Mana: " + game.getPlayer().getTotalHits());
        hitsLabel.setPreferredSize(new Dimension(200, 30));
        hitsLabel.setFont(new Font("Arial", Font.BOLD, 30));
        hitsLabel.setForeground(Color.WHITE);
        hitsLabel.setHorizontalAlignment(JLabel.CENTER);
        add(hitsLabel);
        revalidate();
        repaint();
    }
}
