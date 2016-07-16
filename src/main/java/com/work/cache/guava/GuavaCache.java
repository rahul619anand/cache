package com.work.cache.guava;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.work.cache.TGCache;

import lombok.Getter;

public class GuavaCache extends GuavaConfig implements
		TGCache<String, String, Cache<String, String>, ImmutableMap<String, String>, Iterable<? extends String>, Map<? extends String, ? extends String>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GuavaCache.class);

	public Cache<String, String> cache;

	@Inject
	@Getter
	BuildCache icache;

	public GuavaCache() {
		initConfig();
		cache = CacheBuilder.newBuilder().maximumSize(maxSize).expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS)
				.build();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is not required for guava caching
	 */
	@Override
	public String read(String key, String... set) {
		String value = null;
		try {
			value = cache.get(key, new Callable<String>() {
				@Override
				public String call() throws Exception {
					return icache.buildCache();
				}
			});
		} catch (ExecutionException e) {
			LOGGER.error("Exception", e);
		}
		return value;

	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is not required for guava caching
	 */
	@Override
	public ImmutableMap<String, String> multiRead(Iterable<? extends String> keys, String... set) {
		return cache.getAllPresent(keys);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is not required for guava caching
	 */
	@Override
	public void write(String key, String value, String... set) {
		cache.put(key, value);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is not required for guava caching
	 */
	@Override
	public void multiWrite(Map<? extends String, ? extends String> map, String... key) {
		cache.putAll(map);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * 
	 * specific methods related to guava caching can be accessed via invoking
	 * the method
	 */
	@Override
	public Cache<String, String> getCacheClient() {
		return cache;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * set is not required for guava caching
	 */
	@Override
	public void invalidateKey(String key, String... set) {
		cache.invalidate(key);

	}

	/**
	 * This method reads value associated to key , if present
	 *
	 * 
	 * @param key
	 *            key to be read
	 * @return the value associated to key.
	 */
	public String readIfPresent(String key) {
		return cache.getIfPresent(key);
	}

	/**
	 * This method performs any pending maintenance operations needed by the
	 * cache
	 * 
	 */
	public void cleanup() {
		cache.cleanUp();
	}

	/**
	 * This method invalidates all key in the cache
	 * 
	 */
	public void invalidateAllKeys() {
		cache.invalidateAll();
	}

	/**
	 * This method invalidates list of keys in the cache
	 *
	 * 
	 * @param key
	 *            keys to be invalidated
	 */
	public void invalidateListOfKeys(Iterable<? extends String> key) {
		cache.invalidateAll(key);
	}

	/**
	 * This method returns the map view of cache
	 *
	 * 
	 * @return the map object
	 */
	public ConcurrentMap<String, String> getMapView() {
		return cache.asMap();
	}

	/**
	 * This method provides cache size
	 *
	 * 
	 * @return the cache size
	 */
	public long cacheSize() {
		return cache.size();
	}

	/**
	 * This method returns various statistics related to cache
	 *
	 * 
	 * @return the stats object
	 */
	public CacheStats cacheStats() {
		return cache.stats();
	}

}
