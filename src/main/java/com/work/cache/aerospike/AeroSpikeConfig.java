package com.work.cache.aerospike;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aerospike.client.Log;
import com.aerospike.client.async.AsyncClientPolicy;
import com.aerospike.client.policy.BatchPolicy;
import com.aerospike.client.policy.ClientPolicy;
import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.QueryPolicy;
import com.aerospike.client.policy.WritePolicy;
import com.work.cache.BaseConfig;

public class AeroSpikeConfig extends BaseConfig {
	ClientPolicy clientPolicy;
	AsyncClientPolicy asyncClientPolicy;
	WritePolicy writePolicy = null;
	QueryPolicy queryPolicy = null;
	Policy readPolicy = null;
	BatchPolicy batchPolicy = null;
	protected static String nameSpace;
	protected static List<String> aeroSpikeHosts;
	protected static int aerospikePort;
	protected static int maxThreads;
	protected static int timeOut;
	protected static int asyncMaxCommands;
	protected static boolean logEnabled;
	protected static int ttl;
	private static final Logger LOGGER = LoggerFactory.getLogger(AeroSpikeConfig.class);

	protected void initConfig() {
		try {
			Map<String, Object> config = result.get("aerospikeConf");
			if (null != config) {
				nameSpace = (String) config.get("cache.aerospike.namespace");
				aeroSpikeHosts = (List<String>) config.get("cache.aerospike.hosts");
				aerospikePort = (int) config.get("cache.aerospike.port");
				maxThreads = (int) config.get("cache.aerospike.maxThreads");
				timeOut = (int) config.get("cache.aerospike.timeOut");
				asyncMaxCommands = (int) config.get("cache.aerospike.asyncMaxCommands");
				ttl = (int) config.get("cache.aerospike.ttl");
				logEnabled = (boolean) config.get("cache.aerospike.log.enabled");
			}
			LOGGER.debug("Aerospike  Configurations : [ " + "Namespace = " + nameSpace + "Hosts = " + aeroSpikeHosts
					+ "Port = " + aerospikePort + "Max Threads = " + maxThreads + "Timeout = " + timeOut
					+ "Async Max. Commands = " + asyncMaxCommands + "TTL= " + ttl + "Log Enabled = " + logEnabled
					+ " ]");
		} catch (Exception e) {
			LOGGER.error("Exception in config. initialization ", e);
		}
	}

	protected void setDefaultClientPolicies() {
		clientPolicy = new ClientPolicy();
		clientPolicy.maxThreads = maxThreads;
		clientPolicy.queryPolicyDefault = getDefaultQueryPolicy();
		clientPolicy.readPolicyDefault = getDefaultReadPolicy();
		clientPolicy.writePolicyDefault = getDefaultWritePolicy();
		clientPolicy.batchPolicyDefault = getDefaultBatchPolicy();
	}

	private Policy getDefaultReadPolicy() {
		QueryPolicy readPolicy = new QueryPolicy();
		readPolicy.timeout = timeOut;
		readPolicy.maxRetries = 1;
		readPolicy.sleepBetweenRetries = 50;
		return readPolicy;
	}

	protected BatchPolicy getDefaultBatchPolicy() {
		BatchPolicy batchPolicy = new BatchPolicy();
		batchPolicy.timeout = timeOut;
		return batchPolicy;
	}

	protected WritePolicy getDefaultWritePolicy() {
		WritePolicy writePolicy = new WritePolicy();
		writePolicy.timeout = timeOut;
		writePolicy.maxRetries = 1;
		writePolicy.sleepBetweenRetries = 50;
		return writePolicy;
	}

	protected QueryPolicy getDefaultQueryPolicy() {
		QueryPolicy readPolicy = new QueryPolicy();
		readPolicy.timeout = timeOut;
		readPolicy.maxRetries = 1;
		readPolicy.sleepBetweenRetries = 50;
		return readPolicy;
	}

	public void setAsyncClientPolicy() {
		asyncClientPolicy = new AsyncClientPolicy();
		asyncClientPolicy.asyncMaxCommands = asyncMaxCommands;
		asyncClientPolicy.asyncWritePolicyDefault = getDefaultWritePolicy();
		asyncClientPolicy.asyncBatchPolicyDefault = getDefaultBatchPolicy();
		asyncClientPolicy.asyncReadPolicyDefault = getDefaultQueryPolicy();
	}

	protected void configureLog() {
		if (logEnabled) {
			Log.Callback aerospikeLog = new AerospikeLog();
			Log.setCallback(aerospikeLog);
			Log.setLevel(Log.Level.DEBUG);
		}
	}
}
