package jhelp.android.api.database;

import jhelp.android.api.Debug;

/**
 * Excption may happen in  {@link DatabaseManager} operations
 * Created by jhelp on 21/11/15.
 */
public class DatabaseManagerException extends Exception
{
    /**
     * Create exception with a message
     *
     * @param message Message
     */
    public DatabaseManagerException(Object... message)
    {
        super(Debug.createMessage(message));
    }

    /**
     * Create exception caused by an other one
     *
     * @param cause   Cause of the exception
     * @param message Message
     */
    public DatabaseManagerException(Throwable cause, Object... message)
    {
        super(Debug.createMessage(message), cause);
    }
}