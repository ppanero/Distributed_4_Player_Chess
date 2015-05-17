package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;

public class King extends Piece{
	
	private final Move[] moves = {new Move(1, 1, this), new Move(1, -1, this),
										new Move(-1, 1, this), new Move(-1, -1, this),
										new Move(1, 0, this), new Move(-1, 0, this),
										new Move(0, 1, this), new Move(0, -1, this)};

	public King(PlayerNum player) {
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "King.png";
	}


    @Override
    public int getType(){
        return 4;
    }

}
