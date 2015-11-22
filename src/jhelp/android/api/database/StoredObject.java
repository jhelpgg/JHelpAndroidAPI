package jhelp.android.api.database;

/**
 * Object to stroe.<br>
 * Extends this class to store object in database.<br>
 * Mark with {@link StoredField} annotation fields to store in database<br>
 * Objects names MUST be unique, if two objects have same name, they will be consider the same in
 * database point of view
 * Created by jhelp on 21/11/15.
 */
public abstract class StoredObject
{
    /**
     * Database ID
     */
    private       long   databaseID;
    /**
     * Object name
     */
    private final String name;

    /**
     * Create the object
     *
     * @param name Object name unique identifier
     */
    public StoredObject(String name)
    {
        this.databaseID = -1;
        this.name = name;
    }

    /**
     * Object name unique identifier
     *
     * @return Object name unique identifier
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * Database ID
     *
     * @return Database ID
     */
    public final long getDatabaseID()
    {
        return this.databaseID;
    }

    /**
     * Cange dtabase ID
     *
     * @param databaseID New database ID
     */
    final void setDatabaseID(long databaseID)
    {
        this.databaseID = databaseID;
    }
}