package jhelp.android.api.text2speech;

import android.os.Parcel;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech.EngineInfo;

/**
 * Speecher engine information
 * 
 * @author JHelp
 */
public class SpeecherInfo
      implements Parcelable
{
   /** Creator for parcelable */
   public static final Creator<SpeecherInfo> CREATOR = new Creator<SpeecherInfo>()
                                                     {
                                                        /**
                                                         * Create an information from parcel <br>
                                                         * <br>
                                                         * <b>Parent documentation:</b><br>
                                                         * {@inheritDoc}
                                                         * 
                                                         * @param source
                                                         *           Parcel source
                                                         * @return Information created
                                                         * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
                                                         */
                                                        @Override
                                                        public SpeecherInfo createFromParcel(final Parcel source)
                                                        {
                                                           return new SpeecherInfo(source);
                                                        }

                                                        /**
                                                         * Creat an array of sinformation <br>
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
                                                        public SpeecherInfo[] newArray(final int size)
                                                        {
                                                           return new SpeecherInfo[size];
                                                        }
                                                     };
   /** Carried information */
   private final EngineInfo                  engineInfo;

   /**
    * Create a new instance of SpeecherInfo
    * 
    * @param engineInfo
    *           Engine description
    */
   public SpeecherInfo(final EngineInfo engineInfo)
   {
      if(engineInfo == null)
      {
         throw new NullPointerException("engineInfo musn't be null");
      }

      this.engineInfo = engineInfo;
   }

   /**
    * Create a new instance of SpeecherInfo
    * 
    * @param source
    *           Parcel to parse
    */
   public SpeecherInfo(final Parcel source)
   {
      this.engineInfo = new EngineInfo();

      this.engineInfo.name = source.readString();
      this.engineInfo.label = source.readString();
      this.engineInfo.icon = source.readInt();
   }

   /**
    * Content description <br>
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
    * Embed engine information
    * 
    * @return Embed engine information
    */
   public EngineInfo getEngineInfo()
   {
      return this.engineInfo;
   }

   /**
    * Write engine information to parcel <br>
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
      destination.writeString(this.engineInfo.name);
      destination.writeString(this.engineInfo.label);
      destination.writeInt(this.engineInfo.icon);
   }
}