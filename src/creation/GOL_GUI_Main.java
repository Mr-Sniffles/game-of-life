package creation;

import java.io.IOException;

import util.GOLErrorHandler;
import util.GOLFileParser;


/* TODO:
 * Features to implement:
 * 	- Cursor drag cell placement
 *  - custom rulesets
 *  - Saving/Loading
 *  - more??
 */

public class GOL_GUI_Main {
	public static void main(String[] args) {
		
		GOLView view = new GOLView();
		
		CellWorld model = null;
		if( args.length > 0 ) {
			int[][] world = null;
			try {
				world = GOLFileParser.parseWorldFile(args[0]);
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("\nError: Cannot read file. Make sure formatting is correct.");
				System.exit(GOLErrorHandler.FILE_READ_ERROR);
			}
			model = new CellWorld(world);
		} else {
			model = new CellWorld();
		}
		
		GOLController controller = new GOLController(view, model);
		controller.beginSimulation();
	}
	
	
}
