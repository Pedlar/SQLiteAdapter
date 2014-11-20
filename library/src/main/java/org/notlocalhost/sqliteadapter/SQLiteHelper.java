package org.notlocalhost.sqliteadapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.parsers.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedlar on 8/31/14.
 *
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    WeakReference<Context> mWeakContext;
    WeakReference<AdapterContext> mWeakAdapterContext;
    List<ClassInfo> mClassInfoList = new ArrayList<ClassInfo>();

    public SQLiteHelper(Context context, String name, int version, List<ClassInfo> classInfoList, AdapterContext adapterContext) {
        super(context, name, null, version);
        mWeakContext = new WeakReference<Context>(context);
        mWeakAdapterContext = new WeakReference<AdapterContext>(adapterContext);
        mClassInfoList = classInfoList;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(ClassInfo classInfo : mClassInfoList) {
            createTableIfNotExist(db, classInfo);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }

    private void createTableIfNotExist(SQLiteDatabase db, ClassInfo classInfo) {
        createTableIfNotExist(db, classInfo, false);
    }

    private void createTableIfNotExist(SQLiteDatabase db, ClassInfo classInfo, boolean foreign) {
        long start = System.currentTimeMillis();
        StringBuilder createSQL = new StringBuilder();
        createSQL.append("CREATE TABLE IF NOT EXISTS ");
        createSQL.append(classInfo.getTableName());
        createSQL.append(" (");
        if(foreign) {
            createSQL.append(Constants.FOREIGN_PREFIX + "id INTEGER,");
            createSQL.append(Constants.FOREIGN_PREFIX + "column TEXT,");
        }
        for(FieldInfo fieldInfo : classInfo.getClassFields()) {
            if(fieldInfo.getType() == FieldInfo.Type.COLUMN_NAME) {
                if(fieldInfo.isForeignKey()) {
                    AdapterContext aContext = mWeakAdapterContext.get();
                    if(aContext != null) {
                        ClassInfo foreignClass = aContext.getClassInfo(fieldInfo.getFieldType());
                        String origName = foreignClass.getTableName();
                        foreignClass.setTableName(Constants.FOREIGN_PREFIX + foreignClass.getTableName());
                        createTableIfNotExist(db, foreignClass, true);
                        foreignClass.setTableName(origName);
                    } else {
                        Log.wtf("SQLiteAdapter", "aContext was null, that's not right...");
                    }
                } else {
                    createSQL.append(fieldInfo.getName());
                    createSQL.append(" ");
                    createSQL.append(getSQLType(fieldInfo.getFieldType()));
                    createSQL.append(",");
                }
            }
        }
        createSQL.deleteCharAt(createSQL.length()-1);
        createSQL.append(");");
        db.execSQL(createSQL.toString());

        long totalMs = System.currentTimeMillis() - start;
        Log.d("SQLiteAdapter", "Create Table " + classInfo.getTableName() + " " + totalMs + "ms");
    }

    private String getSQLType(Type fieldType) {
        Class<?> rawType = TypeToken.get(fieldType).getRawType();
        if(String.class.isAssignableFrom(rawType)) {
            return "TEXT";
        } else if(Number.class.isAssignableFrom(rawType)) {
            return "INTEGER";
        } else if(rawType.isPrimitive()) {
            if( int.class.isAssignableFrom(rawType)
             || long.class.isAssignableFrom(rawType)
             || double.class.isAssignableFrom(rawType)
             || float.class.isAssignableFrom(rawType)
             || short.class.isAssignableFrom(rawType)) {
                return "INTEGER";
            }
        }
        Log.w("SQLiteAdapter", "Using a non supported type " + rawType.getName());
        return "TEXT";
    }
}
