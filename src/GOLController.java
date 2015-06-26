import java.util.ArrayList;


public class GOLController {
	
	private ArrayList<GOLView> views;
	private ArrayList<CellWorld> models;
	
	public GOLController(CellWorld model) {
		this.addModel(model);
	}

	// change to comply to MVC design?
	public int getWorldSize(int modelIndex) {
		return 0; //TODO
	}
	
	public void addModel(CellWorld model) {
		// TODO Auto-generated method stub
		
	}
}
