package creation;

import util.GOLErrorHandler;

public class GOLController{
	
	//##########################################################################
	//	Global Variables/Constants
	//##########################################################################
	
	private GOLView view;
	private CellWorld model;
	
	
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
		view.resizeGrid(model.getWorldSize());
		
		while( model.getTickCount() < 100 ) {
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(GOLErrorHandler.THREAD_INTERRUPT_ERROR);
			}
			
			model.tick();
			this.updateViewGrid();
		}
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
	
	private void updateViewGrid() {
		for(int x=0;x<model.getWorldSize();x++) {
			for(int y=0;y<model.getWorldSize();y++) {
				view.updateGridCell(x, y, model.getCellState(x, y));
			}
		}
	}
	
}