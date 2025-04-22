package com.blog.spring_api.service;

import com.blog.spring_api.entity.TestEntity;
import com.blog.spring_api.repository.TestRepository;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamoDBService {

    private final DynamoDbClient dynamoDbClient;
    private final TestRepository testRepository;
    private final String tableName = "deepria";

    public DynamoDBService(DynamoDbClient dynamoDbClient, TestRepository testRepository) {
        this.dynamoDbClient = dynamoDbClient;
        this.testRepository = testRepository;
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
        try {
            return response.item().get("Value").s();
        } catch (Exception e) {
            return "Value not found";
        }
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
        try {
            dynamoDbClient.putItem(request);
        } catch (Exception ignored) {
        }
    }

    public void deleteItem(String part, String index) {
        Map<String, AttributeValue> keyToDelete = new HashMap<>();
        keyToDelete.put("part_001", AttributeValue.builder().s(part).build());
        keyToDelete.put("index_001", AttributeValue.builder().s(index).build());
        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(keyToDelete)
                .build();
        try {
            dynamoDbClient.deleteItem(request);
        } catch (Exception ignored) {
        }
    }


    public List<TestEntity> getAllEntities() {
        return testRepository.findAll();
    }

    public List<TestEntity> getEntitiesByPartitionKey(String id) {
        return testRepository.findByPartitionKey(id);
    }

    public void saveEntity(TestEntity entity) {
        testRepository.save(entity);
    }

    public void deleteEntity(String id) {
        testRepository.delete(id);
    }
}