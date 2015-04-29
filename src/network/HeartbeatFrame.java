package network;

import java.net.InetAddress;

/**
 * Class representing a frame used for heartbeating (failure detection).
 * This class extends the DataFrame class with an attribute of class Heartbeat.
 */
public class HeartbeatFrame extends DataFrame{


    private Heartbeat heartbeat;

    public Heartbeat getHeartbeat() {
        return this.heartbeat;
    }

    public HeartbeatFrame(int destination,InetAddress destinationAddr, int source, InetAddress sourceAddr){
        super(destination, destinationAddr, source, sourceAddr);
        this.heartbeat = new Heartbeat(sourceAddr, System.currentTimeMillis());
    }

    @Override
    public String toString(){
        return  "Heartbeat frame: " +this.heartbeat.getTimestamp() +
                " from: " + this.sourceAddr.getHostAddress() + " , " + this.sourcePort + " to: " +
                this.destinationAddr.getHostAddress() + " , " + this.destinationPort;
    }

}
