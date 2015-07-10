package util;

/**
 * Central error code collection
 * 
 * @author Cameron Rader (github: Mr-Sniffles)
 *
 */
public final class GOLErrorHandler {
	// ##########################################################################
	// Global Variables/Constants
	// ##########################################################################

	/**
	 * Error value to return when specified file cannot be found.
	 */
	public static final int	FILE_NOT_FOUND_ERROR	= 1;
	/**
	 * Error value to return when specified grid size is invalid.
	 */
	public static final int	GRID_SIZE_ERROR			= 2;
	/**
	 * Error value to return when specified file cannot be read.
	 */
	public static final int	FILE_READ_ERROR			= 3;
	/**
	 * Error value to return when a thread is unexpectedly interrupted.
	 */
	public static final int	THREAD_INTERRUPT_ERROR	= 4;

}
