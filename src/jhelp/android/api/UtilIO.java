package jhelp.android.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Input/Output utilities
 * 
 * @author JHelp
 */
public class UtilIO
{
   /** One giga bytes */
   public static final int GIGA           = 1024 * UtilIO.MEGA;
   /** One mega bytes */
   public static final int KILO           = 1024;
   /** One kilo bytes */
   public static final int MEGA           = 1024 * UtilIO.KILO;
   /** Buffer size */
   public static final int TEMPORARY_SIZE = (4 * UtilIO.MEGA) + (2 * UtilIO.KILO) + 1;

   /**
    * Copy a stream to an other ones.<br>
    * Streams aren't not closed at the end of the copy, it is the caller job
    * 
    * @param inputStream
    *           Stream source
    * @param outputStream
    *           Stream destination
    * @throws IOException
    *            On copy issue
    */
   public static void copy(final InputStream inputStream, final OutputStream outputStream) throws IOException
   {
      final byte[] temp = new byte[UtilIO.TEMPORARY_SIZE];

      int read = inputStream.read(temp);

      while(read >= 0)
      {
         outputStream.write(temp, 0, read);

         read = inputStream.read(temp);
      }

      outputStream.flush();
   }

   /**
    * Copy a stream to an other ones.<br>
    * The copy it limited to a number of bytes to copy. If the source is shoerter than the specified length, all the source is
    * copied, if it is bigger, only the first <b>length<</b> are copied<br>
    * Streams aren't not closed at the end of the copy, it is the caller job
    * 
    * @param inputStream
    *           Stream source
    * @param outputStream
    *           Stream destination
    * @param length
    *           Number of maximum of bytes to copy
    * @throws IOException
    *            On copy issue
    */
   public static void copy(final InputStream inputStream, final OutputStream outputStream, long length) throws IOException
   {
      if(length <= 0)
      {
         return;
      }

      int size = UtilIO.TEMPORARY_SIZE;
      if(length < size)
      {
         size = (int) length;
      }

      final byte[] temp = new byte[size];

      int read = inputStream.read(temp);

      while(read >= 0)
      {
         outputStream.write(temp, 0, read);
         length -= read;

         if(length <= 0)
         {
            break;
         }

         size = temp.length;
         if(length < size)
         {
            size = (int) length;
         }

         read = inputStream.read(temp, 0, size);
      }

      outputStream.flush();
   }

   /**
    * Create a directory and its parents if need
    * 
    * @param directory
    *           Directory to create
    * @return {@code true} if directory exists after the method execution
    */
   public static boolean createDirectory(final File directory)
   {
      if(directory == null)
      {
         return false;
      }

      if(directory.exists() == true)
      {
         return directory.isDirectory();
      }

      if(UtilIO.createDirectory(directory.getParentFile()) == true)
      {
         return directory.mkdir();
      }

      return false;
   }

   /**
    * Create a file and its parents directories if need
    * 
    * @param file
    *           File to create
    * @return {@code true} if file exists after the method execution
    */
   public static boolean createFile(final File file)
   {
      if(file == null)
      {
         return false;
      }

      if(file.exists() == true)
      {
         return file.isFile();
      }

      if(UtilIO.createDirectory(file.getParentFile()) == true)
      {
         try
         {
            return file.createNewFile();
         }
         catch(final IOException exception)
         {
            Debug.printException(exception, "Creation of file ", file.getAbsolutePath(), " failed !");

            return false;
         }
      }

      return false;
   }

   /**
    * Delete a file or a directory. If it is a directory, all sub directories and files are deleted also
    * 
    * @param file
    *           File or directory to delete
    * @return {@code true} if deletion completly succed. If {@code false} return, the deletion may be partial (For a directory,
    *         some files have be deleted, some don't) of may realy happen when application exit
    */
   public static boolean delete(final File file)
   {
      if((file == null) || (file.exists() == false))
      {
         return true;
      }

      if(file.isDirectory() == true)
      {
         for(final File child : file.listFiles())
         {
            if(UtilIO.delete(child) == false)
            {
               return false;
            }
         }
      }

      if(file.delete() == false)
      {
         file.deleteOnExit();

         return false;
      }

      return true;
   }

   /**
    * Read a {@link Binarizable} inside a stream.<br>
    * The {@link Binarizable} should be previously written by {@link #writeBinarizable(Binarizable, OutputStream)}
    * 
    * @param <B>
    *           {@link Binarizable} type
    * @param clas
    *           Class of the {@link Binarizable}
    * @param inputStream
    *           Stream to read
    * @return The {@link Binarizable} read
    * @throws IOException
    *            On read the stream or the data not represents the asked {@link Binarizable}
    */
   public static <B extends Binarizable> B readBinarizable(final Class<B> clas, final InputStream inputStream) throws IOException
   {
      try
      {
         final ByteArray byteArray = new ByteArray();

         UtilIO.write(inputStream, byteArray.getOutputStream());

         return byteArray.readBinarizable(clas);
      }
      catch(final Exception exception)
      {
         throw new IOException("Failed to read the Binarizable " + clas.getName() + " in the given stream !", exception);
      }
   }

