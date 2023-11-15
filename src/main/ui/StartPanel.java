package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static ui.BossFight.WINDOW;

//Represents a panel showing the starting screen of the application
public class StartPanel extends JPanel {

    private static final String INTRO_TEXT = "Boss Fight Game";
    private static final int INTRO_TEXT_HEIGHT = 200;
    private static final int INTRO_TEXT_WIDTH = 800;
    private static final Font BUTTON_FONT = new Font("Arial", Font.PLAIN, 40);
    private static final int MARGIN = 20;

    private BossFight bf;
    private ButtonHandler bh;
    private GridBagConstraints row2;
    private GridBagConstraints row1;

    //Creates a panel that is shown when the application is started
    public StartPanel(BossFight bf) {
        setPreferredSize(WINDOW);
        setMinimumSize(WINDOW);
        setMaximumSize(WINDOW);
        setBackground(Color.BLACK);
        this.bf = bf;
        this.bh = new ButtonHandler();
        setLayout(new GridBagLayout());

        row2 = new GridBagConstraints();
        row2.gridy = 1;
        row2.insets = new Insets(MARGIN, MARGIN,
                0, MARGIN);
        row1 = new GridBagConstraints();
        row1.gridwidth = 2;

        addText();
        addButtons();
        revalidate();
        repaint();
    }

    //EFFECTS: adds the title card to the panel
    //MODIFIES: this
    private void addText() {
        JLabel introText = new JLabel(INTRO_TEXT);
        introText.setPreferredSize(new Dimension(INTRO_TEXT_WIDTH, INTRO_TEXT_HEIGHT));
        introText.setFont(new Font("Arial", Font.BOLD, 80));
        introText.setForeground(Color.WHITE);
        introText.setHorizontalAlignment(JLabel.CENTER);
        add(introText, row1);
    }

    //EFFECTS: adds a "New Game" and a "Load Game" button to the panel
    //MODIFIES: this
    private void addButtons() {
        JButton newGame = createButton("New Game", "new", this.bh);
        JButton loadGame = createButton("Load Game", "load", this.bh);
        add(newGame, row2);
        add(loadGame, row2);
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
                case "new":
                    bf.newGame();
                    setVisible(false);
                    bf.getGamePanel().setVisible(true);
                    bf.startTimer();
                    break;
                case "load":
                    bf.loadGame();
                    setVisible(false);
                    bf.getGamePanel().setVisible(true);
                    bf.startTimer();
                    break;
                default:
                    break;
            }
        }
    }

}
