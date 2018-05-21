package com.personal.kafka.springkafkastreams;

import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AnalyticsBinding {

    String PAGE_VIEW_OUT = "pvout";
    String PAGE_VIEW_IN = "pvin";
    String PAGE_COUNT_OUT= "pcout";
    String PAGE_COUNT_IN= "pcin";
    String PAGE_COUNT_MATERIALIZED = "pcmv";

    @Output(PAGE_VIEW_OUT)
    MessageChannel pageViewsOut();

    @Input(PAGE_VIEW_IN)
    KStream<String, PageViewEvent> pageViewIn();

    @Output(PAGE_COUNT_OUT)
    KStream<String, Long> pageCountOut();

    @Input(PAGE_COUNT_IN)
    KTable<String, Long> pageCountIn();
}
