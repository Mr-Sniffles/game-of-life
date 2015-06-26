import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class GOLMain {

	static CellWorld cw;
	static JFrame display;
	static JPanel[][] grid;
	
	static final int SIZE = 10;
	
	public static void main(String[] args) {

		if( args.length == 1 ) {
			init(args[0]);
		} else if( args.length == 2 ) {
			init(args[0], args[1]);
		} else {
			init();
		}
		
		//System.out.println(cw+"\n\n");
		
		while( true ) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			cw.tick();
			updateDisplay();
			//System.out.println(cw+"\n\n");
		}
	}
	
	public static void init() {
		cw = new CellWorld(SIZE);
		initDisplay();
	}
	
	public static void init(String fileName) {
		try {
			cw = new CellWorld(parseWorldFile(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.printf("Unable to parse input file: %s", fileName);
			System.exit(3);
		}
		initDisplay();
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
		
		
		int worldSize = Integer.parseInt(in.readLine());
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
	
	public static void init(String fileName, String rules) {
		try {
			cw = new CellWorld(parseWorldFile(fileName), rules);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.printf("Unable to parse input file: %s", fileName);
			System.exit(3);
		}
		initDisplay();
	}
	
	private static void initDisplay() {
		display = new JFrame();
		grid = new JPanel[cw.getWorldSize()][cw.getWorldSize()];
		
		display.setLayout(new GridLayout(cw.getWorldSize(), cw.getWorldSize()));
		display.setPreferredSize(new Dimension(720, 720));
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		for(int x=0;x<cw.getWorldSize();x++) {
			for(int y=0;y<cw.getWorldSize();y++) {
				display.add( grid[x][y] = new JPanel() );
				grid[x][y].setBackground(Color.BLACK);
				grid[x][y].setBorder(new LineBorder(Color.gray, 1));
			}
		}
		
		updateDisplay();
		display.pack();
		display.setVisible(true);
	}
	
	private static void updateDisplay() {
		for(int x=0;x<cw.getWorldSize();x++) {
			for(int y=0;y<cw.getWorldSize();y++) {
				if( cw.getCellState(x, y) == CellWorld.ALIVE ) {
					grid[x][y].setBackground(Color.BLACK);
				} else if( cw.getCellState(x, y) == CellWorld.DEAD ) {
					grid[x][y].setBackground(Color.WHITE);
				}
			}
		}
	}

}
