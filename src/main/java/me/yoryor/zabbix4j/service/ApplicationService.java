package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.annotation.RpcMethod;
import me.yoryor.zabbix4j.annotation.ParamEntry;
import me.yoryor.zabbix4j.annotation.ZabbixApi;

import java.util.List;

@ZabbixApi
public interface ApplicationService {
    @RpcMethod(name = "application.get", id = 1)
    List<Object> listApplicationIdsByHostId(@ParamEntry(paramKey = "hostids") List<String> hostId);
}
