package jhelp.android.api.database.type;

import java.util.GregorianCalendar;

/**
 * Represents a date : year/month/day for store in database
 * Created by jhelp on 21/11/15.
 */
public class Date extends DatabaseType
{
    /**
     * Stored date
     */
    private final GregorianCalendar gregorianCalendar;

    /**
     * Create date at today
     */
    public Date()
    {
        this.gregorianCalendar = new GregorianCalendar();
    }

    /**
     * Year
     *
     * @return Year
     */
    public int getYear()
    {
        return this.gregorianCalendar.get(GregorianCalendar.YEAR);
    }

    /**
     * Cahnge the year
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
     * Cahnge the month
     *
     * @param month New month
     */
    public void setMonth(Month month)
    {
        this.gregorianCalendar.set(GregorianCalendar.MONTH, month.getMonth());
    }

    /**
     * The day
     *
     * @return The day
     */
    public int getDay()
    {
        return this.gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * Actual maximum value for day (depends on month and year)
     *
     * @return Actual maximum value for day
     */
    public int getCurrentMaximumDay()
    {
        return this.gregorianCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    }

    /**
     * Change the day.<br>
     * Day MUST be between 1 and {@link #getCurrentMaximumDay()}
     *
     * @param day New day
     * @throws IllegalArgumentException If day is invalid
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
     * Change the date
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
     * Parse a serialized string to make the date
     *
     * @param serialized Serialized String to parse
     */
    @Override
    public void parse(String serialized)
    {
        this.gregorianCalendar.setTimeInMillis(Long.parseLong(serialized));
    }

    /**
     * Serialize date to String
     *
     * @return Serialized String
     */
    @Override
    public String serialize()
    {
        return String.valueOf(this.gregorianCalendar.getTimeInMillis());
    }
}