package creation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import util.GOLFileHandler;



/**
 * Represents the view(GUI) of the implementation. It displays information
 * provided by the model via the controller. The view also communicates user
 * input to the controller which is then communicated to the model by the
 * controller.
 * 
 * @author Cameron Rader (github: Mr-Sniffles)
 *
 */
@SuppressWarnings("serial")
public class GOLView extends JFrame {

	// ##########################################################################
	// Global Variables/Constants
	// ##########################################################################

	/**
	 * Menu bar for utility actions
	 */
	private JMenuBar		menuBar;
	/**
	 * Menu for file actions(i.e. load/save)
	 */
	private JMenu			fileMenu;
	/**
	 * Action items belonging to the file menu
	 */
	private JMenuItem		saveItem, loadItem, resizeItem;

	/**
	 * File chooser for saving and loading world configurations
	 */
	private JFileChooser	fileChooser;
	/**
	 * Result(user-input) gathered by the grid resizer dialog
	 */
	private String			resizeDialogValue;

	/**
	 * Container for grid display
	 */
	private JPanel			gridPanel;
	/**
	 * Grid of cell panels depicting the state of every cell in the world
	 */
	private CellPanel[][]	grid;
	/**
	 * Size of the world grid
	 */
	private int				gridSize;
	/**
	 * Color settings for cell panels
	 */
	private Color			aliveCellColor, deadCellColor, cellBorderColor;

	/**
	 * Container for controls and information about the simulation
	 */
	private JPanel			controlPanel;
	/**
	 * Simulation speed adjuster
	 */
	private JSlider			speedAdjust;
	/**
	 * Display and keyboard input for simulation speed
	 */
	private JTextField		speedDisplay;
	/**
	 * Simulation control buttons
	 */
	private JButton			startStopToggle, resetButton, clearButton;
	/**
	 * Information display labels
	 */
	private JLabel			populationLabel, generationLabel;

	// #########################################################################
	// Constructors
	// #########################################################################

	/**
	 * Creates a default view. This world is then altered to match the model by
	 * obtaining information from the controller after initialization.
	 */
	public GOLView() {
		gridSize = 10;
		aliveCellColor = Color.BLACK;
		deadCellColor = Color.WHITE;
		cellBorderColor = Color.GRAY;

		init();
	}

	// #########################################################################
	// Helper Methods
	// #########################################################################

	/**
	 * Initializes the default view including all components(i.e. buttons,
	 * menus, etc.)
	 */
	private void init() {
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(800, 720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.initMenu();

		this.initGridDisplay();

		this.initControls();

		this.pack();
		this.setVisible(true);

		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt",
				"txt", "text");
		fileChooser.setFileFilter(filter);

		resizeDialogValue = "";

	}

	/**
	 * Initializes and displays the menu bar and all sub-components
	 */
	private void initMenu() {
		menuBar = new JMenuBar();

		// --- start file menu ---
		fileMenu = new JMenu("File");

		saveItem = new JMenuItem("Save World..", new ImageIcon(this.getClass()
				.getResource(("/res/saveIcon.png"))));
		fileMenu.add(saveItem);

		loadItem = new JMenuItem("Load World..", new ImageIcon(this.getClass()
				.getResource("/res/loadIcon.png")));
		fileMenu.add(loadItem);

		resizeItem = new JMenuItem("Resize World..", new ImageIcon(this
				.getClass().getResource("/res/resizeIcon.png")));
		fileMenu.add(resizeItem);
		// --- end file menu ---

		menuBar.add(fileMenu);

		this.setJMenuBar(menuBar);
	}

