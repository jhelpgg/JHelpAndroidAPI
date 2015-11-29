package jhelp.android.api.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;

import jhelp.android.api.engine.util.Debug;
import jhelp.android.api.engine.util.UtilBuffer;
import jhelp.android.api.engine.util.UtilMath;

/**
 * Texture must be have a power of 2 width and height : 1, 2, 4, 8, 16, 32, 64, 128, 256 or 512<br>
 * The texture can be mutable or not.<br>
 * Mutable texture can change during time (Draw on it with canvs give by {@link #getCanvas()},
 * paint {@link #getPaint()} or directly on embed bitmap {@link #getBitmap()} and don't forget to
 * refresh {@link #refresh()} to see modification.<br>
 * Mutable texture take more memory in RAM, that's why you can at any moment make mutable texture
 * to immutable {@link #makeImmutable()}, beware reverse operation not possible
 *
 * @author JHelp
 */
public class Texture
{
    /**
     * Create not  mutable texture from random image size.<br>
     * Image will be resize to fit texture size constraints
     *
     * @param inputStream Image stream
     * @return Created texture OT null if creation failed
     */
    public static Texture createTexture(InputStream inputStream)
    {
        return Texture.createTexture(inputStream, false);
    }

    /**
     * Create texture from random image size.<br>
     * Image will be resize to fit texture size constraints
     *
     * @param inputStream Image stream
     * @param mutable     Indicates if texture is mutable
     * @return Created texture OT null if creation failed
     */
    public static Texture createTexture(InputStream inputStream, boolean mutable)
    {
        Options options = new Options();
        options.inScaled = false;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        int width      = options.outWidth;
        int log2Width  = UtilMath.log2(width);
        int height     = options.outHeight;
        int log2Height = UtilMath.log2(height);
        int log2       = Math.max(log2Width, log2Height);
        options.inJustDecodeBounds = false;
        options.inSampleSize = 1;

        if (log2 > 9)
        {
            options.inSampleSize = 1 << (log2 - 9);
        }

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        width = options.outWidth;
        log2Width = UtilMath.log2(width);
        int finalWidth = 1 << log2Width;
        height = options.outHeight;
        log2Height = UtilMath.log2(height);
        int finalHeight = 1 << log2Height;

        if (width != finalWidth || height != finalHeight)
        {
            Bitmap sample = Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, false);
            bitmap.recycle();
            bitmap = sample;
        }

