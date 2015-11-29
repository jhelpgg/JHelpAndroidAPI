package jhelp.android.api.engine;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import jhelp.android.api.engine.util.Debug;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * View for show the 3D
 *
 * @author JHelp
 */
public class JHelpGLSurfaceView
        extends GLSurfaceView
{
    /**
     * 3D scene renderer
     *
     * @author JHelp
     */
    class JHelpSceneRenderer
            implements Renderer
    {
        /**
         * Called at each scene refresh <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param gl Link with Open GL
         * @see Renderer#onDrawFrame(GL10)
         */
        public void onDrawFrame(final GL10 gl)
        {
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

            gl.glMatrixMode(GL10.GL_MODELVIEW);

            Scene3D.SCENE3D.render(gl);

            Scene3D.exitCriticalSection();
        }

        /**
         * Called each time the surface changes <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param gl     Link with Open GL
         * @param width  New width
         * @param height New height
         * @see Renderer#onSurfaceChanged(GL10, int, int)
         */
        public void onSurfaceChanged(final GL10 gl, final int width, final int height)
        {
            gl.glViewport(0, 0, width, height);

         /*
          * Set our projection matrix. This doesn't have to be done each time we draw, but
          * usually a new projection needs to be set when the viewport is resized.
          */

            final float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

            JHelpGLSurfaceView.this.boundsReal.left = 0;
            JHelpGLSurfaceView.this.boundsReal.top = 0;
            JHelpGLSurfaceView.this.boundsReal.right = width;
            JHelpGLSurfaceView.this.boundsReal.bottom = height;

            JHelpGLSurfaceView.this.bounds3D.left = -ratio;
            JHelpGLSurfaceView.this.bounds3D.right = ratio;
            JHelpGLSurfaceView.this.bounds3D.top = 1;
            JHelpGLSurfaceView.this.bounds3D.bottom = -1;
        }

        /**
         * Called when view first created <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param gl     Link with Open GL
         * @param config OpenGL configuration
         * @see Renderer#onSurfaceCreated(GL10,
         * EGLConfig)
         */
        public void onSurfaceCreated(final GL10 gl, final EGLConfig config)
        {
         /*
          * By default, OpenGL enables features that improve quality but reduce performance. One
          * might want to tweak that
          * especially on software renderer.
          */
            gl.glDisable(GL10.GL_DITHER);

         /*
          * Some one-time OpenGL initialization can be made here probably based on features of
          * this particular context
          */
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

            gl.glClearColor(1, 1, 1, 1);
            //   gl.glShadeModel(GL10.GL_SMOOTH);

            gl.glEnable(GL10.GL_DEPTH_TEST);
            //gl.glDepthFunc(GL10.GL_);

            gl.glEnable(GL10.GL_ALPHA_TEST);
            // Set alpha precision
            gl.glAlphaFunc(GL10.GL_GREATER, 0.01f);
            // Way to compute alpha
            gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

            // We accept blending
            gl.glEnable(GL10.GL_BLEND);
            gl.glEnable(GL10.GL_COLOR_MATERIAL);

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
    }

    /**
     * Refresh the scene
     */
    private final Runnable refreshScene = new Runnable()
    {
        /**
         * Refresh the scene in separate scene <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see Runnable#run()
         */
        public void run()
        {
            Scene3D.enterCriticalSection();

            JHelpGLSurfaceView.this.requestRender();
        }
    };
    /**
     * Scene renderer
     */
    private JHelpSceneRenderer sceneRenderer;
    /**
     * Thread refresh link to cancel it when exit application
     */
    private ScheduledFuture<?> scheduledFuture;
    /**
     * Last touch X position
     */
    private float              touchX;
    /**
     * Last touch Y position
     */
    private float              touchY;
    RectF bounds3D   = new RectF();
    RectF boundsReal = new RectF();

    /**
     * Create a new instance of JHelpGLSurfaceView
     *
     * @param context Android context
     */
    public JHelpGLSurfaceView(final Context context)
    {
        super(context);

        this.initialize();
    }

    /**
     * Create a new instance of JHelpGLSurfaceView
     *
     * @param context Android context
     * @param attrs   Attrributes use
     */
    public JHelpGLSurfaceView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);

        this.initialize();
    }

    /**
     * Initialize the view
     */
    private void initialize()
    {
        this.sceneRenderer = new JHelpSceneRenderer();

        this.setRenderer(this.sceneRenderer);

        Scene3D.SCENE3D.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(32);

        // Render the view only when there is a change
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        this.scheduledFuture = Scene3D.SCENE3D.scheduledThreadPoolExecutor.scheduleWithFixedDelay(
                this.refreshScene, 1024, 64,
                TimeUnit.MILLISECONDS);
    }

    /**
     * Called when detached for window, here when application exit <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see GLSurfaceView#onDetachedFromWindow()
     */
    @Override
    protected void onDetachedFromWindow()
    {
        if (this.scheduledFuture != null)
        {
            this.scheduledFuture.cancel(false);

            try
            {
                Scene3D.SCENE3D.scheduledThreadPoolExecutor.awaitTermination(1024,
                                                                             TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException exception)
            {
                // {@todo} TODO Check if print exception is enough
                Debug.printTodo("Check if print exception is enough");
                Debug.printException(exception);
            }

            Scene3D.SCENE3D.scheduledThreadPoolExecutor.shutdown();

            try
            {
                Scene3D.SCENE3D.scheduledThreadPoolExecutor.awaitTermination(1024,
                                                                             TimeUnit.MILLISECONDS);
            }
            catch (final InterruptedException exception)
            {
                Debug.printException(exception);
            }
        }

        this.scheduledFuture = null;

        Scene3D.SCENE3D.destroy();

        System.gc();

        super.onDetachedFromWindow();
    }

    public Point3D convertScreenCoordinate(final float x, final float y, final float z)
    {
        final float w3 = this.bounds3D.right - this.bounds3D.left;
        final float h3 = this.bounds3D.bottom - this.bounds3D.top;

        final float wR = this.boundsReal.right - this.boundsReal.left;
        final float hR = this.boundsReal.bottom - this.boundsReal.top;

        return new Point3D(this.bounds3D.left + ((w3 * x) / wR),
                           this.bounds3D.top + ((h3 * y) / hR),
                           z);
    }

    public RectF getBounds3D()
    {
        return new RectF(this.bounds3D);
    }

    public RectF getBoundsReal()
    {
        return new RectF(this.boundsReal);
    }

    /**
     * Call each time the screen is touched <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param event Event description
     * @return {@code true} to be touch again
     * @see android.view.View#onTouchEvent(MotionEvent)
     */
    @Override
    public boolean onTouchEvent(final MotionEvent event)
    {
        final float x = event.getX();
        final float y = event.getY();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_MOVE:
                Scene3D.SCENE3D.getRoot().position.angleY += (x - this.touchX);
                Scene3D.SCENE3D.getRoot().position.angleX += (y - this.touchY);
                break;
        }

        this.touchX = x;
        this.touchY = y;

        return true;
    }
}