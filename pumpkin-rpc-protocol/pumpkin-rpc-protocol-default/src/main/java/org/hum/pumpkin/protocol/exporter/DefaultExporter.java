package org.hum.pumpkin.protocol.exporter;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.hum.pumpkin.common.serviceloader.ServiceLoaderHolder;
import org.hum.pumpkin.common.url.URL;
import org.hum.pumpkin.exchange.Exchanger;
import org.hum.pumpkin.exchange.Request;
import org.hum.pumpkin.exchange.Response;
import org.hum.pumpkin.exchange.server.ExchangeServer;
import org.hum.pumpkin.exchange.server.ExchangeServerHandler;
import org.hum.pumpkin.protocol.invoker.RpcInvocation;
import org.hum.pumpkin.protocol.invoker.RpcResult;
import org.hum.pumpkin.threadpool.ThreadPoolFactory;
import org.hum.pumpkin.transport.server.ServerHandler;

public class DefaultExporter<T> implements Exporter<T>{

	private static final Exchanger EXCHANGER = ServiceLoaderHolder.loadByCache(Exchanger.class);
	private static final ExecutorService EXECUTOR_SERVICE = ServiceLoaderHolder.loadByCache(ThreadPoolFactory.class).create();
	private T ref;
	private ExchangeServer exchangeServer;
	private ExchangeServerHandler serverHandler = null;
	
	
	public DefaultExporter(Class<T> classType, T instances, URL url) {
		this.ref = instances;
		this.serverHandler = new ExchangeServerHandler() {

			@Override
			public Response handler(Request request) {
				Future<RpcResult> future = EXECUTOR_SERVICE.submit(new Tasker((RpcInvocation) request.getData(), ref));
				try {
					RpcResult result = future.get();
					return new Response(request.getId(), result, null);
				} catch (InterruptedException | ExecutionException e) {
					return new Response(request.getId(), null, e);
				}
			}

			@Override
			public ServerHandler getServerHandler() {
				return null;
			}
		};
		this.exchangeServer = EXCHANGER.bind(url, serverHandler);
	}

	@Override
	public void unexport() {
		exchangeServer.close();
	}
}