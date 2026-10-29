package com.example.javahomeworkapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Storage storage = new Storage();
    private Password password = new Password();
    private Username username = new Username();

    public static class Storage {
        private String userFile;
        public String getUserFile() { return userFile; }
        public void setUserFile(String userFile) { this.userFile = userFile; }
    }

    public static class Password {
        private int minLength = 6;
        public int getMinLength() { return minLength; }
        public void setMinLength(int minLength) { this.minLength = minLength; }
    }

    public static class Username {
        private int minLength = 3;
        private int maxLength = 16;
        private String pattern = "^[a-zA-Z0-9_]+$";
        public int getMinLength() { return minLength; }
        public void setMinLength(int minLength) { this.minLength = minLength; }
        public int getMaxLength() { return maxLength; }
        public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
        public String getPattern() { return pattern; }
        public void setPattern(String pattern) { this.pattern = pattern; }
    }

    public Storage getStorage() { return storage; }
    public void setStorage(Storage storage) { this.storage = storage; }
    public Password getPassword() { return password; }
    public void setPassword(Password password) { this.password = password; }
    public Username getUsername() { return username; }
    public void setUsername(Username username) { this.username = username; }
}
