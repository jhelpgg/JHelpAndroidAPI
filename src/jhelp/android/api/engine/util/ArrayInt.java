package jhelp.android.api.engine.util;

import java.util.ArrayList;
import java.util.Arrays;

import android.util.Pair;

/**
 * Array of integer.<br>
 * More optimized than {@link ArrayList ArrayList<Integer>}
 * 
 * @author JHelp
 */
public class ArrayInt
{
	/** Array of integer */
	private int[]		array;
	/** Actual size */
	private int			size;
	/** Indicates if array is sorted */
	private boolean	sorted;

	/**
	 * Create a new instance of ArrayInt
	 */
	public ArrayInt()
	{
		this(128);
	}

	/**
	 * Create a new instance of ArrayInt
	 * 
	 * @param initalSize
	 *           Initial capacity
	 */
	public ArrayInt(final int initalSize)
	{
		this.array = new int[Math.max(initalSize, 128)];
		this.size = 0;

		this.sorted = true;
	}

	/**
	 * Check if an index is valid
	 * 
	 * @param index
	 *           Index checked
	 * @throws IllegalArgumentException
	 *            if index not valid
	 */
	private void checkIndex(final int index)
	{
		if((index < 0) || (index >= this.size))
		{
			throw new IllegalArgumentException("index must be in [0, " + this.size + "[ not " + index);
		}
	}

	/**
	 * Expand, if need, the capacity
	 * 
	 * @param more
	 *           Number of free space at least need
	 */
	private void expand(final int more)
	{
		if((this.size + more) >= this.array.length)
		{
			int newSize = this.size + more;
			newSize += (newSize / 10) + 1;

			final int[] temp = new int[newSize];
			System.arraycopy(this.array, 0, temp, 0, this.size);

			this.array = temp;
		}
	}

	/**
	 * Call by garbage collector when want free some memory <br>
	 * <br>
	 * <b>Parent documentation:</b><br>
	 * {@inheritDoc}
	 * 
	 * @throws Throwable
	 *            On issue
	 * @see Object#finalize()
	 */
	@Override
	protected void finalize() throws Throwable
	{
		this.destroy();

		super.finalize();
	}

	/**
	 * Add an integer is the array
	 * 
	 * @param integer
	 *           Integer to add
	 */
	public void add(final int integer)
	{
		this.expand(1);

		this.sorted = (this.size == 0) || ((this.sorted == true) && (this.array[this.size - 1] <= integer));

		this.array[this.size] = integer;
		this.size++;
	}

	/**
	 * Clear the array
	 */
	public void clear()
	{
		this.size = 0;
	}

	/**
	 * Indicates if an integer is in the array.<br>
	 * Search is on O(n)
	 * 
	 * @param integer
	 *           Integer search
	 * @return {@code true} if the integer is inside
	 */
	public boolean contains(final int integer)
	{
		return this.getIndex(integer) >= 0;
	}

	/**
	 * Indicates if an integer is in the array.<br>
	 * Search is in O(LN(n)) but work only if the array is sorted
	 * 
	 * @param integer
	 *           Integer search
	 * @return {@code true} if the integer is inside
	 */
	public boolean containsSupposeSorted(final int integer)
	{
		return this.getIndexSupposeSorted(integer) >= 0;
	}

	/**
	 * Destroy the list
	 */
	public void destroy()
	{
		this.size = 0;
		this.array = null;
		this.sorted = true;

		Debug.printVerbose("Delete from memory");
	}

	/**
	 * Index of an integer or -1 if integer not in the array.<br>
	 * Search is on O(n)
	 * 
	 * @param integer
	 *           Integer search
	 * @return Integer index or -1 if integer not in the array
	 */
	public int getIndex(final int integer)
	{
		if(this.sorted == true)
		{
			return this.getIndexSupposeSorted(integer);
		}

		for(int i = 0; i < this.size; i++)
		{
			if(this.array[i] == integer)
			{
				return i;
			}
		}

		return -1;
	}

