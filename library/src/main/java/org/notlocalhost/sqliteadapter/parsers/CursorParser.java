package org.notlocalhost.sqliteadapter.parsers;


import android.database.Cursor;

import org.notlocalhost.sqliteadapter.AdapterContext;
import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by pedlar on 9/1/14.
 */
public class CursorParser {
    public Object objectFromCursor(AdapterContext context, MethodInfo methodInfo, Cursor cursor) {
        try {
            TypeToken<Object> typeToken = TypeToken.get(methodInfo.getReturnClass());
            Object returnObject = constructResponseObject(typeToken.getRawType());

            ClassInfo classInfo = context.getClassInfo(methodInfo.getReturnClass());
            while(cursor.moveToNext()) {
                for (FieldInfo fieldInfo : classInfo.getClassFields()) {
                    Field field = fieldInfo.getField();
                    int cursorIndex = cursor.getColumnIndex(fieldInfo.getName());
                    TypeToken<Object> fieldTypeToken = TypeToken.get(fieldInfo.getFieldType());

                    Object value = new Object();
                    if (Integer.class.isAssignableFrom(fieldTypeToken.getRawType())
                            || int.class.isAssignableFrom(fieldTypeToken.getRawType())) {
                        value = cursor.getInt(cursorIndex);
                    } else if (Short.class.isAssignableFrom(fieldTypeToken.getRawType())
                            || short.class.isAssignableFrom(fieldTypeToken.getRawType())) {
                        value = cursor.getShort(cursorIndex);
                    } else if (Integer.class.isAssignableFrom(fieldTypeToken.getRawType())
                                || int.class.isAssignableFrom(fieldTypeToken.getRawType())) {
                        value = cursor.getDouble(cursorIndex);
                    } else if (Long.class.isAssignableFrom(fieldTypeToken.getRawType())
                            || long.class.isAssignableFrom(fieldTypeToken.getRawType())) {
                        value = cursor.getLong(cursorIndex);
                    } else if (Float.class.isAssignableFrom(fieldTypeToken.getRawType())
                            || float.class.isAssignableFrom(fieldTypeToken.getRawType())) {
                        value = cursor.getFloat(cursorIndex);
                    } else if (byte[].class.isAssignableFrom(fieldTypeToken.getRawType())) {
                        value = cursor.getBlob(cursorIndex);
                    } else if (String.class.isAssignableFrom(fieldTypeToken.getRawType())) {
                        value = cursor.getString(cursorIndex);
                    }

                    field.set(returnObject, value);
                }
            }

            return returnObject;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private <T>T constructResponseObject(Class<? super T> rawType) {
        try {
            final Constructor<? super T> constructor = rawType.getDeclaredConstructor();
            if(!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            Object[] args = null;
            return (T)constructor.newInstance(args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
