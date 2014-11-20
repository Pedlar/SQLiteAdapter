package org.notlocalhost.sqliteadapter.annotations;

import org.notlocalhost.sqliteadapter.models.FieldInfo;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * WHERE paramater for the SQLite Database Calls
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Where {
    String value();
    FieldInfo.Logical logical() default FieldInfo.Logical.AND;
    FieldInfo.Operator[] operator() default { FieldInfo.Operator.EQUAL };
}
