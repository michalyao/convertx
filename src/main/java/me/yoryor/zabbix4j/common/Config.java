package me.yoryor.zabbix4j.common;

import lombok.Data;

@Data
public class Config {
    private String packagePath;
    private String rpcAddress;
    // zabbix 用户名和密码
    private String user;
    private String password;
    private HttpConfig httpConfig = new HttpConfig();

    private static Config instance = new Config();

    public static Config getInstance() {
        return instance;
    }

    @Data
    public static class HttpConfig {
        private int connectTimeout = 5000;
        private int readTimeout = 30000;
        private int maxRequestRetry = 2;
        private int requestTimeout = 5000;
    }
}
