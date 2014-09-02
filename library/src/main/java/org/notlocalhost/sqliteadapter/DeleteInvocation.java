package org.notlocalhost.sqliteadapter;

import org.notlocalhost.sqliteadapter.models.MethodInfo;

/**
 * Created by pedlar on 8/31/14.
 */
public class DeleteInvocation implements Invocation {
    public DeleteInvocation(MethodInfo method, Object[] args) {
        
    }

    @Override
    public Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper) {

        return new Object();
    }
}
