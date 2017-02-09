package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.annotation.MethodName;
import me.yoryor.zabbix4j.annotation.ParamEntry;
import me.yoryor.zabbix4j.annotation.ZabbixApi;
import me.yoryor.zabbix4j.transport.Filter;

@ZabbixApi
public interface HostService {
    @MethodName("host.get")
    String getHostIdByName(@ParamEntry(paramKey = "filter") Filter filter);
}
