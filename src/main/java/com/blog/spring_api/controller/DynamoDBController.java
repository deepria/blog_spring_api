package com.blog.spring_api.controller;

import com.blog.spring_api.service.DynamoDBService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dynamodb")
public class DynamoDBController {

    private final DynamoDBService dynamoDBService;

    public DynamoDBController(DynamoDBService dynamoDBService) {
        this.dynamoDBService = dynamoDBService;
    }

    @PostMapping("/item")
    public String addItem(@RequestParam String part, @RequestParam String index, @RequestParam String pk, @RequestParam String value) {
        dynamoDBService.putItem(part, index, pk, value);
        return "Success";
    }

    @GetMapping("/item")
    public String getItem(@RequestParam String part, @RequestParam String index) {
        return dynamoDBService.getItem(part, index);
    }
}