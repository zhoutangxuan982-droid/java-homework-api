package com.example.javahomeworkapi.controller;

import com.example.javahomeworkapi.common.ApiException;
import com.example.javahomeworkapi.common.ApiResult;
import com.example.javahomeworkapi.dto.LoginRequest;
import com.example.javahomeworkapi.dto.RegisterRequest;
import com.example.javahomeworkapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {   // 构造器注入
        this.userService = userService;
    }

    /** POST /api/register  {"username":"zhangsan","password":"123456"} */
    @PostMapping("/register")
    public ApiResult<String> register(@Valid @RequestBody(required = false) RegisterRequest req) {
        if (req == null) {
            throw new ApiException(400, "请求体不能为空");
        }
        userService.register(req.getUsername(), req.getPassword());
        return ApiResult.ok("注册成功");
    }

    /** POST /api/login  {"username":"zhangsan","password":"123456"} */
    @PostMapping("/login")
    public ApiResult<String> login(@Valid @RequestBody(required = false) LoginRequest req) {
        if (req == null) {
            throw new ApiException(400, "请求体不能为空");
        }
        String result = userService.login(req.getUsername(), req.getPassword());
        return ApiResult.ok(result);
    }
}
