package org.notlocalhost.sqliteadapter.parsers;

import org.notlocalhost.sqliteadapter.Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by pedlar on 9/1/14.
 */
public class TypeToken<T> {
    final Class<? super T> rawType;
    final Type type;
    final int hashCode;

    @SuppressWarnings("unchecked")
    protected TypeToken() {
        this.type = getSuperclassTypeParameter(getClass());
        this.rawType = (Class<? super T>) Types.getRawType(type);
        this.hashCode = type.hashCode();
    }

    @SuppressWarnings("unchecked")
    TypeToken(Type type) {
        this.type = type;
        this.rawType = (Class<? super T>) Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return parameterized.getActualTypeArguments()[0];
    }

    public final Type getType() {
        return type;
    }

    public final Class<? super T> getRawType() {
        return rawType;
    }

    public static <T>TypeToken<T> get(Type type) {
        return new TypeToken<T>(type);
    }

}
