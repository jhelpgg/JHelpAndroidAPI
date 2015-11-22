package jhelp.android.api.database;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to add in {@link StoredObject} subclass to indicates with filed to take.<br>
 * Remember to add an exception in proguard rule for the field is kept and annotation not removed
 * .<br>The real field name can be rename, because we use the name given by the annotation
 * Created by jhelp on 21/11/15.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {
                ElementType.FIELD
        })
public @interface StoredField
{
    /**
     * Field name strored in database
     */
    String name();

    /**
     * Field type
     */
    StoredFieldType type();
}