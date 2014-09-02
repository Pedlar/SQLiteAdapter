package org.notlocalhost.sqliteadapter.models;

import android.util.SparseArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedlar on 9/1/14.
 */
public class ClassInfo {
    private String className;
    private String tableName;
    private Type classType;
    private List<FieldInfo> classFields = new ArrayList<FieldInfo>();

    public void addClassField(FieldInfo fieldInfo) {
        classFields.add(fieldInfo);
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public void setClassType(Type classType) {
        this.classType = classType;
    }

    public String getTableName() {
        return tableName;
    }
    public List<FieldInfo> getClassFields() {
        return classFields;
    }

    public Type getClassType() {
        return classType;
    }
}
