package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;


public class Knight extends Piece{
	
	private static final Move[] moves = {new Move(2, 1), new Move(2, -1), 
										new Move(-2, 1), new Move(-2, -1), 
										new Move(1, 2), new Move(-1, 2), 
										new Move(1, -2), new Move(-1, -2)};
	public Knight(PlayerNum player){
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "Knight.png";
	}

}
