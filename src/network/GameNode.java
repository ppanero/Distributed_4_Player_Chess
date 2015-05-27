package network;

import Utils.CryptoUtils;
import Utils.LogUtils;
import Utils.NetworkUtils;
import game.pieces.Move;
import org.apache.commons.lang3.SerializationUtils;

import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

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

public class GameNode<T> extends Thread{
	
	public static final int MAX_BUFFER = 2048;
    public static final String GAME_CONFIGURATION_FILENAME = "gameconfig.txt";
    //1 minute (60 * 1000 = 60 seconds) in order for a message to be considered failed
    public static final long TIME_TO_FAILURE = 60;
	
	private int id;
    private int msgId;
    private GameNetConfiguration gameConfig;
    private DatagramSocket socket;
    private boolean receiving;
    private boolean playing;
    private boolean running;
    private boolean hasToken;
    private List<GameFrame<T>> ackPendingGameframes;
    private BlockingQueue<T> sendPendingObjects;
    private BlockingQueue<T> receivePendingObjects;
    private ScheduledExecutorService scheduledExecutorService;
    private CryptoUtils encCipher;
    private CryptoUtils decCipher;
	
	public GameNode(int id){
		try {
			this.id = id;
            this.msgId = 0;
			this.gameConfig = GameNetConfiguration.readConfigFromFile(this.GAME_CONFIGURATION_FILENAME);
			this.socket = new DatagramSocket(gameConfig.getLocalPort(), gameConfig.getLocalAddr());
            this.playing = true;
            this.running = true;
            this.hasToken = false;
            this.ackPendingGameframes = new ArrayList<GameFrame<T>>();
            this.sendPendingObjects = new ArrayBlockingQueue<T>(10);
            this.receivePendingObjects = new ArrayBlockingQueue<T>(10);
            this.scheduledExecutorService = Executors.newScheduledThreadPool(4);
            this.receiving = true;
            this.encCipher = new CryptoUtils(true);
            this.decCipher = new CryptoUtils(false);
            //log
            LogUtils.log("Ring node initiated with id: " + this.id, LogUtils.RING_NODE_LOG_FILENAME);
            LogUtils.log(this.gameConfig.toString(), LogUtils.RING_NODE_LOG_FILENAME);
		} catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

    private void sendToken(DataFrame frame){
        frame.setSourceAddr(this.gameConfig.getLocalAddr());
        frame.setSourcePort(this.gameConfig.getLocalPort());
        frame.setDestinationPort(this.gameConfig.getSecondPort());
        frame.setDestinationAddr(this.gameConfig.getSecondAddr());
        sendFrame(frame);
    }

    private void sendAck(DataFrame frame){
        frame.swapAddresses();
        frame.setAck(true);
        sendFrame(frame);
    }

    private int nextMsgId(){
        this.msgId++;
        return this.msgId;
    }

	//send a frame with the DatagramPacket through the socket
	private void sendFrame(DataFrame frame){
			
		DatagramPacket packet = null;
		byte[] buf;

		try{
			/*ByteArrayOutputStream fis = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(fis);
			CipherOutputStream cos = new CipherOutputStream(oos, this.encCipher.getCipher());
			oos.writeObject(frame);
			oos.flush();
            cos.write(fis.toByteArray());
            cos.flush();
            buf = fis.toByteArray();*/
            buf = this.encCipher.encrypt(NetworkUtils.serialize(frame));
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
        DataFrame frame = new GameFrame<Move>(this.gameConfig.getSecondPort(),this.gameConfig.getSecondAddr(),
                this.gameConfig.getLocalPort(), this.gameConfig.getLocalAddr());
        sendToken(frame);
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

    public void stopNode(){
        this.switchReceiving();
    }

    public void communicateMessage(T msg){
        try {
            this.sendPendingObjects.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public T pullMove() {
        try {
            return receivePendingObjects.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void run() {
        //Log
        LogUtils.log("Game node " + this.id + " started", LogUtils.RING_NODE_LOG_FILENAME);
        System.out.println("Game node " + this.id + " started");
        //Gameframe consumer sends the gameframe when they are put in the blocking queue
        this.scheduledExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                while (running && playing){
                    //Send the frame
                    try {
                        T obj = sendPendingObjects.take();
                        //Send frame to the second player
                        GameFrame<T> secondPlayerFrame = new GameFrame<T>(gameConfig.getSecondPort(), gameConfig.getSecondAddr(),
                                gameConfig.getLocalPort(), gameConfig.getLocalAddr(), obj, System.currentTimeMillis(), nextMsgId());
                        sendFrame(secondPlayerFrame);
                        System.out.println(secondPlayerFrame);
                        ackPendingGameframes.add(secondPlayerFrame);
                        //Send frame to the second player
                        GameFrame<T> thirdPlayerFrame = new GameFrame<T>(gameConfig.getThirdPort(), gameConfig.getThirdAddr(),
                                gameConfig.getLocalPort(), gameConfig.getLocalAddr(), obj, System.currentTimeMillis(), nextMsgId());
                        sendFrame(thirdPlayerFrame);
                        System.out.println(thirdPlayerFrame);
                        ackPendingGameframes.add(thirdPlayerFrame);
                        //Send frame to the second player
                        GameFrame<T> fourthPlayerFrame = new GameFrame<T>(gameConfig.getFourthPort(), gameConfig.getFourthAddr(),
                                gameConfig.getLocalPort(), gameConfig.getLocalAddr(), obj, System.currentTimeMillis(), nextMsgId());
                        sendFrame(fourthPlayerFrame);
                        System.out.println(fourthPlayerFrame);
                        ackPendingGameframes.add(fourthPlayerFrame);

                    } catch (InterruptedException iex) {

                    }
                }
            }
        });

        //Failure checker for the ExecutorService (executed each 5 minutes)
        this.scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //Check if there is any heartbeat in the list that have not been acknowledge
                //in the past 5 minutes (max temp for a player to move)
                //Log
                LogUtils.log("Game message failure checker started", LogUtils.FD_NODE_LOG_FILENAME);
                System.out.println("Game Message failure checker started");
                int count = 0;
                for (GameFrame<T> gf: ackPendingGameframes) {
                    long gfTimestamp = gf.getTimestamp() + GameNode.TIME_TO_FAILURE;
                    long currentTimestamp = System.currentTimeMillis();
                    if (currentTimestamp >= gfTimestamp) {
                        count++;
                        //Remove from the pending list the old frame
                        ackPendingGameframes.remove(gf);
                        //Game message failure detected, resending (update timestamp)
                        gf.setTimestamp(System.currentTimeMillis());
                        sendFrame(gf);
                        //Add the new frame to the ack pending list
                        ackPendingGameframes.add(gf);
                        //Log
                        LogUtils.log("Message failure detected. Heartbeat not acknowledge after " + GameNode.TIME_TO_FAILURE
                                , LogUtils.FD_NODE_LOG_FILENAME);
                        LogUtils.log(gf.toString(), LogUtils.FD_NODE_LOG_FILENAME);
                        System.out.println("Game message failure detected");
                    }
                }
                if(count == 0){
                    //Log
                    LogUtils.log("Game message failure checker finished: no failures detected", LogUtils.FD_NODE_LOG_FILENAME);
                    //System.out.println("Game message failure checker finished: no failures detected");
                }
            }
        }, 30, 30, TimeUnit.SECONDS);

        //Receiver of game frames. It deletes the ack from their pending list and acks other players frames
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

                        GameFrame frame = (GameFrame) NetworkUtils.deserialize(decCipher.decrypt(buffer));
                        System.out.println(frame);
                        //Log
                        LogUtils.log("Frame received", LogUtils.FD_NODE_LOG_FILENAME);
                        LogUtils.log(frame.toString(), LogUtils.FD_NODE_LOG_FILENAME);
                        System.out.println("Frame received");

                        if (frame.getAck()) {
                            //Remove frame from list of acknowledges pending
                            //It is assumed that the list contains the pending ack
                            if (!ackPendingGameframes.isEmpty()) {
                                //Remove in a while loop to remove all the heartbeats from the same node
                                //that have a timestamp lower or equal to the heartbeat (later heartbeats
                                //will have to be acknowledge again.
                                while (!ackPendingGameframes.isEmpty() && ackPendingGameframes.remove(frame))
                                    //System.out.println(ackPendingGameframes.toString());
                                //Log
                                LogUtils.log("Acknowledge received", LogUtils.FD_NODE_LOG_FILENAME);
                                LogUtils.log(frame.toString(), LogUtils.FD_NODE_LOG_FILENAME);
                                System.out.println("Acknowledge received");
                            } else {
                                //Log
                                LogUtils.log("ERROR: ack pending list is empty", LogUtils.RING_NODE_LOG_FILENAME);
                            }

                        } else {
                            //Perform action and acknowledge
                            receivePendingObjects.put((T) frame.getMessage());
                            sendAck(frame);
                            //Log
                            LogUtils.log("Acknowledge sent", LogUtils.FD_NODE_LOG_FILENAME);
                            System.out.println("Acknowledge sent");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
