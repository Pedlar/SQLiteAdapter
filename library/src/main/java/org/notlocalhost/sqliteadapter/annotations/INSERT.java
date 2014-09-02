package org.notlocalhost.sqliteadapter.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by pedlar on 8/30/14.
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface INSERT {
}