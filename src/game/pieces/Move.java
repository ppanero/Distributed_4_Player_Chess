package game.pieces;

public class Move {
	
	/**
	 * X coordinate of the move
	 */
	private int x;
	
	/**
	 * Y coordinate of the pawn
	 */
	private int y;
	
	/**
	 * A tag for the types of pieces that can technically move infinitely. They're moves are only stopped by boundaries or a piece on their team
	 */
	private boolean infinite;
	
	/**
	 * Creates a move for a piece at the designated locations. This constructor is for pawns, knights, and kings
	 * @param x x component of the move
	 * @param y y component of the move
	 */
	public Move(int x, int y){
		this.x = x;
		this.y = y;
		this.infinite = false;
	}
	
	/**
	 * Same as above, but used for Queens, rooks, and bishops.
	 * @param x
	 * @param y
	 * @param infinite whether or not the piece's moves depend on other pieces locations
	 */
	public Move(int x, int y, boolean infinite){
		this.x = x;
		this.y = y;
		this.infinite = infinite;
	}
	
	/**
	 * Returns x-coordinate of the move
	 * @return x
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * returns the y-coordinate of the move
	 * @return y
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Returns whether or not the move is infinite
	 * @return infinite
	 */
	public boolean infinite(){
		return infinite;
	}
}
