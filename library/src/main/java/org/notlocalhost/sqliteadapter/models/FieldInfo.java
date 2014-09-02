package org.notlocalhost.sqliteadapter.models;

import java.lang.reflect.Field;

/**
 * Created by pedlar on 8/31/14.
 */
public class FieldInfo {
    public static enum Type {
        COLUMN_NAME, WHERE
    }
    /** Logical AND, Logical OR for Where Annotation */
    public static enum Logical {
        AND, OR
    }
    /** Operators for Where Annoation */
    public static enum Operator {
        EQUAL, NOT_EQUAL, LESS_THAN, GREATHER_THAN
    }
    private Field field;
    private String name;
    private Type type;
    private Class<?> fieldType;

    private Logical logical;
    private Operator[] operators;

    public void setField(Field field) {
        this.field = field;
    }
    public void setType(Type type) {
        this.type = type;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setOperators(Operator[] operators) {
        this.operators = operators;
    }
    public void setLogical(Logical logical) {
        this.logical = logical;
    }
    public void setFieldType(Class<?> fieldType) {
        this.fieldType = fieldType;
    }


    public Field getField() {
        return this.field;
    }
    public String getName() {
        return this.name;
    }
    public Type getType() {
        return this.type;
    }
    public Logical getLogical() {
        return logical;
    }
    public String getOperatorString() {
        return "=";
    }
    public Class<?> getFieldType() {
        return this.fieldType;
    }
}
