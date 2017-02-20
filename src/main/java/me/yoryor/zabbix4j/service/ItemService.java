package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.annotation.RpcMethod;
import me.yoryor.zabbix4j.annotation.ParamEntry;
import me.yoryor.zabbix4j.annotation.ZabbixApi;

import java.util.List;

@ZabbixApi
public interface ItemService {
    @RpcMethod(name = "item.get", id = 1)
        // TODO: 2017/2/9 将返回类型包装成POJO类 
    List<Object> listItemIdsByApplicationId(@ParamEntry(paramKey = "applicationids") List<String> ids);

    @RpcMethod(name = "item.get", id = 2)
    List<Object> listItemByItemIds(@ParamEntry(paramKey = "itemids") List<String> ids);
}
