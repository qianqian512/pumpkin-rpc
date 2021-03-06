package org.hum.pumpkin.exchange;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hum.pumpkin.common.url.URL;
import org.hum.pumpkin.common.url.URLConstant;
import org.hum.pumpkin.exchange.client.DefaultExchangeClient;
import org.hum.pumpkin.exchange.client.ExchangeClient;
import org.hum.pumpkin.exchange.server.DefaultExchangeServer;
import org.hum.pumpkin.exchange.server.ExchangeServer;
import org.hum.pumpkin.exchange.server.ExchangeServerHandler;
import org.hum.pumpkin.transport.client.Client;
import org.hum.pumpkin.transport.server.Server;

public abstract class AbstractExchanger implements Exchanger {

	private static final Map<String, ExchangeClient> EXCHANGE_CLIENTS = new ConcurrentHashMap<>();
	private final Object createLock = new Object();
	private volatile ExchangeServer server;
	
	@Override
	public ExchangeServer bind(URL url, ExchangeServerHandler serverHandler) {
		// TODO test版本 待完善
		Server transporterServer = doBind(url, serverHandler);
		// TODO synchronized，保证ExchangeServer为单例
		if (server == null) {
			server = new DefaultExchangeServer(transporterServer);
		}
		return server;
	}

	protected abstract Server doBind(URL url, ExchangeServerHandler serverHandler);

	@Override
	public ExchangeClient connect(URL url) {
		Boolean isShare = url.getBoolean(URLConstant.IS_SHARE_CONNECTION);
		if (isShare == null || isShare) {
			String serviceKey = getServiceKey(url);
			ExchangeClient exchangeClient = EXCHANGE_CLIENTS.get(serviceKey);
			if (exchangeClient != null) {
				return exchangeClient;
			}
			synchronized (createLock) {
				if (EXCHANGE_CLIENTS.containsKey(serviceKey)) {
					return EXCHANGE_CLIENTS.get(serviceKey);
				}
				exchangeClient = new DefaultExchangeClient(doConnect(url));
				EXCHANGE_CLIENTS.put(serviceKey, exchangeClient);
				return exchangeClient;
			}
		} else {
			return new DefaultExchangeClient(doConnect(url));
		}
	}

	protected abstract Client doConnect(URL url);
	
	private String getServiceKey(URL url) {
		return url.getHost() + ":" + url.getPort(); 
	}
}
