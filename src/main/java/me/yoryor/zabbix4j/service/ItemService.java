package me.yoryor.zabbix4j.service;

import java.util.List;

public interface ItemService {
    List<String> listItemIdsByApplicationId(String id);
}
