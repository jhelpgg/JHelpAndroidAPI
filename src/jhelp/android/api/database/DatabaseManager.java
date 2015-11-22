package jhelp.android.api.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import jhelp.android.api.ByteArray;
import jhelp.android.api.Debug;
import jhelp.android.api.Reflector;
import jhelp.android.api.Utilities;
import jhelp.android.api.database.type.ArrayDouble;
import jhelp.android.api.database.type.ArrayFloat;
import jhelp.android.api.database.type.ArrayInt;
import jhelp.android.api.database.type.ArrayLong;
import jhelp.android.api.database.type.ArrayShort;
import jhelp.android.api.database.type.BooleanList;
import jhelp.android.api.database.type.CharacterList;
import jhelp.android.api.database.type.DatabaseType;
import jhelp.android.api.database.type.Date;
import jhelp.android.api.database.type.DateComplete;
import jhelp.android.api.database.type.Time;

/**
 * Database manager.<br>
 * It stores {@link StoredObject} inside a database.<br>
 * {@link StoredObject} have to use the annotation {@link StoredField} to say witch field to
 * store.<br>
 * The database can be create with a password. For now  the password can't be change/add/remove
 * Created by jhelp on 21/11/15.
 */
public class DatabaseManager implements DatabaseErrorHandler
{
    /**
     * Database description table
     */
    private static final String TABLE_DATABASE_DESCRIPTION = "DatabaseDescription";
    /**
     * Objects stored table
     */
    private static final String TABLE_OBJECTS              = "Objects";
    /**
     * Objects fields table
     */
    private static final String TABLE_FIELDS               = "Fields";

    /**
     * ID column
     */
    private static final String COLUMN_ID         = "ID";
    /**
     * Password column
     */
    private static final String COLUMN_PASSWORD   = "Password";
    /**
     * Class name column
     */
    private static final String COLUMN_CLASS_NAME = "ClassName";
    /**
     * Name column
     */
    private static final String COLUMN_NAME       = "Name";
    /**
     * Type column
     */
    private static final String COLUMN_TYPE       = "Type";
    /**
     * Value column
     */
    private static final String COLUMN_VALUE      = "Value";
    /**
     * Object ID reference column
     */
    private static final String COLUMN_OBJECT_ID  = "ObjectID";

    /**
     * Columns get when select the password
     */
    private static final String[] SELECT_PASSWORD_COLUMNS                = {COLUMN_PASSWORD};
    /**
     * Column password index in {@link #SELECT_PASSWORD_COLUMNS}
     */
    private static final int      SELECT_PASSWORD_COLUMNS_INDEX_PASSWORD = 0;

    /**
     * Columns get when select an object
     */
    private static final String[] SELECT_OBJECTS_COLUMNS                  = {COLUMN_ID,
            COLUMN_CLASS_NAME, COLUMN_NAME};
    /**
     * Column ID index in {@link #SELECT_OBJECTS_COLUMNS}
     */
    private static final int      SELECT_OBJECTS_COLUMNS_INDEX_ID         = 0;
    /**
     * Column class name index in {@link #SELECT_OBJECTS_COLUMNS}
     */
    private static final int      SELECT_OBJECTS_COLUMNS_INDEX_CLASS_NAME = 1;
    /**
     * Column name index in {@link #SELECT_OBJECTS_COLUMNS}
     */
    private static final int      SELECT_OBJECTS_COLUMNS_NAME             = 2;

    /**
     * Columns get when select a field
     */
    private static final String[] SELECT_FILEDS_COLUMNS                 = {COLUMN_ID, COLUMN_NAME,
            COLUMN_TYPE, COLUMN_VALUE, COLUMN_OBJECT_ID};
    /**
     * Column ID index in {@link #SELECT_FILEDS_COLUMNS}
     */
    private static final int      SELECT_FILEDS_COLUMNS_INDEX_ID        = 0;
    /**
     * Column name index in {@link #SELECT_FILEDS_COLUMNS}
     */
    private static final int      SELECT_FILEDS_COLUMNS_INDEX_NAME      = 1;
    /**
     * Column type index in {@link #SELECT_FILEDS_COLUMNS}
     */
    private static final int      SELECT_FILEDS_COLUMNS_INDEX_TYPE      = 2;
    /**
     * Column value index in {@link #SELECT_FILEDS_COLUMNS}
     */
    private static final int      SELECT_FILEDS_COLUMNS_INDEX_VALUE     = 3;
    /**
     * Column object ID reference index in {@link #SELECT_FILEDS_COLUMNS}
     */
    private static final int      SELECT_FILEDS_COLUMNS_INDEX_OBJECT_ID = 4;

