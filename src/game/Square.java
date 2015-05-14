package game;

import com.sun.tools.javac.util.Pair;
import game.enums.Color;
import utils.GameUtils;

public class Square
{
    private Color color;
    private String name;
    private Piece  piece;

    public Square(Piece p, Pair<Integer, Integer> location)
    {
        piece = p;
        name = GameUtils.getSquareName(location);
        color = GameUtils.getSquareColor(location);
    }
}
