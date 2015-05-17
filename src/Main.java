import com.mchange.v2.c3p0.ComboPooledDataSource;
import database.DatabaseManager;
import game.control.Chess;
import game.graphics.GraphicInterface;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;


public class Main {

    public static void main(String[] args) {
        try {
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            DataSource ds;
            Connection con = null;
            try {
                cpds.setDriverClass("com.mysql.jdbc.Driver");
            } catch (PropertyVetoException e1) {
                e1.printStackTrace();
            }
            cpds.setJdbcUrl("jdbc:mysql://localhost/fpchess");
            cpds.setUser("root");
            cpds.setPassword("bAtsDUpX");
            cpds.setAcquireRetryAttempts(1);
            cpds.setAcquireRetryDelay(1);
            cpds.setBreakAfterAcquireFailure(true);
            ds = cpds;
            DatabaseManager dm = new DatabaseManager(ds);
            dm.initializeDatabase();
            final Chess controller = new Chess();
            GraphicInterface fpchess = new GraphicInterface();
            fpchess.setChessController(controller);
            controller.setGI(fpchess);

            fpchess.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    controller.stopGame();
                    System.exit(0);
                }
            });

            fpchess.startGI();
            controller.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