	/**
	 * Initializes and displays the grid display
	 */
	private void initGridDisplay() {
		gridPanel = new JPanel();

		grid = new CellPanel[gridSize][gridSize];

		gridPanel.setLayout(new GridLayout(gridSize, gridSize));

		// initialize all cell panels and add them to the grid
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				JPanel p2 = grid[x][y] = new CellPanel(x, y);
				p2.setBorder(new LineBorder(cellBorderColor, 1));
				gridPanel.add(p2);
			}
		}

		this.add(gridPanel, BorderLayout.CENTER);
	}

	/**
	 * Initializes and displays all controls and information displays
	 */
	private void initControls() {
		controlPanel = new JPanel(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		// --- start slider panel ---

		// sub-panel of the control panel
		JPanel sliderPanel = new JPanel(new GridBagLayout());
		sliderPanel.setBorder(BorderFactory
				.createTitledBorder("Speed Control (ms)"));

		// slider with range 0 to 1000 with an initial position of 100
		speedAdjust = new JSlider(0, 1000, 100);
		speedAdjust.setMajorTickSpacing(200);
		speedAdjust.setMinorTickSpacing(50);
		speedAdjust.setPaintTicks(true);
		speedAdjust.setPaintLabels(true);

		// anchor to the left and stretch the slider so that is takes all free
		// space in the sliderPanel
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 1;
		constraints.gridwidth = 3;

		sliderPanel.add(speedAdjust, constraints);

		// anchor to the right
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_END;

		speedDisplay = new JTextField();
		speedDisplay.setColumns(4);
		speedDisplay.setHorizontalAlignment(JTextField.CENTER);
		speedDisplay.setText(speedAdjust.getValue() + "");

		sliderPanel.add(speedDisplay, constraints);

		// Anchor to the left and take 90% of the remaining free space in the
		// control panel
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_START;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.9;

		controlPanel.add(sliderPanel, constraints);

		// --- end slider panel ---

		// --- start info panel ---

		// sub-panel of the control panel
		JPanel infoPanel = new JPanel(new BorderLayout());
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

		populationLabel = new JLabel("Population: --");
		infoPanel.add(populationLabel, BorderLayout.NORTH);

		generationLabel = new JLabel("Generation: --");
		infoPanel.add(generationLabel, BorderLayout.SOUTH);

		// Pad 10px to the left and right, and take 10% of the remaining space
		// in the control panel
		constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 0.1;
		constraints.insets = new Insets(0, 10, 0, 10);

		controlPanel.add(infoPanel, constraints);

		// --- end info panel ---

		// --- start button panel ---

		// sub-panel of the control panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory
				.createTitledBorder("Simulation Control"));

		startStopToggle = new JButton("Start");
		buttonPanel.add(startStopToggle);

		resetButton = new JButton("Reset");
		buttonPanel.add(resetButton);

		clearButton = new JButton("Clear");
		buttonPanel.add(clearButton);

		// anchor to the right and fill space above and below in the control
		// panel
		constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.LINE_END;
		constraints.fill = GridBagConstraints.VERTICAL;

		controlPanel.add(buttonPanel, constraints);

		// --- end button panel ---

		this.add(controlPanel, BorderLayout.PAGE_END);
	}

	// #########################################################################
	// View Methods
	// #########################################################################

	/**
	 * Get the value of the simulation speed adjust slider
	 * 
	 * @return Value of speed adjust slider (milliseconds)
	 */
	public int getSpeedAdjustValue() {
		return speedAdjust.getValue();
	}

	/**
	 * Set the value of the simulation speed adjust slider
	 * 
	 * @param speed
	 *            New speed of speed adjust slider
	 */
	public void setSpeedAdjustValue(int speed) {
		speedAdjust.setValue(speed);
	}

	/**
	 * Get the value of the simulation speed display
	 * 
	 * @return Value of speed display
	 */
	public int getSpeedDisplayValue() {
		return Integer.parseInt(speedDisplay.getText());
	}

	/**
	 * Set the value of the simulation speed display
	 * 
	 * @return text New value of speed display
	 */
	public void setSpeedDisplayText(String text) {
		speedDisplay.setText(text);
	}

	/**
	 * Set the display text of the start/stop toggle button. This should
	 * typically be either "Start" or "Stop".
	 * 
	 * @param text
	 *            New text to display on button
	 */
	public void setStartStopToggleText(String text) {
		startStopToggle.setText(text);
	}

	/**
	 * Automatically formats and sets the population display value.
	 * 
	 * @param pop
	 *            New population count to display
	 */
	public void setPopulationLabelValue(long pop) {
		populationLabel.setText(String.format("Population: %06d", pop));
	}

	/**
	 * Automatically formats and sets the generation display value.
	 * 
	 * @param gen
	 *            New generation count to display
	 */
	public void setGenerationLabelValue(long gen) {
		generationLabel.setText(String.format("Generation: %019d", gen));
	}

	/**
	 * Update the state of the cell panel at position (x, y)
	 * 
	 * @param x
	 *            X position of the cell panel to change
	 * @param y
	 *            Y position of the cell panel to change
	 * @param state
	 *            New state of the cell panel at (x, y)
	 */
	public void updateGridCell(int x, int y, int state) {
		if ( state == CellWorld.ALIVE ) {
			grid[x][y].setBackground(aliveCellColor);
		} else {
			grid[x][y].setBackground(deadCellColor);
		}
	}

	/**
	 * Invert the state of the cell panel at position (x, y)
	 * 
	 * @param x
	 *            X position of the cell panel to invert
	 * @param y
	 *            Y position of the cell panel to invert
	 */
	public void invertGridCell(int x, int y) {
		if ( grid[x][y].getBackground().equals(aliveCellColor) ) {
			grid[x][y].setBackground(deadCellColor);
		} else {
			grid[x][y].setBackground(aliveCellColor);
		}
	}

	/**
	 * Resize the cell panel display grid to a specified size. This also
	 * destroys the current state of the cell panel display grid.
	 * 
	 * @param newSize
	 *            New size of the cell panel grid
	 */
	public void resizeGrid(int newSize) {
		// hide the grid panel until all changes are finished
		gridPanel.setVisible(false);

		gridSize = newSize;
		grid = new CellPanel[gridSize][gridSize];
		gridPanel.removeAll();

		gridPanel.setLayout(new GridLayout(gridSize, gridSize));

		// initialize all cell panels and add them to the grid
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				JPanel p2 = grid[x][y] = new CellPanel(x, y);
				p2.setBorder(new LineBorder(cellBorderColor));
				gridPanel.add(p2);
			}
		}

		// show the grid panel now that all changes are complete
		gridPanel.setVisible(true);
	}

	/**
	 * Reset the cell panel display so that all cells are displayed as dead.
	 */
	private void resetGrid() {
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				grid[x][y].setBackground(deadCellColor);
			}
		}
	}

	/**
	 * Clear the current state of the view to its initial state while preserving
	 * the simulation speed setting and grid size.
	 */
	public void clear() {
		this.resetGrid();
		this.setStartStopToggleText("Start");
		this.setPopulationLabelValue(0);
		this.setGenerationLabelValue(0);
	}

	/**
	 * Set the display color of living cells
	 * 
	 * @param c
	 *            New color of living cells
	 */
	public void setAliveCellColor(Color c) {
		aliveCellColor = c;
	}

	/**
	 * Set the display color of dead cells
	 * 
	 * @param c
	 *            New color of dead cells
	 */
	public void setDeadCellColor(Color c) {
		aliveCellColor = c;
	}

	/**
	 * Prompt the file chooser to display a Save dialog. Also checks if the
	 * chosen file name will overwrite an existing file and prompts the user to
	 * overwrite if the file already exists.
	 * 
	 * @return Option chosen by user(i.e. accept, cancel, etc.)
	 */
	public int showSaveFileChooser() {
		int action = fileChooser.showSaveDialog(this);

		// If the file does not already exist then this loop only runs once;
		// however, if the file does exist then the user is given an overwrite
		// prompt. If this prompt is declined the loop runs again and asks for a
		// different file. This continues until the user gives a file that does
		// not already exist, chooses to overwrite an existing file, or cancels
		// saving.
		boolean finished = true;
		do {
			if ( action == JFileChooser.APPROVE_OPTION ) {
				File selection = GOLFileHandler.formatFileName(fileChooser
						.getSelectedFile());
				if ( selection.exists() ) {
					int confirm = JOptionPane.showConfirmDialog(this,
							"Replace existing file?");

					if ( finished = (confirm != JOptionPane.YES_OPTION) ) {
						action = fileChooser.showSaveDialog(this);
					}
				}
			}
		} while (!finished);

		return action;
	}

	/**
	 * Prompt the file chooser to display a Load/Open dialog
	 * 
	 * @return Option chosen by user(i.e. accept, cancel, etc.)
	 */
	public int showLoadFileChooser() {
		return fileChooser.showOpenDialog(this);
	}

	/**
	 * Get the selection of the load/save file chooser. This should be called
	 * only once for each file selection since the file chooser's selected file
	 * is disposed upon calling this method.
	 * 
	 * @return Most recent file selected by file chooser
	 */
	public File getFileChooserSelection() {
		File sel = fileChooser.getSelectedFile();
		fileChooser.setSelectedFile(null);
		return sel;
	}

	/**
	 * Prompt a grid resize dialog expecting an integer for the new world size.
	 */
	public void showResizeDialog() {
		resizeDialogValue = JOptionPane.showInputDialog(this, "Resize grid..");
	}

	/**
	 * Get the value of the resize dialog.
	 * 
	 * @return Most recent resize dialog result
	 */
	public String getResizeDialogValue() {
		return resizeDialogValue;
	}

	// #########################################################################
	// Listener Methods
	// #########################################################################

	/**
	 * Add a listener to the save menu item
	 * 
	 * @param listener
	 *            Listener to add to the save menu item
	 */
	public void addSaveItemListener(ActionListener listener) {
		saveItem.addActionListener(listener);
	}

	/**
	 * Add a listener to the load menu item
	 * 
	 * @param listener
	 *            Listener to add to the load menu item
	 */
	public void addLoadItemListener(ActionListener listener) {
		loadItem.addActionListener(listener);
	}

	/**
	 * Add a listener to the resize menu item
	 * 
	 * @param listener
	 *            Listener to add to the resize menu item
	 */
	public void addResizeItemListener(ActionListener listener) {
		resizeItem.addActionListener(listener);
	}

	/**
	 * Add a listener to the simulation speed adjust slider
	 * 
	 * @param listener
	 *            Listener to add to the speed adjust slider
	 */
	public void addSpeedAdjustListener(ChangeListener listener) {
		speedAdjust.addChangeListener(listener);
	}

	/**
	 * Add a listener to the simulation speed display
	 * 
	 * @param listener
	 *            Listener to add to the speed display
	 */
	public void addSpeedDisplayListener(ActionListener listener) {
		speedDisplay.addActionListener(listener);
	}

	/**
	 * Add a listener to the start/stop toggle button
	 * 
	 * @param listener
	 *            Listener to add to the start/stop toggle button
	 */
	public void addStartStopToggleListener(ActionListener listener) {
		startStopToggle.addActionListener(listener);
	}

	/**
	 * Add a listener to the reset button
	 * 
	 * @param listener
	 *            Listener to add to the reset button
	 */
	public void addResetButtonListener(ActionListener listener) {
		resetButton.addActionListener(listener);
	}

	/**
	 * Add a listener to the clear button
	 * 
	 * @param listener
	 *            Listener to add to the clear button
	 */
	public void addClearButtonListener(ActionListener listener) {
		clearButton.addActionListener(listener);
	}

	/**
	 * Add listeners to the cell panel at position (x, y) on the grid. Listeners
	 * include motion and button detection.
	 * 
	 * @param x
	 *            X position of cell panel to add listeners to
	 * @param y
	 *            Y position of cell panel to add listeners to
	 * @param listener
	 *            Listener to add to cell panel at (x, y)
	 */
	public void addCellPanelListener(int x, int y, MouseInputAdapter listener) {
		grid[x][y].addMouseListener(listener);
		grid[x][y].addMouseMotionListener(listener);
	}
}
