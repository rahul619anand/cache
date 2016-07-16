package com.work.cache;

import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.work.cache.injects.CacheModule;

public class BaseConfig {

	private static Injector injector;
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseConfig.class);
	protected static Map<String, Map<String, Object>> result;

	static {
		injector = Guice.createInjector(new CacheModule());
		Yaml yaml = new Yaml();
		final String configFileName = System.getProperty("cache.resource");

		try (InputStream input = BaseConfig.class.getClassLoader().getResourceAsStream(configFileName)) {

			result = (Map<String, Map<String, Object>>) yaml.load(input);

		} catch (Exception e) {
			LOGGER.error("Exception ", e);
		}

	}

}
