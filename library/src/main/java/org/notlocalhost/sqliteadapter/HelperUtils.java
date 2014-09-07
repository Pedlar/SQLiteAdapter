package org.notlocalhost.sqliteadapter;

import android.content.ContentValues;

import org.notlocalhost.sqliteadapter.models.FieldInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;
import org.notlocalhost.sqliteadapter.models.WhereStatement;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedlar on 9/6/14.
 */
public class HelperUtils {
    private HelperUtils() {
        // No Instances
    }

    public static void addContentValue(ContentValues contentValues, String name, Object arg) {
        if(arg == null) {
            return;
        }

        if(arg instanceof String) {
            contentValues.put(name, (String)arg);
        } else if(arg instanceof Byte) {
            contentValues.put(name, (Byte)arg);
        } else if(arg instanceof Short) {
            contentValues.put(name, (Short)arg);
        } else if(arg instanceof Integer) {
            contentValues.put(name, (Integer)arg);
        } else if(arg instanceof Long) {
            contentValues.put(name, (Long)arg);
        } else if(arg instanceof Float) {
            contentValues.put(name, (Float)arg);
        } else if(arg instanceof Double) {
            contentValues.put(name, (Double)arg);
        } else if(arg instanceof Boolean) {
            contentValues.put(name, (Boolean)arg);
        } else if(arg instanceof byte[]) {
            contentValues.put(name, (byte[])arg);
        }
    }

    public static WhereStatement constructWhere(MethodInfo methodInfo, Object[] args) {
        WhereStatement statement = null;

        StringBuilder whereBuilder = new StringBuilder();
        List<String> whereArgList = new ArrayList<String>();
        boolean firstWhere = false;
        for(int i = 0; i < args.length; i++) {
            FieldInfo paramInfo = methodInfo.getParamater(i);
            if(paramInfo.getType() == FieldInfo.Type.WHERE) {
                if(statement == null) {
                    statement = new WhereStatement();
                }
                if(!firstWhere) {
                    firstWhere = true;
                } else {
                    whereBuilder.append(paramInfo.getLogical().name());
                    whereBuilder.append(" ");
                }
                whereBuilder.append(methodInfo.getParamater(i).getName());
                whereBuilder.append(methodInfo.getParamater(i).getOperatorString());
                whereBuilder.append("?");

                whereArgList.add(args[i].toString());
            }
        }

        if(statement != null) {
            statement.whereClause = whereBuilder.toString();
            statement.whereArgs = whereArgList.toArray(new String[whereArgList.size()]);
        }
        return statement;
    }

    public static Type getCollectionElementType(Type context) {
        if (context instanceof WildcardType) {
            context = ((WildcardType)context).getUpperBounds()[0];
        }
        if (context instanceof ParameterizedType) {
            return ((ParameterizedType) context).getActualTypeArguments()[0];
        }
        return Object.class;
    }
}