   /**
    * Read a {@link Binarizable} inside a stream.<br>
    * The {@link Binarizable} should be previously written by {@link #writeBinarizableNamed(Binarizable, OutputStream)}
    * 
    * @param <B>
    *           {@link Binarizable} type
    * @param inputStream
    *           Stream to read
    * @return The {@link Binarizable} read
    * @throws IOException
    *            On read the stream or the data not represents the asked {@link Binarizable}
    */
   public static <B extends Binarizable> B readBinarizableNamed(final InputStream inputStream) throws IOException
   {
      try
      {
         @SuppressWarnings("unchecked")
         final Class<B> clas = (Class<B>) Class.forName(UtilIO.readString(inputStream));

         return UtilIO.readBinarizable(clas, inputStream);
      }
      catch(final Exception exception)
      {
         throw new IOException("Failed to read the Binarizable in the given stream !", exception);
      }
   }

   /**
    * Read an exact number of bytes in a stream.<br>
    * If the stream is not long enough an exception is throw
    * 
    * @param inputStream
    *           Stream to read
    * @param length
    *           Number of bytes to read
    * @return Read bytes
    * @throws IOException
    *            On reading issue, or if the stream doesn't contains enough bytes
    */
   public static byte[] readExactArraySize(final InputStream inputStream, final int length) throws IOException
   {
      final byte[] array = new byte[length];
      int total = 0;

      int read = inputStream.read(array);

      if(read >= 0)
      {
         total += read;
      }

      while((read >= 0) && (total < length))
      {
         read = inputStream.read(array, total, length - total);

         if(read >= 0)
         {
            total += read;
         }
      }

      if(total < length)
      {
         throw new IOException("Not enough data for read " + length + " bytes");
      }

      return array;
   }

   /**
    * Read float from a stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Extracted float
    * @throws IOException
    *            On reading issue
    */
   public static float readFloat(final InputStream inputStream) throws IOException
   {
      return Float.intBitsToFloat(UtilIO.readInt(inputStream));
   }

   /**
    * Read a float array from stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Extracted array
    * @throws IOException
    *            On reading issue
    */
   public static float[] readFloatArray(final InputStream inputStream) throws IOException
   {
      final int lenght = UtilIO.readInt(inputStream);

      if(lenght < 0)
      {
         return null;
      }

      final float[] array = new float[lenght];
      for(int a = 0; a < lenght; a++)
      {
         array[a] = UtilIO.readFloat(inputStream);
      }

      return array;
   }

   /**
    * Read an integer from a stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Read integer
    * @throws IOException
    *            On read issue
    */
   public static final int readInt(final InputStream inputStream) throws IOException
   {
      final byte[] data = UtilIO.readExactArraySize(inputStream, 4);

      return ((data[0] & 0xFF) << 24) | ((data[1] & 0xFF) << 16) | ((data[2] & 0xFF) << 8) | (data[3] & 0xFF);
   }

   /**
    * Read a long from a stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Read long
    * @throws IOException
    *            On read issue
    */
   public static final long readLong(final InputStream inputStream) throws IOException
   {
      final byte[] data = UtilIO.readExactArraySize(inputStream, 8);

      return ((data[0] & 0xFF) << 56) | ((data[1] & 0xFF) << 48) | ((data[2] & 0xFF) << 40) | ((data[3] & 0xFF) << 32) | ((data[4] & 0xFF) << 24) | ((data[5] & 0xFF) << 16) | ((data[6] & 0xFF) << 8) | (data[7] & 0xFF);
   }

   /**
    * Read a string from a stream
    * 
    * @param inputStream
    *           Stream to read
    * @return Read string
    * @throws IOException
    *            On read issue
    */
   public static String readString(final InputStream inputStream) throws IOException
   {
      final int length = UtilIO.readInt(inputStream);

      if(length < 0)
      {
         return null;
      }

      final byte[] utf8 = UtilIO.readExactArraySize(inputStream, length);
      return new String(utf8, "UTF-8");
   }

   /**
    * Read a UTF-8 string from a part of byte array
    * 
    * @param array
    *           Array to read
    * @param offset
    *           Start read index in array
    * @param length
    *           Number of bytes to read
    * @return Read string
    */
   public static String readUTF8(final byte[] array, final int offset, final int length)
   {
      try
      {
         return new String(array, offset, length, "UTF-8");
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Can't convert from UTF-8 : ", array);
         return null;
      }
   }

