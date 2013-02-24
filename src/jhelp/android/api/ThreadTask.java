package jhelp.android.api;

/**
 * Task executed in separate thread, that can be paused.<br>
 * For pause system work, find the "loop" inside the operation of the task to do, and only put the inside loop.For example fir
 * task is something like : <code lang="java>
 * for(int i=0;i<10000;i++)
 *    doSomeThing(i);
 * </code> then the usage can be : <code lang="java">
 * public class MyTask extends ThreadTask<Void>
 * {
 *    private int i;
 *    public MyTask()
 *    {
 *       i=0;
 *    }
 *    protected void doALoopOfTask(Void parameter)
 *    {
 *       doSomeThing(i);
 *    
 *       i++;
 *    
 *       if(i>=10000) 
 *       {
 *          terminateTask();
 *       }
 *    }
 *    protected void finshTask(Void parameter)
 *    {
 *       //Nothing to do
 *    }
 * }
 * </code><br>
 * To start a task, call {@link #start()}, to terminate properly call {@link #terminateTask()}
 * 
 * @author JHelp
 * @param <PARAMETER>
 *           Parameter type
 */
public abstract class ThreadTask<PARAMETER>
      extends Thread
{
   /** Indicates if task is alive */
   private boolean      alive;
   /** Synchronization lock */
   private final Object lock = new Object();
   /** Task parameter */
   private PARAMETER    parameter;
   /** Indicates if task is on pause */
   private boolean      pause;
   /** Indicates if task is started */
   private boolean      started;

   /**
    * Create a new instance of ThreadTask
    */
   public ThreadTask()
   {
      this(null);
   }

   /**
    * Create a new instance of ThreadTask
    * 
    * @param parameter
    *           Task parameter
    */
   public ThreadTask(final PARAMETER parameter)
   {
      this.parameter = parameter;
      this.alive = true;
      this.pause = false;
      this.started = false;
   }

   /**
    * Call at each loop of the task.<br>
    * A pause can only append on a start of a loop (not in the middle of one)
    * 
    * @param parameter
    *           Task parameter
    */
   protected abstract void doALoopOfTask(PARAMETER parameter);

   /**
    * Called when task if finish, that is to say when {@link #terminateTask()} was previouly called
    * 
    * @param parameter
    *           Task parameter
    */
   protected abstract void finshTask(PARAMETER parameter);

   /**
    * Indicates if task is alive
    * 
    * @return {@codetrue} if task alive
    */
   public final boolean isTaskAlive()
   {
      return this.alive;
   }

   /**
    * Indicates if task is in pause
    * 
    * @return {@code true} if task is in pause
    */
   public final boolean isTaskPaused()
   {
      return this.pause;
   }

   /**
    * Pause the task at the next start of loop
    */
   public final void pauseTask()
   {
      synchronized(this.lock)
      {
         this.pause = true;
      }
   }

   /**
    * Resume the task where it was paused.<br>
    * If also start the task if it was not previously started
    */
   public final void resumeTask()
   {
      if(this.started == false)
      {
         this.start();
      }

      synchronized(this.lock)
      {
         if(this.pause == false)
         {
            return;
         }

         this.pause = false;

         this.lock.notify();
      }
   }

   /**
    * Do the thread stufs.<br>
    * NEVER call directly this method <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see java.lang.Thread#run()
    */
   @Override
   public final void run()
   {
      while(this.alive == true)
      {
         this.doALoopOfTask(this.parameter);

         synchronized(this.lock)
         {
            if(this.pause == true)
            {
               try
               {
                  this.lock.wait();
               }
               catch(final Exception exception)
               {
               }
            }
         }
      }

      this.finshTask(this.parameter);
   }

   /**
    * Star the task <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see java.lang.Thread#start()
    */
   @Override
   public synchronized void start()
   {
      if(this.started == true)
      {
         return;
      }

      this.started = true;

      super.start();
   }

   /**
    * Terminate the task
    */
   public final void terminateTask()
   {
      this.alive = false;

      synchronized(this.lock)
      {
         if(this.pause == true)
         {
            this.pause = false;

            this.lock.notify();
         }
      }
   }
}