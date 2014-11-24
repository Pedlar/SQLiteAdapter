package org.notlocalhost.sqliteadapter.parsers;

import android.database.Cursor;

import org.notlocalhost.sqliteadapter.AdapterContext;
import org.notlocalhost.sqliteadapter.HelperUtils;
import org.notlocalhost.sqliteadapter.models.ClassInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Created by pedlar on 9/7/14.
 */
class CollectionParseAdapter<E> extends ParseAdapter<Collection<E>>{
    ClassInfo mClassInfo;
    CursorParser mParser;
    AdapterContext aContext;
    TypeToken<Object> typeToken;
    ParseAdapter<E> objectParseAdapter;

    protected CollectionParseAdapter(AdapterContext context, CursorParser parser, TypeToken<Object> typeToken) {
        mClassInfo = context.getClassInfo(typeToken.getType());
        mParser = parser;
        aContext = context;
        this.typeToken = typeToken;
        objectParseAdapter = ParseAdapterFactory.get(parser, context, TypeToken.get(HelperUtils.getGenericsElementType(typeToken.getType())));
    }

    @Override
    protected Collection<E> parse(Cursor cursor) {
        Collection<E> collection = constructResponseObject(typeToken.getRawType());

        while(cursor.moveToNext()) {
            E object = objectParseAdapter.parse(cursor);
            collection.add(object);
        }

        return collection;
    }

    private <T>T constructResponseObject(Class<? super T> rawType) {
        try {
            final Constructor<? super T> constructor = rawType.getDeclaredConstructor();
            if(!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            Object[] args = null;
            return (T)constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
