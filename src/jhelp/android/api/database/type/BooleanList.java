package jhelp.android.api.database.type;

import jhelp.android.api.ByteArray;

/**
 * Boolean list.<br>One integer contains 32 boolean on it
 * Created by jhelp on 22/11/15.
 */
public class BooleanList extends DatabaseType
{
    /**
     * Booleans. One integer contains 32 boolean on it
     */
    private int[] booleans;
    /**
     * Number of booleans stored
     */
    private int   size;

    /**
     * Create empty list
     */
    public BooleanList()
    {
        this.booleans = new int[8];
        this.size = 0;
    }

    /**
     * Expand array capacity, if neeed, for more boolean
     *
     * @param more Number of booleans to add
     */
    private void expand(int more)
    {
        if (this.size + more > this.booleans.length << 5)
        {
            int length = this.size + more;
            int[] array = new int[((length + (length >> 3) + 31) >> 5) + 1];
            System.arraycopy(this.booleans, 0, array, 0, this.booleans.length);
            this.booleans = array;
        }
    }

    /**
     * Number of booleans stored
     *
     * @return Number of booleans stored
     */
    public int getSize()
    {
        return this.size;
    }

    /**
     * Make the list empty
     */
    public void clear()
    {
        this.size = 0;
    }

    /**
     * Add booleans to the end of the list
     *
     * @param booleans Booleans to add
     */
    public void append(boolean... booleans)
    {
        if (booleans.length == 0)
        {
            return;
        }

        this.expand(booleans.length);
        int index = this.size >> 5;
        int shift = 31 - (this.size & 31);

        for (boolean bool : booleans)
        {
            if (bool == true)
            {
                this.booleans[index] |= 1 << shift;
            }
            else
            {
                this.booleans[index] &= ~(1 << shift);
            }

            shift--;

            if (shift > 0)
            {
                shift = 31;
                index++;
            }
        }

        this.size += booleans.length;
    }

    /**
     * Obtain a boolean
     *
     * @param index Boolean index
     * @return Desired boolean
     * @throws IllegalArgumentException if index is invalid
     */
    public boolean getBoolean(int index)
    {
        if (index < 0 || index >= this.size)
        {
            throw new IllegalArgumentException(
                    "index MUST be in [0, " + this.size + "[ not " + index);
        }

        return (this.booleans[index >> 5] & (1 << (31 - (index & 31)))) != 0;
    }

    /**
     * Parse a string previously serialized by {@link #serialize()}
     *
     * @param serialized Serialized string
     */
    @Override
    public void parse(String serialized)
    {
        ByteArray byteArray = new ByteArray();
        byteArray.parse(serialized);
        this.size = byteArray.readInteger();
        this.booleans = byteArray.readIntegerArray();
    }

    /**
     * Serialize the list in array
     *
     * @return Serialized string
     */
    @Override
    public String serialize()
    {
        ByteArray byteArray = new ByteArray();
        byteArray.writeInteger(this.getSize());
        byteArray.writeIntegerArray(this.booleans);
        return byteArray.serialize();
    }
}