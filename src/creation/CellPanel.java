package creation;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CellPanel extends JPanel {

	private int xPos, yPos;

	public CellPanel(int x, int y) {
		super();

		xPos = x;
		yPos = y;
	}

	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}

}
