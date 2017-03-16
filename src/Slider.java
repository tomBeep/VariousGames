import java.awt.Color;

import ecs100.UI;

public class Slider {

	private int boardSize;
	private int sideLengthDivisor = 3;// how long each side should be

	private int color = (int) (Math.random() * 3) + 1;// chooses a random color each game just to mix things up.

	private int[] board;
	private int sideLength = 100;
	private int pressedBlock = -1;

	public Slider(int difficulty) {
		if (difficulty >= 3)
			sideLengthDivisor = difficulty;
		setupBoard();// sets up board
		displayBoard();// displays board in it current state
		startGame();// creates the mouse listener to start the game
		UI.printMessage("Order the numbers from 1-15 starting in the top left corner. Click and drag to move blocks");
	}

	public void setupBoard() {
		boardSize = sideLengthDivisor * sideLengthDivisor;
		board = new int[boardSize];
		for (int i = 1; i < boardSize; i++) {// fills array with numbers 1 to (boardSize-1) and a -1 (empty tile)
			board[i - 1] = i;
		}
		board[boardSize - 1] = -1;

		for (int i = 1; i < boardSize * 2; i++) {// makes 2*boardSize random swaps in tiles
			int first = (int) (Math.random() * boardSize);
			int second = (int) (Math.random() * boardSize);
			int temp = board[first];
			board[first] = board[second];
			board[second] = temp;
		}
	}

	public void displayBoard() {
		UI.clearGraphics();
		double x = 40;// start coordinates of the board
		double y = 40;
		for (int i = 0; i < boardSize; i++) {
			if (board[i] != -1) {// draw each tile unless the tile is the empty one
				changeColor(board[i]);
				UI.fillRect(x, y, sideLength, sideLength);
				UI.setColor(Color.WHITE);
				UI.setFontSize(20);
				UI.drawString(String.valueOf(board[i]), x + 42, y + 56);
			}
			UI.setColor(Color.BLACK);
			UI.setLineWidth(3);
			UI.drawRect(x, y, sideLength, sideLength);
			UI.setLineWidth(1);
			x += sideLength;
			if ((i + 1) % sideLengthDivisor == 0) {// after 4 blocks, move to next line
				x = 40;
				y += sideLength;
			}
		}
		if (checkWin()) {
			UI.printMessage("You Won!");
		}
	}

	public void doMouse(String s, double x, double y) {
		if (x >= 40 && x <= 40 + sideLengthDivisor * sideLength && y >= 40
				&& y <= 40 + sideLengthDivisor * sideLength) {// if click was within the boundaries
			if (s.equals("pressed")) {
				// identifies which block was pressed
				int number = (int) ((x - 40) / sideLength);
				int number2 = (int) ((y - 40) / sideLength);
				pressedBlock = number + (number2) * sideLengthDivisor;
			} else if (s.equals("released") && pressedBlock != -1) {
				int number = (int) ((x - 40) / sideLength);
				int number2 = (int) ((y - 40) / sideLength);
				int swapBlock = number + (number2) * sideLengthDivisor;

				// if the click and drag was legitimate, swap the blocks
				if (board[swapBlock] == -1 && (pressedBlock - 1 == swapBlock || pressedBlock + 1 == swapBlock
						|| pressedBlock - sideLengthDivisor == swapBlock
						|| pressedBlock + sideLengthDivisor == swapBlock)) {
					int temp = board[pressedBlock];
					board[pressedBlock] = board[swapBlock];
					board[swapBlock] = temp;
					displayBoard();// redisplay the board
				}
				pressedBlock = -1;// forget about the pressed block
			}
		} else {
			pressedBlock = -1;// a fail click will reset the pressed block
		}
	}

	public void changeColor(int decrement) {
		Color col;
		switch (color) {
		case 1:
			col = new Color(255 - decrement * (200 / boardSize), 0, 0);// red
			break;
		case 2:
			col = new Color(0, 0, 255 - decrement * (200 / boardSize));// blue
			break;
		default:
			col = new Color(0, 255 - decrement * (200 / boardSize), 0);// green
			break;
		}
		UI.setColor(col);
	}

	public void startGame() {
		UI.setMouseListener(this::doMouse);
	}

	public boolean checkWin() {
		for (int i = 0; i < boardSize - 1; i++) {
			if (board[i] != i + 1)
				return false;
		}
		return true;
	}
}
