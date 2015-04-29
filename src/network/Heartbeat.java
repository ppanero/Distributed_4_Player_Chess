package network;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * It carries the address of the node to whom it goes (the one receiving the heartbeat)
 */
public class Heartbeat implements Serializable {

    //IP address of the heartbeat's node
    private InetAddress nodeAddress;
    //Timestamp of the heartbeat
    private long timestamp;

    public Heartbeat(InetAddress nodeAddress, long timestamp) {
        this.nodeAddress = nodeAddress;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public InetAddress getNodeAddress() {
        return nodeAddress;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof Heartbeat))return false;
        Heartbeat otherHeartbeat = (Heartbeat)other;
        return this.nodeAddress == otherHeartbeat.nodeAddress &&
                this.timestamp >= otherHeartbeat.timestamp;
    }

    @Override
    public String toString(){
        return "node address: " + this.nodeAddress.getHostAddress()
                + " timestamp " + this.timestamp;
    }
}
