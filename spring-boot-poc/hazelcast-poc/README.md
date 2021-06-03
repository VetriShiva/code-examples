# Kafka POC

```

VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV  

References
	https://medium.com/@thanh_tran/cacheable-annotation-in-spring-d8d347ad72fd
	https://dzone.com/articles/how-to-scale-hazelcast-cluster-with-docker-compose
	https://github.com/hazelcast/hazelcast-code-samples/blob/master/hazelcast-integration/docker-compose/docker-compose.yml

	default cache manager - org.springframework.cache.concurrent.ConcurrentMapCacheManager
	
	https://dzone.com/articles/hazelcast-setup
	https://dzone.com/articles/hazelcast-mancenter
	
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV    
 
Hazelcast Setup
    https://download.hazelcast.com/management-center/hazelcast-management-center-3.11.zip
    https://download.hazelcast.com/download.jsp?version=hazelcast-3.11&p=
        
    configure hazelcast-3.11\bin\hazelcast.xml
    <management-center enabled="true">http://localhost:8080/hazelcast-mancenter</management-center>    
    Start -> hazelcast-3.11\bin>start.bat
    Start -> hazelcast-management-center-3.11>start.bat
    
    http://localhost:8080/hazelcast-mancenter/login.html
    	siva
    	admin143
    
    https://dzone.com/articles/safeguard-spring-app-from-cache-failure
    https://stackoverflow.com/questions/44870963/enable-disable-caching-for-specific-methods-in-a-service-based-on-configuration/44871041#44871041
    	@Cacheable(cacheNames="book", condition="#cached == false") public Book findBook(boolean cached)
    	
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 

POC Ref
    How to enable JUnit 5 in new Spring Boot project
    	https://dev.to/martinbelev/how-to-enable-junit-5-in-new-spring-boot-project-29a8


VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 
        
Notes

Hazelcast

	cache, cluster, NoSQL key/Value Store, App Scaling, Messageing - all put together as Data Grid
	
	max no of partition is 271
	hash > partition > storage nodes
	default primary data one data & duplicate data from another nodes
	
	com.hazlecast.core.IMaP interface is inherited from java.util.Map
	IMap is distributed version of java.util.Map, more processing power
	
	default is dtributed - add multiple records - create local variable to avoid frequent transaction
	if you add one by one then there are more transaction and sync with other node too & performance decrease
	
	1 Hazelcast Instance = 1 Storage Node = 1 Cluster Member
	
	by default all data in memory & to avoid data loss we have to have persistence
		class implement com.hazlecast.core.MapStore
		config hazlecast to above MapStore
		MapStore - Sync Vs Async(batch update)
		
	Searching  
		- Predicate, com.hazlecast.query.Predicate - implement own condition
		- SQLPredicate
		- Indexing
			- CustomersMap.addIndex("dob",true)
			- using Hazelcast config object(Prefered)
			
	Concurrency
		KeyLevelLocks - CustomersMap.tryLock()
		EntryProcessors (prefered) - EntryProcessor, EntryBackupProcessor Interface
			Thread safe, Hazelcast take care, Reduce complexity
			only one Entry Processor will execute at a time
			
	Aggregate - sum with distributed is fast - com.hazlecast.mapreduce.aggregation.Aggregattion
	
	Data Affinity - put all related data in single partition to avoid network latancy
	
	DataStructures - Set, List, Queues, Topics, MultiMap, Locks, Custom DataStructure
		- set - no batch & performance issue
		- Queue Vs Topic
			queue - producer & consumer model - consumer will be one
			topic - publish & subscriber model - consumer will be multiple
		- MultiMap - default is Set, can change to list using MultiMapConfig
		- IAtamicLong, ISemaphore, IAtamicReference, ICountDownLatch, IdGenerator
		
	Events
		- Entry Listeners (Map, MultiMap)
			- EntryAdded, EntryUpdated, EntryDeleted Listeners
		- Continuous Queries - Entry Listeners with filtering
			- listener will assign based on key, SQLPredicate
		- Item Listeners (Set, List, Queue)
		- Partition Lost Listeners
			- default backup is 1 & we can config using setAsyncBackupCount
			- add listner config 
		- Membership, Ditributed Object, Migration, LifeCycle, Client Listeners
		
	Efficent Network
		- Data Serialization, java.io.Serialize is poor(slow, verion rollback)
		- com.hazlecast.nio.serialization.DataSerializable, IdentifiedDataSerializable,Portalbe,VersionPortable, Custom Serialization
		- VersionPortable - multiple version object, rolling relase of app with zero downtime
		
	Debug & Monitor
		- Functional issue
		- Cluster performance issue
		- Remote Debug flags, log4j, YourKit, JProfiler
		- Monitor - Hazelcast managment center
		- Per Node JMX via JMX flags
		- VisualVM plugin - install MBeans 
		
	contant string - put it interface
 
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 

Todo

Hazelcast issue
    Solution 1 : Restart the pod via code
    Solution 2 : Continues retry until you get active connection - bottleneck of business service
    Solution 3 : AOP -> Catch exception & respond based on business cases
    Solution 4 : Using Hazelcast LifecycleListener -> Disable global cache status
    Solution 5 : CacheErrorHandler -> Suppress the exception
    Solution 6 : K8s way deployment

	
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 

```
