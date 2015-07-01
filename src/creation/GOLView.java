package creation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;



@SuppressWarnings("serial")
public class GOLView extends JFrame {
	
	//##########################################################################
	//	Global Variables/Constants
	//##########################################################################
	
	private JMenuBar menuBar;
	private ArrayList<JMenu> menuList;
	
	private JPanel gridPanel;
	private CellPanel[][] grid;
	private int gridSize;
	private Color aliveCellColor;
	private Color deadCellColor;
	private Color cellBorderColor;
	
	private JPanel controlPanel;
	private JButton startStopToggle;
	private JButton resetButton;
	private JButton clearButton;
	private JSlider speedAdjust;
	
	
	//##########################################################################
	//	Constructors
	//##########################################################################

	public GOLView() {
		gridSize = 10;
		aliveCellColor = Color.BLACK;
		deadCellColor = Color.WHITE;
		cellBorderColor = Color.GRAY;
		
		init();
	}
	
	
	//##########################################################################
	//	Helper Methods
	//##########################################################################
	
	private void init() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(800, 720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.initMenu();
		
		this.initGridDisplay();
		
		this.initControls();
		
		this.pack();
		this.setVisible(true);
	}
	
	private void initMenu() {
		menuBar = new JMenuBar();
		menuList = new ArrayList<JMenu>();
		
		//TODO
		JMenu m = new JMenu("File");
		m.add(new JMenuItem("Save World..", 
				new ImageIcon(this.getClass().getResource(("/res/saveIcon.png")))));
		
		m.add(new JMenuItem("Load World..", 
				new ImageIcon(this.getClass().getResource("/res/loadIcon.png"))));
		
		menuList.add(m);
		
		
		for( JMenu menu : menuList ) {
			menuBar.add(menu);
		}
		this.setJMenuBar(menuBar);
	}
	
	private void initGridDisplay() {
		gridPanel =  new JPanel();
		
		grid = new CellPanel[gridSize][gridSize];
		
		gridPanel.setLayout(new GridLayout(gridSize, gridSize));

		
		for(int x=0;x<gridSize;x++) {
			for(int y=0;y<gridSize;y++) {
				JPanel p2 = grid[x][y] = new CellPanel(x, y);
				p2.setBorder(new LineBorder(cellBorderColor, 1));
				gridPanel.add(p2);
			}
		}
		
		this.add(gridPanel, BorderLayout.CENTER);
	}
	
	private void initControls() {
		
		// --- start control panel ---
		controlPanel = new JPanel(new GridLayout(1, 2));
		
		speedAdjust = new JSlider(0, 1000, 100);
		speedAdjust.setMajorTickSpacing(200);
		speedAdjust.setMinorTickSpacing(50);
		speedAdjust.setPaintTicks(true);
		speedAdjust.setPaintLabels(true);
		JPanel sliderPanel = new JPanel(new BorderLayout());
		sliderPanel.add(speedAdjust);
		
		controlPanel.add(sliderPanel);
		// --- end control panel ---
		
		
		// --- start button panel ---
		JPanel buttonPanel = new JPanel();
		
		startStopToggle = new JButton("Start");
		buttonPanel.add(startStopToggle);
		
		resetButton = new JButton("Reset");
		buttonPanel.add(resetButton);
		
		clearButton = new JButton("Clear");
		buttonPanel.add(clearButton);
		
		controlPanel.add(buttonPanel);
		// --- end button panel ---
		
		
		this.add(controlPanel, BorderLayout.PAGE_END);
	}
	
	
	//##########################################################################
	//	View Methods
	//##########################################################################
	
	public int getSpeedAdjustValue() {
		return speedAdjust.getValue();
	}
	
	public void setStartStopToggleText(String text) {
		startStopToggle.setText(text);
	}
	
	public void resizeGrid(int newSize) {
		gridPanel.setVisible(false);
		
		gridSize = newSize;
		grid = new CellPanel[gridSize][gridSize];
		gridPanel.removeAll();
		
		gridPanel.setLayout(new GridLayout(gridSize, gridSize));

		
		for(int x=0;x<gridSize;x++) {
			for(int y=0;y<gridSize;y++) {
				JPanel p2 = grid[x][y] = new CellPanel(x, y);
				p2.setBorder(new LineBorder(cellBorderColor));
				gridPanel.add(p2);
			}
		}

		gridPanel.setVisible(true);
	}
	
	private void resetGrid() {
		for(int x=0;x<gridSize;x++) {
			for(int y=0;y<gridSize;y++) {
				grid[x][y].setBackground(deadCellColor);
			}
		}
	}
	
	public void updateGridCell(int x, int y, int state) {
		if( state == CellWorld.ALIVE ) {
			grid[x][y].setBackground(aliveCellColor);
		} else {
			grid[x][y].setBackground(deadCellColor);
		}
	}
	
	public void inverGridCell(int x, int y) {
		if( grid[x][y].getBackground().equals(aliveCellColor) ) {
			grid[x][y].setBackground(deadCellColor);
		} else {
			grid[x][y].setBackground(aliveCellColor);
		}
	}
	
	public void reset() {
		this.resetGrid();
		this.setStartStopToggleText("Start");
	}
	
	public void setAliveCellColor(Color c) {
		aliveCellColor = c;
	}
	
	public void setDeadCellColor(Color c) {
		aliveCellColor = c;
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
	
	public void addResetButtonListener(ActionListener listener) {
		resetButton.addActionListener(listener);
	}
	
	public void addClearButtonListener(ActionListener listener) {
		clearButton.addActionListener(listener);
	}
	
	public void addGridCellListener(int x, int y, MouseListener listener) {
		grid[x][y].addMouseListener(listener);
	}
}
