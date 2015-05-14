/*
 *  Created by raj.srivastava on 26/03/15
 */

package game;

import com.sun.tools.javac.util.Pair;
import game.enums.Color;
import game.enums.PieceName;
import game.enums.PieceState;

public class Board
{
    private Square[][] squares;
    private Player whitePlayer;
    private Player blackPlayer;
    private Player goldenPlayer;
    private Player copperPlayer;

    public Board(Player white, Player black, Player golden, Player copper)
    {
        whitePlayer = white;
        blackPlayer = black;
        goldenPlayer = golden;
        copperPlayer = copper;
        squares = new Square[15][15];


        //Fill the middle board with null pieces
        for(int i = 2; i < 13; i++)
            for(int j = 2; j < 13; j++)
                squares[i][j] = new Square(null, new Pair<Integer, Integer>(i, j));

        //Initialize the pawns
        for(int i = 3; i < 12; i++) {
            squares[1][i] = new Square(new Piece(Color.BLACK, PieceName.PAWN, PieceState.INITIAL), new Pair<Integer, Integer>(1, i));
            squares[13][14-i] = new Square(new Piece(Color.WHITE, PieceName.PAWN, PieceState.INITIAL), new Pair<Integer, Integer>(1, i));
            squares[i][1] = new Square(new Piece(Color.GOLDEN, PieceName.PAWN, PieceState.INITIAL), new Pair<Integer, Integer>(1, i));
            squares[14-i][13] = new Square(new Piece(Color.COPPER, PieceName.PAWN, PieceState.INITIAL), new Pair<Integer, Integer>(1, i));
        }

        for(int i = 0; i < 8; i++)
        {
            PieceName name;
            switch(i)
            {
                case 0:
                case 7:
                    name = PieceName.ROOK;
                    break;
                case 1:
                case 6:
                    name = PieceName.KNIGHT;
                    break;
                case 2:
                case 5:
                    name = PieceName.BISHOP;
                    break;
                case 3:
                    name = PieceName.QUEEN;
                    break;
                default:
                    name = PieceName.KING;
            }
            squares[0][i] = new Square(new Piece(Color.BLACK, name, PieceState.INITIAL), new Pair<Integer, Integer>(0, i));
            squares[7][i] = new Square(new Piece(Color.WHITE, name, PieceState.INITIAL), new Pair<Integer, Integer>(0, i));
        }
        //Assign non-playable squares null values
        for(int i = 0; i < 3; ++i){
            for(int j = 0; j < 3; ++j) {
                squares[i][j] = null;
                squares[i][j+12] = null;
                squares[i+12][j] = null;
                squares[i+12][j+12] = null;
            }

        }
    }
}
