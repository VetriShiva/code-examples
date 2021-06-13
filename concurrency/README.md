# Java Concurrency

```
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 

Volatile variable
    Can be used with variables. Don't use cache.
    By default copies of local variables stores in heap for every thread.
    Volatile variable tells threads to call general copy of variable to get value.
    Keyword volatile do following:
        guarantied that first will be write operations, then read
        only read/write operations are atomic!
        increment and decrement are not synchronized! i++ i--
        the value of variable can be used safely by every thread	

collections
    CopyOnWriteArrayList
        Thread-safe analogue of ArrayList and Set
        All modifying operations (add, set, remove) will create new copy of collection.
        {@link CopyOnWriteArrayList} has additional methods unlike the {@link CopyOnWriteArraySet}.
    ConcurrentMap
        Use this ConcurrentMap instead HashMap,TreeMap as it has better support for multithreading and scalability
        Data are represented as segments broken by hash of keys
        Thread-safe analogue of TreeMap with multithreading support
        TreeMap} with better support for multithreading and scalability


kunal-saxena		
    https://www.linkedin.com/pulse/java-concurrency-cyclicbarrier-example-kunal-saxena/
    https://www.linkedin.com/pulse/java-concurrency-utility-countdown-latch-kunal-saxena/
    https://www.linkedin.com/pulse/concurrency-semaphore-real-life-kunal-saxena/

    https://www.youtube.com/channel/UC20UQGc_nSoen6lCRMpLQYA/playlists	
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV  

References

    https://github.com/akraskovski/java-concurrency-practice  

```