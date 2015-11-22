package jhelp.android.api.database;

import java.util.HashMap;
import java.util.Set;

/**
 * Describe a database object
 * Created by jhelp on 21/11/15.
 */
class DatabaseObjectDescription
{
    /**
     * Database ID
     */
    private       long                             databaseID;
    /**
     * Object stored fields
     */
    private final HashMap<String, FieldDescrption> fields;
    /**
     * Class name
     */
    private final String                           className;
    /**
     * Object name
     */
    private       String                           name;

    /**
     * Create description
     *
     * @param className Class name
     */
    public DatabaseObjectDescription(String className)
    {
        this.className = className;
        this.databaseID = -1;
        this.fields = new HashMap<String, FieldDescrption>();
    }

    /**
     * Object name
     *
     * @return Object name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Change Name
     *
     * @param name New name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Class name
     *
     * @return Class name
     */
    public String getClassName()
    {
        return this.className;
    }

    /**
     * Database ID
     *
     * @return Database ID
     */
    public long getDatabaseID()
    {
        return this.databaseID;
    }

    /**
     * Change database ID
     *
     * @param databaseID New database ID
     */
    public void setDatabaseID(long databaseID)
    {
        this.databaseID = databaseID;
    }

    /**
     * Add a filed description
     *
     * @param fieldDescrption Field description to add
     */
    public void put(FieldDescrption fieldDescrption)
    {
        this.fields.put(fieldDescrption.getName(), fieldDescrption);
    }

    /**
     * Add a filed description
     *
     * @param name            Filed name
     * @param storedFieldType Filed type
     */
    public void put(String name, StoredFieldType storedFieldType)
    {
        this.put(new FieldDescrption(name, storedFieldType));
    }

    /**
     * Add a filed description
     *
     * @param name            Filed name
     * @param storedFieldType Filed type
     * @param value           Field value
     */
    public void put(String name, StoredFieldType storedFieldType, Object value)
    {
        this.put(new FieldDescrption(name, storedFieldType, value));
    }

    /**
     * Obtain a field description
     *
     * @param name Field name
     * @return Description
     */
    public FieldDescrption get(String name)
    {
        return this.fields.get(name);
    }

    /**
     * List of fields names
     *
     * @return List of fields names
     */
    public Set<String> getNames()
    {
        return this.fields.keySet();
    }
}