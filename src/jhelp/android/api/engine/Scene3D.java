package jhelp.android.api.engine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.opengles.GL10;

import jhelp.android.api.engine.util.Debug;

/**
 * Represents a 3D scene
 * 
 * @author JHelp
 */
public class Scene3D
{
	/**
	 * Load scene
	 * 
	 * @author JHelp
	 */
	class LoadScene
			implements Runnable
	{
		/**
		 * Stream to parse
		 */
		private final InputStream	inputStream;

		/**
		 * Create a new instance of LoadScene
		 * 
		 * @param inputStream
		 *           Stream to parse
		 */
		LoadScene(final InputStream inputStream)
		{
			this.inputStream = inputStream;
		}

		/**
		 * Load the scene in separate thread <br>
		 * <br>
		 * <b>Parent documentation:</b><br>
		 * {@inheritDoc}
		 * 
		 * @see Runnable#run()
		 */
		public void run()
		{
			try
			{
				new LoaderScene(this.inputStream);
			}
			catch(final Exception exception)
			{
				Debug.printException(exception, "Loading scene failed !");
			}
			finally
			{
				try
				{
					this.inputStream.close();
				}
				catch(final Exception exception)
				{
				}
			}
		}
	}

	/** Number of thread waiting in critical section */
	private static int				inCriticalSection	= 0;
	/** Lock for synchronized */
	private static final Object	LOCK					= new Object();
	/** Scene 3D singleton */
	public static final Scene3D	SCENE3D				= new Scene3D();

	/**
	 * Enter in critical section
	 */
	static void enterCriticalSection()
	{
		synchronized(Scene3D.LOCK)
		{
			if(Scene3D.inCriticalSection > 0)
			{
				try
				{
					Scene3D.LOCK.wait(16364);
				}
				catch(final InterruptedException exception)
				{
				}
			}

			Scene3D.inCriticalSection++;
		}
	}

	/**
	 * Exit from critical section
	 */
	static void exitCriticalSection()
	{
		synchronized(Scene3D.LOCK)
		{
			Scene3D.inCriticalSection--;

			if(Scene3D.inCriticalSection > 0)
			{
				Scene3D.LOCK.notify();
			}
		}
	}

	/** Playing animation list */
	private final ArrayList<Animation>	animations;
	/** Root node 3D */
	private Node3D								node3d;
	/** Scheduler of threads */
	public ScheduledThreadPoolExecutor	scheduledThreadPoolExecutor;

	/**
	 * Create a new instance of Scene3D
	 */
	private Scene3D()
	{
		this.node3d = new Node3D();
		this.animations = new ArrayList<Animation>();
	}

	/**
	 * Destroy/empty the scene
	 */
	void destroy()
	{
		this.animations.clear();

		this.node3d.destroy();
		this.node3d = null;
	}

	/**
	 * Scene root
	 * 
	 * @return Scene root
	 */
	public Node3D getRoot()
	{
		if(this.node3d == null)
		{
			this.node3d = new Node3D();
		}

		return this.node3d;
	}

	/**
	 * Load a scene for stream
	 * 
	 * @param inputStream
	 *           Stream to read
	 */
	public void loadScene(final InputStream inputStream)
	{
		final LoadScene loadScene = new LoadScene(inputStream);

		this.scheduledThreadPoolExecutor.schedule(loadScene, 16, TimeUnit.MILLISECONDS);
	}

	/**
	 * Play an animation
	 * 
	 * @param animation
	 *           Animation to play
	 */
	public void playAnimation(final Animation animation)
	{
		Scene3D.enterCriticalSection();
		this.animations.add(animation);
		animation.start();
		Scene3D.exitCriticalSection();
	}

	/**
	 * Render the scene in Open GL
	 * 
	 * @param gl
	 *           Open GL link
	 */
	public void render(final GL10 gl)
	{
		if(this.node3d == null)
		{
			return;
		}

		for(int anim = this.animations.size() - 1; anim >= 0; anim--)
		{
			if(this.animations.get(anim).animate() == false)
			{
				this.animations.remove(anim);
			}
		}

		gl.glLoadIdentity();

		this.node3d.render(gl);
	}
}