package game.graphics;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import game.control.Chess;
import game.pieces.Move;
import game.pieces.Piece.PlayerNum;
import game.control.Board;
import game.control.Player;

public class GraphicInterface extends JFrame {

	private static final int BOARD_LAYER = 0;
	private static final int PIECE_LAYER = 1;
	private static final int HIGHLIGHTED_LAYER = 2;
	private PiecePanel[][] myGrid;
	private HighlightedPanel[][] myHighlighted;
	private JScrollPane pane;
	private static final int SQUARE_SIZE = 30;
	private static final long serialVersionUID = -3023440964932623825L;
	private Highlighted[][] initializeHighlighted;
	private Board board;
    private boolean isTurn = false;
    private Player currentPlayer = null;
    private Player lastPlayer = null;
    private Chess chessController;

	public enum Highlighted {
		BLANK, YELLOW, RED;
	}

	/**
	 * Mouse listener that determines what piece is being selected, where
	 * destination is being selected. on each mouseclick, checking if need to
	 * display checkWindow
	 */
	private class MyMouseListener implements MouseListener {
		
		@Override
		public void mouseClicked(MouseEvent event) {
            if(isTurn) {
                int x = event.getX();
                int y = event.getY();
                // on each click, determines if king of current player is in check,
                // opens dialog box if true.
                ArrayList<PlayerNum> myList = new ArrayList<PlayerNum>();
                myList = board.getPlayersInCheck();

                lastPlayer = currentPlayer;
                currentPlayer = board.getCurrentPlayer();
                if (currentPlayer != lastPlayer && myList.contains(board.getCurrentPlayer().getPlayerNum())) {
                    JOptionPane
                            .showMessageDialog(GraphicInterface.this,
                                    "You are in CHECK! so move to stay alive!");
                }
                // passes x and y coordinates to drawHighlighted, which returns
                // array of available moves.
                try {
                    drawHighlighted(board.selectLocation((x / SQUARE_SIZE), (y / SQUARE_SIZE)));
                    drawPiecesAndRepaint();
                    // on each mouseclick, checks if game is over
                    if (board.isGameOver()) {
                        JOptionPane.showMessageDialog(GraphicInterface.this, "Game over");
                        GraphicInterface.this.createNewGame();
                    }
                } catch (IOException ioe) {
                    System.err.println(ioe.toString());
                }
            }
            else{
                JOptionPane.showMessageDialog(GraphicInterface.this, "Not your turn yet, WAIT");
            }
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			//Do nothing
		}

		@Override
		public void mouseExited(MouseEvent e) {
			//Do nothing
		}

		@Override
		public void mousePressed(MouseEvent e) {
			//Do nothing
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			//Do nothing
		}
	}

    /**
     * Sets up the chess game controller to the view
     * @param controller
     */
    public void setChessController(Chess controller){
        chessController = controller;
    }

	/**
	 * creates new board instance, creates and prepares jpane for game.
	 * @throws IOException
	 */
	public GraphicInterface() throws IOException {
		// creates a new board for four player chess
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout());
        board = new Board(4, 4);
        JPanel boardImage = new BoardPanel(4, SQUARE_SIZE);
        boardImage.addMouseListener(new MyMouseListener());
        boardImage.setOpaque(false);
        boardImage.setBounds(0, 0, SQUARE_SIZE * 14, SQUARE_SIZE * 14);
        boardImage.setPreferredSize(new Dimension(SQUARE_SIZE * 14, SQUARE_SIZE * 14));
        boardImage.setEnabled(true);

		// creates new pane for pieces, highlighted squares to be drawn to
		pane = new JScrollPane();
        pane.setPreferredSize(new Dimension(SQUARE_SIZE * 14, SQUARE_SIZE * 14));
        pane.setEnabled(true);
		pane.add(boardImage, new Integer(BOARD_LAYER), 0);
        getContentPane().add(pane);