    /**
     * Condition for ID value : where ID=?
     */
    private static final String   WHERE_ID            = Debug.createMessage(COLUMN_ID, "=?");
    /**
     * Parameters for {@link #WHERE_ID} : 0->The id
     */
    private static final String[] WHERE_ID_PARAMERTES = new String[1];

    /**
     * Condition for name value : where Name=?
     */
    private static final String   WHERE_NAME            = Debug.createMessage(COLUMN_NAME, "=?");
    /**
     * Parameters for {@link #WHERE_NAME} : 0->The name
     */
    private static final String[] WHERE_NAME_PARAMERTES = new String[1];

    /**
     * Condition for name  and object ID value : where Name=? AND ObjectID=?
     */
    private static final String   WHERE_NAME_AND_OBJECT_ID            =
            Debug.createMessage(COLUMN_NAME, "=? AND ", COLUMN_OBJECT_ID, "=?");
    /**
     * Parameters for {@link #WHERE_NAME_AND_OBJECT_ID} : 0->The name ; 1->Object ID
     */
    private static final String[] WHERE_NAME_AND_OBJECT_ID_PARAMERTES = new String[2];

    /**
     * Condition for object ID value : where ObjectID=?
     */
    private static final String   WHERE_OBJECT_ID            =
            Debug.createMessage(COLUMN_OBJECT_ID, "=?");
    /**
     * Parameters for {@link #WHERE_OBJECT_ID} : 0->Object ID
     */
    private static final String[] WHERE_OBJECT_ID_PARAMERTES = new String[1];

    /**
     * Context reference
     */
    private final Context        context;
    /**
     * Database name
     */
    private final String         databaseName;
    /**
     * Database link
     */
    private final SQLiteDatabase database;
    /**
     * Access key
     */
    private       byte[]         key;

    /**
     * Compute a valid size key from a string
     *
     * @param password Password to transform on key
     * @return Valid size key
     */
    private static byte[] computeKey(final String password)
    {
        final char[] car  = password.toCharArray();
        long         hash = 0;

        for (final char c : car)
        {
            hash = (31L * hash) + c;
        }

        final byte[] key = new byte[8];
        for (int i = 0; i < 8; i++)
        {
            key[i] = (byte) (hash & 0xFF);

            hash >>= 8;
        }

        return key;
    }

    /**
     * Create database manager without password, all stored in clear
     *
     * @param context      Context reference
     * @param databaseName Database name
     * @throws DatabaseManagerException On creation issue. By example you access to a database
     * with a password
     */
    public DatabaseManager(Context context, String databaseName) throws DatabaseManagerException
    {
        this(context, databaseName, null);
    }

    /**
     * Create database manager with password
     *
     * @param context      Context reference
     * @param databaseName Database name
     * @param password     Password to use. If {@code null} or empty, its like no password
     * @throws DatabaseManagerException On creation issue. By example wrong password
     */
    public DatabaseManager(Context context, String databaseName, String password)
            throws DatabaseManagerException
    {
        this.context = context;
        this.databaseName = databaseName;

        if (password == null || password.length() == 0)
        {
            password = "";
            this.key = null;
        }
        else
        {
            this.key = computeKey(password);
        }

        this.database = this.context.openOrCreateDatabase(this.databaseName, Context.MODE_PRIVATE,
                                                          null, this);

        int currentVersion = this.database.getVersion();

        if (currentVersion == 0)
        {
            this.createTables();
            this.database.setVersion(1);
            this.storePassword(password);
        }
        else
        {
            this.checkPassword(password);
        }
    }

