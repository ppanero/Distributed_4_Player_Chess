package network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

/**
 * This class represents the configuration of the network of one node.
 * The network is supposed to be organized in a ring topology. Therefore the
 * node has the address of its predecessorAddr, successorAddr, and itself. It also has the
 * localPort to be used when communicating.
 */
public class GameNetConfiguration {

    private static final String LOCAL_ADDRESS_CONFIG_NAME = "localPlayerAddr";
    private static final String LOCAL_PORT_CONFIG_NAME = "localPlayerPort";
    private static final String SECOND_ADDRESS_CONFIG_NAME = "secondPlayerAddr";
    private static final String SECOND_PORT_CONFIG_NAME = "secondPlayerPort";
    private static final String THIRD_ADDRESS_CONFIG_NAME = "thirdPlayerAddr";
    private static final String THIRD_PORT_CONFIG_NAME = "thirdPlayerPort";
    private static final String FOURTH_ADDRESS_CONFIG_NAME = "fourthPlayerAddr";
    private static final String FOURTH_PORT_CONFIG_NAME = "fourthPlayerPort";

    private InetAddress localAddr;
    private InetAddress secondAddr;
    private InetAddress thirdAddr;
    private InetAddress fourthAddr;
    private int localPort;
    private int secondPort;
    private int thirdPort;
    private int fourthPort;


    public GameNetConfiguration(InetAddress localAddr, InetAddress secondAddr, InetAddress thirdAddr,
                                InetAddress fourthAddr, int localPort, int secondPort, int thirdPort,
                                int fourthPort) {
        this.localAddr = localAddr;
        this.secondAddr = secondAddr;
        this.thirdAddr = thirdAddr;
        this.fourthAddr = fourthAddr;
        this.localPort = localPort;
        this.secondPort = secondPort;
        this.thirdPort = thirdPort;
        this.fourthPort = fourthPort;
    }

    public void setLocalAddr(InetAddress localAddr) {
        this.localAddr = localAddr;
    }

    public void setSecondAddr(InetAddress secondAddr) {
        this.secondAddr = secondAddr;
    }

    public void setThirdAddr(InetAddress thirdAddr) {
        this.thirdAddr = thirdAddr;
    }

    public void setFourthAddr(InetAddress fourthAddr) {
        this.fourthAddr = fourthAddr;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public void setSecondPort(int secondPort) {
        this.secondPort = secondPort;
    }

    public void setThirdPort(int thirdPort) {
        this.thirdPort = thirdPort;
    }

    public void setFourthPort(int fourthPort) {
        this.fourthPort = fourthPort;
    }

    public InetAddress getLocalAddr() {
        return localAddr;
    }

    public InetAddress getSecondAddr() {
        return secondAddr;
    }

    public InetAddress getThirdAddr() {
        return thirdAddr;
    }

    public InetAddress getFourthAddr() {
        return fourthAddr;
    }

    public int getLocalPort() {
        return localPort;
    }

    public int getSecondPort() {
        return secondPort;
    }

    public int getThirdPort() {
        return thirdPort;
    }

    public int getFourthPort() {
        return fourthPort;
    }

    public static GameNetConfiguration readConfigFromFile(String filename){

        BufferedReader in = null;
        String line;
        InetAddress localp = null, secondp = null, thirdp = null, fourthp = null;
        int lport = 0, sport = 0, tport = 0, fport = 0;

        try {
            in = new BufferedReader(new FileReader(filename));
            while((line = in.readLine()) != null)
            {
                System.out.println(line);
                String[] data = line.split("=");
                if(data[0].equals(LOCAL_ADDRESS_CONFIG_NAME)){
                    localp = InetAddress.getByName(data[1]);
                }
                else if(data[0].equals(LOCAL_PORT_CONFIG_NAME)){
                    lport = Integer.parseInt(data[1]);
                }
                else if(data[0].equals(SECOND_ADDRESS_CONFIG_NAME)){
                    secondp = InetAddress.getByName(data[1]);
                }
                else if(data[0].equals(SECOND_PORT_CONFIG_NAME)){
                    sport = Integer.parseInt(data[1]);
                }
                else if(data[0].equals(THIRD_ADDRESS_CONFIG_NAME)){
                    thirdp = InetAddress.getByName(data[1]);
                }
                else if(data[0].equals(THIRD_PORT_CONFIG_NAME)){
                    tport = Integer.parseInt(data[1]);
                }
                else if(data[0].equals(FOURTH_ADDRESS_CONFIG_NAME)){
                    fourthp = InetAddress.getByName(data[1]);
                }
                else if(data[0].equals(FOURTH_PORT_CONFIG_NAME)){
                    fport = Integer.parseInt(data[1]);
                }
            }
            in.close();
            /*localp = InetAddress.getLocalHost();
            secondp = InetAddress.getLocalHost();
            thirdp = InetAddress.getLocalHost();
            fourthp = InetAddress.getLocalHost();*/
        } catch (FileNotFoundException e) {
            System.out.println("The file " + filename + " was not found");
        } catch (IOException e) {
            System.out.println("Error while reading from the network configuration file");
        }

        return new GameNetConfiguration(localp, secondp, thirdp, fourthp, lport, sport, tport, fport);
    }

    @Override
    public String toString() {
        return  "host address:          " + this.localAddr.getHostAddress() + '\n' +
                "host port:             " + this.localPort + '\n' +
                "second player address:   " + this.secondAddr.getHostAddress() + '\n' +
                "second player port:      " + this.secondPort + '\n' +
                "third player address:     " + this.thirdAddr.getHostAddress() + '\n' +
                "third player port:        " + this.thirdPort +'\n' +
                "fourth player address:     " + this.fourthAddr.getHostAddress() + '\n' +
                "fourth player port:        " + this.fourthPort;
    }
}
