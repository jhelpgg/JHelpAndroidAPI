package jhelp.android.api;

import android.hardware.Sensor;
import android.util.Log;
import android.util.Pair;

/**
 * Debug tools
 * 
 * @author JHelp
 */
public class Debug
{
   /** Sensor accelerometer generic name */
   public static final String SENSOR_TYPE_ACCELEROMETER       = "ACCELEROMETER";
   /** All sensor generic name */
   public static final String SENSOR_TYPE_ALL                 = "ALL";
   /** Sensor ambiant temperature generic name */
   public static final String SENSOR_TYPE_AMBIENT_TEMPERATURE = "AMBIENT TEMPERATURE";
   /** Sensor gravity generic name */
   public static final String SENSOR_TYPE_GRAVITY             = "GRAVITY";
   /** Sensor gyroscope generic name */
   public static final String SENSOR_TYPE_GYROSCOPE           = "GYROSCOPE";
   /** Sensor light generic name */
   public static final String SENSOR_TYPE_LIGHT               = "LIGHT";
   /** Sensor linear acceleation generic name */
   public static final String SENSOR_TYPE_LINEAR_ACCELERATION = "LINEAR ACCELERATION";
   /** Sensor magnetic field generic name */
   public static final String SENSOR_TYPE_MAGNETIC_FIELD      = "MAGNETIC FIELD";
   /** Sensor orientation generic name */
   public static final String SENSOR_TYPE_ORIENTATION         = "ORIENTATION";
   /** Sensor pressure generic name */
   public static final String SENSOR_TYPE_PRESSURE            = "PRESSURE";
   /** Sensor proximity generic name */
   public static final String SENSOR_TYPE_PROXIMITY           = "PROXIMITY";
   /** Sensor relative humidity generic name */
   public static final String SENSOR_TYPE_RELATIVE_HUMIDITY   = "RELATIVE HUMIDITY";
   /** Sensor rotation vector generic name */
   public static final String SENSOR_TYPE_ROTATION_VECTOR     = "ROTATION VECTOR";
   /** Sensor temperature generic name */
   public static final String SENSOR_TYPE_TEMPERATURE         = "TEMPERATURE";
   /** Unknow sensor generic start name */
   public static final String SENSOR_TYPE_UNKNOWN             = "UNKNOWN : ";

   /**
    * Create a {@link Pair} of TAG, header for the message to print
    * 
    * @return {@link Pair} of TAG, header for the message to print
    */
   private static Pair<String, String> createHeader()
   {
      final Throwable throwable = new Throwable();
      final StackTraceElement stackTraceElement = throwable.getStackTrace()[2];

      final String className = stackTraceElement.getClassName();

      final int index = className.lastIndexOf('.');
      final String tag = index >= 0
            ? className.substring(index + 1)
            : className;

      final StringBuilder header = new StringBuilder(className);
      header.append(" at ");
      header.append(stackTraceElement.getLineNumber());
      header.append(" : ");

      return new Pair<String, String>(tag, header.toString());
   }

