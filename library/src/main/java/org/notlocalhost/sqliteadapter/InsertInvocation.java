package org.notlocalhost.sqliteadapter;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;
import org.notlocalhost.sqliteadapter.parsers.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pedlar on 8/31/14.
 */
public class InsertInvocation implements Invocation {
    List<ForeignInsert> foreignInserts = new ArrayList<ForeignInsert>();
    ContentValues contentValues = new ContentValues();
    String tableName;
    public InsertInvocation(AdapterContext adapterContext, MethodInfo method, Object[] args) {
        tableName = method.getTableName();
        for(int i = 0; i < args.length; i++) {
            FieldInfo fieldInfo = method.getParamater(i);
            if(fieldInfo.getType() == FieldInfo.Type.COLUMN_NAME) {
                if(fieldInfo.isForeignKey()) {
                    TypeToken typeToken = TypeToken.get(fieldInfo.getFieldType());
                    if(Collection.class.isAssignableFrom(typeToken.getRawType())) {
                        Type enclosingType = HelperUtils.getCollectionElementType(typeToken.getType());
                        for(Object obj : Collection.class.cast(args[i])) {
                            addForeignInsert(adapterContext, fieldInfo.getName(), enclosingType, obj);
                        }
                    } else {
                        addForeignInsert(adapterContext, fieldInfo.getName(), fieldInfo.getFieldType(), args[i]);
                    }
                } else {
                    HelperUtils.addContentValue(contentValues, fieldInfo.getName(), args[i]);
                }
            }
        }
    }

    private void addForeignInsert(AdapterContext adapterContext, String columnName, Type fieldType, Object arg) {
        ClassInfo classInfo = adapterContext.getClassInfo(fieldType);
        ForeignInsert foreignInsert = new ForeignInsert();
        foreignInsert.tableName = Constants.FOREIGN_PREFIX + classInfo.getTableName();
        foreignInsert.columnName = columnName;
        for(FieldInfo fieldInfo : classInfo.getClassFields()) {
            if(fieldInfo.getType() == FieldInfo.Type.COLUMN_NAME) {
                Field field = fieldInfo.getField();
                field.setAccessible(true);
                try {
                    HelperUtils.addContentValue(foreignInsert.contentValues, fieldInfo.getName(), field.get(arg));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        foreignInserts.add(foreignInsert);
    }

    @Override
    public Object invokeCommand(AdapterContext adapterContext, SQLiteHelper sqLiteHelper) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.beginTransaction();
        long id = -1;
        try {
            id = db.insert(tableName, null, contentValues);

            for(ForeignInsert foreignInsert : foreignInserts) {
                foreignInsert.contentValues.put(Constants.FOREIGN_PREFIX + "id", id);
                foreignInsert.contentValues.put(Constants.FOREIGN_PREFIX + "column", foreignInsert.columnName);
                db.insert(foreignInsert.tableName, null, foreignInsert.contentValues);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return id;
    }

    private class ForeignInsert {
        ContentValues contentValues = new ContentValues();
        String tableName;
        String columnName;
    }
}
