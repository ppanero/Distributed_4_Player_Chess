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
	//true if it is a token frame
	private boolean token = false;
	//Destination port of the frame 
    private int destinationPort;
    //Destination address of the frame
    private InetAddress destinationAddr;
	//Source port of the frame
    private int sourcePort;
    //Source address of the frame
    private InetAddress sourceAddr;
    //Message the frame is transmitting (if any)
    private byte[] message;


    public boolean getToken() {
        return this.token;
    }

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

    public byte[] getMessage() {
        return this.message;
    }

    public void setSourceAddr(InetAddress src_addr) {
        this.sourceAddr = src_addr;
    }

    public void setSourcePort(int src_port) {
        this.sourcePort = src_port;
    }

    public void setToken(boolean t){
        this.token = t;
    }

    //create either a Token or amp frame
	public DataFrame(){ }
	
	//create a data frame
	public DataFrame(int destination,InetAddress destinationAddr, int source, InetAddress sourceAddr, byte[] msg){
		this.destinationPort = destination;
        this.destinationAddr = destinationAddr;
		this.sourcePort = source;
        this.sourceAddr = sourceAddr;
		this.message = msg;
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
        return  "Frame status:  " + '\n' +
                "   - token:     " + this.getToken() + '\n' +
                "From: " + this.getSourceAddr() + " port: " + this.getSourcePort() + '\n' +
                "To:   " + this.getDestinationAddr() + " port: " + this.getDestinationPort() + '\n' +
                "Message: " + Arrays.toString(this.getMessage());
    }
}
