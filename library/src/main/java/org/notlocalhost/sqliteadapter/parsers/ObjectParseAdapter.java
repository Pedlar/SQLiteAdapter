package org.notlocalhost.sqliteadapter.parsers;

import android.database.Cursor;

import org.notlocalhost.sqliteadapter.AdapterContext;
import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.FieldInfo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by pedlar on 9/7/14.
 */
class ObjectParseAdapter extends ParseAdapter<Object> {
    ClassInfo mClassInfo;
    CursorParser mParser;
    AdapterContext aContext;
    TypeToken<Object> typeToken;

    protected ObjectParseAdapter(AdapterContext context, CursorParser parser, TypeToken<Object> typeToken) {
        mClassInfo = context.getClassInfo(typeToken.getType());
        mParser = parser;
        aContext = context;
        this.typeToken = typeToken;
    }

    @Override
    protected Object parse(Cursor cursor) {
        try {
            if(cursor.getPosition() == -1) cursor.moveToNext();
            Object object = constructResponseObject(typeToken.getRawType());

            for (FieldInfo fieldInfo : mClassInfo.getClassFields()) {
                Field field = fieldInfo.getField();
                int cursorIndex = cursor.getColumnIndex(fieldInfo.getName());
                TypeToken<Object> fieldTypeToken = TypeToken.get(fieldInfo.getFieldType());

                Object value = null;

                if (fieldInfo.isForeignKey()) {
                    String rowId = cursor.getString(cursor.getColumnIndex("rowid"));
                    value = mParser.queryForeignKey(aContext, rowId, fieldInfo);
                } else {
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
                }

                field.set(object, value);
            }

            return object;
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
