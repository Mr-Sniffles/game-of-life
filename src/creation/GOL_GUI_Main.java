package creation;

import java.io.File;
import java.io.IOException;

import util.GOLErrorHandler;
import util.GOLFileHandler;



/* TODO:
 * Features to implement:
 *  - custom colors
 *  - custom rulesets
 *  - more??
 */

/**
 * Driver class for Conway's Game of Life GUI implementation. Can either be run
 * in CLI with an optional input world file argument, or by simply running it as
 * an executable jar.
 * 
 * @author Cameron Rader (github: Mr-Sniffles)
 * 
 */
public class GOL_GUI_Main {
	public static void main(String[] args) {

		GOLView view = new GOLView();

		CellWorld model = null;
		if ( args.length > 0 ) {
			int[][] world = null;
			try {
				world = GOLFileHandler.parseWorldFile(new File(args[0]));
			} catch (IOException e) {
				e.printStackTrace();
				System.err
						.println("\nError: Cannot read file. Make sure formatting is correct.");
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
