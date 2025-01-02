package com.blog.spring_api.entity;

import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@Setter
@DynamoDbBean
public class TestEntity {

    private String id;                  // Partition Key
    private String value;               // value

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    @DynamoDbAttribute("value")
    public String getValue() {
        return value;
    }
}