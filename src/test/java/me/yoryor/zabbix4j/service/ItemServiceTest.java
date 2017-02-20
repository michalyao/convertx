package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.common.Config;
import me.yoryor.zabbix4j.transport.ServiceFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemServiceTest {
    private Config config;
    @Before
    public void init() {
        config = new Config();
        config.setPackagePath("me.yoryor.zabbix4j.service");
        config.setRpcAddress("http://192.168.116.131/zabbix/api_jsonrpc.php");
        config.setUser("Admin");
        config.setPassword("zabbix");
    }
    @Test
    public void listItemIdsByApplicationId() throws Exception {

        List<String> applicationids = new ArrayList<>();
        applicationids.add("459");
        List<Object> maps = ServiceFactory.getInstance(config)
                .getZabbixService(ItemService.class).listItemIdsByApplicationId(applicationids);
        // 获取id
        System.out.println(maps);
    }

    @Test
    public void listItemsByItemIds() {
        List<String> itemids = Collections.singletonList("23688");
        List<Object> items = ServiceFactory.getInstance(config)
                .getZabbixService(ItemService.class).listItemByItemIds(itemids);
        System.out.println(items);

    }
}