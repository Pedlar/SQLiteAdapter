package org.notlocalhost.sqliteadapter.internal.command;

import org.notlocalhost.sqliteadapter.AdapterContext;
import org.notlocalhost.sqliteadapter.HelperUtils;
import org.notlocalhost.sqliteadapter.SQLiteHelper;
import org.notlocalhost.sqliteadapter.models.MethodInfo;
import org.notlocalhost.sqliteadapter.models.WhereStatement;

/**
 * Created by pedlar on 8/31/14.
 *
 */
class UpdateInvocation implements Invocation {
    String tableName;
    WhereStatement whereStatement;
    public UpdateInvocation(MethodInfo method, Object[] args) {
        tableName = method.getTableName();
        whereStatement = HelperUtils.constructWhere(method, args);
    }

    @Override
    public Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper) {

        return new Object();
    }
}