   /**
    * Create a part of message on filling a buffer
    * 
    * @param builder
    *           Buffer to fill
    * @param object
    *           Part of message to add
    */
   private static void createMessage(final StringBuilder builder, final Object object)
   {
      if((object == null) || (object.getClass().isArray() == false))
      {
         builder.append(object);

         return;
      }

      builder.append('[');

      if(object.getClass().getComponentType().isPrimitive() == true)
      {
         final String name = object.getClass().getComponentType().getName();

         if(Constants.PRIMITIVE_BOOLEAN.equals(name) == true)
         {
            final boolean[] array = (boolean[]) object;
            final int length = array.length;

            if(length > 0)
            {
               builder.append(array[0]);

               for(int i = 1; i < length; i++)
               {
                  builder.append(", ");

                  builder.append(array[i]);
               }
            }
         }
         else if(Constants.PRIMITIVE_BYTE.equals(name) == true)
         {
            final byte[] array = (byte[]) object;
            final int length = array.length;

            if(length > 0)
            {
               builder.append(array[0]);

               for(int i = 1; i < length; i++)
               {
                  builder.append(", ");

                  builder.append(array[i]);
               }
            }
         }
         else if(Constants.PRIMITIVE_CHAR.equals(name) == true)
         {
            final char[] array = (char[]) object;
            final int length = array.length;

            if(length > 0)
            {
               builder.append(array[0]);

               for(int i = 1; i < length; i++)
               {
                  builder.append(", ");

                  builder.append(array[i]);
               }
            }
         }
         else if(Constants.PRIMITIVE_DOUBLE.equals(name) == true)
         {
            final double[] array = (double[]) object;
            final int length = array.length;

            if(length > 0)
            {
               builder.append(array[0]);

               for(int i = 1; i < length; i++)
               {
                  builder.append(", ");

                  builder.append(array[i]);
               }
            }
         }
         else if(Constants.PRIMITIVE_FLOAT.equals(name) == true)
         {
            final float[] array = (float[]) object;
            final int length = array.length;

            if(length > 0)
            {
               builder.append(array[0]);

               for(int i = 1; i < length; i++)
               {
                  builder.append(", ");

                  builder.append(array[i]);
               }
            }
         }
         else if(Constants.PRIMITIVE_INT.equals(name) == true)
         {
            final int[] array = (int[]) object;
            final int length = array.length;

            if(length > 0)
            {
               builder.append(array[0]);

               for(int i = 1; i < length; i++)
               {
                  builder.append(", ");

                  builder.append(array[i]);
               }
            }
         }
         else if(Constants.PRIMITIVE_LONG.equals(name) == true)
         {
            final long[] array = (long[]) object;
            final int length = array.length;

            if(length > 0)
            {
               builder.append(array[0]);

               for(int i = 1; i < length; i++)
               {
                  builder.append(", ");

                  builder.append(array[i]);
               }
            }
         }
         else if(Constants.PRIMITIVE_SHORT.equals(name) == true)
         {
            final short[] array = (short[]) object;
            final int length = array.length;

            if(length > 0)
            {
               builder.append(array[0]);

               for(int i = 1; i < length; i++)
               {
                  builder.append(", ");

                  builder.append(array[i]);
               }
            }
         }
      }
      else
      {
         final Object[] array = (Object[]) object;
         final int length = array.length;

         if(length > 0)
         {
            Debug.createMessage(builder, array[0]);

            for(int i = 1; i < length; i++)
            {
               builder.append(", ");

               Debug.createMessage(builder, array[i]);
            }
         }
      }

      builder.append(']');
   }

   /**
    * Create a message from different {@link Object objects}.<br>
    * If one {@link Object} is an array, then the content of the array is printed automatically
    * 
    * @param message
    *           Part of message to print
    * @return Message formated to be print
    */
   public static String createMessage(final Object... message)
   {
      if(message == null)
      {
         return "null";
      }

      final StringBuilder builder = new StringBuilder();

      for(final Object object : message)
      {
         Debug.createMessage(builder, object);
      }

      return builder.toString();
   }

