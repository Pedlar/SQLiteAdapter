package org.notlocalhost.sqliteadapter.parsers;

import android.database.Cursor;

import org.notlocalhost.sqliteadapter.AdapterContext;

import java.util.Collection;

/**
 * Created by pedlar on 9/7/14.
 */
class ParseAdapterFactory {

    @SuppressWarnings("unchecked")
    public static <T>ParseAdapter<T> get(CursorParser cursorParser, AdapterContext context, TypeToken<Object> typeToken) {
        ParseAdapter<T> adapter;
        if(Collection.class.isAssignableFrom(typeToken.getRawType())) {
            adapter = (ParseAdapter<T>)new CollectionParseAdapter(context, cursorParser, typeToken);
        } else {
            adapter = (ParseAdapter<T>)new ObjectParseAdapter(context, cursorParser, typeToken);
        }
        return adapter;
    }
}
