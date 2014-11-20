package org.notlocalhost.sqliteadapter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * Make a INSERT call to the SQL Database
 *
 * Requires {@link org.notlocalhost.sqliteadapter.annotations.TableName} annotation to be present
 *
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface INSERT {
    boolean updateeOnCollision() default false;
}