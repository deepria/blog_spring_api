package com.blog.spring_api.controller;

import com.blog.spring_api.service.DynamoDBService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/dynamodb")
public class DynamoDBController {

    private final DynamoDBService dynamoDBService;

    public DynamoDBController(DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }

    @GetMapping("/item")
    public String getItem(@RequestParam String part, @RequestParam String index) {
        return dynamoDBService.getItem(part, index);
    }

    @PostMapping("/item")
    public String addItem(@RequestBody Map<String, String> payload) {
        dynamoDBService.putItem(payload.get("part"), payload.get("index"), payload.get("pk"), payload.get("value"));
        return "Success";
    }
}
