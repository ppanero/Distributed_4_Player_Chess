package game.pieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Piece {
	
	public enum PlayerNum{
		ONE, TWO, THREE, FOUR, EMPTY
	}
	
	/**
	 * Boolean for whether or not the piece needs to be highlighted
	 */
	protected boolean highlighted;
	
	/**
	 * The player's number
	 */
	protected PlayerNum player;
	
	/**
	 * The name of the image that needs to be loaded for each piece
	 */
	protected String imageFile;
	
	/**
	 * The available moves for each individual piece
	 */
	protected ArrayList<Move> moveSet;
	
	/**
	 * Creates a piece based and sets the pieces player number to the specified PlayerNUm
	 * @param player The number that needs to be attached to the piece. e.g. Player 4's king
	 */
	protected Piece(PlayerNum player){
		highlighted = false;
		this.player = player;
		imageFile = Piece.getPlayerStringMap().get(player);
	}
	
	/**
	 * Returns a list of the moves that any certain piece could make
	 * @return the piece's available pieces
	 */
	public ArrayList<Move> getMoveSet(){
		return moveSet;
	}
	
	/**
	 * Returns the pieces PlayerNum
	 * @return the which player owns the piece
	 */
	public PlayerNum getPlayerNum(){
		return player;
	}
	
	public void move(){
		//Do nothing unless pawn
	}
	
	/**
	 * Returns the name of the file that contains the pieces image
	 * @return the image file's name
	 */
	public String getImageFileName(){
		return imageFile;
	}
	
	/**
	 * 
	 * @return
	 */
	private static Map<PlayerNum, String> getPlayerStringMap(){
		Map<PlayerNum, String> playerMap = new HashMap<PlayerNum, String>();
		playerMap.put(PlayerNum.ONE, "white");
		playerMap.put(PlayerNum.TWO, "black");
		playerMap.put(PlayerNum.THREE, "red");
		playerMap.put(PlayerNum.FOUR, "green");
		playerMap.put(PlayerNum.EMPTY, "blank");
		return playerMap;
	}
}
