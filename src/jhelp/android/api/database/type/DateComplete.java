package jhelp.android.api.database.type;

import java.util.GregorianCalendar;

/**
 * Complete date year/month/day hour:minute:second.millsenconds for store in database
 * Created by jhelp on 21/11/15.
 */
public class DateComplete extends DatabaseType
{
    /**
     * The date
     */
    private final GregorianCalendar gregorianCalendar;

    /**
     * Create date initialized at now
     */
    public DateComplete()
    {
        this.gregorianCalendar = new GregorianCalendar();
    }

    /**
     * Year
     *
     * @return Tear
     */
    public int getYear()
    {
        return this.gregorianCalendar.get(GregorianCalendar.YEAR);
    }

    /**
     * Change year
     *
     * @param year New year
     */
    public void setYear(int year)
    {
        this.gregorianCalendar.set(GregorianCalendar.YEAR, year);
    }

    /**
     * Month
     *
     * @return Month
     */
    public Month getMonth()
    {
        return Month.getMonth(this.gregorianCalendar.get(GregorianCalendar.MONTH));
    }

    /**
     * Change month
     *
     * @param month New month
     */
    public void setMonth(Month month)
    {
        this.gregorianCalendar.set(GregorianCalendar.MONTH, month.getMonth());
    }

    /**
     * Day
     *
     * @return Day
     */
    public int getDay()
    {
        return this.gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * Maximum value can take a day depends on current month and current year
     *
     * @return Maximum value can take a day
     */
    public int getCurrentMaximumDay()
    {
        return this.gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * Change day.<br>
     * Day MUST be between 1 and {@link #getCurrentMaximumDay()}
     *
     * @param day New day
     * @throws IllegalArgumentException if day invalid
     */
    public void setDay(int day)
    {
        int maximum = this.gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        if (day < 1 || day > maximum)
        {
            throw new IllegalArgumentException("day MUST be in [1, " + maximum + "] not " + day);
        }

        this.gregorianCalendar.set(GregorianCalendar.DAY_OF_MONTH, day);
    }

    /**
     * Change only the day
     *
     * @param year  Year
     * @param month Month
     * @param day   Day
     */
    public void setDate(int year, Month month, int day)
    {
        this.gregorianCalendar.set(year, month.getMonth(), day);
    }

    /**
     * Hour
     *
     * @return Hour
     */
    public int getHour()
    {
        return this.gregorianCalendar.get(GregorianCalendar.HOUR_OF_DAY);
    }

    /**
     * Change hour
     *
     * @param hour New hour
     * @throws IllegalArgumentException if hour invalid
     */
    public void setHour(int hour)
    {
        if (hour < 0 || hour >= 24)
        {
            throw new IllegalArgumentException("hour MUST be in [0, 23], not " + hour);
        }

        this.gregorianCalendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
    }

    /**
     * Minute
     *
     * @return Minute
     */
    public int getMinute()
    {
        return this.gregorianCalendar.get(GregorianCalendar.MINUTE);
    }

    /**
     * Change minute
     *
     * @param minute New minute
     * @throws IllegalArgumentException if minute invalid
     */
    public void setMinute(int minute)
    {
        if (minute < 0 || minute >= 60)
        {
            throw new IllegalArgumentException("minute MUST be in [0, 59], not " + minute);
        }

        this.gregorianCalendar.set(GregorianCalendar.MINUTE, minute);
    }

    /**
     * Second
     *
     * @return Second
     */
    public int getSecond()
    {
        return this.gregorianCalendar.get(GregorianCalendar.SECOND);
    }

    /**
     * Change second
     *
     * @param second New second
     * @throws IllegalArgumentException if second invalid
     */
    public void setSecond(int second)
    {
        if (second < 0 || second >= 60)
        {
            throw new IllegalArgumentException("second MUST be in [0, 59], not " + second);
        }

        this.gregorianCalendar.set(GregorianCalendar.SECOND, second);
    }

    /**
     * Millisecond
     *
     * @return Millsecond
     */
    public int getMillisecond()
    {
        return this.gregorianCalendar.get(GregorianCalendar.MILLISECOND);
    }

    /**
     * Change millisecond
     *
     * @param millisecond New millisecond
     * @throws IllegalArgumentException if millisecond invalid
     */
    public void setMillisecond(int millisecond)
    {
        if (millisecond < 0 || millisecond >= 1000)
        {
            throw new IllegalArgumentException(
                    "millisecond MUST be in [0, 999], not " + millisecond);
        }

        this.gregorianCalendar.set(GregorianCalendar.MILLISECOND, millisecond);
    }

    /**
     * Change the date and the time
     *
     * @param year   Year
     * @param month  Month
     * @param day    Day
     * @param hour   Hour
     * @param minute Minute
     * @param second Second
     */
    public void setDate(int year, Month month, int day, int hour, int minute, int second)
    {
        this.gregorianCalendar.set(year, month.getMonth(), day, hour, minute, second);
    }

    /**
     * Parse String to fill the date
     *
     * @param serialized Serialized String to parse
     */
    @Override
    public void parse(String serialized)
    {
        this.gregorianCalendar.setTimeInMillis(Long.parseLong(serialized));
    }

    /**
     * Serialize the date to String
     *
     * @return Serialized String
     */
    @Override
    public String serialize()
    {
        return String.valueOf(this.gregorianCalendar.getTimeInMillis());
    }
}