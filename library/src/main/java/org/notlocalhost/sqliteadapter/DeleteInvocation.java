package org.notlocalhost.sqliteadapter;

import android.database.sqlite.SQLiteDatabase;

import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;
import org.notlocalhost.sqliteadapter.models.WhereStatement;

/**
 * Created by pedlar on 8/31/14.
 */
public class DeleteInvocation implements Invocation {
    String tableName;
    WhereStatement whereStatement;
    public DeleteInvocation(MethodInfo method, Object[] args) {
        tableName = method.getTableName();
        whereStatement = HelperUtils.constructWhere(method, args);
    }

    @Override
    public Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper) {
        SQLiteDatabase db = null;
        int affected = 0;
        try {
            db = sqLiteHelper.getWritableDatabase();
            affected = db.delete(tableName,
                    whereStatement != null ? whereStatement.whereClause : "1",
                    whereStatement != null ? whereStatement.whereArgs : new String[]{});
        } finally {
            if( db != null ) {
                db.close();
            }
        }
        return affected;
    }
}
