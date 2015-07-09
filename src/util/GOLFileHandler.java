package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import creation.CellWorld;



// @formatter:off
/**
 * Utility class that handles input/output file parsing, formatting, and saving
 * of world configuration files.
 * 
 * 
 * ------------------------- File Format Specification: ------------------------
 * Files are stored in .txt format. Below is how the contents of files should be 
 * laid out:
 * 
 * Note: Let the world size be the integer N.
 * 
 * First line: N (i.e. 10)
 * 
 * All following lines: 
 * 		Rows: N space-separated integers(1=alive cell, 0=dead cell)
 * 		Columns: N integers(1=alive cell, 0=dead cell)
 * 
 * 
 * 
 * ---- Example File Representation: ----
 * 3
 * 0 1 1 
 * 0 1 0
 * 1 0 0
 * 
 * 
 * 
 * @author Cameron Rader (github: Mr-Sniffles)
 *
 */
// @formatter:on
public final class GOLFileHandler {

	/**
	 * Parse a given file with the game-of-life file format specification and
	 * return a 2D integer array representation of the world configuration file.
	 * 
	 * @param fileTarget
	 *            World configuration file to parse
	 * @return 2D array representation of parsed world configuration
	 * @throws IOException
	 *             Occurs when an unexpected format is given
	 */
	public static int[][] parseWorldFile(File fileTarget) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileTarget));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.printf("\nError: Could not find file: %s", fileTarget);
		}

		int worldSize = 0;
		try {
			worldSize = Integer.parseInt(in.readLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
			System.err
					.printf("\nError: Could not read input file. Make sure that "
							+ "the first line of the file is the size of your world's grid.");
			in.close();
		}

		int[][] customWorld = new int[worldSize][worldSize];

		// Read in file and create the world according to the file format
		// specification
		String currLine = null;
		int y = 0;
		while ((currLine = in.readLine()) != null) {
			Scanner sc = new Scanner(currLine);

			for (int x = 0; x < worldSize; x++) {
				customWorld[y][x] = sc.nextInt();
			}

			sc.close();
			y++;
		}

		in.close();

		return customWorld;
	}

	/**
	 * Write the given world model to file and save it at the given location
	 * using the file format specification
	 * 
	 * @param fileTarget
	 *            File location to save to
	 * @param world
	 *            World Model to save
	 * @throws IOException
	 *             Occurs when the file cannot be written to
	 */
	public static void saveWorldFile(File fileTarget, CellWorld world)
			throws IOException {

		BufferedWriter out = new BufferedWriter(new FileWriter(
				GOLFileHandler.formatFileName(fileTarget)));

		int size = world.getWorldSize();
		out.write(size + "\n");
		out.write(world.toString());

		out.close();
	}

	/**
	 * Format and sanitize given file's name so that it meets file format
	 * specification
	 * 
	 * @param file
	 *            File to format
	 * @return Formatted file
	 */
	public static File formatFileName(File file) {
		return file.toString().contains(".txt") ? file
				: new File(file + ".txt");
	}
}
