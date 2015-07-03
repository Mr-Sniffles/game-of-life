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

public final class GOLFileHandler {

	public static int[][] parseWorldFile(File fileTarget) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(fileTarget));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.printf("\nError: Could not find file: %s", fileTarget);
			System.exit(GOLErrorHandler.FILE_NOT_FOUND_ERROR);
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
			System.exit(GOLErrorHandler.GRID_SIZE_ERROR);
		}
		int[][] customWorld = new int[worldSize][worldSize];

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

	public static void saveWorldFile(File fileTarget, CellWorld world)
			throws IOException {

		BufferedWriter out = new BufferedWriter(new FileWriter(
				GOLFileHandler.formatFileName(fileTarget)));

		int size = world.getWorldSize();
		out.write(size + "\n");

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				out.write(world.getCellState(x, y) + "");
				if (y != size - 1) {
					out.write(" ");
				}
			}

			if (x != size - 1) {
				out.write("\n");
			}
		}

		out.close();
	}

	public static File formatFileName(File file) {
		return file.toString().contains(".txt") ? file
				: new File(file + ".txt");
	}
}
