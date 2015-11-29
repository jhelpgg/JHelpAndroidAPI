package jhelp.android.api.engine;

import java.util.ArrayList;

/**
 * Pool of points
 * 
 * @author JHelp
 */
class PoolPoints
{
	/** Point 2D pool */
	private static final ArrayList<Point2D>	points2D	= new ArrayList<Point2D>();
	/** Point 3D pool */
	private static final ArrayList<Point3D>	points3D	= new ArrayList<Point3D>();

	/**
	 * Clear, make empty, the pool
	 */
	static void clear()
	{
		PoolPoints.points3D.clear();
		PoolPoints.points2D.clear();
	}

	/**
	 * Obtain point 2D from pool
	 * 
	 * @param index
	 *           Point index
	 * @return Point 2D
	 */
	static Point2D obtainPoint2D(final int index)
	{
		return PoolPoints.points2D.get(index);
	}

	/**
	 * Obtain point 3D from pool
	 * 
	 * @param index
	 *           Point index
	 * @return Point 3D
	 */
	static Point3D obtainPoint3D(final int index)
	{
		return PoolPoints.points3D.get(index);
	}

	/**
	 * Store point 2D in the pool
	 * 
	 * @param x
	 *           X
	 * @param y
	 *           Y
	 * @return Point 2D index
	 */
	static int storePoint2D(final float x, final float y)
	{
		final Point2D point = new Point2D(x, y);

		int index = PoolPoints.points2D.indexOf(point);

		if(index < 0)
		{
			index = PoolPoints.points2D.size();

			PoolPoints.points2D.add(point);
		}

		return index;
	}

	/**
	 * Store point 3D in the pool
	 * 
	 * @param x
	 *           X
	 * @param y
	 *           Y
	 * @param z
	 *           Z
	 * @return Point 3D index
	 */
	static int storePoint3D(final float x, final float y, final float z)
	{
		final Point3D point = new Point3D(x, y, z);

		int index = PoolPoints.points3D.indexOf(point);

		if(index < 0)
		{
			index = PoolPoints.points3D.size();

			PoolPoints.points3D.add(point);
		}

		return index;
	}
}