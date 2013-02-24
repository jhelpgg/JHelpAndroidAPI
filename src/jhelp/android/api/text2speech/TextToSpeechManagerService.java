package jhelp.android.api.text2speech;

import java.util.List;

import jhelp.android.api.Debug;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.speech.tts.TextToSpeech.EngineInfo;

/**
 * Service that manage text to speach.<br>
 * It manages text queue, priority and access to text to speach
 * 
 * @author JHelp
 */
public class TextToSpeechManagerService
      extends Service
{
   /**
    * Link external to receive command from {@link Speecher}
    * 
    * @author JHelp
    */
   class ITextToSpeechManagerServiceImplementation
         extends ITextToSpeechManagerService.Stub
   {
      /**
       * Create a new instance of ITextToSpeechManagerServiceImplementation
       */
      public ITextToSpeechManagerServiceImplementation()
      {
      }

      /**
       * Clear the text queue <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @throws RemoteException
       *            On link broken
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#clear()
       */
      @Override
      public void clear() throws RemoteException
      {
         TextToSpeechManagerService.this.textToSpeechManager.clear();
      }

      /**
       * Obtain current language <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @return Current language
       * @throws RemoteException
       *            On broken link
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#getLanguage()
       */
      @Override
      public Language getLanguage() throws RemoteException
      {
         return new Language(TextToSpeechManagerService.this.textToSpeechManager.getLocale());
      }

      /**
       * Indicates if a language is supported <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param language
       *           Tested language
       * @return {@code true} if language is supported
       * @throws RemoteException
       *            On broken link
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#isLanguageSuported(jhelp.android.api.text2speech.Language)
       */
      @Override
      public boolean isLanguageSuported(final Language language) throws RemoteException
      {
         return TextToSpeechManagerService.this.textToSpeechManager.isLocaleSupported(language.getLocale());
      }

      /**
       * Change current language <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param language
       *           New lauguage
       * @throws RemoteException
       *            On broken link
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#setLanguage(jhelp.android.api.text2speech.Language)
       */
      @Override
      public void setLanguage(final Language language) throws RemoteException
      {
         TextToSpeechManagerService.this.textToSpeechManager.setLocale(language.getLocale());
      }

      /**
       * Change the current engine <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param speecherInfo
       *           New engine description
       * @throws RemoteException
       *            On broken link
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#setSpeecher(jhelp.android.api.text2speech.SpeecherInfo)
       */
      @Override
      public void setSpeecher(final SpeecherInfo speecherInfo) throws RemoteException
      {
         TextToSpeechManagerService.this.textToSpeechManager.setEngine(speecherInfo.getEngineInfo());
      }

      /**
       * Speak a text in normal priority <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param text
       *           Text to speak
       * @throws RemoteException
       *            On broken link
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#speak(java.lang.String)
       */
      @Override
      public void speak(final String text) throws RemoteException
      {
         TextToSpeechManagerService.this.textToSpeechManager.speak(text);
      }

      /**
       * Speak a text in banal (low) priority <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param text
       *           Text to speak
       * @throws RemoteException
       *            On broken link
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#speakBanal(java.lang.String)
       */
      @Override
      public void speakBanal(final String text) throws RemoteException
      {
         TextToSpeechManagerService.this.textToSpeechManager.speakBanal(text);
      }

      /**
       * Speak a text in urgent (high) priority <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param text
       *           Text to speak
       * @throws RemoteException
       *            On broken link
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#speakUrgent(java.lang.String)
       */
      @Override
      public void speakUrgent(final String text) throws RemoteException
      {
         TextToSpeechManagerService.this.textToSpeechManager.speakUrgent(text);
      }

      /**
       * Obtain available engines list <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @return Available engines list
       * @throws RemoteException
       *            On broken link
       * @see jhelp.android.api.text2speech.ITextToSpeechManagerService#speecherList()
       */
      @Override
      public SpeecherInfo[] speecherList() throws RemoteException
      {
         final List<EngineInfo> list = TextToSpeechManagerService.this.textToSpeechManager.engineList();

         final int size = list.size();
         final SpeecherInfo[] array = new SpeecherInfo[size];

         for(int i = 0; i < size; i++)
         {
            array[i] = new SpeecherInfo(list.get(i));
         }

         return array;
      }
   }

   /** Text to speach service link */
   private final ITextToSpeechManagerServiceImplementation textToSpeechManagerServiceImplementation = new ITextToSpeechManagerServiceImplementation();
   /** Text to speach manager */
   TextToSpeechManager                                     textToSpeechManager;

   /**
    * Called when service is binded from external <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param intent
    *           Intent that bound the service
    * @return Link to the service
    * @see android.app.Service#onBind(android.content.Intent)
    */
   @Override
   public IBinder onBind(final Intent intent)
   {
      return this.textToSpeechManagerServiceImplementation.asBinder();
   }

   /**
    * Called when service is created <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see android.app.Service#onCreate()
    */
   @Override
   public void onCreate()
   {
      super.onCreate();

      this.textToSpeechManager = TextToSpeechManager.obtainTextToSpeechManager(this);
   }

   /**
    * Called when service is distroyed <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @see android.app.Service#onDestroy()
    */
   @Override
   public void onDestroy()
   {
      Debug.println("BEFORE destroy");
      this.textToSpeechManager.destroy();
      Debug.println("AFTER destroy");

      super.onDestroy();
   }

   /**
    * Called when distant is disconnected <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param intent
    *           Intent that bound
    * @return {@code false} because we want reinitialize all at next bind
    * @see android.app.Service#onUnbind(android.content.Intent)
    */
   @Override
   public boolean onUnbind(final Intent intent)
   {
      Debug.printTodo("onUnbind");

      return false;
   }
}