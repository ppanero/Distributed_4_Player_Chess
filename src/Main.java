import network.FDNode;
import network.GameNode;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter an integer");
        int i = keyboard.nextInt();
        FDNode n = new FDNode(i);
            n.start();

        int exit = keyboard.nextInt();
        while(exit!=0){
            exit = keyboard.nextInt();
        }
        n.stopNode();
    }
}
