package org.hum.pumpkin.transport.impl.jdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.hum.pumpkin.common.serviceloader.ExtensionLoader;
import org.hum.pumpkin.common.url.URL;
import org.hum.pumpkin.common.url.URLConstant;
import org.hum.pumpkin.logger.Logger;
import org.hum.pumpkin.logger.LoggerFactory;
import org.hum.pumpkin.serialization.Serialization;
import org.hum.pumpkin.transport.client.Client;
import org.hum.pumpkin.transport.message.Message;
import org.hum.pumpkin.transport.message.MessageBack;

public class JdkKeepAliveClient implements Client {

	private static final Logger logger = LoggerFactory.getLogger(JdkKeepAliveClient.class);

	private Serialization serialization; 

	private Socket socket;
	private InputStream inputStream;
	private OutputStream outputStream;
	private URL url;
	private String host;
	private int port;

	public JdkKeepAliveClient(URL url) throws UnknownHostException, IOException {
		this.host = url.getHost();
		this.port = url.getPort();
		this.url = url;
		this.serialization = ExtensionLoader.getExtensionLoader(Serialization.class).get(url.getString(URLConstant.SERIALIZATION));
		socket = new Socket(host, port);
		logger.info("socket " + host + ":" + port + " build connection success.");
		socket.setKeepAlive(true);
		outputStream = socket.getOutputStream();
		inputStream = socket.getInputStream();
	}

	@Override
	public MessageBack send(Message message) {
		serialization.serialize(outputStream, message);
		return serialization.deserialize(inputStream, MessageBack.class);
	}

	@Override
	public void close() {
		if (inputStream != null) {
			try {
				inputStream.close();
				inputStream = null;
			} catch (IOException e) {
				logger.error("close tcp [" + host + ":" + port + "] inputstream error", e);
			}
		}
		if (outputStream != null) {
			try {
				outputStream.close();
				outputStream = null;
			} catch (IOException e) {
				logger.error("close tcp [" + host + ":" + port + "] outputstream error", e);
			}
		}
		if (socket != null) {
			try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				logger.error("close tcp [" + host + ":" + port + "] socket connection error", e);
			}
		}
	}

	@Override
	public URL getURL() {
		return url;
	}
}
