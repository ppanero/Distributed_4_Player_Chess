package game.enums;

/**
 * This enumeration represents the possible type of a player's piece.
 * A piece can be a king, a queen, a rook, a knight, a bishop, or a pawn.
 */
public enum PieceName {
    KING, QUEEN, ROOK, KNIGHT, BISHOP, PAWN;

    @Override
    public String toString() {
        switch (this){
            case KING:
                return "King";
            case QUEEN:
                return "Queen";
            case ROOK:
                return "Rook";
            case KNIGHT:
                return "Knight";
            case BISHOP:
                return "Bishop";
            case PAWN:
                return "Pawn";
            default:
                return "None";
        }
    }
}
