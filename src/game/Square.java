package game;

import com.sun.tools.javac.util.Pair;
import game.enums.Color;
import Utils.GameUtils;

public class Square
{
    private Color color;
    private Pair<Integer, Integer> location;
    private Piece  piece;

    public Square(Piece p, Pair<Integer, Integer> l) {
        piece = p;
        location = l;
        color = GameUtils.getSquareColor(location);
    }

    public void setPiece(Piece p){
        this.piece = p;
    }

    public Piece getPiece(){
        return piece;
    }
}
