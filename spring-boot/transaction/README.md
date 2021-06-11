# Transaction

```
Database transaction
    single logical unit of work which accesses and possibly modifies the contents of a database

OrganizationService Employee Join Workflow
    OrganizationService -> Insert Employee & Insert HealthInsurance
OrganizationService Employee Exit Workflow
    OrganizationService -> Delete Employee & Delete HealthInsurance

application transaction 
    It is a sequence of application actions that are considered as a single logical unit
    
Action
    it is all or none for a logical unit of work
By default the spring boot transaction is auto commit

Transaction Propagation
    indicates if any component or service will or will not participate in transaction
    behave if the calling calling component/service already has or does not have a transaction created already

    Propagation Types			
        REQUIRED (default)	
            Always executes in a transaction. If there is any existing transaction it uses it. If none exists then only a new one is created
        SUPPORTS	
            It may or may not run in a transaction. If current transaction exists then it is supported. If none exists then gets executed with out transaction.
        NOT_SUPPORTED	
            Always executes without a transaction. If there is any existing transaction it gets suspended
        REQUIRES_NEW	
            Always executes in a new transaction. If there is any existing transaction it gets suspended
        NEVER	
            Always executes with out any transaction. It throws an exception if there is an existing transaction
        MANDATORY	
            Always executes in a transaction. If there is any existing transaction it is used. If there is no existing transaction it will throw an exception.
            
Transaction Isolation
    two transactions concurrently act on the same database entity
    behaviour or state of the database when one transaction is working on database entity and then some other concurrent transaction tries to simultaneously access/edit the same database entity.
            
    Isolation Level
        SERIALIZABLE
            This is total isolation
            performance will be low and deadlock might occur
        REPEATABLE_READ
            till the first transaction is committed the existing records cannot be changed by second transaction but new records can be added.
            MySQL the default isolation level is REPEATABLE_READ
        READ_COMMITTED
            able to fetch commited records from other sessions
        READ_UNCOMMITTED
            able to fetch uncommited records from other sessions
        
        REPEATABLE_READ Vs READ_COMMITTED
            https://stackoverflow.com/questions/4034976/difference-between-read-commited-and-repeatable-read
            REPEATABLE_READ
                The state of the database is maintained from the start of the transaction. If you retrieve a value in session1, then update that value in session2, retrieving it again in session1 will return the same results. Reads are repeatable.
            READ_COMMITTED
                Within the context of a transaction, you will always retrieve the most recently committed value. If you retrieve a value in session1, update it in session2, then retrieve it in session1again, you will get the value as modified in session2. It reads the last committed row.

    Read phenomena
        Dirty reads
            read UNCOMMITED data from another transaction
        Non-repeatable reads
            read COMMITTED data from an UPDATE query from another transaction
        Phantom reads
            read COMMITTED data from an INSERT or DELETE query from another transaction
        
         Non-Repeatable Read Vs Phantom Read
            https://stackoverflow.com/questions/11043712/what-is-the-difference-between-non-repeatable-read-and-phantom-read
        
            Non-repeatable reads are when your transaction reads committed "UPDATES" from another transaction. The same row now has different values than it did when your transaction began.
            Phantom reads are similar but when reading from committed "INSERTS and/or DELETES" from another transaction. There are new rows or rows that have disappeared since you began the transaction.
            For "non-repeatable read", row-locking is needed.
            For "phantom read"，scoped-locking is needed, even a table-locking.
				
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV  

References

    https://www.javainuse.com/spring/boot-transaction
    https://www.javainuse.com/spring/boot-transaction-isolation
    https://www.javainuse.com/spring/boot-rollback
    
    Non-Repeatable Read Vs Phantom Read
        https://stackoverflow.com/questions/11043712/what-is-the-difference-between-non-repeatable-read-and-phantom-read

    REPEATABLE_READ Vs READ_COMMITTED
        https://stackoverflow.com/questions/4034976/difference-between-read-commited-and-repeatable-read
        
```

# Concurrency and Locking With JPA

```

References
    https://www.baeldung.com/jpa-pessimistic-locking
    https://www.baeldung.com/java-jpa-transaction-locks
    https://dzone.com/articles/concurrency-and-locking-with-jpa-everything-you-ne
    
Implementation
    Optimistic Locking 
    https://blog.mimacom.com/testing-optimistic-locking-handling-spring-boot-jpa/
    Pessimistic Locking
    https://blog.mimacom.com/testing-pessimistic-locking-handling-spring-boot-jpa/    
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV  

Optimistic Locking
    @Version field - numeric (int, long, short) or java.sql.Timestamp field
    OptimisticLockException based on this version field
    Each succesful transaction this version value increased
    
Optimistic Lock Modes
    OPTIMISTIC 
        obtains the read lock for the entities with @Version property. 
    OPTIMISTIC_FORCE_INCREMENT 
        obtains the read lock for the entities with @Version property and increments the value of the property.
    
Pessimistic Locking
    JPA creates a transaction that obtains a lock on the data until the transaction is completed.
    Pessimistic locking can be very useful when the data is frequently accessed and modified by multiple transactions.
    Keep in mind that using pessimistic locking may result in decreased application performance, if the entities are not susceptible to frequent modifications.
    
Pessimistic Lock Modes
    PESSIMISTIC_READ 
        obtains a long-term read lock on the data to prevent the data from being updated or deleted. Other transactions may read the data during the lock, but will not be able to modify or delete the data.
    PESSIMISTIC_FORCE_INCREMENT 
        obtains a long-term read lock on the data to prevent the data from being updated or deleted. Also, increments the value of @Version property.
    PESSIMISTIC_WRITE 
        obtains a long-term read lock on the data to prevent the data from being read, updated or deleted.
        
PessimisticLockException,LockTimeoutException 

```