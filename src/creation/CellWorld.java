package creation;

/**
 * This class represents the model(logic) of a GUI implementation of Conway's
 * Game of Life. The premise of Conway's Game of Life is, given a finite grid
 * and a predetermined ruleset, a generational simulation is performed on a
 * number of placed cells.These cells have two states: dead or alive; the given
 * ruleset determines if a cell lives, dies, or is born between each generation.
 * 
 * For more information on Conway's Game of Life:
 * https://en.wikipedia.org/wiki/Conway's_Game_of_Life
 * 
 * 
 * @author Cameron Rader (github: Mr-Sniffles)
 * 
 */
public class CellWorld {

	// #########################################################################
	// Global Variables/Constants
	// #########################################################################

	/**
	 * Indicates cell state
	 */
	public static final int ALIVE = 1;
	public static final int DEAD = 0;

	/**
	 * ticks since initial start of simulation. A tick is synonymous with a
	 * generation.
	 */
	private long tickCount;

	/**
	 * Size of the world
	 */
	private int size;
	/**
	 * The initial state of the world before the simulation starts.
	 */
	private int[][] initialWorld;
	/**
	 * The current state of the world as the simulation progresses.
	 */
	private int[][] world;

	/**
	 * Defines the ruleset of the simulation. If a dead cell is within the range
	 * of bornMin and bornMax, then that cell is born as an alive cell. If an
	 * alive cell is within the range of surviveMin and surviveMax, then that
	 * cell remains alive. In all other cases the cell becomes a dead cell.
	 * 
	 * Note: The standard Game of Life is symbolised as "B3/S23": A cell is
	 * "Born" if it has exactly 3 neighbours, "Stays alive" if it has 2 or 3
	 * living neighbours; it dies otherwise.
	 */
	private int bornMin, bornMax, surviveMin, surviveMax;

	// #########################################################################
	// Constructors
	// #########################################################################

