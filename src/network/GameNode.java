package network;

import Utils.LogUtils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

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

public class GameNode extends Thread{
	
	public static final int MAX_BUFFER = 1024;
    public static final String GAME_CONFIGURATION_FILENAME = "netconfig.txt";
	
	private int id;
    private int msgId;
    private NetConfiguration netConfig;
    private DatagramSocket socket;
    private boolean receiving;
    private boolean hasMessageToSend;
    private byte[] message;
    private InetAddress destinationAddr;
    private int destinationPort;
	
	public GameNode(int id){
		try {
			this.id = id;
            this.msgId = 0;
			this.netConfig = NetConfiguration.readConfigFromFile(this.GAME_CONFIGURATION_FILENAME);
			this.socket = new DatagramSocket(netConfig.getLocalPort(), netConfig.getLocalAddress());
            this.receiving = true;
            this.hasMessageToSend = false;
            this.message = null;
            this.destinationAddr = null;
            this.destinationPort = 0;
            //log
            LogUtils.log("Ring node initiated with id: " + this.id, LogUtils.RING_NODE_LOG_FILENAME);
            LogUtils.log(this.netConfig.toString(), LogUtils.RING_NODE_LOG_FILENAME);
		} catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void giveMessageToSend(byte[] msg, int desPort, InetAddress desAddr){
		this.message = msg;
		this.destinationPort = desPort;
        this.destinationAddr = desAddr;
        this.hasMessageToSend = true;
	}
	
	private void sendMessage(){
		DataFrame frame = new DataFrame(this.destinationPort,this.destinationAddr,
                                        this.netConfig.getLocalPort(), this.netConfig.getLocalAddress(),
                                        this.message, this.nextMsgId());
		sendFrame(frame);
        this.hasMessageToSend = false;
	}

    private void sendToken(DataFrame frame){
        frame.setSourceAddr(this.netConfig.getLocalAddress());
        frame.setSourcePort(this.netConfig.getLocalPort());
        frame.setDestinationPort(this.netConfig.getSuccessorPort());
        frame.setDestinationAddr(this.netConfig.getSuccessorAddr());
        sendFrame(frame);
        this.hasMessageToSend = false;
    }

    private void sendAck(DataFrame frame){
        frame.swapAddresses();
        frame.setAck(true);
        sendFrame(frame);
        this.hasMessageToSend = false;
    }

    private int nextMsgId(){
        this.msgId++;
        return this.msgId;
    }

	//send a frame with the DatagramPacket through the socket
	private void sendFrame(DataFrame frame){
			
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
        DataFrame frame = new DataFrame(this.netConfig.getSuccessorPort(),this.netConfig.getSuccessorAddr(),
                this.netConfig.getLocalPort(), this.netConfig.getLocalAddress());
        sendFrame(frame);
        hasMessageToSend = false;
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
            this.sendMessage();
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

			while(receiving ){
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

                if(frame.getToken()){
                    //Should wait here until player makes move
                    if(hasMessageToSend){
                        sendMessage();
                    }
                    sendToken(frame);
                }
                else if(frame.getAck()){
                    //Remove frame from list of acknowledges pending
                }
                else{
                    //Perform action and acknowledge
                    sendAck(frame);
                }
            }

		//If after 5 seconds nothing has been received, timeout
		//If the node is the monitor it can reinsert the token
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
