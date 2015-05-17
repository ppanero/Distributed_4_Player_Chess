package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;


public class Rook extends Piece{
	
	private final Move[] moves = {new Move(1, 0, true, this), new Move(-1, 0, true, this),
										new Move(0, 1, true, this), new Move(0, -1, true, this)};
	public Rook(PlayerNum player) {
		super(player);
		moveSet = new ArrayList<Move>(Arrays.asList(moves));
		imageFile += "Rook.png";
	}


    @Override
    public int getType(){
        return 1;
    }
	
}
