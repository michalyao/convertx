package me.yoryor.zabbix4j.transport;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * 用于构建HTTP请求
 */
@Data
@Builder
public class RequestData {
    private String auth;
    private String method;
    private String jsonrpc;
    private Map<String, Object> params;
    private Integer id;
    
}
