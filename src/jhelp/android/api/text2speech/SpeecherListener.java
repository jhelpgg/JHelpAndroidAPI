package jhelp.android.api.text2speech;

/**
 * Listener of speecher states
 * 
 * @author JHelp
 */
public interface SpeecherListener
{
   /**
    * Called when speecher destroyed
    */
   public void speecherDestroyed();

   /**
    * Called when speecher ready
    */
   public void speecherReady();
}