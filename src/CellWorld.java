import java.beans.PropertyChangeSupport;

public class CellWorld {
	
	private PropertyChangeSupport propChangeSupp;
	
	public static final int ALIVE = 1;
	public static final int DEAD = 0;

	private long tickCount;

	private int size;
	private int[][] world;

	private int bornMin, bornMax, surviveMin, surviveMax;

	// ~ Constructors ----------------------------------------------------------

	public CellWorld() {
		tickCount = 0;

		size = 10;
		world = new int[size][size];

		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	public CellWorld(int sz) {
		tickCount = 0;

		size = sz;
		world = new int[size][size];

		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	public CellWorld(int[][] worldConfig) {
		tickCount = 0;

		world = worldConfig.clone();
		size = world.length;
		
		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	public CellWorld(int[][] worldConfig, String ruleSet) {
		tickCount = 0;

		world = worldConfig.clone();
		size = world.length;

		this.parseRuleSet(ruleSet);
	}

	// Format: B#(#)/S#(#)
	private void parseRuleSet(String ruleSet) {
		// TODO
	}

	// ~ Methods ---------------------------------------------------------------

	public int getWorldSize() {
		return size;
	}

	public int getCellState(int x, int y) {
		return world[x][y];
	}

	public void setCellState(int x, int y, int state) {
		if (state != 0 && state != 1) {
			throw new IllegalStateException("Invalid cell state: "
					+ "expected '0' or '1'.");
		} else {
			world[x][y] = state;
		}
	}

	public void tick() {
		int[][] nextGen = new int[size][size];

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int neighborCount = this.getNeighborCount(x, y);

				if (this.getCellState(x, y) == CellWorld.ALIVE
						&& neighborCount >= surviveMin
						&& neighborCount <= surviveMax) {
					nextGen[x][y] = CellWorld.ALIVE;
				} else if (this.getCellState(x, y) == CellWorld.DEAD
						&& neighborCount >= bornMin && neighborCount <= bornMax) {
					nextGen[x][y] = CellWorld.ALIVE;
				} else {
					nextGen[x][y] = CellWorld.DEAD;
				}
			}
		}

		world = nextGen.clone();

		tickCount++;
	}

	private int getNeighborCount(int x, int y) {
		int neighbors = 0;

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < size && j >= 0 && j < size) {
					neighbors += this.getCellState(i, j);
				}
			}
		}
		
		return this.getCellState(x, y) == CellWorld.ALIVE ? neighbors - 1 : neighbors;
	}

	public long getTickCount() {
		return tickCount;
	}

	public String toString() {
		StringBuilder out = new StringBuilder();

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				out.append(this.getCellState(x, y) + " ");
			}
			out.append("\n");
		}

		return out.toString();
	}
}
