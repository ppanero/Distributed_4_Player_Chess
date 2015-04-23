package network;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

/**
 * This class represents the configuration of the network of one node.
 * The network is supposed to be organized in a ring topology. Therefore the
 * node has the address of its predecessor, successor, and itself. It also has the
 * port to be used when communicating.
 */
public class Configuration {

    private static final String PREDECESSOR_CONFIG_NAME = "predecessor";
    private static final String SUCCESSOR_CONFIG_NAME = "successor";
    private static final String SOCKET_CONFIG_NAME = "port";

    private InetAddress predecessor;
    private InetAddress successor;
    private InetAddress hostAddress;
    private int port;

    public InetAddress getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(InetAddress predecessor) {
        this.predecessor = predecessor;
    }

    public InetAddress getSuccessor() {
        return successor;
    }

    public void setSuccessor(InetAddress successor) {
        this.successor = successor;
    }

    public InetAddress getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(InetAddress hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Configuration(InetAddress p, InetAddress s, InetAddress h, int port){
        this.setPredecessor(p);
        this.setSuccessor(s);
        this.setHostAddress(h);
        this.setPort(port);
    }

    public static Configuration readConfigFromFile(String filename){

        BufferedReader in = null;
        String line;
        InetAddress predecessor = null, successor = null, host = null;
        int port = 0;

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
                else if(data[0].equals(SOCKET_CONFIG_NAME)){
                    port = Integer.parseInt(data[1]);
                }
            }
            in.close();
            host = InetAddress.getLocalHost();
        } catch (FileNotFoundException e) {
            System.out.println("The file " + filename + " was not found");
        } catch (IOException e) {
            System.out.println("Error while reading from the network configuration file");
        }

        return new Configuration(predecessor, successor, host, port);
    }

    @Override
    public String toString() {
        return  "host address:          " + this.hostAddress.getHostAddress() + '\n' +
                "port:                " + this.port + '\n' +
                "predecessor address:   " + this.predecessor.getHostAddress() + '\n' +
                "successor address:     " + this.successor.getHostAddress() + '\n';
    }
}
