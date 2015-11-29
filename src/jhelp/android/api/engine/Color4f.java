package jhelp.android.api.engine;

import java.nio.FloatBuffer;

import jhelp.android.api.engine.util.UtilBuffer;

/**
 * Represents a color RGBA
 * 
 * @author JHelp
 */
public class Color4f
{
	/**
	 * Dark grey color
	 * 
	 * @return Dark grey color
	 */
	public static Color4f createDarkGreyColor()
	{
		return new Color4f(0.25f);
	}

	/**
	 * Grey color
	 * 
	 * @return Grey color
	 */
	public static Color4f createGreyColor()
	{
		return new Color4f(0.5f);
	}

	/**
	 * Light grey color
	 * 
	 * @return Light grey color
	 */
	public static Color4f createLightGreyColor()
	{
		return new Color4f(0.75f);
	}

	/**
	 * White color
	 * 
	 * @return White color
	 */
	public static Color4f createWhiteColor()
	{
		return new Color4f(1);
	}

	/** Alpha */
	public float	alpha;
	/** Blue */
	public float	blue;
	/** Green */
	public float	green;
	/** Red */
	public float	red;

	/**
	 * Create a new instance of Color4f (Black)
	 */
	public Color4f()
	{
		this(0);
	}

	/**
	 * Create a new instance of Color4f (Grey)
	 * 
	 * @param grey
	 *           Grey level)
	 */
	public Color4f(final float grey)
	{
		this(grey, 1);
	}

	/**
	 * Create a new instance of Color4f (Grey)
	 * 
	 * @param grey
	 *           Grey level
	 * @param alpha
	 *           Alpha
	 */
	public Color4f(final float grey, final float alpha)
	{
		this(grey, grey, grey, alpha);
	}

	/**
	 * Create a new instance of Color4f RGB opaque
	 * 
	 * @param red
	 *           Red
	 * @param green
	 *           Green
	 * @param blue
	 *           Blue
	 */
	public Color4f(final float red, final float green, final float blue)
	{
		this(red, green, blue, 1);
	}

	/**
	 * Create a new instance of Color4f RGBA
	 * 
	 * @param red
	 *           Red
	 * @param green
	 *           Green
	 * @param blue
	 *           Blue
	 * @param alpha
	 *           Alpha
	 */
	public Color4f(final float red, final float green, final float blue, final float alpha)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}

	/**
	 * Push the color in the float buffer
	 * 
	 * @return Filled float buffer
	 */
	public FloatBuffer putInFloatBuffer()
	{
		UtilBuffer.TEMPORARY_FLOAT_BUFFER.rewind();
		UtilBuffer.TEMPORARY_FLOAT_BUFFER.put(this.red);
		UtilBuffer.TEMPORARY_FLOAT_BUFFER.put(this.green);
		UtilBuffer.TEMPORARY_FLOAT_BUFFER.put(this.blue);
		UtilBuffer.TEMPORARY_FLOAT_BUFFER.put(this.alpha);
		UtilBuffer.TEMPORARY_FLOAT_BUFFER.rewind();

		return UtilBuffer.TEMPORARY_FLOAT_BUFFER;
	}
}