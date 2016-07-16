package com.work.cache.guava;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.work.cache.BaseConfig;

public class GuavaConfig extends BaseConfig {
	protected static int expireAfterWrite = 900;
	protected static int maxSize = 100000;
	private static final Logger LOGGER = LoggerFactory.getLogger(GuavaConfig.class);

	protected void initConfig() {
		try {
			Map<String, Object> config = result.get("guavaConf");
			if (null != config) {
				expireAfterWrite = (int) config.get("cache.guava.ttl");
				maxSize = (int) config.get("cache.guava.maxSize");
			}
			LOGGER.debug("Guava  Configurations : [ " + "ExpireAfterWrite = " + expireAfterWrite + "MaxSize = "
					+ expireAfterWrite + " ]");
		} catch (Exception e) {
			LOGGER.error("Exception", e);
		}
	}
}
