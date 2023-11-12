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

    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 40);

    public GameOverPanel(BossFight bf) {
        setPreferredSize(WINDOW);
        setMinimumSize(WINDOW);
        setMaximumSize(WINDOW);
        setBackground(Color.BLACK);
        this.bf = bf;
        this.bh = new ButtonHandler();
        addText();
        addButtons();
    }

    private void addText() {

    }

    private void addButtons() {
        JButton replay = createButton("Replay", "replay", this.bh);
        JButton quit = createButton("Quit", "quit", this.bh);
        add(replay);
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
                case "replay":
                    bf.setVisible(false);
                    new BossFight();
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
