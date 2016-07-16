package com.work.cache.injects;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.work.cache.TGCache;
import com.work.cache.aerospike.AeroSpiker;
import com.work.cache.constants.CacheConstants;
import com.work.cache.guava.BuildCache;
import com.work.cache.guava.BuildCacheImpl;
import com.work.cache.guava.GuavaCache;

public class CacheModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(BuildCache.class).to(BuildCacheImpl.class);
		bind(TGCache.class).annotatedWith(Names.named(CacheConstants.GUAVA)).to(GuavaCache.class);
		bind(TGCache.class).annotatedWith(Names.named(CacheConstants.AEROSPIKE)).to(AeroSpiker.class);

	}

}
