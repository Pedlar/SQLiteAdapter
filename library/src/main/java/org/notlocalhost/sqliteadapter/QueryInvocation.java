package org.notlocalhost.sqliteadapter;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;
import org.notlocalhost.sqliteadapter.models.WhereStatement;
import org.notlocalhost.sqliteadapter.parsers.CursorParser;

/**
 * Created by pedlar on 8/31/14.
 */
public class QueryInvocation implements Invocation {
    private StringBuilder queryString = new StringBuilder();
    private MethodInfo methodInfo;
    private WhereStatement whereStatement;
    public QueryInvocation(MethodInfo method, Object[] args) {
        methodInfo = method;
        queryString.append("SELECT rowid, * FROM ");
        queryString.append(method.getTableName());

        whereStatement = HelperUtils.constructWhere(method, args);
    }

    @Override
    public Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        Object responseObj = null;
        try {
            db = sqLiteHelper.getReadableDatabase();
            if(whereStatement != null) {
                queryString.append(" WHERE ");
                queryString.append(whereStatement.whereClause);
            }
            cursor = db.rawQuery(queryString.toString(), whereStatement != null ? whereStatement.whereArgs : new String[]{});
            if(cursor.getCount() > 0) {
                CursorParser parser = new CursorParser();
                responseObj = parser.objectFromCursor(adapterContext, methodInfo.getReturnClass(), cursor);
            }
        } finally {
            if( cursor != null) {
                cursor.close();
            }
            if( db != null) {
                db.close();
            }
        }

        return responseObj;
    }
}
