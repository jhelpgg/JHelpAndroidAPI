package jhelp.android.api.engine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Buffer utilities
 *
 * @author JHelp
 */
public class UtilBuffer
{
    /**
     * Maximum dimension
     */

    private static final int         MAX_DIMENSION_IN_BYTES = 32;
    /**
     * The buffer
     */
    public static final  ByteBuffer  TEMPORARY_BYTE_BUFFER  = ByteBuffer.allocateDirect(
            UtilBuffer.MAX_DIMENSION_IN_BYTES)
                                                                        .order(ByteOrder
                                                                                       .nativeOrder());
    /**
     * See the buffer in FloatBuffer
     */
    public static final  FloatBuffer TEMPORARY_FLOAT_BUFFER = UtilBuffer.TEMPORARY_BYTE_BUFFER
            .asFloatBuffer();
    /**
     * See the buffer in IntBuffer
     */
    public static final  IntBuffer   TEMPORARY_INT_BUFFER   = UtilBuffer.TEMPORARY_BYTE_BUFFER
            .asIntBuffer();

    /**
     * Create a byte buffer
     *
     * @param size Buffer size (Number of bytes)
     * @return Created buffer
     */
    public static ByteBuffer createByteBuffer(final int size)
    {
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(size);

        byteBuffer.order(ByteOrder.nativeOrder());

        return byteBuffer;
    }

    /**
     * Create a float buffer from an array
     *
     * @param array Array to copy inside the buffer
     * @return Created buffer
     */
    public static FloatBuffer createFloatBuffer(final float[] array)
    {
        final FloatBuffer floatBuffer = UtilBuffer.createFloatBuffer(array.length);

        floatBuffer.put(array);
        floatBuffer.position(0);

        return floatBuffer;
    }

    /**
     * Create a float buffer
     *
     * @param size Buffer size (Number of floats)
     * @return Created buffer
     */
    public static FloatBuffer createFloatBuffer(final int size)
    {
        return UtilBuffer.createByteBuffer(size * 4)
                         .asFloatBuffer();
    }
}