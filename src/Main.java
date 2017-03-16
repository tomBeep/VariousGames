import ecs100.*;

/**
 * @author Thomas Edwards
 * 
 *         Aims for this project:
 * 
 *         Each game should be contained in a single class (AKA not too complex).
 * 
 *         Each game should be able to scale up and down in difficulty.
 * 
 *         Each game should be somewhat challenging.
 */
public class Main {

	Object current;// the current game being played
	private int difficulty = 1;

	public Main() {
		//sets up main GUI
		UI.initialise();
		UI.setWindowSize(1000, 800);
		UI.setDivider(0.0);
		UI.addButton("Slider", this::Slider);
		UI.addButton("Memory Cards", this::MemoryCards);
		UI.addButton("Connect Four (2p)", this::ConnectFour2p);
		UI.addSlider("Difficulty", 1, 5, 1, (i) -> difficulty = (int) i);
	}

	public void Slider() {
		this.restartAll();
		this.current = new Slider(difficulty);
	}

	public void MemoryCards() {
		this.restartAll();
		current = new MemoryCards(difficulty);
	}

	public void ConnectFour2p() {
		this.restartAll();
		current = new ConnectFour2p(difficulty);
	}

	public void restartAll() {
		current = null;// deletes the current game
		UI.clearPanes();// resets panes
		UI.setDivider(0.0);// ensures divider is in correct position
		UI.setKeyListener(this::removeKeyListener);// overwrites current Key listener
		UI.setMouseListener(this::removeMouseListener);// overwrites current Mouse listener
	}

	private void removeKeyListener(String s) {
		// Empty, as this Key listener is just to overwrite any previous ones
	}

	private void removeMouseListener(String s, double x, double y) {
		// Empty, as this Mouse listener is just to overwrite any previous ones
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
