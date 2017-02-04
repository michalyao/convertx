package me.yoryor.zabbix4j.transport;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Request {
    private String jsonrpc;
    private Map<String, Object> params;
    private String method;
    private String auth;
    private Integer id;
}
