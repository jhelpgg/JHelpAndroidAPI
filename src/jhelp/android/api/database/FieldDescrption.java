package jhelp.android.api.database;

/**
 * Describe a field
 * Created by jhelp on 22/11/15.
 */
class FieldDescrption
{
    /**
     * Name
     */
    private final String          name;
    /**
     * Type
     */
    private final StoredFieldType type;
    /**
     * Value
     */
    private       Object          value;

    /**
     * Create description
     *
     * @param name Name
     * @param type Type
     */
    FieldDescrption(String name, StoredFieldType type)
    {
        this(name, type, null);
    }

    /**
     * Create description
     *
     * @param name  Name
     * @param type  Type
     * @param value Value
     */
    FieldDescrption(String name, StoredFieldType type, Object value)
    {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Type
     *
     * @return Type
     */
    public StoredFieldType getType()
    {
        return this.type;
    }

    /**
     * Name
     *
     * @return Name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Value
     *
     * @return Value
     */
    public Object getValue()
    {
        return this.value;
    }

    /**
     * Change value
     *
     * @param value New value
     */
    public void setValue(Object value)
    {
        this.value = value;
    }
}