package jhelp.android.api.engine.util;

/**
 * Math Utilities
 *
 * @author JHelp
 */
public class UtilMath
{
    /**
     * Epsilon precision
     */
    public static final float EPSILON = 1e-5f;

    /**
     * Indicates if 2 floats are equals
     *
     * @param real1 First float
     * @param real2 Second float
     * @return {@code true} if equals
     */
    public static boolean equals(final float real1, final float real2)
    {
        return Math.abs(real1 - real2) <= UtilMath.EPSILON;
    }

    /**
     * Indicates if a float is 0
     *
     * @param real Float to test
     * @return {@code true} if it is 0
     */
    public static boolean isNul(final float real)
    {
        return Math.abs(real) <= UtilMath.EPSILON;
    }

    public static final int log2(int value)
    {
        if (value <= 1)
        {
            return 0;
        }

        int log2 = 1;
        value >>= 1;

        while (value > 1)
        {
            log2++;
            value >>= 1;
        }

        return log2;
    }
}