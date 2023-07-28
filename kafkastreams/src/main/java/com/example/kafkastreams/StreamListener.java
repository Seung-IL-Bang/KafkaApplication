package com.example.kafkastreams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class StreamListener {

    // StreamsBuilder 를 파라미터로 받는 KStream Bean 객체는 Kafka 에 의해 자동 실행된다.
    @Bean
    public KStream<String, String> kStream(StreamsBuilder builder) {
        final String inputTopic = "checkout.complete.v1";
        final String outputTopic = "checkout.productId.aggregated.v1";


        KStream<String, String> inputStream = builder.stream(inputTopic);
        inputStream // input-topic 으로 부터 데이터가 inputStream 으로 들어온다.
                // input-topic 으로 들어온 데이터(key-value) 중 value 에 해당하는 json 문자열에서 productId 와 amount 값만 취해 새로운 key-value Pair 로 만든다.
                .map((k, v) -> new KeyValue<>(JsonUtils.getProductId(v), JsonUtils.getAmount(v)))
                // Group by productId
                .groupByKey(Grouped.with(Serdes.Long(), Serdes.Long()))
                // Window 설정 - 1분간 그룹핑
                .windowedBy(TimeWindows.of(Duration.ofMinutes(1)))
                // Apply sum method
                .reduce(Long::sum)
                // map the window key; output-topic 으로 보낼 stream 생성
                .toStream((key, value) -> key.key())
                // outputTopic 에 보낼 Json String 으로 Generate
                .mapValues(JsonUtils::getSendingJson)
                // outputTopic 으로 보낼 key 값을 null 설정
                .selectKey((key, value) -> null)
                // 최종적으로 outputTopic 으로 메세지(null, jsonString) 전송 설정
                .to(outputTopic, Produced.with(null, Serdes.String()));

        return inputStream;
    }
}
