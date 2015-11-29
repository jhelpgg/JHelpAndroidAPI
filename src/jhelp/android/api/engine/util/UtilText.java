package jhelp.android.api.engine.util;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * Textual utilities
 * 
 * @author JHelp
 */
public final class UtilText
{
	/** Boolean primitive class name */
	public static final String		PRIMITIVE_BOOLEAN	= "boolean";
	/** Byte primitive class name */
	public static final String		PRIMITIVE_BYTE		= "byte";
	/** Char primitive class name */
	public static final String		PRIMITIVE_CHAR		= "char";
	/** Double primitive class name */
	public static final String		PRIMITIVE_DOUBLE	= "double";
	/** Float primitive class name */
	public static final String		PRIMITIVE_FLOAT	= "float";
	/** Int primitive class name */
	public static final String		PRIMITIVE_INT		= "int";
	/** Long primitive class name */
	public static final String		PRIMITIVE_LONG		= "long";
	/** Short primitive class name */
	public static final String		PRIMITIVE_SHORT	= "short";
	/** UTF-8 char set */
	public static final Charset	UTF8					= Charset.forName("UTF-8");

	/**
	 * Append an object to string buffer
	 * 
	 * @param stringBuffer
	 *           Where append
	 * @param object
	 *           Object to append
	 */
	private static void appendObject(final StringBuffer stringBuffer, final Object object)
	{
		if((object == null) || (object.getClass().isArray() == false))
		{
			stringBuffer.append(object);

			return;
		}

		stringBuffer.append('[');

		if(object.getClass().getComponentType().isPrimitive() == true)
		{
			final String name = object.getClass().getComponentType().getName();

			if(UtilText.PRIMITIVE_BOOLEAN.equals(name) == true)
			{
				for(final boolean element : (boolean[]) object)
				{
					UtilText.appendObject(stringBuffer, element);

					stringBuffer.append(", ");
				}
			}
			else if(UtilText.PRIMITIVE_BYTE.equals(name) == true)
			{
				for(final byte element : (byte[]) object)
				{
					UtilText.appendObject(stringBuffer, element);

					stringBuffer.append(", ");
				}
			}
			else if(UtilText.PRIMITIVE_CHAR.equals(name) == true)
			{
				for(final char element : (char[]) object)
				{
					UtilText.appendObject(stringBuffer, element);

					stringBuffer.append(", ");
				}
			}
			else if(UtilText.PRIMITIVE_DOUBLE.equals(name) == true)
			{
				for(final double element : (double[]) object)
				{
					UtilText.appendObject(stringBuffer, element);

					stringBuffer.append(", ");
				}
			}
			else if(UtilText.PRIMITIVE_FLOAT.equals(name) == true)
			{
				for(final float element : (float[]) object)
				{
					UtilText.appendObject(stringBuffer, element);

					stringBuffer.append(", ");
				}
			}
			else if(UtilText.PRIMITIVE_INT.equals(name) == true)
			{
				for(final int element : (int[]) object)
				{
					UtilText.appendObject(stringBuffer, element);

					stringBuffer.append(", ");
				}
			}
			else if(UtilText.PRIMITIVE_LONG.equals(name) == true)
			{
				for(final long element : (long[]) object)
				{
					UtilText.appendObject(stringBuffer, element);

					stringBuffer.append(", ");
				}
			}
			else if(UtilText.PRIMITIVE_SHORT.equals(name) == true)
			{
				for(final short element : (short[]) object)
				{
					UtilText.appendObject(stringBuffer, element);

					stringBuffer.append(", ");
				}
			}
		}
		else
		{
			for(final Object element : (Object[]) object)
			{
				UtilText.appendObject(stringBuffer, element);

				stringBuffer.append(", ");
			}
		}

		final int length = stringBuffer.length();
		stringBuffer.delete(length - 2, length);

		stringBuffer.append(']');
	}

