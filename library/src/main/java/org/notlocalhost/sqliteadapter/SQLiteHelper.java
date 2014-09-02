package org.notlocalhost.sqliteadapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.FieldInfo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedlar on 8/31/14.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    WeakReference<Context> mWeakContext;
    WeakReference<SQLiteAdapter> mWeakAdapter;
    List<ClassInfo> mClassInfoList = new ArrayList<ClassInfo>();

    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteHelper(Context context, String name, int version, List<ClassInfo> classInfoList, SQLiteAdapter adapter) {
        super(context, name, null, version);
        mWeakContext = new WeakReference<Context>(context);
        mWeakAdapter = new WeakReference<SQLiteAdapter>(adapter);
        mClassInfoList = classInfoList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("SQLiteAdapter", "SQLiteHelper: onCreate()");
        for(ClassInfo classInfo : mClassInfoList) {
            createTableIfNotExist(db, classInfo);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("SQLiteAdapter", "SQLiteHelper: onUpgrade()");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d("SQLiteAdapter", "SQLiteHelper: onOpen()");
    }

    private void createTableIfNotExist(SQLiteDatabase db, ClassInfo classInfo) {
        StringBuilder createSQL = new StringBuilder();
        createSQL.append("CREATE TABLE IF NOT EXISTS ");
        createSQL.append(classInfo.getTableName());
        createSQL.append(" (");
        for(FieldInfo fieldInfo : classInfo.getClassFields()) {
            if(fieldInfo.getType() == FieldInfo.Type.COLUMN_NAME) {
                createSQL.append(fieldInfo.getName());
                createSQL.append(" ");
                createSQL.append(getSQLType(fieldInfo.getFieldType()));
                createSQL.append(",");
            }
        }
        createSQL.deleteCharAt(createSQL.length()-1);
        createSQL.append(");");
        Log.d("SQLiteAdapter", "CREATE: " + createSQL.toString());
        db.execSQL(createSQL.toString());
    }

    private String getSQLType(Class<?> fieldType) {
        if(String.class.isAssignableFrom(fieldType)) {
            return "TEXT";
        } else if(Number.class.isAssignableFrom(fieldType)) {
            return "INTEGER";
        } else if(fieldType.isPrimitive()) {
            if( int.class.isAssignableFrom(fieldType)
             || long.class.isAssignableFrom(fieldType)
             || double.class.isAssignableFrom(fieldType)
             || float.class.isAssignableFrom(fieldType)
             || short.class.isAssignableFrom(fieldType)) {
                return "INTEGER";
            }
        }
        Log.w("SQLiteAdapter", "Using a non supported type " + fieldType.getName());
        return "TEXT";
    }
}
