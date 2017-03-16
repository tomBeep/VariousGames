import java.awt.Color;

import ecs100.UI;

public class MemoryCards {

	private int squareSize = 60;
	private Card[] cards;
	private int difficulty = 60;// 20,30,40,50 or 60
	private Card flipped1, flipped2;
	private int moves = 1;
	private double pastX = -1, pastY = -1;

	public MemoryCards(int difficulty) {
		this.difficulty = (difficulty + 1) * 10;
		this.setupCards();
		this.shuffleCards();
		this.displayCards();
		this.startGame();
		UI.printMessage("Click a card to overturn it, Match up all the pairs if you can");
	}

	public void setupCards() {
		cards = new Card[difficulty];
		cards[0] = cards[1] = new Card(Shape.circle, Color.red);
		cards[2] = cards[3] = new Card(Shape.square, Color.red);
		cards[4] = cards[5] = new Card(Shape.triangle, Color.red);
		cards[6] = cards[7] = new Card(Shape.kite, Color.red);
		cards[8] = cards[9] = new Card(Shape.diamond, Color.red);
		cards[10] = cards[11] = new Card(Shape.circle, Color.green);
		cards[12] = cards[13] = new Card(Shape.square, Color.green);
		cards[14] = cards[15] = new Card(Shape.triangle, Color.green);
		cards[16] = cards[17] = new Card(Shape.kite, Color.green);
		cards[18] = cards[19] = new Card(Shape.diamond, Color.green);
		if (difficulty >= 30) {
			cards[20] = cards[21] = new Card(Shape.circle, Color.blue);
			cards[22] = cards[23] = new Card(Shape.square, Color.blue);
			cards[24] = cards[25] = new Card(Shape.triangle, Color.blue);
			cards[26] = cards[27] = new Card(Shape.kite, Color.blue);
			cards[28] = cards[29] = new Card(Shape.diamond, Color.blue);
		}
		if (difficulty >= 40) {
			cards[30] = cards[31] = new Card(Shape.circle, Color.yellow);
			cards[32] = cards[33] = new Card(Shape.square, Color.yellow);
			cards[34] = cards[35] = new Card(Shape.triangle, Color.yellow);
			cards[36] = cards[37] = new Card(Shape.kite, Color.yellow);
			cards[38] = cards[39] = new Card(Shape.diamond, Color.yellow);
		}
		if (difficulty >= 50) {
			cards[40] = cards[41] = new Card(Shape.circle, Color.cyan);
			cards[42] = cards[43] = new Card(Shape.square, Color.cyan);
			cards[44] = cards[45] = new Card(Shape.triangle, Color.cyan);
			cards[46] = cards[47] = new Card(Shape.kite, Color.cyan);
			cards[48] = cards[49] = new Card(Shape.diamond, Color.cyan);
		}
		if (difficulty >= 60) {
			cards[50] = cards[51] = new Card(Shape.circle, Color.MAGENTA);
			cards[52] = cards[53] = new Card(Shape.square, Color.MAGENTA);
			cards[54] = cards[55] = new Card(Shape.triangle, Color.MAGENTA);
			cards[56] = cards[57] = new Card(Shape.kite, Color.MAGENTA);
			cards[58] = cards[59] = new Card(Shape.diamond, Color.MAGENTA);
		}

	}

	public void shuffleCards() {
		for (int i = 0; i < difficulty * 6; i++) {
			int first = (int) (Math.random() * difficulty);
			int second = (int) (Math.random() * difficulty);
			Card temp = cards[first];
			cards[first] = cards[second];
			cards[second] = temp;
		}
	}

	public void displayCards() {
		double x = 40;
		double y = 40;
		UI.setColor(Color.BLACK);
		for (int i = 1; i <= difficulty; i++) {
			if (cards[i - 1].uncovered == false)// only re-draw card if it is undiscovered
				UI.fillRect(x + 10, y + 10, squareSize, squareSize * 1.5);
			else {// draw an outline around discovered cards
				UI.setLineWidth(3);
				UI.drawRect(x + 10, y + 10, squareSize, squareSize * 1.5);
				UI.setLineWidth(1);
			}
			x += squareSize + 20;
			if (i % 10 == 0) {
				x = 40;
				y += squareSize * 1.5 + 20;
			}
		}
		if (checkWin())// win message
			UI.printMessage("YOU WIN, You used " + moves);
	}

