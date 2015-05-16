package game.control;

import game.graphics.GraphicInterface;
import game.pieces.Move;
import network.FDNode;
import network.GameNode;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Chess extends Thread{

    private int isTurn = 0;
    private boolean playing = true;
    private Move move = null;
    private int playerNum = 0;
    private FDNode fdnode;
    private GameNode<Move> gnode;
    private GraphicInterface fpchess;
    private BlockingQueue<Move> ownMove;

    public Chess(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter the number of the player: ");
        playerNum = keyboard.nextInt();
        gnode =  new GameNode<Move>(playerNum);
        fdnode = new FDNode(playerNum);
        ownMove = new ArrayBlockingQueue<Move>(1);
    }

    public  void addMove(Move m){
        try {
            ownMove.put(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Move waitForMove(){
        try {
            return ownMove.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stopGame(){
        gnode.stopNode();
        fdnode.stopNode();
    }

    public void setGI(GraphicInterface gi){
        fpchess = gi;
    }

    public void run() {
        gnode.start();
        fdnode.start();

        //If node is equal to 1 start the game and move
        if (playerNum == 0) {
            fpchess.executeMove(true, move);
            //Communicate movement to the rest
            gnode.communicateMessage(waitForMove());
            isTurn++;
        }
        else{
            isTurn = playerNum;
        }

        while (playing) {

            //If not wait for the movements
            while (isTurn < 4) {
                move = gnode.pullMove();
                if (move != null) {
                    fpchess.executeMove(false, move);
                    isTurn++;
                }
            }
            //Move
            fpchess.executeMove(true, move);
            //Communicate movement to the rest
            gnode.communicateMessage(waitForMove());
            //Wait again
            isTurn = 0;
        }
    }
}
