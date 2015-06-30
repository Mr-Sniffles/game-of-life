package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public final class GOLFileParser {
	
	//##########################################################################
	//	Parsing Methods
	//##########################################################################
	
	public static int[][] parseWorldFile(String fileName) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(new File(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.printf("Error: Could not find file: %s", fileName);
			System.exit(GOLErrorHandler.FILE_NOT_FOUND_ERROR);
		}
		
		int worldSize = 0;
		try {
			worldSize = Integer.parseInt(in.readLine());
		} catch( NumberFormatException e ) {
			e.printStackTrace();
			System.err.printf("\nError: Could not read input file. Make sure that "
					+ "the first line of the file is the size of your world's grid.");
			in.close();
			System.exit(GOLErrorHandler.GRID_SIZE_ERROR);
		}
		int[][] customWorld = new int[worldSize][worldSize];
		
		String currLine = null;
		int y = 0;
		while( (currLine = in.readLine()) != null ) {
			Scanner sc = new Scanner(currLine);
			
			for(int x=0;x<worldSize;x++) {
				customWorld[y][x] = sc.nextInt();
			}
			
			sc.close();
			y++;
		}
		
		in.close();
		
		return customWorld;
	}
}
