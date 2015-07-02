package creation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

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
		view.addSpeedDisplayListener(new SpeedDisplayListener());
		view.addStartStopToggleListener(new StartStopToggleListener());
		view.addResetButtonListener(new ResetButtonListener());
		view.addClearButtonListener(new ClearButtonListener());
		this.addViewGridListeners();
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
	
	private void addViewGridListeners() {
		for(int x=0;x<model.getWorldSize();x++) {
			for(int y=0;y<model.getWorldSize();y++) {
				view.addGridCellListener(x, y, new GridCellListener());
			}
		}
	}
	
	public void beginSimulation() {
		Thread simThread = new Thread( new SimulationLoop() );
		simThread.start();
	}
	
	private void resetSimulation() {
		isRunning = false;
		view.reset();
		model.reset();
		
		this.updateViewGrid();
	}
	
	private void clearSimulation() {
		isRunning = false;
		view.reset();
		model.clear();
		
		this.updateViewGrid();
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
	
	class SpeedAdjustListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			int newSpeed = view.getSpeedAdjustValue();
			view.setSpeedDisplayText(newSpeed+"");
			simulationDelay = newSpeed;
		}
		
	}
	
	class SpeedDisplayListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int newSpeed = view.getSpeedDisplayValue();
			if( newSpeed > 1000 ) {
				newSpeed = 1000;
			} else if( newSpeed < 0 ) {
				newSpeed = 0;
			}
			
			view.setSpeedAdjustValue(newSpeed);
			simulationDelay = newSpeed;
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

		@Override
		public void mouseClicked(MouseEvent e) {
			CellPanel src = (CellPanel) e.getSource();
			int x = src.getxPos();
			int y = src.getyPos();
			
			view.inverGridCell(x, y);
			model.invertCellState(x, y);
		}
		
		
		//FIXME
		@Override
		public void mouseDragged(MouseEvent e) {
			CellPanel src = (CellPanel) e.getSource();
			int x = src.getxPos();
			int y = src.getyPos();
			
			view.inverGridCell(x, y);
			model.invertCellState(x, y);
		}
		
	}
	
	class SimulationLoop implements Runnable {

		@Override
		public void run() {
			
			while( true ) {
				
				if( isRunning ) {

					model.tick();
					updateViewGrid();
					
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