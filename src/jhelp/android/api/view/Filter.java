package jhelp.android.api.view;

import java.util.ArrayList;

/**
 * Filter for decide witch elements are visible or hide in {@link ListFilerableAdapter}
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Element type
 */
public abstract class Filter<ELEMENT>
{
   /** Listeners of filter change */
   private final ArrayList<FilterListener> filterListeners;

   /**
    * Create a new instance of Filter
    */
   public Filter()
   {
      this.filterListeners = new ArrayList<FilterListener>();
   }

   /**
    * Call this method in implementation of filter to indicates to listeners that the filtr has changed, so its time to refresh
    */
   protected final void fireFilterChanged()
   {
      for(final FilterListener filterListener : this.filterListeners)
      {
         filterListener.filterChanged(this);
      }
   }

   /**
    * This method is called when an element is tested, to know if the element pass the filter or not
    * 
    * @param element
    *           Tested element
    * @return {@code true} if the element pass the filter
    */
   public abstract boolean isFiltered(ELEMENT element);

   /**
    * Register a lister to be alert when filter changed
    * 
    * @param filterListener
    *           Filter to alert
    */
   public final void registerFilterListener(final FilterListener filterListener)
   {
      if(filterListener == null)
      {
         throw new NullPointerException("filerListener musn't be null");
      }

      this.filterListeners.add(filterListener);
   }

   /**
    * Unregister a listener. The listener will be no longer alert on filter changed
    * 
    * @param filterListener
    *           Filter to unregister
    */
   public final void unregisterFilterListener(final FilterListener filterListener)
   {
      this.filterListeners.remove(filterListener);
   }
}