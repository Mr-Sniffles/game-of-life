package creation;

import java.io.IOException;

import util.GOLErrorHandler;
import util.GOLFileParser;


public class GOL_GUI_Main {
	public static void main(String[] args) {
		
		GOLView view = new GOLView();
		
		int[][] world = null;
		try {
			world = GOLFileParser.parseWorldFile(args[0]);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error: Cannot read file. Make sure formatting is correct.");
			System.exit(GOLErrorHandler.FILE_READ_ERROR);
		}
		CellWorld model = new CellWorld(world);
		
		GOLController controller = new GOLController(view, model);
	}
	
	
}
