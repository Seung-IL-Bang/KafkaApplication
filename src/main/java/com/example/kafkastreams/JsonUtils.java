package com.example.kafkastreams;

import com.jayway.jsonpath.JsonPath;

public class JsonUtils {

    // Json 으로부터 ProductId 값만 get
    public static Long getProductId(String jsonString) {
        return JsonPath.parse(jsonString).read("$.productId", Long.class);
    }

    // Json 으로부터 Amount 값만 get
    public static Long getAmount(String jsonString) {
        return JsonPath.parse(jsonString).read("$.amount", Long.class);
    }

    // kafka streams 가 집계한 데이터를 output-topic 으로 발행할 json 을 만드는 메소드
    public static String getSendingJson(Long productId, Long amount
    ) {
        String jsonData = "{\"productId\":%d,\"windowedAmount\":%d,\"createdAt\":%d}";
        return String.format(jsonData, productId, amount, System.currentTimeMillis());
    }
}
