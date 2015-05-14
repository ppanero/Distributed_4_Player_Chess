package game;


import com.sun.tools.javac.util.Pair;
import game.enums.Color;
import game.enums.PieceName;
import game.enums.PieceState;

/**
 * This class represents a piece of the game. Therefore it has as attributes its owner (color), its
 * location in the board (represented as x, y coordinates) and and its type.
 */

public class Piece {

    //Attributes
    private PieceName name;
    private PieceState state;
    private Color color;

    //Constructors

    public Piece(PieceName t, PieceState s, Color c){
        this.name = t;
        this.state = s;
        this.color = c;
    }

    //Getters and Setters

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public PieceName getPieceName(){
        return this.name;
    }

    public void setName(PieceName name) {
        this.name = name;
    }

    //Methods

    public boolean isPossibleMoveRook(Pair<Integer, Integer> oldL, Pair<Integer, Integer> newL){
        return oldL.fst.equals(newL.fst) || oldL.snd.equals(newL.snd);
    }

    public  boolean isPossibleMoveKnight(Pair<Integer, Integer> oldL, Pair<Integer, Integer> newL){
        return ((oldL.fst == newL.fst + 1 || oldL.fst == newL.fst - 1) &&
                (oldL.snd == newL.snd + 2 || oldL.snd == newL.snd - 2)) ||
               ((oldL.fst == newL.fst + 2 || oldL.fst == newL.fst - 2) &&
                (oldL.snd == newL.snd + 1 || oldL.snd == newL.snd - 1));
    }

    public boolean isPossibleMoveBishopp(Pair<Integer, Integer> oldL, Pair<Integer, Integer> newL){
        return !isPossibleMoveRook(oldL, newL) && (Math.abs(oldL.fst - newL.fst) == Math.abs(oldL.snd - newL.snd));
    }

    public boolean isPossibleMoveKing(Pair<Integer, Integer> oldL, Pair<Integer, Integer> newL){
        return ((oldL.fst == newL.fst + 1 || oldL.fst == newL.fst - 1 || oldL.fst.equals(newL.fst)) &&
                (oldL.snd == newL.snd + 1 || oldL.snd == newL.snd - 1) || oldL.snd.equals(newL.snd));
    }

    public boolean isPossibleMoveQueen(Pair<Integer, Integer> oldL, Pair<Integer, Integer> newL){
        return isPossibleMoveBishopp(oldL, newL) || isPossibleMoveRook(oldL, newL);
    }

    public boolean isPossibleMovePawn(Pair<Integer, Integer> oldL, Pair<Integer, Integer> newL){
        switch (color){
            case BLACK:
                return (oldL.fst + 1 == newL.fst) && (Math.abs(oldL.snd-newL.snd) <= 1);
            case WHITE:
                return (oldL.fst - 1 == newL.fst) && (Math.abs(oldL.snd-newL.snd) <= 1);
            case GOLDEN:
                return (oldL.snd + 1 == newL.snd) && (Math.abs(oldL.fst-newL.fst) <= 1);
            case COPPER:
                return (oldL.snd - 1 == newL.snd) && (Math.abs(oldL.fst-newL.fst) <= 1);
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return "Piece{" +
                "type=" + name.toString() +
                ", state=" + state.toString() +
                ", color=" + color.toString() +
                '}';
    }
}
