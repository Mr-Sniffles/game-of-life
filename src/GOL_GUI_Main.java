import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class GOL_GUI_Main {
	public static void main(String[] args) {
		GOLController controller = new GOLController();

		try {
			CellWorld model1 = new CellWorld(parseWorldFile(args[0]));
			controller.addModel(model1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		GOLView view1 = new GOLView();
		controller.addView(view1);
	}
	
	private static int[][] parseWorldFile(String fileName) throws IOException {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(new File(fileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.printf("Could not find file: %s", fileName);
			System.exit(2);
		}
		
		int worldSize = 0;
		try {
			worldSize = Integer.parseInt(in.readLine());
		} catch( NumberFormatException e) {
			e.printStackTrace();
			System.err.printf("\nError: Could not read input file. Make sure that "
					+ "the first line of the file is the size of your world's grid.");
			System.exit(4);
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
