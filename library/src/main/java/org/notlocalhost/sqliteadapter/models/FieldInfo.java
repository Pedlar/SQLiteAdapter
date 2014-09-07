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
    private java.lang.reflect.Type fieldType;

    private Logical logical;
    private Operator[] operators;

    private boolean foreignKey;

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
    public void setFieldType(java.lang.reflect.Type fieldType) {
        this.fieldType = fieldType;
    }
    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
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
    public java.lang.reflect.Type getFieldType() {
        return this.fieldType;
    }
    public boolean isForeignKey() {
        return this.foreignKey;
    }
}
