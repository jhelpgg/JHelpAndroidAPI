package jhelp.android.api.text2speech;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jhelp.android.api.Debug;
import jhelp.android.api.Mutex;
import jhelp.android.api.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech.EngineInfo;

/**
 * Speecher for exchange with text to speech.<br>
 * To avoid leak or dead object issue, please call {@link #destroy()} beofre exiting your application or whn you don't want more
 * deal with text to speech
 * 
 * @author JHelp
 */
public class Speecher
      implements ServiceConnection
{
   /**
    * Registered order
    * 
    * @author JHelp
    */
   static class Order
   {
      /** Order type */
      public int    order;
      /** Order parameter */
      public Object parameter;

      /**
       * Create a new instance of Order
       * 
       * @param order
       *           Order type
       */
      public Order(final int order)
      {
         this.order = order;
      }

      /**
       * Create a new instance of Order
       * 
       * @param order
       *           Order type
       * @param parameter
       *           Order parameter
       */
      public Order(final int order, final Object parameter)
      {
         this.order = order;
         this.parameter = parameter;
      }
   }

   /** Map of speecher by context */
   private static final HashMap<Context, Speecher> SPEECHERS = new HashMap<Context, Speecher>();

   /**
    * Obtain a speecher for a context.<br>
    * For first access from a context, its recommend to use {@link Speecher#obtainSpeecher(Context, SpeecherListener)} if you
    * want have speecher status information
    * 
    * @param context
    *           Context to use
    * @return Speecher instance
    */
   public static Speecher obtainSpeecher(final Context context)
   {
      return Speecher.obtainSpeecher(context, null);
   }

   /**
    * Obtain a speecher for a context.<brAt first usage for a context, the listener is register, then it is totally ignore
    * 
    * @param context
    *           Context to use
    * @param speecherListener
    *           Listener to use. Can be {@code null} and used only at the first calling of this method for a given context
    * @return Speecher instance
    */
   public static Speecher obtainSpeecher(Context context, final SpeecherListener speecherListener)
   {
      context = context.getApplicationContext();

      Speecher textToSpeechManager = Speecher.SPEECHERS.get(context);

      if(textToSpeechManager == null)
      {
         textToSpeechManager = new Speecher(context, speecherListener);
         Speecher.SPEECHERS.put(context, textToSpeechManager);
      }

      return textToSpeechManager;
   }

   /** Context link to the speecher */
   private final Context               context;
   /** Mutex for synchronization */
   private final Mutex                 mutex;
   /** Waiting orders (Cummulate during service connection) */
   private final ArrayList<Order>      orders;
   /** Listener to alert speecher states */
   private SpeecherListener            speecherListener;
   /** Link to text to speech service */
   private ITextToSpeechManagerService textToSpeechManagerService;

   /**
    * Create a new instance of Speecher
    * 
    * @param context
    *           Context link with
    * @param speecherListener
    *           Listener to alert
    */
   private Speecher(final Context context, final SpeecherListener speecherListener)
   {
      this.context = context;
      this.speecherListener = speecherListener;

      this.mutex = new Mutex();
      this.orders = new ArrayList<Speecher.Order>();

      final Intent intent = new Intent(context, TextToSpeechManagerService.class);
      context.bindService(intent, this, Context.BIND_AUTO_CREATE);
   }

   /**
    * Clear the text queue
    * 
    * @throws RemoteException
    *            On broken link
    */
   public void clear() throws RemoteException
   {
      this.mutex.lock();
      try
      {
         if(this.textToSpeechManagerService == null)
         {
            this.orders.add(new Order(R.id.SpeecherOrderClear));
         }
         else
         {
            this.textToSpeechManagerService.clear();
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * destroy properly the speecher.<br>
    * Don't forget to call it
    */
   public void destroy()
   {
      if(this.textToSpeechManagerService != null)
      {
         Debug.println("BEFORE unbind");
         this.context.unbindService(this);
         Debug.println("AFTER unbind");
         this.textToSpeechManagerService = null;
         Speecher.SPEECHERS.remove(this.context);
      }

      if(this.speecherListener != null)
      {
         this.speecherListener.speecherDestroyed();
         this.speecherListener = null;
      }
   }

   /**
    * Available engine list.<br>
    * If the link to service not already established, {@code null} is return
    * 
    * @return Available engine list.<br>
    *         If the link to service not already established, {@code null} is return
    * @throws RemoteException
    *            On broken link
    */
   public List<EngineInfo> engineList() throws RemoteException
   {
      if(this.textToSpeechManagerService == null)
      {
         return null;
      }

      final ArrayList<EngineInfo> engineInfos = new ArrayList<EngineInfo>();

      for(final SpeecherInfo speecherInfo : this.textToSpeechManagerService.speecherList())
      {
         engineInfos.add(speecherInfo.getEngineInfo());
      }

      return Collections.unmodifiableList(engineInfos);
   }

   /**
    * Current laungage<br>
    * If the link to service not already established, {@code null} is return
    * 
    * @return Current laungage<br>
    *         If the link to service not already established, {@code null} is return
    * @throws RemoteException
    *            On broken link
    */
   public Locale getLocale() throws RemoteException
   {
      if(this.textToSpeechManagerService == null)
      {
         return null;
      }

      return this.textToSpeechManagerService.getLanguage().getLocale();
   }

   /**
    * Indicates if a language is suported<br>
    * If the link to service not already established, {@code false} is return
    * 
    * @param locale
    *           Locale tested
    * @return {@code true} if language supported, {@code false} if not supported or service is not ready
    * @throws RemoteException
    *            On broken link
    */
   public boolean isLocaleSuported(final Locale locale) throws RemoteException
   {
      if(this.textToSpeechManagerService == null)
      {
         return false;
      }

      return this.textToSpeechManagerService.isLanguageSuported(new Language(locale));
   }

   /**
    * Indicates if service is ready
    * 
    * @return {@code true} if link to service is ready
    */
   public boolean isReady()
   {
      return this.textToSpeechManagerService != null;
   }

   /**
    * Called when service text to speech connection establish <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param name
    *           Connected component name
    * @param service
    *           Link to the service
    * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)
    */
   @Override
   public void onServiceConnected(final ComponentName name, final IBinder service)
   {
      this.mutex.lock();

      try
      {
         this.textToSpeechManagerService = ITextToSpeechManagerService.Stub.asInterface(service);

         int todo;
         for(final Order order : this.orders)
         {
            todo = order.order;

            try
            {
               if(todo == R.id.SpeecherOrderClear)
               {
                  this.textToSpeechManagerService.clear();
               }
               else if(todo == R.id.SpeecherOrderSetEngine)
               {
                  this.textToSpeechManagerService.setSpeecher(new SpeecherInfo((EngineInfo) order.parameter));
               }
               else if(todo == R.id.SpeecherOrderSetLocale)
               {
                  this.textToSpeechManagerService.setLanguage(new Language((Locale) order.parameter));
               }
               else if(todo == R.id.SpeecherOrderSpeakBanal)
               {
                  this.textToSpeechManagerService.speakBanal((String) order.parameter);
               }
               else if(todo == R.id.SpeecherOrderSpeakNormal)
               {
                  this.textToSpeechManagerService.speak((String) order.parameter);
               }
               else if(todo == R.id.SpeecherOrderSpeakUrgent)
               {
                  this.textToSpeechManagerService.speakUrgent((String) order.parameter);
               }
            }
            catch(final Exception exception)
            {
               Debug.printException(exception);
            }
         }

         this.orders.clear();
      }
      finally
      {
         this.mutex.unlock();
      }

      if(this.speecherListener != null)
      {
         this.speecherListener.speecherReady();
      }
   }

   /**
    * Called if connection to text to sêech is lost <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param name
    *           Component name that lost
    * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
    */
   @Override
   public void onServiceDisconnected(final ComponentName name)
   {
      this.textToSpeechManagerService = null;

      if(this.speecherListener != null)
      {
         this.speecherListener.speecherDestroyed();
         this.speecherListener = null;
      }
   }

   /**
    * Change, as soon as possible, the current engine
    * 
    * @param engineInfo
    *           Engine to set
    * @throws RemoteException
    *            On broken link
    */
   public void setEngine(final EngineInfo engineInfo) throws RemoteException
   {
      this.mutex.lock();

      try
      {
         if(this.textToSpeechManagerService == null)
         {
            this.orders.add(new Order(R.id.SpeecherOrderSetEngine, engineInfo));
         }
         else
         {
            this.textToSpeechManagerService.setSpeecher(new SpeecherInfo(engineInfo));
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * Change, as soon as possible, the current language
    * 
    * @param locale
    *           Language to set
    * @throws RemoteException
    *            On broken link
    */
   public void setLocale(final Locale locale) throws RemoteException
   {
      this.mutex.lock();

      try
      {
         if(this.textToSpeechManagerService == null)
         {
            this.orders.add(new Order(R.id.SpeecherOrderSetLocale, locale));
         }
         else
         {
            this.textToSpeechManagerService.setLanguage(new Language(locale));
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * Speak on normal (normal) priority
    * 
    * @param text
    *           Text to speak
    * @throws RemoteException
    *            On broken link
    */
   public void speak(final String text) throws RemoteException
   {
      this.mutex.lock();

      try
      {
         if(this.textToSpeechManagerService == null)
         {
            this.orders.add(new Order(R.id.SpeecherOrderSpeakNormal, text));
         }
         else
         {
            this.textToSpeechManagerService.speak(text);
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * Speak on banl (low) priority
    * 
    * @param text
    *           Text to speak
    * @throws RemoteException
    *            On broken link
    */
   public void speakBanal(final String text) throws RemoteException
   {
      this.mutex.lock();

      try
      {
         if(this.textToSpeechManagerService == null)
         {
            this.orders.add(new Order(R.id.SpeecherOrderSpeakBanal, text));
         }
         else
         {
            this.textToSpeechManagerService.speakBanal(text);
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * Speak on urgent (high) priority
    * 
    * @param text
    *           Text to speak
    * @throws RemoteException
    *            On broken link
    */
   public void speakUrgent(final String text) throws RemoteException
   {
      this.mutex.lock();

      try
      {
         if(this.textToSpeechManagerService == null)
         {
            this.orders.add(new Order(R.id.SpeecherOrderSpeakUrgent, text));
         }
         else
         {
            this.textToSpeechManagerService.speakUrgent(text);
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }
}