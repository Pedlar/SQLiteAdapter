package org.notlocalhost.sqliteadapter.internal.command;

import org.notlocalhost.sqliteadapter.AdapterContext;
import org.notlocalhost.sqliteadapter.SQLiteHelper;
import org.notlocalhost.sqliteadapter.models.MethodInfo;

/**
 * Created by pedlar on 8/31/14.
 *
 */
class UpdateInvocation implements Invocation {
    public UpdateInvocation(MethodInfo method, Object[] args) {

    }

    @Override
    public Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper) {

        return new Object();
    }
}
