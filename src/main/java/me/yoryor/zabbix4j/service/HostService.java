package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.annotation.RpcMethod;
import me.yoryor.zabbix4j.annotation.ParamEntry;
import me.yoryor.zabbix4j.annotation.ZabbixApi;
import me.yoryor.zabbix4j.transport.Filter;

import java.util.List;

@ZabbixApi
public interface HostService {
    @RpcMethod(name = "host.get", id = 1)
    List<Object> getHostIdByName(@ParamEntry(paramKey = "filter") Filter filter);
}
