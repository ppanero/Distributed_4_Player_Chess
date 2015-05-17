package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;


public class Queen extends Piece{
	
	private final Move[] moves= {new Move(1, 1, true, this), new Move(1, -1, true, this),
            new Move(-1, 1, true, this), new Move(-1, -1, true, this),
            new Move(1, 0, true, this), new Move(-1, 0, true, this),
            new Move(0, 1, true, this), new Move(1, 0, true, this)};

	public Queen(PlayerNum player) {
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "Queen.png";
	}


    @Override
    public int getType(){
        return 5;
    }

}
