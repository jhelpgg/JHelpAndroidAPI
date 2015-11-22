package jhelp.android.api.database.type;

/**
 * List of char
 * Created by jhelp on 21/11/15.
 */
public class CharacterList extends DatabaseType implements CharSequence
{
    /**
     * Cahar array
     */
    private char[] array;
    /**
     * Number of chars
     */
    private int    size;

    /**
     * Create empty list
     */
    public CharacterList()
    {
        this.array = new char[128];
        this.size = 0;
    }

    /**
     * Expand capacity, if need, for add some chars
     *
     * @param more Number of char to add
     */
    private void expand(int more)
    {
        if (this.size + more >= this.array.length)
        {
            int length = this.size + more;
            char[] array = new char[length + (length >> 3) + 1];
            System.arraycopy(this.array, 0, array, 0, this.size);
            this.array = array;
        }
    }

    /**
     * Append some chars at the end of the list
     *
     * @param characters Characters to add
     */
    public void append(char... characters)
    {
        if (characters.length == 0)
        {
            return;
        }

        this.expand(characters.length);
        System.arraycopy(characters, 0, this.array, this.size, characters.length);
        this.size += characters.length;
    }

    /**
     * Append a char srquence at the end of the list
     *
     * @param charSequence Char sequence to add
     */
    public void append(CharSequence charSequence)
    {
        int length = charSequence.length();

        if (length == 0)
        {
            return;
        }

        this.expand(length);

        for (int charIndex = 0; charIndex < length; charIndex++)
        {
            this.array[this.size++] = charSequence.charAt(charIndex);
        }
    }

    /**
     * Insert some chars to given position
     *
     * @param index      Index where insert
     * @param characters Characters to insert
     */
    public void insert(int index, char... characters)
    {
        if (characters.length == 0)
        {
            return;
        }

        if (index < 0)
        {
            index = 0;
        }

        if (index >= this.size)
        {
            this.append(characters);
            return;
        }

        this.expand(characters.length);
        System.arraycopy(this.array, index,
                         this.array, index + characters.length,
                         this.array.length - characters.length - index);
        System.arraycopy(characters, 0, this.array, index, characters.length);
        this.size += characters.length;
    }

    /**
     * Insert a char sequence at given index
     *
     * @param index        Index where insert
     * @param charSequence Char sequence to insert
     */
    public void insert(int index, CharSequence charSequence)
    {
        int length = charSequence.length();

        if (length == 0)
        {
            return;
        }

        if (index < 0)
        {
            index = 0;
        }

        if (index >= this.size)
        {
            this.append(charSequence);
            return;
        }

        this.expand(length);
        System.arraycopy(this.array, index,
                         this.array, index + length,
                         this.array.length - length - index);

        for (int charIndex = 0; charIndex < length; charIndex++)
        {
            this.array[index++] = charSequence.charAt(charIndex);
        }

        this.size += length;
    }

    /**
     * Remove a character
     *
     * @param index Character index to remove
     * @throws IllegalArgumentException If index is invalid
     */
    public void remove(int index)
    {
        this.remove(index, 1);
    }

    /**
     * Remove several character
     *
     * @param offset Character index where start to remove
     * @param length Number of char to remove
     * @throws IllegalArgumentException If offset is invalid
     */
    public void remove(int offset, int length)
    {
        if (offset < 0 || offset >= this.size)
        {
            throw new IllegalArgumentException(
                    "index must be in [0, " + this.size + "[ not " + offset);
        }

        length = Math.min(length, this.size - offset);

        if (length <= 0)
        {
            return;
        }

        System.arraycopy(this.array, offset + length,
                         this.array, offset,
                         this.array.length - offset - length);
        this.size -= length;
    }

    /**
     * Number of characters
     *
     * @return Number of characters
     */
    public int getSize()
    {
        return this.size;
    }

    /**
     * Obtain one character
     *
     * @param index Character index
     * @return The character
     * @throws IllegalArgumentException If index is invalid
     */
    public char getChar(int index)
    {
        if (index < 0 || index >= this.size)
        {
            throw new IllegalArgumentException(
                    "index must be in [0, " + this.size + "[ not " + index);
        }

        return this.array[index];
    }

    /**
     * Obtain a sub array of chars
     *
     * @param index  Char index to start to get chars
     * @param length Number of char to get
     * @return Extracted chars
     * @throws IllegalArgumentException If index is invalid
     */
    public char[] getChars(int index, int length)
    {
        if (index < 0 || index >= this.size)
        {
            throw new IllegalArgumentException(
                    "index must be in [0, " + this.size + "[ not " + index);
        }

        length = Math.min(length, this.size - index);

        if (length <= 0)
        {
            return new char[0];
        }

        char[] array = new char[length];
        System.arraycopy(this.array, index, array, 0, length);
        return array;
    }

    /**
     * Convert to char array
     *
     * @return Chars
     */
    public char[] toCharArray()
    {
        return this.getChars(0, this.size);
    }

    /**
     * Make the list empty
     */
    public void clear()
    {
        this.size = 0;
    }

    /**
     * Parse a string for fill the list
     *
     * @param serialized Serialized string
     */
    @Override
    public void parse(String serialized)
    {
        this.clear();
        this.append(serialized);
    }

    /**
     * Serialize the list to String
     *
     * @return Serialized String
     */
    @Override
    public String serialize()
    {
        return new String(this.array, 0, this.size);
    }

    /**
     * Number of characters
     *
     * @return Number of characters
     */
    @Override
    public int length()
    {
        return this.size;
    }

    /**
     * Obtain a character
     *
     * @param index Charater index
     * @return The character
     * @throws IllegalArgumentException If index is invalid
     */
    @Override
    public char charAt(int index)
    {
        return this.getChar(index);
    }

    /**
     * Extract a sub sequence
     *
     * @param start Start index inclusive
     * @param end   End  index exclusive
     * @return Sub sequence
     * @throws IllegalArgumentException If start is invalid
     */
    public CharacterList subList(int start, int end)
    {
        CharacterList characterList = new CharacterList();
        characterList.append(this.getChars(start, end - start));
        return characterList;
    }

    /**
     * Extract a sub sequence
     *
     * @param start Start index inclusive
     * @param end   End  index exclusive
     * @return Sub sequence
     * @throws IllegalArgumentException If start is invalid
     */
    @Override
    public CharSequence subSequence(int start, int end)
    {
        return this.subList(start, end);
    }
}