    /**
     * Encode a String on using the key
     *
     * @param string String to encode
     * @return Encoded string
     * @throws DatabaseManagerException On encryption issue
     */
    private String encode(String string) throws DatabaseManagerException
    {
        if (this.key == null)
        {
            return string;
        }

        try
        {
            ByteArray source = new ByteArray();
            source.writeString(string);
            ByteArray destination = new ByteArray();
            final DESKeySpec desKeySpec = new DESKeySpec(this.key);
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            final Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            final byte[] temp = new byte[8];
            final CipherInputStream cipherInputStream = new CipherInputStream(
                    source.getInputStream(), cipher);

            int read = cipherInputStream.read(temp);

            while (read >= 0)
            {
                destination.write(temp, 0, read);

                read = cipherInputStream.read(temp);
            }

            cipherInputStream.close();
            return Base64.encodeToString(destination.toArray(), Base64.DEFAULT);
        }
        catch (Exception exception)
        {
            throw new DatabaseManagerException(exception, "Failed to encode");
        }
    }

    /**
     * Decode a String on using the key
     *
     * @param string String to decode
     * @return Decoded String
     * @throws DatabaseManagerException On decryption issue
     */
    private String decode(String string) throws DatabaseManagerException
    {
        if (this.key == null)
        {
            return string;
        }

        try
        {
            ByteArray source = new ByteArray();
            source.write(Base64.decode(string, Base64.DEFAULT));
            ByteArray destination = new ByteArray();
            final DESKeySpec desKeySpec = new DESKeySpec(this.key);
            final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            final SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            final Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            final byte[] temp = new byte[8];
            final CipherInputStream cipherInputStream = new CipherInputStream(
                    source.getInputStream(), cipher);

            int read = cipherInputStream.read(temp);

            while (read >= 0)
            {
                destination.write(temp, 0, read);

                read = cipherInputStream.read(temp);
            }

            cipherInputStream.close();
            return destination.readString();
        }
        catch (Exception exception)
        {
            throw new DatabaseManagerException(exception, "Failed to decode");
        }
    }

