package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.lang.System.exit;
import static ui.BossFight.WINDOW;

//Represents a panel that shows the player resume/save/quit options when the game is paused
public class PausePanel extends JPanel {

    private BossFight bf;
    private Game game;
    private ButtonHandler bh;
    private GridBagConstraints row1;
    private GridBagConstraints row2;
    private GridBagConstraints row3;

    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 40);
    private static final int MARGIN = 20;

    //Creates a new panel that is shown when the game is paused
    public PausePanel(Game g, BossFight bf) {
        setPreferredSize(WINDOW);
        setMinimumSize(WINDOW);
        setMaximumSize(WINDOW);
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        row1 = new GridBagConstraints();
        row2 = new GridBagConstraints();
        row2.gridy = 1;
        row2.insets = new Insets(MARGIN, 0,
                MARGIN, 0);
        row3 = new GridBagConstraints();
        row3.gridy = 2;

        this.game = g;
        this.bf = bf;
        this.bh = new ButtonHandler();
        addButtons();
        addKeyListeners();
    }

    //EFFECTS: allows "`" to be pressed to return to the game
    //MODIFIES: this
    private void addKeyListeners() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_BACK_QUOTE) {
                    game.unpause();
                    bf.unpause();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    //EFFECTS: adds a "Resume", "Save and Quit", and "Quit" button to the panel
    //MODIFIES: this
    private void addButtons() {
        JButton resume = createButton("Resume", "resume", this.bh);
        JButton saveQuit = createButton("Save and Quit", "sq", this.bh);
        JButton quit = createButton("Quit", "q", this.bh);
        add(resume, row1);
        add(saveQuit, row2);
        add(quit, row3);
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
                case "resume":
                    game.unpause();
                    bf.unpause();
                    break;
                case "sq":
                    bf.saveGame(game);
                    exit(0);
                    break;
                case "q":
                    exit(0);
                    break;
                default:
                    break;
            }
        }
    }

}
