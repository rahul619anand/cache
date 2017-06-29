
# Cache Facade over Guava and Aerospike Implementations



1) Configuring cache environment :

Step 1 : Specify the cache config. parameters in yaml resource.

	Example : cacheConfig.yaml

	aerospikeConf:
	 cache.aerospike.hosts: ["localhost"]
	 cache.aerospike.port: 3000
	 cache.aerospike.namespace: test
	 cache.aerospike.maxThreads: 100
	 cache.aerospike.timeOut: 2000
	 cache.aerospike.asyncMaxCommands: 100
	 cache.aerospike.log.enabled: false
	 cache.aerospike.ttl: 900

	guavaConf:
	 cache.guava.ttl: 900
	 cache.guava.maxSize: 100000
	 
	 
Step 2 : Place the config resource in the classpath of the application using this utility as a dependency.

Step 3 : Pass the config. resource name as a parameter while running the application.

	Example :
	
	./activator "start -Dhttp.port=9000 -Dcache.resource=cacheConfig.yaml"  
	



