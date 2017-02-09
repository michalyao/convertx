package me.yoryor.zabbix4j.service;

import me.yoryor.zabbix4j.common.Config;
import me.yoryor.zabbix4j.transport.ServiceFactory;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemServiceTest {
    @Test
    public void listItemIdsByApplicationId() throws Exception {
        Config config = new Config();
        config.setPackagePath("me.yoryor.zabbix4j.service");
        config.setRpcAddress("http://192.168.116.131/zabbix/api_jsonrpc.php");
        config.setUser("Admin");
        config.setPassword("zabbix");
        ArrayList<String> applicationids = new ArrayList<>();
        applicationids.add("459");
        List<Object> maps = ServiceFactory.getInstance(config)
                .getZabbixService(ItemService.class).listItemIdsByApplicationId(applicationids);
        // 获取id
        System.out.println(maps);
    }

}