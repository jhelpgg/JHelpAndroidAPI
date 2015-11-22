package jhelp.android.api.database;

/**
 * Created by jhelp on 21/11/15.
 */
public class TestStoredObject extends StoredObject
{
    @StoredField(name = "testInteger",
                 type = StoredFieldType.INTEGER)
    private int testInteger;

    public TestStoredObject(String name)
    {
        super(name);
    }

    public int getTestInteger()
    {
        return this.testInteger;
    }

    public void setTestInteger(int testInteger)
    {
        this.testInteger = testInteger;
    }
}
