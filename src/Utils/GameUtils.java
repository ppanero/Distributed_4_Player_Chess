
package Utils;

import com.sun.tools.javac.util.Pair;
import game.enums.Color;

public class GameUtils
{
    public static Color getSquareColor(Pair<Integer, Integer> location)
    {
        if((location.fst % 2 == 1 && location.snd % 2 == 0)
                || (location.fst % 2 == 0 && location.snd % 2 == 1))
            return Color.WHITE;
        else
            return Color.BLACK;
    }
}
