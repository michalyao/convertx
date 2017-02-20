package me.yoryor.zabbix4j.transport;

import lombok.Data;

import java.util.List;

/**
 * 用于查询host时的过滤规则，是一系列键值对, 暂时支持host名称匹配
 */
@Data
public class Filter {
    private List<String> host;
}
