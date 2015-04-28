import network.Node;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("enter an integer");
        int i = keyboard.nextInt();
        Node n = new Node(i);
        if(i == 1){
            n.start(true);
        }
        else{
            n.start(false);
        }
        int exit = keyboard.nextInt();
        while(exit!=0){
            exit = keyboard.nextInt();
        }
        n.stopNode();
    }
}
