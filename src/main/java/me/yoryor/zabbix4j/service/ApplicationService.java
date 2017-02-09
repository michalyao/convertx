package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.annotation.MethodName;
import me.yoryor.zabbix4j.annotation.ParamEntry;
import me.yoryor.zabbix4j.annotation.ZabbixApi;

import java.util.List;

@ZabbixApi
public interface ApplicationService {
    @MethodName("application.get")
    List<Object> listApplicationIdsByHostId(@ParamEntry(paramKey = "hostids") List<String> hostId);
}
