package org.notlocalhost.sqliteadapter;

import org.notlocalhost.sqliteadapter.models.MethodInfo;

/**
 * Created by pedlar on 8/31/14.
 */
class InvocationFactory {
    static Invocation manufactur(MethodInfo method, Object[] args) {
        switch (method.getType()) {
            case INSERT:
                return new InsertInvocation(method, args);
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
