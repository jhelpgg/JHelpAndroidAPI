package jhelp.android.api.engine;

import java.util.concurrent.TimeUnit;

import jhelp.android.api.Debug;
import jhelp.android.api.R;
import jhelp.android.api.engine.geom.Cube;
import jhelp.android.api.engine.util.Alignment;
import jhelp.android.api.engine.util.Font;
import jhelp.android.api.engine.util.TextPosition;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class GameActivity
        extends Activity
        implements SensorEventListener, Runnable, OnTouchListener
{
    private Animation     animation;
    private Object3D      cube;
    private Node3D        root;
    private Sensor        sensorAccelerometer;
    private SensorManager sensorManager;
    JHelpGLSurfaceView surfaceView;

    @Override
    protected void onPause()
    {
        super.onPause();
        this.sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        this.sensorManager.registerListener(this, this.sensorAccelerometer,
                                            SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onAccuracyChanged(final Sensor sensor, final int accuracy)
    {
    }

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.surfaceView = new JHelpGLSurfaceView(this);
        this.setContentView(this.surfaceView);

        this.sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        this.sensorAccelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION/*
        | Sensor.TYPE_ACCELEROMETER */);

        this.surfaceView.setOnTouchListener(this);

        Scene3D.SCENE3D.scheduledThreadPoolExecutor.schedule(this, 1024, TimeUnit.MILLISECONDS);
    }

    public void onSensorChanged(final SensorEvent event)
    {
        if (this.root == null)
        {
            return;
        }

        this.root.position.angleX = -event.values[1];
        this.root.position.angleY = -event.values[2];
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent)
    {
        if (motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            Scene3D.SCENE3D.scheduledThreadPoolExecutor.schedule(new DoTouch(motionEvent), 1,
                                                                 TimeUnit.MILLISECONDS);
        }

        return true;
    }

    class DoTouch implements Runnable
    {
        private MotionEvent motionEvent;

        DoTouch(MotionEvent motionEvent)
        {
            this.motionEvent = motionEvent;
        }

        public void run()
        {
            GameActivity.this.doTouch(this.motionEvent);
        }
    }

    void doTouch(MotionEvent motionEvent)
    {
        if (this.animation != null)
        {
            this.animation.stop();
        }

        this.animation = new Animation(this.cube);

        final Point3D point3d = this.surfaceView.convertScreenCoordinate(motionEvent.getX(),
                                                                         motionEvent.getY(),
                                                                         this.cube.position.z);

        this.animation.addFrame(10, new Position3D(point3d.x, point3d.y, point3d.z,
                                                   this.cube.position.angleX,
                                                   this.cube.position.angleY,
                                                   this.cube.position.angleZ,
                                                   this.cube.position.scaleX,
                                                   this.cube.position.scaleY,
                                                   this.cube.position.scaleZ));

        Scene3D.SCENE3D.playAnimation(this.animation);
    }

    public void run()
    {
        this.root = Scene3D.SCENE3D.getRoot();
        this.root.position.z = -2.5f;

        this.cube = new Cube(true);
        //this.cube.doubleFace = true;
        this.root.addChild(this.cube);
        this.cube.position.scaleX = this.cube.position.scaleY = this.cube.position.scaleZ = 1.5f;
        this.cube.material.diffuseColor = Color4f.createWhiteColor();
        this.cube.material.alpha = 1f;
        this.cube.material.texture = new Texture(this.getResources(), R.drawable.tex512, true);
        Canvas canvas = this.cube.material.texture.getCanvas();
        Paint  paint  = this.cube.material.texture.getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFF0000FF);
        Font font = new Font(4, 4);
        Debug.printMark("START");
        Rect bound = new Rect();
        font.drawStringAntiAliased("Salut\nCa va ?\nåãâäà\nleftéèêëẽrigh\nñç\n°", 256, 32, Alignment.CENTER,
                                   TextPosition.TOP, canvas, paint, bound);
        Debug.println("bound=", bound);
        font.drawString("Salut\nCa va ?\nåãâäà\néèêëẽ\nñç\n°", 256, 480, Alignment.CENTER,
                        TextPosition.BOTTOM, canvas, paint, bound);
        Debug.println("bound=", bound);
        Debug.printMark("END");

        paint.setColor(0x44FF0000);
        canvas.drawRect(170, 0, 340, 128, paint);

        paint.setColor(0x4400FF00);
        canvas.drawRect(0, 128, 170, 256, paint);
        paint.setColor(0x440000FF);
        canvas.drawRect(170,128,340,256,paint);
        paint.setColor(0x44888800);
        canvas.drawRect(340,128,510,256,paint);

        paint.setColor(0x44880088);
        canvas.drawRect(170, 256, 340, 384, paint);

        paint.setColor(0x44008888);
        canvas.drawRect(170, 384, 340, 512, paint);

        this.cube.material.texture.refresh();
    }
}