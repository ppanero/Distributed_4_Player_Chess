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
     * previous X coordinate
     */
    private int prex;

    /**
     * previous Y coordinate
     */
	private int prey;

	/**
	 * A tag for the types of pieces that can technically move infinitely. They're moves are only stopped by boundaries or a piece on their team
	 */
	private boolean infinite;

    /**
     * The piece that is being moved
     */

    private Piece piece;

    public Move(){
        this.prex = -1;
        this.prey = -1;
        this.x = -1;
        this.y = -1;
        this.piece = null;
        this.infinite = false;
    }
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

    public void setX(int x){
        this.x = x;
    }

	/**
	 * returns the y-coordinate of the move
	 * @return y
	 */
	public int getY(){
		return y;
	}

    public void setY(int y){
        this.y = y;
    }

    public int getPrex(){
        return prex;
    }

    public void setPrex(int px){
        this.prex = px;
    }

    public int getPrey(){
        return prey;
    }

    public void setPrey(int py){
        this.prey = py;
    }


    /**
     * returns the piece being moved
     * @return piece
     */
    public Piece getPiece(){
        return piece;
    }

    public void setPiece(Piece p){
        this.piece = p;
    }

	/**
	 * Returns whether or not the move is infinite
	 * @return infinite
	 */
	public boolean infinite(){
		return infinite;
	}

    @Override
    public String toString(){
        if(piece != null)
            return "Piece " +  piece.getType() + " from " + piece.getPlayerNum() + " moves from x = " + prex + " y = " + prey +
                    " to x = " + x + " y = " + y;
        else
            return "Null piece moves from x = " + prex + " y = " + prey +
                    " to x = " + x + " y = " + y;
    }
}
