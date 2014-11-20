package org.notlocalhost.sqliteadapter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * Annotation that sets the name of the Table for methods and schema to associate with.
 *
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface TableName {
    String value();
}