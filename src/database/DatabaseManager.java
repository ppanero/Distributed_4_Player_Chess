package database;

import game.pieces.*;

import javax.sql.DataSource;


public class DatabaseManager {

    private DBConnector dbConnector;
    private AbstractMapper<Move, String> dbMapper;


    public DatabaseManager(DataSource ds){
        this.dbConnector = new DBConnector();
        this.dbMapper = new MoveMapper(ds);
    }

    public boolean initializeDatabase(){
        //clean database from possible previous matches
        if(!cleanDatabase()){
            return false;
        }
        if(!createPieces()){
                return false;
        }
        return true;
    }

    private boolean cleanDatabase(){
        return dbMapper.truncateTable();
    }

    private boolean createPieces() {
        //Initialize the pawns
        for(int i = 3; i < 11; ++i) {
            dbMapper.insert(new Move(12, 13-i, new Pawn(Piece.PlayerNum.ONE)));
            dbMapper.insert(new Move(1, i, new Pawn(Piece.PlayerNum.TWO)));
            dbMapper.insert(new Move(13-i, 12, new Pawn(Piece.PlayerNum.THREE)));
            dbMapper.insert(new Move(i, 1, new Pawn(Piece.PlayerNum.FOUR)));
        }

        for(int i = 3; i < 11; ++i)
        {
            switch(i)
            {
                case 3:
                case 10:
                    dbMapper.insert(new Move(13, 13-i, new Rook(Piece.PlayerNum.ONE)));
                    dbMapper.insert(new Move(1, i, new Rook(Piece.PlayerNum.TWO)));
                    dbMapper.insert(new Move(13-i, 13, new Rook(Piece.PlayerNum.THREE)));
                    dbMapper.insert(new Move(i, 1, new Rook(Piece.PlayerNum.FOUR)));
                    break;
                case 4:
                case 9:
                    dbMapper.insert(new Move(13, 13-i, new Knight(Piece.PlayerNum.ONE)));
                    dbMapper.insert(new Move(1, i, new Knight(Piece.PlayerNum.TWO)));
                    dbMapper.insert(new Move(13-i, 13, new Knight(Piece.PlayerNum.THREE)));
                    dbMapper.insert(new Move(i, 1, new Knight(Piece.PlayerNum.FOUR)));
                    break;
                case 5:
                case 8:
                    dbMapper.insert(new Move(13, 13-i, new Bishop(Piece.PlayerNum.ONE)));
                    dbMapper.insert(new Move(1, i, new Bishop(Piece.PlayerNum.TWO)));
                    dbMapper.insert(new Move(13-i, 13, new Bishop(Piece.PlayerNum.THREE)));
                    dbMapper.insert(new Move(i, 1, new Bishop(Piece.PlayerNum.FOUR)));
                    break;
                case 6:
                    dbMapper.insert(new Move(13, 13-i, new Queen(Piece.PlayerNum.ONE)));
                    dbMapper.insert(new Move(1, i, new Queen(Piece.PlayerNum.TWO)));
                    dbMapper.insert(new Move(13-i, 13, new Queen(Piece.PlayerNum.THREE)));
                    dbMapper.insert(new Move(i, 1, new Queen(Piece.PlayerNum.FOUR)));
                    break;
                case 7:
                    dbMapper.insert(new Move(13, 13-i, new King(Piece.PlayerNum.ONE)));
                    dbMapper.insert(new Move(1, i, new King(Piece.PlayerNum.TWO)));
                    dbMapper.insert(new Move(13-i, 13, new King(Piece.PlayerNum.THREE)));
                    dbMapper.insert(new Move(i, 1, new King(Piece.PlayerNum.FOUR)));
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    public void updatePiece(Move move){
        dbMapper.update(move);
    }

    /*Main to test database manager
    public static void main(String[] args)
    {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        DataSource ds;
        Connection con = null;
        try {
            cpds.setDriverClass("com.mysql.jdbc.Driver");
        } catch (PropertyVetoException e1) {
            e1.printStackTrace();
        }
        cpds.setJdbcUrl("jdbc:mysql://localhost/fpchess");
        cpds.setUser("root");
        cpds.setPassword("bAtsDUpX");
        cpds.setAcquireRetryAttempts(1);
        cpds.setAcquireRetryDelay(1);
        cpds.setBreakAfterAcquireFailure(true);
        ds = cpds;
        DatabaseManager dm = new DatabaseManager(ds);
        dm.initializeDatabase();
    }*/

}