	/**
	 * Add \ before each " we can chose do treat the ' as normal character or add \ before it also
	 * 
	 * @param string
	 *           String to replace
	 * @param simpleQuote
	 *           Indicates if we also add \ before '
	 * @return Result string
	 */
	public static String addAntiSlash(final String string, final boolean simpleQuote)
	{
		final int length = string.length();
		final StringBuffer stringBuffer = new StringBuffer(length);

		for(final char car : string.toCharArray())
		{
			switch(car)
			{
				case '\'':
					if(simpleQuote == false)
					{
						stringBuffer.append(car);
						break;
					}
					// No break
					//$FALL-THROUGH$
				case '\\':
				case '"':
					stringBuffer.append('\\');
					// No break, we want also add the character itself, like other
					// ones
					//$FALL-THROUGH$
				default:
					stringBuffer.append(car);
				break;
			}
		}

		return stringBuffer.toString();
	}

	/**
	 * Compute "distance" between two texts.<br>
	 * To compute how "far" are the tow texts, we take all words of each and if a word of one not exits in the other, we decide
	 * they are more far than if the word is in the second but not in the same place. If the word is in same place, we say
	 * distance is zero
	 * 
	 * @param text1
	 *           First text
	 * @param text2
	 *           Second text
	 * @param caseSensitive
	 *           Indicates if for word comparison case is important or not
	 * @param wordDelimiters
	 *           List of delimiters of words (Each characters of this string are see as separator and only them)
	 * @return The computed distance
	 */
	public static int computeDistance(String text1, String text2, final boolean caseSensitive, final String wordDelimiters)
	{
		if(text1 == null)
		{
			throw new NullPointerException("text1 musn't be null");
		}
		if(text2 == null)
		{
			throw new NullPointerException("text2 musn't be null");
		}
		if(caseSensitive == false)
		{
			text1 = text1.toUpperCase();
			text2 = text2.toUpperCase();
		}
		final Hashtable<String, Integer> wordList1 = new Hashtable<String, Integer>();
		final Hashtable<String, Integer> wordList2 = new Hashtable<String, Integer>();
		StringTokenizer stringTokenizer = new StringTokenizer(text1, wordDelimiters, false);
		int index = 0;
		while(stringTokenizer.hasMoreTokens() == true)
		{
			wordList1.put(stringTokenizer.nextToken(), index++);
		}
		stringTokenizer = new StringTokenizer(text2, wordDelimiters, false);
		index = 0;
		while(stringTokenizer.hasMoreTokens() == true)
		{
			wordList2.put(stringTokenizer.nextToken(), index++);
		}
		String key;
		int index1;
		int index2;
		Integer integer;
		int distance = 0;
		Enumeration<String> keys = wordList1.keys();
		while(keys.hasMoreElements() == true)
		{
			key = keys.nextElement();
			index1 = wordList1.get(key).intValue();
			integer = wordList2.get(key);
			if(integer == null)
			{
				distance += 10;
			}
			else
			{
				index2 = integer.intValue();
				if(index1 != index2)
				{
					distance++;
				}
			}
		}
		keys = wordList2.keys();
		while(keys.hasMoreElements() == true)
		{
			key = keys.nextElement();
			index2 = wordList2.get(key).intValue();
			integer = wordList1.get(key);
			if(integer == null)
			{
				distance += 10;
			}
			else
			{
				index1 = integer.intValue();
				if(index1 != index2)
				{
					distance++;
				}
			}
		}
		return distance;
	}

	/**
	 * Compute a name not inside a set of name
	 * 
	 * @param base
	 *           Base name
	 * @param names
	 *           Set of names
	 * @return Created name
	 */
	public static String computeNotInsideName(String base, final Collection<String> names)
	{
		while(names.contains(base) == true)
		{
			base = UtilText.computeOtherName(base);
		}

		return base;
	}

	/**
	 * Compute an other name for a String name. It add a number or increarse the number at the end of the String.<br>
	 * Use for auto generate names
	 * 
	 * @param name
	 *           Name base
	 * @return Computed name
	 */
	public static String computeOtherName(final String name)
	{
		final char[] characters = name.toCharArray();
		final int length = characters.length;
		int index = length - 1;

		while((index >= 0) && (characters[index] >= '0') && (characters[index] <= '9'))
		{
			index--;
		}

		index++;

		if(index >= length)
		{
			return UtilText.concatenate(name, " 0");
		}

		if(index == 0)
		{
			return String.valueOf(Integer.parseInt(name) + 1);
		}

		return UtilText.concatenate(name.substring(0, index), Integer.parseInt(name.substring(index)) + 1);
	}

