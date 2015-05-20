package game.control;

import database.DatabaseManager;
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
    private BlockingQueue<Move> ownMove;
    private GraphicInterface fpchess;
    private DatabaseManager db;

    public Chess(){
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter the number of the player: ");
        playerNum = keyboard.nextInt();
        gnode =  new GameNode<Move>(playerNum);
        fdnode = new FDNode(playerNum);
        ownMove = new ArrayBlockingQueue<Move>(225);
        isTurn = playerNum % 4;
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
        playing = false;
    }

    public void setGI(GraphicInterface gi){
        fpchess = gi;
    }

    public void setDB(DatabaseManager dm){
        this.db = dm;
    }
    public void run() {
        gnode.start();
        fdnode.start();

        /*if (playerNum == 1) {
            fpchess.executeMove(true, move);
            //Communicate movement to the rest
            gnode.communicateMessage(waitForMove());
            gnode.makeToken();

        }

        while (playing) {

            //If not wait for the movements
            while (!isTurn) {
                move = gnode.pullMove();
                if (move != null) {
                    fpchess.executeMove(false, move);
                    System.out.println(move.toString());
                }
                else{
                    isTurn=true;
                }
            }
            //Move
            fpchess.executeMove(true, move);
            //Communicate movement to the rest
            gnode.communicateMessage(waitForMove());
            gnode.
            //Wait again
            isTurn = false;*/

        //If node is equal to 0 start the game and move
        if (isTurn == 0) {
            fpchess.executeMove(true, move);
            //Communicate movement to the rest
            Move m = waitForMove();
            db.updatePiece(m);
            gnode.communicateMessage(m);
            isTurn++;
        }

        while (playing) {

            //If not wait for the movements
            while (isTurn < 4) {
                move = gnode.pullMove();
                if (move != null) {
                    db.updatePiece(move);
                    fpchess.executeMove(false, move);
                    System.out.println(move.toString());
                    if (move.getPiece() != null){
                        isTurn++;
                    }
                }
            }
            //Move
            fpchess.executeMove(true, move);
            //Communicate movement to the rest
            Move m = waitForMove();
            db.updatePiece(m);
            gnode.communicateMessage(m);
            //Wait again
            isTurn = 1;
        }
    }
}
