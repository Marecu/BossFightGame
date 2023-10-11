package ui;

import model.BossAttack;
import model.Game;
import model.PlayerAttack;

import java.util.Scanner;

public class BossFight {

    public BossFight() {
        Game game = new Game();
        runGame(game);
    }

    public static void main(String[] args) {
        new BossFight();
    }

    void runGame(Game g) {
        while (true) {
            promptPlayer(g);
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
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter a move (a, d, jump, attack, spell):");
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
}
