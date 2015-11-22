package jhelp.android.api.database.type;

/**
 * Represents a time hour:minute:second.millisecond for database store
 * Created by jhelp on 21/11/15.
 */
public class Time extends DatabaseType
{
    /**
     * The time
     */
    private long time;

    /**
     * Create time initialize to 0
     */
    public Time()
    {
        this.time = 0;
    }

    /**
     * Time in milliseconds
     *
     * @return Time in milliseconds
     */
    public long getTime()
    {
        return this.time;
    }

    /**
     * Modify the  time
     *
     * @param milliseconds Time in milliseconds
     */
    public void setTime(long milliseconds)
    {
        this.time = milliseconds;
    }

    /**
     * Chnge the time
     *
     * @param hours        Hours
     * @param minutes      Minutes
     * @param seconds      Seconds
     * @param milliseconds Milliseconds
     */
    public void setTime(int hours, int minutes, int seconds, int milliseconds)
    {
        this.time = (((hours * 60L) + minutes) * 60L + seconds) * 60L + milliseconds;
    }

    /**
     * Chnge the time
     *
     * @param hours   Hours
     * @param minutes Minutes
     * @param seconds Seconds
     */
    public void setTime(int hours, int minutes, int seconds)
    {
        this.setTime(hours, minutes, seconds, 0);
    }

    /**
     * Chnge the time
     *
     * @param hours   Hours
     * @param minutes Minutes
     */
    public void setTime(int hours, int minutes)
    {
        this.setTime(hours, minutes, 0, 0);
    }

    /**
     * Obtain milliseconds part
     *
     * @return Millisecond part
     */
    public int getMilliseconds()
    {
        return (int) (this.time % 1000);
    }

    /**
     * Obtain seconds part
     *
     * @return Second part
     */
    public int getSeconds()
    {
        return (int) ((this.time / 1000) % 60);
    }

    /**
     * Obtain minutes part
     *
     * @return Minute part
     */
    public int getMinutes()
    {
        return (int) ((this.time / 60000) % 60);
    }

    /**
     * Obtain hours part
     *
     * @return Hour part
     */
    public int getHours()
    {
        return (int) (this.time / 3600000);
    }

    /**
     * Parse a serialized String to fill time
     *
     * @param serialized Serialized String to parse
     */
    @Override
    public void parse(String serialized)
    {
        this.time = Long.parseLong(serialized);
    }

    /**
     * Serialize time to String
     *
     * @return Serialized string
     */
    @Override
    public String serialize()
    {
        return String.valueOf(this.time);
    }
}