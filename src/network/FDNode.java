package network;

import Utils.LogUtils;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 */

public class FDNode {

    public static final int MAX_BUFFER = 1024;
    public static final String NETWORK_CONFIGURATION_FILENAME = "netconfig.txt";
    //5 minutes (5 * 1000 = 5 seconds * 60) in order for a node to be considered failed
    public static final long TIME_TO_FAILURE = 5 * 60 * 1000;

    private int id;
    private boolean failureDetected;
    private NetConfiguration netConfig;
    private DatagramSocket socket;
    private boolean running;
    private List<Heartbeat> pendingHeartbeats;
    ScheduledExecutorService scheduledExecutorService;

    public FDNode(int id){
        try {
            this.id = id;
            this.netConfig = NetConfiguration.readConfigFromFile(FDNode.NETWORK_CONFIGURATION_FILENAME);
            this.socket = new DatagramSocket(netConfig.getLocalPort(), netConfig.getLocalAddress());
            this.pendingHeartbeats = new ArrayList<Heartbeat>();
            this.scheduledExecutorService = Executors.newScheduledThreadPool(3);
            this.failureDetected = false;
            this.running = true;
            //log
            LogUtils.log("Failure detection node created with id: " + this.id, LogUtils.FD_NODE_LOG_FILENAME);
            LogUtils.log(this.netConfig.toString(), LogUtils.FD_NODE_LOG_FILENAME);
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
            LogUtils.log("Frame sent", LogUtils.FD_NODE_LOG_FILENAME);
            LogUtils.log(frame.toString(), LogUtils.FD_NODE_LOG_FILENAME);
        }catch(IOException ex){
            ex.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void stopNode(){
        this.running = false;
        //Shutdown ScheduledExecutorService
        this.scheduledExecutorService.shutdown();
    }

    public void startFDNode() {
        //Log
        LogUtils.log("FD node " + this.id + " started", LogUtils.FD_NODE_LOG_FILENAME);

        //Sender task for the ExecutorService (executed each minute)
        this.scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //Send heartbeat to predecessor
                sendFrame(new HeartbeatFrame(netConfig.getPredecessorPort(), netConfig.getPredecessorAddr(),
                        netConfig.getLocalPort(), netConfig.getLocalAddress()));
                //Log
                LogUtils.log("Predecessor heartbeat sent", LogUtils.FD_NODE_LOG_FILENAME);
                //Sed heartbeat to successor
                sendFrame(new HeartbeatFrame(netConfig.getSuccessorPort(), netConfig.getSuccessorAddr(),
                        netConfig.getLocalPort(), netConfig.getLocalAddress()));
                //Log
                LogUtils.log("Successor heartbeat sent", LogUtils.FD_NODE_LOG_FILENAME);
            }
        }, 0, 60, TimeUnit.SECONDS);

        //Failure checker for the ExecutorService (executed each 5 minutes)
        this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //Check if there is any heartbeat in the list that have not been acknowledge
                //in the past 5 minutes (max temp for a player to move)
                for (Heartbeat h : pendingHeartbeats) {
                    long hTimestamp = h.getTimestamp() + FDNode.TIME_TO_FAILURE;
                    long currentTimestamp = System.currentTimeMillis();
                    if (currentTimestamp <= hTimestamp) {
                        //Failure detected from node h.getNodeAddress()
                        failureDetected = true;
                        //Log
                        LogUtils.log("Failure detected. Heartbeat not acknowledge after " + FDNode.TIME_TO_FAILURE
                                    , LogUtils.FD_NODE_LOG_FILENAME);
                        LogUtils.log(h.toString(), LogUtils.FD_NODE_LOG_FILENAME);
                    }
                }
            }
        }, 10, 300, TimeUnit.SECONDS);

        this.scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                DatagramPacket packet = null;
                byte[] buffer;
                try {
                    while (running) {
                        if (failureDetected) {
                            //Do something
                        }
                        buffer = new byte[MAX_BUFFER];
                        packet = new DatagramPacket(buffer, buffer.length);
                        socket.receive(packet);
                        buffer = packet.getData();
                        ByteArrayInputStream fis = new ByteArrayInputStream(buffer);
                        ObjectInputStream in = new ObjectInputStream(fis);
                        HeartbeatFrame frame = (HeartbeatFrame) in.readObject();
                        //Log
                        LogUtils.log("Frame received", LogUtils.FD_NODE_LOG_FILENAME);
                        LogUtils.log(frame.toString(), LogUtils.FD_NODE_LOG_FILENAME);

                        if (frame.getAck()) {
                            //Remove frame from list of acknowledges pending
                            //It is assumed that the list contains the heartbeat
                            Heartbeat heartbeat = frame.getHeartbeat();
                            if (!pendingHeartbeats.isEmpty()) {
                                //Remove in a while loop to remove all the heartbeats from the same node
                                //that have a timestamp lower or equal to the heartbeat (later heartbeats
                                //will have to be acknowledge again.
                                while (!pendingHeartbeats.isEmpty() && pendingHeartbeats.remove(heartbeat));
                                //Log
                                LogUtils.log("Acknowledge received", LogUtils.FD_NODE_LOG_FILENAME);
                                LogUtils.log(heartbeat.toString(), LogUtils.FD_NODE_LOG_FILENAME);
                            } else {
                                //Log
                                LogUtils.log("ERROR: pending heartbeats list is empty", LogUtils.RING_NODE_LOG_FILENAME);
                            }

                        } else {
                            //Perform action and acknowledge
                            sendAck(frame);
                            //Log
                            LogUtils.log("Acknowledge sent", LogUtils.FD_NODE_LOG_FILENAME);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
