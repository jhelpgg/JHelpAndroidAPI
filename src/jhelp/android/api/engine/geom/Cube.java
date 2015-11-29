package jhelp.android.api.engine.geom;

import java.util.concurrent.TimeUnit;

import jhelp.android.api.engine.Object3D;
import jhelp.android.api.engine.Scene3D;
import jhelp.android.api.engine.util.Debug;

/**
 * A cube
 *
 * @author JHelp
 */
public class Cube
        extends Object3D
{
    /**
     * Create the cube
     */
    private final Runnable construct      = new Runnable()
    {
        /**
         * Create the cube in separate thread <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see Runnable#run()
         */
        public void run()
        {
            final float x1 = -0.5f;
            final float y1 = -0.5f;
            final float z1 = -0.5f;
            final float x2 = 0.5f;
            final float y2 = 0.5f;
            final float z2 = 0.5f;

            final float u1 = 0;
            final float v1 = 0;
            final float u2 = 1;
            final float v2 = 1;

            // FACE
            Cube.this.addTriangle(x1, y1, z2, u1, v2,
                                  x2, y1, z2, u2, v2,
                                  x1, y2, z2, u1, v1);
            Cube.this.addTriangle(x1, y2, z2, u1, v1,
                                  x2, y1, z2, u2, v2,
                                  x2, y2, z2, u2, v1);

            // BACK
            Cube.this.addTriangle(x1, y1, z1, u1, v1,
                                  x1, y2, z1, u1, v2,
                                  x2, y1, z1, u2, v1);
            Cube.this.addTriangle(x1, y2, z1, u1, v2,
                                  x2, y2, z1, u2, v2,
                                  x2, y1, z1, u2, v1);

            // BOTTOM
            Cube.this.addTriangle(x1, y1, z1, u1, v2,
                                  x2, y1, z1, u2, v2,
                                  x1, y1, z2, u1, v1);
            Cube.this.addTriangle(x2, y1, z1, u2, v2,
                                  x2, y1, z2, u2, v1,
                                  x1, y1, z2, u1, v1);

            // TOP
            Cube.this.addTriangle(x1, y2, z1, u1, v1,
                                  x1, y2, z2, u1, v2,
                                  x2, y2, z1, u2, v1);
            Cube.this.addTriangle(x2, y2, z2, u2, v2,
                                  x2, y2, z1, u2, v1,
                                  x1, y2, z2, u1, v2);

            // LEFT
            Cube.this.addTriangle(x1, y1, z1, u1, v2,
                                  x1, y1, z2, u2, v2,
                                  x1, y2, z1, u1, v1);
            Cube.this.addTriangle(x1, y1, z2, u2, v2,
                                  x1, y2, z2, u2, v1,
                                  x1, y2, z1, u1, v1);

            // RIGHT
            Cube.this.addTriangle(x2, y1, z1, u2, v2,
                                  x2, y2, z1, u2, v1,
                                  x2, y1, z2, u1, v2);
            Cube.this.addTriangle(x2, y1, z2, u1, v2,
                                  x2, y2, z1, u2, v1,
                                  x2, y2, z2, u1, v1);

            Cube.this.compact();
        }
    };
    /**
     * Create the cube
     */
    private final Runnable constructCross = new Runnable()
    {
        /**
         * Create the cube in separate thread <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see Runnable#run()
         */
        public void run()
        {
            final float x1 = -0.5f;
            final float y1 = -0.5f;
            final float z1 = -0.5f;
            final float x2 = 0.5f;
            final float y2 = 0.5f;
            final float z2 = 0.5f;

            final float u1 = 0;
            final float u2 = 1f/3f;
            final float u3 = 2f/3f;
            final float u4 = 1;
            final float v1 = 0;
            final float v2 = 0.25f;
            final float v3 = 0.5f;
            final float v4 = 0.75f;
            final float v5 = 1;

            // FACE
            Cube.this.addTriangle(x1, y1, z2, u2, v3,
                                  x2, y1, z2, u3, v3,
                                  x1, y2, z2, u2, v2);
            Cube.this.addTriangle(x1, y2, z2, u2, v2,
                                  x2, y1, z2, u3, v3,
                                  x2, y2, z2, u3, v2);

            // BACK
            Cube.this.addTriangle(x1, y1, z1, u2, v4,
                                  x1, y2, z1, u2, v5,
                                  x2, y1, z1, u3, v4);
            Cube.this.addTriangle(x1, y2, z1, u2, v5,
                                  x2, y2, z1, u3, v5,
                                  x2, y1, z1, u3, v4);

            // BOTTOM
            Cube.this.addTriangle(x1, y1, z1, u2, v4,
                                  x2, y1, z1, u3, v4,
                                  x1, y1, z2, u2, v3);
            Cube.this.addTriangle(x2, y1, z1, u3, v4,
                                  x2, y1, z2, u3, v3,
                                  x1, y1, z2, u2, v3);

            // TOP
            Cube.this.addTriangle(x1, y2, z1, u2, v1,
                                  x1, y2, z2, u2, v2,
                                  x2, y2, z1, u3, v1);
            Cube.this.addTriangle(x2, y2, z2, u3, v2,
                                  x2, y2, z1, u3, v1,
                                  x1, y2, z2, u2, v2);

            // LEFT
            Cube.this.addTriangle(x1, y1, z1, u1, v3,
                                  x1, y1, z2, u2, v3,
                                  x1, y2, z1, u1, v2);
            Cube.this.addTriangle(x1, y1, z2, u2, v3,
                                  x1, y2, z2, u2, v2,
                                  x1, y2, z1, u1, v2);

            // RIGHT
            Cube.this.addTriangle(x2, y1, z1, u4, v3,
                                  x2, y2, z1, u4, v2,
                                  x2, y1, z2, u3, v3);
            Cube.this.addTriangle(x2, y1, z2, u3, v3,
                                  x2, y2, z1, u4, v2,
                                  x2, y2, z2, u3, v2);

            Cube.this.compact();
        }
    };

    /**
     * Create a new instance of Cube
     */
    public Cube()
    {
        this(false);
    }

    public Cube(boolean cross)
    {
        if (cross == true)
        {
            Scene3D.SCENE3D.scheduledThreadPoolExecutor.schedule(this.constructCross, 16,
                                                                 TimeUnit.MILLISECONDS);
        }
        else
        {
            Scene3D.SCENE3D.scheduledThreadPoolExecutor.schedule(this.construct, 16,
                                                                 TimeUnit.MILLISECONDS);
        }
    }
}