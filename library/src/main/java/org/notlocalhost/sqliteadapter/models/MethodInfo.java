package org.notlocalhost.sqliteadapter.models;

import android.util.SparseArray;

import org.notlocalhost.sqliteadapter.HelperUtils;
import org.notlocalhost.sqliteadapter.RxJavaSupport;
import org.notlocalhost.sqliteadapter.Types;
import org.notlocalhost.sqliteadapter.parsers.TypeToken;

/**
 * Created by pedlar on 8/31/14.
 *
 */
public class MethodInfo {
    public static enum Type {
        QUERY, INSERT, UPDATE, DELETE
    }
    private Type methodType;
    private String tableName;
    private boolean isTableConstructor;
    private SparseArray<FieldInfo> paramaters;
    private java.lang.reflect.Type returnObjectType;
    private boolean isObservable;

    public MethodInfo() {
        paramaters = new SparseArray<FieldInfo>();
    }

    public void setType(Type type) {
        methodType = type;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setReturnClass(java.lang.reflect.Type returnObjectType) {
        Class returnClass = Types.getRawType(returnObjectType);
        if(RxJavaSupport.isRxJavaIncluded()) {
            if(RxJavaSupport.isObservable(returnClass)) {
                TypeToken typeToken = TypeToken.get(returnObjectType);
                this.returnObjectType = HelperUtils.getGenericsElementType(typeToken.getType());
                isObservable = true;
                return;
            }
        }
        this.returnObjectType = returnObjectType;
    }

    public void setTableConstructor(boolean isTableConstructor) {
        this.isTableConstructor = isTableConstructor;
    }

    public void addParamater(int index, FieldInfo paramater) {
        paramaters.append(index, paramater);
    }

    public boolean isObservable() {
        return isObservable;
    }

    public Type getType() {
        return methodType;
    }
    public String getTableName() {
        return this.tableName;
    }
    public java.lang.reflect.Type getReturnClass() {
        return returnObjectType;
    }
    public boolean isTableConstructor() {
        return isTableConstructor;
    }
    public FieldInfo getParamater(int index) {
        return paramaters.get(index);
    }
}
