package me.yoryor.zabbix4j.service;

import java.util.List;

public interface ApplicationService {
    List<String> listApplicationIdsByHostId(String hostId);
}
