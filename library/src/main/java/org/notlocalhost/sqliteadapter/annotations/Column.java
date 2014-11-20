package org.notlocalhost.sqliteadapter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 * Set the name for a field in a {@link org.notlocalhost.sqliteadapter.Schema} class when used in
 * a class definition.
 *
 * When used as a Paramater annotation maps the paramater to the specified column.
 *
 */
@Documented
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface Column {
    String name() default "";
    boolean foreignKey() default false;
}