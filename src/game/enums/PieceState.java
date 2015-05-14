package game.enums;

public enum PieceState
{
    INITIAL,
    MOVED,
    AVAILABLE,
    RESURRECTED;

    @Override
    public String toString() {
        switch (this){
            case INITIAL:
                return "Initial";
            case MOVED:
                return "Moved";
            case AVAILABLE:
                return "Available";
            case RESURRECTED:
                return "Resurrected";
            default:
                return "None";
        }
    }
}