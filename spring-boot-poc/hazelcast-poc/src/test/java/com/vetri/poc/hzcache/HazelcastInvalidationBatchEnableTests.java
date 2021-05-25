package com.vetri.poc.hzcache;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.vetri.poc.hzcache.config.DefaultCacheConfig;
import com.vetri.poc.hzcache.config.HazelcastCacheConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HazelcastInvalidationBatchEnableTests {

    @Test
    public void testWithInvalidationBatchEnabled() throws Exception {
        System.setProperty("hazelcast.map.invalidation.batch.enabled", "true");
        doTest();
    }

    @Test
    public void testWithoutInvalidationBatchEnabled() throws Exception {
        System.setProperty("hazelcast.map.invalidation.batch.enabled", "false");
        doTest();
    }

    @AfterAll
    public void shutdownNodes() {
        Hazelcast.shutdownAll();
    }

    protected void doTest() throws Exception {
        // first config for normal cluster member
        Config c1 = new Config();
        c1.getNetworkConfig().setPort(5709);

        // second config for super client
        Config c2 = new Config();
        c2.getNetworkConfig().setPort(5710);

        // map config is the same for both nodes
        MapConfig testMapCfg = new MapConfig("test");
        NearCacheConfig ncc = new NearCacheConfig();
        ncc.setTimeToLiveSeconds(10);
        testMapCfg.setNearCacheConfig(ncc);

        c1.addMapConfig(testMapCfg);
        c2.addMapConfig(testMapCfg);

        // start instances
        HazelcastInstance h1 = Hazelcast.newHazelcastInstance(c1);
        HazelcastInstance h2 = Hazelcast.newHazelcastInstance(c2);

        IMap<Object, Object> mapH1 = h1.getMap("test");
        IMap<Object, Object> mapH2 = h2.getMap("test");

        // initial filling
        mapH1.put("a", -1);

        assertEquals(mapH1.get("a"), -1);
        assertEquals(mapH2.get("a"), -1);

        int updatedH1 = 0, updatedH2 = 0, runs = 0;
        for (int i = 0; i < 5; i++) {
            mapH1.put("a", i);

            // without this short sleep sometimes the nearcache is updated in time, sometimes not
            Thread.sleep(100);

            runs++;
            if (mapH1.get("a").equals(i)) {
                updatedH1++;
            }
            if (mapH2.get("a").equals(i)) {
                updatedH2++;
            }
        }

        assertEquals(runs, updatedH1);
        assertEquals(runs, updatedH2);
    }
}
