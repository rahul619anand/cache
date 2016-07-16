package com.work.cache;

public interface TGCache<ReadReturnType, ValueType, ClientType, MultiReadReturnType, KeysType, MapType> {

	/**
	 * This method reads value associated to key
	 *
	 *  
	   		  @param  key  key to be read
	          @param  set (optional) if applicable to cache
	          @return      the value associated to key.
	 */
	public ReadReturnType read(String key,String... set);

	/**
	 * This method reads value associated to keys
	 *
	 *  
	   		  @param  keyNames  key to be read
	          @param  set (optional) if applicable to cache
	          @return      the values associated to key.
	 */
	public MultiReadReturnType multiRead(KeysType keyNames,String... set) ;

	/**
	 * This method writes  key value pair to cache 
	 *
	 *  
	          @param  key  keys value pairs to be cached
	          @param  value   value associated to  cache
	          @param  set (optional) if applicable to cache
	 */
	
	public void write(String key ,ValueType value, String... set );

	/**
	 * This method writes multiple key value pairs to cache
	 *
	 *  
	          @param  map  keys value pairs to be cached
	          @param  set (optional) if applicable to cache
	 */
	public void multiWrite(MapType map, String... set);

	/**
	 * This method invalidates a key in the cache
	 *
	 *  
	          @param  key  key to be invalidated
	          @param  set (optional) if applicable to cache
	 */
	public void invalidateKey(String key, String... set);

	/**
	 * This method returns the client instance to access  methods specific to cache
	 *
	 *  
	          @param  set (optional) if applicable to cache
	          @return      the client instance of the cache
	 */
	public ClientType getCacheClient();

}
