package com.blog.spring_api.repository;

import com.blog.spring_api.entity.TestEntity;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TestRepository {

    private final DynamoDbTable<TestEntity> table;

    public TestRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.table = dynamoDbEnhancedClient.table("testTable",
                software.amazon.awssdk.enhanced.dynamodb.TableSchema.fromBean(TestEntity.class));
    }

    // 테이블의 모든 항목 조회 (Scan)
    public List<TestEntity> findAll() {
        List<TestEntity> entities = new ArrayList<>();
        table.scan().items().forEach(entities::add);
        return entities;
    }

    // 파티션 키로 데이터 목록 조회
    public List<TestEntity> findByPartitionKey(String id) {
        List<TestEntity> results = new ArrayList<>();
        table.query(QueryConditional.keyEqualTo(k -> k.partitionValue(id))).items().forEach(results::add);
        return results;
    }

    // 데이터 삽입 또는 업데이트 (Upsert)
    public void save(TestEntity entity) {
        table.putItem(entity);
    }

    // 데이터 삭제
    public void delete(String id) {
        table.deleteItem(r -> r.key(k -> k.partitionValue(id)));
    }
}
