import network.Configuration;

public class Main {

    public static void main(String[] args) {
        Configuration netconfig = Configuration.readConfigFromFile("src/network/netconfig.txt");
        System.out.println(netconfig.toString());
    }
}
