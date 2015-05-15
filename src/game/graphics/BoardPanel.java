package game.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class BoardPanel extends JComponent{

	/**
	 * The scale of the board
	 */
	private int size;
	
	private static final long serialVersionUID = 6462284276834153115L;
	
	/**
	 * the image for the board
	 */
	private BufferedImage boardImage;
	
    public BoardPanel(int numPlayers, int size) throws IOException {
    	this.size = size;
    	File boardFile;
    	
    	if(numPlayers == 4){
    		boardFile = new File("./Images/ChessBoard.png");
    	}
    	else {
    		boardFile = new File("./Images/ChessBoard2.png");
    	}
        boardImage = ImageIO.read(boardFile);
    }
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(boardImage.getScaledInstance(size * 14, size * 14, Image.SCALE_SMOOTH), 0, 0, null);
    }
}
