package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;

public class King extends Piece{
	
	private static final Move[] moves = {new Move(1, 1), new Move(1, -1), 
										new Move(-1, 1), new Move(-1, -1),
										new Move(1, 0), new Move(-1, 0),
										new Move(0, 1), new Move(0, -1)};

	public King(PlayerNum player) {
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "King.png";
	}

}
