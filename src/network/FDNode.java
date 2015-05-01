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

public class FDNode extends Thread{

    public static final int MAX_BUFFER = 1024;
    public static final String NETWORK_CONFIGURATION_FILENAME = "netconfig.txt";
    //5 minutes (5 * 1000 = 5 seconds * 60 = 300000) in order for a node to be considered failed
    public static final long TIME_TO_FAILURE = 10;

    private int id;
    private boolean failureDetected;
    private FDNetConfiguration netConfig;
    private DatagramSocket socket;
    private boolean running;
    private List<Heartbeat> pendingHeartbeats;
    private ScheduledExecutorService scheduledExecutorService;

    public FDNode(int id){
        try {
            this.id = id;
            this.netConfig = FDNetConfiguration.readConfigFromFile(FDNode.NETWORK_CONFIGURATION_FILENAME);
            this.socket = new DatagramSocket(netConfig.getLocalPort(), netConfig.getLocalAddress());
            this.pendingHeartbeats = new ArrayList<Heartbeat>();
            this.scheduledExecutorService = Executors.newScheduledThreadPool(3);
            this.failureDetected = false;
            this.running = true;
            //log
            LogUtils.createLog(LogUtils.FD_NODE_LOG_FILENAME);
            LogUtils.log("Failure detection node created with id: " + this.id, LogUtils.FD_NODE_LOG_FILENAME);
            LogUtils.log(this.netConfig.toString(), LogUtils.FD_NODE_LOG_FILENAME);
        } catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private void failureDetected(){
        this.failureDetected = true;
    }

    private void sendAck(DataFrame frame){
        frame.swapAddresses();
        frame.setAck(true);
        sendFrame(frame);
    }

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

    public void run() {
        //Log
        LogUtils.log("FD node " + this.id + " started", LogUtils.FD_NODE_LOG_FILENAME);
        System.out.println("FD node " + this.id + " started");
        //Sender task for the ExecutorService (executed each minute)
        this.scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                //Send heartbeat to predecessor
                HeartbeatFrame hp = new HeartbeatFrame(netConfig.getPredecessorPort(), netConfig.getPredecessorAddr(),
                        netConfig.getLocalPort(), netConfig.getLocalAddress());
                sendFrame(hp);
                pendingHeartbeats.add(hp.getHeartbeat());
                //Log
                LogUtils.log("Predecessor heartbeat sent", LogUtils.FD_NODE_LOG_FILENAME);
                System.out.println("Predecessor heartbeat sent");
                //Sed heartbeat to successor
                HeartbeatFrame hs = new HeartbeatFrame(netConfig.getSuccessorPort(), netConfig.getSuccessorAddr(),
                        netConfig.getLocalPort(), netConfig.getLocalAddress());
                sendFrame(hs);
                pendingHeartbeats.add(hs.getHeartbeat());
                //Log
                LogUtils.log("Successor heartbeat sent", LogUtils.FD_NODE_LOG_FILENAME);
                System.out.println("Successor heartbeat sent");
            }
        }, 5, 5, TimeUnit.SECONDS);

        //Failure checker for the ExecutorService (executed each 5 minutes)
        this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //Check if there is any heartbeat in the list that have not been acknowledge
                //in the past 5 minutes (max temp for a player to move)
                //Log
                LogUtils.log("Failure checker started", LogUtils.FD_NODE_LOG_FILENAME);
                System.out.println("Failure checker started");
                int count = 0;
                for (Heartbeat h : pendingHeartbeats) {
                    long hTimestamp = h.getTimestamp() + FDNode.TIME_TO_FAILURE;
                    long currentTimestamp = System.currentTimeMillis();
                    if (currentTimestamp >= hTimestamp) {
                        count++;
                        //Failure detected from node h.getNodeAddress()
                        failureDetected();
                        //Log
                        LogUtils.log("Failure detected. Heartbeat not acknowledge after " + FDNode.TIME_TO_FAILURE
                                    , LogUtils.FD_NODE_LOG_FILENAME);
                        LogUtils.log(h.toString(), LogUtils.FD_NODE_LOG_FILENAME);
                        System.out.println("Failure detected. Heartbeat not acknowledge after ");
                    }
                }
                if(count == 0){
                    //Log
                    LogUtils.log("Failure checker finished: no failures detected", LogUtils.FD_NODE_LOG_FILENAME);
                    System.out.println("Failure checker finished: no failures detected");
                }
            }
        }, 20, 20, TimeUnit.SECONDS);

        //Receiver of the heartbeats and acknowledges to them
        this.scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                DatagramPacket packet = null;
                byte[] buffer;
                try {
                    while (running) {
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
                        System.out.println("Frame received");

                        if (frame.getAck()) {
                            //Remove frame from list of acknowledges pending
                            //It is assumed that the list contains the heartbeat
                            Heartbeat heartbeat = frame.getHeartbeat();
                            if (!pendingHeartbeats.isEmpty()) {
                                //Remove in a while loop to remove all the heartbeats from the same node
                                //that have a timestamp lower or equal to the heartbeat (later heartbeats
                                //will have to be acknowledge again.
                                while (!pendingHeartbeats.isEmpty() && pendingHeartbeats.remove(heartbeat))
                                    System.out.println(pendingHeartbeats.toString());
                                //Log
                                LogUtils.log("Acknowledge received", LogUtils.FD_NODE_LOG_FILENAME);
                                LogUtils.log(heartbeat.toString(), LogUtils.FD_NODE_LOG_FILENAME);
                                System.out.println("Acknowledge received");
                            } else {
                                //Log
                                LogUtils.log("ERROR: pending heartbeats list is empty", LogUtils.RING_NODE_LOG_FILENAME);
                            }

                        } else {
                            //Perform action and acknowledge
                            sendAck(frame);
                            //Log
                            LogUtils.log("Acknowledge sent", LogUtils.FD_NODE_LOG_FILENAME);
                            System.out.println("Acknowledge sent");
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
