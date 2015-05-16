import game.control.Chess;
import game.graphics.GraphicInterface;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try {
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