	public void startGame() {
		UI.setMouseListener(this::doMouse);
	}

	public void doMouse(String s, double x, double y) {
		if (!checkMouse(s, x, y))
			return;
		double numberX = (int) (x - 40) / 80;
		double numberY = (int) (y - 40) / 110;
		if (numberX == pastX && numberY == pastY)// prevents clicking on the same card twice
			return;
		if (cards[(int) (numberY * 10 + numberX)].uncovered) {// prevents clicking on an already overturned card
			return;
		}
		pastX = numberX;// remembers past card's location
		pastY = numberY;

		// flips the card
		flipCard(cards[(int) (numberY * 10 + numberX)], (int) (numberX * 80 + 40), (int) (numberY * 110 + 40));
		if (flipped1 != null) {// is the card the first or second?
			flipped2 = cards[(int) (numberY * 10 + numberX)];// sets 2nd card
			if (flipped1.compareTo(flipped2)) {// compares the two cards for equality
				flipped1.uncovered = true;// if cards are equal, makes them uncovered
				flipped2.uncovered = true;
			}
			UI.printMessage("Turn " + moves++);
			UI.sleep(1000);
			flipped1 = flipped2 = null;// resets the two cards
			pastX = pastY = -1;// resets past position
			displayCards();// redisplays all cards
		} else {
			flipped1 = cards[(int) (numberY * 10 + numberX)];// sets first card
		}
	}

	public void flipCard(Card c, int x, int y) {
		UI.setColor(Color.GRAY);// front of cards are all grey
		UI.fillRect(x + 10, y + 10, squareSize, squareSize * 1.5);
		UI.setColor(c.color);// sets colour of shape on card
		switch (c.shape) {// draws Shape on card
		case circle:
			UI.fillOval(x + 12, y + 26, squareSize - 4, squareSize - 4);
			break;
		case square:
			UI.fillRect(x + 12, y + 26, squareSize - 4, squareSize - 4);
			break;
		case triangle:
			UI.fillPolygon(new double[] { x + 40, x + 60, x + 20 }, new double[] { y + 30, y + 80, y + 80 }, 3);
			break;
		case kite:
			UI.fillPolygon(new double[] { x + 40, x + 60, x + 20, x + 40 },
					new double[] { y + 15, y + 55, y + 55, y + 95 }, 4);
			break;
		case diamond:
			UI.fillPolygon(new double[] { x + 40, x + 55, x + 40, x + 25 },
					new double[] { y + 20, y + 55, y + 90, y + 55 }, 4);
			break;
		}
	}

	public boolean checkMouse(String s, double x, double y) {
		// checks mouse is within borders of board
		if (!s.equals("released") || x < 50 || x >= 830 || y < 50 || y > 30 + 110 * difficulty / 10)
			return false;// do nothing
		if ((x - 40) % 80 < 10 || (x - 40) % 80 > 60)// checks mouse is on a card, not between cards x-Axis
			return false;
		if ((y - 40) % 110 < 10 || (y - 40) % 110 > 100)// checks mouse is on a card, not between cards y-Axis
			return false;
		return true;
	}

	public boolean checkWin() {
		for (int i = 0; i < difficulty; i++) {
			if (cards[i].uncovered == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @author Thomas Edwards A card is made up of a shape, color and whether or not it has been uncovered. Also has a
	 *         compare method which returns true if the card and another card have the same shape and color
	 */
	private class Card {
		private Shape shape;
		private Color color;
		private boolean uncovered = false;

		private Card(Shape shape, Color col) {
			this.shape = shape;
			this.color = col;
		}

		private boolean compareTo(Card other) {
			if (other.shape.equals(this.shape) && other.color.equals(this.color))
				return true;
			return false;
		}
	}

	private enum Shape {// enum to store the types of shapes
		circle, square, triangle, kite, diamond

	}
}