    /**
     * Store the password
     *
     * @param password Password to store
     * @throws DatabaseManagerException If request failed
     */
    private void storePassword(String password) throws DatabaseManagerException
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PASSWORD, this.encode(password));
        this.database.insert(TABLE_DATABASE_DESCRIPTION, null, contentValues);
    }

    /**
     * Check if password is valid
     *
     * @param password Password to check
     * @throws DatabaseManagerException If password invalid
     */
    private void checkPassword(String password) throws DatabaseManagerException
    {
        Cursor cursor = this.database.query(TABLE_DATABASE_DESCRIPTION, SELECT_PASSWORD_COLUMNS,
                                            null, null, null, null, null);
        cursor.moveToNext();
        String stored = this.decode(cursor.getString(SELECT_PASSWORD_COLUMNS_INDEX_PASSWORD));
        cursor.close();

        if (password.equals(stored) == false)
        {
            // Random sleep against the "time attack"
            Utilities.sleep((int) ((Math.random() * 123) + 12));
            throw new DatabaseManagerException("Wrong password !");
        }
    }

    /**
     * Create a table
     *
     * @param tableName Table name
     * @param columns   Table columns
     * @throws DatabaseManagerException On creation issue
     */
    private void creatTable(String tableName, String... columns) throws DatabaseManagerException
    {
        StringBuilder request = new StringBuilder("CREATE TABLE ");
        request.append(tableName);
        request.append(" (ID INTEGER PRIMARY KEY AUTOINCREMENT");

        for (String column : columns)
        {
            request.append(", ");
            request.append(column);

            if (COLUMN_OBJECT_ID.equals(column) == true)
            {
                request.append(" INTEGER");
            }
            else
            {
                request.append(" TEXT");
            }
        }

        request.append(")");

        try
        {
            this.database.execSQL(request.toString());
        }
        catch (Exception exception)
        {
            throw new DatabaseManagerException(exception, "Failed to create table : ", tableName,
                                               " request=", request);
        }
    }

    /**
     * Create database tables
     *
     * @throws DatabaseManagerException On creation issue
     */
    private void createTables() throws DatabaseManagerException
    {
        this.creatTable(TABLE_DATABASE_DESCRIPTION, COLUMN_PASSWORD);
        this.creatTable(TABLE_OBJECTS, COLUMN_CLASS_NAME, COLUMN_NAME);
        this.creatTable(TABLE_FIELDS, COLUMN_NAME, COLUMN_TYPE, COLUMN_VALUE, COLUMN_OBJECT_ID);
    }

    /**
     * Called if database corrupted
     *
     * @param database Database corrupted
     */
    @Override
    public void onCorruption(SQLiteDatabase database)
    {
        //TODO
    }

    /**
     * Obtain a stored object with its database ID
     *
     * @param databseID Database ID
     * @param <TYPE>    Stored object type
     * @return Stored object OR {@code null} if not found
     * @throws DatabaseManagerException On request issue
     */
    private <TYPE extends StoredObject> TYPE obtain(long databseID) throws DatabaseManagerException
    {
        if (databseID < 0)
        {
            return null;
        }

        WHERE_ID_PARAMERTES[0] = String.valueOf(databseID);
        Cursor cursor = this.database.query(TABLE_OBJECTS, SELECT_OBJECTS_COLUMNS, WHERE_ID,
                                            WHERE_ID_PARAMERTES, null, null, null);

        if (cursor.moveToNext() == false)
        {
            cursor.close();
            return null;
        }

        String name = this.decode(cursor.getString(SELECT_OBJECTS_COLUMNS_NAME));

        try
        {
            Class<TYPE> claz = (Class<TYPE>) Class.forName(
                    this.decode(cursor.getString(SELECT_OBJECTS_COLUMNS_INDEX_CLASS_NAME)));
            return this.obtain(name, claz);
        }
        catch (Exception exception)
        {
            throw new DatabaseManagerException(exception, "Failed to get object ", databseID);
        }
    }

    /**
     * Obtain a stored object
     *
     * @param name   Object name
     * @param clazz  Object class
     * @param <TYPE> Object type
     * @return Stored object OR {@code null} if not found
     * @throws DatabaseManagerException On request issue
     */
    public <TYPE extends StoredObject> TYPE obtain(String name, Class<TYPE> clazz)
            throws DatabaseManagerException
    {
        WHERE_NAME_PARAMERTES[0] = this.encode(name);
        Cursor cursor = this.database.query(TABLE_OBJECTS, SELECT_OBJECTS_COLUMNS, WHERE_NAME,
                                            WHERE_NAME_PARAMERTES, null, null, null);

        if (cursor.moveToNext() == false)
        {
            cursor.close();
            return null;
        }

        TYPE storedObject = (TYPE) Reflector.newInstanceWithParameters(clazz, name);
        long objectID     = cursor.getLong(SELECT_OBJECTS_COLUMNS_INDEX_ID);
        storedObject.setDatabaseID(objectID);
        cursor.close();

        WHERE_OBJECT_ID_PARAMERTES[0] = String.valueOf(objectID);
        cursor = this.database.query(TABLE_FIELDS, SELECT_FILEDS_COLUMNS, WHERE_OBJECT_ID,
                                     WHERE_OBJECT_ID_PARAMERTES, null, null, null);

        String                           filedName;
        StoredFieldType                  type;
        String                           serialized;
        Object                           value;
        HashMap<String, FieldDescrption> fieldDescrptions = new HashMap<String, FieldDescrption>();

        while (cursor.moveToNext() == true)
        {
            filedName = this.decode(cursor.getString(SELECT_FILEDS_COLUMNS_INDEX_NAME));
            type = StoredFieldType.valueOf(
                    this.decode(cursor.getString(SELECT_FILEDS_COLUMNS_INDEX_TYPE)));

            if (cursor.isNull(SELECT_FILEDS_COLUMNS_INDEX_VALUE) == true)
            {
                value = null;
            }
            else
            {
                serialized = this.decode(cursor.getString(SELECT_FILEDS_COLUMNS_INDEX_VALUE));

                switch (type)
                {
                    case TIME_LIST:
                    case DATE_COMPLETE_LIST:
                    case DATE_LIST:
                        value = this.parseDatabaseTypeList(serialized);
                        break;
                    case STRING_LIST:
                    {
                        List<String> list = new ArrayList<String>();
                        ByteArray byteArray = new ByteArray();
                        byteArray.write(Base64.decode(serialized, Base64.DEFAULT));

                        for (String string : byteArray.readStringArray())
                        {
                            list.add(string);
                        }

                        value = list;
                        break;
                    }
                    case STORED_OBJECT_LIST:
                    {
                        List<StoredObject> list = new ArrayList<StoredObject>();
                        ArrayLong arrayLong = new ArrayLong();
                        arrayLong.parse(serialized);
                        int size = arrayLong.getSize();

                        for (int index = 0; index < size; index++)
                        {
                            list.add(this.obtain(arrayLong.getInteger(index)));
                        }

                        value = list;
                        break;
                    }
                    case SHORT_LIST:
                        value = new ArrayShort();
                        ((ArrayShort) value).parse(serialized);
                        break;
                    case BOOLEAN:
                        value = Boolean.parseBoolean(serialized);
                        break;
                    case BOOLEAN_LIST:
                        value = new BooleanList();
                        ((BooleanList) value).parse(serialized);
                        break;
                    case BYTE:
                        value = Byte.parseByte(serialized);
                        break;
                    case BYTE_LIST:
                        value = new ByteArray();
                        ((ByteArray) value).parse(serialized);
                        break;
                    case CHARACTER:
                        value = serialized.charAt(0);
                        break;
                    case CHARACTER_LIST:
                        value = new CharacterList();
                        ((CharacterList) value).parse(serialized);
                        break;
                    case DATE:
                        value = new Date();
                        ((Date) value).parse(serialized);
                        break;
                    case DATE_COMPLETE:
                        value = new DateComplete();
                        ((DateComplete) value).parse(serialized);
                        break;
                    case DOUBLE:
                        value = Double.parseDouble(serialized);
                        break;
                    case DOUBLE_LIST:
                        value = new ArrayDouble();
                        ((ArrayDouble) value).parse(serialized);
                        break;
                    case FLOAT:
                        value = Float.parseFloat(serialized);
                        break;
                    case FLOAT_LIST:
                        value = new ArrayFloat();
                        ((ArrayFloat) value).parse(serialized);
                        break;
                    case INTEGER:
                        value = Integer.parseInt(serialized);
                        break;
                    case INTEGER_LIST:
                        value = new ArrayInt();
                        ((ArrayInt) value).parse(serialized);
                        break;
                    case LONG:
                        value = Long.parseLong(serialized);
                        break;
                    case LONG_LIST:
                        value = new ArrayLong();
                        ((ArrayLong) value).parse(serialized);
                        break;
                    case SHORT:
                        value = Short.parseShort(serialized);
                        break;
                    case STORED_OBJECT:
                        value = this.obtain(Long.parseLong(serialized));
                        break;
                    case STRING:
                        value = serialized;
                        break;
                    case TIME:
                        value = new Time();
                        ((Time) value).parse(serialized);
                        break;
                    default:
                        value = null;
                        break;
                }
            }

            fieldDescrptions.put(filedName, new FieldDescrption(filedName, type, value));
        }

        cursor.close();
        StoredField     storedField;
        FieldDescrption fieldDescrption;

        for (Field filed : clazz.getDeclaredFields())
        {
            storedField = filed.getAnnotation(StoredField.class);

            if (storedField != null)
            {
                fieldDescrption = fieldDescrptions.get(storedField.name());

                if (fieldDescrption != null)
                {
                    try
                    {
                        filed.setAccessible(true);
                        filed.set(storedObject, fieldDescrption.getValue());
                    }
                    catch (Exception exception)
                    {
                        throw new DatabaseManagerException(exception, "Type not match for ",
                                                           fieldDescrption.getName());
                    }
                }
            }
        }

        return storedObject;
    }

    /**
     * Stor OR update a field
     *
     * @param fieldDescrption Filed to store/update
     * @param objectID        Object ID where Field lies to
     * @throws DatabaseManagerException On request issue
     */
    private void storeUpdateField(FieldDescrption fieldDescrption, long objectID)
            throws DatabaseManagerException
    {
        WHERE_NAME_AND_OBJECT_ID_PARAMERTES[0] = this.encode(fieldDescrption.getName());
        WHERE_NAME_AND_OBJECT_ID_PARAMERTES[1] = String.valueOf(objectID);
        Cursor cursor = this.database.query(TABLE_FIELDS, SELECT_FILEDS_COLUMNS,
                                            WHERE_NAME_AND_OBJECT_ID,
                                            WHERE_NAME_AND_OBJECT_ID_PARAMERTES, null, null, null);


        if (cursor.moveToNext() == true)
        {
            WHERE_ID_PARAMERTES[0] = String.valueOf(cursor.getLong(SELECT_FILEDS_COLUMNS_INDEX_ID));
            cursor.close();
            this.database.update(TABLE_FIELDS,
                                 this.computeContentValues(fieldDescrption, objectID),
                                 WHERE_ID, WHERE_ID_PARAMERTES);
        }
        else
        {
            cursor.close();
            this.database.insert(TABLE_FIELDS, null,
                                 this.computeContentValues(fieldDescrption, objectID));
        }
    }

    /**
     * Store a field
     *
     * @param fieldDescrption Filed to store
     * @param objectID        Object ID where Field lies to
     * @throws DatabaseManagerException On request issue
     */
    private void storeField(FieldDescrption fieldDescrption, long objectID)
            throws DatabaseManagerException
    {
        this.database.insert(TABLE_FIELDS, null,
                             this.computeContentValues(fieldDescrption, objectID));
    }

    /**
     * Compute field content values for store or update
     *
     * @param fieldDescrption Filed to store/update
     * @param objectID        Object ID where Field lies to
     * @return Filled content values
     * @throws DatabaseManagerException On request issue
     */
    private ContentValues computeContentValues(FieldDescrption fieldDescrption, long objectID)
            throws DatabaseManagerException
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, this.encode(fieldDescrption.getName()));
        contentValues.put(COLUMN_TYPE, this.encode(fieldDescrption.getType()
                                                                  .name()));
        contentValues.put(COLUMN_OBJECT_ID, objectID);
        Object value = fieldDescrption.getValue();

        if (value == null)
        {
            contentValues.putNull(COLUMN_VALUE);
        }
        else
        {
            switch (fieldDescrption.getType())
            {
                case STRING:
                case SHORT:
                case FLOAT:
                case BOOLEAN:
                case BYTE:
                case CHARACTER:
                case DOUBLE:
                case INTEGER:
                case LONG:
                    contentValues.put(COLUMN_VALUE, this.encode(value.toString()));
                    break;
                case STORED_OBJECT:
                    contentValues.put(COLUMN_VALUE, this.encode(
                            String.valueOf(((StoredObject) value).getDatabaseID())));
                    break;
                case BOOLEAN_LIST:
                case BYTE_LIST:
                case CHARACTER_LIST:
                case DATE:
                case DATE_COMPLETE:
                case DOUBLE_LIST:
                case FLOAT_LIST:
                case INTEGER_LIST:
                case LONG_LIST:
                case SHORT_LIST:
                case TIME:
                    contentValues.put(COLUMN_VALUE,
                                      this.encode(((DatabaseType) value).serialize()));
                    break;
                case DATE_COMPLETE_LIST:
                    contentValues.put(COLUMN_VALUE,
                                      this.encode(this.serializeDatabaseTypeList(
                                              (List<DateComplete>) value)));
                    break;
                case DATE_LIST:
                    contentValues.put(COLUMN_VALUE,
                                      this.encode(this.serializeDatabaseTypeList(
                                              (List<Date>) value)));
                    break;
                case STORED_OBJECT_LIST:
                    ArrayLong arrayLong = new ArrayLong();

                    for (StoredObject storedObject : (List<StoredObject>) value)
                    {
                        if (storedObject == null)
                        {
                            arrayLong.add(-1);
                        }
                        else
                        {
                            arrayLong.add(storedObject.getDatabaseID());
                        }
                    }

                    contentValues.put(COLUMN_VALUE,
                                      this.encode(arrayLong.serialize()));
                    break;
                case STRING_LIST:
                    List<String> list = (List<String>) value;
                    ByteArray byteArray = new ByteArray();
                    byteArray.writeStringArray(list.toArray(new String[list.size()]));
                    contentValues.put(COLUMN_VALUE,
                                      this.encode(Base64.encodeToString(byteArray.toArray(),
                                                                        Base64.DEFAULT)));
                    break;
                case TIME_LIST:
                    contentValues.put(COLUMN_VALUE,
                                      this.encode(this.serializeDatabaseTypeList(
                                              (List<Time>) value)));
                    break;
                default:
                    throw new DatabaseManagerException("Unknow type : ", fieldDescrption.getType());
            }
        }

        return contentValues;
    }

    /**
     * Serialize a database type list
     *
     * @param list   List of database type to serialize
     * @param <TYPE> List elements type
     * @return Serialized String
     */
    private <TYPE extends DatabaseType> String serializeDatabaseTypeList(List<TYPE> list)
    {
        ByteArray byteArray = new ByteArray();
        byteArray.writeInteger(list.size());

        for (DatabaseType databaseType : list)
        {
            if (databaseType == null)
            {
                byteArray.writeString(null);
            }
            else
            {
                byteArray.writeString(databaseType.getClass()
                                                  .getName());
                byteArray.writeString(databaseType.serialize());
            }
        }

        return Base64.encodeToString(byteArray.toArray(), Base64.DEFAULT);
    }

    /**
     * Aprse serialized String to list of database type
     *
     * @param string String to parse
     * @param <TYPE> Element list type
     * @return List of database type
     */
    private <TYPE extends DatabaseType> List<TYPE> parseDatabaseTypeList(String string)
    {
        ByteArray byteArray = new ByteArray();
        byteArray.write(Base64.decode(string, Base64.DEFAULT));
        int        size = byteArray.readInteger();
        List<TYPE> list = new ArrayList<TYPE>();
        String     className;

        for (int i = 0; i < size; i++)
        {
            className = byteArray.readString();

            if (className == null)
            {
                list.add(null);
            }
            else
            {
                try
                {
                    TYPE instance = (TYPE) Reflector.newInstance(className);
                    instance.parse(byteArray.readString());
                    list.add(instance);
                }
                catch (Exception exception)
                {
                    Debug.printException(exception, "Failed to create ", className);
                }
            }
        }

        return list;
    }

    /**
     * Store or update an object in database
     *
     * @param object Object to store/update
     * @throws DatabaseManagerException On request issue
     */
    public void storeUpdate(StoredObject object) throws DatabaseManagerException
    {
        long databaseID = object.getDatabaseID();

        if (databaseID < 0)
        {
            StoredObject other = this.obtain(object.getName(), object.getClass());

            if (other != null)
            {
                databaseID = other.getDatabaseID();
                object.setDatabaseID(databaseID);
            }
        }

        DatabaseObjectDescription databaseObjectDescription = this.obtainDescription(object, true);

        if (databaseID < 0)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CLASS_NAME,
                              this.encode(databaseObjectDescription.getClassName()));
            contentValues.put(COLUMN_NAME, this.encode(databaseObjectDescription.getName()));
            databaseID = this.database.insert(TABLE_OBJECTS, null, contentValues);

            for (String name : databaseObjectDescription.getNames())
            {
                this.storeField(databaseObjectDescription.get(name), databaseID);
            }

            object.setDatabaseID(databaseID);
        }
        else
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CLASS_NAME,
                              this.encode(databaseObjectDescription.getClassName()));
            contentValues.put(COLUMN_NAME, this.encode(databaseObjectDescription.getName()));
            WHERE_ID_PARAMERTES[0] = String.valueOf(databaseID);
            this.database.update(TABLE_OBJECTS, contentValues, WHERE_ID, WHERE_ID_PARAMERTES);

            for (String name : databaseObjectDescription.getNames())
            {
                this.storeUpdateField(databaseObjectDescription.get(name), databaseID);
            }
        }
    }

    /**
     * Remove an object from database
     *
     * @param object Object to remove
     * @throws DatabaseManagerException On request issue
     */
    public void remove(StoredObject object) throws DatabaseManagerException
    {
        long databaseID = object.getDatabaseID();

        if (databaseID < 0)
        {
            return;
        }

        WHERE_ID_PARAMERTES[0] = String.valueOf(databaseID);
        this.database.delete(TABLE_OBJECTS, WHERE_ID, WHERE_ID_PARAMERTES);
        WHERE_OBJECT_ID_PARAMERTES[0] = String.valueOf(databaseID);
        this.database.delete(TABLE_FIELDS, WHERE_OBJECT_ID, WHERE_OBJECT_ID_PARAMERTES);
    }

    /**
     * Obtain object description
     *
     * @param object         Object to describe
     * @param automaticStore Indicates if have to automatically store/update objects references
     * @return Description
     * @throws DatabaseManagerException On request issue
     */
    private DatabaseObjectDescription obtainDescription(StoredObject object,
                                                        boolean automaticStore)
            throws DatabaseManagerException
    {
        Class claz = object.getClass();
        DatabaseObjectDescription databaseObjectDescription = new DatabaseObjectDescription(
                claz.getName());
        databaseObjectDescription.setDatabaseID(object.getDatabaseID());
        databaseObjectDescription.setName(object.getName());
        String          name;
        StoredFieldType storedFieldType;
        Object          value;

        for (Field field : claz.getDeclaredFields())
        {
            StoredField storedField = field.getAnnotation(StoredField.class);

            if (storedField == null)
            {
                continue;
            }

            field.setAccessible(true);
            name = storedField.name();
            storedFieldType = storedField.type();

            try
            {
                switch (storedFieldType)
                {
                    case INTEGER:
                        value = field.getInt(object);
                        break;
                    case LONG:
                        value = field.getLong(object);
                        break;
                    case BOOLEAN:
                        value = field.getBoolean(object);
                        break;
                    case BYTE:
                        value = field.getByte(object);
                        break;
                    case BOOLEAN_LIST:
                        value = (BooleanList) field.get(object);
                        break;
                    case BYTE_LIST:
                        value = (ByteArray) field.get(object);
                        break;
                    case CHARACTER:
                        value = field.getChar(object);
                        break;
                    case CHARACTER_LIST:
                        value = (CharacterList) field.get(object);
                        break;
                    case DATE:
                        value = (Date) field.get(object);
                        break;
                    case TIME_LIST:
                        value = (List<Time>) field.get(object);
                        break;
                    case STRING_LIST:
                        value = (List<String>) field.get(object);
                        break;
                    case STORED_OBJECT_LIST:
                        value = (List<StoredObject>) field.get(object);

                        if (value != null && automaticStore == true)
                        {
                            for (StoredObject storedObject : (List<StoredObject>) value)
                            {
                                this.storeUpdate(storedObject);
                            }
                        }
                        break;
                    case SHORT_LIST:
                        value = (ArrayShort) field.get(object);
                        break;
                    case LONG_LIST:
                        value = (ArrayLong) field.get(object);
                        break;
                    case INTEGER_LIST:
                        value = (ArrayInt) field.get(object);
                        break;
                    case FLOAT_LIST:
                        value = (ArrayFloat) field.get(object);
                        break;
                    case DOUBLE_LIST:
                        value = (ArrayDouble) field.get(object);
                        break;
                    case TIME:
                        value = (Time) field.get(object);
                        break;
                    case DATE_COMPLETE:
                        value = (DateComplete) field.get(object);
                        break;
                    case DATE_COMPLETE_LIST:
                        value = (List<DateComplete>) field.get(object);
                        break;
                    case DATE_LIST:
                        value = (List<Date>) field.get(object);
                        break;
                    case DOUBLE:
                        value = field.getDouble(object);
                        break;
                    case FLOAT:
                        value = field.getFloat(object);
                        break;
                    case SHORT:
                        value = field.getShort(object);
                        break;
                    case STORED_OBJECT:
                        value = (StoredObject) field.get(object);

                        if (value != null && automaticStore == true)
                        {
                            this.storeUpdate((StoredObject) value);
                        }
                        break;
                    case STRING:
                        value = (String) field.get(object);
                        break;
                    default:
                        throw new DatabaseManagerException("Unknow type : ", storedFieldType);
                }

                databaseObjectDescription.put(name, storedFieldType, value);
            }
            catch (Exception exception)
            {
                throw new DatabaseManagerException(exception,
                                                   "Failed to get '", name,
                                                   "' @StoredField  value");
            }
        }

        return databaseObjectDescription;
    }
}