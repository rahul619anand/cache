package com.work.cache.guava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildCacheImpl implements BuildCache {
	private static final Logger LOGGER = LoggerFactory.getLogger(BuildCacheImpl.class);

	@Override
	public String buildCache() {
		LOGGER.debug("building cache");
		return "test value";
	}

}
