package org.notlocalhost.sqliteadapter;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;

/**
 * Created by pedlar on 8/31/14.
 */
public class InsertInvocation implements Invocation {
    ContentValues contentValues = new ContentValues();
    String tableName;
    public InsertInvocation(MethodInfo method, Object[] args) {
        tableName = method.getTableName();
        for(int i = 0; i < args.length; i++) {
            FieldInfo fieldInfo = method.getParamater(i);
            if(fieldInfo.getType() == FieldInfo.Type.COLUMN_NAME) {
                addContentValue(contentValues, fieldInfo.getName(), args[i]);
            }
        }
    }

    private void addContentValue(ContentValues contentValues, String name, Object arg) {
        if(arg instanceof String) {
            contentValues.put(name, (String)arg);
        } else if(arg instanceof Byte) {
            contentValues.put(name, (Byte)arg);
        } else if(arg instanceof Short) {
            contentValues.put(name, (Short)arg);
        } else if(arg instanceof Integer) {
            contentValues.put(name, (Integer)arg);
        } else if(arg instanceof Long) {
            contentValues.put(name, (Long)arg);
        } else if(arg instanceof Float) {
            contentValues.put(name, (Float)arg);
        } else if(arg instanceof Double) {
            contentValues.put(name, (Double)arg);
        } else if(arg instanceof Boolean) {
            contentValues.put(name, (Boolean)arg);
        } else if(arg instanceof byte[]) {
            contentValues.put(name, (byte[])arg);
        }
    }

    @Override
    public Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.beginTransaction();
        long id = -1;
        try {
            id = db.insert(tableName, null, contentValues);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return id;
    }
}
