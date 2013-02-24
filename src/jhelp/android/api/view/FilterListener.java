package jhelp.android.api.view;

/**
 * Listener to know if a {@link Filter} changed
 * 
 * @author JHelp
 */
public interface FilterListener
{
   /**
    * Called when filter changed
    * 
    * @param filter
    *           Filter that changed
    */
   public void filterChanged(Filter<?> filter);
}