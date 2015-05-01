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
public class FDNetConfiguration {

    private static final String LOCAL_CONFIG_NAME = "localAddr";
    private static final String LOCAL_PORT_CONFIG_NAME = "localPort";
    private static final String PREDECESSOR_CONFIG_NAME = "predecessorAddr";
    private static final String PREDECESSOR_PORT_CONFIG_NAME="predecessorPort";
    private static final String SUCCESSOR_CONFIG_NAME = "successorAddr";
    private static final String SUCCESSOR_PORT_CONFIG_NAME="successorPort";

    private InetAddress localAddress;
    private InetAddress predecessorAddr;
    private InetAddress successorAddr;
    private int localPort;
    private int predecessorPort;
    private int successorPort;

    public InetAddress getPredecessorAddr() {
        return predecessorAddr;
    }

    public void setPredecessorAddr(InetAddress predecessorAddr) {
        this.predecessorAddr = predecessorAddr;
    }

    public InetAddress getSuccessorAddr() {
        return successorAddr;
    }

    public void setSuccessorAddr(InetAddress successorAddr) {
        this.successorAddr = successorAddr;
    }

    public InetAddress getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(InetAddress localAddress) {
        this.localAddress = localAddress;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public int getPredecessorPort() {
        return predecessorPort;
    }

    public void setPredecessorPort(int predecessorPort) {
        this.predecessorPort = predecessorPort;
    }

    public int getSuccessorPort() {
        return successorPort;
    }

    public void setSuccessorPort(int successorPort) {
        this.successorPort = successorPort;
    }

    public FDNetConfiguration(InetAddress p, InetAddress s, InetAddress h, int lport, int pport, int sport){
        this.setPredecessorAddr(p);
        this.setSuccessorAddr(s);
        this.setLocalAddress(h);
        this.setLocalPort(lport);
        this.setPredecessorPort(pport);
        this.setSuccessorPort(sport);
    }

    public static FDNetConfiguration readConfigFromFile(String filename){

        BufferedReader in = null;
        String line;
        InetAddress predecessor = null, successor = null, host = null;
        int lport = 0, pport = 0, sport = 0;

        try {
            in = new BufferedReader(new FileReader(filename));
            while((line = in.readLine()) != null)
            {
                System.out.println(line);
                String[] data = line.split("=");
                if(data[0].equals(PREDECESSOR_CONFIG_NAME)){
                    predecessor = InetAddress.getByName(data[1]);
                }
                else if(data[0].equals(SUCCESSOR_CONFIG_NAME)){
                    successor = InetAddress.getByName(data[1]);
                }
                else if(data[0].equals(LOCAL_CONFIG_NAME)){
                    host = InetAddress.getByName(data[1]);
                }
                else if(data[0].equals(LOCAL_PORT_CONFIG_NAME)){
                    lport = Integer.parseInt(data[1]);
                }
                else if(data[0].equals(PREDECESSOR_PORT_CONFIG_NAME)){
                    pport = Integer.parseInt(data[1]);
                }
                else if(data[0].equals(SUCCESSOR_PORT_CONFIG_NAME)){
                    sport = Integer.parseInt(data[1]);
                }
            }
            in.close();
            host = InetAddress.getLocalHost();
            successor = InetAddress.getLocalHost();
            predecessor = InetAddress.getLocalHost();
        } catch (FileNotFoundException e) {
            System.out.println("The file " + filename + " was not found");
        } catch (IOException e) {
            System.out.println("Error while reading from the network configuration file");
        }

        return new FDNetConfiguration(predecessor, successor, host, lport, pport, sport);
    }

    @Override
    public String toString() {
        return  "host address:          " + this.localAddress.getHostAddress() + '\n' +
                "host port:             " + this.localPort + '\n' +
                "predecessor address:   " + this.predecessorAddr.getHostAddress() + '\n' +
                "predecessor port:      " + this.predecessorPort + '\n' +
                "successor address:     " + this.successorAddr.getHostAddress() + '\n' +
                "successor port:        " + this.successorPort;
    }
}
