package game.enums;

public enum PlayerState
{
    JOINED,
    PLAYING,
    IN_CHECK,
    SURRENDERED;

    @Override
    public String toString() {
        switch (this){
            case JOINED:
                return "Joined";
            case PLAYING:
                return "Playing";
            case IN_CHECK:
                return "In check";
            case SURRENDERED:
                return "Surrendered";
            default:
                return "None";
        }
    }
}
