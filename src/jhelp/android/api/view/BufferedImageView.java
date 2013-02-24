package jhelp.android.api.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * View with a buffered image inside, where can draw with on using {@link #getCanvasBuffer()} and {@link #getPaintBuffer()}. To
 * see last changes, refresh with {@link #refreshImageView()}.<br>
 * It is possible, if the layout where the view is allowed it, to change the view size with
 * {@link #setImageViewDimension(int, int)}.<br>
 * To know the real image size use : {@link #getImageViewWidth()} and {@link #getImageViewHeight()}
 * 
 * @author JHelp
 */
public class BufferedImageView
      extends View
{
   /** Buffered embed image */
   private Bitmap bufferedImage;
   /** Canvas link to the image */
   private Canvas canvasBuffer;
   /** Image height */
   private int    imageViewHeight;
   /** Image width */
   private int    imageViewWidth;
   /** Paint link to the image */
   private Paint  paintBuffer;

   /**
    * Create a new instance of BufferedImageView
    * 
    * @param context
    *           Context parent
    */
   public BufferedImageView(final Context context)
   {
      super(context);

      this.initializeImageView();
   }

   /**
    * Create a new instance of BufferedImageView
    * 
    * @param context
    *           Context parent
    * @param attrs
    *           Attributes
    */
   public BufferedImageView(final Context context, final AttributeSet attrs)
   {
      super(context, attrs);

      this.initializeImageView();
   }

   /**
    * Create a new instance of BufferedImageView
    * 
    * @param context
    *           Context parent
    * @param attrs
    *           Attributes
    * @param defStyle
    *           Style ID
    */
   public BufferedImageView(final Context context, final AttributeSet attrs, final int defStyle)
   {
      super(context, attrs, defStyle);

      this.initializeImageView();
   }

   /**
    * Initialize the image
    */
   private void initializeImageView()
   {
      this.imageViewWidth = 256;
      this.imageViewHeight = 256;
      this.paintBuffer = new Paint(Paint.ANTI_ALIAS_FLAG);

      this.uptadeBufferedImage(false);
   }

   /**
    * Update the image if size changed
    * 
    * @param forceRefresh
    *           Indicates if a refresh is need immediately after the operation
    */
   private void uptadeBufferedImage(final boolean forceRefresh)
   {
      final Bitmap bitmap = Bitmap.createBitmap(this.imageViewWidth, this.imageViewHeight, Bitmap.Config.ARGB_8888);
      this.canvasBuffer = new Canvas(bitmap);

      if(this.bufferedImage != null)
      {
         this.canvasBuffer.drawBitmap(this.bufferedImage, new Rect(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight()), new Rect(0, 0, this.imageViewWidth, this.imageViewHeight), this.paintBuffer);

         this.bufferedImage.recycle();
      }

      this.bufferedImage = bitmap;

      if(forceRefresh == true)
      {
         this.refreshImageView();
      }
   }

   /**
    * Draw the image <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param canvas
    *           Canvas to use
    * @see android.view.View#onDraw(android.graphics.Canvas)
    */
   @Override
   protected final void onDraw(final Canvas canvas)
   {
      canvas.drawBitmap(this.bufferedImage, 0, 0, this.paintBuffer);
   }

   /**
    * Called when image is mesured <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param widthMeasureSpec
    *           Width measure specification
    * @param heightMeasureSpec
    *           Height measure specification
    * @see android.view.View#onMeasure(int, int)
    */
   @Override
   protected final void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
   {
      int width = this.imageViewWidth;
      int height = this.imageViewHeight;

      switch(MeasureSpec.getMode(widthMeasureSpec))
      {
         case MeasureSpec.AT_MOST:
            width = Math.min(MeasureSpec.getSize(widthMeasureSpec), Math.max(this.getSuggestedMinimumWidth(), width));
         break;
         case MeasureSpec.EXACTLY:
            width = MeasureSpec.getSize(widthMeasureSpec);
         break;
         case MeasureSpec.UNSPECIFIED:
            width = Math.max(this.getSuggestedMinimumWidth(), width);
         break;
      }

      switch(MeasureSpec.getMode(heightMeasureSpec))
      {
         case MeasureSpec.AT_MOST:
            height = Math.min(MeasureSpec.getSize(heightMeasureSpec), Math.max(this.getSuggestedMinimumHeight(), height));
         break;
         case MeasureSpec.EXACTLY:
            height = MeasureSpec.getSize(heightMeasureSpec);
         break;
         case MeasureSpec.UNSPECIFIED:
            height = Math.max(this.getSuggestedMinimumHeight(), height);
         break;
      }

      this.setMeasuredDimension(width, height);
   }

   /**
    * Called when size cof view changed <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param w
    *           New width
    * @param h
    *           New height
    * @param oldw
    *           Old width
    * @param oldh
    *           Old height
    * @see android.view.View#onSizeChanged(int, int, int, int)
    */
   @Override
   protected final void onSizeChanged(final int w, final int h, final int oldw, final int oldh)
   {
      if((w != this.imageViewWidth) || (h != this.imageViewHeight))
      {
         this.imageViewWidth = w;
         this.imageViewHeight = h;

         this.uptadeBufferedImage(false);
      }
   }

   /**
    * Canvas to use for draw on image
    * 
    * @return Canvas to use for draw on image
    */
   public final Canvas getCanvasBuffer()
   {
      return this.canvasBuffer;
   }

   /**
    * Image height
    * 
    * @return Image height
    */
   public final int getImageViewHeight()
   {
      return this.imageViewHeight;
   }

   /**
    * Image width
    * 
    * @return Image width
    */
   public final int getImageViewWidth()
   {
      return this.imageViewWidth;
   }

   /**
    * Paint to use for draw on the image
    * 
    * @return Paint to use for draw on the image
    */
   public final Paint getPaintBuffer()
   {
      return this.paintBuffer;
   }

   /**
    * Obtain an image color
    * 
    * @param x
    *           Pixel X
    * @param y
    *           Pixel Y
    * @return Pixel color or 0 if (x, y) is outside the image
    */
   public final int pickColor(final int x, final int y)
   {
      if(this.bufferedImage == null)
      {
         return 0;
      }

      if((x < 0) || (y < 0) || (x >= this.imageViewWidth) || (y >= this.imageViewHeight))
      {
         return 0;
      }

      return this.bufferedImage.getPixel(x, y);
   }

   /**
    * Refresh the image to see the last changes
    */
   public final void refreshImageView()
   {
      this.postInvalidate();
      this.forceLayout();
      this.requestLayout();
   }

   /**
    * Change, if allowed by the layout where the image, the image size
    * 
    * @param imageViewWidth
    *           Desired width
    * @param imageViewHeight
    *           Desired height
    */
   public final void setImageViewDimension(final int imageViewWidth, final int imageViewHeight)
   {
      this.imageViewWidth = imageViewWidth;
      this.imageViewHeight = imageViewHeight;

      this.uptadeBufferedImage(true);
   }
}