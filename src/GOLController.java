import java.util.ArrayList;


public class GOLController {
	
	private ArrayList<GOLView> views;
	private ArrayList<CellWorld> models;
	
	public GOLController() {
		views = new ArrayList<GOLView>();
		models = new ArrayList<CellWorld>();
	}
	
	public GOLController(CellWorld model, GOLView view) {
		views = new ArrayList<GOLView>();
		models = new ArrayList<CellWorld>();
		
		this.addModel(model);
		this.addView(view);
	}

	// change to comply to MVC design?
	public int getWorldSize(int modelIndex) {
		return 0; //TODO
	}
	
	public void addModel(CellWorld model) {
		// TODO Auto-generated method stub
		
	}

	public void addView(GOLView view) {
		
	}
}