   /**
    * Convert string to UTF-8 array
    * 
    * @param string
    *           String to convert
    * @return Converted string
    */
   public static byte[] toUTF8(final String string)
   {
      try
      {
         return string.getBytes("UTF-8");
      }
      catch(final Exception exception)
      {
         Debug.printException(exception, "Can't convert to UTF-8 : ", string);
         return null;
      }
   }

   /**
    * Write a stream inside on other one
    * 
    * @param inputStream
    *           Stream source
    * @param outputStream
    *           Stream destination
    * @throws IOException
    *            On copying issue
    */
   public static void write(final InputStream inputStream, final OutputStream outputStream) throws IOException
   {
      final byte[] buffer = new byte[UtilIO.TEMPORARY_SIZE];

      int read = inputStream.read(buffer);

      while(read >= 0)
      {
         outputStream.write(buffer, 0, read);

         read = inputStream.read(buffer);
      }

      outputStream.flush();
   }

   /**
    * Write a {@link Binarizable} inside a stream.<br>
    * To read it later, use {@link #readBinarizable(Class, InputStream)}
    * 
    * @param binarizable
    *           {@link Binarizable} to write
    * @param outputStream
    *           Stream where write
    * @throws IOException
    *            On writing issue
    */
   public static void writeBinarizable(final Binarizable binarizable, final OutputStream outputStream) throws IOException
   {
      final ByteArray byteArray = new ByteArray();

      byteArray.writeBinarizable(binarizable);

      UtilIO.write(byteArray.getInputStream(), outputStream);
   }

   /**
    * Write a {@link Binarizable} inside a stream.<br>
    * To read it later, use {@link #readBinarizableNamed(InputStream)}
    * 
    * @param binarizable
    *           {@link Binarizable} to write
    * @param outputStream
    *           Stream where write
    * @throws IOException
    *            On writing issue
    */
   public static void writeBinarizableNamed(final Binarizable binarizable, final OutputStream outputStream) throws IOException
   {
      UtilIO.writeString(outputStream, binarizable.getClass().getName());

      UtilIO.writeBinarizable(binarizable, outputStream);
   }

   /**
    * Write a float inside a stream
    * 
    * @param outputStream
    *           Stream where write
    * @param f
    *           Float to write
    * @throws IOException
    *            On writing issue
    */
   public static void writeFloat(final OutputStream outputStream, final float f) throws IOException
   {
      UtilIO.writeInt(outputStream, Float.floatToIntBits(f));
   }

   /**
    * Write a float array inside a stream
    * 
    * @param outputStream
    *           Stream where write
    * @param array
    *           Array to write
    * @throws IOException
    *            On writing issue
    */
   public static void writeFloatArray(final OutputStream outputStream, final float[] array) throws IOException
   {
      if(array == null)
      {
         UtilIO.writeInt(outputStream, -1);

         return;
      }

      final int lenght = array.length;
      UtilIO.writeInt(outputStream, lenght);

      for(int a = 0; a < lenght; a++)
      {
         UtilIO.writeFloat(outputStream, array[a]);
      }
   }

   /**
    * Write an integer inside a stream
    * 
    * @param outputStream
    *           Stream where write
    * @param integer
    *           Integer to write
    * @throws IOException
    *            On writing issue
    */
   public static void writeInt(final OutputStream outputStream, final int integer) throws IOException
   {
      final byte[] data =
      {
            (byte) ((integer >> 24) & 0xFF), (byte) ((integer >> 16) & 0xFF), (byte) ((integer >> 8) & 0xFF), (byte) (integer & 0xFF)
      };

      outputStream.write(data);
      outputStream.flush();
   }

   /**
    * Write a long inside a stream
    * 
    * @param outputStream
    *           Stream where write
    * @param integer
    *           Long to write
    * @throws IOException
    *            On writing issue
    */
   public static void writeLong(final OutputStream outputStream, final long integer) throws IOException
   {
      final byte[] data =
      {
            (byte) ((integer >> 56) & 0xFF), (byte) ((integer >> 48) & 0xFF), (byte) ((integer >> 40) & 0xFF), (byte) ((integer >> 32) & 0xFF), (byte) ((integer >> 24) & 0xFF), (byte) ((integer >> 16) & 0xFF),
            (byte) ((integer >> 8) & 0xFF), (byte) (integer & 0xFF)
      };

      outputStream.write(data);
      outputStream.flush();
   }

   /**
    * Write a String inside a stream
    * 
    * @param outputStream
    *           Stream where write
    * @param string
    *           String to write
    * @throws IOException
    *            On writing issue
    */
   public static void writeString(final OutputStream outputStream, final String string) throws IOException
   {
      if(string == null)
      {
         UtilIO.writeInt(outputStream, -1);

         return;
      }

      final byte[] utf8 = string.getBytes("UTF-8");
      UtilIO.writeInt(outputStream, utf8.length);

      outputStream.write(utf8);
      outputStream.flush();
   }
}