   /**
    * Obtain sensor Type name
    * 
    * @param sensor
    *           Sensor to get type name
    * @return Type name
    */
   @SuppressWarnings("deprecation")
   public static String obtainSensorTypeName(final Sensor sensor)
   {
      switch(sensor.getType())
      {
         case Sensor.TYPE_ACCELEROMETER:
            return Debug.SENSOR_TYPE_ACCELEROMETER;
         case Sensor.TYPE_ALL:
            return Debug.SENSOR_TYPE_ALL;
         case Sensor.TYPE_AMBIENT_TEMPERATURE:
            return Debug.SENSOR_TYPE_AMBIENT_TEMPERATURE;
         case Sensor.TYPE_GRAVITY:
            return Debug.SENSOR_TYPE_GRAVITY;
         case Sensor.TYPE_GYROSCOPE:
            return Debug.SENSOR_TYPE_GYROSCOPE;
         case Sensor.TYPE_LIGHT:
            return Debug.SENSOR_TYPE_LIGHT;
         case Sensor.TYPE_LINEAR_ACCELERATION:
            return Debug.SENSOR_TYPE_LINEAR_ACCELERATION;
         case Sensor.TYPE_MAGNETIC_FIELD:
            return Debug.SENSOR_TYPE_MAGNETIC_FIELD;
         case Sensor.TYPE_ORIENTATION:
            return Debug.SENSOR_TYPE_ORIENTATION;
         case Sensor.TYPE_PRESSURE:
            return Debug.SENSOR_TYPE_PRESSURE;
         case Sensor.TYPE_PROXIMITY:
            return Debug.SENSOR_TYPE_PROXIMITY;
         case Sensor.TYPE_RELATIVE_HUMIDITY:
            return Debug.SENSOR_TYPE_RELATIVE_HUMIDITY;
         case Sensor.TYPE_ROTATION_VECTOR:
            return Debug.SENSOR_TYPE_ROTATION_VECTOR;
         case Sensor.TYPE_TEMPERATURE:
            return Debug.SENSOR_TYPE_TEMPERATURE;
      }

      return Debug.SENSOR_TYPE_UNKNOWN + sensor.getType();
   }

   /**
    * Print an error
    * 
    * @param error
    *           Error to print
    * @param message
    *           Additional message
    */
   public static void printError(final Error error, final Object... message)
   {
      final Pair<String, String> header = Debug.createHeader();

      Log.e(header.first, header.second + "!@$ ERROR $@! " + Debug.createMessage(message) + " !@$ ERROR $@!", error);
   }

   /**
    * Print a exception
    * 
    * @param exception
    *           Exception to print
    * @param message
    *           Additional message
    */
   public static void printException(final Exception exception, final Object... message)
   {
      final Pair<String, String> header = Debug.createHeader();

      Log.e(header.first, header.second + Debug.createMessage(message), exception);
   }

   /**
    * Print a message
    * 
    * @param message
    *           Message to print
    */
   public static void println(final Object... message)
   {
      final Pair<String, String> header = Debug.createHeader();

      Log.d(header.first, header.second + Debug.createMessage(message));
   }

   /**
    * Print a message in manner to be emphasys
    * 
    * @param message
    *           Message to emphase
    */
   public static void printMark(final Object... message)
   {
      final Pair<String, String> header = Debug.createHeader();

      final String mark = Debug.createMessage(message);

      final int length = mark.length() + 12;
      final char[] headers = new char[length];

      for(int i = 0; i < length; i++)
      {
         headers[i] = '*';
      }

      final String stars = new String(headers);

      Log.v(header.first, header.second + stars);
      Log.v(header.first, header.second + "***   " + mark + "   ***");
      Log.v(header.first, header.second + stars);
   }

   /**
    * Print a TODO message
    * 
    * @param message
    *           Message to print
    */
   public static void printTodo(final Object... message)
   {
      final Pair<String, String> header = Debug.createHeader();

      Log.d(header.first, header.second + "---TODO--- " + Debug.createMessage(message) + " ---TODO---");
   }

   /**
    * Print the stack trace
    * 
    * @param message
    *           Additional message
    */
   public static void printTrace(final Object... message)
   {
      final Pair<String, String> header = Debug.createHeader();

      Log.d(header.first, header.second + "***TRACE*** " + Debug.createMessage(message) + " ***TACE***", new Throwable());
   }

   /**
    * Print a warning
    * 
    * @param message
    *           Message to print
    */
   public static void printWarning(final Object... message)
   {
      final Pair<String, String> header = Debug.createHeader();

      Log.w(header.first, header.second + "/!\\ " + Debug.createMessage(message) + " /!\\");
   }
}