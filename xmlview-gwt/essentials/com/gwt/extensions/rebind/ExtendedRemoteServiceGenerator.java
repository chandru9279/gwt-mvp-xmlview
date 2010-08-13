package com.gwt.extensions.rebind;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.rpc.ProxyCreator;
import com.google.gwt.user.rebind.rpc.ServiceInterfaceProxyGenerator;

public class ExtendedRemoteServiceGenerator extends ServiceInterfaceProxyGenerator {
    @Override
    protected ProxyCreator createProxyCreator(JClassType remoteService) {
        return new ExtendedProxyCreator(remoteService);
    }

}