        return new Texture(bitmap, mutable);
    }

    /**
     * Texture height
     */
    private int        height;
    /**
     * Indicates if texture need to be refresh
     */
    private boolean    needToRefresh;
    /**
     * Texture pixel
     */
    private ByteBuffer pixels;
    /**
     * Video memory ID
     */
    private int        videoMemoryId;
    /**
     * Texture width
     */
    private int        width;
    /**
     * Embed bitmap, for mutable texture
     */
    private Bitmap     bitmap;
    /**
     * Indicates if texture is mutable
     */
    private boolean    mutable;
    /**
     * Canvas for draw on texture, if mutable
     */
    private Canvas     canvas;
    /**
     * Paint for draw on texture, if mutable
     */
    private Paint      paint;

    /**
     * Create texture from good size bitmap
     *
     * @param bitmap  godd sized bitmap
     * @param mutable Indicates if texture should become mutable
     */
    private Texture(Bitmap bitmap, boolean mutable)
    {
        this.videoMemoryId = -1;
        this.needToRefresh = true;
        this.setBitMap(bitmap, mutable);
    }

    /**
     * Create a new instance of Texture : 512x512 mutable
     */
    public Texture()
    {
        this(512, 512);
    }

    /**
     * Create a new instance of Texture : mutable.<br>
     * Given size are modified to fit texture size constraints
     *
     * @param width   Desired texture width
     * @param Desired texture height
     */
    public Texture(int width, int height)
    {
        if (width <= 0 || height <= 0)
        {
            throw new IllegalArgumentException(
                    "width and height MUST be >0 but specified : " + width + "x" + height);
        }

        this.videoMemoryId = -1;
        this.needToRefresh = true;

        int log2 = UtilMath.log2(width);
        width = 1 << Math.min(log2, 9);
        log2 = UtilMath.log2(height);
        height = 1 << Math.min(log2, 9);

        this.setBitMap(Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888), true);
    }

    /**
     * Create a new instance of not mutable Texture from stream.<br>
     * Stream suppose be an image with width and height in {1, 2, 4, 8, 16, 32, 64, 128, 256, 512}
     *
     * @param inputStream Stream to read
     */
    public Texture(final InputStream inputStream)
    {
        this(inputStream, false);
    }

    /**
     * Create a new instance of Texture from stream.<br>
     * Stream suppose be an image with width and height in {1, 2, 4, 8, 16, 32, 64, 128, 256, 512}
     *
     * @param inputStream Stream to read
     * @param mutable     Indicates if texture is mutable
     */
    public Texture(final InputStream inputStream, boolean mutable)
    {
        this.videoMemoryId = -1;
        this.needToRefresh = true;

        this.width = this.height = 0;

        final Options options = new Options();
        options.inScaled = false;

        this.setBitMap(BitmapFactory.decodeStream(inputStream, null, options), mutable);
    }

    /**
     * Create a  new instance of not mutable Texture with resources.<br>
     * Resource suppose be an image with width and height in {1, 2, 4, 8, 16, 32, 64, 128, 256, 512}
     *
     * @param resources  Resources access
     * @param resourceID Resource ID
     */
    public Texture(final Resources resources, final int resourceID)
    {
        this(resources, resourceID, false);
    }

    /**
     * Create a new instance of Texture with resources.<br>
     * Resource suppose be an image with width and height in {1, 2, 4, 8, 16, 32, 64, 128, 256, 512}
     *
     * @param resources  Resources access
     * @param resourceID Resource ID
     * @param mutable    Indicates if texture ism mutable
     */
    public Texture(final Resources resources, final int resourceID, boolean mutable)
    {
        this.videoMemoryId = -1;
        this.needToRefresh = true;

        this.width = this.height = 0;

        final Options options = new Options();
        options.inScaled = false;

        this.setBitMap(BitmapFactory.decodeResource(resources, resourceID, options), mutable);
    }

    /**
     * Extract pixels from BitMap
     *
     * @param bitmap  BitMap to extract pixels
     * @param mutable Indicates if texture is mutable
     */
    private void setBitMap(final Bitmap bitmap, boolean mutable)
    {
        if (bitmap == null)
        {
            this.mutable = false;
            this.videoMemoryId = -1;
            this.needToRefresh = true;

            this.width = this.height = 1;

            this.pixels = UtilBuffer.createByteBuffer(4);

            this.pixels.put((byte) 0x80);
            this.pixels.put((byte) 0x80);
            this.pixels.put((byte) 0x80);
            this.pixels.put((byte) 0x80);

            return;
        }

        this.mutable = mutable;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
        this.pixels = UtilBuffer.createByteBuffer((this.width * this.height) << 2);

        if (this.mutable == false)
        {
            bitmap.copyPixelsToBuffer(this.pixels);
            this.pixels.position(0);
            bitmap.recycle();
        }
        else
        {
            if (bitmap.isMutable() == true)
            {
                this.bitmap = bitmap;
            }
            else
            {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                this.bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                final int pixels[] = new int[width * height];
                bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
                this.bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
                bitmap.recycle();
            }
        }

        System.gc();
        this.needToRefresh = true;
    }

    /**
     * Apply texture to Open GL
     *
     * @param gl Open GL link
     */
    void bind(final GL10 gl)
    {
        // If no video memory ID, create it
        if (this.videoMemoryId < 0)
        {
            UtilBuffer.TEMPORARY_INT_BUFFER.rewind();
            UtilBuffer.TEMPORARY_INT_BUFFER.put(1);
            UtilBuffer.TEMPORARY_INT_BUFFER.rewind();
            gl.glGenTextures(1, UtilBuffer.TEMPORARY_INT_BUFFER);
            UtilBuffer.TEMPORARY_INT_BUFFER.rewind();
            this.videoMemoryId = UtilBuffer.TEMPORARY_INT_BUFFER.get();
        }

        // If the texture need to be refresh
        if ((this.needToRefresh == true) && (this.pixels != null))
        {
            if (this.mutable == true)
            {
                this.pixels.clear();
                this.bitmap.copyPixelsToBuffer(this.pixels);
            }

            this.pixels.position(0);

            // Push pixels in video memory
            gl.glBindTexture(GL10.GL_TEXTURE_2D, this.videoMemoryId);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
            gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);
            gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, this.width, this.height, 0,
                            GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, this.pixels);

            if (this.mutable == false)
            {
                this.pixels.clear();
                this.pixels = null;
            }
        }
        // Draw the texture
        gl.glBindTexture(GL10.GL_TEXTURE_2D, this.videoMemoryId);
        this.needToRefresh = false;
    }

    /**
     * Texture width
     *
     * @return Texture width
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Texture height
     *
     * @return Texture height
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Indicates if texture is mutable
     *
     * @return {@code true} if texture is mutable
     */
    public boolean isMutable()
    {
        return this.mutable;
    }

    /**
     * Embed bitmap.<br>
     * Note : bitmap is {@code null} if texture not mutable
     *
     * @return Embed bitmap OR {@code null} if texture not mutable
     */
    public Bitmap getBitmap()
    {
        if (this.mutable == false)
        {
            return null;
        }

        return this.bitmap;
    }

    /**
     * Canvas for draw on texture.<br>
     * Note : canvas is {@code null} if texture not mutable
     *
     * @return Canvas for draw OR {@code null} if texture not mutable
     */
    public Canvas getCanvas()
    {
        if (this.mutable == false)
        {
            return null;
        }

        if (this.canvas == null)
        {
            this.canvas = new Canvas(this.bitmap);
        }

        return this.canvas;
    }

    /**
     * Paint for draw on texture.<br>
     * Note : paint is {@code null} if texture not mutable
     *
     * @return Paint for draw OR {@code null} if texture not mutable
     */
    public Paint getPaint()
    {
        if (this.mutable == false)
        {
            return null;
        }

        if (this.paint == null)
        {
            this.paint = new Paint();
        }

        return this.paint;
    }

    /**
     * Refresh last change to see them.<br>
     * Do nothing if texture not mutable
     */
    public void refresh()
    {
        if (this.mutable == true)
        {
            this.needToRefresh = true;
        }
    }

    /**
     * Make the texture not mutable.<br>
     * It will free some memory, but texture can't change after that
     */
    public void makeImmutable()
    {
        if (this.mutable == false)
        {
            return;
        }

        this.mutable = false;
        this.bitmap.copyPixelsToBuffer(this.pixels);
        this.pixels.position(0);
        this.bitmap.recycle();
        this.bitmap = null;
        this.canvas = null;
        this.paint = null;
        System.gc();
        this.needToRefresh = true;
    }
}