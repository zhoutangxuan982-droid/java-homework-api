package com.example.javahomeworkapi.model;

public class User {

    private String username;
    private String passwordHash;   // 只存散列值，绝不存明文

    public User() { }              // Jackson 反序列化需要无参构造

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}