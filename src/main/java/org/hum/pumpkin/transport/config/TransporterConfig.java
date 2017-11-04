package org.hum.pumpkin.transport.config;

/**
 * TransporterConfig是传输层URL的实现
 */
public class TransporterConfig {

	private String address;
	private int port;
	private boolean isKeepAlive;

	public TransporterConfig(String address, int port) {
		this.address = address;
		this.port = port;
		this.isKeepAlive = true;
	}

	public String getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public boolean isKeepAlive() {
		return isKeepAlive;
	}
}
