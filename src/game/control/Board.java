package game.control;

import game.pieces.Piece;
import game.pieces.*;
import game.graphics.GraphicInterface.Highlighted;
import game.pieces.Piece.PlayerNum;
import javax.swing.JOptionPane;
import java.util.ArrayList;


public class Board {

	private Piece[][] board;
	private Player currentPlayer;
	private boolean[][] availableMoves;
	private Player player1;
	private Player player2;
	private Player player3;
	private Player player4;
	private boolean pieceSelected;
	private Piece selectedPiece;
	private Point selectedPoint;
	private static final int FOUR_P_LENGTH = 14;
	private static final int TWO_P_LENGTH = 8;
	private boolean gameOver = false;

	public Board() {
		this(4, 4);
	}

	/**
	 * Creates a new board object with the given number of players and human
	 * players. Sets pieces to proper locations in grid.
	 * 
	 * @param numPlayers
	 * @param numHumans
	 */
	public Board(int numPlayers, int numHumans) {
		pieceSelected = false;
		// for now, only works with 4 human players.
		//This initializes the board with all the pieces in their original spots
		if (numPlayers == 4) {
			board = new Piece[FOUR_P_LENGTH][FOUR_P_LENGTH];
			availableMoves = new boolean[FOUR_P_LENGTH][FOUR_P_LENGTH];
			switch (numHumans) {
			//This code was set up so that we could easily set up AI if we got to it.
			//Each case is a different number of human and AI players
			case 1:
				player1 = new Player(true, true, PlayerNum.ONE);
				player2 = new Player(false, true, PlayerNum.TWO);
				player3 = new Player(false, true, PlayerNum.THREE);
				player4 = new Player(false, true, PlayerNum.FOUR);
				break;
			case 2:
				player1 = new Player(true, true, PlayerNum.ONE);
				player2 = new Player(true, true, PlayerNum.TWO);
				player3 = new Player(false, true, PlayerNum.THREE);
				player4 = new Player(false, true, PlayerNum.FOUR);
				break;
			case 3:
				player1 = new Player(true, true, PlayerNum.ONE);
				player2 = new Player(true, true, PlayerNum.TWO);
				player3 = new Player(true, true, PlayerNum.THREE);
				player4 = new Player(false, true, PlayerNum.FOUR);
				break;
			case 4:
				player1 = new Player(true, true, PlayerNum.ONE);
				player2 = new Player(true, true, PlayerNum.TWO);
				player3 = new Player(true, true, PlayerNum.THREE);
				player4 = new Player(true, true, PlayerNum.FOUR);
				break;
			}
			// P1
			board[board.length - 1][3] = new Rook(PlayerNum.ONE);
			board[board.length - 1][10] = new Rook(PlayerNum.ONE);
			board[board.length - 1][4] = new Knight(PlayerNum.ONE);
			board[board.length - 1][9] = new Knight(PlayerNum.ONE);
			board[board.length - 1][5] = new Bishop(PlayerNum.ONE);
			board[board.length - 1][8] = new Bishop(PlayerNum.ONE);
			board[board.length - 1][6] = new Queen(PlayerNum.ONE);
			board[board.length - 1][7] = new King(PlayerNum.ONE);
			for (int j = 3; j < board.length - 3; j++) {
				board[board.length - 2][j] = new Pawn(PlayerNum.ONE);
			}
			// P2
			board[0][3] = new Rook(PlayerNum.TWO);
			board[0][10] = new Rook(PlayerNum.TWO);
			board[0][4] = new Knight(PlayerNum.TWO);
			board[0][9] = new Knight(PlayerNum.TWO);
			board[0][5] = new Bishop(PlayerNum.TWO);
			board[0][8] = new Bishop(PlayerNum.TWO);
			board[0][6] = new Queen(PlayerNum.TWO);
			board[0][7] = new King(PlayerNum.TWO);
			for (int j = 3; j < board.length - 3; j++) {
				board[1][j] = new Pawn(PlayerNum.TWO);
			}
			// P3
			board[3][board.length - 1] = new Rook(PlayerNum.THREE);
			board[10][board.length - 1] = new Rook(PlayerNum.THREE);
			board[4][board.length - 1] = new Knight(PlayerNum.THREE);
			board[9][board.length - 1] = new Knight(PlayerNum.THREE);
			board[5][board.length - 1] = new Bishop(PlayerNum.THREE);
			board[8][board.length - 1] = new Bishop(PlayerNum.THREE);
			board[6][board.length - 1] = new Queen(PlayerNum.THREE);
			board[7][board.length - 1] = new King(PlayerNum.THREE);
			for (int i = 3; i < board.length - 3; i++) {
				board[i][board.length - 2] = new Pawn(PlayerNum.THREE);
			}
			// P4
			board[3][0] = new Rook(PlayerNum.FOUR);
			board[10][0] = new Rook(PlayerNum.FOUR);
			board[4][0] = new Knight(PlayerNum.FOUR);
			board[9][0] = new Knight(PlayerNum.FOUR);
			board[5][0] = new Bishop(PlayerNum.FOUR);
			board[8][0] = new Bishop(PlayerNum.FOUR);
			board[6][0] = new Queen(PlayerNum.FOUR);
			board[7][0] = new King(PlayerNum.FOUR);
			for (int i = 3; i < board.length - 3; i++) {
				board[i][1] = new Pawn(PlayerNum.FOUR);
			}
			
			//Goes through and places blank queens.
			//What this means is that it places pieces that any player can overtake, but they have a transparent
			//image, so that the player never actually perceives them.
			for (int i = 0; i < board.length; ++i) {
				for (int j = 0; j < board.length; ++j) {
					if ((i < 3 && (j < 3 || j > 10))
							|| (i > 10 && (j < 3 || j > 10))) {
						continue;
					}
					if (board[i][j] == null) {
						board[i][j] = new Queen(PlayerNum.EMPTY);
					}
				}
			}
		} else {
			//This logic was kept in place in case we ever wanted to get make it so that you could
			//Also player two player chess with our logic.
			board = new Piece[TWO_P_LENGTH][TWO_P_LENGTH];
			availableMoves = new boolean[TWO_P_LENGTH][TWO_P_LENGTH];
			switch (numHumans) {
			case 1:
				player1 = new Player(true, true, PlayerNum.ONE);
				player2 = new Player(false, true, PlayerNum.TWO);
				player3 = new Player(false, false, PlayerNum.THREE);
				player4 = new Player(false, false, PlayerNum.FOUR);
				break;
			case 2:
				player1 = new Player(true, true, PlayerNum.ONE);
				player2 = new Player(true, true, PlayerNum.TWO);
				player3 = new Player(false, false, PlayerNum.THREE);
				player4 = new Player(false, false, PlayerNum.FOUR);
				break;
			}
			// P1
			board[board.length - 1][0] = new Rook(PlayerNum.ONE);
			board[board.length - 1][7] = new Rook(PlayerNum.ONE);
			board[board.length - 1][1] = new Knight(PlayerNum.ONE);
			board[board.length - 1][6] = new Knight(PlayerNum.ONE);
			board[board.length - 1][2] = new Bishop(PlayerNum.ONE);
			board[board.length - 1][5] = new Bishop(PlayerNum.ONE);
			board[board.length - 1][3] = new Queen(PlayerNum.ONE);
			board[board.length - 1][4] = new King(PlayerNum.ONE);
			for (int j = 0; j < board.length; j++) {
				board[board.length - 2][j] = new Pawn(PlayerNum.ONE);
			}
			// P2
			board[0][0] = new Rook(PlayerNum.TWO);
			board[0][7] = new Rook(PlayerNum.TWO);
			board[0][1] = new Knight(PlayerNum.TWO);
			board[0][6] = new Knight(PlayerNum.TWO);
			board[0][2] = new Bishop(PlayerNum.TWO);
			board[0][5] = new Bishop(PlayerNum.TWO);
			board[0][3] = new Queen(PlayerNum.TWO);
			board[0][4] = new King(PlayerNum.TWO);
			for (int j = 0; j < board.length - 3; j++) {
				board[1][j] = new Pawn(PlayerNum.TWO);
			}
		}
		currentPlayer = player1;
	}

