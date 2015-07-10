package creation;

import javax.swing.JPanel;



/**
 * JPanel wrapper with information regarding grid location. Used by
 * {@link GOLView}.
 * 
 * @author Cameron Rader (github: Mr-Sniffles)
 *
 */
@SuppressWarnings("serial")
public class CellPanel extends JPanel {

	/**
	 * Positional information
	 */
	private int	xPos, yPos;

	/**
	 * Creates a panel at location (x, y)
	 * 
	 * @param x
	 *            X coordinate of panel
	 * @param y
	 *            Y coordinate of panel
	 */
	public CellPanel(int x, int y) {
		super();

		xPos = x;
		yPos = y;
	}

	/**
	 * Get X coordinate of this panel.
	 * 
	 * @return Panel's X coordinate
	 */
	public int getxPos() {
		return xPos;
	}

	/**
	 * Get Y coordinate of this panel.
	 * 
	 * @return Panel's Y coordinate
	 */
	public int getyPos() {
		return yPos;
	}

}
