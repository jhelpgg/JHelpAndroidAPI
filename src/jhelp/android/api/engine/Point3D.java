package jhelp.android.api.engine;

import jhelp.android.api.engine.util.UtilMath;

/**
 * Represents a 3D point (X, Y, Z)
 *
 * @author JHelp
 */
public class Point3D
        implements Comparable<Point3D>
{
    /**
     * X
     */
    public float x;
    /**
     * Y
     */
    public float y;
    /**
     * Z
     */
    public float z;

    /**
     * Create a new instance of Point3D (0, 0, 0)
     */
    public Point3D()
    {
    }

    /**
     * Create a new instance of Point3D
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public Point3D(final float x, final float y, final float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * &nbsp;Compare&nbsp;with&nbsp;an&nbsp;other&nbsp;point.<br>
     * It&nbsp;returns&nbsp;:<br>
     * <table border=1>
     * <tr>
     * <th>Return&nbsp;value<br>
     * </th>
     * <th>Description<br>
     * </th>
     * </tr>
     * <tr>
     * <td>&lt;&nbsp;0<br>
     * </td>
     * <td>If&nbsp;this&nbsp;point&nbsp;is&nbsp;before&nbsp;the&nbsp;given&nbsp;one<br>
     * </td>
     * </tr>
     * <tr>
     * <td>=&nbsp;0<br>
     * </td>
     * <td>If&nbsp;the&nbsp;both&nbsp;points&nbsp;are&nbsp;equals<br>
     * </td>
     * </tr>
     * <tr>
     * <td>&gt;&nbsp;0<br>
     * </td>
     * <td>If&nbsp;this&nbsp;point&nbsp;is&nbsp;after&nbsp;the&nbsp;given&nbsp;one<br>
     * </td>
     * </tr>
     * </table>
     * <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param point Point to compare with
     * @return Comparison result
     * @see Comparable#compareTo(Object)
     */
    @Override
    public int compareTo(final Point3D point)
    {
        if (UtilMath.equals(this.x, point.x) == false)
        {
            if (this.x < point.x)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }

        if (UtilMath.equals(this.y, point.y) == false)
        {
            if (this.y < point.y)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }

        if (UtilMath.equals(this.z, point.z) == false)
        {
            if (this.z < point.z)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }

        return 0;
    }

    /**
     * Indicates if an Object is equals to this point <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param obj Object to compare with
     * @return {@code true} if equals
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (this.getClass() != obj.getClass())
        {
            return false;
        }
        final Point3D point = (Point3D) obj;

        return (UtilMath.equals(this.x, point.x) == true) && (UtilMath.equals(this.y,
                                                                              point.y) == true)
                && (UtilMath.equals(this.z, point.z) == true);
    }

    /**
     * Point hash code  <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Point hash code
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime  = 31;
        int       result = 1;
        result = (prime * result) + Float.floatToIntBits(this.x);
        result = (prime * result) + Float.floatToIntBits(this.y);
        result = (prime * result) + Float.floatToIntBits(this.z);
        return result;
    }
}