	/**
	 * Concatenate several object to make a string representation
	 * 
	 * @param objects
	 *           Objects to concatenate
	 * @return String representation
	 */
	public static String concatenate(final Object... objects)
	{
		if((objects == null) || (objects.length < 1))
		{
			return "";
		}

		final StringBuffer stringBuffer = new StringBuffer(12 * objects.length);

		for(final Object object : objects)
		{
			UtilText.appendObject(stringBuffer, object);
		}

		return stringBuffer.toString();
	}

	/**
	 * Cut String in several parts
	 * 
	 * @param string
	 *           String to cut
	 * @param separator
	 *           Separator where cut
	 * @return Parts
	 */
	public static String[] cutSringInPart(final String string, final char separator)
	{
		if(string == null)
		{
			return null;
		}

		final int length = string.length();

		if(length == 0)
		{
			return new String[0];
		}

		final char[] chars = string.toCharArray();

		if(length == 1)
		{
			if(chars[0] == separator)
			{
				return new String[]
				{
					""
				};
			}

			return new String[]
			{
				string
			};
		}

		int count = 1;

		for(int i = 0; i < length; i++)
		{
			if(chars[i] == separator)
			{
				count++;
			}
		}

		final String[] array = new String[count];

		int start = 0;
		int index = 0;

		for(int i = 0; i < length; i++)
		{
			if(chars[i] == separator)
			{
				if(start < i)
				{
					array[index] = new String(chars, start, i - start);
				}
				else
				{
					array[index] = "";
				}

				index++;
				start = i + 1;
			}
		}

		if(start < length)
		{
			array[index] = new String(chars, start, length - start);
		}
		else
		{
			array[index] = "";
		}

		return array;
	}