	/**
	 * Returns the Piece at the given coordinate
	 * 
	 * @param x
	 *            the x coordinate of the piece on the board
	 * @param y
	 *            the y coordinate of the piece on the board
	 * @return Piece at the coordinates
	 */
	public Piece getPiece(int x, int y) {
		return board[x][y];
	}

	/**
	 * Process the event of the selecting a piece. Determines where a piece can
	 * move, setting proper locations to highlighted.
	 * 
	 * @param x
	 *            the x coordinate of the piece selected
	 * @param y
	 *            the y coordinate of the piece selected
	 * @returns 2D array containing which pieces should be highlighted
	 */
	public Highlighted[][] selectLocation(int x, int y) {
		//This sets the array of highlighted moves to blanks after a move is made
		Highlighted[][] highlightedArray = initializeBlankHighlightedArray();
		if (!inBounds(x, y)) {
			return highlightedArray;
		}
		//If a piece has not yet been selected:
		if (!pieceSelected) {
			availableMoves = new boolean[FOUR_P_LENGTH][FOUR_P_LENGTH];
			//If the player doesn't click on their own pieces, then nothing should happen
			if (board[x][y].getPlayerNum() != currentPlayer.getPlayerNum()) {
				return highlightedArray;
			}
			//Other wise, make the selected piece red-lighted
			highlightedArray[x][y] = Highlighted.RED;
			//Get the available moves from whatever piece was selected and change the highlighted array 
			//to true at those move's locations
			availableMoves = getAvailableMoves(currentPlayer.getPlayerNum(), x, y);
			for (int i = 0; i < FOUR_P_LENGTH; ++i) {
				for (int j = 0; j < FOUR_P_LENGTH; ++j) {
					//If the spot in the array is true, then highlight yellow
					if (availableMoves[i][j]) {
						highlightedArray[i][j] = Highlighted.YELLOW;
					}
				}
			}
			//If you select a piece of your own, then select the piece.
			if (board[x][y].getPlayerNum() == currentPlayer.getPlayerNum()) {
				pieceSelected = true;
				selectedPiece = board[x][y];
				selectedPoint = new Point(x, y);
			} else {
				pieceSelected = false;
			}
		}
		//Otherwise, if a piece is selected and it's showing it's available moves, then allow clicking on their moves
		else {
			if(currentPlayer.inCheck()){
			}
			if (availableMoves[x][y] == true) {
				Piece oldPiece = board[x][y];
				board[x][y] = selectedPiece;
				selectedPiece.move();
				board[selectedPoint.getX()][selectedPoint.getY()] = new Queen(
						PlayerNum.EMPTY);
				if(getPlayersInCheck().contains(currentPlayer.getPlayerNum())){
					board[selectedPoint.getX()][selectedPoint.getY()] = selectedPiece;
					board[x][y] = oldPiece;
					System.out.println("Didnt move out try again");
					highlightedArray = initializeBlankHighlightedArray();
				}else{
					afterMoveStuffz();
				}
			}
			//Reset all the variable used for clicking logic
			pieceSelected = false;
			selectedPiece = null;
			selectedPoint = null;
		}
		return highlightedArray;
	}

