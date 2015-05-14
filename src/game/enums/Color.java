package game.enums;

/**
 * This enumeration represents the possible colors of a player's pieces.
 * They can be black, white, blue, or orange.
 */
public enum Color {
    BLACK, WHITE, GOLDEN, COPPER;

    @Override
    public String toString() {
        switch (this){
            case BLACK:
                return "Black";
            case WHITE:
                return "White";
            case GOLDEN:
                return "Golden";
            case COPPER:
                return "Copper";
            default:
                return "None";
        }
    }
}
