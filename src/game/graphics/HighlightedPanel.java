package game.graphics;

import game.graphics.GraphicInterface.Highlighted;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class HighlightedPanel extends JComponent{

	private static final long serialVersionUID = -7886079846173653491L;
	
	/**
	 * The actual image that will load when the piece is called.
	 */
	private BufferedImage pieceImage;
	
	/**
	 * The scale of the game
	 */
	private int size;
	
	/**
	 * Contains the information about what type of highlighting it needs
	 */
	private Highlighted highlighted;
	
	/**
	 * The image file that contains the image the piece will use
	 */
	File pieceImageFile;
	
	public HighlightedPanel(Highlighted piece,  int size) throws IOException {
		this.size = size;
		highlighted = piece;
		
		//This code is kind of like a loading bar. It lets the user know what highlighted pieces are being loaded in
		//in the console window as they are loaded
		if(piece == Highlighted.BLANK){
			//System.out.println("Highlighted ./Images/Objects/" + "BlankSquare.png");
			this.pieceImageFile = new File ("./Images/Objects/" + "BlankSquare.png");
		}else if(piece == Highlighted.YELLOW){
			System.out.println("Highlighted ./Images/Objects/" + "HighlightedSquare.png");
			this.pieceImageFile = new File ("./Images/Objects/" + "HighlightedSquare.png");
		}else{
			System.out.println("Highlighted ./Images/Objects/" + "RedPiece.png");
			this.pieceImageFile = new File ("./Images/Objects/" + "HighlightedSquareRed.png");
			//TODO
		}
		pieceImage = ImageIO.read(pieceImageFile);
		
		//This code only prints if the piece can't be read for whatever reason
		if(pieceImage == null) {
			System.out.println("Piece Image null: " + pieceImageFile.toString());
		}
    }
	@Override
    protected void paintComponent(Graphics g) {
		 
//     g.drawImage(pieceImage2.getScaledInstance(size,size, Image.SCALE_SMOOTH), 0, 0, null);
     g.drawImage(pieceImage.getScaledInstance(size,size, Image.SCALE_FAST), 0, 0, null);
        
    }
	public Highlighted getEnum() {
		return highlighted;
	}
}
