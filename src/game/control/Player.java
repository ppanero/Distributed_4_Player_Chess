package game.control;

import game.pieces.Piece.PlayerNum;;

public class Player {
	
	/**
	 * Whether the player is still in the game
	 */
	private boolean playing;
	
	/**
	 * The pieces will be controlled by the player
	 */
	private boolean human;
	
	/**
	 * Number of the current player
	 */
	private PlayerNum number;
	/**
	 * If the current player is in check
	 */
	private boolean inCheck;
	
	public Player(boolean human, boolean playing, PlayerNum player){
		this.human = human;
		this.playing = playing;
		number = player;
	}
	
	/**
	 * Returns whether the current player is playing or not
	 * @return true if the player is still playing
	 */
	public boolean playing(){
		return playing;
	}
	
	/**
	 * Remove the player from the game
	 */
	public void removePlayer(){
		this.playing = false;
	}
	
	/**
	 * Returns whether the current player is a human player
	 * @return true if the player is a human
	 */

	public boolean human(){
		return human;
	}
	
	/**
	 * Returns the current player's number
	 * @return returns the current player's player number (1-4)
	 */
	public PlayerNum getPlayerNum(){
		return number;
	}
	
	/**
	 * Sets if the player is currently in check
	 * @param inCheck Flag if the player is in check
	 */
	public void setInCheck(boolean inCheck){
		this.inCheck = inCheck;
	}
	
	/**
	 * Returns whether the player is in check
	 * @return True if the player is in check, false otherwise
	 */
	public boolean inCheck(){
		return inCheck;
	}
}
