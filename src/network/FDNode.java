package network;

import Utils.LogUtils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

public class FDNode extends Thread{

    public static final int MAX_BUFFER = 1024;
    public static final String NETWORK_CONFIGURATION_FILENAME = "netconfig.txt";

    private int id;
    private boolean failureDetected;
    private NetConfiguration netConfig;
    private DatagramSocket socket;
    private boolean running;
    private List<Heartbeat> pendingHeartbeats;

    public FDNode(int id){
        try {
            this.id = id;
            this.netConfig = NetConfiguration.readConfigFromFile(this.NETWORK_CONFIGURATION_FILENAME);
            this.socket = new DatagramSocket(netConfig.getLocalPort(), netConfig.getLocalAddress());
            this.pendingHeartbeats = new ArrayList<Heartbeat>();
            this.failureDetected = false;
            this.running = true;
            //log
            LogUtils.log("Failure detection node initiated with id: " + this.id, LogUtils.FD_NODE_LOG_FILENAME);
            LogUtils.log(this.netConfig.toString(), LogUtils.RING_NODE_LOG_FILENAME);
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void fialureDetected(){
        this.failureDetected = true;
    }

    private void sendAck(DataFrame frame){
        frame.swapAddresses();
        frame.setAck(true);
        sendFrame(frame);
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

    public void stopNode(){
        this.running = false;
    }

    public void run(){
        DatagramPacket packet = null;
        byte[] buffer;

        //Sender task for the ExecutorService

        //Failure checker for the ExecutorService

        try {
            System.out.println("FD Receiver node " + id + " has started");
            //Log
            LogUtils.log("Ring node " + this.id + " started", LogUtils.FD_NODE_LOG_FILENAME);

            while(running){
                if(failureDetected){
                    //Do something
                }
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

                if(frame.getAck()){
                    //Remove frame from list of acknowledges pending

                }
                else{
                    //Perform action and acknowledge
                    sendAck(frame);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Shutdown ScheduledExecutorService
    }

    private class Heartbeat implements Serializable{
        //id of the heartbeat
        private int id;
        //IP address of the heartbeat's node
        private InetAddress nodeAddress;

        public Heartbeat(int id, InetAddress nodeAddress, Timestamp timestamp) {
            this.id = id;
            this.nodeAddress = nodeAddress;
            this.timestamp = timestamp;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public int getId() {
            return id;
        }

        public InetAddress getNodeAddress() {
            return nodeAddress;
        }

        //Timestamp of the heartbeat
        private Timestamp timestamp;

        @Override
        public boolean equals(Object other){
            if (other == null) return false;
            if (other == this) return true;
            if (!(other instanceof Heartbeat))return false;
            Heartbeat otherHeartbeat = (Heartbeat)other;
            return  this.id == otherHeartbeat.id &&
                    this.nodeAddress == otherHeartbeat.nodeAddress &&
                    this.timestamp == otherHeartbeat.timestamp;
        }

        @Override
        public String toString(){
            return "id: " + this.id + " node address: " + this.nodeAddress.getHostAddress()
                    + " timestamp " + this.timestamp.toString();
        }
    }
}
