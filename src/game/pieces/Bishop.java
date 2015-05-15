package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;


public class Bishop extends Piece{
	
	private static final Move[] moves = {new Move(1, 1, true), new Move(1, -1, true), 
										new Move(-1, 1, true), new Move(-1, -1, true)};
	
	public Bishop(PlayerNum player){
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "Bishop.png";
	}

}
