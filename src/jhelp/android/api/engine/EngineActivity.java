package jhelp.android.api.engine;

import java.util.concurrent.TimeUnit;

import jhelp.android.api.R;
import jhelp.android.api.engine.geom.Plane;
import jhelp.android.api.engine.util.Utilities;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity example of use 3D engine
 *
 * @author JHelp
 */
public class EngineActivity
        extends Activity
        implements Runnable
{
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final JHelpGLSurfaceView surfaceView = new JHelpGLSurfaceView(this);

        this.setContentView(surfaceView);

        Scene3D.SCENE3D.scheduledThreadPoolExecutor.schedule(this, 1024, TimeUnit.MILLISECONDS);

    }

    /**
     * Create test scene in separate thread <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see Runnable#run()
     */
    public void run()
    {
        final Node3D root = Scene3D.SCENE3D.getRoot();
        root.position.angleX = 1;
        root.position.angleY = 2;
        root.position.angleZ = 4;
        root.position.z = -1.5f;

        final Plane planeInifinite = new Plane();
        root.addChild(planeInifinite);
        planeInifinite.position.angleX = 90;
        planeInifinite.material.alpha = 0.5f;

        Utilities.sleep(1024);

        final Clone3D planeProjective = new Clone3D(planeInifinite);
        root.addChild(planeProjective);
        planeProjective.position.angleX = 90;
        planeProjective.position.y = 0.25f;

        planeProjective.material.diffuseColor = Color4f.createWhiteColor();
        planeProjective.material.alpha = 0.8f;
        planeProjective.material.texture = new Texture(this.getResources(), R.drawable.tex512);

        Utilities.sleep(1024);

        final Clone3D planeConstruct1 = new Clone3D(planeInifinite);
        root.addChild(planeConstruct1);
        planeConstruct1.position.angleX = 90;
        planeConstruct1.position.angleY = 45;
        planeConstruct1.material.diffuseColor.red = 1;
        planeConstruct1.material.alpha = 0.25f;

        Utilities.sleep(1024);

        final Clone3D planeConstruct2 = new Clone3D(planeInifinite);
        root.addChild(planeConstruct2);
        planeConstruct2.position.angleX = 90;
        planeConstruct2.position.angleY = -45;
        planeConstruct2.material = planeConstruct1.material;

        Utilities.sleep(1024);

        final Animation  animation = new Animation(planeProjective);
        final Position3D position  = new Position3D();
        position.angleX = 180;
        position.z = 0.25f;
        position.y = 0;
        animation.addFrame(100, position);

        final Position3D position3d = planeProjective.position.copy();

        animation.addFrame(500, position3d);

        for (int i = 1000; i < 10000; )
        {
            animation.addFrame(i, position);
            i += 400;
            animation.addFrame(i, position);
            i += 100;

            animation.addFrame(i, position3d);
            i += 400;
            animation.addFrame(i, position3d);
            i += 100;
        }

        Utilities.sleep(1024);

        Scene3D.SCENE3D.playAnimation(animation);
    }
}