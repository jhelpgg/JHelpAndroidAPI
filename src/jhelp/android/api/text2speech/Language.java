package jhelp.android.api.text2speech;

import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Language description
 * 
 * @author JHelp
 */
public class Language
      implements Parcelable
{
   /** Creator for parcelable */
   public static final Creator<Language> CREATOR = new Creator<Language>()
                                                 {
                                                    /**
                                                     * Create language information from parcel <br>
                                                     * <br>
                                                     * <b>Parent documentation:</b><br>
                                                     * {@inheritDoc}
                                                     * 
                                                     * @param source
                                                     *           Parcel to parse
                                                     * @return Created launguage
                                                     * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
                                                     */
                                                    @Override
                                                    public Language createFromParcel(final Parcel source)
                                                    {
                                                       return new Language(source);
                                                    }

                                                    /**
                                                     * Create an array of language <br>
                                                     * <br>
                                                     * <b>Parent documentation:</b><br>
                                                     * {@inheritDoc}
                                                     * 
                                                     * @param size
                                                     *           Array size
                                                     * @return Created array
                                                     * @see android.os.Parcelable.Creator#newArray(int)
                                                     */
                                                    @Override
                                                    public Language[] newArray(final int size)
                                                    {
                                                       return new Language[size];
                                                    }
                                                 };
   /** Embed locale */
   private final Locale                  locale;

   /**
    * Create a new instance of Language
    * 
    * @param locale
    *           Language information
    */
   public Language(final Locale locale)
   {
      if(locale == null)
      {
         throw new NullPointerException("locale musn't be null");
      }

      this.locale = locale;
   }

   /**
    * Create a new instance of Language
    * 
    * @param source
    *           Parcel to parse
    */
   public Language(final Parcel source)
   {
      final String language = source.readString();
      final String country = source.readString();
      final String variant = source.readString();

      this.locale = new Locale(language, country, variant);
   }

   /**
    * Describe content <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @return 0
    * @see android.os.Parcelable#describeContents()
    */
   @Override
   public int describeContents()
   {
      return 0;
   }

   /**
    * Embed language
    * 
    * @return Embed language
    */
   public Locale getLocale()
   {
      return this.locale;
   }

   /**
    * Write information to parcel <br>
    * <br>
    * <b>Parent documentation:</b><br>
    * {@inheritDoc}
    * 
    * @param destination
    *           Parcel where write
    * @param flags
    *           Flags
    * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
    */
   @Override
   public void writeToParcel(final Parcel destination, final int flags)
   {
      destination.writeString(this.locale.getLanguage());
      destination.writeString(this.locale.getCountry());
      destination.writeString(this.locale.getVariant());
   }
}