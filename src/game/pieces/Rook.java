package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;


public class Rook extends Piece{
	
	private static final Move[] moves = {new Move(1, 0, true), new Move(-1, 0, true),
										new Move(0, 1, true), new Move(0, -1, true)};
	public Rook(PlayerNum player) {
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "Rook.png";
	}
	
}
