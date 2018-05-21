package com.personal.kafka.springkafkastreams;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.*;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PageViewEventsProcessor {

    @StreamListener
    @SendTo(AnalyticsBinding.PAGE_COUNT_OUT)
    public KStream<String, Long> process(@Input(AnalyticsBinding.PAGE_VIEW_IN)KStream<String, PageViewEvent> events){

        return events.filter((key, pageViewEvent) -> pageViewEvent.getDuration() > 10)
                .map((s, pageViewEvent) -> new KeyValue<>(pageViewEvent.getPage(), pageViewEvent.getUserId()))
                .groupByKey()
                .count(Materialized.as(AnalyticsBinding.PAGE_COUNT_MATERIALIZED))
                .toStream();
    }
}
