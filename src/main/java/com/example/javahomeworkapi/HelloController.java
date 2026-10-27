package com.example.javahomeworkapi;

import org.springframework.web.bind.annotation.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(@RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            return "Hello World";
        }
        return "Hello, " + name;
    }

    @PostMapping("/hello")
    public String helloPost(@RequestBody(required = false) Map<String, String> body) {
        String name = (body == null) ? null : body.get("name");
        if (name == null || name.isEmpty()) {
            return "Hello POST World";
        }
        return name + ", nice to meet you!";
    }

    @GetMapping("/hello/json")
    public Map<String, String> helloJson() {
        Map<String, String> result = new LinkedHashMap<>();
        result.put("get", "Hello World");
        result.put("post", "Hello POST World");
        return result;
    }
}