import java.awt.Color;

import ecs100.UI;

/**
 * @author Thomas Edwards
 *
 */
public class ConnectFour2p {
	private int width;
	private int height = 6;

	private Token[][] board;
	private boolean redTurn = true;
	private int column = 0;
	private boolean gameOver = false;

	private WinType winType;// 1 is horizontal, 2 is vertical, 3 is left to right diag, 4 is right to left diag
	private int lastX = -1;// locations of last piece in win row
	private int lastY = -1;

	// board is 7 horizontal by 6 vertical

	public ConnectFour2p(int difficulty) {
		width = 7 + (int) ((difficulty - 1) / 2);
		this.start();
		this.display();
	}

	public void display() {
		UI.setColor(Color.blue);
		UI.fillRect(40, 80, 100 * width, 100 * height);// draws the big blue square
		UI.setColor(Color.white);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {// draws a colored circle if the spot is filled
				if (board[i][j] != null && board[i][j] == Token.red)
					UI.setColor(Color.red);
				else if (board[i][j] != null && board[i][j] == Token.yellow)
					UI.setColor(Color.yellow);
				UI.fillOval(i * 99 + 55, j * 99 + 95, 70, 70);
				UI.setColor(Color.white);// else draws a white circle (Empty)
			}
		}
		drawNewCounter();
		if (checkWin()) {// checks for a win
			UI.printMessage((redTurn ? "Yellow" : "Red") + " Wins!");
			gameOver = true;// prevents any more turns from being played
			drawWinLine();// draws winning line
		}
	}

	public void start() {
		board = new Token[width][height];
		UI.setKeyListener(this::doKey);
		UI.printMessage("Use the arrow keys to move your counter. First to get 4 Tokens in a row wins!");
	}

	/**
	 * Allows movement left, right and down of Tokens using a variety of common keys.
	 * 
	 * @param key
	 */
	public void doKey(String key) {
		if (gameOver)
			return;
		if (key.equals("Left") || key.equals("a")) {
			eraseOldCounter();
			column--;
			if (column < 0)
				column++;
			drawNewCounter();
		} else if (key.equals("Right") || key.equals("d")) {
			eraseOldCounter();
			column++;
			if (column >= width)
				column--;
			drawNewCounter();
		} else if (key.equals("Space") || key.equals("Down") || key.equals("s") || key.equals("Enter")) {
			for (int i = height - 1; i >= 0; i--) {
				if (board[column][i] == null) {
					board[column][i] = redTurn ? Token.red : Token.yellow;
					redTurn = !redTurn;
					break;
				}
			}
			display();// redisplays board only when Token is downed
		}
	}

	public void eraseOldCounter() {
		UI.eraseOval(column * 99 + 50, -10, 80, 80);
	}

	public void drawNewCounter() {
		UI.setColor(redTurn ? Color.RED : Color.yellow);
		UI.fillOval(column * 99 + 50, -10, 80, 80);
		UI.setColor(Color.black);
		UI.setLineWidth(3);
		UI.drawOval(column * 99 + 50, -10, 80, 80);
	}

	/**
	 * Goes through the board vertically, horizontally and twice diagonally checking for four in a rows. If a 4 in a row
	 * is found then it records the last spot's location in the 4 and records the type of row (Ie, vertical, horizontal,
	 * diagonal) and returns true
	 * 
	 * @return true if a player has won, false if no one has 4 in a row
	 */
	public boolean checkWin() {
		int count = 0;// keeps track of number of tokens seen in a row
		Token currentColour = Token.empty;// keeps track of the colour of the last token
		// go through vertical
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (board[i][j] == null) {
					count = 0;
					currentColour = Token.empty;
				} else if (board[i][j] == Token.red) {
					if (currentColour == Token.red) {
						count++;
					} else {
						currentColour = Token.red;
						count = 1;
					}
				} else if (board[i][j] == Token.yellow) {
					if (currentColour == Token.yellow) {
						count++;
					} else {
						currentColour = Token.yellow;
						count = 1;
					}
				}
				if (count == 4) {
					winType = WinType.vertical;
					lastX = i;
					lastY = j;
					return true;
				}

			}
			currentColour = Token.empty;
			count = 0;//after each row/column, reset the trackers
		}
		// go through horizontal
		count = 0;
		currentColour = Token.empty;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (board[j][i] == null) {
					count = 0;
					currentColour = Token.empty;
				} else if (board[j][i] == Token.red) {
					if (currentColour == Token.red) {
						count++;
					} else {
						currentColour = Token.red;
						count = 1;
					}
				} else if (board[j][i] == Token.yellow) {
					if (currentColour == Token.yellow) {
						count++;
					} else {
						currentColour = Token.yellow;
						count = 1;
					}
				}
				if (count == 4) {
					winType = WinType.horizontal;
					lastX = j;
					lastY = i;
					return true;
				}

			}
			currentColour = Token.empty;
			count = 0;
		}
		// Checks diagonals in direction of from bottom left to top right
		// Start in the bottom right corner and moves left around the board
		boolean goingUp = false;// whether the loop has reached the corner or not
		for (int k = width - 1; k >= 0; k--) {// starts in bottom right corner moving left
			int j = goingUp ? 0 : k;
			int i = goingUp ? k : height - 1;
			count = 0;
			currentColour = Token.empty;
			while (j < width && i >= 0 && j >= 0) {
				if (board[j][i] == null) {
					count = 0;
					currentColour = Token.empty;
				} else if (board[j][i] == Token.red) {
					if (currentColour == Token.red) {
						count++;
					} else {
						currentColour = Token.red;
						count = 1;
					}
				} else if (board[j][i] == Token.yellow) {
					if (currentColour == Token.yellow) {
						count++;
					} else {
						currentColour = Token.yellow;
						count = 1;
					}
				}
				if (count == 4) {
					winType = WinType.rampDiag;
					lastX = j;
					lastY = i;
					return true;
				}
				j++;// always checks diagonals along this line
				i--;
			}
			if (goingUp == false && k == 0) {// once loop hits corner, changes it so that it moves upwards
				k = height;
				goingUp = true;
			}
		}
		// The opposite diagonal, going from bottom right to top left direction, This one is harder as changing
		// direction round the corner goes from adding to i to subtracting from j
		goingUp = false;// whether the loop has reached the corner or not
		int k = 0;
		while (k < width) {// starts in bottom left corner, moving right
			int j = goingUp ? width - 1 : k;
			int i = goingUp ? k : height - 1;
			count = 0;
			currentColour = Token.empty;
			while (j >= 0 && i >= 0) {
				if (board[j][i] == null) {
					count = 0;
					currentColour = Token.empty;
				} else if (board[j][i] == Token.red) {
					if (currentColour == Token.red) {
						count++;
					} else {
						currentColour = Token.red;
						count = 1;
					}
				} else if (board[j][i] == Token.yellow) {
					if (currentColour == Token.yellow) {
						count++;
					} else {
						currentColour = Token.yellow;
						count = 1;
					}
				}
				if (count == 4) {
					winType = WinType.slopeDiag;
					lastX = j;
					lastY = i;
					return true;
				}
				j--;// always checks diagonals along this line
				i--;
			}
			if (goingUp == false && k == width - 1) {// once loop hits corner, changes it so that it moves upwards
				k = height;
				goingUp = true;
			}
			if (!goingUp)
				k++;// move right (positive)
			else {
				k--;// Move Up (negative)
			}
			if (k == 0)
				break;
		}
		return false;
	}

	/**
	 * 1 is horizontal, 2 is vertical, 3 is left to right diag, 4 is right to left diag. All measurements are based off
	 * of the current size of board.
	 */
	public void drawWinLine() {
		UI.setLineWidth(3);
		switch (winType) {
		case horizontal:
			UI.drawLine(lastX * 99 + 90, lastY * 99 + 130, (lastX - 3) * 99 + 90, lastY * 99 + 130);
			break;
		case vertical:
			UI.drawLine(lastX * 99 + 90, lastY * 99 + 130, (lastX) * 99 + 90, (lastY - 3) * 99 + 130);
			break;
		case rampDiag:
			UI.drawLine(lastX * 99 + 90, lastY * 99 + 130, (lastX - 3) * 99 + 90, (lastY + 3) * 99 + 130);
			break;
		case slopeDiag:
			UI.drawLine(lastX * 99 + 90, lastY * 99 + 130, (lastX + 3) * 99 + 90, (lastY + 3) * 99 + 130);
			break;
		}
	}

	private enum Token {// a small enum to make things more clear
		red, yellow, empty
	}

	private enum WinType {// another small enum for clarity
		vertical, horizontal, rampDiag, slopeDiag
	}
}
