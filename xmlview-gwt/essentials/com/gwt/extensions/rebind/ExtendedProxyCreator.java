package com.gwt.extensions.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.user.rebind.StringSourceWriter;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.SerializableTypeOracle;
import com.gwt.extensions.client.caching.IMetadataCache;
import com.gwt.extensions.client.remoteservice.ExtendedRemoteServiceProxy;
import com.gwt.extensions.client.remoteservice.StaticResponse;

public class ExtendedProxyCreator extends ProxyCreator {
    private static final String OpenBrace = "{";
    private static final String NewLine = "\n";

    public ExtendedProxyCreator(JClassType serviceIntf) {
        super(serviceIntf);
    }

    @Override
    protected void generateProxyMethod(SourceWriter w, SerializableTypeOracle serializableTypeOracle, JMethod syncMethod, JMethod asyncMethod) {
        String methodSource = getMethodSource(serializableTypeOracle, syncMethod, asyncMethod);
        String callbackName = getCallbackName(asyncMethod);
        String modifiedMethodSource = alter(methodSource, syncMethod.getName(), callbackName, getCacheability(syncMethod, asyncMethod));
        log(modifiedMethodSource);
        w.println();
        w.print(modifiedMethodSource);
        w.println();
    }

    @Override
    protected Class<? extends RemoteServiceProxy> getProxySupertype() {
        return ExtendedRemoteServiceProxy.class;
    }

    private long getCacheability(JMethod syncMethod, JMethod asyncMethod) {
        if (syncMethod.isAnnotationPresent(StaticResponse.class))
            return syncMethod.getAnnotation(StaticResponse.class).duration();
        if (asyncMethod.isAnnotationPresent(StaticResponse.class))
            return asyncMethod.getAnnotation(StaticResponse.class).duration();
        return IMetadataCache.NOT_CACHEABLE;
    }

    private String getCallbackName(JMethod asyncMethod) {
        JParameter[] asyncParams = asyncMethod.getParameters();
        JParameter callbackParam = asyncParams[asyncParams.length - 1];
        return callbackParam.getName();
    }

    private String getMethodSource(SerializableTypeOracle serializableTypeOracle, JMethod syncMethod, JMethod asyncMethod) {
        SourceWriter stringWriter = new StringSourceWriter();
        super.generateProxyMethod(stringWriter, serializableTypeOracle, syncMethod, asyncMethod);
        return stringWriter.toString();
    }

    private String alter(String methodSource, String methodName, String callbackName, long cacheability) {
        final int indexOfBrace = methodSource.indexOf(OpenBrace) + 1;
        String declaration = methodSource.substring(0, indexOfBrace);
        String body = methodSource.substring(indexOfBrace, methodSource.length());
        String insertLine = "\n " + renderServiceHelpers(methodName, callbackName, cacheability) + " \n";
        return join(declaration, insertLine, body);
    }

    private String renderServiceHelpers(String name, String callbackName, long cacheability) {
        String line1 = "com.google.gwt.user.client.rpc.AsyncCallback cc = com.gwt.extensions.client.remoteservice.ServiceCallArbitrer.instance().arbitrate(REMOTE_SERVICE_INTERFACE_NAME, \"" + name + "\", " + callbackName + ", " + cacheability + "L, this);";
        String line2 = "if(null == cc) return;";
        String line3 = callbackName + " = cc;";
        return join(line1, NewLine, line2, NewLine, line3);
    }

    private void log(String log) {
//        System.out.print(log);
    }

    private String join(String... fragments) {
        String combined = "";
        for (String fragment : fragments)
            combined += fragment;
        return combined;
    }
}
