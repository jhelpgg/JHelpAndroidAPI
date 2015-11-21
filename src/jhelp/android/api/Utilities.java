package jhelp.android.api;

import java.lang.reflect.Array;

/**
 * Some utilities
 * 
 * @author JHelp
 */
public class Utilities
{
   public static final <T> T[] copy(final T[] array)
   {
      if(array == null)
      {
         return null;
      }

      return Utilities.copy(array, 0);
   }

   public static final <T> T[] copy(final T[] array, final int offset)
   {
      if(array == null)
      {
         return null;
      }

      return Utilities.copy(array, offset, array.length);
   }

   public static final <T> T[] copy(final T[] array, int offset, int length)
   {
      if(array == null)
      {
         return null;
      }

      if(offset < 0)
      {
         length += offset;
         offset = 0;
      }

      length = Math.max(0, Math.min(array.length - offset, length));

      if(length <= 0)
      {
         return (T[]) Array.newInstance(array.getClass().getComponentType(), 0);
      }

      final T[] copy = (T[]) Array.newInstance(array.getClass().getComponentType(), length);
      System.arraycopy(array, offset, copy, 0, length);

      return copy;
   }

   public static final <T> void scramble(final T[] array)
   {
      if(array == null)
      {
         return;
      }

      final int size = array.length;
      if(size < 2)
      {
         return;
      }

      int i, j;
      final int time = (size * 2) + (size / 2) + 1;
      T temp;

      for(int t = 0; t < time; t++)
      {
         i = (int) (Math.random() * size);
         j = (int) (Math.random() * size);

         temp = array[i];
         array[i] = array[j];
         array[j] = temp;
      }
   }

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