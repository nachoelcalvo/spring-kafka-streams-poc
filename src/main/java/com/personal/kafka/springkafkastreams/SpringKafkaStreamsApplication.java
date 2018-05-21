package com.personal.kafka.springkafkastreams;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@EnableBinding(AnalyticsBinding.class)
@SpringBootApplication
public class SpringKafkaStreamsApplication {


	@Component
	public static class PageViewEventSource implements ApplicationRunner{

		private MessageChannel pageViewsOut;

		public PageViewEventSource(AnalyticsBinding analyticsBinding) {
			this.pageViewsOut = analyticsBinding.pageViewsOut();
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {

			List<String> names = Arrays.asList("jcasado", "nacalvo", "jicas");
			List<String> sites = Arrays.asList("site", "blog", "linkedin");

			Runnable runnable = () -> {

				PageViewEvent pageViewEvent = new PageViewEvent(names.get(new Random().nextInt(names.size())),
						sites.get(new Random().nextInt(sites.size())),
						Math.random() > 0.5 ? 10 : 1000
				);

				Message<PageViewEvent> message = MessageBuilder.withPayload(pageViewEvent)
						.setHeader(KafkaHeaders.MESSAGE_KEY, pageViewEvent.getUserId().getBytes())
						.build();

				try{
					pageViewsOut.send(message);
					log.info("sent message " + message.toString());
				} catch (Exception e){
					log.error("Error sending messages " + e.getMessage());
				}
			};

			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1,1, TimeUnit.SECONDS);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaStreamsApplication.class, args);
	}

}
