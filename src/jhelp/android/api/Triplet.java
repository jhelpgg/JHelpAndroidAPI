package jhelp.android.api;

/**
 * A triplet of elements
 * 
 * @author JHelp
 * @param <FIRST>
 *           First element type
 * @param <SECOND>
 *           Second element type
 * @param <THIRD>
 *           Third element type
 */
public class Triplet<FIRST, SECOND, THIRD>
{
   /** First element */
   public final FIRST  first;
   /** Second element */
   public final SECOND second;
   /** Third element */
   public final THIRD  third;

   /**
    * Create a new instance of Triplet
    * 
    * @param first
    *           First element
    * @param second
    *           Second element
    * @param third
    *           Third element
    */
   public Triplet(final FIRST first, final SECOND second, final THIRD third)
   {
      this.first = first;
      this.second = second;
      this.third = third;
   }

   /**
    * Indicates if an object is equals to this triplet <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param object
    *           Tested object
    * @return {@code true} if equals
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals(final Object object)
   {
      if(object == null)
      {
         return false;
      }

      if(this == object)
      {
         return true;
      }

      if(this.getClass().equals(object.getClass()) == false)
      {
         return false;
      }

      @SuppressWarnings("unchecked")
      final Triplet<FIRST, SECOND, THIRD> triplet = (Triplet<FIRST, SECOND, THIRD>) object;

      if(this.first == null)
      {
         if(triplet.first != null)
         {
            return false;
         }
      }
      else if(this.first.equals(triplet.first) == false)
      {
         return false;
      }

      if(this.second == null)
      {
         if(triplet.second != null)
         {
            return false;
         }
      }
      else if(this.second.equals(triplet.second) == false)
      {
         return false;
      }

      if(this.third == null)
      {
         if(triplet.third != null)
         {
            return false;
         }
      }
      else if(this.third.equals(triplet.third) == false)
      {
         return false;
      }

      return true;
   }

   /**
    * String representation of the triplet <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Triplet string representation
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString()
   {
      return Debug.createMessage("Triplet {", this.first, ", ", this.second, ", ", this.third, "}");
   }
}