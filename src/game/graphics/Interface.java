package game.graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import game.graphics.PiecePanel;
import game.pieces.Piece.PlayerNum;
import game.control.Board;
import game.control.Player;

public class Interface extends JFrame {

	private static final int BOARD_LAYER = 0;
	private static final int PIECE_LAYER = 1;
	private static final int HIGHLIGHTED_LAYER = 2;
	private PiecePanel[][] myGrid;
	private HighlightedPanel[][] myHighlighted;
	private JLayeredPane pane;
	private static final int SQUARE_SIZE = 50;
	private static final long serialVersionUID = -3023440964932623825L;
	private Highlighted[][] initializeHighlighted;
	private Board board;
	private static boolean extraGame;

	public enum Highlighted {
		BLANK, YELLOW, RED;
	}

	/**
	 * Mouse listener that determines what piece is being selected, where
	 * destination is being selected. on each mouseclick, checking if need to
	 * display checkWindow
	 */
	private class MyMouseListener implements MouseListener {
		
		Player currentPlayer = null;
		Player lastPlayer = null;
		
		@Override
		public void mouseClicked(MouseEvent event) {
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
						.showMessageDialog(Interface.this,
								"You HAVE to move out of check. Come on. It's really the best move.");
			}
			// passes x and y coordinates to drawHighlighted, which returns
			// array of available moves.
			try{
				drawHighlighted(board.selectLocation((x / SQUARE_SIZE),(y / SQUARE_SIZE)));
				drawPiecesAndRepaint();
				// on each mouseclick, checks if game is over
				if (board.isGameOver()) {
					JOptionPane.showMessageDialog(Interface.this, "gg");
					Interface.this.createNewGame();
				}
			}catch(IOException ioe){
				System.err.println(ioe.toString());
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
	 * creates new board instance, creates and prepares jpane for game.
	 * @throws IOException
	 */
	public Interface() throws IOException {
		// creates a new board for four player chess
		board = new Board(4, 4);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// creates menubar menus
		JMenu newGameMenu = new JMenu("New Game");
		JMenu GameOptionMenu = new JMenu("Want a challenge?");
		// creates menu options
		JMenuItem fourPlayerMI = new JMenuItem("4 Player");
		JMenuItem MattAndCaleb = new JMenuItem("Play with extra pieces...");
		JMenuItem RegularPieces = new JMenuItem(
				"Play with regular pieces (boring)");
		// adds options to menus
		newGameMenu.add(fourPlayerMI);
		GameOptionMenu.add(MattAndCaleb);
		GameOptionMenu.add(RegularPieces);

		this.setLayout(new BorderLayout());

		// creates menu bar, adds menus
		JMenuBar bar = new JMenuBar();
		bar.add(newGameMenu);
		bar.add(GameOptionMenu);

		// when NewGame option selected, calls the createNewGame method.
		fourPlayerMI.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					createNewGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		// if ExtraPieces selected, sets ExtraGame to true
		MattAndCaleb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				extraGame = true;
				try {
					createNewGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		// If Boring pieces selected, set ExtraGame to false
		RegularPieces.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				extraGame = false;
				try {
					createNewGame();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		// creates new pane for pieces, highlighted squares to be drawn to
		this.pane = new JLayeredPane();
		pane.setBounds(0, 0, SQUARE_SIZE * 14 + 10, SQUARE_SIZE * 14 + 10);
		getContentPane().add(bar, BorderLayout.NORTH);
		getContentPane().add(pane, BorderLayout.CENTER);
		// adds boardImage to jpane, adds mouselistener, and sets bounds
		JComponent boardImage = new BoardPanel(4, SQUARE_SIZE);
		boardImage.addMouseListener(new MyMouseListener());
		boardImage.setOpaque(true);
		pane.add(boardImage, new Integer(BOARD_LAYER), 0);
		boardImage.setBounds(0, 0, SQUARE_SIZE * 14, SQUARE_SIZE * 14);

		// initializes all comparison arrays, draws pieces to board
		initializePieces();
		initializeHighlighted();
		createBlankHighlighted();
		redraw();

		pack();
		setSize(SQUARE_SIZE * 14 + 5, SQUARE_SIZE * 14 + 50);
		setResizable(false);
		setVisible(true);
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
	 * Returns value of "ExtraGame" variable. used when checking if
	 * "special pieces" are to be used.
	 * 
	 * @return true if special pieces are to be used
	 */
	public static boolean isExtraGame() {
		return extraGame;
	}
}
