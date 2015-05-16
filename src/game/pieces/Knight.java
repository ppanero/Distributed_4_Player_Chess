package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;


public class Knight extends Piece{
	
	private final Move[] moves = {new Move(2, 1, this), new Move(2, -1, this),
										new Move(-2, 1, this), new Move(-2, -1, this),
										new Move(1, 2, this), new Move(-1, 2, this),
										new Move(1, -2, this), new Move(-1, -2, this)};
	public Knight(PlayerNum player){
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "Knight.png";
	}

}
