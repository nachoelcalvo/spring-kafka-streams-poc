package com.personal.kafka.springkafkastreams;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PageCountSink {

    @StreamListener
    public void process(@Input(AnalyticsBinding.PAGE_COUNT_IN) KTable<String, Long> counts){

        counts.toStream()
                .foreach((key, value) -> log.info(key + " = " + value));
    }
}
