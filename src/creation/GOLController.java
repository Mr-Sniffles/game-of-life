package creation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import util.GOLErrorHandler;
import util.GOLFileHandler;



/**
 * Represents the controller of the implementation. It communicates state
 * changes between the model and view. The controller also controls the
 * simulation loop, processing changes and input as they come along.
 * 
 * @author Cameron Rader (github: Mr-Sniffles)
 *
 */
public class GOLController {

	// #########################################################################
	// Global Variables/Constants
	// #########################################################################

	/**
	 * Displays information from the model to the user
	 */
	private GOLView		view;
	/**
	 * Holds the logic and data of the simulation and performs simulation
	 * calculations
	 */
	private CellWorld	model;

	/**
	 * Delay(in milliseconds) between each tick of the simulation
	 */
	private int			simulationDelay;

	/**
	 * True if the simulation is running, false otherwise
	 */
	private boolean		isRunning;
	/**
	 * True if the left mouse button is being held down, false if it is not
	 */
	private boolean		mouseButtonDown;

	// #########################################################################
	// Constructors
	// #########################################################################

	/**
	 * Initializes a default, null MVC structure. View and model must be set
	 * before simulation can be used.
	 */
	public GOLController() {
		this.view = null;
		this.model = null;
	}

	/**
	 * Initializes a MVC structure given a view and a model
	 * 
	 * @precondition view and model are initialized
	 * 
	 * @param view
	 *            View to display
	 * @param model
	 *            Model to act on
	 */
	public GOLController(GOLView view, CellWorld model) {
		this.view = view;
		this.model = model;

		this.init();
	}

	// #########################################################################
	// Helper Methods
	// #########################################################################

	private void init() {
		simulationDelay = 100;
		isRunning = false;
		mouseButtonDown = false;

		view.resizeGrid(model.getWorldSize());
		this.updateViewGrid();
		view.setPopulationLabelValue(model.getPopulationCount());
		view.setGenerationLabelValue(model.getTickCount());

		view.addSaveItemListener(new SaveItemListener());
		view.addLoadItemListener(new LoadItemListener());
		view.addResizeItemListener(new ResizeItemListener());

		this.addViewGridListeners();

		view.addSpeedAdjustListener(new SpeedAdjustListener());
		view.addSpeedDisplayListener(new SpeedDisplayListener());

		view.addStartStopToggleListener(new StartStopToggleListener());
		view.addResetButtonListener(new ResetButtonListener());
		view.addClearButtonListener(new ClearButtonListener());
	}

	// #########################################################################
	// Controller Methods
	// #########################################################################

	public void setModel(CellWorld model) {
		this.model = model;
	}

	public void setView(GOLView view) {
		this.view = view;
	}

	private void addViewGridListeners() {
		for (int x = 0; x < model.getWorldSize(); x++) {
			for (int y = 0; y < model.getWorldSize(); y++) {
				view.addCellPanelListener(x, y, new GridCellListener());
			}
		}
	}

	public void beginSimulation() {
		new Thread(new SimulationLoop()).start();
		;
	}

	private void resetSimulation() {
		isRunning = false;

		model.reset();

		view.setPopulationLabelValue(model.getPopulationCount());
		view.setGenerationLabelValue(model.getTickCount());
		view.setStartStopToggleText("Start");
		this.updateViewGrid();
	}

	private void clearSimulation() {
		isRunning = false;
		view.clear();
		model.clear();

		this.updateViewGrid();
	}

	private void updateViewGrid() {
		for (int x = 0; x < model.getWorldSize(); x++) {
			for (int y = 0; y < model.getWorldSize(); y++) {
				view.updateGridCell(x, y, model.getCellState(x, y));
			}
		}
	}

	private void loadNewViewGrid() {
		view.resizeGrid(model.getWorldSize());
		this.addViewGridListeners();
		this.updateViewGrid();
	}

