package game;

import com.sun.tools.javac.util.Pair;

public class Move {
    private Piece piece;
    private Pair<Integer, Integer> oldLocation;
    private Pair<Integer, Integer> newLocation;

    public Move(Piece p, Pair<Integer, Integer> oldL, Pair<Integer, Integer> newL){
        piece = p;
        oldLocation = oldL;
        newLocation = newL;
    }

    public Pair<Integer, Integer> getNewLocation(){
        return newLocation;
    }

    public Pair<Integer,Integer> getOldLocation() {
        return oldLocation;
    }

    public Piece getPiece(){
        return piece;
    }

    public boolean isPossible() {
        switch (piece.getPieceName()){
            case KING:
                return piece.isPossibleMoveKing(oldLocation, newLocation);
            case QUEEN:
                return piece.isPossibleMoveQueen(oldLocation, newLocation);
            case ROOK:
                return piece.isPossibleMoveRook(oldLocation, newLocation);
            case KNIGHT:
                return piece.isPossibleMoveKnight(oldLocation, newLocation);
            case BISHOP:
                return piece.isPossibleMoveBishopp(oldLocation, newLocation);
            case PAWN:
                return piece.isPossibleMovePawn(oldLocation, newLocation);
            default:
                return false;
        }
    }


}
