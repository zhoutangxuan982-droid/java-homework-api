package com.example.javahomeworkapi.service;

import com.example.javahomeworkapi.common.ApiException;
import com.example.javahomeworkapi.config.AppProperties;
import com.example.javahomeworkapi.model.User;
import com.example.javahomeworkapi.store.UserStore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserStore store;
    private final AppProperties props;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserStore store, AppProperties props) {
        this.store = store;
        this.props = props;
    }

    /** 注册：校验 → 查重 → 散列 → 落盘 */
    public void register(String username, String password) {
        String name = checkUsername(username);
        checkPassword(password);

        if (store.exists(name)) {
            throw new ApiException(400, "用户名已存在：" + name);
        }

        store.save(new User(name, encoder.encode(password)));
    }

    /** 登录：查用户 → 比对散列；成功返回 "Success" */
    public String login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new ApiException(400, "用户名和密码不能为空");
        }

        User user = store.find(username.trim())
                .orElseThrow(() -> new ApiException(400, "用户名或密码错误"));

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new ApiException(400, "用户名或密码错误");
        }
        return "Success";
    }

    /** 用户名规则全部来自 application.yml */
    private String checkUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ApiException(400, "用户名不能为空");
        }
        String name = username.trim();
        AppProperties.Username rule = props.getUsername();

        if (name.length() < rule.getMinLength() || name.length() > rule.getMaxLength()) {
            throw new ApiException(400,
                    "用户名长度必须在 " + rule.getMinLength() + "-" + rule.getMaxLength() + " 个字符之间");
        }
        if (!Pattern.matches(rule.getPattern(), name)) {
            throw new ApiException(400, "用户名只能包含字母、数字和下划线");
        }
        return name;
    }

    /** 密码最小长度来自 application.yml */
    private void checkPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ApiException(400, "密码不能为空");
        }
        int min = props.getPassword().getMinLength();
        if (password.length() < min) {
            throw new ApiException(400, "密码长度不能少于 " + min + " 位");
        }
    }
}