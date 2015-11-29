package jhelp.android.api.engine;

import javax.microedition.khronos.opengles.GL10;

/**
 * Clone of 3D object
 * 
 * @author JHelp
 */
public class Clone3D
		extends Node3D
{
	/** Cloned object */
	private final Object3D	clonedObject;
	/** Clone material */
	public Material			material	= new Material();

	/**
	 * Create a new instance of Clone3D
	 * 
	 * @param clonedObject
	 *           Cloaned object
	 */
	public Clone3D(final Object3D clonedObject)
	{
		if(clonedObject == null)
		{
			throw new NullPointerException("clonedObject musn't be null");
		}

		this.clonedObject = clonedObject;
	}

	/**
	 * Draw clone in Open GL <br>
	 * <br>
	 * <b>Parent documentation:</b><br>
	 * {@inheritDoc}
	 * 
	 * @param gl
	 *           Open GL link
	 * @see jhelp.engine.Node3D#renderSpecific(GL10)
	 */
	@Override
	protected void renderSpecific(final GL10 gl)
	{
		final Material material = this.clonedObject.material;

		this.clonedObject.material = this.material;
		this.clonedObject.renderSpecific(gl);
		this.clonedObject.material = material;
	}
}