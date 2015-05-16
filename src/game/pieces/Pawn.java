package game.pieces;

import java.util.ArrayList;
import java.util.Arrays;

public class Pawn extends Piece{
	//These arrays are the moves for the pawns if they haven't already moved
	private final Move[] movesP1NotMoved = {new Move(-2, 0, this), new Move(-1, 0, this)};
	//These arrays are for every move after the initial move
	private final Move[] movesP1HasMoved = {new Move(-1, 0, this)};
	private final Move[] movesP2NotMoved = {new Move(2, 0, this), new Move(1, 0, this)};
	private final Move[] movesP2HasMoved = {new Move(1, 0, this)};
	private final Move[] movesP3NotMoved = {new Move(0, -2, this), new Move(0, -1, this)};
	private final Move[] movesP3HasMoved = {new Move(0, -1, this)};
	private final Move[] movesP4NotMoved = {new Move(0, 2, this), new Move(0, 1, this)};
	private final Move[] movesP4HasMoved = {new Move(0, 1, this)};
	//These arrays contain the moves for attacking
	private final Move[] attacksP1 = {new Move(-1, -1, this), new Move(-1, 1, this)};
	private final Move[] attacksP2 = {new Move(1, -1, this), new Move(1, 1, this)};
	private final Move[] attacksP3 = {new Move(1, -1, this), new Move(-1, -1, this)};
	private final Move[] attacksP4 = {new Move(1, 1, this), new Move(-1, 1, this)};
	
	/**
	 * Creates a pawn associated to the player's number
	 * @param player the owner of the pawn's number
	 */
	public Pawn(PlayerNum player) {
		super(player);
		switch(player){
		case ONE:
			moveSet = new ArrayList<Move>(Arrays.asList(movesP1NotMoved));
			break;
		case TWO:
			moveSet = new ArrayList<Move>(Arrays.asList(movesP2NotMoved));
			break;
		case THREE:
			moveSet = new ArrayList<Move>(Arrays.asList(movesP3NotMoved));
			break;
		case FOUR:
			moveSet = new ArrayList<Move>(Arrays.asList(movesP4NotMoved));
			break;
		default:
			break;
		}
		imageFile += "Pawn.png";
	}
	
	@Override
	public void move(){
		switch(player){
		case ONE:
			moveSet = new ArrayList<Move>(Arrays.asList(movesP1HasMoved));
			break;
		case TWO:
			moveSet = new ArrayList<Move>(Arrays.asList(movesP2HasMoved));
			break;
		case THREE:
			moveSet = new ArrayList<Move>(Arrays.asList(movesP3HasMoved));
			break;
		case FOUR:
			moveSet = new ArrayList<Move>(Arrays.asList(movesP4HasMoved));
			break;
		default:
			break;
		}
	}
	
	/**
	 * Returns the attacking moves for a certain piece
	 * @return a list of the moves for a pawn for attacking
	 */
	public ArrayList<Move> getAttackSet(){
		switch(player){
		case ONE:
			return new ArrayList<Move>(Arrays.asList(attacksP1));
		case TWO:
			return new ArrayList<Move>(Arrays.asList(attacksP2));
		case THREE:
			return new ArrayList<Move>(Arrays.asList(attacksP3));
		case FOUR:
			return new ArrayList<Move>(Arrays.asList(attacksP4));
		default:
			return null;
		}
	}
}
