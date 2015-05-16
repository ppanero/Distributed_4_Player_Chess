package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;


public class Bishop extends Piece{
	
	private final Move[] moves = {new Move(1, 1, true, this), new Move(1, -1, true, this),
										new Move(-1, 1, true, this), new Move(-1, -1, true, this)};
	
	public Bishop(PlayerNum player){
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "Bishop.png";
	}

}
