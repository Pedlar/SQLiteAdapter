package org.notlocalhost.sqliteadapter;

/**
 * Created by pedlar on 8/31/14.
 */
interface Invocation {
    Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper);
}
