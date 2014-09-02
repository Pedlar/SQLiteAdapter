package org.notlocalhost.sqliteadapter.parsers;

import org.notlocalhost.sqliteadapter.annotations.ColumnName;
import org.notlocalhost.sqliteadapter.annotations.DELETE;
import org.notlocalhost.sqliteadapter.annotations.INSERT;
import org.notlocalhost.sqliteadapter.annotations.QUERY;
import org.notlocalhost.sqliteadapter.annotations.Table;
import org.notlocalhost.sqliteadapter.annotations.TableName;
import org.notlocalhost.sqliteadapter.annotations.UPDATE;
import org.notlocalhost.sqliteadapter.annotations.Where;
import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by pedlar on 8/31/14.
 */
/*
    TODO:
        Safeguard against having @Where and @ColumnName on paramater.
        Safeguard Class against implements Schema interface.
 */
public class AnnotationParser {
    Method mMethod;
    Class<?> mClass;

    public AnnotationParser(Method method) {
        mMethod = method;
    }

    public AnnotationParser(Class<?> clzz) {
        mClass = clzz;
    }

    public MethodInfo createMethodInfo() {
        MethodInfo methodInfo = new MethodInfo();
        methodInfo.setReturnClass(mMethod.getGenericReturnType());
        parseMethodAnnotations(methodInfo);
        parseMethodParamaterAnnotations(methodInfo);
        return methodInfo;
    }

    public ClassInfo createClassInfo() {
        ClassInfo classInfo = new ClassInfo();
        parseClassAnnotations(classInfo);
        return classInfo;
    }

    private void parseMethodAnnotations(MethodInfo methodInfo) {
        for (Annotation methodAnnotation : mMethod.getAnnotations()) {
            Class<? extends Annotation> annotationType = methodAnnotation.annotationType();

            if (annotationType == INSERT.class) {
                methodInfo.setType(MethodInfo.Type.INSERT);

            } else if (annotationType == UPDATE.class) {
                methodInfo.setType(MethodInfo.Type.UPDATE);
            } else if (annotationType == DELETE.class) {
                methodInfo.setType(MethodInfo.Type.DELETE);
            } else if (annotationType == QUERY.class) {
                methodInfo.setType(MethodInfo.Type.QUERY);
            } else if (annotationType == TableName.class) {
                String tableName = ((TableName) methodAnnotation).value();
                if(tableName == null) {
                    throw new IllegalArgumentException("@TableName requires the tables name");
                }
                methodInfo.setTableName(tableName);
            } else if (annotationType == Table.class) {
                methodInfo.setTableConstructor(true);
            }
        }
    }

    private void parseMethodParamaterAnnotations(MethodInfo methodInfo) {
        Class<?>[] parameterTypes = mMethod.getParameterTypes();

        Annotation[][] parameterAnnotationArrays = mMethod.getParameterAnnotations();
        int count = parameterAnnotationArrays.length;

        for (int i = 0; i < count; i++) {
            Class<?> parameterType = parameterTypes[i];
            Annotation[] parameterAnnotations = parameterAnnotationArrays[i];
            if (parameterAnnotations != null) {
                for (Annotation parameterAnnotation : parameterAnnotations) {
                    Class<? extends Annotation> annotationType = parameterAnnotation.annotationType();
                    FieldInfo fieldInfo = parseFieldAnnotation(parameterAnnotation, annotationType);

                    methodInfo.addParamater(i, fieldInfo);
                }
            }
        }
    }

    private FieldInfo parseFieldAnnotation(Annotation fieldAnnotation, Class<? extends Annotation> annotationType) {
        FieldInfo fieldInfo = new FieldInfo();
        if (annotationType == ColumnName.class) {
            String name = ((ColumnName) fieldAnnotation).value();
            fieldInfo.setName(name);
            fieldInfo.setType(FieldInfo.Type.COLUMN_NAME);
        } else if (annotationType == Where.class) {
            String name = ((Where) fieldAnnotation).value();
            FieldInfo.Logical logical = ((Where)fieldAnnotation).logical();
            FieldInfo.Operator[] operators = ((Where)fieldAnnotation).operator();
            fieldInfo.setName(name);
            fieldInfo.setLogical(logical);
            fieldInfo.setOperators(operators);
            fieldInfo.setType(FieldInfo.Type.WHERE);
        }
        return fieldInfo;
    }

    private void parseClassAnnotations(ClassInfo classInfo) {
        for(Field field : mClass.getFields()) {
            FieldInfo parseField = new FieldInfo();
            for(Annotation fieldAnnotation : field.getAnnotations()) {
                Class<? extends Annotation> annotationType = fieldAnnotation.annotationType();
                parseField = parseFieldAnnotation(fieldAnnotation, annotationType);
            }
            parseField.setField(field);
            parseField.setFieldType(field.getType());
            classInfo.addClassField(parseField);
        }

        classInfo.setClassName(mClass.getName());
        classInfo.setClassType(mClass.getGenericSuperclass());
        if(mClass.isAnnotationPresent(TableName.class)) {
            TableName tableName = mClass.getAnnotation(TableName.class);
            classInfo.setTableName(tableName.value());
        } else {
            classInfo.setTableName(mClass.getName());
        }
    }
}
