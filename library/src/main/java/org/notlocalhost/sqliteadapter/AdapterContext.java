package org.notlocalhost.sqliteadapter;

import android.database.sqlite.SQLiteDatabase;

import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by pedlar on 9/1/14.
 */
public interface AdapterContext {
    public SQLiteHelper getSqlHelper();
    public ClassInfo getClassInfo(Type type);
    public MethodInfo getMethodInfo(Method method);
}
