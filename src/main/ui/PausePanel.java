package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.System.exit;
import static ui.BossFight.WINDOW;

//Represents a panel that shows the player resume/save/quit options when the game is paused
public class PausePanel extends JPanel {

    private BossFight bf;
    private Game game;
    private ButtonHandler bh;

    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 40);

    public PausePanel(Game g, BossFight bf) {
        setPreferredSize(WINDOW);
        setMinimumSize(WINDOW);
        setMaximumSize(WINDOW);
        setBackground(Color.BLACK);
        this.game = g;
        this.bf = bf;
        this.bh = new ButtonHandler();
        addButtons();
    }

    //EFFECTS: adds a "Resume", "Save and Quit", and "Quit" button to the panel
    //MODIFIES: this
    private void addButtons() {
        JButton resume = createButton("Resume", "resume", this.bh);
        JButton saveQuit = createButton("Save and Quit", "sq", this.bh);
        JButton quit = createButton("Quit", "q", this.bh);
        add(resume);
        add(saveQuit);
        add(quit);
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
