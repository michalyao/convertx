package me.yoryor.zabbix4j.transport;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer code;
    private String message;
    private String data;
}
