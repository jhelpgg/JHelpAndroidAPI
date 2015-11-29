package jhelp.android.api.engine.util;

import java.lang.reflect.Array;

/**
 * Divers utilities
 * 
 * @author JHelp
 */
public final class Utilities
{
	/**
	 * Indicates if a character is inside an array
	 * 
	 * @param character
	 *           Character search
	 * @param array
	 *           Array where search
	 * @return Character index
	 */
	public static boolean contains(final char character, final char... array)
	{
		for(int i = array.length - 1; i >= 0; i--)
		{
			if(array[i] == character)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Create a int array copy
	 * 
	 * @param array
	 *           Array to copy
	 * @return Copy of array
	 */
	public static int[] createCopy(final int[] array)
	{
		if(array == null)
		{
			return null;
		}

		final int length = array.length;

		final int[] clone = new int[length];

		System.arraycopy(array, 0, clone, 0, length);

		return clone;
	}

	/**
	 * Create an array copy
	 * 
	 * @param <T>
	 *           Type of array elements
	 * @param array
	 *           Array to copy
	 * @return Array copy
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] createCopy(final T[] array)
	{
		if(array == null)
		{
			return null;
		}

		final int length = array.length;

		final Class<T> classT = (Class<T>) array.getClass().getComponentType();

		final T[] clone = (T[]) Array.newInstance(classT, length);

		System.arraycopy(array, 0, clone, 0, length);

		return clone;
	}

	/**
	 * Index of an element inside an array
	 * 
	 * @param <T>
	 *           Element type
	 * @param array
	 *           Array where search
	 * @param element
	 *           Element search
	 * @return Index where is element or -1 if not found
	 */
	public static <T> int indexOf(final T[] array, final T element)
	{
		if(array == null)
		{
			return -1;
		}

		final int length = array.length;

		for(int i = 0; i < length; i++)
		{
			if((array[i] == null) && (element == null))
			{
				return i;
			}
			else if((array[i] != null) && (array[i].equals(element) == true))
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Indicates if a class extends or implements directly or not a class or a interface
	 * 
	 * @param testedClass
	 *           Class tested
	 * @param parentOrInterface
	 *           Class or interface to extends or implements
	 * @return {@code true} if the class extends or implements directly or not the class or interface
	 */
	public static final boolean isSubTypeOf(Class<?> testedClass, final Class<?> parentOrInterface)
	{
		while(testedClass != null)
		{
			if(testedClass.equals(parentOrInterface) == true)
			{
				return true;
			}

			for(final Class<?> interf : testedClass.getInterfaces())
			{
				return Utilities.isSubTypeOf(interf, parentOrInterface);
			}

			testedClass = testedClass.getSuperclass();
		}

		return false;
	}

	/**
	 * Give a random value between 0 (include) and given limit (exclude)
	 * 
	 * @param limit
	 *           Limit to respect
	 * @return Random value
	 */
	public static int random(final int limit)
	{
		if(limit == 0)
		{
			throw new IllegalArgumentException("limit can't be 0");
		}

		return (int) (Math.random() * limit);
	}

	/**
	 * Give random number inside an interval, each limit are includes
	 * 
	 * @param minimum
	 *           Minimum value
	 * @param maximum
	 *           Maximum value
	 * @return Random value
	 */
	public static int random(final int minimum, final int maximum)
	{
		final int min = Math.min(minimum, maximum);
		final int max = Math.max(minimum, maximum);

		return min + (int) (Math.random() * ((max - min) + 1));
	}

	/**
	 * Make thread call it sleep for specified time in milliseconds
	 * 
	 * @param milliseconds
	 *           Time to sleep in milliseconds
	 */
	public static final void sleep(final int milliseconds)
	{
		try
		{
			Thread.sleep(milliseconds);
		}
		catch(final Exception exception)
		{
		}
	}

	/**
	 * To avoid instance
	 */
	private Utilities()
	{
	}
}