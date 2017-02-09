package me.yoryor.zabbix4j.transport;

import lombok.Data;

/**
 * 用于查询host时的过滤规则，是一系列键值对
 */
@Data
public class Filter {
    private String key;
    private Object value;
}
