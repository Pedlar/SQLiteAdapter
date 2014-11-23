package org.notlocalhost.sqliteadapter.internal.command;

import org.notlocalhost.sqliteadapter.AdapterContext;
import org.notlocalhost.sqliteadapter.models.MethodInfo;

/**
 * Created by pedlar on 8/31/14.
 *
 */
public final class InvocationFactory {
    public static Invocation manufacture(AdapterContext adapterContext, MethodInfo method, Object[] args) {
        switch (method.getType()) {
            case INSERT:
                return new InsertUpdateInvocation(adapterContext, method, args, InsertUpdateInvocation.InvocationType.INSERT);
            case DELETE:
                return new DeleteInvocation(method, args);
            case UPDATE:
                return new InsertUpdateInvocation(adapterContext, method, args, InsertUpdateInvocation.InvocationType.UPDATE);
            case QUERY:
                return new QueryInvocation(method, args);
            default:
                throw new RuntimeException("Method called against service with no Type Annotation.");
        }
    }
}