	/**
	 * Called after a piece has moved, determining players that are in check,
	 * updating currentPlayer, checking if checkMate, end of game, and removing
	 * checkMated players from play list.
	 */
	private void afterMoveStuffz() {
		//Checks for players in check
		getPlayersInCheck();
		//Rotates the player
		currentPlayer = getNextPlayer();
		if (currentPlayer == null) {
			gameOver = true;
		} else {
			if (isCheckmate()) {
				JOptionPane.showMessageDialog(null, "Checkmate on " + currentPlayer.getPlayerNum());
				removePlayer(currentPlayer);
				currentPlayer = getNextPlayer();
			}
		}
	}

	/**
	 * With given location and destination, determines if pawn can attack
	 * Destination. again, if kingCheck is true, disregards player types,
	 * checking for possible check.
	 * 
	 * @param move
	 * @param playerNum
	 * @param x
	 * @param y
	 * @param kingCheck
	 * @return true if pawn can attack destination, false otherwise
	 */
	private boolean pawnCanAttack(Move move, PlayerNum playerNum, int x, int y,
			boolean kingCheck) {
		int moveX = x + move.getX();
		int moveY = y + move.getY();
		if (inBounds(moveX, moveY)) {
			if (kingCheck) {
				return true;
			} else if (board[moveX][moveY].getPlayerNum() != currentPlayer
					.getPlayerNum()
					&& board[moveX][moveY].getPlayerNum() != PlayerNum.EMPTY) {
				return true;
			}
		}
		return false;
	}

