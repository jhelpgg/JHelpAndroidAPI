package jhelp.android.api;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * A generic state machine.<br>
 * Its run in its own thread.<br>
 * Don't forget to call the method {@link #destroy()} to terminate the thread properly
 * 
 * @author JHelp
 * @param <INFORMATION>
 *           Information type
 */
public abstract class StateMachine<INFORMATION>
{
   /**
    * Embed handler for manage messages
    * 
    * @author JHelp
    * @param <INFO>
    *           Information type
    */
   private static class EmbededHandler<INFO>
         extends Handler
   {
      /** State machine parent */
      private final StateMachine<INFO> stateMachine;

      /**
       * Create a new instance of EmbededHandler
       * 
       * @param stateMachine
       *           State machine parent
       */
      EmbededHandler(final StateMachine<INFO> stateMachine)
      {
         this.stateMachine = stateMachine;
      }

      /**
       * Called when a message is delivered <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param message
       *           The arrived message
       * @see android.os.Handler#handleMessage(android.os.Message)
       */
      @SuppressWarnings("unchecked")
      @Override
      public void handleMessage(final Message message)
      {
         if(this.stateMachine.stateChanged(this.stateMachine.currentState, message.what, (INFO) message.obj) == true)
         {
            this.stateMachine.currentState = message.what;
         }
      }
   }

   /**
    * Internal state machine thread
    * 
    * @author JHelp
    */
   class InternalThread
         implements Runnable
   {
      /** Looper linked to the thread and the message handler */
      private Looper looper;

      /**
       * Destroy properly the thread
       */
      public void destroy()
      {
         if(this.looper != null)
         {
            this.looper.quit();
         }

         this.looper = null;
      }

      /**
       * Do the thread action <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @see java.lang.Runnable#run()
       */
      @Override
      public void run()
      {
         Looper.prepare();

         this.looper = Looper.myLooper();

         StateMachine.this.embededHandler = new EmbededHandler<INFORMATION>(StateMachine.this);

         Looper.loop();

         StateMachine.this.embededHandler = null;
      }
   }

   /** State machine internal thread */
   private final InternalThread internalThread;
   /** Current state of the machine */
   int                          currentState;
   /** Embed handler */
   EmbededHandler<INFORMATION>  embededHandler;

   /**
    * Create a new instance of StateMachine.<br>
    * The internal thread is started automatically
    */
   public StateMachine()
   {
      this.internalThread = new InternalThread();

      (new Thread(this.internalThread)).start();

      Utilities.sleep(1024);
   }

   /**
    * Called when garbage collector want to free memory.<br>
    * It an attempt to stop the embed thread if developer forget call {@link #destroy()} <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @throws Throwable
    *            On issue
    * @see java.lang.Object#finalize()
    */
   @Override
   protected void finalize() throws Throwable
   {
      this.destroy();

      super.finalize();
   }

   /**
    * Cancel a state before it is sending.<br>
    * Work with {@link #changeStateDelayed(int, Object, int)}.<br>
    * It can be used for example to manage a timeout.
    * 
    * @param state
    *           State ordered to cancel
    */
   public final void cancelChangeState(final int state)
   {
      this.embededHandler.removeMessages(state);
   }

   /**
    * Change "immediately" the current state
    * 
    * @param state
    *           New state
    * @param information
    *           Information to give to the state (Can be {@code null})
    */
   public final void changeState(final int state, final INFORMATION information)
   {
      this.embededHandler.sendMessage(this.embededHandler.obtainMessage(state, information));
   }

   /**
    * Change the current state in a given millisecond delay.<br>
    * The changing can be canceled by {@link #cancelChangeState(int)}
    * 
    * @param state
    *           New state
    * @param information
    *           Information to give to the state (Can be {@code null})
    * @param millisecondDelay
    *           Delay before change the state
    */
   public final void changeStateDelayed(final int state, final INFORMATION information, final int millisecondDelay)
   {
      this.embededHandler.sendMessageDelayed(this.embededHandler.obtainMessage(state, information), millisecondDelay);
   }

   /**
    * Stop the internal thread and destroy properly the state machine.<br>
    * Call it as soon as you need no more the state machine or on the onDestroy of your Activity, for example
    */
   public final void destroy()
   {
      this.internalThread.destroy();

      this.embededHandler = null;
   }

   /**
    * Current state
    * 
    * @return Current state
    */
   public final int getCurrentState()
   {
      return this.currentState;
   }

   /**
    * Called when a state is about to changed.<br>
    * If it is possible to go on the desired state this method return {@code true}, if the changing is not allowed, its return
    * {@code false}<br>
    * Its also there is done the action corresponds to a state
    * 
    * @param currentState
    *           Current state
    * @param futureState
    *           Desired state
    * @param information
    *           Future state information
    * @return {@code true} if the state changing is allowed
    */
   public abstract boolean stateChanged(int currentState, int futureState, INFORMATION information);
}