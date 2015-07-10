package creation;

/**
 * Represents the model(logic) of the implementation. The premise of Conway's
 * Game of Life is, given a finite grid and a predetermined ruleset, a
 * generational simulation is performed on a number of placed cells.These cells
 * have two states: dead or alive; the given ruleset determines if a cell lives,
 * dies, or is born between each generation.
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
	 * Indicates an alive cell
	 */
	public static final int	ALIVE	= 1;
	/**
	 * Indicates a dead cell
	 */
	public static final int	DEAD	= 0;

	/**
	 * Ticks since initial start of simulation. A tick is synonymous with a
	 * generation.
	 */
	private long			tickCount;

	/**
	 * Initial population of the world (i.e. number of alive cells) before the
	 * simulation starts.
	 */
	private long			initialPopulationCount;
	/**
	 * Population of the world (i.e. number of alive cells)
	 */
	private long			populationCount;

	/**
	 * Size of the world
	 */
	private int				size;
	/**
	 * The initial state of the world before the simulation starts.
	 */
	private int[][]			initialWorld;
	/**
	 * The current state of the world as the simulation progresses.
	 */
	private int[][]			world;

	/**
	 * Defines the rule set of the simulation. If a dead cell is within the
	 * range of bornMin and bornMax, then that cell is born as an alive cell. If
	 * an alive cell is within the range of surviveMin and surviveMax, then that
	 * cell remains alive. In all other cases the cell becomes a dead cell.
	 * 
	 * Note: The standard Game of Life is symbolized as "B3/S23": A cell is
	 * "Born" if it has exactly 3 neighbors, "Stays alive" if it has 2 or 3
	 * living neighbors; it dies otherwise.
	 */
	private int				bornMin, bornMax, surviveMin, surviveMax;

	// #########################################################################
	// Constructors
	// #########################################################################

	/**
	 * Creates new blank world with a size of ten. Uses default rule set of
	 * B3/S23.
	 */
	public CellWorld() {
		tickCount = 0;
		populationCount = 0;

		size = 10;
		initialWorld = new int[size][size];
		world = new int[size][size];

		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	/**
	 * Creates new blank world with a size of sz. Uses default rule set of
	 * B3/S23.
	 * 
	 * @param sz
	 *            Size of the world
	 */
	public CellWorld(int sz) {
		tickCount = 0;
		populationCount = 0;

		size = sz;
		initialWorld = new int[size][size];
		world = new int[size][size];

		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	/**
	 * Creates a world based on a pre-configured setup. Uses default rule set of
	 * B3/S23.
	 * 
	 * @precondition worldConfig is initialized
	 * 
	 * @param worldConfig
	 *            Pre-configured world setup
	 */
	public CellWorld(int[][] worldConfig) {
		tickCount = 0;

		initialWorld = this.duplicateArray(worldConfig);
		world = this.duplicateArray(worldConfig);
		size = world.length;

		initialPopulationCount = this.countInitialWorldPopulation();
		populationCount = initialPopulationCount;

		bornMin = bornMax = 3;
		surviveMin = 2;
		surviveMax = 3;
	}

	/**
	 * TODO: Creates a world based on a pre-configured setup. Uses
	 * pre-configured rule set.
	 * 
	 * @precondition worldConfig is initialized
	 * 
	 * @param worldConfig
	 *            Pre-configured world setup
	 * 
	 * @param ruleSet
	 *            Pre-configured rule set
	 */
	public CellWorld(int[][] worldConfig, String ruleSet) {
		tickCount = 0;

		initialWorld = this.duplicateArray(worldConfig);
		world = this.duplicateArray(worldConfig);
		size = world.length;

		initialPopulationCount = this.countInitialWorldPopulation();
		populationCount = initialPopulationCount;

		this.parseRuleSet(ruleSet);
	}

	// #########################################################################
	// Helper Methods
	// #########################################################################

	/**
	 * TODO: Parse a given rule set string for a born range and a survive range
	 * 
	 * Format: B#(#)/S#(#)
	 * 
	 * @param ruleSet
	 * @return
	 */
	private int[] parseRuleSet(String ruleSet) {
		return new int[0];
	}

	/**
	 * Gets the population count of the initial world state
	 * 
	 * Note: Should only be used when resetting or initializing worlds
	 * 
	 * @return Number of alive cells in the initial world state
	 */
	private long countInitialWorldPopulation() {
		long count = 0;
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				count += initialWorld[x][y];
			}
		}

		return count;
	}

	/**
	 * Gets the number cells that are alive adjacent to the cell at position (x,
	 * y).
	 * 
	 * @precondition x and y are positive integers within the bounds of the
	 *               world size
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
				if ( i >= 0 && i < size && j >= 0 && j < size ) {
					neighbors += this.getCellState(i, j);
				}
			}
		}

		return this.getCellState(x, y) == CellWorld.ALIVE ? neighbors - 1
				: neighbors;
	}

	/**
	 * Create a duplicated array equal to an input array.
	 * 
	 * @precondition arr is initialized
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
	 * Load a pre-configured world setup.
	 * 
	 * @precondition newWorld is initialized
	 * 
	 * @param newWorld
	 *            Pre-configured world setup
	 */
	public void loadWorld(int[][] newWorld) {
		world = this.duplicateArray(newWorld);
		initialWorld = this.duplicateArray(newWorld);
		size = world.length;
		initialPopulationCount = this.countInitialWorldPopulation();
		populationCount = initialPopulationCount;
		tickCount = 0;
	}

	/**
	 * TODO: Load a pre-configured rule set.
	 * 
	 * @precondition ruleSet follows the rule set format specification
	 * 
	 * @param ruleSet
	 *            Pre-configured rule set
	 */
	public void loadRuleSet(String ruleSet) {
	}

	/**
	 * Get the world size
	 * 
	 * @return World size
	 */
	public int getWorldSize() {
		return size;
	}

	/**
	 * Get the population(alive cell) count.
	 * 
	 * @return Number of alive cells
	 */
	public long getPopulationCount() {
		return populationCount;
	}

	/**
	 * Get the state of the cell at (x, y)
	 * 
	 * @precondition x and y are positive integers within the bounds of the
	 *               world size
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
	 * @precondition x and y are positive integers within the bounds of the
	 *               world size; and state is either 0 or 1.
	 * 
	 * @param x
	 *            X position of cell to set
	 * @param y
	 *            Y position of cell to set
	 * @param state
	 *            The state to set the cell to
	 */
	public void setCellState(int x, int y, int state) {
		if ( state != 0 && state != 1 ) {
			throw new IllegalStateException("Invalid cell state: "
					+ "expected '0' or '1'.");
		} else {
			world[x][y] = state;
			if ( state == CellWorld.ALIVE ) {
				populationCount++;
			} else {
				populationCount--;
			}
		}
	}

	/**
	 * Invert the state of the cell (i.e. 0 -> 1, 1 -> 0)
	 * 
	 * @precondition x and y are positive integers within the bounds of the
	 *               world size
	 * 
	 * @param x
	 *            X position of cell to check
	 * @param y
	 *            Y position of cell to check
	 */
	public void invertCellState(int x, int y) {
		// (0 + 1) % 2 = 1
		// (1 + 1) % 2 = 0
		int state = (world[x][y] + 1) % 2;
		world[x][y] = state;
		if ( state == CellWorld.ALIVE ) {
			populationCount++;
		} else {
			populationCount--;
		}
	}

	/**
	 * Process the next tick/generation of the world. Each cell is processed in
	 * in current state and its resulting state in placed in a new world. The
	 * resulting state is determined by the rule set of the world. Once all
	 * cells have been processed set the current the newly generated world.
	 * 
	 * @postcondition The world array holds the the generation immediately
	 *                following its generation at the time of
	 *                {@link CellWorld#tick()} invocation.
	 */
	public void tick() {
		int[][] nextGen = new int[size][size];
		long newPop = 0;

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				int neighborCount = this.getNeighborCount(x, y);

				if ( this.getCellState(x, y) == CellWorld.ALIVE
						&& neighborCount >= surviveMin
						&& neighborCount <= surviveMax ) {
					nextGen[x][y] = CellWorld.ALIVE;
					newPop++;
				} else if ( this.getCellState(x, y) == CellWorld.DEAD
						&& neighborCount >= bornMin && neighborCount <= bornMax ) {
					nextGen[x][y] = CellWorld.ALIVE;
					newPop++;
				} else {
					nextGen[x][y] = CellWorld.DEAD;
				}
			}
		}

		populationCount = newPop;
		world = nextGen;

		tickCount++;
	}

	/**
	 * Reset the state of the world to its initial state.
	 * 
	 * @postcondition The world array holds the zeroth generation
	 */
	public void reset() {
		world = this.duplicateArray(initialWorld);
		populationCount = initialPopulationCount;
		tickCount = 0;
	}

	/**
	 * Completely clear the world, making all cells dead.
	 * 
	 * @postcondition The world array is cleared of all living cells, and the
	 *                tick and population counts are set to zero.
	 */
	public void clear() {
		initialWorld = new int[size][size];
		world = new int[size][size];
		tickCount = 0;
		populationCount = initialPopulationCount = 0;
	}

	/**
	 * Resize the world to newSize and clear it.
	 * 
	 * @precondition newSize is greater than zero
	 * 
	 * @param newSize
	 *            New size of the world
	 * 
	 * @postcondition The world array is resized to newSize and cleared by
	 *                {@link CellWorld#clear()}.
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
		initialPopulationCount = populationCount;
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
	 * Gets the world as a formatted string. This string is compliant with the
	 * file format specification.
	 * 
	 * @return Formatted string representation of the world
	 */
	public String toString() {
		StringBuilder out = new StringBuilder();

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				out.append(this.getCellState(x, y) + " ");
				if ( y != size - 1 ) {
					out.append(" ");
				}
			}

			if ( x != size - 1 ) {
				out.append("\n");
			}
		}

		return out.toString();
	}

}