	/**
	 * Creates new blank world with a size of ten. Uses default ruleset of
	 * B3/S23.
	 */
	public CellWorld() {
		tickCount = 0;

		size = 10;
		initialWorld = new int[size][size];
		world = new int[size][size];

		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	/**
	 * Creates new blank world with a size of sz. Uses default ruleset of
	 * B3/S23.
	 * 
	 * @param sz
	 *            Size of the world
	 */
	public CellWorld(int sz) {
		tickCount = 0;

		size = sz;
		initialWorld = new int[size][size];
		world = new int[size][size];

		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	/**
	 * Creates a world based on a pre-configured setup. Uses default ruleset of
	 * B3/S23.
	 * 
	 * @param worldConfig
	 *            Pre-configured world setup
	 */
	public CellWorld(int[][] worldConfig) {
		tickCount = 0;

		initialWorld = this.duplicateArray(worldConfig);
		world = this.duplicateArray(worldConfig);
		size = world.length;

		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	/**
	 * TODO Creates a world based on a pre-configured setup. Uses pre-configured
	 * ruleset.
	 * 
	 * @param worldConfig
	 *            Pre-configured world setup
	 * 
	 * @param ruleSet
	 *            Pre-configured ruleset
	 */
	/*
	 * public CellWorld(int[][] worldConfig, String ruleSet) { tickCount = 0;
	 * 
	 * initialWorld = this.duplicateArray(worldConfig); world =
	 * this.duplicateArray(worldConfig); size = world.length;
	 * 
	 * this.parseRuleSet(ruleSet); }
	 */

	// #########################################################################
	// Helper Methods
	// #########################################################################

	// Format: B#(#)/S#(#)
	/*
	 * private int[] parseRuleSet(String ruleSet) { // TODO return new int[0]; }
	 */

	/**
	 * Gets the number cells that are alive adjacent to the cell at position (x,
	 * y).
	 * 
	 * @param x
	 *            X position of cell to check
	 * @param y
	 *            Y position of cell to check
	 * @return Number of living cells adjacent to the cell at (x, y)
	 */
	private int getNeighborCount(int x, int y) {
		int neighbors = 0;

		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (i >= 0 && i < size && j >= 0 && j < size) {
					neighbors += this.getCellState(i, j);
				}
			}
		}

		return this.getCellState(x, y) == CellWorld.ALIVE ? neighbors - 1
				: neighbors;
	}

	/**
	 * Create a duplicated array equal to an input array
	 * 
	 * @param arr
	 *            Array to duplicate
	 * @return Duplicated array
	 */
	private int[][] duplicateArray(int[][] arr) {
		int[][] dupe = new int[arr.length][arr.length];

		for (int x = 0; x < arr.length; x++) {
			for (int y = 0; y < arr.length; y++) {
				dupe[x][y] = arr[x][y];
			}
		}
		return dupe;
	}

	// #########################################################################
	// Model Methods
	// #########################################################################

	/**
	 * Load a pre-configured world setup
	 * 
	 * @param newWorld
	 *            Pre-configured world setup
	 */
	public void loadWorld(int[][] newWorld) {
		world = this.duplicateArray(newWorld);
		initialWorld = this.duplicateArray(newWorld);
		size = world.length;
	}

	/**
	 * Load a pre-configured ruleset
	 * 
	 * @param ruleSet
	 *            Pre-configured ruleset
	 */
	/*
	 * public void loadRuleSet(String ruleSet) { // TODO }
	 */

	/**
	 * Get the world size
	 * 
	 * @return World size
	 */
	public int getWorldSize() {
		return size;
	}

	/**
	 * Get the state of the cell at (x, y)
	 * 
	 * @param x
	 *            X position of cell to check
	 * @param y
	 *            Y position of cell to check
	 * @return The state of the cell at position (x, y)
	 */
	public int getCellState(int x, int y) {
		return world[x][y];
	}

	/**
	 * Set the state of the cell at (x, y)
	 * 
	 * @param x
	 *            X position of cell to set
	 * @param y
	 *            Y position of cell to set
	 * @param state
	 *            The state to set the cell to
	 */
	public void setCellState(int x, int y, int state) {
		if (state != 0 && state != 1) {
			throw new IllegalStateException("Invalid cell state: "
					+ "expected '0' or '1'.");
		} else {
			world[x][y] = state;
		}
	}

	/**
	 * Invert the state of the cell (i.e. 0 -> 1, 1 -> 0)
	 * 
	 * @param x
	 *            X position of cell to check
	 * @param y
	 *            Y position of cell to check
	 */
	public void invertCellState(int x, int y) {
		// (0 + 1) % 2 = 1
		// (1 + 1) % 2 = 0
		world[x][y] = (world[x][y] + 1) % 2;
	}

	/**
	 * Process the next tick/generation of the world. Each cell is processed in
	 * in current state and its resulting state in placed in a new world. The
	 * resulting state is determined by the ruleset of the world. Once all cells
	 * have been processed set the current the newly generated world.
	 */
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

		world = nextGen;

		tickCount++;
	}

	/**
	 * Reset the state of the world to its initial state.
	 */
	public void reset() {
		world = this.duplicateArray(initialWorld);
		tickCount = 0;
	}

	/**
	 * Completely clear the world, making all cells dead.
	 */
	public void clear() {
		initialWorld = new int[size][size];
		world = new int[size][size];
		tickCount = 0;
	}

	/**
	 * Resize the world to newSize and clear it.
	 * 
	 * @param newSize New size of the world
	 */
	public void resize(int newSize) {
		size = newSize;
		this.clear();
	}

	/**
	 * Sets the initial state of the world to the current state of the world.
	 */
	public void syncInitialState() {
		initialWorld = this.duplicateArray(world);
	}

	/**
	 * Get the number of ticks since start
	 * 
	 * @return Number of ticks since start
	 */
	public long getTickCount() {
		return tickCount;
	}

	/**
	 * Formats world in a 
	 */
	public String toString() {
		StringBuilder out = new StringBuilder();

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				out.append(this.getCellState(x, y) + " ");
				if (y != size - 1) {
					out.append(" ");
				}
			}

			if (x != size - 1) {
				out.append("\n");
			}
		}

		return out.toString();
	}

}