	/**
	 * With the taken in playerNum type, determines which player it is referring
	 * to.
	 * 
	 * @param playerNum
	 * @return which player ENUM was referring to.
	 */
	private Player playerNumToPlayer(PlayerNum playerNum) {
		switch (playerNum) {
		case ONE:
			return player1;
		case TWO:
			return player2;
		case THREE:
			return player3;
		case FOUR:
			return player4;
		default:
			return null;
		}
	}
	
	/**
	 * Override for getAvailableMoves(PlayerNum playerNum, int x, int y, boolean kingCheck)
	 * Used in cases when not calling to see what can move on a king
	 */
	/**
	 * Overloaded type of getAvailableMoves, used only when determining
	 * highlighted locations (not when checking for check).
	 * 
	 * @param playerNum
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean[][] getAvailableMoves(PlayerNum playerNum, int x, int y) {
		return this.getAvailableMoves(playerNum, x, y, false);
	}

	/**
	 * Takes the piece at the given location, and returns an array of locations
	 * the piece is allowed to move to. If kingCheck variable is true, allows
	 * pieces to "overtake" pieces of their own type for purposes of checking
	 * for check.
	 * 
	 * @param playerNum
	 * @param x
	 * @param y
	 * @param kingCheck
	 * @return boolean 2D array, true if can move to location, false if not
	 */
	private boolean[][] getAvailableMoves(PlayerNum playerNum, int x, int y,
			boolean kingCheck) {
		Piece selectedPiece = board[x][y];
		boolean[][] moveArray = new boolean[FOUR_P_LENGTH][FOUR_P_LENGTH];

		ArrayList<Move> moveSet = selectedPiece.getMoveSet();
		ArrayList<Move> attackSet = null; // pawns
		for (Move move : moveSet) {
			int moveX = x + move.getX();
			int moveY = y + move.getY();
			if (!inBounds(moveX, moveY)) {
				continue;
			}
			boolean stillMoving = false; // for infinite move pieces

			if (moveIsValid(move, selectedPiece.getPlayerNum(), x, y, kingCheck)) {
				moveArray[moveX][moveY] = true;
				if (board[moveX][moveY].getPlayerNum() == PlayerNum.EMPTY) {
					stillMoving = true;
				}
			}
			if (move.infinite() && stillMoving) {
				while (stillMoving) {
					moveX += move.getX();
					moveY += move.getY();
					if (!inBounds(moveX, moveY)) {
						break;
					}
					if (moveIsValid(move, selectedPiece.getPlayerNum(), moveX
							- move.getX(), moveY - move.getY(), kingCheck)) {
						moveArray[moveX][moveY] = true;
						if (board[moveX][moveY].getPlayerNum() == PlayerNum.EMPTY) {
							stillMoving = true;
						} else {
							stillMoving = false;
						}
					} else {
						stillMoving = false;
					}
				}
			}
		}
		//This is separate logic made specifically for pawns, cuz they're a bunch of assholes.
		if (selectedPiece.getClass() == Pawn.class) {
			attackSet = ((Pawn) selectedPiece).getAttackSet();
			for (Move attack : attackSet) {
				int moveX = x + attack.getX();
				int moveY = y + attack.getY();
				if (pawnCanAttack(attack, selectedPiece.getPlayerNum(), x, y,
						kingCheck)) {
					moveArray[moveX][moveY] = true;
				}
			}
		}
		return moveArray;
	}

