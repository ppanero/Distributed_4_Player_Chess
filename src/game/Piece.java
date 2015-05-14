package game;


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
    private int x;
    private int y;
    private Color color;

    //Constructors

    public Piece(PieceName t, PieceState s, int x, int y, Color c){
        this.name = t;
        this.state = s;
        this.x = x;
        this.y = y;
        this.color = c;
    }

    //Getters and Setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setName(PieceName name) {
        this.name = name;
    }

    //Methods


    @Override
    public String toString() {
        return "Piece{" +
                "type=" + name.toString() +
                "state=" + state.toString() +
                ", x=" + x +
                ", y=" + y +
                ", color=" + color.toString() +
                '}';
    }
}
