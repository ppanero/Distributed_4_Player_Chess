/*
 *  Created by raj.srivastava on 04/04/15
 */

package baseModel.exceptions;

public class ChessException extends Exception
{
    private ChessError error;

    public ChessException(ChessError chessError)
    {
        super(chessError.getMessage());
        error = chessError;
    }
}
