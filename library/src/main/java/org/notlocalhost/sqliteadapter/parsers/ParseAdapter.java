package org.notlocalhost.sqliteadapter.parsers;

import android.database.Cursor;

/**
 * Created by pedlar on 9/7/14.
 */
abstract class ParseAdapter<T> {
        protected abstract T parse(Cursor cursor);
}
