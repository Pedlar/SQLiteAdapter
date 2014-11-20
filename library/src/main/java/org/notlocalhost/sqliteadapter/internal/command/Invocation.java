package org.notlocalhost.sqliteadapter.internal.command;

import org.notlocalhost.sqliteadapter.AdapterContext;
import org.notlocalhost.sqliteadapter.SQLiteHelper;

/**
 * Created by pedlar on 8/31/14.
 */
public interface Invocation {
    Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper);
}
