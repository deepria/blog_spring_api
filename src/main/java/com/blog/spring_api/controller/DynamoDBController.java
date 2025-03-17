package com.blog.spring_api.controller;

import com.blog.spring_api.entity.TestEntity;
import com.blog.spring_api.service.DynamoDBService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dynamodb")
public class DynamoDBController {

    private final DynamoDBService dynamoDBService;

    public DynamoDBController(DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*Attribute-------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

    @GetMapping("/item")
    public String getItem(@RequestParam String part, @RequestParam String index) {
        return dynamoDBService.getItem(part, index);
    }

    @PostMapping("/item")
    public String addItem(@RequestBody Map<String, String> payload) {
        dynamoDBService.putItem(payload.get("part"), payload.get("index"), payload.get("pk"), payload.get("value"));
        return "Success";
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*Entity----------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

    // 전체 데이터 조회
    @GetMapping("/list")
    public List<TestEntity> getAllEntities() {
        return dynamoDBService.getAllEntities();
    }

    // 특정 Partition Key 기준 조회
    @GetMapping("/{id}")
    public List<TestEntity> getEntitiesByPartitionKey(@PathVariable String id) {
        return dynamoDBService.getEntitiesByPartitionKey(id);
    }

    // 데이터 생성 또는 업데이트 (Upsert)
    @PostMapping
    public String saveEntity(@RequestBody TestEntity entity) {
        dynamoDBService.saveEntity(entity);
        return "Entity saved successfully.";
    }

    // 데이터 삭제
    @DeleteMapping("/{id}")
    public String deleteEntity(@PathVariable String id) {
        dynamoDBService.deleteEntity(id);
        return "Entity deleted successfully.";
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /*Tasks-----------------------------------------------------------------------------------------------------------*/
    /*----------------------------------------------------------------------------------------------------------------*/

}
