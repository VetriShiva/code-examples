# Kafka POC

```

VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV  

References
Setting up and Running Apache Kafka on Windows OS
    https://dzone.com/articles/running-apache-kafka-on-windows-os

Generate Multiple Consumer Groups Dynamically With Spring-Kafka    
    https://dzone.com/articles/kafka-tutorial-generate-multiple-consumer-groups-with-spring-kafka
    
Spring Boot Kafka Multiple Consumers Example
    https://howtodoinjava.com/kafka/multiple-consumers-example/
    
Spring Boot Kafka JsonSerializer Example    
    https://howtodoinjava.com/kafka/spring-boot-jsonserializer-example    

Spring Boot Implementation For Apache Kafka With Kafka┬áTool
    https://www.maestralsolutions.com/spring-boot-implementation-for-apache-kafka-with-kafka-tool/
    
Java: set timeout on a certain block of code?    
    https://stackoverflow.com/questions/5715235/java-set-timeout-on-a-certain-block-of-code
    
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV    
 
Kafka Setup
    https://www.apache.org/dyn/closer.cgi?path=/kafka/2.8.0/kafka_2.12-2.8.0.tgz
    https://dzone.com/articles/running-apache-kafka-on-windows-os

    Troubleshoot
        https://stackoverflow.com/questions/48834927/the-input-line-is-too-long-when-starting-kafka
            fix kafka-run-class.bat
            
    Local Setup Location
        D:\DevSetup\Docker\splunk
                
    kafka-producer-perf-test.sh --broker-list localhost:9092 --topic kafkaTopic --messages 100
    
    start zookeeper
        D:
        start "ZooKeeper" zkserver
        ::pause
    start kafka      
        D:
        cd D:\DevSetup\Docker\splunk\kafka
        start "Kafka"  .\bin\windows\kafka-server-start.bat .\config\server.properties
        ::pause        

VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 

POC Ref
    https://dzone.com/articles/kafka-consumer-and-multi-threading

    https://stackoverflow.com/questions/58889112/spring-kafkalistener-and-concurrency
        Kafka doesn't work that way; you need at least as many partitions as consumers (controlled by concurrency in the spring container).

        Also, only one consumer (in a group) can consume from a partition at a time so, even if you increase the partitions, records in the same partition behind the "stuck" consumer will not be received by other consumers.

    Spring Boot and Kafka Configuration Tuning
        https://dzone.com/articles/spring-boot-and-kafka-configuration-tuning

    https://www.maestralsolutions.com/spring-boot-implementation-for-apache-kafka-with-kafka-tool/
    https://dzone.com/articles/running-apache-kafka-on-windows-os


    https://dzone.com/articles/kafka-tutorial-generate-multiple-consumer-groups-with-spring-kafka	
    https://dzone.com/articles/synchronous-kafka-using-spring-request-reply-1
    
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 
        
Todo
    https://docs.spring.io/spring-kafka/docs/2.5.3.RELEASE/reference/html/#event-consumption
    
Dynamic Kafka Listener
    https://stackoverflow.com/questions/63664549/create-message-liseners-dynamically-for-the-topics
    https://stackoverflow.com/questions/53715268/kafka-spring-how-to-create-listeners-dynamically-or-in-a-loop
    https://stackoverflow.com/questions/50325766/implementing-shared-logic-for-multiple-kafkalisteners-in-spring-kafka
       
MethodKafkaListenerEndpoint
    https://stackoverflow.com/questions/50325766/implementing-shared-logic-for-multiple-kafkalisteners-in-spring-kafka/50331279#50331279
    https://github.com/bkatwal/kafka-util
    https://bikas-katwal.medium.com/start-stop-kafka-consumers-or-subscribe-to-new-topic-programmatically-using-spring-kafka-2d4fb77c9117
        
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV 



```