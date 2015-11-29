package jhelp.android.api.engine.util;

import android.util.Log;
import android.util.Pair;

/**
 * Debug utilities
 * 
 * @author JHelp
 */
public class Debug
{
	/**
	 * Create message composed of couple (Class name, Message text)
	 * 
	 * @param message
	 *           Message base
	 * @return Created message pair
	 */
	private static Pair<String, String> createMessage(final Object... message)
	{
		final StackTraceElement stackTraceElement = (new Throwable()).getStackTrace()[2];

		return new Pair<String, String>(stackTraceElement.getClassName(), UtilText.concatenate(stackTraceElement.getClassName(),
				'.', stackTraceElement.getMethodName(), "  at ", stackTraceElement.getLineNumber(), " : ", message));
	}

	/**
	 * Print debug message
	 * 
	 * @param message
	 *           Message to print
	 */
	public static void printDebug(final Object... message)
	{
		final Pair<String, String> pair = Debug.createMessage(message);

		Log.d(pair.first, pair.second);
	}

	/**
	 * Print an error
	 * 
	 * @param error
	 *           Error to print
	 * @param message
	 *           Adding message
	 */
	public static void printError(final Error error, final Object... message)
	{
		final Pair<String, String> pair = Debug.createMessage(message);

		Log.e(pair.first, pair.second, error);
	}

	/**
	 * Print an exception
	 * 
	 * @param exception
	 *           Exception to print
	 * @param message
	 *           Additional message
	 */
	public static void printException(final Exception exception, final Object... message)
	{
		final Pair<String, String> pair = Debug.createMessage(message);

		Log.e(pair.first, pair.second, exception);
	}

	/**
	 * Print a to do message
	 * 
	 * @param message
	 *           Explains what to do
	 */
	public static void printTodo(final Object... message)
	{
		final Pair<String, String> pair = Debug.createMessage("---TODO---", message);

		Log.i(pair.first, pair.second);
	}

	/**
	 * Print trace, to know the stack trace
	 * 
	 * @param message
	 *           Additional message
	 */
	public static void printTrace(final Object... message)
	{
		final Pair<String, String> pair = Debug.createMessage(message);

		Log.v(pair.first, pair.second);

		final StackTraceElement[] stackTraceElements = (new Throwable()).getStackTrace();
		final int legnth = stackTraceElements.length;
		StackTraceElement stackTraceElement;

		Log.v(pair.first, "*** START TRACE ***");
		for(int i = 1; i < legnth; i++)
		{
			stackTraceElement = stackTraceElements[i];

			Log.v(pair.first, UtilText.concatenate(stackTraceElement.getClassName(), '.', stackTraceElement.getMethodName(),
					"  at ", stackTraceElement.getLineNumber()));
		}

		Log.v(pair.first, "*** END TRACE ***");
	}

	/**
	 * Print verbose message
	 * 
	 * @param message
	 *           Message to print
	 */
	public static void printVerbose(final Object... message)
	{
		final Pair<String, String> pair = Debug.createMessage(message);

		Log.v(pair.first, pair.second);
	}

	/**
	 * Print warning message
	 * 
	 * @param message
	 *           Message to print
	 */
	public static void printWarning(final Object... message)
	{
		final Pair<String, String> pair = Debug.createMessage(message);

		Log.w(pair.first, pair.second);
	}
}