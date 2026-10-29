package com.example.javahomeworkapi.store;

import com.example.javahomeworkapi.config.AppProperties;
import com.example.javahomeworkapi.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserStore {

    private final AppProperties props;
    private final ObjectMapper mapper;
    private final Map<String, User> users = new LinkedHashMap<>();

    public UserStore(AppProperties props, ObjectMapper mapper) {   // ← 构造器注入
        this.props = props;
        this.mapper = mapper;
    }

    /** Bean 创建后执行一次：把文件里的用户读进内存 */
    @PostConstruct
    public void load() {
        Path file = filePath();
        if (!Files.exists(file)) {
            return;
        }
        try {
            String json = Files.readString(file);
            if (json.isBlank()) {
                return;
            }
            Map<String, User> loaded = mapper.readValue(
                    json, new TypeReference<LinkedHashMap<String, User>>() { });
            users.clear();
            users.putAll(loaded);
        } catch (IOException e) {
            throw new UncheckedIOException("读取用户文件失败：" + file, e);
        }
    }

    public synchronized Optional<User> find(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public synchronized boolean exists(String username) {
        return users.containsKey(username);
    }

    public synchronized void save(User user) {
        users.put(user.getUsername(), user);
        flush();
    }

    private void flush() {
        Path file = filePath();
        try {
            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(file,
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(users));
        } catch (IOException e) {
            throw new UncheckedIOException("写入用户文件失败：" + file, e);
        }
    }

    private Path filePath() {
        return Path.of(props.getStorage().getUserFile());   // ★ 路径来自 application.yml
    }
}