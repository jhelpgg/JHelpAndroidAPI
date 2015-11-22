package jhelp.android.api.database.type;

import java.util.GregorianCalendar;

/**
 * A month
 * Created by jhelp on 22/11/15.
 */
public enum Month
{
    /*JANUARY**/
    JANUARY(GregorianCalendar.JANUARY),
    /**
     * FEBRUARY
     */
    FEBRUARY(GregorianCalendar.FEBRUARY),
    /**
     * MARCH
     */
    MARCH(GregorianCalendar.MARCH),
    /**
     * APRIL
     */
    APRIL(GregorianCalendar.APRIL),
    /**
     * MAY
     */
    MAY(GregorianCalendar.MAY),
    /**
     * JUNE
     */
    JUNE(GregorianCalendar.JUNE),
    /**
     * JULY
     */
    JULY(GregorianCalendar.JULY),
    /**
     * AUGUST
     */
    AUGUST(GregorianCalendar.AUGUST),
    /**
     * SEPTEMBER
     */
    SEPTEMBER(GregorianCalendar.SEPTEMBER),
    /**
     * OCTOBER
     */
    OCTOBER(GregorianCalendar.OCTOBER),
    /**
     * NOVEMBER
     */
    NOVEMBER(GregorianCalendar.NOVEMBER),
    /**
     * DECEMBER
     */
    DECEMBER(GregorianCalendar.DECEMBER);

    /**
     * Convert a month number to month
     *
     * @param monthNumber Month number
     * @return The month
     */
    public static final Month getMonth(int monthNumber)
    {
        for (Month month : values())
        {
            if (month.month == monthNumber)
            {
                return month;
            }
        }

        return null;
    }

    /**
     * Month number
     */
    private final int month;

    /**
     * Create month
     *
     * @param month Month number
     */
    Month(int month)
    {
        this.month = month;
    }

    /**
     * Month number
     *
     * @return Month number
     */
    public int getMonth()
    {
        return this.month;
    }
}