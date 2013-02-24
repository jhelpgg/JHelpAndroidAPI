package jhelp.android.api.text2speech;

import  jhelp.android.api.text2speech.SpeecherInfo;
import  jhelp.android.api.text2speech.Language;

/**
 * Link to text to speech manager service
 */
interface ITextToSpeechManagerService
{
	 /**
	  * Clear the text queue
	  */
	 void clear();
	 /**
	  * Obtain the engine list
	  * @return The engine list
	  */
	 SpeecherInfo[] speecherList();
	 /**
	  * Indicates if a language is supported
	  * @param language Tested language
	  * @return {@code true} if language is supported
	  */
	 boolean isLanguageSuported(in Language language);
	 /**
	  * Set the engine
	  * @param speecherInfo Engine to set
	  */
	 void setSpeecher(in SpeecherInfo speecherInfo);
	 /**
	  * Obtain current language
	  * @return Current language
	  */
	 Language getLanguage();
	 /**
	  * Change current language
	  * @param language New language
	  */
	 void setLanguage(in Language language);
	 /**
	  * Speak in normal priority
	  * @param text Text to speak
	  */
	 void speak(in String text);
    /**
     * Speak in banal priority
     * @param text Text to speak
     */
	 void speakBanal(in String text);
    /**
     * Speak in urgent priority
     * @param text Text to speak
     */
	 void speakUrgent(in String text);
}