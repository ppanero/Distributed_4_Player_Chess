package network;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Arrays;

/**
 *
 * A class which represents a data frame on the
 * token ring network.It is serializable which means it can be
 * flatten it and reuse later. This essentially means that the object
 * exists beyond the lifetime of the virtual machine.
 *
 */

public class DataFrame implements Serializable {

    //true if it is acknowledging a message
    protected boolean ack;
	//Destination port of the frame 
    protected int destinationPort;
    //Destination address of the frame
    protected InetAddress destinationAddr;
	//Source port of the frame
    protected int sourcePort;
    //Source address of the frame
    protected InetAddress sourceAddr;

    public boolean getAck(){ return this.ack;}

    public int getDestinationPort() {
        return this.destinationPort;
    }

    public InetAddress getDestinationAddr() {
        return this.destinationAddr;
    }

    public int getSourcePort() {
        return this.sourcePort;
    }

    public InetAddress getSourceAddr() {
        return this.sourceAddr;
    }

    public void setSourceAddr(InetAddress src_addr) {
        this.sourceAddr = src_addr;
    }

    public void setSourcePort(int src_port) {
        this.sourcePort = src_port;
    }

    public void setDestinationPort(int destPort){
        this.destinationPort = destPort;
    }

    public void setDestinationAddr(InetAddress destAddr){
        this.destinationAddr = destAddr;
    }

    public void setAck(boolean a){
        this.ack = a;
    }

    //For the token
	public DataFrame(int destination,InetAddress destinationAddr, int source, InetAddress sourceAddr){
        this.destinationPort = destination;
        this.destinationAddr = destinationAddr;
        this.sourcePort = source;
        this.sourceAddr = sourceAddr;
        this.ack = false;
    }

	//swap the addresses when acknowledging a frame
	public void swapAddresses(){
        int temp_port = destinationPort;
		destinationPort = sourcePort;
		sourcePort = temp_port;
        InetAddress temp_addr = destinationAddr;
        destinationAddr = sourceAddr;
        sourceAddr = temp_addr;
	}

    @Override
    public String toString(){
        return  "Frame from: " + this.getSourceAddr() + " port: " + this.getSourcePort() +
                " To:   " + this.getDestinationAddr() + " port: " + this.getDestinationPort();
    }
}