	/**
	 * Index of a string in string array
	 * 
	 * @param tableau
	 *           Array where search
	 * @param chaine
	 *           String search
	 * @return Index or -1 if not found
	 */
	public static int getIndice(final String tableau[], final String chaine)
	{
		if(tableau == null)
		{
			return -1;
		}
		for(int i = 0; i < tableau.length; i++)
		{
			if(chaine.equals(tableau[i]))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Compute the index of a character in a string on ignoring characters between " or ' an in ignore characters with \ just
	 * beofre them
	 * 
	 * @param string
	 *           String where search
	 * @param character
	 *           Looking for character
	 * @return Index of character or -1 if not found
	 */
	public static int indexOfIgnoreString(final String string, final char character)
	{
		final char[] characters = string.toCharArray();
		final int length = characters.length;
		char ch;
		char delimString = 0;
		boolean antiSlash = false;

		for(int i = 0; i < length; i++)
		{
			ch = characters[i];

			if(antiSlash == true)
			{
				antiSlash = false;
			}
			else if(delimString > 0)
			{
				if(delimString == ch)
				{
					delimString = 0;
				}
			}
			else
			{
				switch(ch)
				{
					case '\\':
						antiSlash = true;
					break;
					case '"':
					case '\'':
						delimString = ch;
					break;
					default:
						if(ch == character)
						{
							return i;
						}
				}
			}
		}

		return -1;
	}

	/**
	 * Give index of a string inside an other one on ignoring characters bettwen string delimiters.<br>
	 * By example you type:<br>
	 * <code>int index=indexOfIgnoreStrings("Hello 'this is a test' and this end", "this", 0, UtilText.DEFAULT_STRING_LIMITERS);</code>
	 * <br>
	 * The index will be 26 (not 6)
	 * 
	 * @param text
	 *           Text where search
	 * @param search
	 *           Searched text
	 * @param startIndex
	 *           Index where start the search
	 * @param stringLimiters
	 *           String limiters to use
	 * @return Index found or -1 if not found
	 */
	public static int indexOfIgnoreStrings(final String text, final String search, final int startIndex,
			final String stringLimiters)
	{
		final int lengthText = text.length();
		final char[] charsText = text.toCharArray();

		final int lengthSearch = search.length();
		final char[] charsSearch = search.toCharArray();

		final char[] limiters = stringLimiters.toCharArray();

		boolean insideString = false;
		char currentStringLimiter = ' ';

		int index = startIndex;
		int i;
		char character;

		while((index + lengthSearch) <= lengthText)
		{
			character = charsText[index];

			if(insideString == true)
			{
				if(character == currentStringLimiter)
				{
					insideString = false;
				}
			}
			else if(character == charsSearch[0])
			{
				for(i = 1; i < lengthSearch; i++)
				{
					if(charsText[index + i] != charsSearch[i])
					{
						break;
					}
				}

				if(i == lengthSearch)
				{
					return index;
				}
			}
			else if(Utilities.contains(character, limiters) == true)
			{
				insideString = true;
				currentStringLimiter = character;
			}

			index++;
		}

		return -1;
	}

	/**
	 * Replace character that follow an \ by it's symbol : \n by carriage return, \t by tabulation and \&lt;other&gt; by
	 * &lt;other&gt;
	 * 
	 * @param string
	 *           String to replace
	 * @return Result string
	 */
	public static String interpretAntiSlash(final String string)
	{
		final StringBuffer stringBuffer = new StringBuffer(string.length());

		final int limit = string.length() - 1;
		int start = 0;
		int index = string.indexOf('\\');
		char car;

		while(index >= 0)
		{
			stringBuffer.append(string.substring(start, index));

			if(index < limit)
			{
				car = string.charAt(index + 1);
				switch(car)
				{
					case 'n':
						stringBuffer.append('\n');
					break;
					case 't':
						stringBuffer.append('\t');
					break;
					default:
						stringBuffer.append(car);
					break;
				}
			}

			start = index + 2;
			index = string.indexOf('\\', start);
		}

		stringBuffer.append(string.substring(start));

		return stringBuffer.toString();
	}

	/**
	 * Parse an hexadecimal string to integer
	 * 
	 * @param string
	 *           String to parse
	 * @return Integer
	 */
	public static int parseHexaString(final String string)
	{
		final char[] characters = string.toUpperCase().toCharArray();
		final int length = characters.length;
		if(length > 8)
		{
			throw new IllegalArgumentException(UtilText.concatenate(
					"The number in hexadecimal can't be more than 8 characters, so '", string, "' can't be converted"));
		}

		int integer = 0;
		char character;

		for(int i = 0; i < length; i++)
		{
			integer <<= 4;

			if(((character = characters[i]) >= '0') && (character <= '9'))
			{
				integer |= character - '0';
			}
			else if((character >= 'A') && (character <= 'F'))
			{
				integer |= (character - 'A') + 10;
			}
			else
			{
				throw new IllegalArgumentException(UtilText.concatenate("The string '", string, "' have illegal character at ", i));
			}
		}

		return integer;
	}

	/**
	 * Parse a string to integer
	 * 
	 * @param string
	 *           String to parse
	 * @return Integer
	 */
	public static int parseInteger(final String string)
	{
		if(string.startsWith("0x") == false)
		{
			return Integer.parseInt(string);
		}

		final char[] characters = string.substring(2).toUpperCase().toCharArray();
		final int length = characters.length;
		if(length > 8)
		{
			throw new IllegalArgumentException(
					UtilText.concatenate("The number in hexadecimal can't be more than 8 characters (after the 0x), so '", string,
							"' can't be converted"));
		}

		int integer = 0;
		char character;

		for(int i = 0; i < length; i++)
		{
			integer <<= 4;

			if(((character = characters[i]) >= '0') && (character <= '9'))
			{
				integer |= character - '0';
			}
			else if((character >= 'A') && (character <= 'F'))
			{
				integer |= (character - 'A') + 10;
			}
			else
			{
				throw new IllegalArgumentException(UtilText.concatenate("The string '", string, "' have illegal character at ",
						i + 2));
			}
		}

		return integer;
	}

	/**
	 * Read a UTF-8 string from a part of byte array
	 * 
	 * @param array
	 *           Array to read
	 * @param offset
	 *           Start read index in array
	 * @param length
	 *           Number of bytes to read
	 * @return Read string
	 */
	public static String readUTF8(final byte[] array, final int offset, final int length)
	{
		return new String(array, offset, length, UtilText.UTF8);
	}

	/**
	 * Remove all white characters of a string
	 * 
	 * @param string
	 *           String to "clean"
	 * @return String without white characters
	 */
	public static String removeWhiteCharacters(final String string)
	{
		if(string == null)
		{
			return null;
		}

		final int length = string.length();
		final char[] chars = string.toCharArray();
		final char[] result = new char[length];
		int size = 0;

		for(final char ch : chars)
		{
			if(ch > ' ')
			{
				result[size++] = ch;
			}
		}

		return new String(result, 0, size);
	}

	/**
	 * Create a String full of same character
	 * 
	 * @param character
	 *           Character to repeat
	 * @param time
	 *           Number of character
	 * @return Created string
	 */
	public static String repeat(final char character, final int time)
	{
		if(time < 1)
		{
			return "";
		}

		final char[] characters = new char[time];
		for(int i = 0; i < time; i++)
		{
			characters[i] = character;
		}

		return new String(characters);
	}

	/**
	 * Create string that repeat always the same string
	 * 
	 * @param string
	 *           String to repeat
	 * @param time
	 *           Number of repeat
	 * @return Result string
	 */
	public static String repeat(final String string, final int time)
	{
		final int length = string.length();
		if((time < 1) || (length == 0))
		{
			return "";
		}

		final StringBuffer stringBuffer = new StringBuffer(time * length);
		for(int i = 0; i < time; i++)
		{
			stringBuffer.append(string);
		}

		return stringBuffer.toString();
	}

	/**
	 * Replace "hole" by a value.<br>
	 * holes are like {0}, {8}, ... That means that the first replacement replace every holes {0}, then ninth every {8}, ...<br>
	 * Example:
	 * <table border>
	 * <tr>
	 * <td><center><b>originalString</b></center></td>
	 * <td><center><b>replacement</b></center></td>
	 * <td><center><b>result</b></center></td>
	 * </tr>
	 * <tr>
	 * <td>I want exchange this {0} with this {1}</td>
	 * <td>hammer, apple</td>
	 * <td>I want exchange this <b>hammer</b> with this <b>apple</b></td>
	 * </tr>
	 * <tr>
	 * <td>I saw a {0} run after a {1}. I wonder if the {1} can escape from the {0}</td>
	 * <td>dog, cat</td>
	 * <td>I saw a <b>dog</b> run after a <b>cat</b>. I wonder if the <b>cat</b> can escape from the <b>dog</b></td>
	 * </tr>
	 * </table>
	 * 
	 * @param originalString
	 *           String with "holes"
	 * @param replacement
	 *           List of replacement for "holes". First replace hole {0}, second [1}, third {2} ,....
	 * @return Result string with replacement
	 */
	public static String replaceHole(final String originalString, String... replacement)
	{
		StringBuffer stringBuffer;
		int start;
		int indexOpenHole;
		int indexCloseHole;
		int indexReplacement;

		if(originalString == null)
		{
			throw new NullPointerException("The originalString musn't be null !");
		}

		if(replacement == null)
		{
			replacement = new String[0];
		}

		stringBuffer = new StringBuffer();

		start = 0;
		indexOpenHole = originalString.indexOf('{');
		while(indexOpenHole >= 0)
		{
			indexCloseHole = originalString.indexOf('}', indexOpenHole + 1);
			if(indexCloseHole >= 0)
			{
				stringBuffer.append(originalString.substring(start, indexOpenHole));

				indexReplacement = -1;

				try
				{
					indexReplacement = Integer.parseInt(originalString.substring(indexOpenHole + 1, indexCloseHole));
				}
				catch(final Exception exception)
				{
					// Nothing to do here
				}

				if((indexReplacement >= 0) && (indexReplacement < replacement.length))
				{
					stringBuffer.append(replacement[indexReplacement]);
				}

				start = indexCloseHole + 1;
				indexOpenHole = originalString.indexOf('{', start);
			}
			else
			{
				indexOpenHole = -1;
			}
		}
		stringBuffer.append(originalString.substring(start));

		return stringBuffer.toString();
	}

	/**
	 * Replace holes like {0} or {2_s} or {1_###eet} or ... depends of the given numbers.<br>
	 * Examples:
	 * <table border>
	 * <tr>
	 * <td><center><b>originalString</b></center></td>
	 * <td><center><b>replacement</b></center></td>
	 * <td><center><b>result</b></center></td>
	 * </tr>
	 * <tr>
	 * <td>There {0} cat{0_s} and {1} foot{1_###eet}</td>
	 * <td>1, 1</td>
	 * <td>There 1 cat and 1 foot</td>
	 * </tr>
	 * <tr>
	 * <td>There {0} cat{0_s} and {1} foot{1_###eet}</td>
	 * <td>5,1</td>
	 * <td>There 5 cats and 1 foot</td>
	 * </tr>
	 * <tr>
	 * <td>There {0} cat{0_s} and {1} foot{1_###eet}</td>
	 * <td>1,3</td>
	 * <td>There 1 cat and 3 feet</td>
	 * </tr>
	 * <tr>
	 * <td>There {0} cat{0_s} and {1} foot{1_###eet}</td>
	 * <td>5, 3</td>
	 * <td>There 5 cats and 3 feet</td>
	 * </tr>
	 * </table>
	 * 
	 * @param originalString
	 *           String with "holes"
	 * @param values
	 *           List of values for "holes". First evaluate hole {0}, second [1}, third {2} ,....
	 * @return Result string with replacement
	 */
	public static String replaceSeveral(final String originalString, int... values)
	{
		StringBuffer stringBuffer;
		int start;
		int indexOpenHole;
		int indexCloseHole;
		int indexUnderscore;
		int indexReplacement;
		String several;

		if(originalString == null)
		{
			throw new NullPointerException("The originalString musn't be null !");
		}

		if(values == null)
		{
			values = new int[0];
		}

		stringBuffer = new StringBuffer();

		start = 0;
		indexOpenHole = originalString.indexOf('{');
		while(indexOpenHole >= 0)
		{
			indexCloseHole = originalString.indexOf('}', indexOpenHole + 1);
			if(indexCloseHole >= 0)
			{
				stringBuffer.append(originalString.substring(start, indexOpenHole));

				indexReplacement = -1;
				several = originalString.substring(indexOpenHole + 1, indexCloseHole);
				try
				{
					indexReplacement = Integer.parseInt(several);
					if((indexReplacement >= 0) && (indexReplacement < values.length))
					{
						stringBuffer.append(values[indexReplacement]);
					}
				}
				catch(final Exception exception)
				{
					indexUnderscore = several.indexOf('_');
					if(indexUnderscore >= 0)
					{
						try
						{
							indexReplacement = Integer.parseInt(several.substring(0, indexUnderscore));
							if((indexReplacement >= 0) && (indexReplacement < values.length) && (values[indexReplacement] > 1))
							{
								indexUnderscore++;
								while(several.charAt(indexUnderscore) == '#')
								{
									stringBuffer.delete(stringBuffer.length() - 1, stringBuffer.length());
									indexUnderscore++;
								}
								stringBuffer.append(several.substring(indexUnderscore));
							}
						}
						catch(final Exception exception1)
						{
							// Nothing to do, just ignore this error
						}
					}
				}

				start = indexCloseHole + 1;
				indexOpenHole = originalString.indexOf('{', start);
			}
			else
			{
				indexOpenHole = -1;
			}
		}
		stringBuffer.append(originalString.substring(start));

		return stringBuffer.toString();
	}

	/**
	 * Convert a byte array to hexadecimal representation
	 * 
	 * @param array
	 *           Array to convert
	 * @return Hexadecimal representation
	 */
	public static String toHexadecimal(final byte[] array)
	{
		final int length = array.length;
		final char[] chars = new char[length * 2];

		int indexWrite = 0;
		int b;

		for(int indexRead = 0; indexRead < length; indexRead++)
		{
			b = array[indexRead] & 0xFF;

			chars[indexWrite++] = Integer.toHexString((b >> 4) & 0xF).charAt(0);
			chars[indexWrite++] = Integer.toHexString(b & 0xF).charAt(0);
		}

		return new String(chars);
	}

	/**
	 * Convert string to UTF-8 array
	 * 
	 * @param string
	 *           String to convert
	 * @return Converted string
	 */
	public static byte[] toUTF8(final String string)
	{
		return string.getBytes(UtilText.UTF8);
	}

	/** To avoid instance */
	private UtilText()
	{
	}
}