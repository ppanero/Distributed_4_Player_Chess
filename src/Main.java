import network.GameNode;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter an integer");
        int i = keyboard.nextInt();
        GameNode n = new GameNode(i);
        if(i == 1){
            n.startNode(true);
        }
        else{
            n.startNode(false);
        }
        int exit = keyboard.nextInt();
        while(exit!=0){
            exit = keyboard.nextInt();
        }
        n.stopNode();
    }
}
