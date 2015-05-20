package network;

import java.net.InetAddress;
import java.util.Arrays;

/**
 * Class representing a frame used for in game messaging.
 * This class extends the DataFrame class with an attribute of class T (in this case
 * messages are of class Move), and a boolean attribute token (true if it is a token).
 */
public class GameFrame<T> extends DataFrame{

    //true if it is a token frame
    private boolean token;
    //Message the frame is transmitting (if any)
    private T message;
    private long timestamp;


    public boolean getToken() {
        return this.token;
    }

    public T getMessage() {
        return this.message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setToken(boolean t){
        this.token = t;
    }

    public void setTimestamp(long ts){
        this.timestamp = ts;
    }


    //For the token
    public GameFrame(int destination,InetAddress destinationAddr, int source, InetAddress sourceAddr){
        super(destination, destinationAddr, source, sourceAddr);
        this.message = (T)(new Object());
        this.ack = false;
        this.token = true;
    }

    //create a data frame
    public GameFrame(int destination,InetAddress destinationAddr, int source, InetAddress sourceAddr,
                     T msg, long timestamp,int id){
        super(destination, destinationAddr, source, sourceAddr);
        this.timestamp = timestamp;
        this.message = msg;
        this.ack = false;
        this.token = false;
    }

    @Override
    public String toString(){
        if(this.message != null)
            return  "Frame from: " + this.sourceAddr.getHostAddress() + " , " + this.getSourcePort() +
                " to:   " + this.getDestinationAddr() + " , " + this.getDestinationPort() + '\n' +
                "Message: " + this.message.toString();
        else
            return  "Frame from: " + this.sourceAddr.getHostAddress() + " , " + this.getSourcePort() +
                    " to:   " + this.getDestinationAddr() + " , " + this.getDestinationPort() + '\n' +
                    "Null message";
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof GameFrame))return false;
        GameFrame otherGameFrame = (GameFrame)other;
        return this.sourceAddr.equals(otherGameFrame.sourceAddr) &&
                this.destinationAddr.equals(otherGameFrame.destinationAddr);
    }
}
