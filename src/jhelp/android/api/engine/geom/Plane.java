package jhelp.android.api.engine.geom;

import java.util.concurrent.TimeUnit;

import jhelp.android.api.engine.Object3D;
import jhelp.android.api.engine.Scene3D;

/**
 * Represents a plane
 *
 * @author JHelp
 */
public class Plane
        extends Object3D
{
    /**
     * Create the plane
     */
    private final Runnable construct = new Runnable()
    {
        /**
         * Create plane in separate thread <br>
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
            final float x2 = 0.5f;
            final float y2 = 0.5f;
            final float z2 = 0f;

            final float u1 = 0;
            final float v1 = 0;
            final float u2 = 1;
            final float v2 = 1;

            Plane.this.addTriangle(x1, y1, z2, u1, v1,
                                   x2, y1, z2, u2, v1,
                                   x1, y2, z2, u1, v2);
            Plane.this.addTriangle(x1, y2, z2, u1, v2,
                                   x2, y1, z2, u2, v1,
                                   x2, y2, z2, u2, v2);

            Plane.this.compact();
        }
    };

    /**
     * Create a new instance of Plane
     */
    public Plane()
    {
        this.doubleFace = true;

        Scene3D.SCENE3D.scheduledThreadPoolExecutor.schedule(this.construct, 16,
                                                             TimeUnit.MILLISECONDS);
    }
}