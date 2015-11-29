package jhelp.android.api.engine;

import javax.microedition.khronos.opengles.GL10;

/**
 * Represents a material
 * 
 * @author JHelp
 */
public class Material
{
	/** Material alpha in [0, 1]. 0 : transparent, 1: opaque */
	public float	alpha;
	/** Diffuse color */
	public Color4f	diffuseColor	= Color4f.createGreyColor();
	/** Texture to use */
	public Texture	texture;

	/**
	 * Create a new instance of Material
	 */
	public Material()
	{
		this.alpha = 1;
	}

	/**
	 * Render the material in Open GL
	 * 
	 * @param gl
	 *           Open GL link
	 */
	public void render(final GL10 gl)
	{
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glColor4f(this.diffuseColor.red, this.diffuseColor.green, this.diffuseColor.blue, this.alpha);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, this.diffuseColor.putInFloatBuffer());

		if(this.texture != null)
		{
			gl.glEnable(GL10.GL_TEXTURE_2D);
			this.texture.bind(gl);
		}
	}
}