	/**
	 * Checks to see if a king is in check at the given location
	 * 
	 * @param playerNum
	 * @param x
	 * @param y
	 * @return true if the king is in check at (x,y), otherwise false
	 */
	private boolean kingInCheck(PlayerNum playerNum, int x, int y) {
		if (!inBounds(x, y)) {
			return false;
		}
		//Iterates through the board and check each pieces movesets
		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board[0].length; ++j) {
				if (!inBounds(i, j)) {
					continue;
				}
				boolean[][] moves;
				//Only check pieces that are opponent pieces
				if (board[i][j].getClass() != game.pieces.King.class
						&& board[i][j].getPlayerNum() != PlayerNum.EMPTY
						&& board[i][j].getPlayerNum() != currentPlayer
								.getPlayerNum()) {
					moves = getAvailableMoves(board[i][j].getPlayerNum(), i, j,
							true);
					//If an opponent piece can move in the king's location, it's in check
					if (moves[x][y]) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks to see if a move is valid for a piece at a certain location
	 * 
	 * @param move
	 *            The move to be verified
	 * @param playerNum
	 *            Which player is moving
	 * @param x
	 *            The x location of the piece
	 * @param y
	 *            The y location of the piece
	 * @return True if the piece can complete this move, otherwise, false
	 */
	private boolean moveIsValid(Move move, PlayerNum playerNum, int x, int y,
			boolean kingCheck) {
		int moveX = x + move.getX();
		int moveY = y + move.getY();
		//If it puts the king in check, it's not a vaild move
		if (board[x][y].getClass() == game.pieces.King.class) {
			if (kingInCheck(playerNum, moveX, moveY)) {
				return false;
			}
		}
		if (board[x][y].getClass() == game.pieces.Pawn.class && !kingCheck) {
			switch (move.getX()) {
			case 2:
				if (board[moveX][moveY].getPlayerNum() == PlayerNum.EMPTY
						&& board[moveX - 1][moveY].getPlayerNum() == PlayerNum.EMPTY) {
					return true;
				}
			case -2:
				if (board[moveX][moveY].getPlayerNum() == PlayerNum.EMPTY
						&& board[moveX + 1][moveY].getPlayerNum() == PlayerNum.EMPTY) {
					return true;
				}
			default:
				break;
			}
			switch (move.getY()) {
			case 2:
				if (board[moveX][moveY].getPlayerNum() == PlayerNum.EMPTY
						&& board[moveX][moveY - 1].getPlayerNum() == PlayerNum.EMPTY) {
					return true;
				}
			case -2:
				if (board[moveX][moveY].getPlayerNum() == PlayerNum.EMPTY
						&& board[moveX][moveY + 1].getPlayerNum() == PlayerNum.EMPTY) {
					return true;
				}
			default:
				break;
			}
			if ((Math.abs(move.getX()) == 1 || Math.abs(move.getY()) == 1)
					&& board[moveX][moveY].getPlayerNum() == PlayerNum.EMPTY) {
				return true;
			}
		} else {
			if (inBounds(moveX, moveY)) {
				if (kingCheck && board[x][y].getClass() != Pawn.class) {
					return true;
				}
				if (board[moveX][moveY].getPlayerNum() != playerNum && !kingCheck) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * A nice function that tests to see if the given location is within the
	 * playing field of the board.
	 * 
	 * @param x
	 * @param y
	 * @return true if location is in playing field, false otherwise.
	 */
	private boolean inBounds(int x, int y) {
		return !(x < 0 || x > 13 || y < 0 || y > 13
				|| (x < 3 && (y < 3 || y > 10))
				|| (x > 10 && (y < 3 || y > 10)) || board[x][y] == null);
	}

	/**
	 * When called, returns an arrayList of players that are in check at the end
	 * of each turn.
	 * 
	 * @return an array of the players that are currently in check
	 */
	public ArrayList<PlayerNum> getPlayersInCheck() {
		player1.setInCheck(false);
		player2.setInCheck(false);
		player3.setInCheck(false);
		player4.setInCheck(false);
		ArrayList<PlayerNum> checkedPlayers = new ArrayList<PlayerNum>();
		for (int i = 0; i < FOUR_P_LENGTH; ++i) {
			for (int j = 0; j < FOUR_P_LENGTH; ++j) {
				if (!inBounds(i, j)) {
					continue;
				}
				if (getPiece(i, j).getClass() == King.class) {
					if (kingInCheck(getPiece(i, j).getPlayerNum(), i, j)) {
						playerNumToPlayer(getPiece(i, j).getPlayerNum()).setInCheck(true);
						checkedPlayers.add(getPiece(i, j).getPlayerNum());
					}
				}

			}
		}
		return checkedPlayers;
	}

	/**
	 * Checks to see if the current player is in checkmate.
	 * 
	 * @return true if the current player is in checkmate, false otherwise
	 */
	private boolean isCheckmate() {
		boolean checkMate = true;
		if (!getPlayersInCheck().contains(currentPlayer.getPlayerNum())) {
			return false;
		}
		//get current player's king's location
		int kingX = 0, kingY = 0;
		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board.length; ++j) {
				if (!inBounds(i, j)) {
					continue;
				}
				if (board[i][j].getClass() == game.pieces.King.class && board[i][j].getPlayerNum() == currentPlayer.getPlayerNum()) {
					kingX = i;
					kingY = j;
					i = board.length;
					break;
				}
			}
		}
		//see if the king can move itself out of check
		for (int k = 0; k < board[kingX][kingY].getMoveSet().size(); ++k) {
			Move move = board[kingX][kingY].getMoveSet().get(k);
			int moveX = kingX + move.getX();
			int moveY = kingY + move.getY();
			if(!inBounds(moveX, moveY) || board[moveX][moveY].getPlayerNum() == currentPlayer.getPlayerNum()){
				continue;
			}
			if (!kingInCheck(currentPlayer.getPlayerNum(), moveX,
					moveY)) {
				checkMate = false;
			}
		}
		//see if another piece can move the king out of check
		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board.length; ++j) {
				if (!inBounds(i, j)) {
					continue;
				}
				if(board[i][j].getPlayerNum() == currentPlayer.getPlayerNum() && board[i][j].getClass() != game.pieces.King.class){
					boolean[][] moves = getAvailableMoves(board[i][j].getPlayerNum(), i, j);
					for(int l = 0; l < FOUR_P_LENGTH; ++l){
						for(int m = 0; m < FOUR_P_LENGTH; ++m){
							if(moves[l][m]){
								Piece pieceAtMove = board[l][m];
								Piece movingPiece = board[i][j];
								board[l][m] = movingPiece;
								board[i][j] = new Queen(PlayerNum.EMPTY);
								if(!kingInCheck(currentPlayer.getPlayerNum(), kingX, kingY)){
									checkMate = false;
								}
								board[l][m] = pieceAtMove;
								board[i][j] = movingPiece;
							}
						}
					}
				}
			}
		}
		return checkMate;
	}

	/**
	 * After a player has been checkMated, this function is called to remove all
	 * their pieces from the board, and the corresponding player from the
	 * playset.
	 * 
	 * @param player
	 *            the player to remove
	 */
	private void removePlayer(Player player) {
		for (int i = 0; i < board.length; ++i) {
			for (int j = 0; j < board.length; ++j) {
				if (!inBounds(i, j)) {
					continue;
				}
				if (board[i][j].getPlayerNum().equals(player.getPlayerNum())) {
					board[i][j] = new Queen(PlayerNum.EMPTY);
				}
			}
		}
		player.removePlayer();
	}

	/**
	 * Takes into account if players have been removed. Returns null if only one
	 * player is still playing.
	 * 
	 * @return the next player, null if one player is remaining
	 */
	private Player getNextPlayer() {
		int count = 0;
		if (player1.playing()) {
			count++;
		}
		if (player2.playing()) {
			count++;
		}
		if (player3.playing()) {
			count++;
		}
		if (player4.playing()) {
			count++;
		}
		if (count == 1) {
			return null;
		}

		if (currentPlayer == player1) {
			if (player2.playing()) {
				return player2;
			}
			if (player3.playing()) {
				return player3;
			}
			if (player4.playing()) {
				return player4;
			}
		}
		if (currentPlayer == player2) {
			if (player3.playing()) {
				return player3;
			}
			if (player4.playing()) {
				return player4;
			}
			if (player1.playing()) {
				return player1;
			}
		}
		if (currentPlayer == player3) {
			if (player4.playing()) {
				return player4;
			}
			if (player1.playing()) {
				return player1;
			}
			if (player2.playing()) {
				return player2;
			}
		}
		if (currentPlayer == player4) {
			if (player1.playing()) {
				return player1;
			}
			if (player2.playing()) {
				return player2;
			}
			if (player3.playing()) {
				return player3;
			}
		}
		return null;
	}

	/**
	 * When called, creates a array of highlighted types, all set to blank. Used
	 * to reset highlightedArray after each time setLocation is called.
	 * 
	 * @return
	 */
	private static final Highlighted[][] initializeBlankHighlightedArray() {
		Highlighted[][] arr = new Highlighted[FOUR_P_LENGTH][FOUR_P_LENGTH];
		for (int i = 0; i < FOUR_P_LENGTH; ++i) {
			for (int j = 0; j < FOUR_P_LENGTH; ++j) {
				arr[i][j] = Highlighted.BLANK;
			}
		}
		return arr;
	}

	/**
	 * When called, returns the value stored by the boolean instance variable
	 * "gameOver." used to check if the game is over or if game still continues.
	 * 
	 * @return
	 */
	public boolean isGameOver() {
		return gameOver;
	}

	/**
	 * When called, returns the player stored by the "currentPlayer" instance
	 * variable
	 * 
	 * @return current player of the game
	 */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

}
