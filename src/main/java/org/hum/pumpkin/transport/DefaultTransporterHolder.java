package org.hum.pumpkin.transport;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.hum.pumpkin.serviceloader.ServiceLoaderHolder;
import org.hum.pumpkin.threadpool.ThreadPoolFactory;

public class DefaultTransporterHolder implements TransporterHolder {

	private final Map<String, Transporter> transpoterMap = new ConcurrentHashMap<>();
	private final ExecutorService executorService = ServiceLoaderHolder.loadByCache(ThreadPoolFactory.class).create();

	@Override
	public void join(String address, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public void remove(String address, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public Response send(final Request request) {

		Transporter transporter = getTransporter(request);

		if (!request.isAsyn()) {
			Object result = transporter.invoke(request.getData());
			return new Response(result, null);
		}

		try {
			Future<Object> future = executorService.submit(new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					return transporter.invoke(request.getData());
				}
			});
			Object result = future.get();
			return new Response(result, null);
		} catch (Exception ce) {
			return new Response(null, ce);
		}
	}

	private Transporter getTransporter(Request request) {
		return transpoterMap.get(request.getUrl() + ":" + request.getPort());
	}
}
