package org.notlocalhost.sqliteadapter;

import org.notlocalhost.sqliteadapter.models.MethodInfo;

/**
 * Created by pedlar on 8/31/14.
 */
class InvocationFactory {
    static Invocation manufacture(AdapterContext adapterContext, MethodInfo method, Object[] args) {
        switch (method.getType()) {
            case INSERT:
                return new InsertInvocation(adapterContext, method, args);
            case DELETE:
                return new DeleteInvocation(method, args);
            case UPDATE:
                return new UpdateInvocation(method, args);
            case QUERY:
                return new QueryInvocation(method, args);
            default:
                throw new RuntimeException("Method called against service with no Type Annotation.");
        }
    }
}
