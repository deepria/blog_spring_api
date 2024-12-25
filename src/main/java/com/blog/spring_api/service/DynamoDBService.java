package com.blog.spring_api.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

@Service
public class DynamoDBService {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "deepria";

    public DynamoDBService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public String getItem(String part, String index) {
        Map<String, AttributeValue> keyToGet = new HashMap<>();
        keyToGet.put("part_001", AttributeValue.builder().s(part).build()); // 파티션 키
        keyToGet.put("index_001", AttributeValue.builder().s(index).build());   // 정렬 키

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(keyToGet)
                .build();

        GetItemResponse response = dynamoDbClient.getItem(request);
        return response.item().get("Value").s();
    }

    public void putItem(String part, String index, String pk, String value) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("part_001", AttributeValue.builder().s(part).build()); // 파티션 키
        item.put("index_001", AttributeValue.builder().s(index).build());   // 정렬 키
        item.put("PrimaryKey", AttributeValue.builder().s(pk).build());
        item.put("Value", AttributeValue.builder().s(value).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        PutItemResponse response = dynamoDbClient.putItem(request);
        System.out.println("PutItem response: " + response);
    }
}