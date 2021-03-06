package game.graphics;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

import game.pieces.Piece;
import game.pieces.Queen;
import game.pieces.Piece.PlayerNum;

public class PiecePanel extends JComponent {

	private static final long serialVersionUID = -7886079846173653491L;
	private BufferedImage pieceImage;
	private int size;
	private Piece piece;

	public PiecePanel(Piece piece, int size) throws IOException {
		this.piece = piece;
		this.size = size;
		if (piece == null) {
			File pieceImageFile = new File("./Images/Objects/BlankSquare.png");
            pieceImage = ImageIO.read(pieceImageFile);
			piece = new Queen(PlayerNum.EMPTY);
		} else {
			File pieceImageFile = new File("./Images/Pieces/"
					+ piece.getImageFileName());
            pieceImage = ImageIO.read(pieceImageFile);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(
				pieceImage.getScaledInstance(size, size, Image.SCALE_SMOOTH),
				0, 0, null);
	}

	public Piece getPiece() {
		return piece;
	}
}