	/**
	 * Index of an integer or -1 if integer not in the array.<br>
	 * Search is in O(LN(n)) but work only if the array is sorted
	 * 
	 * @param integer
	 *           Integer search
	 * @return Integer index or -1 if integer not in the array
	 */
	public int getIndexSupposeSorted(final int integer)
	{
		if(this.size <= 0)
		{
			return -1;
		}

		int actual = this.array[0];

		if(integer < actual)
		{
			return -1;
		}

		if(integer == actual)
		{
			return 0;
		}

		int min = 0;
		int max = this.size - 1;

		actual = this.array[max];

		if(integer > actual)
		{
			return -1;
		}

		if(integer == actual)
		{
			return max;
		}

		int mil;
		while(min < (max - 1))
		{
			mil = (min + max) >> 1;
			actual = this.array[mil];

			if(integer == actual)
			{
				return mil;
			}

			if(integer > actual)
			{
				min = mil;
			}
			else
			{
				max = mil;
			}
		}

		return -1;
	}

	/**
	 * Obtain an integer from the array
	 * 
	 * @param index
	 *           Integer index
	 * @return Integer
	 */
	public int getInteger(final int index)
	{
		this.checkIndex(index);

		return this.array[index];
	}

	/**
	 * Obtain&nbsp;interval&nbsp;indexes&nbsp;for&nbsp;an&nbsp;integer.<br>
	 * It&nbsp;suppose&nbsp;the&nbsp;list&nbsp;is&nbsp;sorted,&nbsp;if&nbsp;not&nbsp;the&nbsp;case&nbsp;the&nbsp;return&nbsp;
	 * value&nbsp;will&nbsp;not&nbsp;be&nbsp;valid.<br>
	 * It&nbsp;returns&nbsp;:<br>
	 * <table border=1>
	 * <tr>
	 * <th>Return&nbsp;value<br>
	 * </th>
	 * <th>Condition<br>
	 * </th>
	 * </tr>
	 * <tr>
	 * <td>First&nbsp;element&nbsp;and&nbsp;second&nbsp;element&nbsp;are&nbsp;-1<br>
	 * </td>
	 * <td>If&nbsp;the&nbsp;intger&nbsp;is&nbsp;before&nbsp;the&nbsp;first&nbsp;element<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>First&nbsp;element&nbsp;and&nbsp;second&nbsp;elements&nbsp;are&nbsp;equals<br>
	 * </td>
	 * <td>
	 * The&nbsp;value&nbsp;of&nbsp;first&nbsp;and&nbsp;second&nbsp;are&nbsp;the&nbsp;exact&nbsp;index&nbsp;where&nbsp;is&nbsp;
	 * the&nbsp;integer<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>First&nbsp;element&nbsp;and&nbsp;second&nbsp;element&nbsp;are&nbsp;the&nbsp;size&nbsp;of&nbsp;the&nbsp;list<br>
	 * </td>
	 * <td>If&nbsp;the&nbsp;integer&nbsp;is&nbsp;after&nbsp;the&nbsp;last&nbsp;element<br>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td>First&nbsp;element&nbsp;and&nbsp;second&nbsp;elemnt&nbsp;are&nbsp;different<br>
	 * </td>
	 * <td>It&nbsp;indicates&nbsp;that&nbsp;the&nbsp;first&nbsp;element&nbsp;is&nbsp;the&nbsp;index&nbsp;of&nbsp;the&nbsp;integer
	 * &nbsp;just&nbsp;lower&nbsp;the&nbsp;integer,&nbsp;the&nbsp;second&nbsp;element&nbsp;is&nbsp;the&nbsp;index&nbsp;of&nbsp;
	 * &nbsp;just&nbsp;upper&nbsp;integer<br>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param integer
	 *           Integer to search interval
	 * @return Interval result
	 */
	public Pair<Integer, Integer> getIntervalSupposeSorted(final int integer)
	{
		int start = -1;
		int end = -1;

		if(this.size > 0)
		{
			int actual = this.array[0];

			if(integer == actual)
			{
				start = end = 0;
			}
			else if(integer > actual)
			{
				int min = 0;
				int max = this.size - 1;

				actual = this.array[max];

				if(integer == actual)
				{
					start = end = max;
				}
				else if(integer > actual)
				{
					start = end = this.size;
				}
				else
				{
					int mil;
					while(min < (max - 1))
					{
						mil = (min + max) >> 1;
						actual = this.array[mil];

						if(integer == actual)
						{
							start = end = mil;

							break;
						}

						if(integer > actual)
						{
							min = mil;
						}
						else
						{
							max = mil;
						}
					}

					if(start < 0)
					{
						start = min;
						end = max;
					}
				}
			}
		}

		return new Pair<Integer, Integer>(start, end);
	}