		// initializes all comparison arrays, draws pieces to board
		initializePieces();
		initializeHighlighted();
		createBlankHighlighted();
		redraw();
		pack();
		setPreferredSize(new Dimension(SQUARE_SIZE * 14, SQUARE_SIZE * 14 + 20));
		setResizable(true);
		setVisible(true);
        setEnabled(true);
	}

    /**
     * Tells the graphical interface to perform a movement
     * @param own - if its the players move or the oponents' one
     * @param move - the move to perform
     */
    public void executeMove(boolean own, Move move){
        if(own){
            JOptionPane.showMessageDialog(GraphicInterface.this, "Your turn! MOVE");
            isTurn = true;
        }
        else{
            int x = move.getX();
            int y = move.getY();
            // on each click, determines if king of current player is in check,
            // opens dialog box if true.
            ArrayList<PlayerNum> myList = new ArrayList<PlayerNum>();
            myList = board.getPlayersInCheck();

            lastPlayer = currentPlayer;
            currentPlayer = board.getCurrentPlayer();
            if (currentPlayer != lastPlayer && myList.contains(board.getCurrentPlayer().getPlayerNum())) {
                JOptionPane.showMessageDialog(GraphicInterface.this, "You are in CHECK! so move to stay alive!");
            }
            // passes x and y coordinates to drawHighlighted, which returns
            // array of available moves.
            try{
                drawHighlighted(board.selectLocation((x / SQUARE_SIZE),(y / SQUARE_SIZE)));
                drawPiecesAndRepaint();
                // on each mouseclick, checks if game is over
                if (board.isGameOver()) {
                    JOptionPane.showMessageDialog(GraphicInterface.this, "Game over");
                    GraphicInterface.this.createNewGame();
                }
            }catch(IOException ioe){
                System.err.println(ioe.toString());
            }
        }
    }

    /**
	 * creates an array used to remove all highlighted squares from pane by comparison
	 */
	private void createBlankHighlighted() {
		initializeHighlighted = new Highlighted[14][14];
		for (int i = 0; i < 14; ++i) {
			for (int j = 0; j < 14; ++j) {
				initializeHighlighted[i][j] = Highlighted.BLANK;
			}
		}
	}

	/**
	 * Initializes myGrid to have the same piece-positions as board, allowing
	 * drawPiecesAndRepaint to have a "fresh" array to compare against.
	 * 
	 * @throws IOException
	 */
	private void initializePieces() throws IOException {
		myGrid = new PiecePanel[14][14];
		// create array of piece panels, will be used to compare with board
		// class. Corners are left null, blanks filled
		// with blank queens
		int x = 0, y = 0;
		for (int i = 0; i < SQUARE_SIZE * 14; i += SQUARE_SIZE, x++) {
			for (int j = 0; j < SQUARE_SIZE * 14; j += SQUARE_SIZE, y++) {
				//check if location in playfield
				if ((x < 3 && (y < 3 || y > 10))
						|| (x > 10 && (y < 3 || y > 10))) {
					continue;
				}
				PiecePanel temp = null;
				//if location should be left blank, fills space with "blankQueen", is just a placeholder
				if (board.getPiece(x, y).getPlayerNum() == PlayerNum.EMPTY)
					temp = new PiecePanel(null, SQUARE_SIZE);
				else
					//else, fetches piece type, creates new piece pane, adds to grid
					temp = new PiecePanel(board.getPiece(x, y), SQUARE_SIZE);

				myGrid[x][y] = temp;
			}
			y = 0;

			// draw to pane
		}
		x = 0;
		y = 0;
		for (int i = 0; i < SQUARE_SIZE * 14; i += SQUARE_SIZE, x++) {
			for (int j = 0; j < SQUARE_SIZE * 14; j += SQUARE_SIZE, y++) {
				if (myGrid[x][y] != null) {
					//adds to pane, but not yet redrawn
					pane.add(myGrid[x][y], new Integer(PIECE_LAYER), 0);
					myGrid[x][y].setBounds(i, j, SQUARE_SIZE, SQUARE_SIZE);
				}
			}
			y = 0;
		}
	}

	/**
	 * This function will reinitialize the myHighlighted array to none
	 * highlighted, so that when drawHighlighed is called, there is a "fresh"
	 * array to compare against.
	 * 
	 * @throws IOException
	 */
	private void initializeHighlighted() throws IOException {
		// fill array of highlighteds. Corners are filled with blanks
		myHighlighted = new HighlightedPanel[14][14];
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 14; j++) {
				myHighlighted[i][j] = new HighlightedPanel(Highlighted.BLANK,
						SQUARE_SIZE);
			}
		}
	}

	/**
	 * Performs all steps needed to create a new game- creating a new board,
	 * resetting the jpane
	 * 
	 * @throws IOException
	 */
	private void createNewGame() throws IOException {
		board = new Board(4, 4);
		// initializePieces();
		// initializeHighlighted();
		drawHighlighted(initializeHighlighted);
		drawPiecesAndRepaint();
	}

	/**
	 * repaints pane
	 */
	private void redraw() {
		// update pane
		this.pane.repaint();
	}

	/**
	 * Compares board to myGrid, updating pane and removing pieces before
	 * updating myGrid
	 * 
	 * @throws IOException
	 */
	private void drawPiecesAndRepaint() throws IOException {
		PiecePanel piece;
		int x = 0, y = 0;
		for (int i = 0; i < SQUARE_SIZE * 14; i += SQUARE_SIZE, x++) {
			for (int j = 0; j < SQUARE_SIZE * 14; j += SQUARE_SIZE, y++) {
				if ((x < 3 && (y < 3 || y > 10))
						|| (x > 10 && (y < 3 || y > 10))) {
					continue;
				}
				if (!board.getPiece(x, y).equals(
						myGrid[x][y] == null ? null : myGrid[x][y].getPiece())) {
					//if location is not in corners (out of bounds)
					if (myGrid[x][y] != null) {
						//if need to be empty, fill with blank queen
						if (board.getPiece(x, y).getPlayerNum() == PlayerNum.EMPTY) {
							piece = new PiecePanel(null, SQUARE_SIZE);
							//otherwise, creates new piece for location
						} else {
							piece = new PiecePanel(board.getPiece(x, y),
									SQUARE_SIZE);
                            this.chessController.addMove(new Move(x,y,piece.getPiece()));
                            isTurn = false;
						}
						//adds to pane
						pane.remove(myGrid[x][y]);
						myGrid[x][y] = piece;
						piece.setBounds(i, j, SQUARE_SIZE, SQUARE_SIZE);
						pane.add(piece, new Integer(PIECE_LAYER), 0);
					}
				}
			}
			y = 0;
		}
		this.pane.repaint();
	}

	/**
	 * Compares passed array to myHighlighted, removing and updating highlighted
	 * squares to pane to make them the same. Updates myHighlighted afterward
	 * 
	 * @param arr
	 * @throws IOException
	 */
	private void drawHighlighted(Highlighted[][] arr) throws IOException {
		HighlightedPanel piece;
		int x = 0, y = 0;
		for (int i = 0; i < SQUARE_SIZE * 14; i += SQUARE_SIZE, x++) {
			for (int j = 0; j < SQUARE_SIZE * 14; j += SQUARE_SIZE, y++) {
				if ((x < 3 && (y < 3 || y > 10))
						|| (x > 10 && (y < 3 || y > 10))) {
					continue;
				}
				//same methodology as in drawPiecesAndRepaint(comparison)
				if (!arr[x][y].equals(myHighlighted[x][y] == null ? null
						: myHighlighted[x][y].getEnum())) {
					if (myHighlighted[x][y] != null) {
						pane.remove(myHighlighted[x][y]);
						piece = new HighlightedPanel(arr[x][y], SQUARE_SIZE);
						myHighlighted[x][y] = piece;
						piece.setBounds(i, j, SQUARE_SIZE, SQUARE_SIZE);
						pane.add(piece, new Integer(HIGHLIGHTED_LAYER), 0);
					}
				}
			}
			y = 0;
		}
	}

    /**
     * Starts the graphical interface
     */
    public void startGI(){
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            }
        });
    }
}
