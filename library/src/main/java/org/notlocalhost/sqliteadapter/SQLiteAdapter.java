package org.notlocalhost.sqliteadapter;

import android.content.Context;
import android.util.Log;

import org.notlocalhost.sqliteadapter.internal.command.Invocation;
import org.notlocalhost.sqliteadapter.internal.command.InvocationFactory;
import org.notlocalhost.sqliteadapter.models.ClassInfo;
import org.notlocalhost.sqliteadapter.models.MethodInfo;
import org.notlocalhost.sqliteadapter.parsers.AnnotationParser;
import org.notlocalhost.sqliteadapter.parsers.TypeToken;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    private ExecutorService mThreadExecutor = Executors.newCachedThreadPool();

    private SQLiteAdapter(Context context, String databaseName, int versionNumber, List<ClassInfo> classSchemas) {
        mSqliteHelper = new SQLiteHelper(context, databaseName, versionNumber, classSchemas, this);

        for(ClassInfo classInfo : classSchemas) {
            mClassInfoCache.put(classInfo.getClassType(), classInfo);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T create(Class<T> service) {
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
            if(Collection.class.isAssignableFrom(typeToken.getRawType())) {
                typeToken = TypeToken.get(HelperUtils.getGenericsElementType(typeToken.getType()));
            }
            ClassInfo classInfo = mClassInfoCache.get(typeToken.getType());
            if(classInfo == null) {
                AnnotationParser annotationParser = new AnnotationParser(typeToken.getRawType());
                classInfo = annotationParser.createClassInfo();
                mClassInfoCache.put(typeToken.getRawType(), classInfo);
            }
            return classInfo;
        }
    }

    public SQLiteHelper getSqlHelper() {
        return mSqliteHelper;
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

            return proxyInvoke(method.getName(), methodInfo, args);
        }
    }

    private Object proxyInvoke(final String methodName, final MethodInfo methodInfo, final Object[] args) {
        Future<Object> responseFuture = mThreadExecutor.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                Log.d("SQLiteAdapter", "-> @" + methodInfo.getType().name() + " " + methodName + "(" + Arrays.toString(args) + ")");
                Invocation invocation = InvocationFactory.manufacture(SQLiteAdapter.this, methodInfo, args);

                long start = System.currentTimeMillis();
                Object responseObject = invocation.invokeCommand(SQLiteAdapter.this, mSqliteHelper);

                long totalMs = System.currentTimeMillis() - start;
                Log.d("SQLiteAdapter", "<- @" + methodInfo.getType().name() + " " + methodName + ": "
                        + (responseObject != null ? responseObject.toString() : "NULL")
                        + " " + totalMs + "ms");
                return responseObject;
            }
        });

        if(RxJavaSupport.isRxJavaIncluded()) {
            if(methodInfo.isObservable()) {
                return RxJavaSupport.createObservable(responseFuture);
            }
        }

        try {
            return responseFuture.get();
        } catch (InterruptedException e) {
            Log.e("SQLiteAdapter", "InterruptedException in Future handling SQL Call", e);
        } catch (ExecutionException e) {
            Log.e("SQLiteAdapter", "ExecutionException in Future handling SQL Call", e);
        }
        return null;
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
