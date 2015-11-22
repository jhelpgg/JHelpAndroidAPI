package jhelp.android.api.database;

import java.util.List;

import jhelp.android.api.ByteArray;
import jhelp.android.api.database.type.ArrayDouble;
import jhelp.android.api.database.type.ArrayFloat;
import jhelp.android.api.database.type.ArrayInt;
import jhelp.android.api.database.type.ArrayLong;
import jhelp.android.api.database.type.ArrayShort;
import jhelp.android.api.database.type.BooleanList;
import jhelp.android.api.database.type.CharacterList;
import jhelp.android.api.database.type.Date;
import jhelp.android.api.database.type.DateComplete;
import jhelp.android.api.database.type.Time;

/**
 * Type of stored field
 * Created by jhelp on 21/11/15.
 */
public enum StoredFieldType
{
    /**
     * Indicates that field is a char
     */
    CHARACTER,
    /**
     * Indicates that field is a boolean
     */
    BOOLEAN,
    /**
     * Indicates that field is a byter
     */
    BYTE,
    /**
     * Indicates that field is a short
     */
    SHORT,
    /**
     * Indicates that field is a int
     */
    INTEGER,
    /**
     * Indicates that field is a long
     */
    LONG,
    /**
     * Indicates that field is a float
     */
    FLOAT,
    /**
     * Indicates that field is a double
     */
    DOUBLE,
    /**
     * Indicates that field is a {@link String}
     */
    STRING,
    /**
     * Indicates that field is a {@link Date}
     */
    DATE,
    /**
     * Indicates that field is a {@link DateComplete}
     */
    DATE_COMPLETE,
    /**
     * Indicates that field is a {@link Time}
     */
    TIME,
    /**
     * Indicates that field is a {@link StoredObject}, beware of cyclic reference
     */
    STORED_OBJECT,
    /**
     * Indicates that field is a {@link CharacterList}
     */
    CHARACTER_LIST,
    /**
     * Indicates that field is a {@link BooleanList}
     */
    BOOLEAN_LIST,
    /**
     * Indicates that field is a {@link ByteArray}
     */
    BYTE_LIST,
    /**
     * Indicates that field is a {@link ArrayShort}
     */
    SHORT_LIST,
    /**
     * Indicates that field is a {@link ArrayInt}
     */
    INTEGER_LIST,
    /**
     * Indicates that field is a {@link ArrayLong}
     */
    LONG_LIST,
    /**
     * Indicates that field is a {@link ArrayFloat}
     */
    FLOAT_LIST,
    /**
     * Indicates that field is a {@link ArrayDouble}
     */
    DOUBLE_LIST,
    /**
     * Indicates that field is a {@link List Lis&lt;String&gt;}
     */
    STRING_LIST,
    /**
     * Indicates that field is a {@link List Lis&lt;Date&gt;}
     */
    DATE_LIST,
    /**
     * Indicates that field is a {@link List Lis&lt;DateComplete&gt;}
     */
    DATE_COMPLETE_LIST,
    /**
     * Indicates that field is a {@link List Lis&lt;Time&gt;}
     */
    TIME_LIST,
    /**
     * Indicates that field is a {@link List Lis&lt;StoredObject&gt;}, beware of cyclic reference
     */
    STORED_OBJECT_LIST
}