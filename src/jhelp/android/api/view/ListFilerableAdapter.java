package jhelp.android.api.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jhelp.android.api.Debug;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 * Generic adapter for easily add, remove, sort, show, hide elements in android.view.ListView. <br>
 * For show/hide elements, its based on a {@link Filter}
 * 
 * @author JHelp
 * @param <ELEMENT>
 *           Element inside adapter type
 */
public abstract class ListFilerableAdapter<ELEMENT>
      implements ListAdapter, FilterListener
{
   /**
    * Comparator used to compare 2 {@link ListFilerableAdapter.Filterable}
    * 
    * @author JHelp
    * @param <ELT>
    *           Element contained by {@link ListFilerableAdapter.Filterable} type
    */
   private static class ComparatorFilterable<ELT>
         implements Comparator<Filterable<ELT>>
   {
      /** Base comparator of element inside {@link ListFilerableAdapter.Filterable} */
      private final Comparator<ELT> comparator;

      /**
       * Create a new instance of ComparatorFilterable
       * 
       * @param comparator
       *           Base comparator of element inside {@link ListFilerableAdapter.Filterable}
       */
      ComparatorFilterable(final Comparator<ELT> comparator)
      {
         this.comparator = comparator;
      }

      /**
       * Compare 2 {@link ListFilerableAdapter.Filterable}, it returns :
       * <table>
       * <tr>
       * <th>&lt; 0</th>
       * <td>:</td>
       * <td>If the first parameter is before the second one</td>
       * </tr>
       * <tr>
       * <th>0</th>
       * <td>:</td>
       * <td>If 2 elements are in same place</td>
       * </tr>
       * <tr>
       * <th>&gt; 0</th>
       * <td>:</td>
       * <td>If first element is after the second one</td>
       * </tr>
       * </table>
       * <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param filterable1
       *           First parameter
       * @param filterable2
       *           Second parameter
       * @return Comparison result
       * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
       */
      @Override
      public int compare(final Filterable<ELT> filterable1, final Filterable<ELT> filterable2)
      {
         return this.comparator.compare(filterable1.element, filterable2.element);
      }
   }

   /**
    * !!!!element of the list that contains the element plus an index information
    * 
    * @author JHelp
    * @param <ELT>
    *           Element contains type
    */
   private static class Filterable<ELT>
   {
      /** Contained element */
      final ELT element;
      /** Index in visible list or {@link ListFilerableAdapter#NOT_VISIBLE} is element not visible */
      int       index;

      /**
       * Create a new instance of Filterable
       * 
       * @param element
       *           Contained element
       */
      Filterable(final ELT element)
      {
         this.element = element;
      }
   }

   /**
    * Indicates that an element not inside the list, see {@link ListFilerableAdapter#indexOf(Object)} or
    * {@link ListFilerableAdapter#visibleIndexOf(Object)}
    */
   public static final int                      NOT_IN_LIST = -1;
   /** Indicates that an element is inside the adapter, but not currently visible, see {@link #visibleIndexOf(Object)} */
   public static final int                      NOT_VISIBLE = -10;
   /** Number of current visible elements */
   private int                                  count;
   /** List fiterable change listeners */
   private final ArrayList<DataSetObserver>     dataSetObservers;
   /** Elements inside the adapter */
   private final ArrayList<Filterable<ELEMENT>> elements;
   /** Actual filter */
   private Filter<ELEMENT>                      filter;

   /**
    * Create a new instance of ListFilerableAdapter
    */
   public ListFilerableAdapter()
   {
      this.dataSetObservers = new ArrayList<DataSetObserver>();
      this.elements = new ArrayList<Filterable<ELEMENT>>();
      this.filter = null;
      this.count = 0;
   }

   /**
    * Refresh, if need, the elements visibility and count
    */
   private void refreshFilters()
   {
      if(this.count >= 0)
      {
         return;
      }

      int index = 0;

      for(final Filterable<ELEMENT> filterable : this.elements)
      {
         if(this.isFiltered(filterable.element) == true)
         {
            filterable.index = index;
            index++;
         }
         else
         {
            filterable.index = ListFilerableAdapter.NOT_VISIBLE;
         }
      }

      this.count = index;
   }

   /**
    * Alert listeners that data inside changed
    */
   protected final void fireDataSetChanged()
   {
      for(final DataSetObserver dataSetObserver : this.dataSetObservers)
      {
         dataSetObserver.onChanged();
      }
   }

   /**
    * Alert listeners that list have received a so big change, that we consider it like invalid and need be validate again
    */
   protected final void fireDataSetInvalidate()
   {
      for(final DataSetObserver dataSetObserver : this.dataSetObservers)
      {
         dataSetObserver.onInvalidated();
      }
   }

   /**
    * Obtain an element id.<br>
    * By default, it return the index given in parameters. To have an other behavior, override this method
    * 
    * @param element
    *           Element to get its ID
    * @param index
    *           Visible position
    * @return Element ID
    */
   protected long getItemID(final ELEMENT element, final int index)
   {
      return index;
   }

   /**
    * Obtain the type of view for an element. By default it returns always 0. To have an other behavior, override this method
    * 
    * @param element
    *           Element search its view type
    * @param index
    *           Element visible index
    * @return View type to use for this element
    */
   protected int getItemViewType(final ELEMENT element, final int index)
   {
      return 0;
   }

   /**
    * Obtain a view to use for an element
    * 
    * @param element
    *           Element to have its view
    * @param view
    *           Recycled view where draw. May be null
    * @param parent
    *           View parent
    * @return Created/updated view
    */
   protected abstract View getView(ELEMENT element, final View view, final ViewGroup parent);

   /**
    * Add one or several elements inside the adapter
    * 
    * @param elements
    *           Elements to add
    */
   public final void addElement(final ELEMENT... elements)
   {
      if(elements == null)
      {
         throw new NullPointerException("elements musn't be null");
      }

      for(final ELEMENT element : elements)
      {
         if(element == null)
         {
            throw new NullPointerException(Debug.createMessage("All element of the list musn't be null : ", elements));
         }

         this.elements.add(new Filterable<ELEMENT>(element));
      }

      this.count = -1;
      this.fireDataSetChanged();
   }

   /**
    * Indicates if all elements are enabled or not. By default it always return true. Override to do a enable/isable strategy <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return {@code true} if all item are enabled
    * @see android.widget.ListAdapter#areAllItemsEnabled()
    */
   @Override
   public boolean areAllItemsEnabled()
   {
      return true;
   }

   /**
    * Destroy the adapter properly.<br>
    * Call it when finish usage of this list
    */
   public final void destroy()
   {
      if(this.filter != null)
      {
         this.filter.unregisterFilterListener(this);
      }

      this.filter = null;
      this.dataSetObservers.clear();
      this.elements.clear();
   }

   /**
    * Called when current filter chnaged <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param filter
    *           Filter that changed
    * @see jhelp.android.api.view.FilterListener#filterChanged(jhelp.android.api.view.Filter)
    */
   @Override
   public final void filterChanged(final Filter<?> filter)
   {
      this.count = -1;

      this.fireDataSetInvalidate();
   }

   /**
    * Number of visible elements <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Number of visible elements
    * @see android.widget.Adapter#getCount()
    */
   @Override
   public final int getCount()
   {
      this.refreshFilters();

      return this.count;
   }

   /**
    * Obtain an element of the adapter
    * 
    * @param index
    *           Index inside the adapter
    * @return Required element
    */
   public final ELEMENT getElement(final int index)
   {
      return this.elements.get(index).element;
   }

   /**
    * Obtain an element inside the visible ones <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param index
    *           Visible index
    * @return Required element
    * @see android.widget.Adapter#getItem(int)
    */
   @Override
   public final Object getItem(final int index)
   {
      this.refreshFilters();

      for(final Filterable<ELEMENT> filterable : this.elements)
      {
         if(filterable.index == index)
         {
            return filterable.element;
         }
      }

      return null;
   }

   /**
    * Obtain visible element ID <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param index
    *           Visible index
    * @return Item ID
    * @see android.widget.Adapter#getItemId(int)
    */
   @Override
   public final long getItemId(final int index)
   {
      @SuppressWarnings("unchecked")
      final ELEMENT element = (ELEMENT) this.getItem(index);

      if(element != null)
      {
         return this.getItemID(element, index);
      }

      return index;
   }

   /**
    * Obtain the view type of a visible element <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param index
    *           Visible index
    * @return Element view type
    * @see android.widget.Adapter#getItemViewType(int)
    */
   @Override
   public final int getItemViewType(final int index)
   {
      @SuppressWarnings("unchecked")
      final ELEMENT element = (ELEMENT) this.getItem(index);

      if(element != null)
      {
         return this.getItemViewType(element, index);
      }

      return 0;
   }

   /**
    * Number of elements inside the adapter
    * 
    * @return Number of elements inside the adapter
    */
   public final int getNumberOfElements()
   {
      return this.elements.size();
   }

   /**
    * Obtain view for a visible element <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param index
    *           Visible index
    * @param view
    *           View to recycle
    * @param parent
    *           View parent
    * @return Created/updated view
    * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
    */
   @Override
   public final View getView(final int index, final View view, final ViewGroup parent)
   {
      @SuppressWarnings("unchecked")
      final ELEMENT element = (ELEMENT) this.getItem(index);

      return this.getView(element, view, parent);
   }

   /**
    * Number of different type that list may show. By default it returns 1. Override it to change this <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return Number of different type
    * @see android.widget.Adapter#getViewTypeCount()
    */
   @Override
   public int getViewTypeCount()
   {
      return 1;
   }

   /**
    * Indicates if IDS are stable. Here its false, because they change if filter changeTODO Explains what does the method
    * hasStableIds in jhelp.android.api.view [JHelpAndroidAPI] <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return {@code false}
    * @see android.widget.Adapter#hasStableIds()
    */
   @Override
   public final boolean hasStableIds()
   {
      return false;
   }

   /**
    * Obtain the index of an element inside the adapter
    * 
    * @param element
    *           Element tested
    * @return Element index or {@link #NOT_IN_LIST} if the element not found
    */
   public final int indexOf(final ELEMENT element)
   {
      final int length = this.elements.size();
      Filterable<ELEMENT> filterable;

      for(int index = 0; index < length; index++)
      {
         filterable = this.elements.get(index);

         if(filterable.element.equals(element) == true)
         {
            return index;
         }
      }

      return ListFilerableAdapter.NOT_IN_LIST;
   }

   /**
    * Indicates if list is visibily empty <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return {@code true} if no element are shown
    * @see android.widget.Adapter#isEmpty()
    */
   @Override
   public final boolean isEmpty()
   {
      this.refreshFilters();

      return this.count == 0;
   }

   /**
    * Indicates is an element is enable. By default it return true. Override this method to enable/disable elements <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param position
    *           Visible index
    * @return {@code true} if element enable
    * @see android.widget.ListAdapter#isEnabled(int)
    */
   @Override
   public boolean isEnabled(final int position)
   {
      return true;
   }

   /**
    * Test if an element pass the current filter
    * 
    * @param element
    *           Element tested
    * @return {@code true} if element pass
    */
   public final boolean isFiltered(final ELEMENT element)
   {
      if(this.filter == null)
      {
         return true;
      }

      return this.filter.isFiltered(element);
   }

   /**
    * Register a listener to be alert when data changed or list become invalid <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param dataSetObserver
    *           Listener to register
    * @see android.widget.Adapter#registerDataSetObserver(android.database.DataSetObserver)
    */
   @Override
   public final void registerDataSetObserver(final DataSetObserver dataSetObserver)
   {
      if(dataSetObserver == null)
      {
         throw new NullPointerException("dataSetObserver musn't be null");
      }

      this.dataSetObservers.add(dataSetObserver);
   }

   /**
    * Remove an element in adapter
    * 
    * @param element
    *           Element to remove
    */
   public final void removeElement(final ELEMENT element)
   {
      Filterable<ELEMENT> filterable = null;

      for(final Filterable<ELEMENT> f : this.elements)
      {
         if(f.element.equals(element) == true)
         {
            filterable = f;

            break;
         }
      }

      if(filterable == null)
      {
         return;
      }

      this.elements.remove(filterable);
      this.count = -1;
      this.fireDataSetChanged();
   }

   /**
    * Remove an element of adapter at indicated index
    * 
    * @param index
    *           Element index
    */
   public final void removeElement(final int index)
   {
      this.elements.remove(index);
      this.count = -1;
      this.fireDataSetChanged();
   }

   /**
    * Change the current filter
    * 
    * @param filter
    *           New filter. You can use {@code null} to indicates their are no filter, that is to say all elements becomes
    *           visible
    */
   public final void setFilter(final Filter<ELEMENT> filter)
   {
      if(this.filter != null)
      {
         this.filter.unregisterFilterListener(this);
      }

      if(filter != null)
      {
         filter.registerFilterListener(this);
      }

      this.filter = filter;
      this.count = -1;

      this.fireDataSetInvalidate();
   }

   /**
    * Sort elements
    * 
    * @param comparator
    *           Comparator to use
    */
   public final void sortElements(final Comparator<ELEMENT> comparator)
   {
      if(comparator == null)
      {
         throw new NullPointerException("comparator musn't be null");
      }

      Collections.sort(this.elements, new ComparatorFilterable<ELEMENT>(comparator));
      this.count = -1;

      this.fireDataSetInvalidate();
   }

   /**
    * Unregister a listener. The listner will be no more alert on data changed or list invalidate <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param dataSetObserver
    *           Listener to unregister
    * @see android.widget.Adapter#unregisterDataSetObserver(android.database.DataSetObserver)
    */
   @Override
   public final void unregisterDataSetObserver(final DataSetObserver dataSetObserver)
   {
      this.dataSetObservers.remove(dataSetObserver);
   }

   /**
    * Obtain the visibility index of an element
    * 
    * @param element
    *           Element tested
    * @return Element visiblity index or {@link #NOT_VISIBLE} if the element is not currently visible or {@link #NOT_IN_LIST} if
    *         the element not inisde the adaptr
    */
   public final int visibleIndexOf(final ELEMENT element)
   {
      for(final Filterable<ELEMENT> filterable : this.elements)
      {
         if(filterable.element.equals(element) == true)
         {
            return filterable.index;
         }
      }

      return ListFilerableAdapter.NOT_IN_LIST;
   }
}