package creation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.GOLErrorHandler;

public class GOLController {
	
	//##########################################################################
	//	Global Variables/Constants
	//##########################################################################
	
	private GOLView view;
	private CellWorld model;
	
	// in milliseconds
	private int simulationDelay;
	
	private boolean isRunning;
	
	//##########################################################################
	//	Constructors
	//##########################################################################
	
	public GOLController() {
		this.view = null;
		this.model = null;
	}
	
	public GOLController(GOLView view, CellWorld model) {
		this.view = view;
		this.model = model;
		
		this.init();
	}

	
	//##########################################################################
	//	Helper Methods
	//##########################################################################
	
	private void init() {
		simulationDelay = 100;
		isRunning = false;
		
		view.resizeGrid(model.getWorldSize());
		this.updateViewGrid();
		
		view.addSpeedAdjustListener(new SpeedAdjustListener());
		view.addStartStopToggleListener(new StartStopToggleListener());
		view.addResetButtonListener(new ResetButtonListener());
	}
	
	//##########################################################################
	//	Controller Methods
	//##########################################################################
	
	public void setModel(CellWorld model) {
		this.model = model;
	}

	public void setView(GOLView view) {
		this.view = view;
	}
	
	public void beginSimulation() {
		Thread simThread = new Thread( new SimulationLoop() );
		simThread.start();
	}
	
	private void updateViewGrid() {
		for(int x=0;x<model.getWorldSize();x++) {
			for(int y=0;y<model.getWorldSize();y++) {
				view.updateGridCell(x, y, model.getCellState(x, y));
			}
		}
	}

	
	//##########################################################################
	//	Internal Classes
	//##########################################################################
	
	class SpeedAdjustListener implements ChangeListener{

		@Override
		public void stateChanged(ChangeEvent e) {
			simulationDelay = view.getSpeedAdjustValue();
		}
		
	}
	
	class StartStopToggleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			isRunning = !isRunning;
			if( isRunning ) {
				view.setStartStopToggleText("Stop");
			} else {
				view.setStartStopToggleText("Start");
			}
		}

	}
	
	class ResetButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO
		}

	}
	
	class SimulationLoop implements Runnable {

		@Override
		public void run() {
			
			while( true ) {
				
				if( isRunning ) {
					try {
						Thread.sleep(simulationDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.exit(GOLErrorHandler.THREAD_INTERRUPT_ERROR);
					}
					//System.out.println(".");
					model.tick();
					updateViewGrid();
				}
				System.out.print(""); //keep-alive
			}
			
		}
		
	}
}