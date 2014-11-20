package org.notlocalhost.sqliteadapter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * Make a SELECT call to the SQL Database and returns the Results
 *
 * Requires {@link org.notlocalhost.sqliteadapter.annotations.TableName} annotation to be present
 *
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface QUERY {
}