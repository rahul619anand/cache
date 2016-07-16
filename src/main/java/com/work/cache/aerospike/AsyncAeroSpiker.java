package com.work.cache.aerospike;

import java.util.List;

import com.aerospike.client.Bin;
import com.aerospike.client.Host;
import com.aerospike.client.Key;
import com.aerospike.client.async.AsyncClient;
import com.aerospike.client.listener.RecordArrayListener;
import com.aerospike.client.listener.RecordListener;
import com.aerospike.client.listener.WriteListener;
import com.aerospike.client.policy.WritePolicy;

public class AsyncAeroSpiker extends AeroSpikeConfig {
	AsyncClient client = null;

	public AsyncAeroSpiker() {
		Host[] hosts = aeroSpikeHosts.stream().map(ip -> new Host(ip, aerospikePort)).toArray(size -> new Host[size]);
		setAsyncClientPolicy();
		client = new AsyncClient(asyncClientPolicy, hosts);
		configureLog();
	}

	public void write(String set, WriteListener listener, String keyName, String colName, String colValue) {
		Key key = new Key(nameSpace, set, keyName);
		Bin bin = new Bin(colName, colValue);
		client.put(writePolicy, listener, key, bin);
	}

	public void cache(String set, int ttl, WriteListener listener, String keyName, String colName, String colValue) {
		Key key = new Key(nameSpace, set, keyName);
		Bin bin = new Bin(colName, colValue);
		WritePolicy writePolicy = getDefaultWritePolicy();
		writePolicy.expiration = ttl;
		client.put(writePolicy, listener, key, bin);
	}

	public AsyncClient getClient() {
		return this.client;
	}

	public void closeClient() {
		if (this.client != null) {
			this.client.close();
		}
	}

	protected void isConnected() throws Throwable {
		this.client.isConnected();
	}

	public void read(String set, RecordListener recordListener, String keyName) {
		Key key = new Key(nameSpace, set, keyName);
		client.get(readPolicy, recordListener, key);
	}

	public void read(String set, RecordListener recordListener, String keyName, String bin) {
		Key key = new Key(nameSpace, set, keyName);
		client.get(readPolicy, recordListener, key, bin);
	}

	public void read(String set, RecordArrayListener recordListener, List<String> keyNames) {
		int size = keyNames.size();
		Key[] keys = new Key[size];
		for (int i = 0; i < size; i++) {
			keys[i] = new Key(nameSpace, set, i + 1);
		}
		client.get(batchPolicy, recordListener, keys);
	}

	public void read(String set, String keyName, String[] binArray) {
		Key key = new Key(nameSpace, set, keyName);
		client.get(readPolicy, key, binArray);
	}

}
