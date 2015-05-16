package game.pieces;

import java.io.Serializable;

public class Move implements Serializable{
	
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
     * The piece that is being moved
     */

    private Piece piece;

	/**
	 * Creates a move for a piece at the designated locations. This constructor is for pawns, knights, and kings
	 * @param x x component of the move
	 * @param y y component of the move
	 */
	public Move(int x, int y, Piece p){
		this.x = x;
		this.y = y;
		this.infinite = false;
        this.piece = p;
	}
	
	/**
	 * Same as above, but used for Queens, rooks, and bishops.
	 * @param x
	 * @param y
	 * @param infinite whether or not the piece's moves depend on other pieces locations
	 */
	public Move(int x, int y, boolean infinite, Piece p){
		this.x = x;
		this.y = y;
		this.infinite = infinite;
        this.piece = p;
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
     * returns the piece being moved
     * @return piece
     */
    public Piece getPiece(){
        return piece;
    }
	
	/**
	 * Returns whether or not the move is infinite
	 * @return infinite
	 */
	public boolean infinite(){
		return infinite;
	}
}
