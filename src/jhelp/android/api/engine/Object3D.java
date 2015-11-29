package jhelp.android.api.engine;

import android.opengl.GLES10;
import android.opengl.GLES20;
import android.opengl.GLES30;

import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import jhelp.android.api.engine.util.ArrayInt;
import jhelp.android.api.engine.util.Debug;
import jhelp.android.api.engine.util.UtilBuffer;
import jhelp.android.api.engine.util.Utilities;

/**
 * Represents a 3D object
 *
 * @author JHelp
 */
public class Object3D
        extends Node3D
        implements Runnable
{
    /**
     * Indicates if object can change. That is to say, if can add triangle with
     * {@link #addTriangle(float, float, float, float, float, float, float, float, float, float,
     * float, float, float, float, float)}
     */
    private boolean     canChange;
    /**
     * Indicates if object is on construction
     */
    private boolean     onConstruction;
    /**
     * Object points
     */
    private FloatBuffer points;
    /**
     * Object UVs
     */
    private FloatBuffer uvs;
    /**
     * Points indexes
     */
    ArrayInt indexPoint = new ArrayInt();
    /**
     * UV indexes
     */
    ArrayInt indexUV    = new ArrayInt();
    /**
     * Number of triangles
     */
    int numberOfTriangles;
    /**
     * Indicates if object is double face or not
     */
    public boolean  doubleFace = false;
    /**
     * Object material
     */
    public Material material   = new Material();

    /**
     * Create a new instance of Object3D
     */
    public Object3D()
    {
        this.onConstruction = false;
        this.canChange = true;
    }

    /**
     * Refresh the object
     */
    private void internalRefresh()
    {
        Utilities.sleep(8);

        Scene3D.enterCriticalSection();

        this.points = null;
        this.uvs = null;

        this.onConstruction = false;

        Scene3D.exitCriticalSection();
    }

    /**
     * Render the object in Open GL <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param gl Open GL link
     * @see jhelp.android.api.engine.Node3D#renderSpecific(GL10)
     */
    @Override
    protected final void renderSpecific(final GL10 gl)
    {
        if (this.points == null)
        {
            if ((this.indexPoint == null) || (this.indexPoint.getSize() == 0) || (this.onConstruction == true))
            {
                return;
            }
            this.onConstruction = true;

            Scene3D.SCENE3D.scheduledThreadPoolExecutor.schedule(this, 16, TimeUnit.MILLISECONDS);

            return;
        }

        if (this.doubleFace == true)
        {
            gl.glDisable(GL10.GL_CULL_FACE);
        }
        else
        {
            gl.glEnable(GL10.GL_CULL_FACE);
        }

        this.material.render(gl);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, this.points);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.uvs);

        for (int i = 0, offset = 0; i < this.numberOfTriangles; i++, offset += 3)
        {
            gl.glDrawArrays(GL10.GL_TRIANGLES, offset, 3);
        }
    }

    /**
     * Add a triangle to the object
     *
     * @param x1 First point X
     * @param y1 First point Y
     * @param z1 First point Z
     * @param u1 First point U
     * @param v1 First point V
     * @param x2 Second point X
     * @param y2 Second point Y
     * @param z2 Second point Z
     * @param u2 Second point U
     * @param v2 Second point V
     * @param x3 Third point X
     * @param y3 Third point Y
     * @param z3 Third point Z
     * @param u3 Third point U
     * @param v3 Third point V
     */
    public final void addTriangle(final float x1, final float y1, final float z1,//
                                  final float u1, final float v1,//
                                  final float x2, final float y2, final float z2,//
                                  final float u2, final float v2,//
                                  final float x3, final float y3, final float z3,//
                                  final float u3, final float v3)
    {
        if (this.canChange == false)
        {
            return;
        }

        Scene3D.enterCriticalSection();

        this.points = null;
        this.uvs = null;

        this.indexPoint.add(PoolPoints.storePoint3D(x1, y1, z1));
        this.indexUV.add(PoolPoints.storePoint2D(u1, v1));

        this.indexPoint.add(PoolPoints.storePoint3D(x2, y2, z2));
        this.indexUV.add(PoolPoints.storePoint2D(u2, v2));

        this.indexPoint.add(PoolPoints.storePoint3D(x3, y3, z3));
        this.indexUV.add(PoolPoints.storePoint2D(u3, v3));

        this.numberOfTriangles++;

        Scene3D.exitCriticalSection();
    }

    /**
     * Compact the object to take less memory, but after done this operation, it is impossible to
     * add more triangle, so add all need triangle before call it
     */
    public final void compact()
    {
        if (this.canChange == false)
        {
            return;
        }

        this.canChange = false;

        Debug.printVerbose("COMPACT");

        this.internalRefresh();
    }

    /**
     * Force the object to refresh
     */
    public final void refresh()
    {
        if (this.canChange == false)
        {
            return;
        }

        this.internalRefresh();
    }

    /**
     * Build object in separate thread <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see Runnable#run()
     */
    public void run()
    {
        Scene3D.enterCriticalSection();

        Point3D point3d;
        Point2D point2d;

        final float[] coordPoints = new float[this.numberOfTriangles * 3 * 3];
        final float[] coordUV     = new float[this.numberOfTriangles * 2 * 3];

        int indexPoint = 0;
        int indexUV    = 0;
        int index      = 0;

        for (int i = 0; i < this.numberOfTriangles; i++)
        {
            for (int p = 0; p < 3; p++)
            {
                point3d = PoolPoints.obtainPoint3D(this.indexPoint.getInteger(index));
                coordPoints[indexPoint++] = point3d.x;
                coordPoints[indexPoint++] = point3d.y;
                coordPoints[indexPoint++] = point3d.z;

                point2d = PoolPoints.obtainPoint2D(this.indexUV.getInteger(index));
                coordUV[indexUV++] = point2d.x;
                coordUV[indexUV++] = point2d.y;

                index++;
            }
        }

        this.points = UtilBuffer.createFloatBuffer(coordPoints);
        this.uvs = UtilBuffer.createFloatBuffer(coordUV);

        this.onConstruction = false;

        Scene3D.exitCriticalSection();

        if (this.canChange == false)
        {
            Debug.printVerbose("DESTROY");

            this.indexPoint.destroy();
            this.indexPoint = null;

            this.indexUV.destroy();
            this.indexUV = null;

            System.gc();
        }
    }
}