package com.work.cache.adaptor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.work.cache.TGCache;
import com.work.cache.injects.CacheModule;

public class CacheAdaptor {
	private static Injector injector=Guice.createInjector(new CacheModule());

	public static TGCache getInstance(String cacheType) {
		TGCache cache = (TGCache) injector.getInstance(Key.get(TGCache.class,Names.named(cacheType)));
		return cache;
	}
}
