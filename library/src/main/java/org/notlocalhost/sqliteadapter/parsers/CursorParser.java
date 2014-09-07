package org.notlocalhost.sqliteadapter.parsers;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.notlocalhost.sqliteadapter.AdapterContext;
import org.notlocalhost.sqliteadapter.Constants;
import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.FieldInfo;

import java.lang.reflect.Type;

/**
 * Created by pedlar on 9/1/14.
 */
public class CursorParser {

    public Object objectFromCursor(AdapterContext context, Type objectType, Cursor cursor) {
        TypeToken<Object> typeToken = TypeToken.get(objectType);
        ParseAdapter parseAdapter = ParseAdapterFactory.get(this, context, typeToken);
        return parseAdapter.parse(cursor);
    }

    protected Object queryForeignKey(AdapterContext context, String rowId, FieldInfo fieldInfo) {
        ClassInfo classInfo = context.getClassInfo(fieldInfo.getFieldType());
        String tableName = Constants.FOREIGN_PREFIX + classInfo.getTableName();
        String query = "SELECT rowid, * FROM " + tableName + " WHERE foreign_id=? AND foreign_column=?";
        SQLiteDatabase db = context.getSqlHelper().getReadableDatabase();
        Object responseObject = null;
        Cursor c = null;
        try {
            c = db.rawQuery(query, new String[]{rowId, fieldInfo.getName()});
            if(c.getCount() > 0) {
                responseObject = objectFromCursor(context, fieldInfo.getFieldType(), c);
            }
        } finally {
            if( c != null) {
                c.close();
            }
            if( db != null) {
                db.close();
            }
        }
        return responseObject;
    }
}
