package org.notlocalhost.sqliteadapter;

import android.database.CrossProcessCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;
import org.notlocalhost.sqliteadapter.parsers.CursorParser;

/**
 * Created by pedlar on 8/31/14.
 */
public class QueryInvocation implements Invocation {
    private StringBuilder queryString = new StringBuilder();
    private MethodInfo methodInfo;
    public QueryInvocation(MethodInfo method, Object[] args) {
        methodInfo = method;
        queryString.append("SELECT * FROM ");
        queryString.append(method.getTableName());

        boolean hasWhere = false;
        Log.d(QueryInvocation.class.getCanonicalName(), "QUERY: args length: " + args.length);
        for(int i = 0; i < args.length; i++) {
            FieldInfo paramInfo = method.getParamater(i);
            if(paramInfo.getType() == FieldInfo.Type.WHERE) {
                if(!hasWhere) {
                    queryString.append(" WHERE ");
                    hasWhere = true;
                } else {
                    queryString.append(paramInfo.getLogical().name());
                    queryString.append(" ");
                }
                queryString.append(method.getParamater(i).getName());
                queryString.append(method.getParamater(i).getOperatorString());
                queryString.append("'");
                queryString.append(args[i]);
                queryString.append("' ");
            }
        }
    }

    @Override
    public Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper) {
        Log.d(QueryInvocation.class.getCanonicalName(), "QUERY: " + queryString.toString());
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString.toString(), new String[]{});
        CursorParser parser = new CursorParser();
        Object responseObj = parser.objectFromCursor(adapterContext, methodInfo, cursor);
        cursor.close();
        db.close();
        return responseObj;
    }
}
