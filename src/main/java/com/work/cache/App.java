package com.work.cache;

import com.aerospike.client.Record;
import com.work.cache.adaptor.CacheAdaptor;
import com.work.cache.aerospike.beans.ColumnData;
import com.work.cache.guava.GuavaCache;

public class App {

	public static void main(String args[]) {

		// Guava
		TGCache cache = CacheAdaptor.getInstance("guava");
		cache.write("key", "value", null);
		String value = (String) cache.read("key");
		System.out.println("Result: " + value);

		// AeroSpike
		cache = CacheAdaptor.getInstance("guava");
		ColumnData col = new ColumnData();
		col.setColName("colName");
		col.setColValue("colValue");
		cache.write("key", col, "cacheSet");
		Record result = (Record) cache.read("key", "cacheSet");
		System.out.println("Result: " + result);
	}
}
