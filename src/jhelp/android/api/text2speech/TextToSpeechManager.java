package jhelp.android.api.text2speech;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import jhelp.android.api.Debug;
import jhelp.android.api.Mutex;
import jhelp.android.api.R;
import jhelp.android.api.UtilMath;
import jhelp.android.api.Utilities;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.UtteranceProgressListener;

/**
 * Manage text to speach. IOt manages text queue, text priority, engine changes and language changes
 * 
 * @author JHelp
 */
class TextToSpeechManager
      implements OnInitListener, Runnable
{
   /**
    * Listener of text to speach progress
    * 
    * @author JHelp
    */
   class EventListener
         extends UtteranceProgressListener
   {
      /**
       * Called when a text is finish to be spoken <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param utteranceId
       *           Text ID
       * @see android.speech.tts.UtteranceProgressListener#onDone(java.lang.String)
       */
      @Override
      public void onDone(final String utteranceId)
      {
         synchronized(TextToSpeechManager.this.LOCK)
         {
            TextToSpeechManager.this.state = R.id.TextToSpeechStateReadyToSpeak;
            TextToSpeechManager.this.LOCK.notify();
         }
      }

      /**
       * Called when an error happen in text to speach <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param utteranceId
       *           Text ID
       * @see android.speech.tts.UtteranceProgressListener#onError(java.lang.String)
       */
      @Override
      public void onError(final String utteranceId)
      {
         synchronized(TextToSpeechManager.this.LOCK)
         {
            TextToSpeechManager.this.state = R.id.TextToSpeechStateReadyToSpeak;
            TextToSpeechManager.this.LOCK.notify();
         }
      }

      /**
       * Called when text started to be poken <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param utteranceId
       *           Text ID
       * @see android.speech.tts.UtteranceProgressListener#onStart(java.lang.String)
       */
      @Override
      public void onStart(final String utteranceId)
      {
         // Nothing to do
      }
   }

   /**
    * Text information
    * 
    * @author JHelp
    */
   static class Text
         implements Comparable<Text>
   {
      /** Text ID */
      final long     id;
      /** Text priority */
      final Priority priority;
      /** Text itself */
      final String   text;

      /**
       * Create a new instance of Text
       * 
       * @param priority
       *           Priority
       * @param text
       *           Text
       */
      public Text(final Priority priority, final String text)
      {
         this.id = TextToSpeechManager.nextTextId.getAndIncrement();
         this.priority = priority;
         this.text = text;
      }

      /**
       * Compare with an other text.<br>
       * It returns :
       * <table>
       * <tr>
       * <th>&lt; 0</th>
       * <td>:</td>
       * <td>If this text is before the compare one</td>
       * </tr>
       * <tr>
       * <th>0</th>
       * <td>:</td>
       * <td>If this text and compared one are in same place</td>
       * </tr>
       * <tr>
       * <th>&gt; 0</th>
       * <td>:</td>
       * <td>If this textr is after the compared one</td>
       * </tr>
       * </table>
       * <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param text
       *           Text to compare
       * @return Comparison result
       * @see java.lang.Comparable#compareTo(java.lang.Object)
       */
      @Override
      public int compareTo(final Text text)
      {
         final int res = this.priority.compare(text.priority);

         if(res == 0)
         {
            return UtilMath.sign(this.id - text.id);
         }

         return res;
      }
   }

   /**
    * Text priority
    * 
    * @author JHelp
    */
   public static enum Priority
   {
      /** Banal (low) priority */
      BANAL(100),
      /** Normal priority */
      NORMAL(10),
      /** Urgent (high) priority */
      URGENT(0);
      /** The priority */
      private int priority;

      /**
       * Create a new instance of Priority
       * 
       * @param priority
       *           The priority
       */
      Priority(final int priority)
      {
         this.priority = priority;
      }

      /**
       * Compare with an other priority.<br>
       * It returns :
       * <table>
       * <tr>
       * <th>&lt; 0</th>
       * <td>:</td>
       * <td>If this priority is before the compared one</td>
       * </tr>
       * <tr>
       * <th>0</th>
       * <td>:</td>
       * <td>If this priority and compared one are in same place</td>
       * </tr>
       * <tr>
       * <th>&gt; 0</th>
       * <td>:</td>
       * <td>If this priority is after the compared one</td>
       * </tr>
       * </table>
       * 
       * @param priority
       *           Priority to compare with
       * @return Comparison result
       */
      public int compare(final Priority priority)
      {
         return this.priority - priority.priority;
      }
   }

   /** Maximum of attempt for text to speech connexction */
   private static final int                                   MAXIMUM_ATTEMPT         = 10;
   /** Text to speech manager link to a context */
   private static final HashMap<Context, TextToSpeechManager> TEXT_TO_SPEECH_MANAGERS = new HashMap<Context, TextToSpeechManager>();
   /** Next text id */
   static AtomicLong                                          nextTextId              = new AtomicLong();

   /**
    * Obtain a text to speach manager link to a context
    * 
    * @param context
    *           Context for get the manager
    * @return Text to speech manager
    */
   public static TextToSpeechManager obtainTextToSpeechManager(final Context context)
   {
      // context = context.getApplicationContext();

      TextToSpeechManager textToSpeechManager = TextToSpeechManager.TEXT_TO_SPEECH_MANAGERS.get(context);

      if(textToSpeechManager == null)
      {
         textToSpeechManager = new TextToSpeechManager(context);
         TextToSpeechManager.TEXT_TO_SPEECH_MANAGERS.put(context, textToSpeechManager);
      }

      return textToSpeechManager;
   }

   /** Number of current connextion attempt */
   private int                               attempt;
   /** Linked context */
   private final Context                     context;
   /** Current engine */
   private final String                      engine;
   /** Liostener of text to speach progress */
   private final EventListener               eventListener;
   /** Current language */
   private Locale                            locale;
   /** Mutex for synchronization */
   private final Mutex                       mutex;
   /** Current parameters */
   private final HashMap<String, String>     parameters;
   /** Texts priority queue */
   private final PriorityBlockingQueue<Text> texts;
   /** Linked text to speech */
   private TextToSpeech                      textToSpeech;
   /** Lock for synchronization */
   final Object                              LOCK = new Object();
   /** Current manaer state */
   int                                       state;

   /**
    * Create a new instance of TextToSpeechManager
    * 
    * @param context
    *           Linked context
    */
   private TextToSpeechManager(final Context context)
   {
      this.context = context;
      this.mutex = new Mutex();
      this.state = R.id.TextToSpeechStateNotInitialized;
      this.texts = new PriorityBlockingQueue<TextToSpeechManager.Text>();
      this.eventListener = new EventListener();
      this.attempt = 0;
      this.parameters = new HashMap<String, String>();
      this.textToSpeech = new TextToSpeech(context, this);
      this.engine = this.textToSpeech.getDefaultEngine();
   }

   /**
    * Attempt to force a proper destruction in case of {@link #destroy()} is not called <br>
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
    * Clear the text queue
    */
   public void clear()
   {
      this.texts.clear();

      if(this.state == R.id.TextToSpeechStateSpeaking)
      {
         this.textToSpeech.stop();
      }

      if(this.state != R.id.TextToSpeechStateNotInitialized)
      {
         this.state = R.id.TextToSpeechStateReadyToSpeak;
      }
   }

   /**
    * destroy prperly the manager
    */
   public void destroy()
   {
      if(this.state == R.id.TextToSpeechStateSpeaking)
      {
         // this.textToSpeech.stop();
      }

      this.state = R.id.TextToSpeechStateInDestruction;
      synchronized(this.LOCK)
      {
         this.LOCK.notify();
      }

      TextToSpeechManager.TEXT_TO_SPEECH_MANAGERS.remove(this.context);
   }

   /***
    * List of avialable engines
    * 
    * @return List of avialable engines
    */
   public List<EngineInfo> engineList()
   {
      return this.textToSpeech.getEngines();
   }

   /**
    * Current launguage
    * 
    * @return Current launguage
    */
   public Locale getLocale()
   {
      return this.locale;
   }

   /**
    * Indicates if a language is supported
    * 
    * @param locale
    *           Tested language
    * @return {@code true} if language is supported
    */
   public boolean isLocaleSupported(final Locale locale)
   {
      if(locale == null)
      {
         throw new NullPointerException("locale musn't be null");
      }

      switch(this.textToSpeech.isLanguageAvailable(locale))
      {
         case TextToSpeech.LANG_AVAILABLE:
         case TextToSpeech.LANG_COUNTRY_AVAILABLE:
         case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
            return true;
         case TextToSpeech.LANG_MISSING_DATA:
            Debug.printWarning("Data  missing !");
            return false;
         case TextToSpeech.LANG_NOT_SUPPORTED:
            return false;
      }

      Debug.printWarning("Shoudn't go there !");
      return false;
   }

   /**
    * Called when text to speech is initialized <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param status
    *           Initialization status
    * @see android.speech.tts.TextToSpeech.OnInitListener#onInit(int)
    */
   @Override
   public void onInit(final int status)
   {
      if(status == TextToSpeech.ERROR)
      {
         Debug.println("Init failed");
         this.attempt++;

         if(this.attempt < TextToSpeechManager.MAXIMUM_ATTEMPT)
         {
            Debug.println("Try init again");

            this.textToSpeech = new TextToSpeech(this.context, this);
         }
         else
         {
            Debug.printWarning("Text to speech didn't initialized");
         }

         return;
      }

      this.locale = this.textToSpeech.getLanguage();
      this.attempt = 0;
      this.textToSpeech.setOnUtteranceProgressListener(this.eventListener);
      this.state = R.id.TextToSpeechStateReadyToSpeak;

      (new Thread(this)).start();
   }

   /**
    * Do the thread for play texts <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see java.lang.Runnable#run()
    */
   @Override
   public void run()
   {
      Text text;

      while(this.state != R.id.TextToSpeechStateInDestruction)
      {
         Debug.println("Loop");
         if((this.texts.isEmpty() == false) && (this.state == R.id.TextToSpeechStateReadyToSpeak))
         {
            synchronized(this.LOCK)
            {
               this.state = R.id.TextToSpeechStateSpeaking;
            }

            text = this.texts.poll();

            this.parameters.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, String.valueOf(text.id));

            this.textToSpeech.speak(text.text, TextToSpeech.QUEUE_FLUSH, this.parameters);
         }

         synchronized(this.LOCK)
         {
            Debug.println("Wait");
            try
            {
               this.LOCK.wait();
            }
            catch(final Exception exception)
            {
               Debug.printException(exception);
            }
         }
      }

      this.state = R.id.TextToSpeechStateNotInitialized;
      this.textToSpeech.shutdown();
      this.textToSpeech = null;
   }

   /**
    * Change current engine
    * 
    * @param engineInfo
    *           New engine
    */
   public void setEngine(final EngineInfo engineInfo)
   {
      this.mutex.lock();
      try
      {
         if(this.engine.equalsIgnoreCase(engineInfo.name) == true)
         {
            return;
         }

         if(this.state == R.id.TextToSpeechStateSpeaking)
         {
            // this.textToSpeech.stop();
         }

         this.state = R.id.TextToSpeechStateInDestruction;
         synchronized(this.LOCK)
         {
            this.LOCK.notify();
         }

         while(this.textToSpeech != null)
         {
            Utilities.sleep(128);
         }

         this.textToSpeech = new TextToSpeech(this.context, this, engineInfo.name);
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * Change current language
    * 
    * @param locale
    *           New language
    * @return {@code true} if set language is done
    */
   public boolean setLocale(final Locale locale)
   {
      this.mutex.lock();
      try
      {
         if(locale == null)
         {
            throw new NullPointerException("locale musn't be null");
         }

         if(this.locale.equals(locale) == true)
         {
            return true;
         }

         switch(this.textToSpeech.setLanguage(locale))
         {
            case TextToSpeech.LANG_AVAILABLE:
            case TextToSpeech.LANG_COUNTRY_AVAILABLE:
            case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
               this.locale = locale;
               return true;
            case TextToSpeech.LANG_MISSING_DATA:
               Debug.printWarning("Data  missing !");
               return false;
            case TextToSpeech.LANG_NOT_SUPPORTED:
               return false;
         }

         Debug.printWarning("Shoudn't go there !");
         return false;
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * Seak some text
    * 
    * @param priority
    *           Text priority
    * @param text
    *           Text to speak
    */
   public void speak(final Priority priority, final String text)
   {
      this.mutex.lock();
      try
      {
         if(priority == null)
         {
            throw new NullPointerException("priority musn't be null");
         }

         if(text == null)
         {
            throw new NullPointerException("text musn't be null");
         }

         Debug.println("Speak " + priority + " : " + text);

         this.texts.add(new Text(priority, text));

         if(this.state == R.id.TextToSpeechStateReadyToSpeak)
         {
            Debug.println("Notify");
            synchronized(this.LOCK)
            {
               this.LOCK.notify();
            }
         }
      }
      finally
      {
         this.mutex.unlock();
      }
   }

   /**
    * Speak in normal priority
    * 
    * @param text
    *           Text to speak
    */
   public void speak(final String text)
   {
      this.speak(Priority.NORMAL, text);
   }

   /**
    * Speak in banal (low) priority
    * 
    * @param text
    *           Text to speak
    */
   public void speakBanal(final String text)
   {
      this.speak(Priority.BANAL, text);
   }

   /**
    * Speak in urgent (high) priority
    * 
    * @param text
    *           Text to speak
    */
   public void speakUrgent(final String text)
   {
      this.speak(Priority.URGENT, text);
   }
}