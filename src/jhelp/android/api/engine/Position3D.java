package jhelp.android.api.engine;

import javax.microedition.khronos.opengles.GL10;

/**
 * Describes a 3D position
 * 
 * @author JHelp
 */
public class Position3D
{
	/** Angle X */
	public float	angleX;
	/** Angle Y */
	public float	angleY;
	/** Angle Z */
	public float	angleZ;
	/** Scale X */
	public float	scaleX;
	/** Scale Y */
	public float	scaleY;
	/** Scale Z */
	public float	scaleZ;
	/** X */
	public float	x;
	/** Y */
	public float	y;
	/** Z */
	public float	z;

	/**
	 * Create a new instance of Position3D
	 */
	public Position3D()
	{
		this.x = this.y = this.z = this.angleX = this.angleY = this.angleZ = 0;
		this.scaleX = this.scaleY = this.scaleZ = 1;
	}

	/**
	 * Create a new instance of Position3D
	 * 
	 * @param x
	 *           X
	 * @param y
	 *           Y
	 * @param z
	 *           Z
	 * @param angleX
	 *           Angle X
	 * @param angleY
	 *           Angle Y
	 * @param angleZ
	 *           Angle Z
	 * @param scaleX
	 *           Scale X
	 * @param scaleY
	 *           Scale Y
	 * @param scaleZ
	 *           Scale Z
	 */
	public Position3D(final float x, final float y, final float z, final float angleX, final float angleY, final float angleZ,
			final float scaleX, final float scaleY, final float scaleZ)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.angleX = angleX;
		this.angleY = angleY;
		this.angleZ = angleZ;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
		this.scaleZ = scaleZ;
	}

	/**
	 * Create a new instance of Position3D copy of other position
	 * 
	 * @param position3d
	 *           Position to copy
	 */
	public Position3D(final Position3D position3d)
	{
		this.x = position3d.x;
		this.y = position3d.y;
		this.y = position3d.y;

		this.angleX = position3d.angleX;
		this.angleY = position3d.angleY;
		this.angleZ = position3d.angleZ;

		this.scaleX = position3d.scaleX;
		this.scaleY = position3d.scaleY;
		this.scaleZ = position3d.scaleZ;
	}

	/**
	 * Apply the position in Open GL
	 * 
	 * @param gl
	 *           Open GL link
	 */
	public void apply(final GL10 gl)
	{
		gl.glScalef(this.scaleX, this.scaleY, this.scaleZ);
		gl.glTranslatef(this.x, this.y, this.z);
		gl.glRotatef(this.angleX, 1, 0, 0);
		gl.glRotatef(this.angleY, 0, 1, 0);
		gl.glRotatef(this.angleZ, 0, 0, 1);
	}

	/**
	 * Copy the position
	 * 
	 * @return The copy
	 */
	public Position3D copy()
	{
		return new Position3D(this);
	}

	/**
	 * Copy a position
	 * 
	 * @param position3d
	 *           Position to copy
	 */
	public void copy(final Position3D position3d)
	{
		this.x = position3d.x;
		this.y = position3d.y;
		this.y = position3d.y;

		this.angleX = position3d.angleX;
		this.angleY = position3d.angleY;
		this.angleZ = position3d.angleZ;

		this.scaleX = position3d.scaleX;
		this.scaleY = position3d.scaleY;
		this.scaleZ = position3d.scaleZ;
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
		final StringBuilder builder = new StringBuilder();
		builder.append("Position3D [x=");
		builder.append(this.x);
		builder.append(", y=");
		builder.append(this.y);
		builder.append(", z=");
		builder.append(this.z);
		builder.append(", angleX=");
		builder.append(this.angleX);
		builder.append(", angleY=");
		builder.append(this.angleY);
		builder.append(", angleZ=");
		builder.append(this.angleZ);
		builder.append(", scaleX=");
		builder.append(this.scaleX);
		builder.append(", scaleY=");
		builder.append(this.scaleY);
		builder.append(", scaleZ=");
		builder.append(this.scaleZ);
		builder.append("]");
		return builder.toString();
	}
}