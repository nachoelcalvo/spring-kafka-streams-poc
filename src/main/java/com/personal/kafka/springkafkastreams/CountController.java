package com.personal.kafka.springkafkastreams;


import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreType;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.cloud.stream.binder.kafka.streams.QueryableStoreRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CountController {

    private QueryableStoreRegistry registry;

    public CountController(QueryableStoreRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/counts")
    public Map<String, Long> getCounts(){

        HashMap<String, Long> counts = new HashMap<>();

        ReadOnlyKeyValueStore<String, Long> storeCounts = this.registry.getQueryableStoreType(AnalyticsBinding.PAGE_COUNT_MATERIALIZED, QueryableStoreTypes.keyValueStore());
        KeyValueIterator<String, Long> all = storeCounts.all();

        while(all.hasNext()){
            KeyValue<String, Long> value = all.next();
            counts.put(value.key, value.value);
        }
        return counts;
    }
}
