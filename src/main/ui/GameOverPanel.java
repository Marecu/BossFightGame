package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;
import static ui.BossFight.WINDOW;

//Represents a panel that is shown when the game is over
public class GameOverPanel extends JPanel {

    private BossFight bf;
    private ButtonHandler bh;

    private GridBagConstraints row1;
    private GridBagConstraints row2;

    private String gameOverText = "Game over!";
    private int fontSize = 0;
    private Game game;

    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 40);
    private static final int MARGIN = 20;
    private static final int GO_TEXT_HEIGHT = 200;
    private static final int GO_TEXT_WIDTH = 800;

    //Creates a new panel that is shown when the game is over
    public GameOverPanel(Game g, BossFight bf) {
        this.game = g;
        setPreferredSize(WINDOW);
        setMinimumSize(WINDOW);
        setMaximumSize(WINDOW);
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        row1 = new GridBagConstraints();
        row1.gridwidth = 2;
        row2 = new GridBagConstraints();
        row2.gridy = 1;
        row2.insets = new Insets(MARGIN, MARGIN,
                0, MARGIN);
        this.bf = bf;
        this.bh = new ButtonHandler();
        addButtons();
    }

    //EFFECTS: sets the text shown to match whether the player won or lost
    //MODIFIES: this
    public void pullText() {
        this.gameOverText = this.game.getPlayer().getHP() <= 0 ? "You died!" :
                "Congratulations! You defeated the boss.";
        this.fontSize = this.game.getPlayer().getHP() <= 0 ? 80 : 41;
        addText();
    }

    //EFFECTS: adds the game over text to the panel
    //MODIFIES: this
    private void addText() {
        JLabel introText = new JLabel(gameOverText);
        introText.setPreferredSize(new Dimension(GO_TEXT_WIDTH, GO_TEXT_HEIGHT));
        introText.setFont(new Font("Arial", Font.BOLD, this.fontSize));
        introText.setForeground(Color.WHITE);
        introText.setHorizontalAlignment(JLabel.CENTER);
        add(introText, row1);
    }

    //EFFECTS: adds a replay and quit button to the panel
    //MODIFIES: this
    private void addButtons() {
        JButton replay = createButton("Replay", "replay", this.bh);
        JButton quit = createButton("Quit", "quit", this.bh);
        add(replay, row2);
        add(quit, row2);
    }

    // EFFECTS: Returns a pre-templated button
    private JButton createButton(String text, String action, ButtonHandler handler) {
        JButton button = new JButton(text);
        button.setActionCommand(action);
        button.addActionListener(handler);
        button.setBackground(Color.GRAY);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        button.setFont(BUTTON_FONT);
        button.setContentAreaFilled(false);
        button.setPreferredSize(new Dimension(400, 150));
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setRolloverEnabled(false);
        return button;
    }

    /*
     * A button handler to respond to button events
     */
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "replay":
                    bf.newGame();
                    bf.getGamePanel().setVisible(true);
                    setVisible(false);
                    break;
                case "quit":
                    exit(0);
                    break;
                default:
                    break;
            }
        }
    }

}
