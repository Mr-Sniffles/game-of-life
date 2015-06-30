package creation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;



@SuppressWarnings("serial")
public class GOLView extends JFrame {
	
	//##########################################################################
	//	Global Variables/Constants
	//##########################################################################
	
	private JPanel gridPanel;
	private JPanel[][] grid;
	private int gridSize;
	
	private JPanel controlPanel;
	private JButton startStopToggle;
	private JButton reset;
	private JSlider speedAdjust;
	
	
	//##########################################################################
	//	Constructors
	//##########################################################################

	public GOLView() {
		gridSize = 10;
		init();
	}
	
	
	//##########################################################################
	//	Helper Methods
	//##########################################################################
	
	private void init() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(770, 720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gridPanel =  new JPanel();
		
		grid = new JPanel[gridSize][gridSize];
		
		gridPanel.setLayout(new GridLayout(gridSize, gridSize));

		
		for(int x=0;x<gridSize;x++) {
			for(int y=0;y<gridSize;y++) {
				JPanel p2 = grid[x][y] = new JPanel();
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
	
	
	//##########################################################################
	//	View Methods
	//##########################################################################
	
	public void resizeGrid(int newSize) {
		gridPanel.setVisible(false);
		
		gridSize = newSize;
		grid = new JPanel[gridSize][gridSize];
		gridPanel.removeAll();
		
		gridPanel.setLayout(new GridLayout(gridSize, gridSize));

		
		for(int x=0;x<gridSize;x++) {
			for(int y=0;y<gridSize;y++) {
				JPanel p2 = grid[x][y] = new JPanel();
				p2.setBorder(new LineBorder(Color.GRAY));
				gridPanel.add(p2);
			}
		}

		gridPanel.setVisible(true);
	}
	
	public void updateGridCell(int x, int y, int state) {
		if( state == CellWorld.ALIVE ) {
			grid[x][y].setBackground(Color.BLACK);
		} else {
			grid[x][y].setBackground(Color.WHITE);
		}
	}
	
	
	//##########################################################################
	//	Listener Methods
	//##########################################################################
	
	public void addSpeedAdjustListener(ChangeListener listener) {
		speedAdjust.addChangeListener(listener);
	}
	
	public void addStartStopToggleListener(ActionListener listener) {
		startStopToggle.addActionListener(listener);
	}
	
}
