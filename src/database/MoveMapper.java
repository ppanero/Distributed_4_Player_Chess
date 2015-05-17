package database;

import game.pieces.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;


/**
 * This class represents a move in the applications and a piece in the database, mapping those two different type of classes.
 */
public class MoveMapper extends AbstractMapper<Move, String> {

    private static final String[] PIECE_KEY_COLUMN_NAME = {"X","Y","Type","PlayerNum"};
    private static final String PIECE_TABLE_NAME = "Piece";

    public MoveMapper(DataSource ds) {
        super(ds);
    }


    @Override
    protected String[] getKeyColumnNames() {
    	String[] aux = PIECE_KEY_COLUMN_NAME;
        return aux;
    }


    @Override
    protected Move buildObject(ResultSet rs) throws SQLException {
        Move result;
        int x = rs.getInt(PIECE_KEY_COLUMN_NAME[0]);
        int y = rs.getInt(PIECE_KEY_COLUMN_NAME[1]);
        int type = rs.getInt(PIECE_KEY_COLUMN_NAME[2]);
        int playerNum = rs.getInt(PIECE_KEY_COLUMN_NAME[3]);
        result = new Move(x, y, buildPiece(playerNum, type));
        return result;
    }

    /**
     * Builds the piece according to the specified type and the player given
     * @param playerNum
     * @return a new piece of the specified type
     */
    private Piece buildPiece(int playerNum, int type){
        Piece piece;
        switch (type){
            case 0:
                piece = new Pawn(Piece.PlayerNum.getPlayerNum(playerNum));
                break;
            case 1:
                piece = new Rook(Piece.PlayerNum.getPlayerNum(playerNum));
                break;
            case 2:
                piece = new Knight(Piece.PlayerNum.getPlayerNum(playerNum));
                break;
            case 3:
                piece = new Bishop(Piece.PlayerNum.getPlayerNum(playerNum));
                break;
            case 4:
                piece = new King(Piece.PlayerNum.getPlayerNum(playerNum));
                break;
            case 5:
                piece = new Queen(Piece.PlayerNum.getPlayerNum(playerNum));
                break;
            default:
                piece = null;
                break;
        }
        return piece;
    }

    @Override
    protected String[] getColumnNames() {
        return PIECE_KEY_COLUMN_NAME;
    }


    @Override
    protected String getTableName() {
        return PIECE_TABLE_NAME;
    }


	@Override
	protected Object[] serializeKey(String key) {
		String[] aux = {key};
		return aux;
	}


	@Override
	protected String getKeyFromObject(Move objeto) {
		return objeto.getX() + "," + objeto.getY() + "," + objeto.getPiece().getType() + "," + Piece.PlayerNum.toInt(objeto.getPiece().getPlayerNum());
	}


	@Override
	protected Object[] serializeObject(Move objeto) {
		
		Object[] aux = {objeto.getX(),
				objeto.getY(),
				objeto.getPiece().getType(),
                Piece.PlayerNum.toInt(objeto.getPiece().getPlayerNum())};
		return aux;
	}


	@Override
	protected String getIncrementalColumnName() {
		return null;
	}


	@Override
	protected Object[] serializeObjectForIncremental(Move objeto) {
		// TODO Auto-generated method stub
		return null;
	}

}
