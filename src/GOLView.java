import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;



@SuppressWarnings("serial")
public class GOLView extends JFrame {
	
	private JPanel gridPanel;
	private JPanel[][] grid;
	
	private JPanel controlPanel;
	private JButton startStopToggle;
	private JButton reset;
	private JSlider speedAdjust;
	
	public GOLView() {
		init();
	}
	
	private void init() {
		//TODO: use layout to fix GUI
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(770, 720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//FIXME ported code
		gridPanel =  new JPanel();
		/*grid = new JPanel[controller.getWorldSize()][controller.getWorldSize()];
		
		gridPanel.setLayout(new GridLayout(controller.getWorldSize(), 
				controller.getWorldSize()));*/

		
		for(int x=0;x<10;x++) {
			for(int y=0;y<10;y++) {
				JPanel p2 = new JPanel();
				p2.setBorder(new LineBorder(Color.BLACK, 1));
				gridPanel.add(p2);
			}
		}
		
		this.add(gridPanel, BorderLayout.CENTER);
		
		controlPanel = new JPanel();
		
		speedAdjust = new JSlider(0, 1000, 100);
		controlPanel.add(speedAdjust);
		
		startStopToggle = new JButton("Start");
		controlPanel.add(startStopToggle);
		
		reset = new JButton("Reset");
		controlPanel.add(reset);
		
		this.add(controlPanel, BorderLayout.PAGE_END);
		
		this.pack();
		this.setVisible(true);
	}
	
	// Deprecated?
	/*private void update() {
		for(int x=0;x<controller.getWorldSize();x++) {
			for(int y=0;y<controller.getWorldSize();y++) {
				if( cw.getCellState(x, y) == CellWorld.ALIVE ) {
					grid[x][y].setBackground(Color.BLACK);
				} else if( cw.getCellState(x, y) == CellWorld.DEAD ) {
					grid[x][y].setBackground(Color.WHITE);
				}
			}
		}
	}*/
}
