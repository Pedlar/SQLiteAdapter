package org.notlocalhost.sqliteadapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;
import org.notlocalhost.sqliteadapter.parsers.AnnotationParser;
import org.notlocalhost.sqliteadapter.parsers.TypeToken;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Main Entrypoint class for the SQLiteAdapter
 *
 * Created by pedlar on 8/30/14.
 */
public class SQLiteAdapter implements AdapterContext {
    protected final Map<Method, MethodInfo> mMethodInfoCache = new HashMap<Method, MethodInfo>();
    protected final Map<Type, ClassInfo> mClassInfoCache = new HashMap<Type, ClassInfo>();
    protected SQLiteHelper mSqliteHelper;

    private SQLiteAdapter(Context context, String databaseName, int versionNumber, List<ClassInfo> classSchemas) {
        mSqliteHelper = new SQLiteHelper(context, databaseName, versionNumber, classSchemas, this);

        for(ClassInfo classInfo : classSchemas) {
            mClassInfoCache.put(classInfo.getClassType(), classInfo);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service) {
        Log.d(SQLiteAdapter.class.getCanonicalName(), "create: " + service.getClass().getCanonicalName());
        if (!service.isInterface()) {
            throw new IllegalArgumentException("Only interfaces are supported.");
        }
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new ProxyHandler());
    }

    public MethodInfo getMethodInfo(Method method) {
        synchronized (mMethodInfoCache) {
            MethodInfo methodInfo = mMethodInfoCache.get(method);
            if(methodInfo == null) {
                AnnotationParser annotationParser = new AnnotationParser(method);
                methodInfo = annotationParser.createMethodInfo();
                mMethodInfoCache.put(method, methodInfo);
            }
            return methodInfo;
        }
    }

    public ClassInfo getClassInfo(Type type) {
        synchronized (mClassInfoCache) {
            TypeToken<?> typeToken = TypeToken.get(type);
            ClassInfo classInfo = mClassInfoCache.get(typeToken.getRawType());
            if(classInfo == null) {
                AnnotationParser annotationParser = new AnnotationParser(typeToken.getRawType());
                classInfo = annotationParser.createClassInfo();
                mClassInfoCache.put(typeToken.getRawType(), classInfo);
            }
            return classInfo;
        }
    }

    private class ProxyHandler implements InvocationHandler {
        ProxyHandler() {

        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            MethodInfo methodInfo = getMethodInfo(method);

            return proxyInvoke(methodInfo, args);
        }
    }

    private Object proxyInvoke(MethodInfo methodInfo, Object[] args) {
        Invocation invocation = InvocationFactory.manufactur(methodInfo, args);
        Log.d(SQLiteAdapter.class.getCanonicalName(), "proxyInvoke: " + invocation.getClass().getCanonicalName());
        Object responseObject = invocation.invokeCommand(this, mSqliteHelper);

        return responseObject;
    }

    public static class Builder {
        private int versionNumber = 1;
        private String databaseName = "sqliteadapter_default.db";
        private Context context;
        private List<ClassInfo> schemaClasses = new ArrayList<ClassInfo>();
        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public <T>Builder setSchemas(Class<T>... schemaClasses) {
            if(schemaClasses != null) {
                for(Class<T> schemaClass : schemaClasses) {
                    AnnotationParser annotationParser = new AnnotationParser(schemaClass);
                    this.schemaClasses.add(annotationParser.createClassInfo());
                }
            }
            return this;
        }
        public SQLiteAdapter build() {
            return new SQLiteAdapter(context, databaseName, versionNumber, schemaClasses);
        }

        public Builder setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }
    }
}
