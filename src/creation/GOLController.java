package creation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
	 * Displays information from the model to the user.
	 */
	private GOLView		view;
	/**
	 * Holds the logic and data of the simulation and performs simulation
	 * calculations.
	 */
	private CellWorld	model;

	/**
	 * Delay(in milliseconds) between each tick of the simulation.
	 */
	private int			simulationDelay;

	/**
	 * True if the simulation is running, false otherwise.
	 */
	private boolean		isRunning;
	/**
	 * True if the left mouse button is being held down, false if it is not.
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
	 * Initializes a MVC structure given a view and a model.
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

	/**
	 * Initialize the controller by connecting listeners to the view and
	 * initializing local controller variables. Also syncs the view with the
	 * model.
	 */
	private void init() {
		simulationDelay = 100;
		isRunning = false;
		mouseButtonDown = false;

		// sync the view with model data
		view.resizeGrid(model.getWorldSize());
		this.updateViewGrid();
		view.setPopulationLabelValue(model.getPopulationCount());
		view.setGenerationLabelValue(model.getTickCount());

		// add menu listeners
		view.addSaveItemListener(new SaveItemListener());
		view.addLoadItemListener(new LoadItemListener());
		view.addResizeItemListener(new ResizeItemListener());

		// add cell panel listeners
		this.addViewGridListeners();

		// add speed listeners
		view.addSpeedAdjustListener(new SpeedAdjustListener());
		view.addSpeedDisplayListener(new SpeedDisplayListener());

		// add button control listeners
		view.addStartStopToggleListener(new StartStopToggleListener());
		view.addResetButtonListener(new ResetButtonListener());
		view.addClearButtonListener(new ClearButtonListener());
	}

	// #########################################################################
	// Controller Methods
	// #########################################################################

	/**
	 * Set the controller's underlying model.
	 * 
	 * @param model
	 *            Model to set
	 */
	public void setModel(CellWorld model) {
		this.model = model;
	}

	/**
	 * Set the controller's view that will be used to display data from the
	 * model.
	 * 
	 * @param view
	 *            View to set
	 */
	public void setView(GOLView view) {
		this.view = view;
	}

	/**
	 * Add listeners to every cell panel in the view grid display
	 * 
	 * @postcondition Each cell panel belonging to the view has a listener
	 *                attached to it.
	 */
	private void addViewGridListeners() {
		for (int x = 0; x < model.getWorldSize(); x++) {
			for (int y = 0; y < model.getWorldSize(); y++) {
				view.addCellPanelListener(x, y, new GridCellListener());
			}
		}
	}

	/**
	 * Start the simulation loop in a new thread.
	 * 
	 * @precondition All required data is initialized and the simulation is
	 *               ready to begin.
	 * 
	 * @postcondition The simulation loop is running.
	 */
	public void beginSimulation() {
		new Thread(new SimulationLoop()).start();
	}

	/**
	 * Reset the simulation to its initial state.
	 * 
	 * @precondition All required data is initialized.
	 * 
	 * @postcondition The simulation is stopped, the model is reset, and the
	 *                view displays the data from the new reset model.
	 */
	private void resetSimulation() {
		isRunning = false;

		model.reset();

		view.setPopulationLabelValue(model.getPopulationCount());
		view.setGenerationLabelValue(model.getTickCount());
		view.setStartStopToggleText("Start");
		this.updateViewGrid();
	}

	/**
	 * Clear the simulation completely.
	 * 
	 * @precondition All required data is initialized.
	 * 
	 * @postcondition The simulation is stopped, the model is cleared, and the
	 *                view displays the data from the new reset model.
	 */
	private void clearSimulation() {
		isRunning = false;
		view.clear();
		model.clear();

		this.updateViewGrid();
	}

	/**
	 * Update all cell panels of the view with the corresponding data from the
	 * model's world.
	 * 
	 * @precondition All required data is initialized.
	 * 
	 * @postcondition The view's panel grid is updated to match model's world.
	 */
	private void updateViewGrid() {
		for (int x = 0; x < model.getWorldSize(); x++) {
			for (int y = 0; y < model.getWorldSize(); y++) {
				view.updateGridCell(x, y, model.getCellState(x, y));
			}
		}
	}

	/**
	 * Load a new cell panel grid in the view that matches the model's world.
	 * Should only be called if the new grid is a different size than the
	 * current one.
	 * 
	 * @postcondition View's panel grid is updated to match model's world,
	 *                including size.
	 */
	private void loadNewViewGrid() {
		view.resizeGrid(model.getWorldSize());
		this.addViewGridListeners();
		this.updateViewGrid();
	}

	/**
	 * Resize both the model and the view grids to a size of resizeValue
	 * 
	 * @param resizeValue
	 *            New size of the model/view grids
	 * 
	 * @postcondition Grid of model/view are resized to a size of resizeValue
	 */
	private void resizeAll(int resizeValue) {
		model.resize(resizeValue);
		this.loadNewViewGrid();
	}

	// #########################################################################
	// Internal Classes
	// #########################################################################

	/**
	 * Listener for view's save menu item.
	 */
	class SaveItemListener implements ActionListener {

		/**
		 * Flag the simulation as not running, adjust appropriate view
		 * components, and open a save dialog.
		 */
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

	/**
	 * Listener for view's load menu item.
	 */
	class LoadItemListener implements ActionListener {

		/**
		 * Flag the simulation as not running, adjust appropriate view
		 * components, and open a load dialog. Will also update view upon
		 * loading a world configuration.
		 */
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

	/**
	 * Listener for view's resize menu item.
	 */
	class ResizeItemListener implements ActionListener {

		/**
		 * Flag the simulation as not running, adjust appropriate view
		 * components, and open a resize dialog. Will also check for appropriate
		 * input. If input is valid, then resize view and model cell grids.
		 */
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
					} else {
						JOptionPane.showMessageDialog(view,
								"Size must be a positive integer.");
					}
				} catch (NumberFormatException exc) {
					exc.printStackTrace();
					System.err.println("\nError: Input was not a number");
				}
			}
		}

	}

	/**
	 * Listener for view's speed adjust slider.
	 */
	class SpeedAdjustListener implements ChangeListener {

		/**
		 * Gets new value from speed adjust slider and updates model and view
		 * components.
		 */
		@Override
		public void stateChanged(ChangeEvent e) {
			int newSpeed = view.getSpeedAdjustValue();
			view.setSpeedDisplayText(newSpeed + "");
			simulationDelay = newSpeed;
		}

	}

	/**
	 * Listener for view's speed display.
	 */
	class SpeedDisplayListener implements ActionListener {

		/**
		 * Gets new value from the speed display field and updates model and
		 * view components. Also sanitizes input that is outside the speed
		 * adjust slider's range.
		 */
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

	/**
	 * Listener for view's start/stop toggle button.
	 */
	class StartStopToggleListener implements ActionListener {

		/**
		 * Inverts the running flag. If the simulation is being flagged to run
		 * for the first time(tickCount = 0), then also sets the initial state
		 * of the model.
		 */
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

	/**
	 * Listener for view's reset button.
	 */
	class ResetButtonListener implements ActionListener {

		/**
		 * Resets the entire simulation to its initial state.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			resetSimulation();
		}

	}

	/**
	 * Listener for view's clear button.
	 */
	class ClearButtonListener implements ActionListener {

		/**
		 * Clears the entire simulation to a blank state.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			clearSimulation();
		}

	}

	/**
	 * Listener for view's cell panel.
	 */
	class GridCellListener extends MouseInputAdapter {

		/**
		 * Helper method that inverts the cell clicked in the model and view,
		 * and update population count accordingly.
		 * 
		 * @param e
		 *            Event passed by mouse listener
		 */
		private void invertCell(MouseEvent e) {
			CellPanel src = (CellPanel) e.getSource();
			int x = src.getxPos();
			int y = src.getyPos();

			view.invertGridCell(x, y);
			model.invertCellState(x, y);
			view.setPopulationLabelValue(model.getPopulationCount());
		}

		/**
		 * Flag the left mouse button as pressed down and invert the cell
		 * clicked.
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			mouseButtonDown = true;
			this.invertCell(e);
		}

		/**
		 * Flag the left mouse button as released.
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			mouseButtonDown = false;
		}

		/**
		 * Invert the cell that the cursor is over if the left mouse button is
		 * flagged as pressed down.
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			if ( mouseButtonDown ) {
				this.invertCell(e);
			}
		}

	}

	/**
	 * Simulation control loop. Runs indefinitely once the simulation has been
	 * initialized and started.
	 */
	class SimulationLoop implements Runnable {

		/**
		 * Perform a model tick and update view with new model data.
		 */
		private void update() {
			model.tick();
			updateViewGrid();
			view.setPopulationLabelValue(model.getPopulationCount());
			view.setGenerationLabelValue(model.getTickCount());
		}

		/**
		 * If the simulation is flagged as running then process model ticks;
		 * otherwise, simply fire a keep-alive signal.
		 */
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