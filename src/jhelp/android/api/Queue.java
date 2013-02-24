package jhelp.android.api;

/**
 * A queue of element
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Element type
 */
public class Queue<ELEMENT>
{
	/**
	 * Element of the queue
	 * 
	 * @author JHelp
	 * @param <TYPE>
	 *           Element carry type
	 */
	static class Element<TYPE>
	{
		/** Next element */
		Element<TYPE>	next;
		/** Value carry */
		final TYPE		value;

		/**
		 * Create a new instance of Element
		 * 
		 * @param value
		 *           Value carry
		 */
		public Element(final TYPE value)
		{
			this.value = value;
		}
	}

	/** Head of the queue */
	private Element<ELEMENT>	head;
	/** Tail of the queue */
	private Element<ELEMENT>	tail;

	/**
	 * Create a new instance of Queue
	 */
	public Queue()
	{
	}

	/**
	 * Add an element at the end of the queue
	 * 
	 * @param value
	 *           Element to add
	 */
	public void inQueue(final ELEMENT value)
	{
		if(this.head == null)
		{
			this.head = this.tail = new Element<ELEMENT>(value);

			return;
		}

		this.tail.next = new Element<ELEMENT>(value);

		this.tail = this.tail.next;
	}

	/**
	 * Indicates if queue is empty
	 * 
	 * @return {@code true} if queue is empty
	 */
	public boolean isEmpty()
	{
		return this.head == null;
	}

	/**
	 * Get first element of the queue and remove it from the queue
	 * 
	 * @return First element of the queue
	 */
	public ELEMENT outQueue()
	{
		if(this.head == null)
		{
			return null;
		}

		final ELEMENT value = this.head.value;

		this.head = this.head.next;

		if(this.head == null)
		{
			this.tail = null;
		}

		return value;
	}
}