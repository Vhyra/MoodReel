package com.example.moviefetcher.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api.external")
public class ApiConfig {
    private String key;
    private String url;
    private String auth_token;

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getAuth() { return auth_token; }
    public void setAuth(String auth_token) { this.auth_token = auth_token; }
}