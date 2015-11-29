package jhelp.android.api.engine;

import jhelp.android.api.engine.util.ArrayInt;
import android.util.Pair;
import android.util.SparseArray;

/**
 * 3D animation.<br>
 * Animation is composed of frames, that can be steps to reach at precise time
 * 
 * @author JHelp
 */
public class Animation
{
   /** Animation FPS */
   private final int                     fps;
   /** Frames to play */
   private final SparseArray<Position3D> frames;
   /** Frames indexes */
   private final ArrayInt                indexes;
   /** Indicates if animation is playing */
   private boolean                       isPlaying;
   /** Animated node */
   private final Node3D                  node;
   /** Start position */
   private Position3D                    start;
   /** Start time */
   private long                          time;

   /**
    * Create a new instance of Animation at 25 FPS
    * 
    * @param node
    *           Node to animate
    */
   public Animation(final Node3D node)
   {
      this(node, 25);
   }

   /**
    * Create a new instance of Animation
    * 
    * @param node
    *           Node to animate
    * @param fps
    *           Animation FPS
    */
   public Animation(final Node3D node, final int fps)
   {
      if(node == null)
      {
         throw new NullPointerException("node musn't be null");
      }

      this.node = node;
      this.isPlaying = false;
      this.frames = new SparseArray<Position3D>();
      this.indexes = new ArrayInt();
      this.fps = Math.min(100, Math.max(1, fps));
   }

   /**
    * Animate the animation
    * 
    * @return {@code true} if animation have to be continue. {@code false} if animation is finish
    */
   boolean animate()
   {
      if(this.isPlaying == false)
      {
         return false;
      }

      final long relative = (System.currentTimeMillis() - this.time);
      int frame = (int) ((relative * this.fps) / 1000);
      final int size = this.indexes.getSize();
      Position3D p1 = this.start;
      Position3D p2 = this.start;

      if(size == 0)
      {
         this.isPlaying = false;

         return false;
      }

      final Pair<Integer, Integer> interval = this.indexes.getIntervalSupposeSorted(frame);

      if(interval.second == -1)
      {
         this.isPlaying = true;

         p1 = this.start;
         p2 = this.frames.get(this.indexes.getInteger(0));

         final int dist = this.indexes.getInteger(0);
         final int inv = dist - frame;

         this.node.position.x = ((p1.x * inv) + (p2.x * frame)) / dist;
         this.node.position.y = ((p1.y * inv) + (p2.y * frame)) / dist;
         this.node.position.z = ((p1.z * inv) + (p2.z * frame)) / dist;

         this.node.position.angleX = ((p1.angleX * inv) + (p2.angleX * frame)) / dist;
         this.node.position.angleY = ((p1.angleY * inv) + (p2.angleY * frame)) / dist;
         this.node.position.angleZ = ((p1.angleZ * inv) + (p2.angleZ * frame)) / dist;

         this.node.position.scaleX = ((p1.scaleX * inv) + (p2.scaleX * frame)) / dist;
         this.node.position.scaleY = ((p1.scaleY * inv) + (p2.scaleY * frame)) / dist;
         this.node.position.scaleZ = ((p1.scaleZ * inv) + (p2.scaleZ * frame)) / dist;

         return true;
      }

      if(interval.first == size)
      {
         this.isPlaying = false;

         this.node.position.copy(this.frames.get(this.indexes.getInteger(size - 1)));

         return false;
      }

      if(interval.first == interval.second)
      {
         this.isPlaying = true;

         this.node.position.copy(this.frames.get(this.indexes.getInteger(interval.first)));

         return true;
      }

      this.isPlaying = true;

      final int f = this.indexes.getInteger(interval.first);
      final int s = this.indexes.getInteger(interval.second);

      p1 = this.frames.get(f);
      p2 = this.frames.get(s);

      frame -= f;
      final int dist = s - f;
      final int inv = dist - frame;

      this.node.position.x = ((p1.x * inv) + (p2.x * frame)) / dist;
      this.node.position.y = ((p1.y * inv) + (p2.y * frame)) / dist;
      this.node.position.z = ((p1.z * inv) + (p2.z * frame)) / dist;

      this.node.position.angleX = ((p1.angleX * inv) + (p2.angleX * frame)) / dist;
      this.node.position.angleY = ((p1.angleY * inv) + (p2.angleY * frame)) / dist;
      this.node.position.angleZ = ((p1.angleZ * inv) + (p2.angleZ * frame)) / dist;

      this.node.position.scaleX = ((p1.scaleX * inv) + (p2.scaleX * frame)) / dist;
      this.node.position.scaleY = ((p1.scaleY * inv) + (p2.scaleY * frame)) / dist;
      this.node.position.scaleZ = ((p1.scaleZ * inv) + (p2.scaleZ * frame)) / dist;

      return true;
   }

   /**
    * Start the animation
    */
   void start()
   {
      if(this.isPlaying == true)
      {
         return;
      }

      this.isPlaying = true;
      this.time = System.currentTimeMillis();
      this.start = this.node.position.copy();
   }

   /**
    * Add frame to animation
    * 
    * @param frame
    *           Frame
    * @param position3d
    *           Position
    */
   public void addFrame(final int frame, final Position3D position3d)
   {
      if(this.isPlaying == true)
      {
         throw new IllegalStateException("Can't modify animation while playing");
      }

      if(frame < 0)
      {
         throw new IllegalArgumentException("Frame must be >=0, not " + frame);
      }

      if(position3d == null)
      {
         throw new NullPointerException("position3d musn't be null");
      }

      this.indexes.add(frame);
      this.indexes.sortUniq();

      this.frames.put(frame, position3d);
   }

   /**
    * Indicates if animation is playing
    * 
    * @return {@code true} if animation is playing
    */
   public boolean isPlaying()
   {
      return this.isPlaying;
   }

   public void stop()
   {
      this.isPlaying = false;
   }
}