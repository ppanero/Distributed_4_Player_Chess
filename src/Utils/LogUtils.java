package Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogUtils {

    public static final String FD_NODE_LOG_FILENAME = "fdlog.txt";
    public static final String RING_NODE_LOG_FILENAME = "rnlog.txt";
    public static final String GAME_LOG_FILENAME = "gamelog.txt";

    public static void createLog(String filename){
        File file = new File(filename);

        // if file doesnt exists, then create it
        if (!file.exists()) {
            try {
                if(!file.createNewFile()){
                    System.err.println("Could not create file " + filename);
                }
            } catch (IOException e) {
                System.err.println("Could not create file " + filename);
            }
        }
        else{
            FileWriter fw = null;
            try {
                fw = new FileWriter(file.getAbsoluteFile()); //To empty the file if existing
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("");
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(String content, String filename){
        File file = new File(filename);

        // if file doesnt exists, then create it
        if (!file.exists()) {
            System.err.println("Could not create file " + filename);
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(file.getAbsoluteFile(), true);

            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content + '\n');
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
