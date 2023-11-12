package ui;

import model.BossAttack;
import model.Game;
import model.PlayerAttack;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import static java.lang.System.exit;


public class BossFight extends JFrame {

    private static final String FILE_PATH = "./data/gameData.json";
    public static final int FLOOR_HEIGHT = 75;
    public static final Dimension WINDOW = new Dimension(Game.WIDTH, Game.HEIGHT + FLOOR_HEIGHT);
    private static final int TIME_STEP = 10;
    private Scanner scan;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Game game;
    private Set<Integer> keys;
    private StartPanel sp;
    private GamePanel gp;
    private PausePanel pp;
    private GameOverPanel gop;

    public BossFight() {
        super("Boss Fight Game");
        this.scan = new Scanner(System.in);
        this.jsonWriter = new JsonWriter(FILE_PATH);
        this.jsonReader = new JsonReader(FILE_PATH);
        this.keys = new HashSet<>();
        DefaultKeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(new KeyHandler());
        addWindowListener(new WindowHandler());
        setResizable(false);
        setPreferredSize(WINDOW);
        setMinimumSize(WINDOW);
        setMaximumSize(WINDOW);
        centreScreen();
        setVisible(true);
        addPanels();
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    }

    public static void main(String[] args) {
        new BossFight();
    }

    private void addPanels() {
        sp = new StartPanel(this);
        gop = new GameOverPanel(this);
        add(sp);
        add(gop);
        gop.setVisible(false);
        sp.setVisible(true);
    }

    //EFFECTS: moves the window to the centre of the screen
    //MODIFIES: this
    private void centreScreen() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screen.width - getWidth()) / 2, ((screen.height - getHeight()) / 2) - 25);
    }

    //EFFECTS: adds a timer to the game that updates every TIME_STEP ms
    //Created largely based on B02-SpaceInvadersBase
    public void startTimer() {
        Timer t = new Timer(TIME_STEP, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (game.getPause()) {
                    handlePause();
                } else {
                    game.update(keys);
                    if (game.isGameOver()) {
                        handleGameOver();
                        System.out.println("Game over");
                    }
                    printGameStatus(game);
                    gp.repaint();
                }

            }
        });

        t.start();
    }

    private class KeyHandler implements KeyEventPostProcessor {
        @Override
        // EFFECTS: Adds key presses to the list of keys being pressed + removes keys no longer being pressed
        public boolean postProcessKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_RELEASED) {
                if (keys.contains(e.getKeyCode())) {
                    keys.remove(e.getKeyCode());
                }
                return true;
            }
            if (e.getID() != KeyEvent.KEY_PRESSED) {
                return true;
            }
            keys.add(e.getKeyCode());
            return true;
        }
    }

    private class WindowHandler extends WindowAdapter {
        @Override
        // EFFECTS: Handles the closing of the window, prompting the user to save their game
        // MODIFIES: this
        public void windowClosing(WindowEvent e) {
            // DO STUFF, EXIT WITH System.exit(<exit code>)
        }
    }

    private void handlePause() {
        gp.setVisible(false);
        pp.setVisible(true);
    }

    public void unpause() {
        pp.setVisible(false);
        gp.setVisible(true);
    }

    private void handleGameOver() {
        gp.setVisible(false);
        gop.setVisible(true);
    }

    //SHOULD NOT BE NECESSARY ANYMORE
    //void promptPlayer(Game g) {
    //    System.out.println("Enter a move (a, d, jump, attack, spell, save):");
    //    String userInput = scan.nextLine();
    //    g.update(userInput);
    //}

    void printGameStatus(Game g) {
        System.out.println("You are located at: [" + g.getPlayer().getX() + ", " +  g.getPlayer().getY() + "]");
        System.out.println("Your speed is: [" + g.getPlayer().getSpeedX() + ", " + g.getPlayer().getSpeedY() + "]");
        System.out.println("Your HP is: " + g.getPlayer().getHP());
        System.out.println("Your attack counter is (3 hits req for spell): " + g.getPlayer().getTotalHits());
        System.out.print("Your attacks are located at: ");
        for (PlayerAttack next : g.getPlayer().getPlayerAttacks()) {
            System.out.print("[" + next.getX() + ", " + next.getY() + "], ");
        }
        System.out.println();
        System.out.println("The boss is located at: [" + g.getBoss().getX() + ", " + g.getBoss().getY() + "]");
        System.out.println("The boss' HP is: " + g.getBoss().getHP());
        System.out.print("The boss' attacks are located at: ");
        for (BossAttack next : g.getBoss().getBossAttacks()) {
            System.out.print("[" + next.getX() + ", " + next.getY() + "], ");
        }
        System.out.println();
        System.out.println("The boss can attack in: " + g.getBoss().getAttackTimer() + " turns");
    }

    void newGame() {
        this.game = new Game();
        addGameRelevantPanels();
    }

    void addGameRelevantPanels() {
        gp = new GamePanel(this.game);
        pp = new PausePanel(this.game, this);
        add(gp);
        add(pp);
        gp.setVisible(false);
        pp.setVisible(false);
    }

    void saveGame(Game g) {
        try {
            jsonWriter.open();
            jsonWriter.write(this.game);
            jsonWriter.close();
            System.out.println("Game saved successfully");
        } catch (FileNotFoundException e) {
            System.out.println("Cannot write to the given file path");
        }
    }

    void loadGame() {
        try {
            this.game = jsonReader.read();
            addGameRelevantPanels();
            System.out.println("Game loaded successfully");
        } catch (IOException e) {
            System.out.println("Cannot read from saved file");
        }
    }

    //SHOULD NOT NEED ANYMORE
    boolean promptLoad() {
        System.out.println("Do you want to load a saved game? (Y/N)");
        String userInput = scan.nextLine();
        return (userInput.equals("Y"));
    }

    public JPanel getGamePanel() {
        return this.gp;
    }
}