	private void resizeAll(int resizeValue) {
		model.resize(resizeValue);
		view.resizeGrid(resizeValue);
		this.addViewGridListeners();
		this.updateViewGrid();
	}

	// #########################################################################
	// Internal Classes
	// #########################################################################

	class SaveItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			isRunning = false;
			view.setStartStopToggleText("Start");

			int action = view.showSaveFileChooser();
			File selection = view.getFileChooserSelection();
			if ( selection != null && action == JFileChooser.APPROVE_OPTION ) {
				try {
					GOLFileHandler.saveWorldFile(selection, model);
				} catch (IOException exc) {
					exc.printStackTrace();
					System.err.println("\nError: Unable to save world.");
				}
			}
		}

	}

	class LoadItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			isRunning = false;
			view.setStartStopToggleText("Start");

			int action = view.showLoadFileChooser();
			File selection = view.getFileChooserSelection();
			if ( selection != null && action == JFileChooser.APPROVE_OPTION ) {
				try {
					int[][] world = GOLFileHandler.parseWorldFile(selection);
					model.loadWorld(world);
					loadNewViewGrid();
					view.setPopulationLabelValue(model.getPopulationCount());
					view.setGenerationLabelValue(model.getTickCount());
				} catch (IOException exc) {
					exc.printStackTrace();
					System.err.println("\nError: Cannot read file. "
							+ "Make sure formatting is correct.");
				}
			}
		}

	}

	class ResizeItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			isRunning = false;
			view.setStartStopToggleText("Start");

			view.showResizeDialog();
			String valueString = view.getResizeDialogValue();
			if ( valueString != null ) {
				try {
					int resizeValue = Integer.parseInt(valueString);
					if ( resizeValue > 0 ) {
						resizeAll(resizeValue);
					}
				} catch (NumberFormatException exc) {
					exc.printStackTrace();
					System.err.println("\nError: Input was not a number");
				}
			}
		}

	}

	class SpeedAdjustListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			int newSpeed = view.getSpeedAdjustValue();
			view.setSpeedDisplayText(newSpeed + "");
			simulationDelay = newSpeed;
		}

	}

	class SpeedDisplayListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int newSpeed = view.getSpeedDisplayValue();
			if ( newSpeed > 1000 ) {
				newSpeed = 1000;
			} else if ( newSpeed < 0 ) {
				newSpeed = 0;
			}

			view.setSpeedAdjustValue(newSpeed);
			simulationDelay = newSpeed;
		}

	}

	class StartStopToggleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if ( model.getTickCount() == 0 ) {
				model.syncInitialState();
			}

			isRunning = !isRunning;
			if ( isRunning ) {
				view.setStartStopToggleText("Stop");
			} else {
				view.setStartStopToggleText("Start");
			}
		}

	}

	class ResetButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			resetSimulation();
		}

	}

	class ClearButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			clearSimulation();
		}

	}

	class GridCellListener extends MouseInputAdapter {

		private void invertCell(MouseEvent e) {
			CellPanel src = (CellPanel) e.getSource();
			int x = src.getxPos();
			int y = src.getyPos();

			view.invertGridCell(x, y);
			model.invertCellState(x, y);
			view.setPopulationLabelValue(model.getPopulationCount());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseButtonDown = true;
			this.invertCell(e);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseButtonDown = false;
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if ( mouseButtonDown ) {
				this.invertCell(e);
			}
		}

	}

	class SimulationLoop implements Runnable {

		private void update() {
			model.tick();
			updateViewGrid();
			view.setPopulationLabelValue(model.getPopulationCount());
			view.setGenerationLabelValue(model.getTickCount());
		}

		@Override
		public void run() {

			while (true) {

				if ( isRunning ) {

					this.update();

					try {
						Thread.sleep(simulationDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.exit(GOLErrorHandler.THREAD_INTERRUPT_ERROR);
					}

				}
				System.out.print(""); // keep-alive
			}

		}

	}
}