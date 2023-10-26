package ui;

import model.BossAttack;
import model.Game;
import model.PlayerAttack;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class BossFight {

    private static final String FILE_PATH = "./data/gameData.json";
    private Scanner scan;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Game game;

    public BossFight() {
        this.scan = new Scanner(System.in);
        this.jsonWriter = new JsonWriter(FILE_PATH);
        this.jsonReader = new JsonReader(FILE_PATH);
        if (promptLoad()) {
            loadGame();
        } else {
            this.game = new Game();
        }
        runGame(game);
    }

    public static void main(String[] args) {
        new BossFight();
    }

    void runGame(Game g) {
        while (true) {
            promptPlayer(g);
            if (g.getSave()) {
                saveGame(g);
                return;
            }
            printGameStatus(g);
            if (g.isGameOver()) {
                if (g.getPlayer().getHP() <= 0) {
                    System.out.println("You died!");
                } else {
                    System.out.println("You win!");
                }
                return;
            }
        }
    }

    void promptPlayer(Game g) {
        System.out.println("Enter a move (a, d, jump, attack, spell, save):");
        String userInput = scan.nextLine();
        g.update(userInput);
    }

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
            System.out.println("Game loaded successfully");
        } catch (IOException e) {
            System.out.println("Cannot read from saved file");
        }
    }

    boolean promptLoad() {
        System.out.println("Do you want to load a saved game? (Y/N)");
        String userInput = scan.nextLine();
        return (userInput.equals("Y"));
    }
}
