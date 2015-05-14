package game;


import java.util.Stack;

public class Game
{
    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player goldenPlayer;
    private Player copperPlayer;
    private Stack<Move> moves;

    public Game(Player white, Player black, Player golden, Player copper){
        whitePlayer = white;
        blackPlayer = black;
        goldenPlayer = golden;
        copperPlayer = copper;
        moves = new Stack<Move>();
        board = new Board();
    }

    public boolean addMove(Move m){
        if(!isPossibleMove(m)){
            return false;
        }
        executeMove(m);
        moves.push(m);
        return true;
    }

    private boolean isPossibleMove(Move m){
        if(!m.isPossible())
            return false;
        Piece newLocationP = this.board.getSquare(m.getNewLocation()).getPiece();
        if(newLocationP == null){
            return true;
        }
        if(newLocationP.getColor().equals(m.getPiece().getColor())) {
            return false;
        }
        return true;
    }

    private void executeMove(Move m){
        Square oldSquare = this.board.getSquare(m.getOldLocation());
        Square newSquare = this.board.getSquare(m.getNewLocation());
        Piece newLocationP = newSquare.getPiece();
        if(newLocationP != null) {
            switch (newLocationP.getColor()){
                case BLACK:
                    blackPlayer.removePiece(newLocationP);
                    break;
                case WHITE:
                    whitePlayer.removePiece(newLocationP);
                    break;
                case GOLDEN:
                    goldenPlayer.removePiece(newLocationP);
                    break;
                case COPPER:
                    copperPlayer.removePiece(newLocationP);
                    break;
            }
        }
        oldSquare.setPiece(null);
        newSquare.setPiece(m.getPiece());
    }
}
