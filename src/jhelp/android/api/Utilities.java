package jhelp.android.api;

/**
 * Some utilities
 * 
 * @author JHelp
 */
public class Utilities
{
   /**
    * Make caller thread sleep for a time in millisencond
    * 
    * @param millisecond
    *           Millisencond to sleep
    */
   public static void sleep(final int millisecond)
   {
      try
      {
         Thread.sleep(millisecond);
      }
      catch(final Exception exception)
      {
      }
   }
}