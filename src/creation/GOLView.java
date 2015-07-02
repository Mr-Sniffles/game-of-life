package creation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;



@SuppressWarnings("serial")
public class GOLView extends JFrame {
	
	//##########################################################################
	//	Global Variables/Constants
	//##########################################################################

	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem saveItem, loadItem, resizeItem;
	
	private JPanel gridPanel;
	private CellPanel[][] grid;
	private int gridSize;
	private Color aliveCellColor, deadCellColor, cellBorderColor;
	
	private JPanel controlPanel;
	private JButton startStopToggle, resetButton, clearButton;
	private JSlider speedAdjust;
	private JTextField speedDisplay;
	
	
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
		
		
		// --- start file menu ---
		fileMenu = new JMenu("File");
		
		saveItem = new JMenuItem("Save World..", 
				new ImageIcon(this.getClass().getResource(("/res/saveIcon.png"))));
		fileMenu.add(saveItem);
		
		loadItem = new JMenuItem("Load World..", 
				new ImageIcon(this.getClass().getResource("/res/loadIcon.png")));
		fileMenu.add(loadItem);
		
		resizeItem = new JMenuItem("Resize World..", 
				new ImageIcon(this.getClass().getResource("/res/resizeIcon.png")));
		fileMenu.add(resizeItem);
		// --- end file menu ---
		
		
		menuBar.add(fileMenu);
		
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
		controlPanel = new JPanel(new GridLayout(1, 2));
		
		
		// --- start slider panel ---
		JPanel sliderPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.weightx = 1;
		constraints.gridwidth = 3;
		
		speedAdjust = new JSlider(0, 1000, 100);
		speedAdjust.setMajorTickSpacing(200);
		speedAdjust.setMinorTickSpacing(50);
		speedAdjust.setPaintTicks(true);
		speedAdjust.setPaintLabels(true);
		sliderPanel.add(speedAdjust, constraints);
		
		
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_END;
		
		speedDisplay = new JTextField();
		speedDisplay.setColumns(4);
		speedDisplay.setHorizontalAlignment(JTextField.CENTER);
		speedDisplay.setText(speedAdjust.getValue()+"");
		sliderPanel.add(speedDisplay, constraints);
		
		controlPanel.add(sliderPanel);
		// --- end slider panel ---
		
		
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
	
	public void setSpeedAdjustValue(int speed) {
		speedAdjust.setValue(speed);
	}
	
	public int getSpeedDisplayValue() {
		return Integer.parseInt(speedDisplay.getText());
	}
	
	public void setSpeedDisplayText(String text) {
		speedDisplay.setText(text);
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
	
	public void addSaveItemListener(ActionListener listener) {
		saveItem.addActionListener(listener);
	}
	
	public void addLoadItemListener(ActionListener listener) {
		loadItem.addActionListener(listener);
	}
	
	public void addResizeItemListener(ActionListener listener) {
		resizeItem.addActionListener(listener);
	}
	
	public void addSpeedAdjustListener(ChangeListener listener) {
		speedAdjust.addChangeListener(listener);
	}
	
	public void addSpeedDisplayListener(ActionListener listener) {
		speedDisplay.addActionListener(listener);
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
	
	public void addGridCellListener(int x, int y, MouseInputAdapter listener) {
		grid[x][y].addMouseListener(listener);
	}
}
