package org.notlocalhost.sqliteadapter;

import java.util.concurrent.Future;

import rx.Observable;

/**
 * Created by pedlar on 11/23/14.
 */
public class RxJavaSupport {
    RxJavaSupport() {
        // no-instance
    }

    public static boolean isObservable(Class rawType) {
        return rawType == Observable.class;
    }

    public static boolean isRxJavaIncluded() {
        try {
            Class<?> rxJava = Class.forName("rx.Observable");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Observable createObservable(Future<Object> future) {
        return Observable.from(future);
    }
}