	/**
	 * Array size
	 * 
	 * @return Array size
	 */
	public int getSize()
	{
		return this.size;
	}

	/**
	 * Insert an integer at a given index
	 * 
	 * @param integer
	 *           Integer to insert
	 * @param index
	 *           Index where insert
	 */
	public void insert(final int integer, int index)
	{
		this.expand(1);

		if(index < 0)
		{
			index = 0;
		}

		if(index >= this.size)
		{
			this.add(integer);

			return;
		}

		this.sorted = (this.sorted == true) && ((index == 0) || (integer >= this.array[index - 1]))
				&& (integer <= this.array[index]);

		System.arraycopy(this.array, index, this.array, index + 1, this.array.length - index - 1);

		this.array[index] = integer;
		this.size++;
	}

	/**
	 * Indicates if array is empty
	 * 
	 * @return {@code true} if array is empty
	 */
	public boolean isEmpty()
	{
		return this.size == 0;
	}

	/**
	 * Indicates if array is sorted.<br>
	 * But it does it in fast way, so if the answer is {@code true}, its sure that the array is sorted, but if {@code false}
	 * indicates that sorted is unknown
	 * 
	 * @return {@code true} if array is sorted. {@code false} if not sure about sorted status
	 */
	public boolean isSortedFast()
	{
		return this.sorted;
	}

	/**
	 * Indicates if array is sorted.<br>
	 * It is a slower method than {@link #isSortedFast()} but the answer is accurate, that means if {@code false} is answer, you
	 * are sure that the array is not sorted
	 * 
	 * @return {@code true} if array is sorted. {@code false} if array not sorted
	 */
	public boolean isSortedSlow()
	{
		if(this.sorted == true)
		{
			return true;
		}

		int previous = this.array[0];
		int actual;

		for(int i = 1; i < this.size; i++)
		{
			actual = this.array[i];

			if(previous > actual)
			{
				return false;
			}

			previous = actual;
		}

		this.sorted = true;
		return true;
	}

	/**
	 * remove an integer
	 * 
	 * @param index
	 *           Index of intger to remove
	 */
	public void remove(final int index)
	{
		this.checkIndex(index);

		System.arraycopy(this.array, index + 1, this.array, index, this.size - index - 1);
		this.size--;

		if(this.size < 2)
		{
			this.sorted = true;
		}
	}

	/**
	 * Sort the array.<br>
	 * For example, [2, 5, 9, 2, 6, 2, 5, 7, 1] -> [1, 2, 2, 2, 5, 5, 6, 7, 9]
	 */
	public void sort()
	{
		if(this.sorted == true)
		{
			return;
		}

		Arrays.sort(this.array, 0, this.size);
		this.sorted = true;
	}

	/**
	 * Sort array in unique mode.<br>
	 * That is to say if tow integer are equals, only one is keep.<br>
	 * For example, [2, 5, 9, 2, 6, 2, 5, 7, 1] -> [1, 2, 5, 6, 7, 9]
	 */
	public void sortUniq()
	{
		if(this.size < 2)
		{
			return;
		}

		this.sort();
		int actual;
		int previous = this.array[this.size - 1];

		for(int index = this.size - 2; index >= 0; index--)
		{
			actual = this.array[index];

			if(actual == previous)
			{
				System.arraycopy(this.array, index + 1, this.array, index, this.size - index - 1);
				this.size--;
			}

			previous = actual;
		}
	}

	/**
	 * Convert in int array
	 * 
	 * @return Extracted array
	 */
	public int[] toArray()
	{
		final int[] array = new int[this.size];

		System.arraycopy(this.array, 0, array, 0, this.size);

		return array;
	}

	/**
	 * String representation <br>
	 * <br>
	 * <b>Parent documentation:</b><br>
	 * {@inheritDoc}
	 * 
	 * @return String representation
	 * @see Object#toString()
	 */
	@Override
	public String toString()
	{
		final StringBuilder stringBuilder = new StringBuilder("[");

		if(this.size > 0)
		{
			stringBuilder.append(this.array[0]);

			for(int i = 1; i < this.size; i++)
			{
				stringBuilder.append(", ");
				stringBuilder.append(this.array[i]);
			}
		}

		stringBuilder.append(']');

		return stringBuilder.toString();
	}
}