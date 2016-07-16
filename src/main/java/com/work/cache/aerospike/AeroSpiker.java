package com.work.cache.aerospike;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Host;
import com.aerospike.client.Key;
import com.aerospike.client.Operation;
import com.aerospike.client.Record;
import com.aerospike.client.policy.WritePolicy;
import com.work.cache.TGCache;
import com.work.cache.aerospike.beans.ColumnData;

public class AeroSpiker extends AeroSpikeConfig implements
		TGCache<Record, ColumnData, AerospikeClient, Record[], List<String>, Map<String, Map<String, Object>>> {
	public AerospikeClient client;
	private static final Logger LOGGER = LoggerFactory.getLogger(AeroSpiker.class);

	public AeroSpiker() {
		initConfig();
		Host[] hosts = aeroSpikeHosts.stream().map(ip -> new Host(ip, aerospikePort)).toArray(size -> new Host[size]);
		setDefaultClientPolicies();
		client = new AerospikeClient(clientPolicy, hosts);
		configureLog();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is required
	 */
	@Override
	public void multiWrite(Map<String, Map<String, Object>> map, String... set) {
		try {
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				Key key = new Key(nameSpace, set[0], entry.getKey());
				Bin[] bins = new Bin[entry.getValue().size()];
				int array = 0;
				for (Map.Entry<String, Object> entryBinData : entry.getValue().entrySet()) {
					Bin bin = new Bin((String) entryBinData.getValue(), entryBinData.getValue());
					bins[array] = bin;
					array++;
				}
				WritePolicy writePolicy = getDefaultWritePolicy();
				writePolicy.expiration = ttl;
				client.put(writePolicy, key, bins);
			}
		} catch (Exception exception) {
			LOGGER.error("Can't cache for set ", set, exception);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Column Data should be used to pass values for a specific key, set is
	 * required
	 */
	@Override
	public void write(String keyName, ColumnData col, String... set) {

		try {
			Key key = new Key(nameSpace, set[0], keyName);
			Bin bin = new Bin(col.getColName(), col.getColValue());
			WritePolicy writePolicy = getDefaultWritePolicy();
			writePolicy.expiration = ttl;
			client.put(writePolicy, key, bin);
			LOGGER.debug("Cached bean with key ", keyName);
		} catch (Exception exception) {
			LOGGER.error("Can't cache key ", keyName, exception);
		}

	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is required
	 */
	@Override
	public Record read(String keyName, String... set) {
		Record record = null;
		try {
			Key key = new Key(nameSpace, set[0], keyName);
			record = client.get(readPolicy, key);
		} catch (Exception exception) {
			LOGGER.error("Error in reading value from cache", exception);
		}
		return record;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is required
	 */
	@Override
	public Record[] multiRead(List<String> keyNames, String... set) {
		Record[] record = null;
		try {
			int size = keyNames.size();
			Key[] keys = new Key[size];
			for (int i = 0; i < size; i++) {
				keys[i] = new Key(nameSpace, set[0], i + 1);
			}
			record = client.get(batchPolicy, keys);
		} catch (Exception exception) {
			LOGGER.error("Error in reading value from cache for keynames", keyNames, exception);
		}
		return record;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * specific methods related to guava caching can be accessed via invoking
	 * the method
	 */
	@Override
	public AerospikeClient getCacheClient() {
		return this.client;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is required
	 */
	@Override
	public void invalidateKey(String keyName, String... set) {
		try {
			Key key = new Key(nameSpace, set[0], keyName);
			client.delete(writePolicy, key);
		} catch (Exception exception) {
			LOGGER.error("Error in invalidating key " + keyName, exception);
		}
	}

	/**
	 * This method closes connection to aerospike client
	 * 
	 */
	public void closeClient() {
		if (this.client != null) {
			this.client.close();
		}
	}

	/**
	 * This method checks if a client connection exists
	 *
	 */
	protected void isConnected() throws Throwable {
		this.client.isConnected();
	}

	/**
	 * This method checks if a key entry exist in the set
	 *
	 * 
	 * @param keyName
	 *            key to be read
	 * @param set
	 *            set in the cache
	 * @return the boolean flag
	 */
	public boolean exists(String set, String keyName) {
		Key key = new Key(nameSpace, set, keyName);
		return client.exists(readPolicy, key);
	}

	/**
	 * This method increments bin value for the key
	 *
	 * 
	 * @param set
	 *            set in the cache
	 * @param keyName
	 *            key to be read
	 * @param binName
	 *            bin associated to key
	 * @return the record value
	 */
	public Record increment(String set, String keyName, String binName) {
		Key key = new Key(nameSpace, set, keyName);
		Bin bin = new Bin(binName, 1);
		return client.operate(writePolicy, key, Operation.add(bin));
	}

	/**
	 * This method performs multiple operations
	 *
	 * 
	 * @param set
	 *            set in the cache
	 * @param keyName
	 *            key to be read
	 * @param operations
	 *            array of operations to be performed
	 * @return the record value
	 */
	public Record multiOps(String set, String keyName, Operation[] operations) {
		Key key = new Key(nameSpace, set, keyName);
		Record record = client.operate(null, key, operations);
		return record;
	}

	/**
	 * This method writes key value pair to cache
	 *
	 * 
	 * @param key
	 *            keys value pairs to be cached
	 * @param col
	 *            value associated to cache
	 * @param expireAfterTime
	 *            ttl in seconds
	 * @param set
	 *            set of the cache (required)
	 */
	public void write(String keyName, ColumnData col, int expireAfterTime, String... set) {

		try {
			Key key = new Key(nameSpace, set[0], keyName);
			Bin bin = new Bin(col.getColName(), col.getColValue());
			WritePolicy writePolicy = getDefaultWritePolicy();
			writePolicy.expiration = expireAfterTime;
			client.put(writePolicy, key, bin);
			LOGGER.debug("Cached bean with key ", keyName);
		} catch (Exception exception) {
			LOGGER.error("Can't cache key ", keyName, exception);
		}

	}

	/**
	 * This method writes multiple key value pairs to cache
	 *
	 * 
	 * @param map
	 *            keys value pairs to be cached
	 * @param expireAfterTime
	 *            ttl in seconds
	 * @param set
	 *            set of the cache (required)
	 */
	public void multiWrite(Map<String, Map<String, Object>> map, int expireAfterTime, String... set) {
		try {
			for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
				Key key = new Key(nameSpace, set[0], entry.getKey());
				Bin[] bins = new Bin[entry.getValue().size()];
				int array = 0;
				for (Map.Entry<String, Object> entryBinData : entry.getValue().entrySet()) {
					Bin bin = new Bin((String) entryBinData.getValue(), entryBinData.getValue());
					bins[array] = bin;
					array++;
				}
				WritePolicy writePolicy = getDefaultWritePolicy();
				writePolicy.expiration = expireAfterTime;
				client.put(writePolicy, key, bins);
			}
		} catch (Exception exception) {
			LOGGER.error("Can't cache for set ", set, exception);
		}
	}

}
