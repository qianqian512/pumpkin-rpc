package org.hum.pumpkin.invoker.direct;

import org.hum.pumpkin.invoker.RpcInvocation;
import org.hum.pumpkin.invoker.RpcResult;
import org.hum.pumpkin.protocol.URL;
import org.hum.pumpkin.serviceloader.ServiceLoaderHolder;
import org.hum.pumpkin.transport.Request;
import org.hum.pumpkin.transport.Response;
import org.hum.pumpkin.transport.TransporterHolder;

public class DirectInvoker<T> extends AbstractDirectInvoker<T>{
	
	private final TransporterHolder transporterHolder = ServiceLoaderHolder.loadByCache(TransporterHolder.class);
	private Class<T> classType;
	private URL url;
	
	public DirectInvoker(Class<T> classType, URL url) {
		this.classType = classType;
		this.url = url;
	}

	@Override
	public Class<T> getType() {
		return classType;
	}

	@Override
	public RpcResult invoke(RpcInvocation invocation) {
		Request request = new Request(url.getAddress(), url.getPort(), invocation);
		Response response = transporterHolder.send(request);
		return new RpcResult(response, null);
	}
}
