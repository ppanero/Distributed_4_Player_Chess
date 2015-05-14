/*
 *  Created by raj.srivastava on 04/04/15
 */

package baseModel.exceptions;

public enum ChessError
{
    INVALID_PIECE(101, "This piece cannot move until opponent plays"),
    INVALID_MOVE(102, "This square is outside the valid moves of this piece"),
    INVALID_SQUARE(103, "This square is already occupied by a piece of same color"),
    DISCOVERED_CHECK(104, "This move puts your king in check"),
    CASTLING_NOT_ALLOWED(105, "Castling cannot be done. Either the king or rook has moved or the king has been checked"),

    NEW_PIECE_INVALID_SQUARE(201, "A new piece cannot be placed after your half of chess board"),
    NEW_PIECE_CREATES_CHECK(202, "A new piece cannot be placed so as to give a check in first move");

    private int    code;
    private String message;

    ChessError(int errorCode, String msg)
    {
        code = errorCode;
        message = msg;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }
}
