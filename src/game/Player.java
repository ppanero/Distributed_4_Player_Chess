package game;


import game.enums.Color;
import game.enums.PieceName;
import game.enums.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class Player
{
    private Long                 time;
    private Color color;
    private List<Piece> availablePieces;
    private PlayerState state;

    private Player()
    {
    }

    //Constructors

    public Player(Color c, List<Piece> pieceList){
        this.color = c;
        this.availablePieces = pieceList;
    }

    public Player(Color c){
        this.color = c;
        this.availablePieces = initializePieces(this.color);
    }

    //Getters and Setters

    public List<Piece> getPieces() {
        return availablePieces;
    }

    public void setPieces(List<Piece> pieces) {
        this.availablePieces = pieces;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    //Methods
    private static List<Piece> initializePieces(Color c) {
        List<Piece> list = new ArrayList<Piece>();

        return list;
    }

    public void addTime(Long milliSecs)
    {
        time += milliSecs;
    }

    public void addPiece(Piece piece)
    {
        availablePieces.add(piece);
    }

    public void removePiece(Piece piece) {
        availablePieces.remove(piece);
        if(piece.getPieceName().equals(PieceName.KING)){
            availablePieces.clear();
        }
        if(availablePieces.isEmpty()){
            this.state = PlayerState.SURRENDERED;
        }
    }
}
