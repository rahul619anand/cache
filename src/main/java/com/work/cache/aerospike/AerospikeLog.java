package com.work.cache.aerospike;

import org.slf4j.*;

import com.aerospike.client.Log.Callback;
import com.aerospike.client.Log.Level;

public class AerospikeLog implements Callback {

	private static final Logger LOGGER = LoggerFactory.getLogger(AerospikeLog.class);

	@Override
	public void log(Level level, String message) {
		LOGGER.debug("Aerospike callback log:" + level + "message:" + message);

	}
}
