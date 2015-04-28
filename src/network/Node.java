package network;

import Utils.LogUtils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.Charset;

/**
 * 
 * An individual node on the token ring network.
 * Implements runnable as it is meant to be executed by a thread.
 * Once it is ran it will first wait to receive a packet (unless it is 
 * has to start, in that case it will inject the token).
 * When it receives a packet it unpacks it into the Network.DataFrame class
 * and examines it. 
 * If it is the token it moves it on unless it has a message
 * to send.
 * Otherwise it checks if it is meant for it, if it isn't it just sends 
 * it on.
 * The node keeps on receiving frames while the boolean receiving is true.
 *
 */

public class Node extends Thread{
	
	public static final int MAX_BUFFER = 1024;
    public static final String NETWORK_CONFIGURATION_FILENAME = "netconfig.txt";
	
	int id;
    Configuration netConfig;
    int destinationPort = 0;
    InetAddress destinationAddr = null;
	DatagramSocket socket = null;
	boolean receiving = true;
	boolean hasMessageToSend = false;
	byte[] message = null;
	
	public Node(int id){
		try {
			this.id = id;
			this.netConfig = Configuration.readConfigFromFile(this.NETWORK_CONFIGURATION_FILENAME);
			socket = new DatagramSocket(netConfig.getLocalPort(), netConfig.getLocalAddress());
            //log
            LogUtils.log("Ring node initiated with id: " + this.id, LogUtils.RING_NODE_LOG_FILENAME);
            LogUtils.log(this.netConfig.toString(), LogUtils.RING_NODE_LOG_FILENAME);
		} catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

	
	public int getSocketPort(){
		return netConfig.getLocalPort();
	}
	
	public void giveMessageToSend(byte[] msg, int des_port, InetAddress des_addr){
		hasMessageToSend = true;
		message = msg;
		destinationAddr = des_addr;
        destinationPort = des_port;
	}
	
	public void sendMessage(){
		DataFrame frame = new DataFrame(destinationPort, destinationAddr,
                                        netConfig.getLocalPort(), netConfig.getLocalAddress(), message);
		frame.setToken(true);
		hasMessageToSend = false;
		sendFrame(frame);
	}
	
	//send a frame with the DatagramPacket through the socket
	public void sendFrame(DataFrame frame){
			
		DatagramPacket packet = null;
		byte[] buf = new byte[MAX_BUFFER];

		try{
			ByteArrayOutputStream fis = new ByteArrayOutputStream();
			ObjectOutputStream is = new ObjectOutputStream(fis);
			is.writeObject(frame);
			is.flush();
			buf = fis.toByteArray();
			packet = new DatagramPacket(buf, buf.length, 
					frame.getDestinationAddr(), frame.getDestinationPort());
			socket.send(packet);
            //Log
            LogUtils.log("Frame sent", LogUtils.RING_NODE_LOG_FILENAME);
            LogUtils.log(frame.toString(), LogUtils.RING_NODE_LOG_FILENAME);
		}catch(IOException ex){
			ex.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	//Make a new token frame send it on
	public void makeToken(){
		DataFrame tokenFrame = new DataFrame();
		tokenFrame.setToken(true);
		sendFrame(tokenFrame);
	}
	
	//stop the node from receiving
	public void switchReceiving(){
		
		try {
			receiving = !receiving;
			//socket.close();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
		
	}

    public void startNode(boolean initialNode){
        if(initialNode){
            String s = new String ("test");
            this.sendFrame(new DataFrame(this.netConfig.getSuccessorPort(),this.netConfig.getSuccessorAddr(),
                    this.netConfig.getLocalPort(), this.netConfig.getLocalAddress(),
                    s.getBytes(Charset.forName("UTF-8"))));
        }
        this.start();
    }

    public void stopNode(){
        this.switchReceiving();
    }
	
	public void run(){
		DatagramPacket packet = null;
		byte[] buffer;
		
		try {
			System.out.println("Node " + id + " has started");
            //Log
            LogUtils.log("Ring node " + this.id + " started", LogUtils.RING_NODE_LOG_FILENAME);

			while(receiving){
				buffer = new byte[MAX_BUFFER];
				packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				buffer = packet.getData();

				ByteArrayInputStream fis = new ByteArrayInputStream(buffer);
				ObjectInputStream in = new ObjectInputStream(fis);
				DataFrame frame = (DataFrame)in.readObject();

                //Log
                LogUtils.log("Frame received", LogUtils.RING_NODE_LOG_FILENAME);
                LogUtils.log(frame.toString(), LogUtils.RING_NODE_LOG_FILENAME);

                System.out.println("Node " + id + " has received the following message.");
                System.out.println("Received: " + frame.getMessage());
                System.out.println("From: "+packet.getAddress()+":"+packet.getPort());
                frame.swapAddresses();
                //frame.acknowledge();
                wait(3000);
                sendFrame(frame);

            }

		//If after 5 seconds nothing has been received, timeout
		//If the node is the monitor it can reinsert the token
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
