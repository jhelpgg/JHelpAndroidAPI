package jhelp.android.api.database.type;

import jhelp.android.api.Debug;

/**
 * Element type  stored in database
 * Created by jhelp on 21/11/15.
 */
public abstract class DatabaseType
{
    /**
     * Create element
     */
    public DatabaseType()
    {
    }

    /**
     * Parse a string to fill the type
     *
     * @param serialized Serialized String to parse
     */
    public abstract void parse(String serialized);

    /**
     * Serialize type in String
     *
     * @return Serialized String
     */
    public abstract String serialize();

    /**
     * Default String representation
     *
     * @return String representation
     */
    public String toString()
    {
        return Debug.createMessage(this.getClass()
                                       .getSimpleName(), " : ", this.serialize());
    }
}