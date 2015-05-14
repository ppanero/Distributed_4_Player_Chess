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

    public Board() {
        squares = new Square[14][14];


        //Fill the middle board with null pieces
        for(int i = 2; i < 12; ++i)
            for(int j = 2; j < 12; ++j)
                squares[i][j] = new Square(null, new Pair<Integer, Integer>(i, j));

        //Initialize the pawns
        for(int i = 3; i < 12; ++i) {
            squares[1][i] = new Square(new Piece(PieceName.PAWN, PieceState.INITIAL, Color.BLACK), new Pair<Integer, Integer>(1, i));
            squares[12][13-i] = new Square(new Piece(PieceName.PAWN, PieceState.INITIAL, Color.WHITE), new Pair<Integer, Integer>(13, 14-i));
            squares[i][1] = new Square(new Piece(PieceName.PAWN, PieceState.INITIAL, Color.GOLDEN), new Pair<Integer, Integer>(i, 1));
            squares[13-i][12] = new Square(new Piece(PieceName.PAWN, PieceState.INITIAL, Color.COPPER), new Pair<Integer, Integer>(14-i, 13));
        }

        for(int i = 3; i < 11; ++i)
        {
            PieceName name;
            switch(i)
            {
                case 3:
                case 10:
                    name = PieceName.ROOK;
                    break;
                case 4:
                case 9:
                    name = PieceName.KNIGHT;
                    break;
                case 5:
                case 8:
                    name = PieceName.BISHOP;
                    break;
                case 6:
                    name = PieceName.QUEEN;
                    break;
                case 7:
                    name = PieceName.KING;
                    break;
                default:
                    name = null;
            }
            squares[0][i] = new Square(new Piece(name, PieceState.INITIAL, Color.BLACK), new Pair<Integer, Integer>(0, i));
            squares[13][i] = new Square(new Piece(name, PieceState.INITIAL, Color.WHITE), new Pair<Integer, Integer>(0, i));
            squares[i][0] = new Square(new Piece(name, PieceState.INITIAL, Color.GOLDEN), new Pair<Integer, Integer>(0, i));
            squares[i][13] = new Square(new Piece(name, PieceState.INITIAL, Color.COPPER), new Pair<Integer, Integer>(0, i));
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

    public Square getSquare(Pair<Integer, Integer> location){
        return squares[location.fst][location.snd];
    }
}
