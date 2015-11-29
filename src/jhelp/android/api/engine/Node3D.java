package jhelp.android.api.engine;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

/**
 * Represents a 3D node
 *
 * @author JHelp
 */
public class Node3D
{
    /**
     * Node children
     */
    private final ArrayList<Node3D> children;
    /**
     * Node position (relative to parent)
     */
    public Position3D position = new Position3D();

    /**
     * Create a new instance of Node3D
     */
    public Node3D()
    {
        this.children = new ArrayList<Node3D>();
    }

    /**
     * Destroy the node
     */
    void destroy()
    {
        synchronized (this.children)
        {
            for (final Node3D node : this.children)
            {
                node.destroy();
            }

            this.children.clear();
        }

        this.position = null;
    }

    /**
     * Called when the instance is remove by garbage collector <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws Throwable On issue
     * @see Object#finalize()
     */
    @Override
    protected final void finalize() throws Throwable
    {
        this.destroy();

        super.finalize();
    }

    /**
     * Render specific in Open GL
     *
     * @param gl Open GL link
     */
    protected void renderSpecific(final GL10 gl)
    {
    }

    /**
     * Add a child
     *
     * @param node Child to add
     */
    public final void addChild(final Node3D node)
    {
        synchronized (this.children)
        {
            this.children.add(node);
        }
    }

    /**
     * Render in Open GL
     *
     * @param gl Open GL link
     */
    public final void render(final GL10 gl)
    {
        gl.glPushMatrix();

        this.position.apply(gl);

        this.renderSpecific(gl);

        synchronized (this.children)
        {
            for (final Node3D node3d : this.children)
            {
                node3d.render(gl);
            }
        }

        gl.glPopMatrix();
    }
}